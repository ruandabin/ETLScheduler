package top.ruandb.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ruandb.exception.ParamException;
/**
 * 全局异常处理
 * @author rdb
 *
 */
@ControllerAdvice
public class ExceptionResolver {
	private  final Logger logger = LoggerFactory.getLogger(getClass());
	static final String DEFAULT_MSG = "SYSTEM ERROR";
	
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<String,Object> resolveException (Exception e){
		Map<String, Object> resultMap = new HashMap<>();
		if( e instanceof ParamException) {
			resultMap.put("success", false);
			resultMap.put("msg",e.getMessage());
			
		}else{
			logger.error("unknown exception", e);
			resultMap.put("success", false);
			resultMap.put("msg",DEFAULT_MSG);
		}
		return resultMap ;
	}
}
