/**
 * 
 */
package com.menu.manger.service.impl;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import storelle.api.pos.CloseTransactionResponse;
import storelle.api.pos.Coupon;
import storelle.api.pos.Discount;
import storelle.api.pos.Member;
import storelle.api.pos.MemberEnquiryResponse;
import storelle.api.pos.ReverseTransactionResponse;
import storelle.api.pos.SubmitRedeemptionResponse;

import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.AccountFlow;
import com.menu.manger.dto.BranchStore;
import com.menu.manger.dto.IntegralRole;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.MenuFoodExchange;
import com.menu.manger.dto.ScoreHis;
import com.menu.manger.dto.SysConfig;
import com.menu.manger.mapper.BranchStoreMapper;
import com.menu.manger.mapper.MembersMapper;
import com.menu.manger.service.IAccountFlowService;
import com.menu.manger.service.IAcctBalanceService;
import com.menu.manger.service.IBranchStoreService;
import com.menu.manger.service.IIntegralRoleService;
import com.menu.manger.service.IMenuFoodExchangeService;
import com.menu.manger.service.IMenuFoodService;
import com.menu.manger.service.IMyCouponService;
import com.menu.manger.service.INoticeInfoService;
import com.menu.manger.service.IPosTransactionSerivce;
import com.menu.manger.service.IScoreHisService;
import com.menu.manger.service.ISysConfigService;
import com.menu.manger.util.DateUtils;

/**
 * @author liuzhen
 *
 */
@Service
public class PosTransactionSerivce implements IPosTransactionSerivce {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	MembersMapper memBersMapper;
	
	@Autowired
	IMyCouponService myCouponService;
	@Autowired
	ISysConfigService sysConfigService;
	@Autowired
	IMenuFoodExchangeService memuFoodExchange;
	@Autowired
	IMenuFoodService menuFoodservice;
	@Autowired
	IScoreHisService scoreHisService;
	@Autowired
	IAccountFlowService accountFlowService;
	@Autowired
	IAcctBalanceService accBalanceService;
	@Autowired
	IBranchStoreService branchStoreSerivce;
	@Autowired
	IIntegralRoleService roleService;
	@Autowired
	BranchStoreMapper branchMapper;
	@Autowired
	INoticeInfoService noticeInfoService;
	private static String  falseSerNo="MOB181221AA";
    private static final Logger log = LoggerFactory.getLogger(PosTransactionSerivce.class);
	/* (non-Javadoc)
	 * @see com.menu.manger.service.IPosTransactionSerivce#queryMemberById(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public MemberEnquiryResponse queryMemberById(String memberID, String phone,
			String brandID, String t, String h) throws ParseException {
		
		/*if(!checkHash(h,memberID,phone,brandID)){
			return SOAPResult.error(1,"HASH不匹配!");
		}*/
		
		log.info("begin request memberEnquiry,parmat ={}",memberID,phone,brandID,t,h);
//		BranchStore branchStore =new BranchStore();
		MemberEnquiryResponse response =new MemberEnquiryResponse();
//		branchStore.setStoreNo(Integer.parseInt(brandID));
//		List<BranchStore> selectBranchStoreList = branchMapper.selectBranchStoreList(branchStore);
//		if(selectBranchStoreList ==null || selectBranchStoreList.size() ==0){
//			 response.setResult(2);;
//	    	 response.setErrCode(2);
//	    	 response.setErrMsg("根據分店ID查找,分店沒有錄入該系統不存在!,分店ID: "+brandID);
//	    	 return response;
//		}
		
		Lock lock =new ReentrantLock();
		lock.lock();
		Map<String, Object> map =new HashMap<>();
		map.put("key", "code");
		map.put("value", memberID);
		Members members =new Members();
		members.setCode(memberID);
		List<Members> selectMembersList = memBersMapper.selectMembersList(members);
		log.info(" memberEnquiry,select By code,member ={}",selectMembersList);
		if(selectMembersList==null || selectMembersList.size() ==0){
			members.setPhone(phone);
			members.setCode(null);
			selectMembersList = memBersMapper.selectMembersList(members);
		}
		Members membersByEmail = selectMembersList ==null || selectMembersList.size() ==0? null :selectMembersList.get(0);
		log.info(" memberEnquiry,select By phone,member ={}",selectMembersList);
		if(membersByEmail ==null){
		      membersByEmail = memBersMapper.selectMembersByPhone(phone);
		     if(membersByEmail ==null){
		    	 response.setResult(2);;
		    	 response.setErrCode(2);
		    	 response.setErrMsg("根據賬號和手機號未查到會員信息");
		    	 return response;
		     }
		}
		membersByEmail.setSalt(null);
		membersByEmail.setPwd(null);
		//設置會員信息
//		ressult.setMember(membersByEmail);
		
