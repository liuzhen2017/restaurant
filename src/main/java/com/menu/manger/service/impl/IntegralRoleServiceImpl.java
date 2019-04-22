package com.menu.manger.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.IntegralRole;
import com.menu.manger.mapper.IntegralRoleMapper;
import com.menu.manger.service.IIntegralRoleService;
import com.menu.manger.util.Convert;

/**
 * 积分规则 服务层实现
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Service
public class IntegralRoleServiceImpl implements IIntegralRoleService 
{
	@Autowired
	private IntegralRoleMapper integralRoleMapper;

	/**
     * 查询积分规则信息
     * 
     * @param id 积分规则ID
     * @return 积分规则信息
     */
    @Override
	public IntegralRole selectIntegralRoleById(Integer id)
	{
	    return integralRoleMapper.selectIntegralRoleById(id);
	}
	
	/**
     * 查询积分规则列表
     * 
     * @param integralRole 积分规则信息
     * @return 积分规则集合
     */
	@Override
	public List<IntegralRole> selectIntegralRoleList(IntegralRole integralRole)
	{
	    return integralRoleMapper.selectIntegralRoleList(integralRole);
	}
	
    /**
     * 新增积分规则
     * 
     * @param integralRole 积分规则信息
     * @return 结果
     */
	@Override
	public int insertIntegralRole(IntegralRole integralRole)
	{
//		integralRole.setCreateBy(ShiroUtils.getSysUser().getLoginName()+"");
		integralRole.setCreateDate(new Date());
	    return integralRoleMapper.insertIntegralRole(integralRole);
	}
	
	/**
     * 修改积分规则
     * 
     * @param integralRole 积分规则信息
     * @return 结果
     */
	@Override
	public int updateIntegralRole(IntegralRole integralRole)
	{
//		integralRole.setUpdateBy(ShiroUtils.getSysUser().getLoginName()+"");
		integralRole.setUpdateDate(new Date());
	    return integralRoleMapper.updateIntegralRole(integralRole);
	}

	/**
     * 删除积分规则对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteIntegralRoleByIds(String ids)
	{
		return integralRoleMapper.deleteIntegralRoleByIds(Convert.toStrArray(ids));
	}

	/* (non-Javadoc)
	 * @see com.menu.manger.service.IIntegralRoleService#selectByRole(java.lang.Integer)
	 */
	@Override
	public IntegralRole selectByRole(Integer membersType) {
		
		 List<IntegralRole> byRole = integralRoleMapper.selectByRole(null);
		 IntegralRole inteRole = null;
		 for (IntegralRole integralRole : byRole) {
			 //如果类型匹配,获取是通用类型
			if(integralRole.getMenmType() ==membersType || integralRole.getMenmType() ==HttpConstants.EmmbersType_2){
				inteRole =integralRole;
				break;
			}
		}
		 //如果不匹配,并且没有通用类型,则取优先级最高的第一条
		 if(inteRole ==null){
			 inteRole =byRole.get(0);
		 }
		 return inteRole;
	}

	/* (non-Javadoc)
	 * @see com.menu.manger.service.IIntegralRoleService#selectByRoleByintegralType(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public IntegralRole selectByRoleByintegralType(Integer membersType,
			Integer integralType) {
		 List<IntegralRole> byRole = integralRoleMapper.selectByRole(integralType);
		 IntegralRole inteRole = null;
		 for (IntegralRole integralRole : byRole) {
			 //如果类型匹配,获取是通用类型
			if(integralRole.getMenmType() ==membersType || integralRole.getMenmType() ==HttpConstants.EmmbersType_2){
				inteRole =integralRole;
				break;
			}
		}
		 //如果不匹配,并且没有通用类型,则取优先级最高的第一条
		 if(inteRole ==null){
			 inteRole =byRole.get(0);
		 }
		 return inteRole;
	}
	
}
