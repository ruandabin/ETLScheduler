package top.ruandb.service;

import top.ruandb.entity.OdrProLog;

/**
 * odr存储日志service
 * @author rdb
 *
 */
public interface OdrProLogService {

	public OdrProLog getLastedOdrProLog(String proName) ;
	
	public OdrProLog getLastedOracleOdrProLog(String proName) ;
	
	public OdrProLog getLastedSqlserverOdrProLog(String proName);
}
