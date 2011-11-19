package com.pandawork.core.search.engine;

import java.io.Serializable;

import org.apache.lucene.document.Document;

import com.pandawork.core.search.cfg.ConfigBean.BuildContext;
import com.pandawork.core.util.ReflectionHelper;


public final class DocumentBuilderHelper {
	private DocumentBuilderHelper(){}
	
	public static Class<?> getDocumentClass(String className) {
		try {
			return ReflectionHelper.classForName( className );
		}
		catch ( ClassNotFoundException e ) {
			throw new RuntimeException( "Unable to load indexed class: " + className, e );
		}
	}
	
	public static Serializable getDocumentId(Class<?> clazz, Document document) {
		DocumentBuilderIndexedEntity<?> builderIndexedEntity = BuildContext.class2IndexedEntites.get(clazz);
		if ( builderIndexedEntity == null ) {
			throw new RuntimeException( "No Lucene configuration set up for: " + clazz );
		}
		
		//TODO 应该用Bridge
		String fieldName = builderIndexedEntity.getIdentifierName();
		return document.get(fieldName);
	}
	
	public static String getDocumentIdName(Class<?> clazz){
		DocumentBuilderIndexedEntity<?> builderIndexedEntity = BuildContext.class2IndexedEntites.get(clazz);
		if ( builderIndexedEntity == null ) {
			throw new RuntimeException( "No Lucene configuration set up for: " + clazz );
		}
		return builderIndexedEntity.getIdentifierName();
	}
}
