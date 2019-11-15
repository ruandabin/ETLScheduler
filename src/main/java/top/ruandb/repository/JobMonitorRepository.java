package top.ruandb.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import top.ruandb.entity.JobMonitor;
@Repository
public interface JobMonitorRepository extends JpaRepository<JobMonitor,Long>,JpaSpecificationExecutor<JobMonitor>{

	/**
	 * 初始化主键
	 * @param userId
	 */
	@Transactional
	@Query(value="UPDATE TB_GENERATOR  SET gen_value = (SELECT case when MAX(id) is null then 0 else MAX(id) end FROM TASK_CRON_JOB) WHERE gen_name = 'TASK_CRON_JOB_PK'",nativeQuery = true)
	@Modifying
	public void initTaskCronJobPk();
	
	/**
	 * 初始化主键
	 * @param userId
	 */
	@Transactional
	@Query(value="UPDATE TB_GENERATOR  SET gen_value = (SELECT case when MAX(id) is null then 0 else MAX(id) end FROM JOB_LOG) WHERE gen_name = 'JOB_LOG_PK'",nativeQuery = true)
	@Modifying
	public void initJobLogPk();
}


