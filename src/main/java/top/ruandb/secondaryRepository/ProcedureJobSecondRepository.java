package top.ruandb.secondaryRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.serializer.ObjectArrayCodec;

import top.ruandb.config.GreenPlumConfig;
import top.ruandb.secondaryEntity.OdrProLog;

@Repository
public class ProcedureJobSecondRepository {
	
	@Autowired
	@Qualifier("jdbcTemplateSecondary")
	private JdbcTemplate jdbcTemplate;
	
	
	public void runSql(String sql) throws Exception{
		jdbcTemplate.execute(sql);
	}
	
	public OdrProLog getOneOdrProLog(String proName) {
		
		String sql= "select id,pro_status,errormessage from "+GreenPlumConfig.GP_SCHEMA+"PRODUCURE_LOG where id in( SELECT max(id) FROM "+GreenPlumConfig.GP_SCHEMA+"PRODUCURE_LOG where pro_name = lower(?) or pro_name = upper(?))";
		//String sql="select id,pro_status,errormessage from hospital_cubedb.PRODUCURE_LOG where id=10 and pro_name = lower(?)  ";
		/*List<OdrProLog> odrProLogs = jdbcTemplate.queryForList(sql.toString(), new Object[]{proName,proName}, (rs, rowNum) -> {
			OdrProLog temp = new OdrProLog();
			if(rowNum == 1) {
				temp.setId(rs.getLong("id"));
	            temp.setProStatus(rs.getString("pro_status"));
	            temp.setErrorMessage(rs.getString("errormessage"));
			}
            
            return temp;
        });*/
		
		RowMapper<OdrProLog> rowMapper = new BeanPropertyRowMapper<>(OdrProLog.class);
		List<OdrProLog> odrProLogs = jdbcTemplate.query(sql,rowMapper,proName,proName);
		if(odrProLogs.size()>0) {
			return  odrProLogs.get(0);
		}else {
			return  null;
		}

		
		
	}

}
