package com.menu.manger.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.menu.manger.dto.IntegralRole;

/**
 * 积分规则 数据层
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Mapper
public interface IntegralRoleMapper 
{
	/**
     * 查询积分规则信息
     * 
     * @param id 积分规则ID
     * @return 积分规则信息
     */
	public IntegralRole selectIntegralRoleById(Integer id);
	
	/**
     * 查询积分规则列表
     * 
     * @param integralRole 积分规则信息
     * @return 积分规则集合
     */
	public List<IntegralRole> selectIntegralRoleList(IntegralRole integralRole);
	
	/**
     * 新增积分规则
     * 
     * @param integralRole 积分规则信息
     * @return 结果
     */
	public int insertIntegralRole(IntegralRole integralRole);
	
	/**
     * 修改积分规则
     * 
     * @param integralRole 积分规则信息
     * @return 结果
     */
	public int updateIntegralRole(IntegralRole integralRole);
	
	/**
     * 删除积分规则
     * 
     * @param id 积分规则ID
     * @return 结果
     */
	public int deleteIntegralRoleById(Integer id);
	
	/**
     * 批量删除积分规则
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteIntegralRoleByIds(String[] ids);
	/**查找积分规则
	 * @param membersType
	 * @return
	 */
	public List<IntegralRole> selectByRole(@Param("integralType") Integer integralType);
	
}