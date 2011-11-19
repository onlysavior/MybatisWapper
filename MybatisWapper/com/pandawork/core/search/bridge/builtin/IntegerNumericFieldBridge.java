package com.pandawork.core.search.bridge.builtin;

import org.apache.lucene.document.Document;

public class IntegerNumericFieldBridge extends NumericFieldBridge {

	public Object get(String name, Document document) {
		return Integer.valueOf(document.getFieldable(name).stringValue());
	}

}
