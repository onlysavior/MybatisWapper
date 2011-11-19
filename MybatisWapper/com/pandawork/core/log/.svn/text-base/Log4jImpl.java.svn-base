package com.pandawork.core.log;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

/**
 * 日志记录类
 * 
 * @author Lionel pang
 * @date 2010-3-15
 */
public class Log4jImpl extends Log{
	// log实例
	private CLogger log;
	
	public Log4jImpl(String name){
		Logger lg = Logger.getLogger(name);
		this.log = new Log4jLogger(lg);
	}
	
	@Override
	protected CLogger instance() {
		return log;
	}
	
	private class Log4jLogger implements CLogger {
		private volatile Logger _log;
		
		public Log4jLogger(Logger log){
			_log = log;
		}

		public void addAppender(Appender newAppender) {
			_log.addAppender(newAppender);
		}

		public void assertLog(boolean assertion, String msg) {
			_log.assertLog(assertion, msg);
		}

		public void debug(Object message, Throwable t) {
			_log.debug(message, t);
		}

		public void debug(Object message) {
			_log.debug(message);
		}

		public boolean equals(Object obj) {
			return _log.equals(obj);
		}

		public void error(Object message, Throwable t) {
			_log.error(message, t);
		}

		public void error(Object message) {
			if (message instanceof Throwable) {
				Throwable throwable = (Throwable) message;
				_log.error("Log4j Log Stack:", throwable);
			} else {
				_log.error(message);
			}
		}

		public void fatal(Object message, Throwable t) {
			_log.fatal(message, t);
		}

		public void fatal(Object message) {
			_log.fatal(message);
		}

		public void info(Object message, Throwable t) {
			_log.info(message, t);
		}

		public void info(Object message) {
			_log.info(message);
		}

		public String toString() {
			return _log.toString();
		}

		public void trace(Object message, Throwable t) {
			_log.trace(message, t);
		}

		public void trace(Object message) {
			_log.trace(message);
		}

		public void warn(Object message, Throwable t) {
			_log.warn(message, t);
		}

		public void warn(Object message) {
			_log.warn(message);
		}
	}
}
