/**
 * 
 */
package com.menu.manger.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.menu.manger.util.HttpUtils;
import com.menu.manger.util.MD5Util;

/**
 * @author liuzhen
 *
 */
/**
 * @author liuzhen
 *
 */
@Controller
@RequestMapping("/pay/sanPay/")
@ResponseBody
public class SanPay {
	public static String baseUrl ="http://lmapi.changchunjlqcdj.com/open/v1/";
	
	public static String getAccessTokenUrl="getAccessToken/merchant";
	public static String  MERCHANTNO ="SFM000000000000292";
	public static String KEY ="e21a02d1cb29467f8388a0137c9c7da1";
	/**
	 * 下单
	 */
	public static String quickPayUrl="quickPay/hQuick";
	/**
	 *获取订单
	 */
	public static String getByCustomerNo="order/getByCustomerNo";
	@RequestMapping("apisubmit.do")
	public String apisubmit(String merchantNo,
			String key,
			String nonce,
			String timestamp,
			String sign,
			String token){
		Map<String, Object> map =new HashMap<String, Object>();
		merchantNo =StringUtils.isEmpty(merchantNo)? MERCHANTNO:merchantNo;
		key =StringUtils.isEmpty(key)? KEY:key;
		nonce =StringUtils.isEmpty(nonce)? new Random().nextInt()+"":nonce;
		map.put("merchantNo", merchantNo);
		map.put("nonce", nonce);
		map.put("timestamp", System.currentTimeMillis());
		String signStr ="merchantNo="+merchantNo+"&nonce="+nonce+"&timestamp="+map.get("timestamp");
		if(StringUtils.isNotEmpty(token)){
			signStr +="&token="+token;
		}
		signStr+="&key="+key;
		map.put("token", token);
		map.put("key", key);
		try {
			
			map.put("sign", MD5Util.md5(signStr).toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return HttpUtils.doPost(baseUrl+getAccessTokenUrl, map);
	}
	/**
	 * 快捷下单
	 * @param merchantNo
	 * @param key
	 * @param nonce
	 * @param timestamp
	 * @param sign
	 * @param token
	 * @return
	 */
	@RequestMapping("quickPay.do")
	public String quickPay(String accessToken,
			String outTradeNo,
			String money,
			String type,
			String body,
			String detail,
			String notifyUrl,
			String productId,
			String cardName,
			String cardNo,
			String bank,
			String idType,
			String cardPhone,
			String cardIdNumber){
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("accessToken", accessToken);
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("outTradeNo", outTradeNo);
		param.put("money", money);
		param.put("type", type);
		param.put("body", body);
		param.put("detail", detail);
		param.put("type", type);
		param.put("body", body);
		param.put("detail", detail);
		param.put("notifyUrl", notifyUrl);
		param.put("productId", productId);
		param.put("cardName", cardName);
		param.put("cardNo", cardNo);
		param.put("bank", bank);
		param.put("idType", idType);
		param.put("cardPhone", cardPhone);
		param.put("cardIdNumber",cardIdNumber);
		map.put("param", param);
		
		return HttpUtils.doPost(baseUrl+quickPayUrl, map);
	}
	/**
	 * 支付回调地址
	 * @param no
	 * @param outTradeNo
	 * @param merchantNo
	 * @param productId
	 * @param money
	 * @param body
	 * @param detail
	 * @param tradeType
	 * @param date
	 * @param nonce
	 * @param timestamp
	 * @param sign
	 * @param success
	 * @return
	 */
	public String payNotify(String no,String outTradeNo,String merchantNo,String productId,String money,String body,String detail,String tradeType,String date,String nonce,String timestamp,String sign,String success){
		
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("no", no);
		map.put("outTradeNo", outTradeNo);
		map.put("merchantNo", merchantNo);
		map.put("productId", productId);
		map.put("money", money);
		map.put("body", body);
		map.put("detail", detail);
		map.put("body", body);
		map.put("detail", detail);
		map.put("tradeType", tradeType);
		map.put("productId", productId);
		map.put("date", date);
		map.put("nonce", nonce);
		map.put("timestamp", timestamp);
		map.put("sign", sign);
		map.put("success", success);
		
		System.out.println(JSONObject.toJSON(map));
		//如果處理成功
		return "SUCCESS";
		//失敗返回任意值
//		return "code:01,msg="訂單不存在,請校驗!";
	}
	/**
	 * 查詢訂單
	 * @param accessToken
	 * @param param
	 * @return
	 */
	@RequestMapping("getByCustomerNo.do")
	public String getByCustomerNo(String accessToken,String param){
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("accessToken", accessToken);
		map.put("param", param);
		return HttpUtils.doPost(baseUrl+getByCustomerNo, map);
	}
	
}
