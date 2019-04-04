package com.menu.manger.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.menu.manger.dto.MenuFoodExchange;
import com.menu.manger.dto.MyCoupon;

/**
 * 兑换商品 数据层
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Mapper
public interface MenuFoodExchangeMapper 
{
	/**
     * 查询兑换商品信息
     * 
     * @param id 兑换商品ID
     * @return 兑换商品信息
     */
	public MenuFoodExchange selectMenuFoodExchangeById(Integer id);
	
	/**
     * 查询兑换商品列表
     * 
     * @param menuFoodExchange 兑换商品信息
     * @return 兑换商品集合
     */
	public List<MenuFoodExchange> selectMenuFoodExchangeList(MenuFoodExchange menuFoodExchange);
	
	/**
     * 新增兑换商品
     * 
     * @param menuFoodExchange 兑换商品信息
     * @return 结果
     */
	public int insertMenuFoodExchange(MenuFoodExchange menuFoodExchange);
	
	/**
     * 修改兑换商品
     * 
     * @param menuFoodExchange 兑换商品信息
     * @return 结果
     */
	public int updateMenuFoodExchange(MenuFoodExchange menuFoodExchange);
	
	/**
     * 删除兑换商品
     * 
     * @param id 兑换商品ID
     * @return 结果
     */
	public int deleteMenuFoodExchangeById(Integer id);
	
	/**
     * 批量删除兑换商品
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMenuFoodExchangeByIds(String[] ids);

	/**
	 * @param ids
	 * @return
	 */
	public List<MenuFoodExchange> selectMenuFoodExchangeByIds(String[] ids);

	/**
	 * @param myCoupon
	 * @return
	 */
	public List<Map<String, Object>> isVaild(MyCoupon myCoupon);
	
}