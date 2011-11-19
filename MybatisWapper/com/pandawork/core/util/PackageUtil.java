package com.pandawork.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class PackageUtil {

	private static String[] CLASS_PATH_PROP = { "java.class.path", "java.ext.dirs", "sun.boot.class.path"};

	private static List<File> CLASS_PATH_ARRAY = getClassPath();

	private static List<File> getClassPath() {
		List<File> ret = new ArrayList<File>();
		String delim = ":";
		if (System.getProperty("os.name").indexOf("Windows") != -1) delim = ";";
		for (String pro : CLASS_PATH_PROP) {
			String[] pathes = System.getProperty(pro).split(delim);
			for (String path : pathes){
				ret.add(new File(path));
			}
		}
		return ret;
	}

	public static List<String> getClassInPackage(String pkgName) {
		List<String> ret = new ArrayList<String>();
		String rPath = pkgName.replace('.', '/') + "/";
		try {
			for (File classPath : CLASS_PATH_ARRAY) {
				if (!classPath.exists()) continue;
				if (classPath.isDirectory()) {
					File dir = new File(classPath, rPath);
					if (!dir.exists()) continue;
					for (File file : dir.listFiles()) {
						if (file.isFile()) {
							String clsName = file.getName();
							clsName = pkgName + "." + clsName.substring(0, clsName.length() - 6);
							ret.add(clsName);
						} else if(file.isDirectory()){
							scanDirectory(pkgName, file, ret);
						}
					}
				} else {
					FileInputStream fis = new FileInputStream(classPath);
					JarInputStream jis = new JarInputStream(fis, false);
					JarEntry e = null;
					while ((e = jis.getNextJarEntry()) != null) {
						String eName = e.getName();
						if (eName.startsWith(rPath) && !eName.endsWith("/")) {
							ret.add(eName.replace('/', '.').substring(0, eName.length() - 6));
						}
						jis.closeEntry();
					}
					jis.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ret;
	}
	
	/**
	 * 扫描目录下的class文件
	 * 
	 * @param dir	
	 * @param ret
	 */
	private static void scanDirectory(String pkgName, File dir, List<String> ret){
		if(dir.isFile()){
			addFile(pkgName, dir, ret);
			return;
		}
		
		pkgName += "." + dir.getName();
		
		for (File file : dir.listFiles()) {
			if(file.isDirectory()){
				// 如果是目录，继续扫描
				scanDirectory(pkgName, file, ret);
			}
			
			if (file.isFile()) {
				addFile(pkgName, file, ret);
			}
		}
		
	}
	
	/**
	 * 添加文件到一个list中
	 * @param pkgName	包名
	 * @param file		文件名称 
	 * @param ret		类容器名称
	 */
	private static void addFile(String pkgName, File file, List<String> ret){
		String clsName = file.getName();
		clsName = pkgName + "." + clsName.substring(0, clsName.length() - 6);
		ret.add(clsName);
	}
	
	public static void main(String[] args){
		System.out.println(System.getProperty("java.class.path"));
	}
}
