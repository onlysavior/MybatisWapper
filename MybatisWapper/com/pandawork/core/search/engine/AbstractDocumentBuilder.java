package com.pandawork.core.search.engine;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.Similarity;

import com.pandawork.core.search.annotations.DocumentId;
import com.pandawork.core.search.annotations.IndexEum;
import com.pandawork.core.search.annotations.NumericField;
import com.pandawork.core.search.annotations.StoreEum;
import com.pandawork.core.search.bridge.BridgeFactory;
import com.pandawork.core.search.bridge.FieldBridge;
import com.pandawork.core.search.cfg.ConfigBean;
import com.pandawork.core.search.utils.PassThroughAnalyzer;
import com.pandawork.core.search.utils.ScopedAnalyzer;
import com.pandawork.core.util.ClassLoaderHelper;

/**
 * 不写注释 反正你也看不懂
 * 
 * @author Administrator
 * 
 * @param <T>
 */
public abstract class AbstractDocumentBuilder<T> {

	public static final String CLASS_FIELDNAME = "IBATIS_CLASS";

	//private Map<String, Analyzer> scopedAnalyzers = new HashMap<String, Analyzer>();
	private ScopedAnalyzer scopedAnalyzers = new ScopedAnalyzer();
	
	private Analyzer passThroughAnalyzer = new PassThroughAnalyzer();

	private final PropertyMeta meta = new PropertyMeta();

	private Similarity similarity;

	private Class<?> clazz;

	private String className;

	private String tableName;
	
	protected Member idGetter;

	public AbstractDocumentBuilder(Class<?> beanClass, ConfigBean configBean,
			Similarity similarity) {
		this.clazz = beanClass;
		this.className = beanClass.getName();
		this.similarity = similarity;
		this.tableName = getTableNameFormAnnotion(clazz);
		meta.analyzer = configBean.getDefaultAnalyzer();
		scopedAnalyzers.setGlobalAnalyzer(meta.analyzer);

		initializeClass(this.clazz, meta, configBean);

		if (this.similarity == null) {
			this.similarity = configBean.getDefaultSimilarity();
		}
	}

	abstract void documentBuilderSpecificChecks(Member member,
			PropertyMeta propertiesMetadata, ConfigBean context);
	
	private String getTableNameFormAnnotion(Class<?> clazz) {
		Table ann = clazz.getAnnotation(Table.class);
		if (ann == null) {
			throw new IllegalArgumentException("实体 没有 @Table注解");
		}
		return ann.name();
	}

	private void initializeClass(Class<?> beanClass, PropertyMeta meta,
			ConfigBean cfg) {
		initializeClassLevelAnnotations(beanClass, meta, cfg);
		java.lang.reflect.Field[] fields = beanClass.getDeclaredFields();
		for (java.lang.reflect.Field field : fields) {
			initializeMemberLevelAnnotations(field, meta, cfg);
			documentBuilderSpecificChecks(field,meta,cfg);
		}
	}

	private void initializeClassLevelAnnotations(Class<?> beanClass,
			PropertyMeta meta, ConfigBean cfg) {
		Analyzer analyzer = getAnalyzer(beanClass, cfg);
		meta.analyzer = analyzer;
	}

	private void initializeMemberLevelAnnotations(
			java.lang.reflect.Field field, PropertyMeta meta, ConfigBean cfg) {
		checkForField(field, meta, cfg);
	}

