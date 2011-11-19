package com.pandawork.core.search.bridge.builtin;

import org.apache.lucene.document.Document;

public class FloatNumericFieldBridge extends NumericFieldBridge {

	public Object get(String name, Document document) {
		return Float.valueOf(document.getFieldable(name).stringValue());
	}

}
