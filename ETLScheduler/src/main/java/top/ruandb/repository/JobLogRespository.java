package top.ruandb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import top.ruandb.entity.JobLog;

@Repository
public interface JobLogRespository extends JpaRepository<JobLog,Long>,JpaSpecificationExecutor<JobLog>{

}
