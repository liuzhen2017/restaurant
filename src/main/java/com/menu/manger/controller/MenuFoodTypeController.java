package com.menu.manger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.MenuFoodType;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.IMenuFoodTypeService;

/**
 * 餐牌類型 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-02-21
 */
@Controller
@RequestMapping("/api/menuFoodType")
public class MenuFoodTypeController extends BaseController
{
	
	@Autowired
	private IMenuFoodTypeService menuFoodTypeService;
	
	
	/**
	 * 查询餐牌類型列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public TableDataInfo list(MenuFoodType menuFoodType)
	{
		startPage();
		menuFoodType.setIsVaild("yes");
        List<MenuFoodType> list = menuFoodTypeService.selectMenuFoodTypeList(menuFoodType);
		return getDataTable(list);
	}
	
	/**
	 * 查询包含有兑换商品的列表 
	 */
	@RequestMapping("/queryByType.do")
	@ResponseBody
	public TableDataInfo queryByType(MenuFoodType menuFoodType)
	{
		startPage();
        List<MenuFoodType> list = menuFoodTypeService.queryByType(menuFoodType);
		return getDataTable(list);
	}
	
}
