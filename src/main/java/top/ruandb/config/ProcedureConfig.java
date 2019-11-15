package top.ruandb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * 存储过程的一些变量配置
 * @author rdb
 *
 */
@Component
public class ProcedureConfig {

	/*public static String PROCEDURE_PRO ;

	@Value("${ProcedurePro}")
	public static void setPROCEDURE_PRO(String pROCEDURE_PRO) {
		PROCEDURE_PRO = pROCEDURE_PRO;
	}*/
	
	public static  String SQLSERVER_PROCEDURE_PRO;
	
	public static  String ORACLE_PROCEDURE_PRO;

	@Value("${sqlserverProcedurePro}")
	public static void setSQLSERVER_PROCEDURE_PRO(String sQLSERVER_PROCEDURE_PRO) {
		SQLSERVER_PROCEDURE_PRO = sQLSERVER_PROCEDURE_PRO;
	}

	@Value("${oracleProcedurePro}")
	public static void setORACLE_PROCEDURE_PRO(String oRACLE_PROCEDURE_PRO) {
		ORACLE_PROCEDURE_PRO = oRACLE_PROCEDURE_PRO;
	}
	
	
	
	
}