	private Analyzer getAnalyzer(Class<?> clazz, ConfigBean cfg) {
		com.pandawork.core.search.annotations.Analyzer analyzerDef = clazz
				.getAnnotation(com.pandawork.core.search.annotations.Analyzer.class);
		if (analyzerDef == null) {
			return cfg.getDefaultAnalyzer();
		} else {
			Class<?> clazzDef = analyzerDef.impl();
			try {
				return (Analyzer) clazzDef.newInstance();
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
		return cfg.getDefaultAnalyzer();
	}

	private void checkForField(java.lang.reflect.Field field,
			PropertyMeta meta, ConfigBean cfg) {
		com.pandawork.core.search.annotations.Field fieldAnn = field
				.getAnnotation(com.pandawork.core.search.annotations.Field.class);
		DocumentId documentId = field.getAnnotation(DocumentId.class);
		NumericField numericFieldAnn = field.getAnnotation( NumericField.class );
		if (fieldAnn != null) {
			bindFieldAnnotation(field, meta, cfg, documentId, fieldAnn,numericFieldAnn);
		}
//		if ((fieldAnn == null && documentId == null)) {
//			throw new IllegalArgumentException("没有实体域的注解！");
//		}
	}

 	private void bindFieldAnnotation(java.lang.reflect.Field field,
			PropertyMeta meta, ConfigBean cfg, DocumentId documentId,
			com.pandawork.core.search.annotations.Field fieldAnn,
			NumericField numberAnn) {
		
		field.setAccessible(true);
		meta.fieldGetter.add(field);
		String fieldName = StringUtils.isEmpty(fieldAnn.name()) ? field.getName() : fieldAnn.name();
		meta.fieldNames.add(fieldName);
		meta.fieldIndex.add(getIndex(fieldAnn.index()));
		meta.fieldStore.add(fieldAnn.store());
		meta.precisionSteps.add( getPrecisionStep( numberAnn ) );
		if(documentId != null){
			this.idGetter = field;
		}
		
		FieldBridge fieldBridge = BridgeFactory.guessType( fieldAnn, numberAnn, field );
		meta.fieldBridges.add( fieldBridge );
		Analyzer analyzer = getAnalyzer( fieldAnn.analyzer(), cfg );
		addToScopedAnalyzer( fieldName, analyzer, fieldAnn.index() );
	}
	
	protected void addToScopedAnalyzer(String fieldName, Analyzer analyzer, IndexEum index) {
		if ( index == IndexEum.TOKENIZED ) {
			if ( analyzer != null ) {
				this.scopedAnalyzers.addScopedAnalyzer( fieldName, analyzer );
			}
		}
		else {
			this.scopedAnalyzers.addScopedAnalyzer( fieldName, passThroughAnalyzer );
		}
	}
	
	private Analyzer getAnalyzer(
			com.pandawork.core.search.annotations.Analyzer analyzer,
			ConfigBean cfg) {
		Class<?> analyzerClass = analyzer == null ? void.class : analyzer.impl();
		if(analyzerClass != void.class){
			return ClassLoaderHelper.analyzerInstanceFromClass( analyzerClass );
		}
		return new PaodingAnalyzer();
	}

	protected Field.Index getIndex(IndexEum index) {
		switch ( index ) {
			case NO:
				return Field.Index.NO;
			case NO_NORMS:
				return Field.Index.NOT_ANALYZED_NO_NORMS;
			case TOKENIZED:
				return Field.Index.ANALYZED;
			case UN_TOKENIZED:
				return Field.Index.NOT_ANALYZED;
			default:
				throw new RuntimeException( "Unexpected Index: " + index );
		}
	}
	
	protected Integer getPrecisionStep(NumericField numericFieldAnn) {
		return numericFieldAnn == null ? 4 : numericFieldAnn.precisionStep();
	}
	
	public PropertyMeta getMetadata() {
		return meta;
	}
	
	public ScopedAnalyzer getScopedAnalyzers() {
		return scopedAnalyzers;
	}


	static class PropertyMeta {
		public Analyzer analyzer;
		public final List<String> fieldNames = new ArrayList<String>();
		public final List<Member> fieldGetter = new ArrayList<Member>();
		public final List<FieldBridge> fieldBridges = new ArrayList<FieldBridge>();
		public final List<StoreEum> fieldStore = new ArrayList<StoreEum>();
		public final List<Field.Index> fieldIndex = new ArrayList<Field.Index>();
		public final List<Integer> precisionSteps = new ArrayList<Integer>();
		public final List<String> classNames = new ArrayList<String>();
		public final List<StoreEum> classStores = new ArrayList<StoreEum>();
		public final List<Field.Index> classIndexes = new ArrayList<Field.Index>();


		protected LuceneOptions getClassLuceneOptions(int i) {
			if(classIndexes.size() > 0 && classStores.size() > 0){
				return new LuceneOptions(classIndexes.get(i), classStores.get(i));
			}
			return null;
		}

		protected LuceneOptions getFieldLuceneOptions(int i, Object value) {
			LuceneOptions options;
			options = new LuceneOptions(fieldIndex.get(i), fieldStore.get(i));
			return options;
		}
	}
}
