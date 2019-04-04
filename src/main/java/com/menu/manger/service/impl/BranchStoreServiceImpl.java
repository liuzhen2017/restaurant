package com.menu.manger.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menu.manger.dto.BranchStore;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.MyCollection;
import com.menu.manger.mapper.BranchStoreMapper;
import com.menu.manger.mapper.MyCollectionMapper;
import com.menu.manger.mapper.MyCouponMapper;
import com.menu.manger.service.IBranchStoreService;
import com.menu.manger.util.Convert;
import com.menu.manger.util.ThreadLocalUtil;

/**
 * 分店管理 服务层实现
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Service
public class BranchStoreServiceImpl implements IBranchStoreService 
{
	@Autowired
	private BranchStoreMapper branchStoreMapper;
	@Autowired
	private MyCollectionMapper myCollerctMapper;

	/**
     * 查询分店管理信息
     * 
     * @param id 分店管理ID
     * @return 分店管理信息
     */
    @Override
	public BranchStore selectBranchStoreById(Integer id)
	{
	    return branchStoreMapper.selectBranchStoreById(id);
	}
	
	/**
     * 查询分店管理列表
     * 
     * @param branchStore 分店管理信息
     * @return 分店管理集合
     */
	@Override
	public List<BranchStore> selectBranchStoreList(BranchStore branchStore)
	{
		List<BranchStore> list = branchStoreMapper.selectBranchStoreList(branchStore);
		for (BranchStore branchStore2 : list) {
			MyCollection myCollection =new MyCollection();
			myCollection.setResourceId(branchStore2.getId());
			myCollection.setResourceTable("myShop");
			Members mem =(Members)ThreadLocalUtil.getUserInfo();
			myCollection.setMemuId(mem.getId());
			List<MyCollection> selectMyCollectionList = myCollerctMapper.selectMyCollectionList(myCollection);
			branchStore2.setIsColle("no");
			if(selectMyCollectionList !=null && selectMyCollectionList.size() >0){
				branchStore2.setIsColle("yes");
			}
		}
		return list;
	}
	
    /**
     * 新增分店管理
     * 
     * @param branchStore 分店管理信息
     * @return 结果
     */
	@Override
	public int insertBranchStore(BranchStore branchStore)
	{
//		branchStore.setCreateBy(ShiroUtils.getSysUser().getLoginName()+"");
		branchStore.setIsVaild("yes");
	    return branchStoreMapper.insertBranchStore(branchStore);
	}
	
	/**
     * 修改分店管理
     * 
     * @param branchStore 分店管理信息
     * @return 结果
     */
	@Override
	public int updateBranchStore(BranchStore branchStore)
	{
//		branchStore.setUpdateBy(ShiroUtils.getSysUser().getLoginName()+"");
	    return branchStoreMapper.updateBranchStore(branchStore);
	}

	/**
     * 删除分店管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteBranchStoreByIds(String ids)
	{
		return branchStoreMapper.deleteBranchStoreByIds(Convert.toStrArray(ids));
	}

	/* (non-Javadoc)
	 * @see com.menu.manger.service.IBranchStoreService#queryList(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> queryList(Integer regionId, Integer memId,Integer id) {
		// TODO Auto-generated method stub
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("memId", memId	);
		map.put("regionId", regionId);
		map.put("id", id);
		return branchStoreMapper.queryList(map);
	}
	
}
