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
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryPrimary", transactionManagerRef = "transactionManagerPrimary", basePackages = {
		"top.ruandb.repository" })
public class PrimarySourceConfig {

	@Autowired
	@Qualifier("primaryDataSource")
	private DataSource primaryDataSource;
	
	@Autowired
	private Properties jpaProperties;
	
	/**
     * JdbcTemplate 支持
     * @return
     */
    @Primary
    @Bean(name = "jdbcTemplatePrimary")
    public JdbcTemplate primaryJdbcTemplate() {
        return new JdbcTemplate(primaryDataSource);
    }


	/**
	 * 设置实体类所在位置
	 * 对@Primary修饰的LocalContainerEntityManagerFactoryBean可以不用指定TransactionManager，
	 * spring上下文自动使用默认的JpaTransactionManager，但是对于第二个或第三个等等必须指定TransactionManager，笔者在实测过程中却发现即使是@Primar修饰的也得指定TransactionManager
	 */
	@Primary
	@Bean(name = "entityManagerFactoryPrimary")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = builder.dataSource(primaryDataSource)
				.packages("top.ruandb.entity").persistenceUnit("primaryPersistenceUnit").build();
		entityManagerFactory.setJpaProperties(jpaProperties);
		return entityManagerFactory;
	}
	
	@Primary
	@Bean(name = "entityManagerPrimary")
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
	}
	
	@Primary
	@Bean(name = "transactionManagerPrimary")
	public PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
	}
}
