package com.menu.manger.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.SysConfig;
import com.menu.manger.mapper.SysConfigMapper;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.JsonWebTokenUtil;
import com.menu.manger.util.ThreadLocalUtil;

/**
 * @author liiuzhen
 *
 *         2018年5月13日下午5:00:41
 */
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*", filterName = "corsFilter")
public class LoginFilter implements Filter {
	private static final Logger LOG = Logger.getLogger(LoginFilter.class);

	public static void sendMessage(HttpServletResponse response, String str)
			throws Exception {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(str);
		writer.close();
		response.flushBuffer();
	}
	String noLoginUrl=null;
	private boolean forwardLogin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 拦截所有请求
		boolean isLogin = true;
		Members userInfo = null;
		String strErrMsg = "";
		SysConfigMapper configMapper = ApplicationContextHelper.getBean(SysConfigMapper.class);
		SysConfig selectByKey = configMapper.selectByKey("noNeedLoginUrl");
		String noNeedLogin =selectByKey.getConfigValue();
		String requestUrl = request.getRequestURI();
		try {
			String token = request.getParameter("tokent");
			if(StringUtils.isEmpty(token)){
				token = request.getParameter("token");
			}
			requestUrl =requestUrl.substring(requestUrl.lastIndexOf("/")+1,requestUrl.lastIndexOf("."));
			if ((StringUtils.isEmpty(token)  || "undefined".equals(token)) && !noNeedLogin.contains(requestUrl) ) {
				LOG.info("request token is Empty..");
				sendMessage(response, JSONObject.toJSONString(AjaxResult.error(
						4444, "請求口令錯誤!")));
				return false;
			}

			if (!StringUtils.isEmpty(token)) {
				userInfo = JsonWebTokenUtil.unsign(token, Members.class);
				if (userInfo != null) {
					ThreadLocalUtil.set(HttpConstants.SERVICE_CACHE_USER,
							userInfo);
				}
			}
		} catch (Exception e) {
			if (userInfo == null && !noNeedLogin.contains(requestUrl)) {
				isLogin = false;
			}
			LOG.error(e.getMessage(), e);
			strErrMsg = "系統錯誤：" + e.getMessage();
		}
		// 判断用户信息
		if (userInfo == null || StringUtils.isEmpty(userInfo.getEmail())
				|| StringUtils.isNotBlank(strErrMsg)) {

			if (!noNeedLogin.contains(requestUrl)) {
				isLogin = false;
				if (StringUtils.isNotBlank(strErrMsg)) {
					sendMessage(response, JSONObject.toJSONString(AjaxResult
							.error(strErrMsg)));
					return false;
				} else {
					sendMessage(response, JSONObject.toJSONString(AjaxResult
							.error(4444, "用戶登陸已經過期,請重新登陸!")));
					return false;
				}
			}
		}
		return isLogin;
	}

	@Override
	public void doFilter(ServletRequest requests, ServletResponse responses,
			FilterChain arg2) throws IOException, ServletException {
		// 先判断是否登陆,如果没登陆,则 转发到第一页,进行登陆操作
		HttpServletRequest request = (HttpServletRequest) requests;
		HttpServletResponse response = (HttpServletResponse) responses;
		SysConfigMapper configMapper = ApplicationContextHelper.getBean(SysConfigMapper.class);
		SysConfig selectByKey = configMapper.selectByKey("noNeedLoginUrl");
		String noNeedLogin =selectByKey.getConfigValue();
		if (!request.getRequestURI().contains(noNeedLogin) && request.getRequestURI().endsWith(".do")) {
			try {
				 if(forwardLogin(request, response)){
					 arg2.doFilter(request, response);
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			 arg2.doFilter(request, response);
		}

	}

}
