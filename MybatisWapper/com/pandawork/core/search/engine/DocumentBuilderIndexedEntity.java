package com.pandawork.core.search.engine;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

import com.pandawork.core.search.annotations.DocumentId;
import com.pandawork.core.search.annotations.NumericField;
import com.pandawork.core.search.annotations.StoreEum;
import com.pandawork.core.search.bridge.BridgeFactory;
import com.pandawork.core.search.bridge.FieldBridge;
import com.pandawork.core.search.bridge.TwoWayFieldBridge;
import com.pandawork.core.search.bridge.TwoWayString2FieldBridgeAdaptor;
import com.pandawork.core.search.cfg.ConfigBean;
import com.pandawork.core.search.store.DirectoryProvider;
import com.pandawork.core.util.ReflectionHelper;

/**
 * ...^_^...
 * 
 * @author Administrator
 * 
 * @param <T>
 */
public class DocumentBuilderIndexedEntity<T> extends AbstractDocumentBuilder<T> {

	private DirectoryProvider<?> directoryProviders;
	private String identifierName;
	private TwoWayFieldBridge idBridge;

	public DocumentBuilderIndexedEntity(Class<?> beanClass,
			ConfigBean configBean) {
		super(beanClass, configBean, configBean.getDefaultSimilarity());
		this.directoryProviders = configBean.getDirectory();
	}

	protected void documentBuilderSpecificChecks(Member member,
			PropertyMeta propertiesMetadata, ConfigBean context) {
		checkDocumentId(member, propertiesMetadata, context);
	}

	protected void checkDocumentId(Member member, PropertyMeta meta,
			ConfigBean context) {
		Annotation idAnnotation = getIdAnnotation(member, context);

		NumericField numericFieldAnn = ((AnnotatedElement) member).getAnnotation(NumericField.class);
		if (idAnnotation != null) {
			String attributeName = getIdAttributeName(member, idAnnotation);
			identifierName = attributeName;
			FieldBridge fieldBridge = BridgeFactory.guessType(null,
					numericFieldAnn, member);
			if (fieldBridge instanceof TwoWayFieldBridge) {
				idBridge = (TwoWayFieldBridge) fieldBridge;
			} else {
				throw new RuntimeException("@DocumentId 的类型不可转换");
			}
			ReflectionHelper.setAccessible(member);
			idGetter = member;
		}

	}

	public Serializable getId(Object entity) {
		if (entity == null || idGetter == null) {
			throw new IllegalArgumentException("得不到Id");
		}

		if (idGetter instanceof Field) {
			Field idField = (Field) idGetter;
			idField.setAccessible(true);
			try {
				return (Serializable) idField.get(entity);
			} catch (Exception e) {
				return null;
			}
		} else if (idGetter instanceof Method) {
			Method idMethod = (Method) idGetter;
			idMethod.setAccessible(true);
			try {
				return (Serializable) idMethod.invoke(entity, null);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	public <T> Document getDocument(T entity, Serializable id,
			Map<String, String> fieldToAnalyzerMap) {
		// if ( fieldToAnalyzerMap == null ) {
		// throw new IllegalArgumentException(
		// "fieldToAnalyzerMap cannot be null" );
		// }

		Document doc = new Document();
		final Class<?> entityType = entity.getClass();

		org.apache.lucene.document.Field classField = new org.apache.lucene.document.Field(
				CLASS_FIELDNAME, entityType.getName(),
				org.apache.lucene.document.Field.Store.YES,
				org.apache.lucene.document.Field.Index.NOT_ANALYZED_NO_NORMS,
				org.apache.lucene.document.Field.TermVector.NO);
		doc.add(classField);

		// LuceneOptions luceneOptions = new LuceneOptions(
		// org.apache.lucene.document.Field.Index.NOT_ANALYZED_NO_NORMS,
		// StoreEum.YES
		// );

		Set<String> processedFieldNames = new HashSet<String>();
		buildDocumentFields(entity, doc, getMetadata(), fieldToAnalyzerMap,
				processedFieldNames);
		return doc;
	}

	public Term getTerm(Serializable id) {
		return new Term(identifierName, objectToString(idBridge,
				identifierName, id));
	}

	public String getIdentifierName() {
		return this.identifierName;
	}

	private void buildDocumentFields(Object instance, Document doc,
			PropertyMeta propertiesMetadata,
			Map<String, String> fieldToAnalyzerMap,
			Set<String> processedFieldNames) {
		if (instance == null) {
			return;
		}

		// indexed fields
		for (int i = 0; i < propertiesMetadata.fieldNames.size(); i++) {
			Member member = propertiesMetadata.fieldGetter.get(i);
			Object value = com.pandawork.core.util.ReflectionHelper
					.getMemberValue(instance, member);

			final FieldBridge fieldBridge = propertiesMetadata.fieldBridges
					.get(i);
			final String fieldName = propertiesMetadata.fieldNames.get(i);

			// 采用Bridge来获得准确的String化的值
			Object memberValue = ReflectionHelper.getMemberValue(instance,
					member);
			String valueString;
			if (fieldBridge != null) {
				valueString = ((TwoWayString2FieldBridgeAdaptor) fieldBridge)
						.objectToString(memberValue);
			} else {
				// Field 本身为String类型时
				valueString = memberValue.toString();
			}
			// TODO 这里可能有问题 getFieldLuceneOptions 与 getClassLuceneOptions
			LuceneOptions opts = propertiesMetadata.getFieldLuceneOptions(i,
					valueString);

			opts.addFieldToDocument(fieldName, valueString, doc);
		}
	}

	private Annotation getIdAnnotation(Member member, ConfigBean context) {
		Annotation idAnnotation = null;
		DocumentId documentIdAnn = ((AnnotatedElement) member)
				.getAnnotation(DocumentId.class);

		if (documentIdAnn != null) {
			idAnnotation = documentIdAnn;
		}

		return idAnnotation;
	}

	private String getIdAttributeName(Member member, Annotation idAnnotation) {
		String name = null;
		try {
			Method m = idAnnotation.getClass().getMethod("name");
			name = (String) m.invoke(idAnnotation);
		} catch (Exception e) {
		}
		return ReflectionHelper.getAttributeName(member, name);
	}

	private String objectToString(TwoWayFieldBridge bridge, String fieldName,
			Object value) {
		// TODO 可能有问题
		return bridge.objectToString(value);
	}

	public DirectoryProvider<?> getDirectoryProviders() {
		return directoryProviders;
	}
}
