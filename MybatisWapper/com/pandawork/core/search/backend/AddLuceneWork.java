package com.pandawork.core.search.backend;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import com.pandawork.core.search.cfg.ConfigBean.BuildContext;
import com.pandawork.core.search.engine.DocumentBuilderIndexedEntity;
import com.pandawork.core.search.utils.ScopedAnalyzer;

public class AddLuceneWork extends LuceneWork implements Serializable {

	private static final long serialVersionUID = -3669118671483648925L;

	private final Map<String, String> fieldToAnalyzerMap;

	public AddLuceneWork(Serializable id, String idInString, Class<?> entity,
			Document document) {
		this(id, idInString, entity, document, false);
	}

	public AddLuceneWork(Serializable id, String idInString, Class<?> entity,
			Document document, boolean batch) {
		this(id, idInString, entity, document, null, batch);
	}

	public AddLuceneWork(Serializable id, String idInString, Class<?> entity,
			Document document, Map<String, String> fieldToAnalyzerMap) {
		this(id, idInString, entity, document, fieldToAnalyzerMap, false);
	}

	public AddLuceneWork(Serializable id, String idInString, Class<?> entity,
			Document document, Map<String, String> fieldToAnalyzerMap,
			boolean batch) {
		super(id, idInString, entity, document, batch);
		this.fieldToAnalyzerMap = fieldToAnalyzerMap;
	}

	public Map<String, String> getFieldToAnalyzerMap() {
		return fieldToAnalyzerMap;
	}

	public void performWork(IndexWriter indexWriter) {
		final Class<?> entityClazz = this.getEntityClass();
		DocumentBuilderIndexedEntity<?> builder = BuildContext.class2IndexedEntites.get(entityClazz);
		
		ScopedAnalyzer analyzer = builder.getScopedAnalyzers();
		try{
			indexWriter.addDocument(getDocument(), analyzer);
		}catch(IOException e){
			throw new RuntimeException("不能加入索引");
		}
	}
}
