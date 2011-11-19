package com.pandawork.core.search.reader;

import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;

public abstract class ReaderProviderHelper {

	public static Set<IndexReader> getIndexReaders(IndexSearcher searchable) {
		Set<IndexReader> readers = new HashSet<IndexReader>();
		getIndexReadersInternal( readers, searchable );
		return readers;
	}
	
	private static void getIndexReadersInternal(Set<IndexReader> readers, Object obj) {
		if ( obj instanceof IndexSearcher ) {
			getIndexReadersInternal( readers, ( (IndexSearcher) obj ).getIndexReader() );
		}
		else if ( obj instanceof IndexReader ) {
			readers.add( (IndexReader) obj );
		}
	}
}
