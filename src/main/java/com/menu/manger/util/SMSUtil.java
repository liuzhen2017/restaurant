package com.menu.manger.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

/**
 * 發送短信
 */
public class SMSUtil{
	
	
	@SuppressWarnings("unchecked")
	public static AjaxResult sendMsg(SMSDto sms,String url) {
		
	
		
		Map<String, Object> parmat = new HashMap<String, Object>();
		// 封装请求參數
		String parmatJson =JSONObject.toJSONString(sms);
		parmat = JSONObject.parseObject(parmatJson,
				parmat.getClass());
		// 请求短信接口
		System.out.println("發送消息:"+parmatJson);
		String json = HttpUtils.sendGet(url, createGetParmat(parmat));
		System.out.println("返回消息:"+json);
		// 正则表达式解析
		String pattem = "<Success>true</Success><ResponseCode>A000</ResponseCode>";
		Pattern compile = Pattern.compile(pattem);
		Matcher matcher = compile.matcher(json);
		sms.setReplyAllMsg(json);
		String code =null;
		if (matcher.find()) {
			
			return AjaxResult.success("發送信息成功",sms);
		} else {
			pattem = "<ResponseCode>(.*)</ResponseCode><ResponseMessage>(.*)</ResponseMessage>";
			compile = Pattern.compile(pattem);
			matcher = compile.matcher(json);
			if (matcher.find()) {
				code =matcher.group(1);
				String msg =matcher.group(2);
				sms.setReplyCode(code);
				sms.setReplyMSG(msg);
				return AjaxResult.error(500,"發送消息失敗!,MSG=" + matcher.group(2),sms);
			}
		}
		return AjaxResult.error(500,"發送消息失敗!",sms);
	}
	public static String createGetParmat(Map<String, Object> map){
		Iterator<String> iterator = map.keySet().iterator();
		StringBuffer sb =new StringBuffer();
		String key =null;
		while (iterator.hasNext()) {
			key =iterator.next();
			sb.append(key+"="+map.get(key));
			sb.append("&");
		}
		return sb.toString();
	}

}
