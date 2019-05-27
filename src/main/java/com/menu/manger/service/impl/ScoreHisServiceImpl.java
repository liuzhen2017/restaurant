package com.menu.manger.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.AccountFlow;
import com.menu.manger.dto.AcctBalance;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.MenuFood;
import com.menu.manger.dto.MenuFoodExchange;
import com.menu.manger.dto.MyCoupon;
import com.menu.manger.dto.ScoreHis;
import com.menu.manger.dto.SysConfig;
import com.menu.manger.mapper.AccountFlowMapper;
import com.menu.manger.mapper.AcctBalanceMapper;
import com.menu.manger.mapper.MenuFoodExchangeMapper;
import com.menu.manger.mapper.MenuFoodMapper;
import com.menu.manger.mapper.MyCouponMapper;
import com.menu.manger.mapper.ScoreHisMapper;
import com.menu.manger.service.IIntegralRoleService;
import com.menu.manger.service.IMembersService;
import com.menu.manger.service.IMyCouponService;
import com.menu.manger.service.INoticeInfoService;
import com.menu.manger.service.IScoreHisService;
import com.menu.manger.service.ISysConfigService;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.Convert;
import com.menu.manger.util.DateUtils;
import com.stripe.Stripe;
import com.stripe.model.Charge;

/**
 * 积分记录 服务层实现
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Service
public class ScoreHisServiceImpl implements IScoreHisService 
{
	@Autowired
	private ScoreHisMapper scoreHisMapper;
	@Autowired
	AcctBalanceMapper balanceMapper;
	@Autowired
	AccountFlowMapper balanceMapperMapper;
	@Autowired
	IScoreHisService iscoreHis;
	@Autowired
	IMembersService membersService;
	@Autowired
	MenuFoodExchangeMapper exchangeMapper;
	@Autowired
	IIntegralRoleService roleService;
	@Autowired
	MenuFoodMapper foodMapper;
	@Autowired
	INoticeInfoService noticeInfoService;
	@Autowired
	private MyCouponMapper myCouponMapper;
    private static final Logger log = LoggerFactory.getLogger(ScoreHisServiceImpl.class);

    @Autowired
	ISysConfigService configService;
	@Autowired
	IScoreHisService hisService;
	@Autowired
	IMyCouponService myCouponService;
	
	
	
	/**
     * 查询积分记录信息
     * 
     * @param id 积分记录ID
     * @return 积分记录信息
     */
    @Override
	public ScoreHis selectScoreHisById(Integer id)
	{
	    return scoreHisMapper.selectScoreHisById(id);
	}
	
	/**
     * 查询积分记录列表
     * 
     * @param scoreHis 积分记录信息
     * @return 积分记录集合
     */
	@Override
	public List<ScoreHis> selectScoreHisList(ScoreHis scoreHis)
	{
	    return scoreHisMapper.selectScoreHisList(scoreHis);
	}
	
    /**
     * 新增积分记录
     * 
     * @param scoreHis 积分记录信息
     * @return 结果
     */
	@Override
	public int insertScoreHis(ScoreHis scoreHis)
	{
	    return scoreHisMapper.insertScoreHis(scoreHis);
	}
	
	/**
     * 修改积分记录
     * 
     * @param scoreHis 积分记录信息
     * @return 结果
     */
	@Override
	public int updateScoreHis(ScoreHis scoreHis)
	{
	    return scoreHisMapper.updateScoreHis(scoreHis);
	}

	/**
     * 删除积分记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteScoreHisByIds(String ids)
	{
		return scoreHisMapper.deleteScoreHisByIds(Convert.toStrArray(ids));
	}

	@Override
	public ScoreHis selectScoreHisByUserId(Integer menId) {
		return scoreHisMapper.selectScoreHisByUserId(menId);
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void calScore(String email, Double ammount, Members members) {
		/*log.info("1.賬戶餘額增加");
		//账户加钱
		AcctBalance selectAcctBalanceById = balanceMapper.selectAcctBalanceById(1);
		selectAcctBalanceById.getCanBalance().add(new BigDecimal(ammount));
		balanceMapper.updateAcctBalance(selectAcctBalanceById);
		//流水写入记录
		log.info("2.寫入流水");
		AccountFlow accountFlow =new AccountFlow();
		accountFlow.setAccNo(""+0);
		accountFlow.setTranType(1);//支出
		accountFlow.setMoney(new BigDecimal(ammount));
		accountFlow.setMenuId(members.getId());
		accountFlow.setMenuAccNo(email);
		accountFlow.setCreateDate(new Date());
		accountFlow.setBranchStoreName("綫上支護");
		accountFlow.setNetAmount(new BigDecimal(ammount));
		accountFlow.setTranDes("支付成功！");
		balanceMapperMapper.insertAccountFlow(accountFlow);
		
		//记录积分
		ScoreHis scoreHis =new ScoreHis();
		
		ScoreHis dbScord = iscoreHis.selectScoreHisByUserId(members.getId());
		
		scoreHis.setMembersId(members.getId());
		scoreHis.setCreateBy("admin");
		scoreHis.setDescribes("購買會員送"+ammount+"積分");
		scoreHis.setMembersName(members.getName());
		if(dbScord !=null){
		   scoreHis.setOlbScore(dbScord.getNewScore());
		   scoreHis.setNewScore(dbScord.getNewScore()+ammount.intValue());
		}else{
			scoreHis.setOlbScore(0);
			   scoreHis.setNewScore(ammount.intValue());
		}
		log.info("3.計算積分");
		Members selectMembersById = membersService.selectMembersById(members.getId());
		selectMembersById.setScore(selectMembersById.getScore()+ammount.intValue());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());//设置起时间
		 cal.add(Calendar.YEAR, 1);//增加一年
		selectMembersById.setUpgradeDate(DateUtils.parseDateToStr("yyyyMMdd", new Date()));
		selectMembersById.setVipDate(DateUtils.parseDateToStr("yyyyMMdd", cal.getTime()));
		selectMembersById.setMembersType(1);
		membersService.updateMembers(selectMembersById);
		iscoreHis.insertScoreHis(scoreHis);
		log.info("3.贈送菜品");
		//贈送菜品
		MenuFood menuFood =new MenuFood();
		menuFood.setRulesType(1); //贈送菜品
		menuFood.setIsExchange("yes");
		menuFood.setIsValid("yes");
		menuFood.setInvalidDate("yes");
		List<MenuFood> selectMenuFoodList = foodMapper.selectMenuFoodList(menuFood);
		MenuFoodExchange menuFoodExchange =null;
		for (MenuFood menuFood2 : selectMenuFoodList) {
			if(menuFood2.getUsedNums() == null ||menuFood2.getUsedNums() == -1 || menuFood2.getTotalNums() >= menuFood2.getUsedNums()) {
				menuFoodExchange =new MenuFoodExchange();
				menuFoodExchange.setExchangeStatus(0);
				menuFoodExchange.setCreateDate(new Date());
				menuFoodExchange.setMembersId(members.getId());
				menuFoodExchange.setIsVaild("yes");
				if(!StringUtils.isEmpty(menuFood2.getInvalidDate())){
					menuFoodExchange.setInvalidDate(menuFood2.getInvalidDate());
				}
				if(!StringUtils.isEmpty(menuFood2.getTakeEffectDate())){
					menuFoodExchange.setTakeEffectDate(menuFood2.getTakeEffectDate());
				}
				menuFoodExchange.setMenuFoodId(menuFood2.getId());
				menuFoodExchange.setMenbersName(members.getName());
				menuFoodExchange.setMenuFoodName(menuFood2.getTitle());
				menuFoodExchange.setMenuFoodPic(menuFood2.getPicUrl());
				menuFoodExchange.setSpareField1(menuFood2.getSpareField1());
				menuFoodExchange.setSpareField2("升級VIP贈送");
				menuFoodExchange.setSpareField3(menuFood2.getTitle());
				exchangeMapper.insertMenuFoodExchange(menuFoodExchange);
				if(menuFood2.getUsedNums() != null){
					menuFood2.setUsedNums(menuFood2.getUsedNums()+1);
					menuFood2.setTotalNums(menuFood2.getTotalNums()-1);
					if(menuFood2.getTotalNums() ==0) {
						menuFood2.setIsValid("no");
						menuFood2.setIsSellOut("yes");
					}
				}
				noticeInfoService.insertNoticeInfo("购买VIP赠送优惠券", members.getId(), menuFood2.getId(), "MenuFood", menuFood2.getPicUrl());
				foodMapper.updateMenuFood(menuFood2);
			}
		}*/
		
	}
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void upgradeVIP(String email, Double ammount, Members members,String code) {
		log.info("1.賬戶餘額增加");
		//账户加钱
		AcctBalance selectAcctBalanceById = balanceMapper.selectAcctBalanceById(1);
		selectAcctBalanceById.getCanBalance().add(new BigDecimal(ammount));
		balanceMapper.updateAcctBalance(selectAcctBalanceById);
		//流水写入记录
		log.info("2.寫入流水");
		AccountFlow accountFlow =new AccountFlow();
		accountFlow.setAccNo(""+0);
		accountFlow.setTranType(1);//支出
		accountFlow.setMoney(new BigDecimal(ammount));
		accountFlow.setMenuId(members.getId());
		accountFlow.setMenuAccNo(email);
		accountFlow.setCreateDate(new Date());
		accountFlow.setBranchStoreName("綫上支護");
		accountFlow.setNetAmount(new BigDecimal(ammount/100));
		balanceMapperMapper.insertAccountFlow(accountFlow);
		accountFlow.setTranDes("升級VIP！");
		log.info("3.計算積分");
		//记录积分
		/*ScoreHis scoreHis =new ScoreHis();
		IntegralRole byRole = roleService.selectByRoleByintegralType(members.getMembersType(),null);
		ScoreHis dbScord = iscoreHis.selectScoreHisByUserId(members.getId());
		Double newScore= ammount;
		if(byRole !=null){
			newScore= byRole.getScoreValue() *ammount;		

		}
		log.info("3.新增積分為:"+newScore);
		scoreHis.setMembersId(members.getId());
		scoreHis.setCreateBy("admin");
		scoreHis.setCreatedDate(new Date());
		scoreHis.setDescribes("升級會員送"+newScore+"積分");
		scoreHis.setMembersName(members.getName());
		if(dbScord !=null){
		   scoreHis.setOlbScore(dbScord.getNewScore());
		   scoreHis.setNewScore(dbScord.getNewScore()+newScore.intValue());
		}else{
			scoreHis.setOlbScore(0);
			   scoreHis.setNewScore(ammount.intValue());
		}*/
		log.info("4.計算VIP年限:");
		Members selectMembersById = membersService.selectMembersById(members.getId());
		//selectMembersById.setScore(selectMembersById.getScore()+ammount.intValue());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());//设置起时间
		 cal.add(Calendar.YEAR, 1);//增加一年
		 Date vipDateEnd =cal.getTime();
		if(selectMembersById.getMembersType() ==HttpConstants.EmmbersType_1){
		    members.setVipDate(StringUtils.isEmpty(members.getVipDate())? DateUtils.dateTime():members.getVipDate());
			Date dateTime = DateUtils.dateTime("yyyyMMdd",members.getVipDate());
			if(dateTime.after(new Date())){
				//如果會員沒有過期，則過期時間 +1年  20191230 + 1 =20201230
				vipDateEnd =DateUtils.addYears(dateTime, 1);
			}
		}
		selectMembersById.setUpgradeDate(DateUtils.parseDateToStr("yyyyMMdd", new Date()));
		selectMembersById.setVipDate(DateUtils.parseDateToStr("yyyyMMdd", vipDateEnd));
		//selectMembersById.setScore(selectMembersById.getScore()+newScore.intValue());
		selectMembersById.setMembersType(1);
		membersService.updateMembers(selectMembersById);
		//iscoreHis.insertScoreHis(scoreHis);
		log.info("5.贈送菜品:");
		//贈送菜品
		MenuFood menuFood =new MenuFood();
		menuFood.setRulesType(1); //贈送菜品
		menuFood.setIsExchange("yes");
		menuFood.setIsValid("yes");
		menuFood.setInvalidDate("yes");
		List<MenuFood> selectMenuFoodList = foodMapper.selectMenuFoodList(menuFood);
		MenuFoodExchange menuFoodExchange =null;
		for (MenuFood menuFood2 : selectMenuFoodList) {
			if(menuFood2.getUsedNums() == null ||menuFood2.getUsedNums() == -1 || menuFood2.getTotalNums() >= menuFood2.getUsedNums()) {
				menuFoodExchange =new MenuFoodExchange();
				menuFoodExchange.setExchangeStatus(0);
				menuFoodExchange.setCreateDate(new Date());
				menuFoodExchange.setMembersId(members.getId());
				menuFoodExchange.setIsVaild("yes");
				menuFoodExchange.setInvalidDate(DateUtils.parseDateToStr("yyyyMMdd HH:mm:ss",DateUtils.addYears(new Date(),1)));
				menuFoodExchange.setTakeEffectDate(menuFood2.getTakeEffectDate());
				
				menuFoodExchange.setMenuFoodId(menuFood2.getId());
				menuFoodExchange.setMenbersName(members.getName());
				menuFoodExchange.setMenuFoodName(menuFood2.getTitle());
				menuFoodExchange.setMenuFoodPic(menuFood2.getPicUrl());
				menuFoodExchange.setSpareField1(menuFood2.getSpareField1());
				menuFoodExchange.setSpareField2("獲贈VIP迎新電子劵！");
				menuFoodExchange.setSpareField3(menuFood2.getTitle());
				exchangeMapper.insertMenuFoodExchange(menuFoodExchange);
				if(menuFood2.getUsedNums() != null){
					menuFood2.setUsedNums(menuFood2.getUsedNums()+1);
					menuFood2.setTotalNums(menuFood2.getTotalNums()-1);
					if(menuFood2.getTotalNums() ==0) {
						menuFood2.setIsValid("no");
					}
				}
				noticeInfoService.insertNoticeInfo("獲贈VIP迎新電子劵！", members.getId(), menuFood2.getId(), "MenuFood", menuFood2.getPicUrl());
				foodMapper.updateMenuFood(menuFood2);
			}
		}
		//消费优惠券
		if(! StringUtils.isEmpty(code)){
		   MyCoupon selectMyCouponByCode = myCouponMapper.selectMyCouponByCode(code);
		   if(selectMyCouponByCode != null){
			   selectMyCouponByCode.setIsVaild("no");
			   selectMyCouponByCode.setStatus(3);
			   myCouponMapper.updateMyCoupon(selectMyCouponByCode);
			   
		   }
		}
		
		
		log.info("6.完畢:");
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public AjaxResult payTest(String email, Double ammount, Members members,
			String code,String tokenId) throws Exception {
		Stripe.apiKey = configService.selectConfigByKey("pay_user_service_key");

		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", ammount.intValue());
		chargeParams.put("currency", "HKD");
		chargeParams.put("description", "購買會員");
		chargeParams.put("source", tokenId);
		double price = 0;
		
		
		AjaxResult ajax =membersService.regist(members);
		if(!ajax.get("code").toString().equals("0")){
			return ajax; 
		}
		Map<String, Object> map =(Map<String, Object>)ajax.get("data");
		Members membersRegis= (Members)map.get("membersInfo");
		log.info("購買VIP 1.核對金額");
		if (StringUtils.isNoneEmpty(code) && !"null".equals(code)) {
			AjaxResult calVIPCode = myCouponService.calVIPCode(code);
			if (calVIPCode.get("code").toString().equals("1")) {
				return AjaxResult.error("計算交易金額錯誤!");
			}
			Map<String, Object> data = new HashMap<>();
			data = (Map<String, Object>) calVIPCode.get("data");
			if(data ==null){
				throw new Exception(calVIPCode.get("msg").toString());
			}
			price = (double) data.get("newPrice");
		} else {
			SysConfig selectByKey = configService
					.selectByKey(HttpConstants.VIPPrice);
			price = Double.parseDouble(selectByKey.getConfigValue());
		}
		if (price/10 != ammount) {
			throw new Exception("交易金額被和系統不匹配!");
		}
			log.info("購買VIP 2.調用支付系統");
			;
		Charge charge =  Charge.create(chargeParams);
		if (charge.getStatus().equals("succeeded")) {
			log.info("購買VIP 3.寫入積分處理");
			;
			hisService.upgradeVIP(email, ammount, membersRegis,code);

			// 發送贈送菜

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("myscorp", membersRegis.getScore());
			return AjaxResult.success("升級VIP會員成功,贈送菜品已經發放到優惠券!",ajax.get("data"));
		}else{
			throw new Exception("交易失败!:"+charge.getStatus());
		}
		
	}
}
