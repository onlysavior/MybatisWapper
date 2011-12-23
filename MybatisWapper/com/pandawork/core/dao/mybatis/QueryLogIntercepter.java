package com.pandawork.core.dao.mybatis;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.pandawork.core.dao.CommonDao;
import com.pandawork.core.dao.cfg.Env;
import com.pandawork.core.entity.EntityProxy;
@Intercepts({@Signature(type=Executor.class,method = "query",args={MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class})})
public class QueryLogIntercepter implements Interceptor {
	@Autowired
	private CommonDao commonDao;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement)args[0];
		Object param = args[1];
		System.out.println("sql@@@@@:"+ms.getBoundSql(param).getSql());
		
		
		
		Object result = invocation.proceed();
		Class<?> clazz = result.getClass();
		if(Env.containsLazyClass(clazz)){
			EntityProxy proxy = new EntityProxy(Env.getExcludeFieldName(clazz));
			proxy.setCommonDao(commonDao);
			
			return proxy.getEntityProxy(result);
		}
		
		return result;
	}

	@Override
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	@Override
	public void setProperties(Properties arg0) {

	}


}
