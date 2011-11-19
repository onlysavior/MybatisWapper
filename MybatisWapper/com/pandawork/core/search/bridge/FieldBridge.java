package com.pandawork.core.search.bridge;

import org.apache.lucene.document.Document;

import com.pandawork.core.search.engine.LuceneOptions;

public interface FieldBridge {
	void set(String name, Object value, Document document, LuceneOptions luceneOptions);
}
