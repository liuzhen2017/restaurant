package com.menu.manger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.MenuFoodExchange;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.IMenuFoodExchangeService;
import com.menu.manger.util.AjaxResult;

/**
 * 兑换商品 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Controller
@RequestMapping("/api/menuFoodExchange")
public class MenuFoodExchangeController extends BaseController
{
	
	@Autowired
	private IMenuFoodExchangeService menuFoodExchangeService;
	
	
	/**
	 * 查询兑换商品列表
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public TableDataInfo list(MenuFoodExchange menuFoodExchange)
	{
		startPage();
        List<MenuFoodExchange> list = menuFoodExchangeService.selectMenuFoodExchangeList(menuFoodExchange);
		return getDataTable(list);
	}
	@RequestMapping("/selectById.do")
	@ResponseBody
	public AjaxResult selectById(Integer id)
	{
        return AjaxResult.success("", menuFoodExchangeService.selectMenuFoodExchangeById(id));
	}
	
	
	
	
}
