package com.menu.manger.service;

import java.util.List;

import com.menu.manger.dto.MenuFood;
import com.menu.manger.util.AjaxResult;

/**
 * 餐牌 服务层
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
public interface IMenuFoodService 
{
	/**
     * 查询餐牌信息
     * 
     * @param id 餐牌ID
     * @return 餐牌信息
     */
	public MenuFood selectMenuFoodById(Integer id);
	
	/**
     * 查询餐牌列表
     * 
     * @param menuFood 餐牌信息
     * @return 餐牌集合
     */
	public List<MenuFood> selectMenuFoodList(MenuFood menuFood);
	
	/**
     * 新增餐牌
     * 
     * @param menuFood 餐牌信息
     * @return 结果
     */
	public int insertMenuFood(MenuFood menuFood);
	
	/**
     * 修改餐牌
     * 
     * @param menuFood 餐牌信息
     * @return 结果
     */
	public int updateMenuFood(MenuFood menuFood);
		
	/**
     * 删除餐牌信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMenuFoodByIds(String ids);

	public MenuFood getFoodByScose(Integer score);

	public AjaxResult requestExchege(Integer id, Integer num, String code, Integer integer);

	public List<String> selectFoodType();

	public MenuFood getFoodByScoseEnd(Integer score);

	/**
	 * @param id
	 * @return
	 */
	public AjaxResult giveAway(Integer id,String phone);

	/**
	 * @param str
	 * @return
	 */
	public AjaxResult qcMenuFood(String str);

	
}
