package top.ruandb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import top.ruandb.entity.TaskCronJob;
/**
 * CronJob Repository层
 * @author rdb
 *
 */
@Repository
public interface TaskCronJobRepository extends JpaRepository<TaskCronJob,Long>,JpaSpecificationExecutor<TaskCronJob>{

	@Query(value="select * from TASK_CRON_JOB where job_group in( select t.job_group from TASK_CRON_JOB t where t.id = ?1) and id <> ?1 AND enabled = 1", nativeQuery = true)
	List<TaskCronJob> findGroupPro(Long id);
	
	
	@Query(value="select * from TASK_CRON_JOB where job_group in( select t.job_group from TASK_CRON_JOB t where t.job_group = ?1) AND enabled = 1", nativeQuery = true)
	List<TaskCronJob> findGroupProAll(String  jobGroup);
}
