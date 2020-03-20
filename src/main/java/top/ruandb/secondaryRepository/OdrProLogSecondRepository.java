package top.ruandb.secondaryRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.ruandb.secondaryEntity.OdrProLog;


@Repository
public interface OdrProLogSecondRepository extends JpaRepository<OdrProLog,Long>,JpaSpecificationExecutor<OdrProLog>{
	
	
	
	@Query(value="select id,pro_status,errormessage from hospital_cubedb.PRODUCURE_LOG where id in( SELECT max(id) FROM hospital_cubedb.PRODUCURE_LOG where pro_name = lower(?1) or pro_name = upper(?1))", nativeQuery = true)
	public OdrProLog getLatestOdrProLog(String proName);
}
