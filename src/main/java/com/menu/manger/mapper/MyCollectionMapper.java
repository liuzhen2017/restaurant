package com.menu.manger.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.menu.manger.dto.BranchStore;
import com.menu.manger.dto.MenuFood;
import com.menu.manger.dto.MyCollection;

/**
 * 收藏 数据层
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
@Mapper
public interface MyCollectionMapper 
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
     * 删除收藏
     * 
     * @param id 收藏ID
     * @return 结果
     */
	public int deleteMyCollectionById(Integer id);
	
	/**
     * 批量删除收藏
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMyCollectionByIds(String[] ids);

	public List<BranchStore> selectMyShop(MyCollection myCollection);

	public List<MenuFood> selectMyCoup(MyCollection myCollection);
	
}