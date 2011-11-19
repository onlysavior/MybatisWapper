package com.pandawork.core.search.bridge;

import org.apache.lucene.document.Document;

import com.pandawork.core.search.engine.LuceneOptions;

public class String2FieldBridgeAdaptor implements StringBridge, FieldBridge {

	private final StringBridge stringBridge;

	public String2FieldBridgeAdaptor(StringBridge stringBridge) {
		this.stringBridge = stringBridge;
	}

	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		String indexedString = stringBridge.objectToString( value );
		luceneOptions.addFieldToDocument( name, indexedString, document );
	}

	public String objectToString(Object object) {
		return stringBridge.objectToString( object );
	}

}
