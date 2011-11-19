package com.pandawork.core.log;

import org.apache.log4j.Appender;

/**
 * 日志类的抽象父类
 * 
 * @author Lionel pang
 * @date 2010-7-7
 */
public abstract class Log{
	/**
	 * 获取实体类
	 * @return
	 */
	protected abstract CLogger instance();
	
	public void addAppender(Appender newAppender) {
		instance().addAppender(newAppender);
	}

	public void assertLog(boolean assertion, String msg) {
		instance().assertLog(assertion, msg);
	}

	public void debug(Object message, Throwable t) {
		instance().debug(message, t);
	}

	public void debug(Object message) {
		instance().debug(message);
	}

	public boolean equals(Object obj) {
		return instance().equals(obj);
	}

	public void error(Object message, Throwable t) {
		instance().error(message, t);
	}

	public void error(Object message) {
		if(message instanceof Throwable){
			Throwable throwable = (Throwable) message;
			instance().error("Log4j Log Stack:", throwable);
		} else{
			instance().error(message);
		}
	}

	public void fatal(Object message, Throwable t) {
		instance().fatal(message, t);
	}

	public void fatal(Object message) {
		instance().fatal(message);
	}

	public void info(Object message, Throwable t) {
		instance().info(message, t);
	}

	public void info(Object message) {
		instance().info(message);
	}

	public String toString() {
		return instance().toString();
	}

	public void trace(Object message, Throwable t) {
		instance().trace(message, t);
	}

	public void trace(Object message) {
		instance().trace(message);
	}

	public void warn(Object message, Throwable t) {
		instance().warn(message, t);
	}

	public void warn(Object message) {
		instance().warn(message);
	}
}
