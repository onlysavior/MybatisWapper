package com.pandawork.core.util;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

/**
 * IO操作工具类
 * 
 * @author Administrator
 * 
 */
public class IOUtil {
    // 类目录前缀标识符
    public final static String CLASSPATH = "classpath:";
    
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static File getFile(String path) {
		File file = null;
		URL url = null;
		if (path.startsWith(CLASSPATH)) {
			path = path.substring(CLASSPATH.length());

			url = getClassLoader().getResource(path);
			final boolean fileExist = url != null;
			try {
                path = url != null? URLDecoder.decode(url.getPath(), "UTF-8") : path;
            } catch (Exception e) {
                e.printStackTrace();
            }
			path = path.replaceAll("%20", " ");
			
			file = new File(path) {
				private static final long serialVersionUID = 4009013298629147887L;

				public boolean exists() {
					return fileExist;
				}
			};
		} else {
			file = new File(path);
		}

		return file;
	}

	/**
	 * 获取类装载器
	 * 
	 * @return
	 */
	private static ClassLoader getClassLoader() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = IOUtil.class.getClassLoader();
		}

		return loader;
	}
}
