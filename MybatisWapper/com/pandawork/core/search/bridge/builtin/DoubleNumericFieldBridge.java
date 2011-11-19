package com.pandawork.core.search.bridge.builtin;

import org.apache.lucene.document.Document;

public class DoubleNumericFieldBridge extends NumericFieldBridge {

	public Object get(String name, Document document) {
		return Double.valueOf(document.getFieldable(name).stringValue());
	}

}
