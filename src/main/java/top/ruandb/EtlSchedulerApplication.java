package top.ruandb;

import java.io.File;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EtlSchedulerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(EtlSchedulerApplication.class, args);
	}
	
	

	
	

}

