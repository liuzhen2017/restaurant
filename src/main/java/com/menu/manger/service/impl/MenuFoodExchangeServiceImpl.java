package com.menu.manger.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menu.manger.dto.MenuFoodExchange;
import com.menu.manger.dto.MyCoupon;
import com.menu.manger.mapper.MenuFoodExchangeMapper;
import com.menu.manger.service.IMenuFoodExchangeService;
import com.menu.manger.util.Convert;

/**
 * 兑换商品 服务层实现
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Service
public class MenuFoodExchangeServiceImpl implements IMenuFoodExchangeService 
{
	@Autowired
	private MenuFoodExchangeMapper menuFoodExchangeMapper;

	/**
     * 查询兑换商品信息
     * 
     * @param id 兑换商品ID
     * @return 兑换商品信息
     */
    @Override
	public MenuFoodExchange selectMenuFoodExchangeById(Integer id)
	{
	    return menuFoodExchangeMapper.selectMenuFoodExchangeById(id);
	}
	
	/**
     * 查询兑换商品列表
     * 
     * @param menuFoodExchange 兑换商品信息
     * @return 兑换商品集合
     */
	@Override
	public List<MenuFoodExchange> selectMenuFoodExchangeList(MenuFoodExchange menuFoodExchange)
	{
	    return menuFoodExchangeMapper.selectMenuFoodExchangeList(menuFoodExchange);
	}
	
    /**
     * 新增兑换商品
     * 
     * @param menuFoodExchange 兑换商品信息
     * @return 结果
     */
	@Override
	public int insertMenuFoodExchange(MenuFoodExchange menuFoodExchange)
	{
	    return menuFoodExchangeMapper.insertMenuFoodExchange(menuFoodExchange);
	}
	
	/**
     * 修改兑换商品
     * 
     * @param menuFoodExchange 兑换商品信息
     * @return 结果
     */
	@Override
	public int updateMenuFoodExchange(MenuFoodExchange menuFoodExchange)
	{
	    return menuFoodExchangeMapper.updateMenuFoodExchange(menuFoodExchange);
	}

	/**
     * 删除兑换商品对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMenuFoodExchangeByIds(String ids)
	{
		return menuFoodExchangeMapper.deleteMenuFoodExchangeByIds(Convert.toStrArray(ids));
	}

	/* (non-Javadoc)
	 * @see com.menu.manger.service.IMenuFoodExchangeService#selectMenuFoodExchangeByIds(java.lang.String)
	 */
	@Override
	public List<MenuFoodExchange> selectMenuFoodExchangeByIds(String ids) {
		return menuFoodExchangeMapper.selectMenuFoodExchangeByIds(Convert.toStrArray(ids));
	}

	/* (non-Javadoc)
	 * @see com.menu.manger.service.IMenuFoodExchangeService#isVaild(com.menu.manger.dto.MyCoupon)
	 */
	@Override
	public List<Map<String, Object>> isVaild(MyCoupon myCoupon) {
		return menuFoodExchangeMapper.isVaild(myCoupon);
	}
	
}
