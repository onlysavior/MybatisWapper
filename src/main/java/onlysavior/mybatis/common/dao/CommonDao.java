package onlysavior.mybatis.common.dao;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface CommonDao {
    /**
     * 插入对象
     *
     * @param <T>
     * @param city
     */
    public <T> void insert(T obj) throws Exception;

    /**
     * 更新对象
     *
     * @param <T>
     * @param obj
     * @return
     */
    public <T> void update(T obj) throws Exception;

    /**
     * 删除对象
     *
     * @param <T>
     * @param obj
     * @return
     */
    public <T> void delete(T obj) throws Exception;

    /**
     * 更新或者删除
     *
     * @param <T>
     * @param ojb
     */
    public <T> void saveOrUpdate(T ojb) throws Exception;

    /**
     * 按照id查询实体类
     *
     * @param <T>
     * @param <I>
     * @param ojb
     * @param id
     * @return
     * @throws Exception
     */
    public <T, I extends Serializable> T queryById(Class<T> clazz, I id) throws Exception;

    /**
     * 分页
     *
     * @param <T>
     * @param <F>
     * @param <N>
     * @param hql
     * @param firstResult
     * @param num
     * @return
     * @throws Exception
     */
    public <T> List<T> queryByPage(String hql, int firstResult, int num,Class<T> cla) throws Exception;

    /**
     * 一次性删除多个实体
     *
     * @param <T>
     * @param entities
     */
    public <T> void deleteAll(Collection<T> entities) throws Exception;

    /**
     * 通过Id删除实体
     *
     * @param <T>
     * @param obj
     * @throws Exception
     */
    public <T> void deleteById(T obj) throws Exception;

    /**
     * 批量插入
     *
     * @param <T>
     * @param objs
     * @throws Exception
     */
    public <T> void insertAll(Collection<T> objs) throws Exception;

    /**
     * 更新指定域的值
     *
     * @param <T>
     * @param obj
     * @param fields
     * @throws Exception
     */
    public <T> void updateFieldsById(T obj, String... fields) throws Exception;

    /**
     * 插入并返回有Id值的实体
     * @param <T>
     * @param obj
     * @return
     * @throws Exception
     */
    public <T> Serializable insertAndReturn(T obj) throws Exception;

    //以下是lazy load 的方法
    public <T,I extends Serializable> T queryInLazyWay(Class<T> clazz,I id,List<String> excludeFieldList) throws Exception;
    public <T> Object lazyLoad(T obj,String propertyName) throws Exception;
}
