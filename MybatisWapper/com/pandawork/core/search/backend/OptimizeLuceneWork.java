package com.pandawork.core.search.backend;

import java.io.Serializable;

import org.apache.lucene.index.IndexWriter;

public class OptimizeLuceneWork extends LuceneWork implements Serializable {
	
	private static final long serialVersionUID = -6010415061579173079L;

	public OptimizeLuceneWork(Class<?> entity) {
		super( null, null, entity );
	}

	public void performWork(IndexWriter indexWriter) {
		final Class<?> entityClazz = getEntityClass();
		
		try {
			indexWriter.optimize();
		} catch (Exception e){
			throw new RuntimeException("不能优化"+entityClazz);
		}finally{
			closeWriter(indexWriter);
		}
	}
}
