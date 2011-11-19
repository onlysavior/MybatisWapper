package com.pandawork.core.search.engine.impl;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.search.Weight;

//TODO 没有时间限制 实现的简单
public class QueryHits {

	private static final int DEFAULT_TOP_DOC_RETRIEVAL_SIZE = 100;
	private final org.apache.lucene.search.Query preparedQuery;
	private final IndexSearcherWithPayload searcher;
	private final Sort sort;
	
	private int totalHits;
	private TopDocs topDocs;
	
	public QueryHits(
			IndexSearcherWithPayload searcher,
			org.apache.lucene.search.Query preparedQuery,
			Sort sort
			) throws IOException{
		this.preparedQuery = preparedQuery;
		this.searcher = searcher;
		this.sort = sort;
		
		updateTopDocs( DEFAULT_TOP_DOC_RETRIEVAL_SIZE );
	}
	
	public QueryHits(IndexSearcherWithPayload searcher,
			org.apache.lucene.search.Query preparedQuery,
			Sort sort, Integer n) throws IOException {
		this.preparedQuery = preparedQuery;
		this.searcher = searcher;
		this.sort = sort;
		updateTopDocs(n);
	}
	
	public Document doc(int index) throws IOException {
		return searcher.getSearcher().doc( docId( index ) );
	}

	public Document doc(int index, FieldSelector selector) throws IOException {
		return searcher.getSearcher().doc( docId( index ), selector );
	}
	
	public int docId(int index) throws IOException {
		return scoreDoc( index ).doc;
	}

	public float score(int index) throws IOException {
		return scoreDoc( index ).score;
	}
	
	public ScoreDoc scoreDoc(int index) throws IOException {
		if ( index >= totalHits ) {
			throw new RuntimeException( "Not a valid ScoreDoc index: " + index );
		}

		if ( index >= topDocs.scoreDocs.length ) {
			updateTopDocs( 2 * index );
		}
		return topDocs.scoreDocs[index];
	}
	
	public int getTotalHits() {
		return totalHits;
	}

	public TopDocs getTopDocs() {
		return topDocs;
	}
	
	private void updateTopDocs(int n) throws IOException{
		int totalMaxDocs = searcher.getSearcher().maxDoc();
		final int maxDocs = Math.min( n, totalMaxDocs );
		final Weight weight = preparedQuery.weight( searcher.getSearcher() );

		final TopDocsCollector<?> topDocCollector;
		final TotalHitCountCollector hitCountCollector;
		if ( maxDocs != 0 ) {
			topDocCollector = createTopDocCollector( maxDocs, weight );
			hitCountCollector = null;
		}
		else {
			topDocCollector = null;
			hitCountCollector = new TotalHitCountCollector();
		}
		
		if ( maxDocs != 0 ) {
			this.topDocs = topDocCollector.topDocs();
			this.totalHits = topDocs.totalHits;
		}
		else {
			this.topDocs = null;
			this.totalHits = hitCountCollector.getTotalHits();
		}
	}
	
	private TopDocsCollector<?> createTopDocCollector(int maxDocs, Weight weight) throws IOException {
		TopDocsCollector<?> topCollector;
		if ( sort == null ) {
			topCollector = TopScoreDocCollector.create( maxDocs, !weight.scoresDocsOutOfOrder() );
		}
		else {
			boolean fillFields = true;
			topCollector = TopFieldCollector.create(
					sort,
					maxDocs,
					fillFields,
					searcher.isFieldSortDoTrackScores(),
					searcher.isFieldSortDoMaxScore(),
					!weight.scoresDocsOutOfOrder()
			);
		}
		return topCollector;
	}
}
