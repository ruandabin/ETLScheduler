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

	public static String PROCEDURE_PRO ;

	@Value("${ProcedurePro}")
	public static void setPROCEDURE_PRO(String pROCEDURE_PRO) {
		PROCEDURE_PRO = pROCEDURE_PRO;
	}
	
	
}
