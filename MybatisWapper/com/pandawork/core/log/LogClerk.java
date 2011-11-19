package com.pandawork.core.log;

/**
 * 小秘类。 管理所有的输出。
 * 包括：
 * 1 业务日志
 * 2 系统输出日志
 * 
 * @author Lionel pang
 * @date 2010-3-15
 */
public final class LogClerk {
	// 系统日志对象
	public final static Log sysout = new Log4jImpl(LogConstants.sysOutLogName);
	// 业务日志
	public final static Log bizLog = new Log4jImpl(LogConstants.bizOutNanme);
	// 统计日志
	public final static Log monitorLog = new Log4jImpl(LogConstants.statLogName);
	// 系统错误日志
	public final static Log errLog = new Log4jImpl(LogConstants.errorLogName);
	// web日志
	public final static Log webLog = new Log4jImpl(LogConstants.webLogName);
	// sql语句日志
	public final static Log sqlLog = new Log4jImpl(LogConstants.sqlLogName);
}
