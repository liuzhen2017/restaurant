package com.menu.manger.service;

import java.util.List;
import java.util.Map;

import com.menu.manger.dto.BranchStore;

/**
 * 分店管理 服务层
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
public interface IBranchStoreService 
{
	/**
     * 查询分店管理信息
     * 
     * @param id 分店管理ID
     * @return 分店管理信息
     */
	public BranchStore selectBranchStoreById(Integer id);
	
	/**
     * 查询分店管理列表
     * 
     * @param branchStore 分店管理信息
     * @return 分店管理集合
     */
	public List<BranchStore> selectBranchStoreList(BranchStore branchStore);
	
	/**
     * 新增分店管理
     * 
     * @param branchStore 分店管理信息
     * @return 结果
     */
	public int insertBranchStore(BranchStore branchStore);
	
	/**
     * 修改分店管理
     * 
     * @param branchStore 分店管理信息
     * @return 结果
     */
	public int updateBranchStore(BranchStore branchStore);
		
	/**
     * 删除分店管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteBranchStoreByIds(String ids);


	/**
	 * @param regionId
	 * @param id
	 * @param id2 
	 * @return
	 */
	public List<Map<String, Object>> queryList(Integer regionId, Integer id, Integer id2);
	
}
