package com.menu.manger.util;

import java.io.Serializable;

import com.menu.manger.constants.HttpConstants;

/**
 * @author liuzhen
 * 封装返回值接口
 */
public class ResultUtil implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;
	public String message;
	public Object data;
	public ResultUtil(String code,String message,Object data,String busiRowId){
		this.code =code;
		this.message =message;
		this.data =data;
	}
	public ResultUtil() {
	
	}
	public static ResultUtil success(String message,Object data,String busiRowId){
		return new ResultUtil(HttpConstants.SERVICE_RESPONSE_SUCCESS_CODE, message, data,busiRowId);
	}
	public static ResultUtil success(String message,Object data){
		return new ResultUtil(HttpConstants.SERVICE_RESPONSE_SUCCESS_CODE, message, data,null);
	}
	public static ResultUtil success(String message,String busiRowId){
		return new ResultUtil(HttpConstants.SERVICE_RESPONSE_SUCCESS_CODE, message, null,busiRowId);
	}
	public static ResultUtil success(Object data,String busiRowId){
		return new ResultUtil(HttpConstants.SERVICE_RESPONSE_SUCCESS_CODE,"ok", data,busiRowId);
	}
	public static ResultUtil success(Object data){
		return new ResultUtil(HttpConstants.SERVICE_RESPONSE_SUCCESS_CODE,"ok", data,null);
	}
	public static ResultUtil success(String message){
		return new ResultUtil(HttpConstants.SERVICE_RESPONSE_SUCCESS_CODE, message, null,null);
	}
	public static ResultUtil error(String message,Object data,String busiRowId){
		return new ResultUtil(HttpConstants.SERVICE_RESPONSE_RESULT_FLAG, message, data,busiRowId);
	}
	public static ResultUtil error(String message,Object data){
		return new ResultUtil(HttpConstants.SERVICE_RESPONSE_RESULT_FLAG, message, data,null);
	}
	public static ResultUtil error(String code,String message){
		return new ResultUtil(code, message,null,null);
	}
	public static ResultUtil error(String message){
		return new ResultUtil(HttpConstants.SERVICE_RESPONSE_RESULT_FLAG, message,null,null);
	}
	public String getCode() {
		return code;
	}
	public void setCode(String returnCode) {
		this.code = returnCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
