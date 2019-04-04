/**
 * 
 */
package com.menu.manger.service;

import java.text.ParseException;

import storelle.api.pos.CloseTransactionResponse;
import storelle.api.pos.MemberEnquiryResponse;
import storelle.api.pos.ReverseTransactionResponse;
import storelle.api.pos.SubmitRedeemptionResponse;

/**
 * @author liuzhen
 *
 */
public interface IPosTransactionSerivce {
	/*
	 * 根据会员ID 和手机号查询是否存在
	 * @param memberID
	 * @param phone
	 * @param brandID
	 * @param t
	 * @param h
	 * @return
	 */
	public MemberEnquiryResponse queryMemberById(String memberID,String phone,String brandID,String t,String h) throws ParseException;
	/**
	 * 兑换优惠券 
	 * @param memberID 会员ID
	 * @param brandID 店铺ID
	 * @param shopCode 商品code
	 * @param redeemDatetime 兑换日期
	 * @param couponID 优惠券编号
	 * @param serialNo 序列号
	 * @param couponStatus 优惠券状态 1已兑现 2有效
	 * @param t
	 * @param h
	 * @return
	 */
	public SubmitRedeemptionResponse submitRedemption(String memberID,String brandID,String shopCode,String redeemDatetime,String couponID,
			String serialNo,
			String couponStatus,
			String t,
			String h);
	/**
	 * 提交交易
	 * @param brandID 店铺ID
	 * @param transactionDatetime 交易时间
	 * @param shopCode 商品代码
	 * @param invoiceNo  发票号码
	 * @param invoiceAmount 发票金额 
	 * @param netAmount 净额
	 * @param pax 商品数量
	 * @param coupons 优惠券集合
	 * @param memberID 会员ID
	 * @param t
	 * @param h
	 * @return
	 */
	public CloseTransactionResponse closeTransaction(String brandID,String transactionDatetime,String shopCode,String invoiceNo,
			String invoiceAmount,String netAmount,String pax,String coupons,String memberID,
			String t,String h) throws Exception;
	/**
	 * 撤销交易
	 * @param brandID 店铺ID
	 * @param transactionDatetime 交易时间
	 * @param invoiceNo 发票号码
	 * @param t 
	 * @param h
	 * @return
	 * @throws Exception 
	 */
	public ReverseTransactionResponse backTransaction(String brandID,String transactionDatetime,String invoiceNo,String t,String h) throws Exception;
}
