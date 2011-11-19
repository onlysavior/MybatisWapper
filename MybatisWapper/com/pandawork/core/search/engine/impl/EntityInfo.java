package com.pandawork.core.search.engine.impl;

import java.io.Serializable;

public class EntityInfo {

	private final Class<?> clazz;
	private final Serializable id;
	private final String idName;
	
	public Class<?> getClazz() {
		return clazz;
	}

	public Serializable getId() {
		return id;
	}

	public String getIdName() {
		return idName;
	}
	
	public EntityInfo(Class<?> clazz,  String idName,  Serializable id) {
		this.clazz = clazz;
		this.idName = idName;
		this.id = id;
	}
}
