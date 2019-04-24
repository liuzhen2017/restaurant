package com.menu.manger.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.MenuFood;
import com.menu.manger.dto.MenuFoodExchange;
import com.menu.manger.dto.MyCoupon;
import com.menu.manger.dto.SysConfig;
import com.menu.manger.mapper.MembersMapper;
import com.menu.manger.mapper.MenuFoodExchangeMapper;
import com.menu.manger.mapper.MenuFoodMapper;
import com.menu.manger.mapper.MyCouponMapper;
import com.menu.manger.service.IAccountFlowService;
import com.menu.manger.service.IMenuFoodService;
import com.menu.manger.service.INoticeInfoService;
import com.menu.manger.service.ISendMessagerService;
import com.menu.manger.service.ISysConfigService;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.Convert;
import com.menu.manger.util.DateUtils;
import com.menu.manger.util.RC4;
import com.menu.manger.util.SMSDto;
import com.menu.manger.util.SendEmailUtil;
import com.menu.manger.util.SendEmaill;
import com.menu.manger.util.ServiceUtil;
import com.menu.manger.util.ThreadLocalUtil;

/**
 * 餐牌 服务层实现
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Service
public class MenuFoodServiceImpl implements IMenuFoodService 
{
	@Autowired
	private MenuFoodMapper menuFoodMapper;

	@Autowired
	private MyCouponMapper myCouponMapper;
	@Autowired
	private MembersMapper membersMapper ;
	
	@Autowired
	private MenuFoodExchangeMapper menuFoodExchangeMapper; ;
	@Autowired
	INoticeInfoService noticeInfoService;
	
	@Autowired
	private SendEmailUtil sendEmailUtil;
	@Autowired
	ISysConfigService configService;
	@Autowired
	ISendMessagerService sendMessService;
	@Autowired
	private INoticeInfoService notiCeInfoService;
	@Autowired
	IAccountFlowService accountFlowService;
	/**
     * 查询餐牌信息
     * 
     * @param id 餐牌ID
     * @return 餐牌信息
     */
    @Override
	public MenuFood selectMenuFoodById(Integer id)
	{
	    return menuFoodMapper.selectMenuFoodById(id);
	}
	
	/**
     * 查询餐牌列表
     * 
     * @param menuFood 餐牌信息
     * @return 餐牌集合
     */
	@Override
	public List<MenuFood> selectMenuFoodList(MenuFood menuFood)
	{
		//個別
		if("individual".equals(menuFood.getTypeName())){
			menuFood.setTypeName(null);
			menuFood.setIsHot(88+"");
			//特別
		}else if("special".equals(menuFood.getTypeName())){
			menuFood.setTypeName(null);
			menuFood.setIsHot(1+"");
		}
		return menuFoodMapper.selectMenuFoodList(menuFood);
	}
	
    /**
     * 新增餐牌
     * 
     * @param menuFood 餐牌信息
     * @return 结果
     */
	@Override
	public int insertMenuFood(MenuFood menuFood)
	{
//		menuFood.setCreateBy(ShiroUtils.getSysUser().getLoginName()+"");
		menuFood.setCreateDate(new Date());
	    return menuFoodMapper.insertMenuFood(menuFood);
	}
	
	/**
     * 修改餐牌
     * 
     * @param menuFood 餐牌信息
     * @return 结果
     */
	@Override
	public int updateMenuFood(MenuFood menuFood)
	{
	    return menuFoodMapper.updateMenuFood(menuFood);
	}

	/**
     * 删除餐牌对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMenuFoodByIds(String ids)
	{
		return menuFoodMapper.deleteMenuFoodByIds(Convert.toStrArray(ids));
	}

	@Override
	public MenuFood getFoodByScose(Integer score) {
		return menuFoodMapper.getFoodByScose(score);
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public AjaxResult requestExchege(Integer id, Integer num, String code, Integer memId) {
		//查询优惠代码,看存不存在
 		Lock lock =new ReentrantLock();
		lock.lock();
		try {
			MenuFood selectMenuFoodById = menuFoodMapper.selectMenuFoodById(id);
			if(selectMenuFoodById ==null){
				return AjaxResult.error("該積分商品不存在");
			}
			
			Members selectMembersById = membersMapper.selectMembersById(memId);
			if(!StringUtils.isEmpty(code)){
				MyCoupon selectMyCouponById = myCouponMapper.selectMyCouponByCode(code);
				if(selectMyCouponById ==null){
					return AjaxResult.error("該優惠代碼不存在或者已經過期,,請確認再操作");
				}
				if(selectMenuFoodById.getExchangePointsScord() * num  * selectMyCouponById.getCouponValues() < selectMembersById.getScore()){
					return AjaxResult.error("兌換失敗,優惠券積分不足以兌換!");
				}
				if(!StringUtils.isEmpty(selectMyCouponById.getSpareField2()) && selectMenuFoodById.getId() != Integer.parseInt(selectMyCouponById.getSpareField2())){
                    return AjaxResult.error("兌換失敗,該優惠券不能兌換當前商品!");
                }
                if(StringUtils.isEmpty(code) && selectMembersById.getScore() < selectMenuFoodById.getExchangePointsScord()*num * selectMyCouponById.getCouponValues()){
                    return AjaxResult.error("兌換失敗,積分不足以兌換!");
                }
			}
			if(selectMenuFoodById.getSpareField5() !=null && Integer.parseInt(selectMenuFoodById.getSpareField5()) ==HttpConstants.EmmbersType_1 && selectMembersById.getMembersType() ==HttpConstants.EmmbersType_1){
				return AjaxResult.error("該商品僅限於VIP用戶兌換,請升級VIP");
			}
			if(selectMenuFoodById.getIsSellOut().equals("yes")){
				return AjaxResult.error("商品已經售完!");
			}
			selectMenuFoodById.setUsedNums(selectMenuFoodById.getUsedNums()==null ? 0:selectMenuFoodById.getUsedNums());
			if(selectMenuFoodById.getTotalNums() !=null &&selectMenuFoodById.getTotalNums() != -1 &&selectMenuFoodById.getTotalNums() -selectMenuFoodById.getUsedNums()-num < 0 ){
				return AjaxResult.error("商品庫存不足!");
			}
			if(StringUtils.isEmpty(code) && selectMembersById.getScore() < selectMenuFoodById.getExchangePointsScord()*num ){
				return AjaxResult.error("兌換失敗,積分不足以兌換!");
			}
			//兌換
			MenuFoodExchange menuFoodExchange =null;
			for(int i=0;i<num;i++){
				menuFoodExchange =new MenuFoodExchange();
				menuFoodExchange.setMembersId(memId);
				menuFoodExchange.setMenbersName(selectMembersById.getName());
				menuFoodExchange.setMenuFoodId(selectMenuFoodById.getId());
				menuFoodExchange.setMenuFoodName(selectMenuFoodById.getTitle());
				menuFoodExchange.setMenuFoodPic(selectMenuFoodById.getPicUrl());
				menuFoodExchange.setCouponType(selectMenuFoodById.getTypess());
				menuFoodExchange.setInvalidDate(selectMenuFoodById.getInvalidDate());
				menuFoodExchange.setSpareField1(selectMenuFoodById.getSpareField1());
				menuFoodExchange.setTakeEffectDate(selectMenuFoodById.getTakeEffectDate());
				menuFoodExchange.setCouponType(selectMenuFoodById.getTypess());
				menuFoodExchange.setCouponCode(RC4.encry_RC4_string(String.format("%07d",(int) (Math.random()*1000))+"",UUID.randomUUID().toString()));
				menuFoodExchange.setExchangeStatus(0);
				menuFoodExchange.setSpareField2("消費積分兌換");
				menuFoodExchange.setCreateDate(new Date());
				menuFoodExchange.setSpareField3(selectMenuFoodById.getTitle());
				menuFoodExchangeMapper.insertMenuFoodExchange(menuFoodExchange);
				noticeInfoService.insertNoticeInfo("積分兌換商品成功!", selectMembersById.getId(), menuFoodExchange.getId(), "MenuFoodExchange", menuFoodExchange.getMenuFoodPic());
			}
			if(selectMenuFoodById.getTotalNums() !=null && selectMenuFoodById.getTotalNums() !=-1){
				selectMenuFoodById.setUsedNums(selectMenuFoodById.getTotalNums() - num);
				menuFoodMapper.updateMenuFood(selectMenuFoodById);
			}
			//修改用戶積分
			Integer tet =(int) (selectMembersById.getScore() -selectMenuFoodById.getExchangePointsScord()*num);
			tet =tet >0? tet:0;
			selectMembersById.setScore(tet);
			membersMapper.updateMembers(selectMembersById);
			selectMembersById.setPwd(null);
			return AjaxResult.success("積分兌換商品成功!",ServiceUtil.tokenByUser(selectMembersById));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
			
		}
		return AjaxResult.error();
		
		
		
	}

	@Override
	public List<String> selectFoodType() {
		return menuFoodMapper.selectFoodType();
	}

	@Override
	public MenuFood getFoodByScoseEnd(Integer score) {
		return menuFoodMapper.getFoodByScoseEnd(score);
	}

	@Override
	public AjaxResult giveAway(Integer id,String phone) {
		MenuFoodExchange selectMenuFoodExchangeById = menuFoodExchangeMapper.selectMenuFoodExchangeById(id);
		if(selectMenuFoodExchangeById ==null){
			return AjaxResult.error("該優惠券不存在數據庫!");
		}
		if(HttpConstants.ISVAILD_NO.equals(selectMenuFoodExchangeById.getIsVaild()) || selectMenuFoodExchangeById.getExchangeStatus() !=0){
			return AjaxResult.error("該優惠狀態不正確,不允許贈送!");
		}
		if(!StringUtils.isEmpty(selectMenuFoodExchangeById.getInvalidDate()) && DateUtils.dateTime("yyyy-MM-dd", selectMenuFoodExchangeById.getInvalidDate()).before(new Date())){
			return AjaxResult.error("該優惠狀態已經過期,不允許贈送!"); 
		}
		
		Members giveMembers = membersMapper.selectMembersByPhone(phone);
		Members loginMem = (Members)ThreadLocalUtil.getUserInfo();
		if(selectMenuFoodExchangeById.getMembersId().intValue() != loginMem.getId()){
			return AjaxResult.error("您沒有擁有該優惠券,贈送失敗!"); 
		}
		if(giveMembers !=null && giveMembers.getId() ==loginMem.getId()){
			return AjaxResult.error("不允許自己贈送給自己!");
		}
		
		SysConfig appUpload = configService.selectByKey("selectByAppUpload");
		selectMenuFoodExchangeById.setSpareField2("好友贈送");
		//如果爲空，則發送短信通知
		String notityInfo="贈送成功,因為該用戶沒有注冊app,已經短信通知該好友!";
		if(giveMembers ==null){
			//修改贈送優惠券 會員ID 為手機號
			selectMenuFoodExchangeById.setMembersId(Long.valueOf(phone.replace("+","").replace(" ","").replace("_","")));
			String content ="溫馨提醒:您的好友"+loginMem.getName()+",賬號:"+loginMem.getEmail()+" ,贈送您 "+selectMenuFoodExchangeById.getMenuFoodName()+" 優惠券一張。"
					+"<br/>請點擊<a href=\""+ appUpload.getConfigValue() + "\"'>下載</a>安裝app. 如果收到騷擾,請勿理會.";
			SMSDto sendMessager =new SMSDto();
			sendMessager.setMessageReceiver(phone);
			sendMessager.setMessageBody(content);
			sendMessService.sendSMs(sendMessager);
		}else{
			//修改贈送優惠券 會員ID
			selectMenuFoodExchangeById.setMembersId(giveMembers.getId());
			
			//如果有用戶，則發送郵件
			SendEmaill sendEm = new SendEmaill();
			sendEm.setSendTo(giveMembers.getEmail());

			sendEm.setTitle("收到贈送優惠券通知");
			try {
				sendEm.setSendContent("溫馨提醒:您好友"+loginMem.getName()+",賬號:"+loginMem.getEmail()+" ,贈送您 "+selectMenuFoodExchangeById.getMenuFoodName()+" 優惠券一張。"
						+"<br/>如果沒有安裝app,請點擊<a href=\""+ appUpload.getConfigValue() + "\"'>下載</a>安裝app。<br/>如果已經在已經安裝,登錄使用...如果收到騷擾,請勿理會");
				sendEmailUtil.sendEmail(sendEm);
				noticeInfoService.insertNoticeInfo("收到好友贈送優惠券通知", giveMembers.getId(), selectMenuFoodExchangeById.getId(), "MenuFoodExchange", selectMenuFoodExchangeById.getMenuFoodPic());
				notityInfo="優惠券贈送成功,已經 郵件和系統信息 通知該好友!";
			} catch (Exception e) {
				e.printStackTrace();
				return AjaxResult.error("郵件發送失敗!" + e.getMessage());
			}
			
		}
		//保存贈送信息
		
		menuFoodExchangeMapper.updateMenuFoodExchange(selectMenuFoodExchangeById);
		return AjaxResult.success(notityInfo);
	}

	/**
	 * 扫描二维码分配赠送物品
	 */
	@Override
	public AjaxResult qcMenuFood(String code) {
		Lock lock =new ReentrantLock();
		lock.lock();
		//獲取優惠券
		String couponCode =code.split(";")[1];
		if(StringUtils.isEmpty(couponCode)){
			return AjaxResult.error("該二維碼無效!");
		}
		//invoiceNo
		MenuFood menuFood =new MenuFood();
		menuFood.setIsExchange("yes");
		menuFood.setIsValid("yes");
		menuFood.setId(Integer.valueOf(couponCode));
		List<MenuFood> selectMenuFoodList = menuFoodMapper.selectMenuFoodList(menuFood);
		if(selectMenuFoodList ==null || selectMenuFoodList.size() == 0){
			return AjaxResult.error("該優惠券已經失效,或者不存在");
		}
		if(selectMenuFoodList.get(0).getUsedNums()!=null &&selectMenuFoodList.get(0).getUsedNums() <=0){
			return AjaxResult.error("該優惠券已經發放完畢，請等候下次優惠"); 
		}
		MenuFood menuFood2 = selectMenuFoodList.get(0);
		//將優惠券發放到個人賬戶裏
		Members mem= (Members)ThreadLocalUtil.getUserInfo();
		if(mem ==null){
			return AjaxResult.error(555,"用戶不存在!");
		}
		Members selectMembersById = membersMapper.selectMembersById(mem.getId());
		
		//判断是否领取
		MenuFoodExchange menuFoodExchange =new MenuFoodExchange();
		menuFoodExchange.setMenuFoodId(menuFood2.getId());
		List<MenuFoodExchange> selectMenuFoodExchangeList = menuFoodExchangeMapper.selectMenuFoodExchangeList(menuFoodExchange);
		if(selectMenuFoodExchangeList !=null && selectMenuFoodExchangeList.size() >0){
			return AjaxResult.error("您已經領取過了該優惠券,把機會讓給別人吧"); 
		}
		
		//發放
		menuFoodExchange.setMembersId(selectMembersById.getId());
		menuFoodExchange.setMenbersName(selectMembersById.getName());
		menuFoodExchange.setMenuFoodId(menuFood2.getId());
		menuFoodExchange.setMenuFoodName(menuFood2.getTitle());
		menuFoodExchange.setMenuFoodPic(menuFood2.getPicUrl());
		menuFoodExchange.setCouponType(menuFood2.getTypess());
		menuFoodExchange.setInvalidDate(menuFood2.getInvalidDate());
		menuFoodExchange.setSpareField1(menuFood2.getSpareField1());
		menuFoodExchange.setTakeEffectDate(menuFood2.getTakeEffectDate());
		menuFoodExchange.setCouponType(menuFood2.getTypess());
		menuFoodExchange.setCouponCode(RC4.encry_RC4_string(String.format("%07d",(int) (Math.random()*1000))+"",UUID.randomUUID().toString()));
		menuFoodExchange.setExchangeStatus(0);
		menuFoodExchange.setSpareField2("掃描二維碼獲取");
		menuFoodExchange.setCreateDate(new Date());
		menuFoodExchange.setSpareField3(menuFood2.getTitle());
		menuFoodExchangeMapper.insertMenuFoodExchange(menuFoodExchange);
		noticeInfoService.insertNoticeInfo("掃描二維碼獲取商品成功!", selectMembersById.getId(), menuFoodExchange.getId(), "MenuFoodExchange", menuFoodExchange.getMenuFoodPic());
		if(menuFood2.getTotalNums() !=null && menuFood2.getTotalNums() !=-1){
			menuFood2.setUsedNums(menuFood2.getTotalNums()-1);
			menuFoodMapper.updateMenuFood(menuFood2);
		}
		
		lock.unlock();
		return AjaxResult.success("扫描成功,获得优惠券一张", menuFoodExchange.getId());
	}
	
}
