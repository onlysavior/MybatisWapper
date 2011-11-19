package com.pandawork.core.search.engine.impl;

import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;

import com.pandawork.core.search.cfg.ConfigBean;
import com.pandawork.core.search.reader.NotSharedReaderProvider;
import com.pandawork.core.search.reader.ReaderProviderHelper;

public class IndexSearcherWithPayload {
	
	private final IndexSearcher searcher;
	private boolean fieldSortDoTrackScores;
	private boolean fieldSortDoMaxScore;
	
	public IndexSearcherWithPayload(IndexSearcher searcher, boolean fieldSortDoTrackScores, boolean fieldSortDoMaxScore) {
		this.searcher = searcher;
		this.fieldSortDoTrackScores = fieldSortDoTrackScores;
		this.fieldSortDoMaxScore = fieldSortDoMaxScore;
		this.searcher.setDefaultFieldSortScoring( fieldSortDoTrackScores, fieldSortDoMaxScore );
	}
	
	public void closeSearcher(ConfigBean config) {
		Set<IndexReader> indexReaders = ReaderProviderHelper.getIndexReaders( getSearcher() );
		NotSharedReaderProvider readerProvider = config.getReader();
		for ( IndexReader indexReader : indexReaders ) {
			try {
				readerProvider.closeReader( indexReader );
			}
			catch (RuntimeException e) {
			}
		}
	}

	public IndexSearcher getSearcher() {
		return searcher;
	}

	public boolean isFieldSortDoTrackScores() {
		return fieldSortDoTrackScores;
	}

	public boolean isFieldSortDoMaxScore() {
		return fieldSortDoMaxScore;
	}
	
	
}
