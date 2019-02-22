package top.ruandb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QuartzConfig {

	@Value("${quartzConfig.quartz.scheduler.instance-name}")
	private String quartzSchedulerInstanceName;
	@Value("${quartzConfig.quartz.scheduler.instance-id}")
	private String quartzSchedulerInstanceId;
	@Value("${quartzConfig.quartz.scheduler.skip-update-check}")
	private String quartzSchedulerSkipUpdateCheck;
	@Value("${quartzConfig.quartz.scheduler.job-factory.class}")
	private String quartzSchedulerJobFactoryClass;
	@Value("${quartzConfig.quartz.job-store.class}")
	private String quartzJobStoreClass;
	@Value("${quartzConfig.quartz.job-store.driver-delegate-class}")
	private String quartzJobStoreDriverDelegateClass;
	@Value("${quartzConfig.quartz.job-store.datasource}")
	private String quartzJobStoreDatasource;
	@Value("${quartzConfig.quartz.job-store.table-prefix}")
	private String quartzJobStoreTablePrefix;
	@Value("${quartzConfig.quartz.job-store.is-clustered}")
	private String quartzJobStoreIsClustered;
	@Value("${quartzConfig.quartz.thread-pool.class}")
	private String quartzThreadPoolClass;
	@Value("${quartzConfig.quartz.thread-pool.thread-count}")
	private String quartzThreadPoolThreadCount;

	@Value("${quartzConfig.quartz.datasource.quartzDataSource.driver}")
	private String quartzDatasourceQuartzDataSourceDriver;
	@Value("${quartzConfig.quartz.datasource.quartzDataSource.url}")
	private String quartzDatasourceQuartzDataSourceUrl;
	@Value("${quartzConfig.quartz.datasource.quartzDataSource.user}")
	private String quartzDatasourceQuartzDataSourceUser;
	@Value("${quartzConfig.quartz.datasource.quartzDataSource.password}")
	private String quartzDatasourceQuartzDataSourcePassword;
	@Value("${quartzConfig.quartz.datasource.quartzDataSource.maxConnections}")
	private String quartzDatasourceQuartzDataSourceMaxConnections;
	@Value("${quartzConfig.quartz.datasource.quartzDataSource.connection-provider.class}")
	private String quartzDatasourceQuartzDataSourceConnectionProviderClass;

	public String getQuartzSchedulerInstanceName() {
		return quartzSchedulerInstanceName;
	}

	public void setQuartzSchedulerInstanceName(String quartzSchedulerInstanceName) {
		this.quartzSchedulerInstanceName = quartzSchedulerInstanceName;
	}

	public String getQuartzSchedulerInstanceId() {
		return quartzSchedulerInstanceId;
	}

	public void setQuartzSchedulerInstanceId(String quartzSchedulerInstanceId) {
		this.quartzSchedulerInstanceId = quartzSchedulerInstanceId;
	}

	public String getQuartzSchedulerSkipUpdateCheck() {
		return quartzSchedulerSkipUpdateCheck;
	}

	public void setQuartzSchedulerSkipUpdateCheck(String quartzSchedulerSkipUpdateCheck) {
		this.quartzSchedulerSkipUpdateCheck = quartzSchedulerSkipUpdateCheck;
	}

	public String getQuartzSchedulerJobFactoryClass() {
		return quartzSchedulerJobFactoryClass;
	}

	public void setQuartzSchedulerJobFactoryClass(String quartzSchedulerJobFactoryClass) {
		this.quartzSchedulerJobFactoryClass = quartzSchedulerJobFactoryClass;
	}

	public String getQuartzJobStoreClass() {
		return quartzJobStoreClass;
	}

	public void setQuartzJobStoreClass(String quartzJobStoreClass) {
		this.quartzJobStoreClass = quartzJobStoreClass;
	}

	public String getQuartzJobStoreDriverDelegateClass() {
		return quartzJobStoreDriverDelegateClass;
	}

	public void setQuartzJobStoreDriverDelegateClass(String quartzJobStoreDriverDelegateClass) {
		this.quartzJobStoreDriverDelegateClass = quartzJobStoreDriverDelegateClass;
	}

	public String getQuartzJobStoreDatasource() {
		return quartzJobStoreDatasource;
	}

	public void setQuartzJobStoreDatasource(String quartzJobStoreDatasource) {
		this.quartzJobStoreDatasource = quartzJobStoreDatasource;
	}

	public String getQuartzJobStoreTablePrefix() {
		return quartzJobStoreTablePrefix;
	}

	public void setQuartzJobStoreTablePrefix(String quartzJobStoreTablePrefix) {
		this.quartzJobStoreTablePrefix = quartzJobStoreTablePrefix;
	}

	public String getQuartzJobStoreIsClustered() {
		return quartzJobStoreIsClustered;
	}

	public void setQuartzJobStoreIsClustered(String quartzJobStoreIsClustered) {
		this.quartzJobStoreIsClustered = quartzJobStoreIsClustered;
	}

	public String getQuartzThreadPoolClass() {
		return quartzThreadPoolClass;
	}

	public void setQuartzThreadPoolClass(String quartzThreadPoolClass) {
		this.quartzThreadPoolClass = quartzThreadPoolClass;
	}

	public String getQuartzThreadPoolThreadCount() {
		return quartzThreadPoolThreadCount;
	}

	public void setQuartzThreadPoolThreadCount(String quartzThreadPoolThreadCount) {
		this.quartzThreadPoolThreadCount = quartzThreadPoolThreadCount;
	}

	public String getQuartzDatasourceQuartzDataSourceDriver() {
		return quartzDatasourceQuartzDataSourceDriver;
	}

	public void setQuartzDatasourceQuartzDataSourceDriver(String quartzDatasourceQuartzDataSourceDriver) {
		this.quartzDatasourceQuartzDataSourceDriver = quartzDatasourceQuartzDataSourceDriver;
	}

	public String getQuartzDatasourceQuartzDataSourceUrl() {
		return quartzDatasourceQuartzDataSourceUrl;
	}

	public void setQuartzDatasourceQuartzDataSourceUrl(String quartzDatasourceQuartzDataSourceUrl) {
		this.quartzDatasourceQuartzDataSourceUrl = quartzDatasourceQuartzDataSourceUrl;
	}

	public String getQuartzDatasourceQuartzDataSourceUser() {
		return quartzDatasourceQuartzDataSourceUser;
	}

	public void setQuartzDatasourceQuartzDataSourceUser(String quartzDatasourceQuartzDataSourceUser) {
		this.quartzDatasourceQuartzDataSourceUser = quartzDatasourceQuartzDataSourceUser;
	}

	public String getQuartzDatasourceQuartzDataSourcePassword() {
		return quartzDatasourceQuartzDataSourcePassword;
	}

	public void setQuartzDatasourceQuartzDataSourcePassword(String quartzDatasourceQuartzDataSourcePassword) {
		this.quartzDatasourceQuartzDataSourcePassword = quartzDatasourceQuartzDataSourcePassword;
	}

	public String getQuartzDatasourceQuartzDataSourceMaxConnections() {
		return quartzDatasourceQuartzDataSourceMaxConnections;
	}

	public void setQuartzDatasourceQuartzDataSourceMaxConnections(
			String quartzDatasourceQuartzDataSourceMaxConnections) {
		this.quartzDatasourceQuartzDataSourceMaxConnections = quartzDatasourceQuartzDataSourceMaxConnections;
	}

	public String getQuartzDatasourceQuartzDataSourceConnectionProviderClass() {
		return quartzDatasourceQuartzDataSourceConnectionProviderClass;
	}

	public void setQuartzDatasourceQuartzDataSourceConnectionProviderClass(
			String quartzDatasourceQuartzDataSourceConnectionProviderClass) {
		this.quartzDatasourceQuartzDataSourceConnectionProviderClass = quartzDatasourceQuartzDataSourceConnectionProviderClass;
	}
}
