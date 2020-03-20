package top.ruandb.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 * 调度存储过程Job
 * 
 * @author rdb
 *
 */
@Repository
public class ProcedureJobRepository {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void runSqlserverPro(String sql) throws Exception {
		jdbcTemplate.execute(sql);
	}
	
	public void runSqlserverPro(String sql, final String parm) {
		jdbcTemplate.execute(sql, new CallableStatementCallback<Void>() {
			@Override
			public Void doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				if(parm != null && !parm.equals("")) {
					cs.setInt(1, Integer.parseInt(parm));
				}
				cs.execute();
				return null;
			}
		});
	}

	public void runOraclePro(String sql, String parm) throws Exception {
		
		jdbcTemplate.execute(sql, new CallableStatementCallback<Void>() {
			@Override
			public Void doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				if(parm != null && !parm.equals("")) {
					cs.setInt(1, Integer.parseInt(parm));
				}
				cs.execute();
				return null;
			}
		});

	}
	
	public void runGreenPlumPro(String sql,String param) {
		
	}

}
