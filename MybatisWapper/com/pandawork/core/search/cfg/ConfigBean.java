package com.pandawork.core.search.cfg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.persistence.Table;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.MergeScheduler;
import org.apache.lucene.search.DefaultSimilarity;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.util.Version;
import org.springframework.beans.BeansException;
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

import com.pandawork.core.entity.AbstractEntity;
import com.pandawork.core.search.annotations.Index;
import com.pandawork.core.search.engine.DocumentBuilderIndexedEntity;
import com.pandawork.core.search.exception.SearchException;
import com.pandawork.core.search.reader.NotSharedReaderProvider;
import com.pandawork.core.search.store.DirectoryProvider;
import com.pandawork.core.search.store.impl.FSDirectoryProvider;
import com.pandawork.core.util.ReflectionHelper;

/**
 * Search 启动 <P> ...T_T....
 * @author Administrator
 *
 */
public class ConfigBean implements ApplicationContextAware,BeanFactoryPostProcessor{
	
	private String basePath;
	
	private String luncenePropertyHome;
	
	private DirectoryProvider<?> directory;
	
	private String directoryName;
	
	private String defaultAnalyzerName;
	
	private String defaultSimilarityName;
	
	private Analyzer defaultAnalyzer;
	
	private Similarity defaultSimilarity;
	
	private BuildContext context = new BuildContext();
	
	private ApplicationContext applicationContext;
	
	private SearchClassPathScanner scanner;
	
	private volatile static ConfigBean INSTANSE;
	
	private NotSharedReaderProvider  reader;
	
	private Properties lunceneProperty;

	{
//		scanner.addIncludeFilter(new AnnotationTypeFilter(Index.class));
		reader = new NotSharedReaderProvider();
	}
	
	private ConfigBean(){}
	
	public void initIbatisSearch(){
		Assert.notNull(basePath, "basePath 不能为空");
		
		if(!"".equals(defaultAnalyzerName)){
			try {
				defaultAnalyzer = (Analyzer) (ReflectionHelper.classForName(defaultAnalyzerName)).newInstance();
			} catch (Exception e) {
				defaultAnalyzer = new PaodingAnalyzer();
//				defaultAnalyzer = new StandardAnalyzer(Version.LUCENE_31);
			}
		}
		
		if(!"".equals(defaultSimilarityName)){
			try {
				defaultSimilarity = (Similarity) (ReflectionHelper.classForName(defaultSimilarityName)).newInstance();
			} catch (Exception e) {
				defaultSimilarity = new DefaultSimilarity();
			}
		}
		
		if(StringUtils.isEmpty(luncenePropertyHome)){
			throw new SearchException("沒有配置Luncene相关的Property");
		}else{
			if(luncenePropertyHome.startsWith("classpath:")){
				luncenePropertyHome = luncenePropertyHome.substring("classpath:".length());
				//URL url = getClass().getResource(luncenePropertyHome);
				URL url = getClass().getClassLoader().getResource(luncenePropertyHome);
				InputStream in;
				try {
					in = url.openStream();
					if(lunceneProperty == null){
						lunceneProperty = new Properties();
					}
					lunceneProperty.load(in);
				} catch (IOException e) {
					throw new SearchException("无法加载相关Property"+url.getPath());
				}
			}else{
				throw new SearchException("暂不支持classpath以外的其他资源加载");
			}
		}
		
		scanner.scan(basePath);
	}
	
	public static ConfigBean getInstanse(){
		if(INSTANSE == null){
			synchronized (ConfigBean.class) {
				if(INSTANSE == null){
					INSTANSE = new ConfigBean();
				}
			}
		}
		return INSTANSE;
	}
	
	
	/***********************************setter and getter**************************/
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getDefaultAnalyzerName() {
		return defaultAnalyzerName;
	}

	public void setDefaultAnalyzerName(String defaultAnalyzerName) {
		this.defaultAnalyzerName = defaultAnalyzerName;
	}

	public String getDefaultSimilarityName() {
		return defaultSimilarityName;
	}

	public void setDefaultSimilarityName(String defaultSimilarityName) {
		this.defaultSimilarityName = defaultSimilarityName;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}


	public Analyzer getDefaultAnalyzer() {
		return defaultAnalyzer;
	}

	public void setDefaultAnalyzer(Analyzer defaultAnalyzer) {
		this.defaultAnalyzer = defaultAnalyzer;
	}

	public Similarity getDefaultSimilarity() {
		return defaultSimilarity;
	}

	public void setDefaultSimilarity(Similarity defaultSimilarity) {
		this.defaultSimilarity = defaultSimilarity;
	}

