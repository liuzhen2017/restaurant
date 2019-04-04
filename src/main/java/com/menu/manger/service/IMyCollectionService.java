package com.menu.manger.service;

import java.util.List;

import com.menu.manger.dto.BranchStore;
import com.menu.manger.dto.MenuFood;
import com.menu.manger.dto.MyCollection;

/**
 * 收藏 服务层
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
public interface IMyCollectionService 
{
	/**
     * 查询收藏信息
     * 
     * @param id 收藏ID
     * @return 收藏信息
     */
	public MyCollection selectMyCollectionById(Integer id);
	
	/**
     * 查询收藏列表
     * 
     * @param myCollection 收藏信息
     * @return 收藏集合
     */
	public List<MyCollection> selectMyCollectionList(MyCollection myCollection);
	/**
     * 查询收藏列表
     * 
     * @param myCollection 收藏信息
     * @return 收藏集合
     */
	public List<MenuFood> selectMyCoup(MyCollection myCollection);
	/**
     * 查询收藏列表
     * 
     * @param myCollection 收藏信息
     * @return 收藏集合
     */
	public List<BranchStore> selectMyShop(MyCollection myCollection);
	
	
	/**
     * 新增收藏
     * 
     * @param myCollection 收藏信息
     * @return 结果
     */
	public int insertMyCollection(MyCollection myCollection);
	
	/**
     * 修改收藏
     * 
     * @param myCollection 收藏信息
     * @return 结果
     */
	public int updateMyCollection(MyCollection myCollection);
		
	/**
     * 删除收藏信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMyCollectionByIds(String ids);
	
}
