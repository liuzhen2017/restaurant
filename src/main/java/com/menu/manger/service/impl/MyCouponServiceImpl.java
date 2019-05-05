package com.menu.manger.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.CouponManger;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.MyCoupon;
import com.menu.manger.dto.NoticeInfo;
import com.menu.manger.dto.SysConfig;
import com.menu.manger.mapper.MyCouponMapper;
import com.menu.manger.mapper.NoticeInfoMapper;
import com.menu.manger.service.ICouponMangerService;
import com.menu.manger.service.IMembersService;
import com.menu.manger.service.IMenuFoodExchangeService;
import com.menu.manger.service.IMyCouponService;
import com.menu.manger.service.ISysConfigService;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.Convert;
import com.menu.manger.util.DateUtils;
import com.menu.manger.util.ThreadLocalUtil;

/**
 * 我的优惠券 服务层实现
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
@Service
public class MyCouponServiceImpl implements IMyCouponService 
{
	
	@Autowired
	private MyCouponMapper myCouponMapper;
	@Autowired
	NoticeInfoMapper noticeInfoMapper;
	@Autowired
	ICouponMangerService couponMamService;
	@Autowired
	IMembersService memberService;

    @Autowired
    private ISysConfigService configService;
    @Autowired
    private IMenuFoodExchangeService menuFoodExchangeService;

	/**
     * 查询我的优惠券信息
     * 
     * @param id 我的优惠券ID
     * @return 我的优惠券信息
     */
    @Override
	public MyCoupon selectMyCouponById(Integer id)
	{
	    return myCouponMapper.selectMyCouponById(id);
	}
	
	/**
     * 查询我的优惠券列表
     * 
     * @param myCoupon 我的优惠券信息
     * @return 我的优惠券集合
     */
	@Override
	public List<MyCoupon> selectMyCouponList(MyCoupon myCoupon)
	{
	    return myCouponMapper.selectMyCouponList(myCoupon);
	}
	
    /**
     * 新增我的优惠券
     * 
     * @param myCoupon 我的优惠券信息
     * @return 结果
     */
	@Override
	public int insertMyCoupon(MyCoupon myCoupon)
	{
	    return myCouponMapper.insertMyCoupon(myCoupon);
	}
	
	/**
     * 修改我的优惠券
     * 
     * @param myCoupon 我的优惠券信息
     * @return 结果
     */
	@Override
	public int updateMyCoupon(MyCoupon myCoupon)
	{
	    return myCouponMapper.updateMyCoupon(myCoupon);
	}

	/**
     * 删除我的优惠券对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMyCouponByIds(String ids)
	{
		return myCouponMapper.deleteMyCouponByIds(Convert.toStrArray(ids));
	}

	@Override
	public MyCoupon selectMyCouponByRuleType(int i) {
		  
		return myCouponMapper.selectMyCouponById(i);
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void grantLoginCoupon() {
		Lock lock =new ReentrantLock();
		lock.lock();
		
		MyCoupon selectMyCouponById = myCouponMapper.selectMyCouponById(3);
		Members user= (Members)ThreadLocalUtil.getUserInfo();
		selectMyCouponById.setMembersId(user.getId());
		selectMyCouponById.setStatus(1);
		myCouponMapper.updateMyCoupon(selectMyCouponById);
		
		
		//寫入消息
		NoticeInfo noticeInfo =new NoticeInfo();
		noticeInfo.setTitle("升級VIP贈送優惠券");
		noticeInfo.setCreateDate(new Date());
		noticeInfo.setIsSee("no");
		noticeInfo.setMemId(user.getId());
		noticeInfo.setResourceId(selectMyCouponById.getId());;
		noticeInfo.setResourceTable("MyCoupon");
		noticeInfo.setPicUrl(selectMyCouponById.getPicUrl());
		noticeInfoMapper.insertNoticeInfo(noticeInfo);
		
		CouponManger mangerByCouponCode =couponMamService.selectCouponMangerById(selectMyCouponById.getCouponId());
		//修改优惠券主表,未发放数量
		mangerByCouponCode.setNoGrantNum(mangerByCouponCode.getNoGrantNum() - 1);
		mangerByCouponCode.setStatus(1);
		couponMamService.updateCouponManger(mangerByCouponCode);
		
		
		//修改會員信息
		lock.unlock();
	}

	@Override
	public AjaxResult userMyCoupon(String couponCode, String status,
			String msg, String branchStoreId, String userNo) {
		MyCoupon selectMyCouponByCode = myCouponMapper.selectMyCouponByCode(couponCode);
		if(selectMyCouponByCode == null){
			return AjaxResult.error("優惠券已經使用或者不存在!");
		}
		
		return null;
	}

	@Override
	public List<Map<String, Object>> isVaild(MyCoupon myCoupon) {
		List<Map<String, Object>> list = menuFoodExchangeService.isVaild(myCoupon);
		List<MyCoupon> listMycoupon = myCouponMapper.isVaild(myCoupon);
		Map<String, Object> map =null;
		for (Map<String, Object> mp : list) {
			mp.put("table", "menufood");
			if(Integer.parseInt(mp.get("status")+"") ==1 ){
				mp.put("status", "已使用!");
			}else if(mp.get("effectiveTimeEnd") != null && DateUtils.dateTime("yyyyMMdd",mp.get("effectiveTimeEnd")+"").before(new Date())){
				mp.put("status", "已過期!");
			}
		}
		for (MyCoupon myCoupon2 : listMycoupon) {
			map =new HashMap<String, Object>();
			map.put("table", "mycoupon");
			map.put("title", myCoupon2.getTitle());
			map.put("picUrl", myCoupon2.getPicUrl());
			map.put("effectiveTimeEnd", myCoupon2.getEffectiveTimeEnd());
			map.put("name", myCoupon2.getExplanation());
			map.put("id", myCoupon2.getId());
			map.put("table", "MyCoupon");
			map.put("source", myCoupon2.getSpareField1());
			
			if(myCoupon2.getStatus() ==2 || myCoupon2.getIsVaild().equals(HttpConstants.ISVAILD_NO)){
				map.put("status", "已使用!");
			}else if(myCoupon2.getEffectiveTimeEnd() != null && DateUtils.dateTime("yyyyMMdd",myCoupon2.getEffectiveTimeEnd()).before(new Date())){
				map.put("status", "已過期!");
			}
			list.add(map);
		}
		return list;
	}
	/**
	 * 1查找配置文件,配置的VIP升級金額
	 * 2.判斷優惠代碼存不存在,不存在,則返回錯誤信息
	 * 3.判斷優惠多少折
	 */
	@Override
	public AjaxResult calVIPCode(String code) {
		SysConfig vipPrice = configService.selectByKey("VIPPrice");
		
		MyCoupon selectMyCouponByCode = myCouponMapper.selectMyCouponByCode(code);
		
		if(selectMyCouponByCode ==null || selectMyCouponByCode.getMembersId() !=null) {
			return AjaxResult.error("該優惠券不存在,或者已經失效!");
		}
		if(selectMyCouponByCode.getMembersId() !=null) {
			return AjaxResult.error("該優惠券已經綁定用戶,請重新輸入!");
		}
		if(selectMyCouponByCode.getSpareField3() != null && selectMyCouponByCode.getSpareField3().equals("3")) {
			return AjaxResult.error("該優惠券類型不能使用購買VIP");
		}
		double money = Double.parseDouble(vipPrice.getConfigValue()) * selectMyCouponByCode.getCouponValues();
		Map<String, Object> map =new HashMap<>();
		map.put("olbPrice", vipPrice.getConfigValue());
		map.put("newPrice", money);
		return AjaxResult.success("ok", map);
	}
	public static void main(String[] args) {
		System.out.println(100*0.98);
	}

	/* (non-Javadoc)
	 * @see com.menu.manger.service.IMyCouponService#selectGrant(com.menu.manger.dto.MyCoupon)
	 */
	@Override
	public MyCoupon selectGrant(Integer myCoupon) {
		// TODO Auto-generated method stub
		return myCouponMapper.selectGrant(myCoupon);
	}
}
