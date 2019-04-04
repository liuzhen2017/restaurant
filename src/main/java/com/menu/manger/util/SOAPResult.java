/**
 * 
 */
package com.menu.manger.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.menu.manger.dto.Members;

import storelle.api.pos.Coupon;
import storelle.api.pos.Discount;


/**
 * @author liuzhen
 *
 */
public class SOAPResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int result;
	private int  errCode;	
	private String  errMsg;
	
	/**
	 * @author liuzhen
	   @date 2019年2月23日
	   @version 1.0
	 */
	public SOAPResult(int result, int errCode, String errMsg) {
		super();
		this.result = result;
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Members getMember() {
		return member;
	}

	public BigDecimal getAccTransactionAmount() {
		return accTransactionAmount;
	}

	public String getLastTransactionDatetime() {
		return lastTransactionDatetime;
	}

	public Discount getDisCount() {
		return disCount;
	}

	public List<Coupon> getCoupon() {
		return coupon;
	}

	/**
	 * @author liuzhen
	   @date 2019年2月23日
	   @version 1.0
	 */
	public SOAPResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Members member;
	public BigDecimal accTransactionAmount;
	public String lastTransactionDatetime;
	public Discount disCount;
	public List<Coupon> coupon;
	
	public static SOAPResult error(int errCode,String errMsg){
		return new SOAPResult(1, errCode, errMsg);
	}
	public static SOAPResult success(){
		return new SOAPResult(0, 0,null);
	}
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/*public Members getMember() {
		return member;
	}*/

	public void setMember(Members member) {
		this.member = member;
	}

	/*public BigDecimal getAccTransactionAmount() {
		return accTransactionAmount;
	}*/

	public void setAccTransactionAmount(BigDecimal accTransactionAmount) {
		this.accTransactionAmount = accTransactionAmount;
	}

/*	public String getLastTransactionDatetime() {
		return lastTransactionDatetime;
	}*/

	public void setLastTransactionDatetime(String lastTransactionDatetime) {
		this.lastTransactionDatetime = lastTransactionDatetime;
	}

	/*public Discount getDisCount() {
		return disCount;
	}*/

	public void setDisCount(Discount disCount) {
		this.disCount = disCount;
	}

/*	public List<Coupon> getCoupon() {
		return coupon;
	}*/

	public void setCoupon(List<Coupon> coupon) {
		this.coupon = coupon;
	}

}
