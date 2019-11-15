package top.ruandb.config;

import java.util.Properties;
import javax.annotation.Resource;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 调度器工厂
 * 
 * @author rdb
 *
 */
@Configuration
public class TaskSchedulerFactory {

	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private volatile Scheduler scheduler;

	@Resource
	private QuartzConfig quartzConfig;
	
	 @Autowired
	 private MyJobFactory myJobFactory;

	// 获取scheduler实例
	public Scheduler getSchduler()  {
		Scheduler s = scheduler;
		if (s == null) {
			synchronized (this) {
				s = scheduler;
				if (s == null) {
					SchedulerFactoryBean sf;
					sf = new SchedulerFactoryBean();
							//StdSchedulerFactory(getQuartzProperties());
					sf.setQuartzProperties(getQuartzProperties());
					sf.setJobFactory(myJobFactory);
					s = sf.getScheduler();
				}
			}
		}
		return s;
	}

	//@Bean
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
}
