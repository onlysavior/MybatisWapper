package com.pandawork.core.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Query;

import com.pandawork.core.dao.CommonDao;
import com.pandawork.core.entity.AbstractEntity;
import com.pandawork.core.search.cfg.ConfigBean;
import com.pandawork.core.search.cfg.ConfigBean.BuildContext;
import com.pandawork.core.search.engine.impl.EntityInfo;
import com.pandawork.core.search.engine.impl.IBQuery;
import com.pandawork.core.util.Pagination;

public class FullTextSqlSession {

	private CommonDao commonDao;
	private static final ConfigBean config = ConfigBean.getInstanse();
	private IBQuery query;
	private Query luceneQuery;
	
	public FullTextSqlSession createFullTextSqlSession(Query query,Class<?> clazz,CommonDao dao){
		return createFullTextSqlSession(query,clazz,dao,null);
	}
	
	public FullTextSqlSession createFullTextSqlSession(Query query,Class<?> clazz,CommonDao dao,Pagination page){
		this.commonDao = dao;
		this.luceneQuery = query;
		
		List<Class<?>> classes = new ArrayList<Class<?>>(1);
		classes.add(clazz);
		
		this.query = new IBQuery().luceneQuery(luceneQuery).targetedEntities(classes);
		
		if(page != null){
			this.query = this.query.firstResult(page.getCurrentFristPosition()).maxResults(page.getPageSize());
		}
		
		return this;
	}
	
	public List<? extends AbstractEntity> list(){
		final List<EntityInfo> entityInfos = query.queryEntityInfos();
		
		EntityInfo firstEntity = entityInfos.get(0);
		String idName = firstEntity.getIdName();
		String tableName = BuildContext.class2TableName.get(firstEntity.getClazz());
		List<Serializable> idList = new ArrayList<Serializable>();
		for(EntityInfo entity : entityInfos){
			idList.add(entity.getId());
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tableName", tableName);
		param.put("idName", idName);
		param.put("idList", idList);
		
		try {
			//TODO 是不是应该转换类型
			return commonDao.fullTextList(param);
		} catch (Exception e) {
			throw new RuntimeException("出错了"+e.getMessage());
		}
	}
}
