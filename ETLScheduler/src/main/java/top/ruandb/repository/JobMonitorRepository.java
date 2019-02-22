package top.ruandb.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import top.ruandb.entity.JobMonitor;
@Repository
public interface JobMonitorRepository extends JpaRepository<JobMonitor,Long>,JpaSpecificationExecutor<JobMonitor>{

}
