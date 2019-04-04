package com.menu.manger.service;

import java.util.List;

import com.menu.manger.dto.CouponManger;
import com.menu.manger.util.AjaxResult;

/**
 * 优惠券管理 服务层
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
public interface ICouponMangerService 
{
	/**
     * 查询优惠券管理信息
     * 
     * @param id 优惠券管理ID
     * @return 优惠券管理信息
     */
	public CouponManger selectCouponMangerById(Integer id);
	
	/**
     * 查询优惠券管理列表
     * 
     * @param couponManger 优惠券管理信息
     * @return 优惠券管理集合
     */
	public List<CouponManger> selectCouponMangerList(CouponManger couponManger);
	
	/**
     * 新增优惠券管理
     * 
     * @param couponManger 优惠券管理信息
     * @return 结果
     */
	public int insertCouponManger(CouponManger couponManger);
	
	/**
     * 修改优惠券管理
     * 
     * @param couponManger 优惠券管理信息
     * @return 结果
     */
	public int updateCouponManger(CouponManger couponManger);
		
	/**
     * 删除优惠券管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteCouponMangerByIds(String ids);

	public AjaxResult analysisQc(String qcInfo);

	public AjaxResult selectCouponMangerByCouponCode(String code);
	
}