		//設置折扣信息
		Discount discount =new Discount();
		log.info(" memberEnquiry,Begin query Discount",selectMembersList);
		if(membersByEmail.getMembersType() == HttpConstants.EmmbersType_1){
			SysConfig sysConfig = sysConfigService.selectByKey(HttpConstants.VIPDiscount);
			Integer vipDiscount =sysConfig ==null? 0:Integer.parseInt(sysConfig.getConfigValue());
			discount.setAmount(vipDiscount);
			discount.setName(sysConfig.getConfigName());
			discount.setExpiry(membersByEmail.getVipDate());
		}
		
		response.setDiscount(discount);;
		if(membersByEmail !=null) {
			Member m =new Member();
			if(membersByEmail !=null) {
				m.setMemberID(Integer.parseInt(membersByEmail.getCode()));
				m.setName(membersByEmail.getName());
				m.setSecurityCode(membersByEmail.getSalt());
				response.setMember(m);;
			}
		}
		
		MenuFoodExchange menuFoodExchange =new MenuFoodExchange();
		menuFoodExchange.setMembersId(membersByEmail.getId());
		menuFoodExchange.setIsVaild(HttpConstants.ISVAILD_YES);
		menuFoodExchange.setExchangeStatus(0);
		//設置優惠券信息
		List<MenuFoodExchange> selectMenuFoodExchangeList = memuFoodExchange.selectMenuFoodExchangeList(menuFoodExchange);
		log.info(" memberEnquiry,Begin query MenuFoodExchange ={}",selectMenuFoodExchangeList);
		
