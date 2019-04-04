package com.menu.manger.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menu.manger.dto.BranchStore;
import com.menu.manger.dto.MenuFood;
import com.menu.manger.dto.MyCollection;
import com.menu.manger.mapper.MyCollectionMapper;
import com.menu.manger.service.IMyCollectionService;
import com.menu.manger.util.Convert;

/**
 * 收藏 服务层实现
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
@Service
public class MyCollectionServiceImpl implements IMyCollectionService 
{
	@Autowired
	private MyCollectionMapper myCollectionMapper;
	
	

	/**
     * 查询收藏信息
     * 
     * @param id 收藏ID
     * @return 收藏信息
     */
    @Override
	public MyCollection selectMyCollectionById(Integer id)
	{
	    return myCollectionMapper.selectMyCollectionById(id);
	}
	
	/**
     * 查询收藏列表
     * 
     * @param myCollection 收藏信息
     * @return 收藏集合
     */
	@Override
	public List<BranchStore> selectMyShop(MyCollection myCollection)
	{
            return	 myCollectionMapper.selectMyShop(myCollection);
	}
	/**
     * 查询收藏列表
     * 
     * @param myCollection 收藏信息
     * @return 收藏集合
     */
	@Override
	public List<MenuFood> selectMyCoup(MyCollection myCollection)
	{
		 //優惠券
			return myCollectionMapper.selectMyCoup(myCollection);
		
	}
    /**
     * 新增收藏
     * 
     * @param myCollection 收藏信息
     * @return 结果
     */
	@Override
	public int insertMyCollection(MyCollection myCollection)
	{
	    return myCollectionMapper.insertMyCollection(myCollection);
	}
	
	/**
     * 修改收藏
     * 
     * @param myCollection 收藏信息
     * @return 结果
     */
	@Override
	public int updateMyCollection(MyCollection myCollection)
	{
	    return myCollectionMapper.updateMyCollection(myCollection);
	}

	/**
     * 删除收藏对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMyCollectionByIds(String ids)
	{
		return myCollectionMapper.deleteMyCollectionByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<MyCollection> selectMyCollectionList(MyCollection myCollection) {
		return myCollectionMapper.selectMyCollectionList(myCollection);
	}
	
}
