package top.ruandb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import top.ruandb.entity.OdrProLog;

public interface OdrProLogRepository extends JpaRepository<OdrProLog,Long>,JpaSpecificationExecutor<OdrProLog>{

	@Query(value="select id,pro_status,errormessage from HOSPITAL_CUBEDB.PRODUCURE_LOG where id in( SELECT max(id) FROM HOSPITAL_CUBEDB.PRODUCURE_LOG where pro_name = ?1)", nativeQuery = true)
	public OdrProLog getLatestOdrProLog(String proName);
	
	@Query(value="select id,pro_status,errormessage from HOSPITAL_CUBEDB.PRODUCURE_LOG where id in( SELECT max(id) FROM HOSPITAL_CUBEDB.PRODUCURE_LOG where pro_name = lower(?1) or pro_name = upper(?1))", nativeQuery = true)
	public OdrProLog getLatestOracleOdrProLog(String proName);
	
	
	@Query(value="select id,pro_status,errormessage from HOSPITAL_CUBEDB.dbo.PRODUCURE_LOG where id in( SELECT max(id) FROM HOSPITAL_CUBEDB.dbo.PRODUCURE_LOG where pro_name = lower(?1) or pro_name = upper(?1))", nativeQuery = true)
	public OdrProLog getLatestSqlserverOdrProLog(String proName);
}
