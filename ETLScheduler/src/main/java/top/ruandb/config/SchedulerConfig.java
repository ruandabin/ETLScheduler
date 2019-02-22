package top.ruandb.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig {

	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private QuartzConfig quartzConfig;
	
	@Resource
	private MyJobFactory myJobFactory;

	@Bean(name = "SchedulerFactory")
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setQuartzProperties(getQuartzProperties());
		factory.setJobFactory(myJobFactory);
		return factory;
	}

	@Bean
	public Properties getQuartzProperties() {
		Properties prop = new Properties();
		prop.put("quartz.scheduler.instanceName", quartzConfig.getQuartzSchedulerInstanceName());
		prop.put("org.quartz.scheduler.instanceId", quartzConfig.getQuartzSchedulerInstanceId());
		prop.put("org.quartz.scheduler.skipUpdateCheck", quartzConfig.getQuartzSchedulerSkipUpdateCheck());
		prop.put("org.quartz.scheduler.jobFactory.class", quartzConfig.getQuartzSchedulerJobFactoryClass());
		prop.put("org.quartz.jobStore.class", quartzConfig.getQuartzJobStoreClass());
		prop.put("org.quartz.jobStore.driverDelegateClass", quartzConfig.getQuartzJobStoreDriverDelegateClass());
		prop.put("org.quartz.jobStore.dataSource", quartzConfig.getQuartzJobStoreDatasource());
		prop.put("org.quartz.jobStore.tablePrefix", quartzConfig.getQuartzJobStoreTablePrefix());
		prop.put("org.quartz.jobStore.isClustered", quartzConfig.getQuartzJobStoreIsClustered());
		prop.put("org.quartz.threadPool.class", quartzConfig.getQuartzThreadPoolClass());
		prop.put("org.quartz.threadPool.threadCount", quartzConfig.getQuartzThreadPoolThreadCount());
		prop.put("org.quartz.dataSource.quartzDataSource.connectionProvider.class",
				quartzConfig.getQuartzDatasourceQuartzDataSourceConnectionProviderClass());
		prop.put("org.quartz.dataSource.quartzDataSource.driver",
				quartzConfig.getQuartzDatasourceQuartzDataSourceDriver());
		prop.put("org.quartz.dataSource.quartzDataSource.URL", quartzConfig.getQuartzDatasourceQuartzDataSourceUrl());
		prop.put("org.quartz.dataSource.quartzDataSource.user", quartzConfig.getQuartzDatasourceQuartzDataSourceUser());
		prop.put("org.quartz.dataSource.quartzDataSource.password",
				quartzConfig.getQuartzDatasourceQuartzDataSourcePassword());
		prop.put("org.quartz.dataSource.quartzDataSource.maxConnections",
				quartzConfig.getQuartzDatasourceQuartzDataSourceMaxConnections());

		return prop;
	}

	/*
	 * quartz初始化监听器
	 * 
	 * @Bean public QuartzInitializerListener executorListener() { return new
	 * QuartzInitializerListener(); }
	 */

	/*
	 * 通过SchedulerFactoryBean获取Scheduler的实例
	 */
	@Bean(name = "Scheduler")
	public Scheduler scheduler() throws IOException {
		return schedulerFactoryBean().getScheduler();
	}

}
