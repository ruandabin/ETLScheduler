package top.ruandb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GreenPlumConfig {

	public static String GP_SCHEMA ;


	@Value("${greenPlumSchema}")
	public  void setGP_SCHEMA(String gP_SCHEMA) {
		GP_SCHEMA = gP_SCHEMA;
	}
	
	
}
