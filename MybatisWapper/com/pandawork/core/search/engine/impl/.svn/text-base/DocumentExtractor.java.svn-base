package com.pandawork.core.search.engine.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;

import com.pandawork.core.search.cfg.ConfigBean;
import com.pandawork.core.search.engine.DocumentBuilderHelper;
import com.pandawork.core.search.engine.DocumentBuilderIndexedEntity;

public class DocumentExtractor {

	private static final ConfigBean config = ConfigBean.getInstanse();
	private final QueryHits queryHits;
	private final IndexSearcherWithPayload searcher;
	private final Map<String, Class<?>> targetedClasses;
	private int firstIndex;
	private int maxIndex;
	private Object query;
	
	private final Class<?> singleClassIfPossible;

	public DocumentExtractor(QueryHits hits, IndexSearcherWithPayload searcher,
			Object query, int firstIndex, int maxIndex,
			Set<Class<?>> classesAndSubclasses) {
		this.queryHits = hits;
		this.searcher = searcher;
		
		this.targetedClasses = new HashMap<String, Class<?>>( classesAndSubclasses.size() );
		for ( Class<?> clazz : classesAndSubclasses ) {
			//useful to reload classes from index without using reflection
			targetedClasses.put( clazz.getName(), clazz );
		}
		
		if ( classesAndSubclasses.size() == 1 ) {
			singleClassIfPossible = classesAndSubclasses.iterator().next();
		}
		else {
			singleClassIfPossible = null;
		}
		this.firstIndex = firstIndex;
		this.maxIndex = maxIndex;
		this.query = query;
	}
	
	public EntityInfo extract(int scoreDocIndex) throws IOException{
		int docId = queryHits.docId( scoreDocIndex );
		Document document = extractDocument( scoreDocIndex );

		EntityInfo entityInfo = extractEntityInfo( docId, document, scoreDocIndex );
		return entityInfo;
	}
	
	public int getFirstIndex() {
		return firstIndex;
	}

	public int getMaxIndex() {
		return maxIndex;
	}

	public void close() {
		searcher.closeSearcher(config );
	}
	
	
	private EntityInfo extractEntityInfo(int docId, Document document, int scoreDocIndex) throws IOException {
		Class<?> clazz = extractClass( docId, document, scoreDocIndex );
		String idName = DocumentBuilderHelper.getDocumentIdName( clazz );
		Serializable id = extractId( docId, document, clazz );
		return new EntityInfo( clazz, idName, id );
	}
	
	private Class<?> extractClass(int docId, Document document, int scoreDocIndex) throws IOException {
		if ( singleClassIfPossible != null ) {
			return singleClassIfPossible;
		}
		String className;
		className = document.get( DocumentBuilderIndexedEntity.CLASS_FIELDNAME );
		Class<?> clazz = targetedClasses.get( className );
		if ( clazz != null ) {
			return clazz;
		}
		else {
			return DocumentBuilderHelper.getDocumentClass( className );
		}
	}
	
	private Serializable extractId(int docId, Document document, Class<?> clazz) {
		Serializable id = null;
		id = DocumentBuilderHelper.getDocumentId( clazz, document );
		return id;
	}
	
	private Document extractDocument(int index) throws IOException {
			return queryHits.doc( index );
	}
}