		List<Coupon> listCoupon =new ArrayList<>();
	    Coupon c ;
	    Calendar calen =Calendar.getInstance();
		calen.setTime(new Date());
		calen.add(Calendar.MONTH,6);;
		List<String> itemCode =null;
		//设置優惠券信息
	    for (MenuFoodExchange coupon : selectMenuFoodExchangeList) {
			c =new Coupon();
			c.setId(coupon.getId().intValue());
			c.setName(coupon.getMenuFoodName());
			c.setType(0);
			c.setOffType(0);
			c.setNetOff(5);
			itemCode = new ArrayList<>();
			if(StringUtils.isNoneBlank(coupon.getInvalidDate())){
			  c.setExpiry(coupon.getInvalidDate());
			}
			c.setDescription(coupon.getRemark());
			itemCode.add("0000"+coupon.getSpareField1());
			c.setItemCode(itemCode);
			//假的序列號
			c.setSerialNo(falseSerNo+coupon.getId()); 
			listCoupon.add(c);
		}
		response.setCoupons(listCoupon);
		response.setResult(0);;
		lock.unlock();
		log.info(" memberEnquiry end ,request={}",response);
		return response;
	}

	/* (non-Javadoc)
	 * @see com.menu.manger.service.IPosTransactionSerivce#submitRedemption(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public SubmitRedeemptionResponse submitRedemption(String memberID, String brandID,
			String shopCode, String redeemDatetime, String couponID,
			String serialNo, String couponStatus, String t, String h) {
		SubmitRedeemptionResponse response =new SubmitRedeemptionResponse();
//		if(!checkHash(h,memberID,brandID,shopCode,redeemDatetime,couponID,serialNo,couponStatus)){
//			response
//			return SOAPResult.error(1,"HASH不匹配!");
//		}
		log.info("begin request submitRedemption,memberID ={} ,brandID={},shopCode={},redeemDatetime={},couponID={},serialNo={},couponStatus={}",memberID,brandID,shopCode,redeemDatetime,couponID,serialNo,couponStatus);
		response.setResult(0);
		Lock lock =new ReentrantLock();
		lock.lock();
		Members members =new Members();
		members.setCode(memberID);
		List<Members> selectMembersList = memBersMapper.selectMembersList(members);
		log.info("submitRedemption query Members By code={}",selectMembersList);
		Members membersByEmail = selectMembersList ==null || selectMembersList.size() ==0? null :selectMembersList.get(0);
		if(membersByEmail ==null){
			response.setResult(1);
			response.setErrCode(2);;
			response.setErrMsg("根據賬號未查到會員信息");;
			return response;
		}
		MenuFoodExchange selectMenuFoodExchangeList = memuFoodExchange.selectMenuFoodExchangeById(Integer.parseInt(couponID));
		log.info("submitRedemption query menuFoodChange By ID={}",selectMenuFoodExchangeList);
		if(selectMenuFoodExchangeList ==null || selectMenuFoodExchangeList.getMembersId().intValue() != membersByEmail.getId()){
				if(selectMenuFoodExchangeList ==null ){
					response.setResult(1);
					response.setErrCode(2);;
					response.setErrMsg("該優惠券不存在");
					return response;
			  }
		}
		if(couponStatus.equals("1")) {
			log.info("執行兌換==couponId={}",couponID);
			if(!selectMenuFoodExchangeList.getIsVaild().equals(HttpConstants.ISVAILD_YES)){
				log.info("兌換失敗,狀態為已經兌換!==   couponId={}",couponID);
				response.setResult(1);
				response.setErrCode(2);;
				response.setErrMsg("该優惠券状态已經兌換!");
				return response;
			}
			log.info("兌換成功   couponId={}",couponID);
			selectMenuFoodExchangeList.setExchangeStatus(1);
			selectMenuFoodExchangeList.setIsVaild(HttpConstants.ISVAILD_NO);
			memuFoodExchange.updateMenuFoodExchange(selectMenuFoodExchangeList);
		}else if(couponStatus.equals("2")){
			log.info("執行撤銷兌換==couponId={}",couponID);
			if(!selectMenuFoodExchangeList.getIsVaild().equals(HttpConstants.ISVAILD_NO) || selectMenuFoodExchangeList.getExchangeStatus() != 1){
				response.setResult(1);
				response.setErrCode(2);;
				log.info("執行撤銷兌換失敗,狀態為沒有使用  couponId={}",couponID);
				response.setErrMsg("该優惠券状态沒有使用!");
				return response;
			}
			log.info("執行撤銷兌換成功,  couponId={}",couponID);
			selectMenuFoodExchangeList.setExchangeStatus(0);
			selectMenuFoodExchangeList.setIsVaild(HttpConstants.ISVAILD_YES);
			memuFoodExchange.updateMenuFoodExchange(selectMenuFoodExchangeList);
		}
		response.setResult(0);;
		lock.unlock();
		return response;
	}

	/* (non-Javadoc)
	 * @see com.menu.manger.service.IPosTransactionSerivce#closeTransaction(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public CloseTransactionResponse closeTransaction(String brandID, String transactionDatetime,
			String shopCode, String invoiceNo, String invoiceAmount,
			String netAmount, String pax, String coupons, String memberID,
			String t, String h) {
		CloseTransactionResponse response =new CloseTransactionResponse();
		/*if(StringUtils.isEmpty(coupons) || StringUtils.isEmpty(memberID)) {
			response.setResult(1);
			response.setErrCode(2);;
			response.setErrMsg("關鍵信息不能為空!");;
			return response;
		}*/
		log.info("begin request closeTransaction ,brandID ={},transactionDatetime ={},shopCode={},invoiceNo ={},invoiceAmount ={},netAmount ={},coupons={},memberID ={}",brandID,transactionDatetime,shopCode,invoiceNo,invoiceAmount,netAmount,coupons,memberID);
		List<Members> selectMembersList =null;
		Members members =new Members();
		if(!StringUtils.isEmpty(memberID)) {
			members.setCode(memberID);
			selectMembersList = memBersMapper.selectMembersList(members);
			log.info("closeTransaction 1: query members By code ={} ",selectMembersList);
			if(selectMembersList ==null || selectMembersList.size() ==0){
				response.setResult(1);
				response.setErrCode(2);;
				response.setErrMsg("根據賬號未查到會員信息");;
				response.setStatus(1);
				return response;
			}
			members=selectMembersList.get(0);
		}
		Lock lock =new ReentrantLock();
		lock.lock();
		if(!StringUtils.isEmpty(coupons)) {
			List<MenuFoodExchange> selectMenuFoodExchangeByIds = memuFoodExchange.selectMenuFoodExchangeByIds(coupons);
			log.info("closeTransaction 2: 查詢優惠券={} ",selectMenuFoodExchangeByIds);
			if(selectMenuFoodExchangeByIds ==null || selectMenuFoodExchangeByIds.size() ==0){
				response.setResult(1);
				response.setErrCode(2);;
				response.setErrMsg("優惠券ID有誤!");;
				response.setStatus(1);
				return response;
			}
			for (MenuFoodExchange foodExchange : selectMenuFoodExchangeByIds) {
				if(foodExchange ==null || !foodExchange.getIsVaild().equals("no") ||foodExchange.getExchangeStatus() != 1){
					response.setResult(1);
					  response.setErrCode(2);;
					  response.setErrMsg("優惠券狀態已經不可以消費!");;
					  response.setStatus(1);
					  return response;
				  }
				
				if(StringUtils.isNoneBlank(foodExchange.getInvalidDate())&& DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",foodExchange.getInvalidDate()).before(new Date())){
					response.setResult(1);
					  response.setErrCode(2);;
					  response.setErrMsg("該優惠券已經過期,優惠券代碼:0000"+foodExchange.getSpareField1());;
					  return response;
				}
				if(!StringUtils.isEmpty(memberID)) {
				  noticeInfoService.insertNoticeInfo("您在pos机消费了一张优惠券", members.getId(), foodExchange.getId().intValue(), "MenuFoodExchange", foodExchange.getMenuFoodPic());
				}
			}
		}
		log.info("closeTransaction 3:填写账户流水 ");
		
	    AccountFlow accountFlow =new AccountFlow();
	    if(!StringUtils.isEmpty(memberID)) {
	        accountFlow.setAccNo(memberID);
	        accountFlow.setMenuId(selectMembersList.get(0).getId());
	    	accountFlow.setMenuAccNo(selectMembersList.get(0).getCode());
	    	accountFlow.setUpdateBy(selectMembersList.get(0).getName());
	    }
	    
		BranchStore branchStore =new BranchStore();
		branchStore.setStoreNo(Integer.parseInt(shopCode));
		List<BranchStore> selectBranchStoreList = branchStoreSerivce.selectBranchStoreList(branchStore);
		accountFlow.setBranchStoreId(shopCode);
	    BranchStore selectBranchStoreById = selectBranchStoreList.size()==0? null :selectBranchStoreList.get(0);
	    if(selectBranchStoreById !=null){
	    	accountFlow.setBranchStoreName(selectBranchStoreById.getStoreName());
	    }
	    accountFlow.setInvoiceAmount(new BigDecimal(invoiceAmount));
	    accountFlow.setInvoiceNo(invoiceNo);
	    accountFlow.setMoney(new BigDecimal(netAmount));
	    accountFlow.setCreateDate(new Date());
	    accountFlow.setTranDes("pos機消費");
	    accountFlow.setTranType(2);
	    accountFlow.setNetAmount(new BigDecimal(netAmount));
	    accountFlow.setCouponCode(coupons);
	    accountFlow.setCreateBy(selectMembersList !=null? selectMembersList.get(0).getName(): null);
		accountFlowService.insertAccountFlow(accountFlow);
		if(!StringUtils.isEmpty(memberID)) {
			ScoreHis scoreHis =new ScoreHis();
			//查詢積分規則
			log.info("closeTransaction 3:计算积分 ");
			IntegralRole byRole = roleService.selectByRoleByintegralType(selectMembersList.get(0).getMembersType(),0);
			if(byRole ==null){
				log.info("closeTransaction 3.1:積分規則爲空,本次交易沒計算積分 ");
			}else{
				if(Double.valueOf(netAmount) < 2 && selectMembersList.get(0).getMembersType() ==0
						|| Double.valueOf(netAmount) < 1 && selectMembersList.get(0).getMembersType() ==1){
					log.info("本次交易小於規定金額,不計入積分!");
				}else {

					Double score = Double.parseDouble(byRole.getScoreValue()) * Double.parseDouble(invoiceAmount);
					//記錄積分

					scoreHis.setDescribes("pos機消費積分:" + score);
					scoreHis.setMembersId(selectMembersList.get(0).getId());
					scoreHis.setOlbScore(0);
					scoreHis.setNewScore(score.intValue());
					//ScoreHis hisByUserId = scoreHisService.selectScoreHisByUserId(selectMembersList.get(0).getId());
					scoreHis.setOlbScore(members.getScore());
					scoreHis.setNewScore(scoreHis.getNewScore() + members.getScore());
					scoreHis.setCreatedDate(new Date());
					scoreHis.setBusiId(invoiceNo);
					scoreHisService.insertScoreHis(scoreHis);
					//判斷用戶消費，是否滿足自動升級會員
					//查詢是否有活動
//					if (selectMembersList.get(0).getMembersType() == 0) {
						IntegralRole selectByRoleByintegralType = roleService.selectByRoleByintegralType(selectMembersList.get(0).getMembersType(), 2);
						int money = 0;
						if (selectByRoleByintegralType == null) {
							String queryCigKey = HttpConstants.autoUpgradingMoney;
							if (selectMembersList.get(0).getMembersType() == HttpConstants.EmmbersType_1) {
								queryCigKey = HttpConstants.autoUpgradingMoneyVIP;
							}
							SysConfig selectByKey = sysConfigService.selectByKey(queryCigKey);
							money = Integer.parseInt(selectByKey.getConfigValue());
						} else {
							money =Double.valueOf(selectByRoleByintegralType.getScoreValue()).intValue();
						}
						int moneyByMemId = accountFlowService.selectAccountMoneyByMemId(selectMembersList.get(0).getId());
						Date dateTemp =StringUtils.isEmpty(selectMembersList.get(0).getSpareField2()) ?null: DateUtils.dateTime("yyyyMMdd", selectMembersList.get(0).getSpareField2());
						if (moneyByMemId >= money &&( dateTemp == null || DateUtils.addYears(new Date(), -1).after(dateTemp))) {
							//如果是會員
							Date vipDateEnd = new Date();
							if (selectMembersList.get(0).getMembersType() == HttpConstants.EmmbersType_1) {
								Date dateTime = DateUtils.dateTime("yyyyMMdd", selectMembersList.get(0).getVipDate());
								if (dateTime.after(new Date())) {
									//如果會員沒有過期，則過期時間 +1年  20191230 + 1 =20201230
									vipDateEnd = DateUtils.addYears(dateTime, 1);
								}
							}
							selectMembersList.get(0).setUpgradeDate(DateUtils.parseDateToStr("yyyyMMdd", new Date()));
							selectMembersList.get(0).setVipDate(DateUtils.parseDateToStr("yyyyMMdd", vipDateEnd));
							selectMembersList.get(0).setSpareField2(DateUtils.parseDateToStr("yyyyMMdd", new Date()));
							selectMembersList.get(0).setMembersType(1);
							noticeInfoService.insertNoticeInfo("消費金額滿" + money + " 積分自動升級通知", members.getId(), 0, "noticeType", "恭喜您：本年度" + DateUtils.dateTime() + ", 消費金額滿" + money + " 積分自動升級,享受VIP優惠,該優惠于：" + selectMembersList.get(0).getVipDate() + "失效.");
							
							accountFlowService.updateAccountFlow(accountFlow);
						}
//					}
//				AcctBalance selectAcctBalanceById = accBalanceService.selectAcctBalanceById(1);
//				selectAcctBalanceById.setCanBalance(selectAcctBalanceById.getCanBalance().add(accountFlow.getMoney()));
//				accBalanceService.updateAcctBalance(selectAcctBalanceById);
					//修改用户积分
					selectMembersList.get(0).setScore(scoreHis.getNewScore());
					memBersMapper.updateMembers(selectMembersList.get(0));
				}
			}
			noticeInfoService.insertNoticeInfo("消費積分通知", members.getId(), 0, "noticeType", "尊敬的會員,您本次消費積分:"+scoreHis.getNewScore()+",纍計積分為:"+selectMembersList.get(0).getScore());
		}
		log.info("closeTransaction end ,result ={} ",response);
		lock.unlock();
		response.setResult(0);
		response.setStatus(0);
		return response;
	}

	/* (non-Javadoc)
	 * @see com.menu.manger.service.IPosTransactionSerivce#backTransaction(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ReverseTransactionResponse backTransaction(String brandID, String transactionDatetime,
			String invoiceNo, String t, String h) throws Exception {
		ReverseTransactionResponse response =new ReverseTransactionResponse();
//		if(!checkHash(h,brandID,transactionDatetime,invoiceNo)){
//			return SOAPResult.error(1,"HASH不匹配!");
//		}
		log.info("request reverseTransaction ,parmat:  ",brandID,transactionDatetime,invoiceNo,t,h);
		Lock lock =new ReentrantLock();
		lock.lock();
		AccountFlow accountFlow =new AccountFlow();
		accountFlow.setIsVaild("yes");
		accountFlow.setInvoiceNo(invoiceNo);
		accountFlow.setBranchStoreId(brandID);
		List<AccountFlow> selectAccountFlowList = accountFlowService.selectAccountFlowList(accountFlow);
		log.info("reverseTransaction 1:查詢交易流水: ",selectAccountFlowList);
		if(selectAccountFlowList ==null || selectAccountFlowList.size() ==0){
			response.setStatus(1);
			response.setErrCode(2);;
			response.setErrMsg("未查詢到該筆交易!請確認業務編號:"+invoiceNo);
			return response;
		}
		//將賬戶流水設置為失效
		log.info("reverseTransaction 1.2:將賬戶流水設置為失效");
		selectAccountFlowList.get(0).setIsVaild("no");
		selectAccountFlowList.get(0).setTranDes("交易請求撤銷");
		selectAccountFlowList.get(0).setTranType(HttpConstants.transtionStatus_3);
		accountFlowService.updateAccountFlow(selectAccountFlowList.get(0));
		
		ScoreHis queryScoreHis =new ScoreHis();
		queryScoreHis.setBusiId(invoiceNo);
		queryScoreHis.setIsVaild("yes");
		//設置積分為失效
		List<ScoreHis> scoreHisList = scoreHisService.selectScoreHisList(queryScoreHis);
		log.info("reverseTransaction 2:查詢積分記錄",scoreHisList);
		if(scoreHisList !=null && scoreHisList.size() ==1){
			scoreHisList.get(0).setIsVaild("no");
			scoreHisService.updateScoreHis(scoreHisList.get(0));
		}else{
			throw new Exception("積分記錄爲空!數據有誤!");
		}
		Members membersByEmail = memBersMapper.selectMembersById(selectAccountFlowList.get(0).getMenuId());
		log.info("reverseTransaction 2:查詢會員記錄",membersByEmail);
		if(membersByEmail ==null){
			throw new Exception("根據賬號未查到會員信息!");
		}
		int delScord =membersByEmail.getScore() -scoreHisList.get(0).getNewScore();
		if(delScord > 0){
			throw new Exception("該筆積分已經使用,交易撤銷失敗,現有積分:"+membersByEmail.getScore()+",撤銷交易需要扣除積分:"+scoreHisList.get(0).getNewScore());
		}
		membersByEmail.setScore(delScord);
		memBersMapper.updateMembers(membersByEmail);
		
		//查詢優惠券
		List<MenuFoodExchange> selectMenuFoodExchangeByIds = memuFoodExchange.selectMenuFoodExchangeByIds(accountFlow.getCouponCode());
		log.info("submitRedemption 3: 查詢優惠券 ",selectMenuFoodExchangeByIds);
		if(selectMenuFoodExchangeByIds ==null || selectMenuFoodExchangeByIds.size() ==0){
			throw new Exception("優惠券ID有誤!");
		}
		for (MenuFoodExchange foodExchange : selectMenuFoodExchangeByIds) {
			if(StringUtils.isNoneBlank(foodExchange.getInvalidDate()) &&DateUtils.dateTime(foodExchange.getInvalidDate(),"yyyyMMdd").before(new Date())){
				throw new Exception("该優惠券已经过期!");
			}
			if(foodExchange.getExchangeStatus() ==0){
				throw new Exception("该優惠券未使用,状态为 未使用");
			}
			foodExchange.setIsVaild("Y");
			foodExchange.setExchangeStatus(0);
			memuFoodExchange.updateMenuFoodExchange(foodExchange);
		}
		response.setResult(0);;
		return response;
	}
	/**
	 * 校验hash值
	 * @param hash
	 * @param param
	 * @return
	 */
	public boolean checkHash(String hash,String...param){
		String newHash =null;
		for(String str: param){
			newHash+=str;
		}
		return hash.equals(getStringMD5(newHash));
	}
	 /**
     * MD5校验字符串
     * @param s String to be MD5
     * @return 'null' if cannot get MessageDigest
     */
    
    private static String getStringMD5( String s) {
        MessageDigest mdInst;
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
 
        byte[] btInput = s.getBytes();
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
       
        return new String(md);
    }
    public static void main(String[] args) {
    	Date dateTemp =DateUtils.dateTime("yyyyMMdd", "2018050");
    	System.out.println(DateUtils.addYears(new Date(), -1).after(dateTemp));
	}
}
