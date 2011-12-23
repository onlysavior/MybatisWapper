package com.pandawork.core.search.engine.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

import com.pandawork.core.search.cfg.ConfigBean;
import com.pandawork.core.search.cfg.ConfigBean.BuildContext;
import com.pandawork.core.search.engine.DocumentBuilderIndexedEntity;
import com.pandawork.core.search.store.DirectoryProvider;

public class IBQuery {
	
	private static final ConfigBean config = ConfigBean.getInstanse();
	
	private Query luceneQuery;
	
	private Sort sort;
	
	private List<Class<?>> targetedEntities;
	
	private Set<Class<?>> classesAndSubclasses;
	
	private int firstResult;
	
	private Integer maxResults;
	
	private Integer resultSize;
	
	
	public IBQuery luceneQuery (Query query){
		this.luceneQuery = query;
		return this;
	}
	
	public IBQuery sort(Sort luceneSort){
		this.sort = luceneSort;
		return this;
	}
	
	public IBQuery firstResult(int firstResult) {
		if ( firstResult < 0 ) {
			throw new IllegalArgumentException( "'first' pagination parameter less than 0" );
		}
		this.firstResult = firstResult;
		return this;
	}
	
	public IBQuery maxResults(Integer maxResults) {
		if ( maxResults != null && maxResults < 0 ) {
			throw new IllegalArgumentException( "'max' pagination parameter less than 0" );
		}
		this.maxResults = maxResults;
		return this;
	}
	
	public IBQuery targetedEntities(List<Class<?>> classes){
		//没有做 @Index 检测
		this.targetedEntities = classes != null ? new ArrayList<Class<?>>( classes ) : new ArrayList<Class<?>>(0);
		return this;
	}
	
	public List<EntityInfo> queryEntityInfos(){
		IndexSearcherWithPayload searcher = buildSearcher();
		if ( searcher == null ) {
			return Collections.emptyList();
		}
		QueryHits queryHits;
		try {
			queryHits = getQueryHits( searcher, calculateTopDocsRetrievalSize() );
			int first = getFirstResultIndex();
			int max = Math.max( first, queryHits.getTotalHits() );

			int size = max - first + 1 < 0 ? 0 : max - first + 1;
			List<EntityInfo> infos = new ArrayList<EntityInfo>( size );
			DocumentExtractor extractor = buildDocumentExtractor( searcher, queryHits, first, max );
			
			for ( int index = first; index < max; index++ ) {
				infos.add( extractor.extract( index ) );
			}
			return infos;
			
		} catch (IOException e) {
			throw new RuntimeException( "Unable to query Lucene index", e );
		}finally{
			closeSearcher(searcher);
		}
	}
	
	public Query getLuceneQuery() {
		return luceneQuery;
	}
	
	private IndexSearcherWithPayload buildSearcher(){
		Map<Class<?>, DocumentBuilderIndexedEntity<?>> documentBuildersIndexedEntities = BuildContext.class2IndexedEntites;
		this.classesAndSubclasses = documentBuildersIndexedEntities.keySet();
		
		final DirectoryProvider<?> directoryProviders = config.getDirectory();
		IndexSearcher is = new IndexSearcher(config.getReader().openReader(directoryProviders));
		is.setSimilarity( config.getDefaultSimilarity() );
		
		//默认值
		return new IndexSearcherWithPayload( is, false, false );
	}
	
	private Integer calculateTopDocsRetrievalSize() {
		if ( maxResults == null ) {
			return null;
		}
		else {
			long tmpMaxResult = (long) getFirstResultIndex() + maxResults;
			if ( tmpMaxResult >= Integer.MAX_VALUE ) {
				return Integer.MAX_VALUE - 1;
			}
			if ( tmpMaxResult == 0 ) {
				return 1; 
			}
			else {
				return (int) tmpMaxResult;
			}
		}
	}
	
	private int getFirstResultIndex() {
		return firstResult;
	}
	
	private QueryHits getQueryHits(IndexSearcherWithPayload searcher, Integer n) throws IOException {
		QueryHits queryHits;

		if ( n == null ) { // try to make sure that we get the right amount of top docs
			queryHits = new QueryHits(
					searcher,
					luceneQuery,
					sort
			);
		}
		else if ( 0 == n) {
			queryHits = new QueryHits(
					searcher,
					luceneQuery,
					null,
					0
			);
		}
		else {
			queryHits = new QueryHits(
					searcher,
					luceneQuery,
					sort,
					n
			);
		}
		resultSize = queryHits.getTotalHits();

		return queryHits;
	}
	
	private DocumentExtractor buildDocumentExtractor(IndexSearcherWithPayload searcher, QueryHits queryHits, int first, int max) {
		return new DocumentExtractor(
				queryHits,
				searcher,
				luceneQuery,
				first,
				max,
				classesAndSubclasses
		);
	}
	
	private void closeSearcher(IndexSearcherWithPayload searcherWithPayload) {
		if ( searcherWithPayload == null ) {
			return;
		}
		searcherWithPayload.closeSearcher(config);
	}
}
