package com.pandawork.core.dao.mybatis;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;

import com.pandawork.core.log.LogClerk;

/**
 * 查找mapper.mxl文件
 * 
 * @author Lionel pang
 * @date 2011-9-14
 * 
 */
public class MapperUtil {

    public static Resource[] mutilScan(String packageName) {
        String[] paths = StringUtils.tokenizeToStringArray(packageName,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

        Set<Resource> result = new LinkedHashSet<Resource>();

        for (String path : paths) {
            result.addAll(scan(path));
        }
        Resource[] rResult = new Resource[result.size()];
        result.toArray(rResult);
        return rResult;
    }

    /**
     * 扫描当前目录下的所有的mapper文件，并装载成为FileSystemResource返回.
     * 
     * @param pkgName
     * @return
     */
    public static Set<Resource> scan(String pkgName) {
        Set<Resource> ret = new LinkedHashSet<Resource>();
        String rPath = pkgName.replace('.', '/') + "/";
        
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            resourceLoader.getResource(rPath);
            Enumeration<URL> resourceUrls = resourceLoader.getClassLoader().getResources(rPath);
            Set<Resource> result = new LinkedHashSet<Resource>(16);
            while (resourceUrls.hasMoreElements()) {
                URL url = resourceUrls.nextElement();
                result.add(new UrlResource(url));
            }
            
            for(Resource resource: result){
                scanDirectory(resource.getFile(), ret);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    private static void scanDirectory(File dir, Set<Resource> ret) {
        if (dir.isFile()) {
            addFile(dir, ret);
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                // 如果是目录，继续扫描
                scanDirectory(file, ret);
            }

            if (file.isFile()) {
                addFile(file, ret);
            }
        }

    }

    private static void addFile(File file, Set<Resource> ret) {
        String clsName = file.getName();
        if (clsName.endsWith(".mapper.xml")) { //如果是mapper文件，则创建一个文件系统资源
            ret.add(new FileSystemResource(file));
            
            LogClerk.sysout.info("Loading custom mapper file:" + file.getAbsolutePath());
        }
    }
}
