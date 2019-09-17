package top.ruandb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.ruandb.entity.OdrProLog;
import top.ruandb.repository.OdrProLogRepository;
import top.ruandb.service.OdrProLogService;
/**
 * 判断Odr存储日志service
 * @author rdb
 *
 */
@Service
public class OdrProLogServiceImpl implements OdrProLogService{
	
	@Autowired
	private OdrProLogRepository odrProLogRepository;

	@Override
	public OdrProLog getLastedOdrProLog(String proName) {
		return odrProLogRepository.getLatestOdrProLog(proName);
	}

	@Override
	public OdrProLog getLastedOracleOdrProLog(String proName) {
		return odrProLogRepository.getLatestOracleOdrProLog(proName);
	}

	@Override
	public OdrProLog getLastedSqlserverOdrProLog(String proName) {
		return odrProLogRepository.getLatestSqlserverOdrProLog(proName);
	}

	

}
