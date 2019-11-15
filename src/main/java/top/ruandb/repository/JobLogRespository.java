package top.ruandb.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.ruandb.entity.JobLog;

@Repository
public interface JobLogRespository extends JpaRepository<JobLog,Long>,JpaSpecificationExecutor<JobLog>{

	
	//@Procedure()
	@Transactional
	@Modifying
	@Query(value = "exec HDW_SK.dbo.sp_create_tmp_table :tableName ", nativeQuery = true)
	void RunProcedure(@Param("tableName") String tableName);
	
	
	/**
	 * 修改成已经查看状态
	 * @param userId
	 */
	@Transactional
	@Query(value="UPDATE JobLog xe SET xe.isDeal=:isDeal WHERE id=:id")
	@Modifying
	public void updateIsDeal(@Param("id")Long id,@Param("isDeal")String idDeal);
	
	
	/**
	 * 日志查询优化，列表查询时不查询log_text
	 */
	@Query(value = "SELECT id,jobname,status,errors,startdate,enddate,is_deal,'' as logtext FROM(SELECT id,enddate,errors,is_deal,jobname,startdate,status, ROWNUM RN FROM (SELECT id,enddate,errors,is_deal,jobname,startdate,status FROM job_log order by id desc) A WHERE ROWNUM <= ?1)WHERE RN > ?2", nativeQuery = true)
	public List<JobLog> getLogList(Integer rowNum, Integer rn);
	
	/**
	 * 日志查询优化，列表查询时不查询log_text,带模糊查询
	 */
	@Query(value = "SELECT id,jobname,status,errors,startdate,enddate,is_deal,'' as logtext FROM(SELECT id,enddate,errors,is_deal,jobname,startdate,status, ROWNUM RN FROM (SELECT id,enddate,errors,is_deal,jobname,startdate,status FROM job_log  where (jobname like ?3 OR ?3 is null) and  (status like ?4 OR ?4 is null ) order by id desc) A WHERE ROWNUM <= ?1)WHERE RN > ?2", nativeQuery = true)
	public List<JobLog> getLogList(Integer rowNum, Integer rn, String jobName, String status);
	
	

}
