package com.menu.manger.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.AccountFlow;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.SysConfig;
import com.menu.manger.mapper.AccountFlowMapper;
import com.menu.manger.service.IMembersService;
import com.menu.manger.service.IMyCouponService;
import com.menu.manger.service.IScoreHisService;
import com.menu.manger.service.ISysConfigService;
import com.menu.manger.util.AjaxResult;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@Controller
@RequestMapping("/api/payApi/")
@ResponseBody
public class PayApiController extends BaseController {

	@Autowired
	ISysConfigService configService;
	@Autowired
	IScoreHisService hisService;
	@Autowired
	AccountFlowMapper balanceMapperMapper;
	@Autowired
	IMyCouponService myCouponService;
	
	
	
	private static final Logger log = LoggerFactory
			.getLogger(PayApiController.class);
	@Autowired
	IMembersService membersService;

	/*
	 * 查询短信發送記錄列表
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("payment.do")
	@ResponseBody
	public AjaxResult payment(String email, String tokenId, Double ammount,
			String code) {
		log.info("購買VIP開始");
		;
		Stripe.apiKey = configService.selectConfigByKey("pay_user_service_key");

		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", ammount.intValue());
		chargeParams.put("currency", "HKD");
		chargeParams.put("description", "購買會員");
		chargeParams.put("source", tokenId);
		double price = 0;
		log.info("購買VIP 1.核對金額");
		if (StringUtils.isNoneEmpty(code)) {
			AjaxResult calVIPCode = myCouponService.calVIPCode(code);
			if (calVIPCode.get("code").toString().equals("1")) {
				return AjaxResult.error("計算交易金額錯誤!");
			}
			Map<String, Object> data = new HashMap<>();
			data = (Map<String, Object>) calVIPCode.get("data");
			price = (double) data.get("newPrice");
		} else {
			SysConfig selectByKey = configService
					.selectByKey(HttpConstants.VIPPrice);
			price = Double.parseDouble(selectByKey.getConfigValue());
		}
		if (price != ammount) {
			return AjaxResult.error("交易金額被和系統不匹配!");
		}
		try {
			log.info("購買VIP 2.調用支付系統");
			;
			Charge charge = Charge.create(chargeParams);
			if (charge.getStatus().equals("succeeded")) {
				log.info("購買VIP 3.寫入積分處理");
				;
				hisService.upgradeVIP(email, ammount, getUserInfo(),code);

				// 發送贈送菜

				Map<String, Object> data = new HashMap<String, Object>();
				Members membersById = membersService
						.selectMembersById(getUserInfo().getId());
				data.put("myscorp", membersById.getScore());
				return AjaxResult.success("升級VIP會員成功,贈送菜品已經發放到優惠券!", data);
			} else {
				// 流水写入记录
				AccountFlow accountFlow = new AccountFlow();
				accountFlow.setAccNo("" + 0);
				accountFlow.setTranType(3);// 支出
				accountFlow.setMoney(new BigDecimal(ammount));
				accountFlow.setMenuId(getUserInfo().getId());
				accountFlow.setMenuAccNo(email);
				accountFlow.setTranDes("支付失敗！Code=" + charge.getStatus());
				balanceMapperMapper.insertAccountFlow(accountFlow);
				return AjaxResult.error("支付失敗！請重試,Code=" + charge.getStatus());
			}
		} catch (StripeException e) {
			e.printStackTrace();
			// 流水写入记录
			AccountFlow accountFlow = new AccountFlow();
			accountFlow.setAccNo("" + 0);
			accountFlow.setTranType(3);// 失敗
			accountFlow.setMoney(new BigDecimal(ammount));
			accountFlow.setMenuId(getUserInfo().getId());
			accountFlow.setMenuAccNo(email);
			accountFlow.setTranDes("支付失敗！Code=" + e.getMessage());
			balanceMapperMapper.insertAccountFlow(accountFlow);
			return AjaxResult.error("支付失敗！請重試");
		}
	}

	/**
	 * 会员升级
	 * 
	 * @param email
	 *            支付ID
	 * @param tokenId
	 *            支付ID
	 * @param ammount
	 *            金额
	 * @param code
	 *            优惠券代码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("upgradeVIP.do")
	@ResponseBody
	public AjaxResult upgradeVIP(String email, String tokenId, Double ammount,
			String code) {
		log.info("升級VIP 1.升級VIP開始");
		Stripe.apiKey = configService.selectConfigByKey("pay_user_service_key");

		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", ammount.intValue());
		chargeParams.put("currency", "HKD");
		chargeParams.put("description", "升級會員");
		chargeParams.put("source", tokenId);
		double price = 0;
		log.info("升級VIP 2.核對金額");
		;

		if (StringUtils.isNoneEmpty(code)) {
			AjaxResult calVIPCode = myCouponService.calVIPCode(code);
			if (calVIPCode.get("code").toString().equals("1")) {
				return AjaxResult.error("計算交易金額錯誤!");
			}
			Map<String, Object> data = new HashMap<>();
			data = (Map<String, Object>) calVIPCode.get("data");
			price = (double) data.get("newPrice");
		} else {
			SysConfig selectByKey = configService
					.selectByKey(HttpConstants.VIPPrice);
			price = Double.parseDouble(selectByKey.getConfigValue());
		}
		if (price != ammount) {
			return AjaxResult.error("交易金額被和系統不匹配!");
		}
		try {
			log.info("升級VIP 3.調用第三方支付");
			;
			Charge charge = Charge.create(chargeParams);
			log.info("升級VIP 4.第三方支付回復:" + JSONObject.toJSONString(charge));
			;
			if (charge.getStatus().equals("succeeded")) {
				log.info("升級VIP 5.升級業務處理:" + JSONObject.toJSONString(charge));
				;
				hisService.upgradeVIP(email, ammount, getUserInfo(),code);

				// 發送贈送菜

				// myCouponService.grantLoginCoupon();
					Map<String, Object> data = new HashMap<String, Object>();
					Members membersById = membersService
							.selectMembersById(getUserInfo().getId());
					data.put("myscorp", membersById.getScore());
					return AjaxResult.success("升級VIP會員成功,贈送菜品已經發放到優惠券!", data);
			} else {
				// 流水写入记录
				AccountFlow accountFlow = new AccountFlow();
				accountFlow.setAccNo("" + 0);
				accountFlow.setTranType(3);// 支出
				accountFlow.setMoney(new BigDecimal(ammount));
				accountFlow.setMenuId(getUserInfo().getId());
				accountFlow.setMenuAccNo(email);
				accountFlow.setTranDes("支付失敗！Code=" + charge.getStatus());
				balanceMapperMapper.insertAccountFlow(accountFlow);
				return AjaxResult.error("支付失敗！請重試,Code=" + charge.getStatus());
			}
		} catch (StripeException e) {
			e.printStackTrace();
			// 流水写入记录
			AccountFlow accountFlow = new AccountFlow();
			accountFlow.setAccNo("" + 0);
			accountFlow.setTranType(3);// 失敗
			accountFlow.setMoney(new BigDecimal(ammount));
			accountFlow.setMenuId(getUserInfo().getId());
			accountFlow.setMenuAccNo(email);
			accountFlow.setTranDes("支付失敗！Code=" + e.getMessage());
			balanceMapperMapper.insertAccountFlow(accountFlow);
			return AjaxResult.error("支付失敗！請重試");
		}
	}
	@PostMapping("paymentRegist.do")
	@ResponseBody
	public AjaxResult paymentRegist(String email, String tokenId, Double ammount,
			String code,String members){
		log.info("購買VIP開始");
		;
		members=members.replace("\\", "");
		members=members.substring(1,members.length()-1);
		Members mem =JSONObject.parseObject(members,Members.class);
		if(mem ==null){
			return AjaxResult.error("請求參數爲空!");
		}
		if(mem.getName() ==null){
			return AjaxResult.error("姓名不能爲空!");
		}
		if(mem.getEmail() ==null){
			return AjaxResult.error("郵箱不能爲空!");
		}
		if(mem.getPhone() ==null){
			return AjaxResult.error("電話不能爲空!");
		}
		try {			
			return hisService.payTest(email, ammount, mem, code,tokenId);
		} catch (Exception e) {
			return AjaxResult.error(e.getMessage());
		}
	}

}