	public DirectoryProvider<?> getDirectory() {
		//ugly implement
		if(this.directory == null){
			this.directory = new FSDirectoryProvider();
			this.directory.initialize("FSDirectoryProvider", lunceneProperty, getInstanse());
		}
		return this.directory;
	}

	public void setDirectory(DirectoryProvider<?> directory) {
		this.directory = directory;
	}
	
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		applicationContext = arg0;
	}
	
	public BuildContext getBuildContext(){
		return context;
	}
	
	public NotSharedReaderProvider getReader(){
		return this.reader;
	}
	
	public static class BuildContext{
		public static  Map<Class<?>, String> class2TableName;
		public static  Map<Class<?>,DocumentBuilderIndexedEntity<?>> class2IndexedEntites;
		
		public static Queue<? extends AbstractEntity> entityBuffer;
		
		private static final Analyzer SIMPLE_ANALYZER = new SimpleAnalyzer( Version.LUCENE_31 );
		private final IndexWriterConfig batchWriterConfig = new IndexWriterConfig( Version.LUCENE_31, SIMPLE_ANALYZER );
		
		private IndexWriter writer;
		
		static{
			class2TableName = new HashMap<Class<?>, String>(32);
			class2IndexedEntites = new HashMap<Class<?>, DocumentBuilderIndexedEntity<?>>(32);
			entityBuffer = new LinkedBlockingQueue<AbstractEntity>();
		}
		
		/**
		 * TODO batchmode 批量
		 * @param batchmode
		 * @return
		 */
		public synchronized IndexWriter getIndexWriter(boolean batchmode){
			if(writer != null){
				return writer;
			}
			
			try {
				//TODO 这个可能出错
				writer = createNewIndexWriter(ConfigBean.getInstanse().getDirectory(),batchWriterConfig);
			} catch (IOException e) {
				throw new SearchException(e);
			}
			return writer;
		} 
		
		private IndexWriter createNewIndexWriter(DirectoryProvider<?> directoryProvider, IndexWriterConfig writerConfig) throws IOException {
			LogByteSizeMergePolicy newMergePolicy = new LogByteSizeMergePolicy(); 
			writerConfig.setMergePolicy( newMergePolicy );
			MergeScheduler mergeScheduler = new org.apache.lucene.index.ConcurrentMergeScheduler();
			writerConfig.setMergeScheduler( mergeScheduler );
			IndexWriter writer = new IndexWriter( directoryProvider.getDirectory(), writerConfig );
			return writer;
		}
	}
	
	static class SearchClassPathScanner extends ClassPathBeanDefinitionScanner{
		
		private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

		private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
		
		private final BeanDefinitionRegistry registry;

		public SearchClassPathScanner(BeanDefinitionRegistry registry,boolean useDefaultFilters) {
			super(registry, useDefaultFilters);
			this.registry = registry;
		}
		
		public SearchClassPathScanner(BeanDefinitionRegistry registry){
			super(registry,false);
			this.registry = registry;
		}
		
		@SuppressWarnings({ "unchecked", "restriction" })
		public int scan(String... basePackages) {
			Assert.notEmpty(basePackages, "不能为空");
			int beanCountAtScanStart = this.registry.getBeanDefinitionCount();

			Set<BeanDefinitionHolder> resources = doScan(basePackages);

			for(BeanDefinitionHolder bdh:resources){
				String className = bdh.getBeanDefinition().getBeanClassName();
				Class<?> clazz;
				try {
					clazz = Class.forName(className);
				} catch (ClassNotFoundException e) {
					throw new SearchException("实体类找不到"+className);
				}
				Table tableAnn = (Table) clazz.getAnnotation(Table.class);
				if(tableAnn == null){
					throw new SearchException("没有 @Table 注解");
				}
				DocumentBuilderIndexedEntity<?> indexEntityBuilder = new DocumentBuilderIndexedEntity(clazz, ConfigBean.getInstanse());
				BuildContext.class2IndexedEntites.put(clazz, indexEntityBuilder);
				BuildContext.class2TableName.put(clazz, tableAnn.name());
			}
			
			return this.registry.getBeanDefinitionCount() - beanCountAtScanStart;
		}
		
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

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		scanner = new SearchClassPathScanner((BeanDefinitionRegistry) beanFactory, false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Index.class));
	}

	public void setLuncenePropertyHome(String luncenePropertyHome) {
		this.luncenePropertyHome = luncenePropertyHome;
	}

	public String getLuncenePropertyHome() {
		return luncenePropertyHome;
	}
}
