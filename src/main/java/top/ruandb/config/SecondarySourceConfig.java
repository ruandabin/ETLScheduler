package top.ruandb.config;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactorySecondary", transactionManagerRef = "transactionManagerPrimary", basePackages = {
		"top.ruandb.secondaryRepository" })
public class SecondarySourceConfig {

	@Autowired
	@Qualifier("secondaryDataSource")
	private DataSource secondaryDataSource;
	
	@Autowired
	private Properties jpaProperties;
	
	/**
     * JdbcTemplate 支持
     * @return
     */
    @Bean(name = "jdbcTemplateSecondary")
    public JdbcTemplate primaryJdbcTemplate() {
        return new JdbcTemplate(secondaryDataSource);
    }
    
    /**
	 * 设置实体类所在位置
	 * 对@Primary修饰的LocalContainerEntityManagerFactoryBean可以不用指定TransactionManager，
	 * spring上下文自动使用默认的JpaTransactionManager，但是对于第二个或第三个等等必须指定TransactionManager，笔者在实测过程中却发现即使是@Primar修饰的也得指定TransactionManager
	 */
	@Bean(name = "entityManagerFactorySecondary")
	public LocalContainerEntityManagerFactoryBean entityManagerFactorySecondary(EntityManagerFactoryBuilder builder) {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = builder.dataSource(secondaryDataSource)
				.packages("top.ruandb.secondaryEntity").persistenceUnit("primaryPersistenceUnit").build();
		entityManagerFactory.setJpaProperties(jpaProperties);
		return entityManagerFactory;
	}
	

	@Bean(name = "entityManagerSecondary")
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return entityManagerFactorySecondary(builder).getObject().createEntityManager();
	}
	

	@Bean(name = "transactionManagerSecondary")
	public PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactorySecondary(builder).getObject());
	}
}
