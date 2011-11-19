package com.pandawork.core.search.bridge.builtin;

import org.apache.lucene.document.Document;

public class LongNumericFieldBridge extends NumericFieldBridge {

	public Object get(String name, Document document) {
		return Long.valueOf(document.getFieldable(name).stringValue());
	}

}
