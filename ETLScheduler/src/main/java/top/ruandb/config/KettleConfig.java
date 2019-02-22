package top.ruandb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * kettle的一些变量配置
 * @author rdb
 *
 */
@Component
public class KettleConfig {

	/**
	 * kettle的一些常量配置
	 */
	
	public static String KETTLE_HOME ;
	
	public static String KETTLE_JOB_DIR ;
	
	public static String KETTLE_LOG_DB_NAME;
	
	public static String KETTLE_LOG_DB_TYPE;
	
	public static String KETTLE_LOG_DB_ACCESS;
	
	public static String KETTLE_LOG_DB_HOST;
	
	public static String KETTLE_LOG_DB_DB;
	
	public static String KETTLE_LOG_DB_PORT;
	
	public static String KETTLE_LOG_DB_USER;
	
	public static String KETTLE_LOG_DB_PASS;
	
	@Value("${kettleConfig.kettle_home}")
	public  void setKETTLE_HOME(String kETTLE_HOME) {
		KETTLE_HOME = kETTLE_HOME;
	}
	
	@Value("${kettleConfig.kettle_job_dir}")
	public  void setKETTLE_JOB_DIR(String kETTLE_JOB_DIR) {
		KETTLE_JOB_DIR = kETTLE_JOB_DIR;
	}
	
	@Value("${kettleConfig.kettle_log_db_name}")
	public  void setKETTLE_LOG_DB_NAME(String kETTLE_LOG_DB_NAME) {
		KETTLE_LOG_DB_NAME = kETTLE_LOG_DB_NAME;
	}

	@Value("${kettleConfig.kettle_log_db_type}")
	public  void setKETTLE_LOG_DB_TYPE(String kETTLE_LOG_DB_TYPE) {
		KETTLE_LOG_DB_TYPE = kETTLE_LOG_DB_TYPE;
	}

	@Value("${kettleConfig.kettle_log_db_access}")
	public  void setKETTLE_LOG_DB_ACCESS(String kETTLE_LOG_DB_ACCESS) {
		KETTLE_LOG_DB_ACCESS = kETTLE_LOG_DB_ACCESS;
	}

	@Value("${kettleConfig.kettle_log_db_host}")
	public  void setKETTLE_LOG_DB_HOST(String kETTLE_LOG_DB_HOST) {
		KETTLE_LOG_DB_HOST = kETTLE_LOG_DB_HOST;
	}

	@Value("${kettleConfig.kettle_log_db_db}")
	public  void setKETTLE_LOG_DB_DB(String kETTLE_LOG_DB_DB) {
		KETTLE_LOG_DB_DB = kETTLE_LOG_DB_DB;
	}

	@Value("${kettleConfig.kettle_log_db_port}")
	public  void setKETTLE_LOG_DB_PORT(String kETTLE_LOG_DB_PORT) {
		KETTLE_LOG_DB_PORT = kETTLE_LOG_DB_PORT;
	}

	@Value("${kettleConfig.kettle_log_db_user}")
	public  void setKETTLE_LOG_DB_USER(String kETTLE_LOG_DB_USER) {
		KETTLE_LOG_DB_USER = kETTLE_LOG_DB_USER;
	}

	@Value("${kettleConfig.kettle_log_db_pass}")
	public  void setKETTLE_LOG_DB_PASS(String kETTLE_LOG_DB_PASS) {
		KETTLE_LOG_DB_PASS = kETTLE_LOG_DB_PASS;
	}
	
	
}
