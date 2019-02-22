package top.ruandb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import top.ruandb.entity.TaskCronJob;
/**
 * CronJob Repository层
 * @author rdb
 *
 */
@Repository
public interface TaskCronJobRepository extends JpaRepository<TaskCronJob,Long>,JpaSpecificationExecutor<TaskCronJob>{

}
