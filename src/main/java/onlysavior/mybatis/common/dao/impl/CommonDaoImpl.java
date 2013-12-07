package onlysavior.mybatis.common.dao.impl;

import onlysavior.mybatis.common.context.EntityContext;
import onlysavior.mybatis.common.dao.CommonDao;
import onlysavior.mybatis.common.util.Pair;
import onlysavior.mybatis.common.util.ReflectionUtil;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-7
 * Time: 下午3:34
 * To change this template use File | Settings | File Templates.
 */
public class CommonDaoImpl extends SqlSessionDaoSupport implements CommonDao {
    // 当批量操作时，每次的操作数目
    private static final int BATCH_SIZE = 20;
    private EntityContext context;

    public void setContext(EntityContext context) {
        this.context = context;
    }

    // commondao命名空间
    private String getNameSpace() {
        return "onlysavior.mybatis.common.dao.";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public <T> void delete(T obj) throws Exception {
        String idName = context.getIdNames().getName(obj.getClass());
        Object idValue = ReflectionUtil.readValue(obj, idName);

        String tableName = context.getTableNames().get(obj.getClass());

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
        String tableName = context.getTableNames().getName(obj.getClass());
        Map<String, Object> param = new HashMap<String, Object>();

        List<Pair> keyValuePairs = ReflectionUtil.listKeyValuePair(obj);

        param.put("keyList", keyValuePairs);
        param.put("tableName", tableName);
        getSqlSession().insert(getNameSpace() + "insert", param);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public <T> Serializable insertAndReturn(T obj) throws Exception {
        String tableName = context.getTableNames().get(obj.getClass());
        String idName = context.getIdNames().get(obj.getClass());

        Map<String, Object> param = new HashMap<String, Object>();

        List<Pair> keyValuePairs = ReflectionUtil.listKeyValuePair(obj);

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
        String tableName = context.getTableNames().get(obj.getClass());
        List<String> fieldNames = new ArrayList<String>();
        List<Pair> fields = ReflectionUtil.listKeyValuePair(obj);
        for (Pair fp : fields) {
            fieldNames.add((String) fp.getKey());
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableName", tableName);
        param.put("fieldNames", fieldNames);
        List<List<Pair>> keyValuePairs = new ArrayList<List<Pair>>();
        List<Pair> subValuePairs;
        Iterator<T> it = objs.iterator();
        T tempObj;

        int runTimeindex = 0; // 运行阶段计数器
        int totleIndex = 0; // 总计数器
        while (it.hasNext()) {
            tempObj = it.next();
            subValuePairs = ReflectionUtil.listKeyValuePair(tempObj);
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
        String idName = context.getIdNames().get(obj.getClass());
        Object idValue = ReflectionUtil.readValue(obj, idName);
        String tableName = context.getTableNames().get(obj.getClass());

        List<Pair> fieldsName = ReflectionUtil.listKeyValuePair(obj);

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
        String tableName = context.getTableNames().get(obj.getClass());
        Map<String, Object> param = new HashMap<String, Object>();

        List<Pair> keyValuePairs = ReflectionUtil.listKeyValuePair(obj);

        param.put("keyList", keyValuePairs);
        param.put("tableName", tableName);
        getSqlSession().insert(getNameSpace() + "saveOrUpdate", param);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, I extends Serializable> T queryById(Class<T> clazz, I id)
            throws Exception {
        String idName = context.getIdNames().get(clazz);
        String tableName = context.getTableNames().get(clazz);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("tableName", tableName);
        param.put("idName", idName);
        Map<String, Object> resultMap;
        resultMap = (HashMap<String, Object>) getSqlSession().selectOne(
                getNameSpace() + "queryById", param);
        return ReflectionUtil.convertMap2Object(clazz, resultMap);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> queryByPage(String hql, int firstResult, int num,
                                   Class<T> cla) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("queryString", hql);
        param.put("startPos", firstResult);
        param.put("rowNum", num);
        List<Map<String, Object>> resultMap = getSqlSession().selectList(getNameSpace() + "queryByPage", param);

        return ReflectionUtil.convertListMap2ListObject(cla, resultMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public <T> void deleteById(T obj) throws Exception {
        delete(obj);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public <T> void updateFieldsById(T obj, String... fields) throws Exception {
        String idName = context.getIdNames().get(obj.getClass());
        Object idValue = ReflectionUtil.readValue(obj, idName);
        String tableName = context.getTableNames().get(obj.getClass());

        List<Pair> keyValuePairs = ReflectionUtil.listKeyValuePair(obj, false, fields);

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("idName", idName);
        param.put("fieldsName", keyValuePairs);
        param.put("tableName", tableName);
        param.put("id", idValue);

        getSqlSession().update(getNameSpace() + "update", param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public <T, I extends Serializable> T queryInLazyWay(Class<T> clazz, I id,
                                                        List<String> excludeFieldList) throws Exception {
/*        EntityProxy proxy = new EntityProxy(excludeFieldList);
        proxy.setCommonDao(this);

        String tableName = Env.getTableName(clazz);
        String idName = Env.getIdName(clazz);

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
        return proxyObj;*/

        return null;
    }

    @Override
    public <T> Object lazyLoad(T obj, String propertyName) throws Exception {
/*        FieldPair idPair = CommonDaoUtil.getPrimKey(obj, obj.getClass());
        String idName = Env.getIdName(obj.getClass());
        Object idValue = CommonDaoUtil.readValue(obj, idPair);
        String tableName = Env.getTableName(obj.getClass());

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableName", tableName);
        param.put("idName", idName);
        param.put("id", idValue);
        param.put("properyName", propertyName);

        return getSqlSession().selectOne(getNameSpace() + "lazyLoad", param);*/
        return null;
    }
}
