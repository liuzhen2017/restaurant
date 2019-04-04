package com.menu.manger.service.impl;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.CouponManger;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.MyCoupon;
import com.menu.manger.mapper.CouponMangerMapper;
import com.menu.manger.service.ICouponMangerService;
import com.menu.manger.service.IMembersService;
import com.menu.manger.service.IMyCouponService;
import com.menu.manger.service.INoticeInfoService;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.Convert;
import com.menu.manger.util.RC4;
import com.menu.manger.util.ThreadLocalUtil;

/**
 * 优惠券管理 服务层实现
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Service
public class CouponMangerServiceImpl implements ICouponMangerService 
{
	@Autowired
	private CouponMangerMapper couponMangerMapper;
	private static String qcKey ="showQCByCoup";
	@Autowired
	private IMyCouponService myCoupnService;
	@Autowired
	private IMembersService memBersService;
	Lock lock =new ReentrantLock();
	@Autowired
	private INoticeInfoService notiCeInfoService;

	/**
     * 查询优惠券管理信息
     * 
     * @param id 优惠券管理ID
     * @return 优惠券管理信息
     */
    @Override
	public CouponManger selectCouponMangerById(Integer id)
	{
	    return couponMangerMapper.selectCouponMangerById(id);
	}
	
	/**
     * 查询优惠券管理列表
     * 
     * @param couponManger 优惠券管理信息
     * @return 优惠券管理集合
     */
	@Override
	public List<CouponManger> selectCouponMangerList(CouponManger couponManger)
	{
	    return couponMangerMapper.selectCouponMangerList(couponManger);
	}
	
    /**
     * 新增优惠券管理
     * 
     * @param couponManger 优惠券管理信息
     * @return 结果
     */
	@Override
	public int insertCouponManger(CouponManger couponManger)
	{
	    return couponMangerMapper.insertCouponManger(couponManger);
	}
	
	/**
     * 修改优惠券管理
     * 
     * @param couponManger 优惠券管理信息
     * @return 结果
     */
	@Override
	public int updateCouponManger(CouponManger couponManger)
	{
	    return couponMangerMapper.updateCouponManger(couponManger);
	}

	/**
     * 删除优惠券管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteCouponMangerByIds(String ids)
	{
		return couponMangerMapper.deleteCouponMangerByIds(Convert.toStrArray(ids));
	}

	@Override
	public AjaxResult analysisQc(String qcInfo) {
//		RC4.encry_RC4_string(String.format("%07d",selectCouponMangerById.getId())+"",qcKey),900, "JPEG");
		String formId =RC4.decry_RC4(qcInfo, qcKey);
		MyCoupon coupon =new MyCoupon();
		coupon.setCouponId(Integer.parseInt(formId));
		coupon.setStatus(0);
//		coupon.setIsVaild("yes");
		if(lock.tryLock()){
	     	
		}
		return null;
	}
	public static void main(String[] args) {
		System.out.println(String.format("%07d",1000000));
	}

	@Override
	public AjaxResult selectCouponMangerByCouponCode(String code) {
		Lock lock =new ReentrantLock();
		lock.lock();
		//獲取優惠券
		String couponCode =code.substring(code.lastIndexOf("/")+1, code.length());
		if(StringUtils.isEmpty(couponCode)){
			return AjaxResult.error("該二維碼無效!");
		}
		CouponManger couponMangerByCouponCode = couponMangerMapper.selectCouponMangerByCouponCode(couponCode);
		if(couponMangerByCouponCode ==null){
			return AjaxResult.error("該優惠券已經失效");
		}
		if(couponMangerByCouponCode.getNoGrantNum()!=null &&couponMangerByCouponCode.getNoGrantNum() <=0){
			return AjaxResult.error("該優惠券已經發放完畢，請等候下次優惠"); 
		}
		
		//將優惠券發放到個人賬戶裏
		Members mem= (Members)ThreadLocalUtil.getUserInfo();
		if(mem ==null){
			return AjaxResult.error(555,"用戶不存在!");
		}
		Members selectMembersById = memBersService.selectMembersById(mem.getId());
		
		//判断类型是否可以领取
		if(couponMangerByCouponCode.getMembersType() != selectMembersById.getMembersType()  && couponMangerByCouponCode.getMembersType() != HttpConstants.EmmbersType_2){
			String type =couponMangerByCouponCode.getMembersType()==HttpConstants.EmmbersType_0? "普通會員":"VIP會員";
			return AjaxResult.error("很抱歉,該券只允許 "+type+"領取"); 
		}
		//判断是否领取
		MyCoupon myCoupon =new MyCoupon();
		myCoupon.setCouponId(couponMangerByCouponCode.getId());
		List<MyCoupon> selectMyCouponList = myCoupnService.selectMyCouponList(myCoupon);
		if(selectMyCouponList !=null && selectMyCouponList.size() >0){
			return AjaxResult.error("您已經領取過了該優惠券,把機會讓給別人吧"); 
		}
		
		//發放
		MyCoupon myCouponList = myCoupnService.selectGrant(couponMangerByCouponCode.getId());
		if(myCouponList ==null){
			return AjaxResult.error("該優惠券已經發放完畢，請等候下次優惠"); 
		}
		myCouponList.setMembersId(selectMembersById.getId());
		myCouponList.setStatus(1);
		myCouponList.setSpareField1("掃二維碼活動領取");
		myCoupnService.updateMyCoupon(myCouponList);
		if(couponMangerByCouponCode.getNoGrantNum() !=null){
		  couponMangerByCouponCode.setNoGrantNum(couponMangerByCouponCode.getNoGrantNum()-1);
		  couponMangerMapper.updateCouponManger(couponMangerByCouponCode);
		}
		
		notiCeInfoService.insertNoticeInfo("掃碼領取優惠券成功通知", selectMembersById.getId(),myCouponList.getId(), "MyCoupon", myCouponList.getPicUrl());
		lock.unlock();
		return AjaxResult.success("扫描成功,获得优惠券一张", myCouponList.getId());
		
	}
}
