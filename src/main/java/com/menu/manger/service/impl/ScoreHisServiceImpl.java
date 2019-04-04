package com.menu.manger.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.AccountFlow;
import com.menu.manger.dto.AcctBalance;
import com.menu.manger.dto.IntegralRole;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.MenuFood;
import com.menu.manger.dto.MenuFoodExchange;
import com.menu.manger.dto.ScoreHis;
import com.menu.manger.mapper.AccountFlowMapper;
import com.menu.manger.mapper.AcctBalanceMapper;
import com.menu.manger.mapper.MenuFoodExchangeMapper;
import com.menu.manger.mapper.MenuFoodMapper;
import com.menu.manger.mapper.ScoreHisMapper;
import com.menu.manger.service.IIntegralRoleService;
import com.menu.manger.service.IMembersService;
import com.menu.manger.service.INoticeInfoService;
import com.menu.manger.service.IScoreHisService;
import com.menu.manger.util.Convert;
import com.menu.manger.util.DateUtils;

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
    private static final Logger log = LoggerFactory.getLogger(ScoreHisServiceImpl.class);

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
	public void upgradeVIP(String email, Double ammount, Members members) {
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
		accountFlow.setNetAmount(new BigDecimal(ammount));
		balanceMapperMapper.insertAccountFlow(accountFlow);
		accountFlow.setTranDes("升級VIP！");
		log.info("3.計算積分");
		//记录积分
		ScoreHis scoreHis =new ScoreHis();
		IntegralRole byRole = roleService.selectByRole(1);
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
		}
		log.info("4.計算VIP年限:"+newScore);
		Members selectMembersById = membersService.selectMembersById(members.getId());
		selectMembersById.setScore(selectMembersById.getScore()+ammount.intValue());
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
		selectMembersById.setScore(selectMembersById.getScore()+newScore.intValue());
		selectMembersById.setMembersType(1);
		membersService.updateMembers(selectMembersById);
		iscoreHis.insertScoreHis(scoreHis);
		log.info("5.贈送菜品:"+newScore);
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
					}
				}
				noticeInfoService.insertNoticeInfo("购买VIP赠送优惠券", members.getId(), menuFood2.getId(), "MenuFood", menuFood2.getPicUrl());
				foodMapper.updateMenuFood(menuFood2);
			}
		}
		log.info("6.完畢:"+newScore);
	}
}
