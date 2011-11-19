package com.pandawork.core.log;

import org.apache.log4j.Appender;

/**
 * 日志logger
 * 
 * @author Lionel pang
 * @date 2010-7-7
 */
public interface CLogger {

	public void addAppender(Appender newAppender);

	public void assertLog(boolean assertion, String msg);

	public void debug(Object message, Throwable t);

	public void debug(Object message);

	public boolean equals(Object obj);

	public void error(Object message, Throwable t);

	public void error(Object message);

	public void fatal(Object message, Throwable t);

	public void fatal(Object message);

	public void info(Object message, Throwable t);

	public void info(Object message);

	public void trace(Object message, Throwable t);

	public void trace(Object message);

	public void warn(Object message, Throwable t);

	public void warn(Object message);

}
