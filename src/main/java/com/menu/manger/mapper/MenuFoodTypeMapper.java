package com.menu.manger.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.menu.manger.dto.MenuFoodType;

/**
 * 餐牌類型 数据层
 * 
 * @author liuzhen
 * @date 2019-02-21
 */
@Mapper
public interface MenuFoodTypeMapper 
{
	/**
     * 查询餐牌類型信息
     * 
     * @param id 餐牌類型ID
     * @return 餐牌類型信息
     */
	public MenuFoodType selectMenuFoodTypeById(Integer id);
	
	/**
     * 查询餐牌類型列表
     * 
     * @param menuFoodType 餐牌類型信息
     * @return 餐牌類型集合
     */
	public List<MenuFoodType> selectMenuFoodTypeList(MenuFoodType menuFoodType);
	
	/**
     * 新增餐牌類型
     * 
     * @param menuFoodType 餐牌類型信息
     * @return 结果
     */
	public int insertMenuFoodType(MenuFoodType menuFoodType);
	
	/**
     * 修改餐牌類型
     * 
     * @param menuFoodType 餐牌類型信息
     * @return 结果
     */
	public int updateMenuFoodType(MenuFoodType menuFoodType);
	
	/**
     * 删除餐牌類型
     * 
     * @param id 餐牌類型ID
     * @return 结果
     */
	public int deleteMenuFoodTypeById(Integer id);
	
	/**
     * 批量删除餐牌類型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMenuFoodTypeByIds(String[] ids);

	/** 查询包含有兑换商品的列表
	 * @param menuFoodType 
	 * @return
	 */
	public List<MenuFoodType> queryByType(MenuFoodType menuFoodType);

	/**
	 * @param menuFoodType
	 * @return
	 */
	public List<MenuFoodType> queryJX(MenuFoodType menuFoodType);
	
}