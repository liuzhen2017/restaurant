 package com.menu.manger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.menu.manger.filter.CorsFilter;
import com.menu.manger.filter.LoginFilter;

@SpringBootApplication
//開啓緩存注解
//@EnableCaching
@ComponentScan("com.*,storelle.*")
public class MangerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MangerApplication.class, args);
		System.out.println("启动成功!");
		//SET GLOBAL time_zone='+8:00'; // 數據庫執行
	}
	@Bean
	public FilterRegistrationBean<LoginFilter> testFilterRegistration() {
		FilterRegistrationBean<LoginFilter> registration = new FilterRegistrationBean<LoginFilter>(new LoginFilter());
		registration.addUrlPatterns("/api/*"); //
		registration.addInitParameter("paramName", "paramValue"); //
		registration.setName("loginFilter");
		registration.setOrder(2);
		return registration;
	}
 
	@Bean
	public FilterRegistrationBean<CorsFilter> authFilterRegistration() {
		FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<CorsFilter>(new CorsFilter());
		registration.addUrlPatterns("/api/*"); //api
		registration.addInitParameter("paramName", "paramValue"); //
		registration.setName("myCorsFilter");
		registration.setOrder(1);
		return registration;
	}
}

