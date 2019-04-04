package com.menu.manger.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menu.manger.dto.UserOrder;
import com.menu.manger.mapper.UserOrderMapper;
import com.menu.manger.service.IUserOrderService;
import com.menu.manger.util.Convert;

/**
 * 訂單 服务层实现
 * 
 * @author liuzhen
 * @date 2019-01-31
 */
@Service
public class UserOrderServiceImpl implements IUserOrderService 
{
	@Autowired
	private UserOrderMapper orderMapper;

	/**
     * 查询訂單信息
     * 
     * @param id 訂單ID
     * @return 訂單信息
     */
    @Override
	public UserOrder selectOrderById(Integer id)
	{
	    return orderMapper.selectOrderById(id);
	}
	
	/**
     * 查询訂單列表
     * 
     * @param order 訂單信息
     * @return 訂單集合
     */
	@Override
	public List<UserOrder> selectOrderList(UserOrder order)
	{
	    return orderMapper.selectOrderList(order);
	}
	
    /**
     * 新增訂單
     * 
     * @param order 訂單信息
     * @return 结果
     */
	@Override
	public int insertOrder(UserOrder order)
	{
	    return orderMapper.insertOrder(order);
	}
	
	/**
     * 修改訂單
     * 
     * @param order 訂單信息
     * @return 结果
     */
	@Override
	public int updateOrder(UserOrder order)
	{
	    return orderMapper.updateOrder(order);
	}

	/**
     * 删除訂單对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteOrderByIds(String ids)
	{
		return orderMapper.deleteOrderByIds(Convert.toStrArray(ids));
	}
	
}
