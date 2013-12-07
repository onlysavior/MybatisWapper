package onlysavior.mybatis.common.context;

import onlysavior.mybatis.common.util.EntityInfoHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * the context of all the entity
 */
public class EntityContext implements ApplicationContextAware,InitializingBean {
    private ApplicationContext context;
    private ClassPathBeanDefinitionScanner scanner;
    private String basePath;

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.scanner = new PathScaner((BeanDefinitionRegistry)context,true);
        this.scanner.setResourceLoader(context);
        this.scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
    }

    protected void doCheck(BeanDefinitionHolder beanDefinitionHolder) {
        Class clz = beanDefinitionHolder.getClass();
        checkTableName(clz);
        checkIdName(clz);
    }

    private void checkTableName(Class clz) {
        Table tableAnnotation = (Table)clz.getAnnotation(Table.class);
        if(tableAnnotation == null) {
            throw new IllegalArgumentException("@Table not found,class :"+clz);
        }
        getTableNames().addClass(clz, tableAnnotation.name());
    }

    private void checkIdName(Class clz) {
        Field[] fields = clz.getDeclaredFields();
        for(Field field : fields) {
            Id idAnnotation = field.getAnnotation(Id.class);
            if(idAnnotation != null) {
                //the EntityInfoHolder can throw exception
                //when meet depuliate key
                getIdNames().addClass(clz, field.getName());
            }
        }

        throw new AssertionError("unreachable code");
    }

    class PathScaner extends ClassPathBeanDefinitionScanner {
        private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
        private final BeanDefinitionRegistry registry;

        public PathScaner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
            super(registry, useDefaultFilters);
            this.registry = registry;
        }

        @Override
        public int scan(String... basePackages) {
            int beanCountAtScanStart = this.registry.getBeanDefinitionCount();
            Set<BeanDefinitionHolder> resources = doScan(basePackages);
            for(BeanDefinitionHolder bdh:resources){
                doCheck(bdh);
            }
            return this.registry.getBeanDefinitionCount() - beanCountAtScanStart;
        }

        @Override
        protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
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

    private EntityInfoHolder tableNames = new EntityInfoHolder("tableName");
    private EntityInfoHolder idNames = new EntityInfoHolder("idName");

    public EntityInfoHolder getTableNames() {
        return tableNames;
    }

    public EntityInfoHolder getIdNames() {
        return idNames;
    }
}
