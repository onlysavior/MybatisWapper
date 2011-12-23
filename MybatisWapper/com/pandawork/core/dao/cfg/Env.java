package com.pandawork.core.dao.cfg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import com.pandawork.core.annotations.LazyClass;
import com.pandawork.core.annotations.LazyField;

/**
 * DAO层的上下文
 * 
 * @author Administrator
 * 
 */
public final class Env implements ApplicationContextAware,InitializingBean,BeanFactoryPostProcessor{
	private static Env INCETANTCE;

	private Map<Class<?>, String> tableCache;
	private Map<Class<?>, String> IdNameCache;
	private List<Class<?>> lazyClassCache;
	private Map<Class<?>, List<String>> lazyFieldCache;
	private static ApplicationContext context;
	//private static PathScanner scanner = new PathScanner((BeanDefinitionRegistry) context, false);
	private static PathScanner scanner;
	private Env() {
		tableCache = new ConcurrentHashMap<Class<?>, String>();
		IdNameCache = new ConcurrentHashMap<Class<?>, String>();
		lazyClassCache = new ArrayList<Class<?>>();
		lazyFieldCache = new ConcurrentHashMap<Class<?>, List<String>>();
		
//		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
	}

	public static void configure(String ...basePath) {
		scanner.scan(basePath);
	}
	
	private static void doCheck(BeanDefinitionHolder bdh){
		String className = bdh.getBeanDefinition().getBeanClassName();
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("实体类找不到"+className);
		}
		checkTableAnnotation(clazz);
		checkIdNameCache(clazz);
		checkLazy(clazz);
	}
	
	protected static void checkTableAnnotation(Class<?> clazz){
		Table tableAnn = clazz.getAnnotation(Table.class);
		if(tableAnn == null){
			throw new RuntimeException("实体类中不能没有Table注解"+clazz.getName());
		}else{
			String tableName = tableAnn.name();
			INCETANTCE.getTableCache().put(clazz, tableName);
		}
	}
	
	protected static void checkIdNameCache(Class<?> clazz){
		boolean finded = false;
		Field[] fields = clazz.getDeclaredFields();
		for(Field field :fields){
			Id idAnn = field.getAnnotation(Id.class);
			if(idAnn != null){
				String idName = field.getName();
				INCETANTCE.getIdNameCache().put(clazz, idName);
				finded = true;
				break;
			}
		}
		
		if(finded == false){
			throw new RuntimeException("实体类没有Id注解"+clazz.getName());
		}
	}
	
	protected static void checkLazy(Class<?> clazz){
		LazyClass lazyClassAnn = clazz.getAnnotation(LazyClass.class);
		if(lazyClassAnn != null){
			INCETANTCE.getLazyClassCache().add(clazz);
			
			List<String> excludeFieldName = new ArrayList<String>();
			
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields){
				LazyField lazyFieldAnn = field.getAnnotation(LazyField.class);
				if(lazyFieldAnn != null){
					String fieldName = field.getName();
					excludeFieldName.add(fieldName);
				}
			}
			
			if(excludeFieldName.size() == 0){
				throw new RuntimeException("配置了LazyClass，但是没有配置LazyField"+clazz.getName());
			}else{
				INCETANTCE.lazyFieldCache.put(clazz, excludeFieldName);
			}
		}
	}
	
	//因为是ConcurrentHashMap，所以不用加同步
	
	public static String getTableName(Class<?> clazz){
		return INCETANTCE.tableCache.get(clazz);
	}
	
	public static String getIdName(Class<?> clazz){
		return INCETANTCE.IdNameCache.get(clazz);
	}
	
	public static List<String> getExcludeFieldName(Class<?> clazz){
		return INCETANTCE.lazyFieldCache.get(clazz);
	}
	
	public static boolean containsLazyClass(Class<?> clazz){
		return INCETANTCE.lazyClassCache.contains(clazz);
	}

	public static Env getEnvironment() {
		if(INCETANTCE == null){
			synchronized (Env.class) {
				if(INCETANTCE == null){
					INCETANTCE = new Env();
				}
			}
		}
		return INCETANTCE;
	}

	public Map<Class<?>, String> getTableCache() {
		return tableCache;
	}

	public Map<Class<?>, String> getIdNameCache() {
		return IdNameCache;
	}

	public List<Class<?>> getLazyClassCache() {
		return lazyClassCache;
	}

	public Map<Class<?>, List<String>> getLazyFieldCache() {
		return lazyFieldCache;
	}

	private static class PathScanner extends ClassPathBeanDefinitionScanner {
		private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

		private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();

		private final BeanDefinitionRegistry registry;

		public PathScanner(BeanDefinitionRegistry registry,
				boolean useDefaultFilters) {
			super(registry, useDefaultFilters);
			this.registry = registry;
		}

		public PathScanner(BeanDefinitionRegistry registry) {
			super(registry, false);
			this.registry = registry;
		}
		
		@Override
		public int scan(String... basePackages){
			Assert.notEmpty(basePackages, "包名不能为空");
			int beanCountAtScanStart = this.registry.getBeanDefinitionCount();
			Set<BeanDefinitionHolder> resources = doScan(basePackages);
			for(BeanDefinitionHolder bdh:resources){
				//回调doCheck()
				doCheck(bdh);
			}
			return this.registry.getBeanDefinitionCount() - beanCountAtScanStart;
		}
		
		@Override
		protected Set<BeanDefinitionHolder> doScan(String... basePackages){
			Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
			for (String basePackage : basePackages) {
				Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
				for (BeanDefinition candidate : candidates) {
					ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
					candidate.setScope(scopeMetadata.getScopeName());
					String beanName = this.beanNameGenerator.generateBeanName(candidate, this.registry);
					if (candidate instanceof AbstractBeanDefinition) {
						postProcessBeanDefinition((AbstractBeanDefinition) candidate, beanName);
					}
					if (checkCandidate(beanName, candidate)) {
						BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
						beanDefinitions.add(definitionHolder);
					}
				}			
				
			}
			return beanDefinitions;
		}
	}

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		context = ctx;
//		scanner = new PathScanner((BeanDefinitionRegistry) context, false);
//		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException {
		scanner = new PathScanner((BeanDefinitionRegistry) beanFactory, false);
		scanner.setResourceLoader(context);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
	}
}
