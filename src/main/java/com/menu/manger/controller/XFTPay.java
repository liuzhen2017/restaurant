///**
// * 
// */
//package com.menu.manger.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.menu.manger.util.HttpUtils;
//import com.menu.manger.util.MD5Util;
//
///**
// * @author liuzhen
// *
// */
//@Controller
//@RequestMapping("/pay/apisubmit")
//@ResponseBody
//public class XFTPay {
//	public static String baseUrl ="https://home.yjubao.com/";
//
//	/**
//	 * 支付
//	 */
//	public static String APISUBMIT = "apisubmit";
//	/**
//	 * 查询订单接口,查询余额,提现
//	 */
//	public static String apiorderquery = "apiorderquery";
//	public static String CUSTOMERID="11033";
//	public static String pwd="9bcd853e84ccda27112daa3d3d9fa85d32b94b57";
//    
//	
//	/**
//	 * 
//	 * @param version 版本
//	 * @param paytype
//	 * @param method
//	 * @param customerid
//	 * @param total_fee
//	 * @param sdorderno
//	 * @param notifyurl
//	 * @param returnurl
//	 * @param sign md5('version=值&customerid=值&total_fee=值&sdorderno=值&notifyurl=值&returnurl=值&key')  key是商户密钥(商户后台获取)
//	 * @param bankcode
//	 * @param remark
//	 * @param bank_card
//	 * @return
//	 * @throws Exception 
//	 */
//
//	@RequestMapping("apisubmit.do")
//	public String apisubmit( String version,String paytype,String method,String customerid,String total_fee,String sdorderno,String notifyurl,String returnurl,String sign,String bankcode,String remark,String bank_card) throws Exception {
//		Map<String,Object> params =new HashMap<String, Object>();
//		params.put("version", StringUtils.isEmpty(version)? "1.0" : version);
//		params.put("paytype", paytype);
//		params.put("method", method);
//		params.put("customerid", StringUtils.isEmpty(customerid)? CUSTOMERID:customerid);
//		params.put("total_fee", total_fee);
//		params.put("sdorderno", sdorderno);
//		params.put("notifyurl", notifyurl);
//		params.put("returnurl", returnurl);
//		String signStr ="version="+params.get("version")+"&customerid="+params.get("customerid")+"&total_fee="+total_fee
//				+"&sdorderno="+sdorderno+"&notifyurl="+notifyurl+"&returnurl="+returnurl+"&key="+pwd;
//		params.put("sign", MD5Util.md5(signStr));
//		params.put("bankcode", bankcode);
//		params.put("bank_card", bank_card);
//		params.put("remark", remark);
//		return HttpUtils.doPost(baseUrl+APISUBMIT, params);
//	}
//	/**
//	 * 查询商户资金
//	 * @param customerid 商户号
//	 * @param reqtime 请求时间
//	 * @param sign md5('customerid=值&reqtime=值&key') key是商户密钥(商户后台获取)
//	 * @return
//	 */
//	@RequestMapping("queryBalance.do")
//	public String apiorderquery(String customerid,String reqtime,String sign){
//		Map<String,Object> params =new HashMap<String, Object>();
//		params.put("customerid", StringUtils.isEmpty(customerid)? CUSTOMERID:customerid);
//		params.put("reqtime", StringUtils.isEmpty(reqtime)? System.currentTimeMillis():reqtime);
//		params.put("querytype", "querybalance");
//		try {
//			params.put("sign", MD5Util.md5(customerid,params.get("reqtime")+"",pwd));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return HttpUtils.doPost(baseUrl+apiorderquery, params);
//	}
//	/**
//	 * 查询订单
//	 * @param customerid 商户号
//	 * @param reqtime 请求时间
//	 * @param sign md5('customerid=值&reqtime=值&key') key是商户密钥(商户后台获取)
//	 * @return
//	 */
//	@RequestMapping("queryOrder.do")
//	public String queryOrder(String customerid,String sdorderno,String reqtime,String sign){
//		Map<String,Object> params =new HashMap<String, Object>();
//		params.put("customerid", customerid);
//		params.put("reqtime", StringUtils.isEmpty(reqtime)? System.currentTimeMillis():reqtime);
//		params.put("querytype", "queryorder");
//		try {
//			params.put("sign", MD5Util.md5(customerid,sdorderno,params.get("reqtime")+"",pwd));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		params.put("sdorderno", sdorderno);
//		return HttpUtils.doPost(baseUrl+apiorderquery, params);
//	}
//	/**
//	 * 提现
//	 * @param customerid 商户号
//	 * @param reqtime 请求时间
//	 * @param sign md5('customerid=值&reqtime=值&key') key是商户密钥(商户后台获取)
//	 * @return
//	 */
//	@RequestMapping("withdraw.do")
//	public String withdraw(String customerid,
//			String txmoney,
//			String realname,
//			String batype,
//			String baname,
//			String baaddr,
//			String identity_card,
//			String phone,
//			String sdorderno,
//			String querytype,
//			String sign
//){
//		Map<String,Object> params =new HashMap<String, Object>();
//		params.put("customerid", customerid);
//		params.put("txmoney", txmoney);
//		params.put("realname", realname);
//		params.put("batype", batype);
//		params.put("baname", baname);
//		params.put("baaddr", baaddr);
//		params.put("identity_card", identity_card);
//		params.put("phone", phone);
//		params.put("querytype", "withdraw");
//		try {
//			params.put("sign", MD5Util.md5(customerid,sdorderno,txmoney,realname,batype,baname,baaddr,identity_card,phone,pwd));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return HttpUtils.doPost(baseUrl+apiorderquery, params);
//	}
//	@RequestMapping("notifyurl.do")
//	public void notifyurl(String arry){
//		System.out.println("notifyurl "+arry);
//	}
//	@RequestMapping("returnurl.do")
//	public void returnurl(String returnurl){
//		System.out.println("returnurl: "+returnurl);
//	}
//}
