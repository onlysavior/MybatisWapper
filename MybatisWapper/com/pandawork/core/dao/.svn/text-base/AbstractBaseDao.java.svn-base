package com.pandawork.core.dao;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * 所有 DAO的父亲，抽象的类型
 * 
 * @author Lionel pang
 * @date 2010-3-18
 */
public abstract class AbstractBaseDao extends SqlSessionDaoSupport {

    protected SqlSession session() {
        return getSqlSession();
    }
}
