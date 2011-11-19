package com.pandawork.core.search.bridge.builtin;

import org.apache.lucene.document.Document;

import com.pandawork.core.search.bridge.TwoWayFieldBridge;
import com.pandawork.core.search.engine.LuceneOptions;

public abstract class NumericFieldBridge implements TwoWayFieldBridge {

	public String objectToString(Object object) {
		return object.toString();
	}

	public void set(String name, Object value, Document document,
			LuceneOptions luceneOptions) {
		if(value != null){
			luceneOptions.addNumericFieldToDocument(name, value, document);
		}
	}

}
