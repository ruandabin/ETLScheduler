package top.ruandb.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class TestController {

	//private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("/test01")
	public String test01() {
		//logger.info("sss");
		return "admin/main";
		//return "login";
	}
	
	
}
