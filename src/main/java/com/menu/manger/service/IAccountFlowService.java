package com.menu.manger.service;

import java.util.List;

import com.menu.manger.dto.AccountFlow;

/**
 * 賬戶流水 服务层
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
public interface IAccountFlowService 
{
	/**
     * 查询賬戶流水信息
     * 
     * @param id 賬戶流水ID
     * @return 賬戶流水信息
     */
	public AccountFlow selectAccountFlowById(Integer id);
	
	/**
     * 查询賬戶流水列表
     * 
     * @param accountFlow 賬戶流水信息
     * @return 賬戶流水集合
     */
	public List<AccountFlow> selectAccountFlowList(AccountFlow accountFlow);
	
	/**
     * 新增賬戶流水
     * 
     * @param accountFlow 賬戶流水信息
     * @return 结果
     */
	public int insertAccountFlow(AccountFlow accountFlow);
	
	/**
     * 修改賬戶流水
     * 
     * @param accountFlow 賬戶流水信息
     * @return 结果
     */
	public int updateAccountFlow(AccountFlow accountFlow);
		
	/**
     * 删除賬戶流水信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteAccountFlowByIds(String ids);

	/**
	 * 根據會員ID查詢該會員年度消費總額
	 * @param id
	 */
	public int selectAccountMoneyByMemId(Integer id);
	
}
