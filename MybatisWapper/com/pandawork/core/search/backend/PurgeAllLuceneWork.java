package com.pandawork.core.search.backend;

import java.io.Serializable;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import com.pandawork.core.search.engine.DocumentBuilderIndexedEntity;

public class PurgeAllLuceneWork extends LuceneWork implements Serializable {
	
	private static final long serialVersionUID = -8412581587649867635L;

	public PurgeAllLuceneWork(Class<?> entity) {
		super( null, null, entity, null );
	}

	public void performWork(IndexWriter indexWriter) {
		final Class<?> entityClazz = getEntityClass();
		try {
			Term term = new Term( DocumentBuilderIndexedEntity.CLASS_FIELDNAME, entityClazz.getName() );
			indexWriter.deleteDocuments( term );
		}catch(Exception e){
			throw new RuntimeException("不能操作");
		}finally{
			closeWriter(indexWriter);
		}
	}
}
