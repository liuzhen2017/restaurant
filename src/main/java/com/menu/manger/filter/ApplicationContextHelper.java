/**
 * 
 */
package com.menu.manger.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.menu.manger.service.impl.PosTransactionSerivce;

/**
 * @author liuzhen
 *
 */
@Component
@Lazy(value=false)
public class ApplicationContextHelper implements ApplicationContextAware  {
    private static ApplicationContext context;
    private static final Logger log = LoggerFactory.getLogger(PosTransactionSerivce.class);

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context =applicationContext;
		log.info("context 注入成功!");
	}
	public static <T> T getBean(Class<T> tClass){
        if(null==context){
            return null;
        }
        return context.getBean(tClass);
    }
}
