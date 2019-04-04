package com.menu.manger.exception;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.alibaba.fastjson.JSONObject;
import com.menu.manger.util.AjaxResult;


/**
 * 异常拦截
 * @author Administrator
 *
 */
@ControllerAdvice()
public class CommonExceptionHandler {

	/**
     *  拦截Exception类的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception e){
    	if(e instanceof HttpRequestMethodNotSupportedException){
    		if(e.getMessage().contains("GET")){
    	     return	JSONObject.toJSONString(AjaxResult.error("請使用POST方法訪問!"));
    		}else{
    		 return	JSONObject.toJSONString(AjaxResult.error("請使用GET方法訪問!"));
    		}
    	}else if(e instanceof MissingServletRequestParameterException){
    		// Required String parameter 'phone' is not present
    	     String pattern = "('.*')";
    		 
    	    Pattern pattern2 = Pattern.compile(pattern);
    	    Matcher matcher = pattern2.matcher(e.getMessage());
    	    String missInfo =null;
    	    if(matcher.find()){
    	    	missInfo = matcher.group(0);
    	    }
    		 return	JSONObject.toJSONString(AjaxResult.error("請求參數異常,缺失"+missInfo+"參數!"));
    	}else if(e instanceof MethodArgumentTypeMismatchException){
    		return	JSONObject.toJSONString(AjaxResult.error("請求參數異常,類型不匹配,MSG="+e.getMessage()));
    	}
    	
        return JSONObject.toJSONString(AjaxResult.error("請求異常，請稍後再重試!"+e.getMessage())); 
    }
}
