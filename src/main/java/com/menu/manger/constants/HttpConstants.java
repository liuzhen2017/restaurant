package com.menu.manger.constants;

public class HttpConstants {

	
	// 服务端返回成功的标志
	public static final String SERVICE_RESPONSE_SUCCESS_CODE = "00";
	
	// 服务端返回结果的标志
	public static final String SERVICE_RESPONSE_RESULT_FLAG = "01";
	// 服务端返回结果的标志
	public static final String SERVICE_CACHE_USER = "menmbers";
	
	
	public static final String ISVAILD_YES="yes";
	
	
	public static final String ISVAILD_NO ="no";
	
	/**
	 * 普通會員
	 */
	public static final Integer EmmbersType_0=0;
	/**
	 * VIP會員
	 */
	public static final Integer EmmbersType_1=1;
	/**
	 * 通用
	 */
	public static final Integer EmmbersType_2=2;
	/**
	 * 会员折扣
	 */
	public static final String VIPDiscount="VIPDiscount";
	/**
	 *  发送邮箱
	 */
	public static final String sendEmail="sendEmail";
	/**
	 * 发送短信
	 */
	public static final String sendSMSSetting="sendSMSSetting";
	public static final String selectByAppUpload="selectByAppUpload";
	
	/**
	 * 发送短信
	 */
	public static final String sms_invite="sms_invite";
	
	/**
	 * 购买VIP价格
	 */
	public static final String  VIPPrice="VIPPrice"; 
	/**
	 * 支付系统前台key
	 */

	public static final String pay_user_web_key="pay_user_web_key"; 
	/**
	 *  支付系统后台key
	 */
	public static final String pay_user_service_key="pay_user_service_key";
	/**
	 * 消費多少錢自動升級會員
	 */
	public static final String autoUpgradingMoney ="autoUpgradingMoney";
	/**
	 * 消費多少錢自動升級會員VIP用戶
	 */
	public static final String autoUpgradingMoneyVIP ="autoUpgradingMoneyVIP";
	
	/**
	 * 入賬
	 */
	public static final int transtionStatus_1 =1;
	/**
	 * 支出
	 */
	public static final int transtionStatus_2 =2;
	/**
	 * 撤銷交易
	 */
	public static final int transtionStatus_3 =3;
	/**
	 * 兑换菜品状态-未使用
	 */
	public static final int exchange_status_0=0;
	/**
	 * 兑换菜品状态-已使用
	 */
	public static final int exchange_status_1=1;
	/**
	 * 兑换菜品状态-已过期
	 */
	public static final int exchange_status_2=2;
	
	/**
	 * 掃描二維碼積分天數
	 */
	public static final String qcSaveDate ="qcSaveDate";
}
