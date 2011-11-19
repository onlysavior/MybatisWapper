package com.pandawork.core.dao.mybatis;

import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import com.pandawork.core.log.LogClerk;

public class SQLLogPluginInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation inv) throws Throwable {
        return inv.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if(target instanceof StatementHandler) {
            StatementHandler handler = (StatementHandler) target;
            LogClerk.sqlLog.debug("sql: \n" + handler.getBoundSql().getSql());
        }
        
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
