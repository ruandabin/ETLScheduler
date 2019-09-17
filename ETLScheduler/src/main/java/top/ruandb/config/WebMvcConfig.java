package top.ruandb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import top.ruandb.filter.LoginInterceptor;

//@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	 @Override
	 public void addInterceptors(InterceptorRegistry registry) {
		 
		 InterceptorRegistration registration = registry.addInterceptor(loginInterceptor());
		 registration.addPathPatterns("/**");
		 registration.excludePathPatterns("/","/login","/error","/static/**","/logout","/user/login");  
	 }
	 
	 public LoginInterceptor loginInterceptor() {
		 return new LoginInterceptor();
	 }
	 
}
