package com.pandawork.core.search.backend;

import java.io.Serializable;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import com.pandawork.core.search.cfg.ConfigBean;

public abstract class LuceneWork implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected static ConfigBean context = ConfigBean.getInstanse();
	
	private final Document document;
	private final Class<?> entityClass;
	private final Serializable id;
	
	private final boolean batch;
	private final String idInString;
	
	public LuceneWork(Serializable id, String idInString, Class<?> entity) {
		this( id, idInString, entity, null );
	}

	public LuceneWork(Serializable id, String idInString, Class<?> entity, Document document) {
		this( id, idInString, entity, document, false );
	}

	public LuceneWork(Serializable id, String idInString, Class<?> entity, Document document, boolean batch) {
		this.id = id;
		this.idInString = idInString;
		this.entityClass = entity;
		this.document = document;
		this.batch = batch;
	}
	
	public abstract void performWork(IndexWriter indexWriter);
	
	public boolean isBatch() {
		return batch;
	}

	public Document getDocument() {
		return document;
	}

	public Class getEntityClass() {
		return entityClass;
	}

	public Serializable getId() {
		return id;
	}

	public String getIdInString() {
		return idInString;
	}
}
