package com.menu.manger.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.menu.manger.dto.BranchStore;

/**
 * 分店管理 数据层
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Mapper
public interface BranchStoreMapper 
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
     * 删除分店管理
     * 
     * @param id 分店管理ID
     * @return 结果
     */
	public int deleteBranchStoreById(Integer id);
	
	/**
     * 批量删除分店管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteBranchStoreByIds(String[] ids);

	/**
	 * @param regionId
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> queryList(Map<String, Object> map);
	
}