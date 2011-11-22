package com.pandawork.core.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pandawork.core.dao.AbstractBaseDao;
import com.pandawork.core.dao.CommonDao;
import com.pandawork.core.entity.AbstractEntity;
import com.pandawork.core.entity.EntityProxy;
import com.pandawork.core.search.spi.SearchFacade;

/**
 * 通用DAO实现 主要包括通用的删除，插入，更新，更新或者删除（不推荐）。
 * 
 * @author Lionel pang
 * @date 2010-3-18
 * 
 * @update (insert: queryById, queryByPage)
 * @author yangbl
 * @date 2010-4-19
 */
@Repository("commonDao")
public class CommonDefaultDao extends AbstractBaseDao implements CommonDao {

	// 当批量操作时，每次的操作数目
	private static final int BATCH_SIZE = 20;

	// commondao命名空间
	private String getNameSpace() {
		return "com.pandawork.core.dao.";
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void delete(T obj) throws Exception {
		FieldPair idPair = CommonDaoUtil.getPrimKey(obj, obj.getClass());
		String idName = idPair.getField().getName();
		Object idValue = CommonDaoUtil.readValue(obj, idPair);

		String tableName = CommonDaoUtil.getTableName(obj.getClass());

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tableName", tableName);
		param.put("id", idValue);
		param.put("idName", idName);
		getSqlSession().delete(getNameSpace() + "deleteById", param);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void deleteAll(Collection<T> entities) throws Exception {
		Iterator<T> it = entities.iterator();

		while (it.hasNext()) {
			T obj = it.next();
			delete(obj);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void insert(T obj) throws Exception {
		String tableName = CommonDaoUtil.getTableName(obj.getClass());
		Map<String, Object> param = new HashMap<String, Object>();

		List<KeyValuePair> keyValuePairs = CommonDaoUtil.listKeyValuePair(obj);

		param.put("keyList", keyValuePairs);
		param.put("tableName", tableName);
		getSqlSession().insert(getNameSpace() + "insert", param);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public <T> Serializable insertAndReturn(T obj) throws Exception {
		String tableName = CommonDaoUtil.getTableName(obj.getClass());
		FieldPair idPair = CommonDaoUtil.getPrimKey(obj, obj.getClass());
		String idName = idPair.getField().getName();

		Map<String, Object> param = new HashMap<String, Object>();

		List<KeyValuePair> keyValuePairs = CommonDaoUtil.listKeyValuePair(obj);

		param.put("keyList", keyValuePairs);
		param.put("tableName", tableName);
		getSqlSession().insert(getNameSpace() + "insert", param);

		param.put("idName", idName);

		return (Integer) getSqlSession().selectOne(
				getNameSpace() + "getLastestId", param);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void insertAll(Collection<T> objs) throws Exception {
		// 批量插入
		T obj = objs.iterator().next();
		String tableName = CommonDaoUtil.getTableName(obj.getClass());
		List<String> fieldNames = new ArrayList<String>();
		List<KeyValuePair> fields = CommonDaoUtil.listKeyValuePair(obj);
		for (KeyValuePair fp : fields) {
			fieldNames.add((String) fp.getKey());
		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tableName", tableName);
		param.put("fieldNames", fieldNames);
		List<List<KeyValuePair>> keyValuePairs = new ArrayList<List<KeyValuePair>>();
		List<KeyValuePair> subValuePairs;
		Iterator<T> it = objs.iterator();
		T tempObj;

		int runTimeindex = 0; // 运行阶段计数器
		int totleIndex = 0; // 总计数器
		while (it.hasNext()) {
			tempObj = it.next();
			subValuePairs = CommonDaoUtil.listKeyValuePair(tempObj);
			keyValuePairs.add(subValuePairs);
			runTimeindex++;
			totleIndex++;

			if (runTimeindex == BATCH_SIZE || totleIndex == objs.size()) {
				param.put("keyPairs", keyValuePairs);
				getSqlSession().insert(getNameSpace() + "insertAll", param);
				keyValuePairs.clear();
				runTimeindex = 0;
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void update(T obj) throws Exception {
		FieldPair idPair = CommonDaoUtil.getPrimKey(obj, obj.getClass());
		String idName = idPair.getField().getName();
		Object idValue = CommonDaoUtil.readValue(obj, idPair);
		String tableName = CommonDaoUtil.getTableName(obj.getClass());

		List<KeyValuePair> fieldsName = CommonDaoUtil.listKeyValuePair(obj);

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("fieldsName", fieldsName);
		param.put("tableName", tableName);
		param.put("id", idValue);
		param.put("idName", idName);
		getSqlSession().update(getNameSpace() + "update", param);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void saveOrUpdate(T obj) throws Exception {
		String tableName = CommonDaoUtil.getTableName(obj.getClass());
		Map<String, Object> param = new HashMap<String, Object>();

		List<KeyValuePair> keyValuePairs = CommonDaoUtil.listKeyValuePair(obj,
				obj.getClass(), true);

		param.put("keyList", keyValuePairs);
		param.put("tableName", tableName);
		getSqlSession().insert(getNameSpace() + "saveOrUpdate", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, I extends Serializable> T queryById(Class<T> clazz, I id)
			throws Exception {
		String idName = CommonDaoUtil.getPrimKeyName(clazz);
		String tableName = CommonDaoUtil.getTableName(clazz);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("tableName", tableName);
		param.put("idName", idName);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = (HashMap<String, Object>) getSqlSession().selectOne(
				getNameSpace() + "queryById", param);
		return CommonDaoUtil.convertMap2Object(clazz, resultMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> queryByPage(String hql, int firstResult, int num,
			Class<T> cla) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("queryString", hql);
		param.put("startPos", firstResult);
		param.put("rowNum", num);
		List<Map<String, Object>> resultMap = (List<Map<String, Object>>) getSqlSession()
				.selectList(getNameSpace() + "queryByPage", param);

		return CommonDaoUtil.convertListMap2ListObject(cla, resultMap);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void deleteById(T obj) throws Exception {
		delete(obj);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void updateFieldsById(T obj, String... fields) throws Exception {
		FieldPair idPair = CommonDaoUtil.getPrimKey(obj, obj.getClass());
		String idName = idPair.getField().getName();
		Object idValue = CommonDaoUtil.readValue(obj, idPair);
		String tableName = CommonDaoUtil.getTableName(obj.getClass());

		List<KeyValuePair> keyValuePairs = CommonDaoUtil.listKeyValuePair(obj,
				obj.getClass(), false, fields);

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("idName", idName);
		param.put("fieldsName", keyValuePairs);
		param.put("tableName", tableName);
		param.put("id", idValue);

		getSqlSession().update(getNameSpace() + "update", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<? extends AbstractEntity> fullTextList(Map<String, Object> param)
			throws Exception {

		return (List<? extends AbstractEntity>) getSqlSession().selectList(
				getNameSpace() + "fullTextList", param);
		// TODO 没写XML
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void fullTextInsert(T obj) throws Exception {
		this.insert(obj);
		SearchFacade.onPostInsert(obj);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void fullTextUpdate(T obj) throws Exception {
		this.update(obj);
		SearchFacade.onPostUpdate(obj);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void fullTextDelete(T obj) throws Exception {
		this.delete(obj);
		SearchFacade.onPostDelete(obj);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T, I extends Serializable> T queryInLazyWay(Class<T> clazz, I id,
			List<String> excludeFieldList) throws Exception {
		EntityProxy.setCommonDao(this);
		EntityProxy<T> proxy = new EntityProxy<T>(excludeFieldList);

		String tableName = CommonDaoUtil.getTableName(clazz);
		String idName = CommonDaoUtil.getPrimKeyName(clazz);

		List<String> fieldNames = CommonDaoUtil.listFieldNamesExcludeGiven(
				clazz, excludeFieldList);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("fieldNames", fieldNames);
		param.put("tableName", tableName);
		param.put("idName", idName);
		param.put("id", id);

		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) getSqlSession()
				.selectOne(getNameSpace() + "queryInLazyWay", param);
		
		T obj = CommonDaoUtil.convertMap2Object(clazz, resultMap);
		proxy.setOriginEntity(obj);
		T proxyObj = proxy.getEntityProxy(obj);
		return proxyObj;
	}

	@Override
	public <T> Object lazyLoad(T obj, String propertyName) throws Exception {
		FieldPair idPair = CommonDaoUtil.getPrimKey(obj, obj.getClass());
		String idName = idPair.getField().getName();
		Object idValue = CommonDaoUtil.readValue(obj, idPair);
		String tableName = CommonDaoUtil.getTableName(obj.getClass());

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tableName", tableName);
		param.put("idName", idName);
		param.put("id", idValue);
		param.put("properyName", propertyName);

		return getSqlSession().selectOne(getNameSpace() + "lazyLoad", param);
	}

}
