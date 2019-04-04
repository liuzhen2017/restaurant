/**
 * 
 */
package com.menu.manger.config;


import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.menu.manger.filter.CFXEndFiter;
import com.menu.manger.filter.CFXFiter;

import storelle.api.pos.IPosTransaction;

/**
 * @author liuzhen
 *
 */
@Configuration
public class SoapConfig {
	@Autowired
    private IPosTransaction userService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public ServletRegistrationBean soapServlet() {
		return new ServletRegistrationBean(new CXFServlet(), "/storelle/*");
	}
	
	
	@Autowired
    @Qualifier(Bus.DEFAULT_BUS_ID)
    private SpringBus bus;
	@Bean(name = Bus.DEFAULT_BUS_ID)
	public SpringBus springBus() {
		return new SpringBus();
	}
	
	//此处要注意导入正取的Endpoint、EndpointImpl包
	 @Bean
	    public Endpoint endpoint() {
	        EndpointImpl endpoint = new EndpointImpl(springBus(), userService);
	        endpoint.getInInterceptors().add(new CFXFiter());
	        endpoint.getInInterceptors().add(new LoggingInInterceptor()) ;
	        endpoint.getOutInterceptors().add(new CFXEndFiter()) ;
	        endpoint.getOutInterceptors().add(new LoggingInInterceptor()) ;
	        endpoint.publish("/api");
	        return  endpoint;
	    }
}
