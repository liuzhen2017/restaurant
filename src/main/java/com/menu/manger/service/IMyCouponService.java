package com.menu.manger.service;

import java.util.List;
import java.util.Map;

import com.menu.manger.dto.MyCoupon;
import com.menu.manger.util.AjaxResult;

/**
 * 我的优惠券 服务层
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
public interface IMyCouponService 
{
	/**
     * 查询我的优惠券信息
     * 
     * @param id 我的优惠券ID
     * @return 我的优惠券信息
     */
	public MyCoupon selectMyCouponById(Integer id);
	
	/**
     * 查询我的优惠券列表
     * 
     * @param myCoupon 我的优惠券信息
     * @return 我的优惠券集合
     */
	public List<MyCoupon> selectMyCouponList(MyCoupon myCoupon);
	
	/**
     * 新增我的优惠券
     * 
     * @param myCoupon 我的优惠券信息
     * @return 结果
     */
	public int insertMyCoupon(MyCoupon myCoupon);
	
	/**
     * 修改我的优惠券
     * 
     * @param myCoupon 我的优惠券信息
     * @return 结果
     */
	public int updateMyCoupon(MyCoupon myCoupon);
		
	/**
     * 删除我的优惠券信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMyCouponByIds(String ids);

	public MyCoupon selectMyCouponByRuleType(int i);

	public void grantLoginCoupon();

	public AjaxResult userMyCoupon(String couponCode, String status,
			String msg, String branchStoreId, String userNo);

	/**
	 * @param myCoupon
	 * @return
	 */
	public List<Map<String, Object>> isVaild(MyCoupon myCoupon);

	public AjaxResult calVIPCode(String code);

	/**
	 * @param myCoupon
	 * @return
	 */
	public MyCoupon selectGrant(Integer myCoupon);
	
}
