package com.pandawork.core.search.backend;

import java.io.Serializable;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import com.pandawork.core.search.cfg.ConfigBean.BuildContext;
import com.pandawork.core.search.engine.DocumentBuilderIndexedEntity;

public class DeleteLuceneWork extends LuceneWork implements Serializable {
	
	private static final long serialVersionUID = 6636635663627652413L;

	public DeleteLuceneWork(Serializable id, String idInString, Class<?> entity) {
		super( id, idInString, entity );
	}

	public void performWork(IndexWriter indexWriter) {
		final Class<?> entityClazz = getEntityClass();
		final Serializable id = getId();
		
		DocumentBuilderIndexedEntity<?> builder = BuildContext.class2IndexedEntites.get(entityClazz);
		
		BooleanQuery entityDeletionQuery = new BooleanQuery();
		Query idQueryTerm;
		//TODO 主键为Bridge的问题
		idQueryTerm = new TermQuery( builder.getTerm( id ) );
		entityDeletionQuery.add( idQueryTerm, BooleanClause.Occur.MUST );
		
		Term classNameQueryTerm =  new Term( DocumentBuilderIndexedEntity.CLASS_FIELDNAME, entityClazz.getName() );
		TermQuery classNameQuery = new TermQuery( classNameQueryTerm );
		entityDeletionQuery.add( classNameQuery, BooleanClause.Occur.MUST );
		
		try {
			indexWriter.deleteDocuments( entityDeletionQuery );
		}
		catch ( Exception e ) {
			String message = "删除不了 " + entityClazz + "#" + id + "";
			throw new RuntimeException( message, e );
		}finally{
			closeWriter(indexWriter);
		}
	}
}
