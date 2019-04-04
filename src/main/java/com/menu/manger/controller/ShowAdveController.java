package com.menu.manger.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.ShowAdve;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.IShowAdveService;


/**
 * 首頁廣告 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-04-02
 */
@Controller
@RequestMapping("/system/showAdve")
public class ShowAdveController extends BaseController
{
    private String prefix = "system/showAdve";
	
	@Autowired
	private IShowAdveService showAdveService;
	
	@RequiresPermissions("system:showAdve:view")
	@GetMapping()
	public String showAdve()
	{
	    return prefix + "/showAdve";
	}
	
	/**
	 * 查询首頁廣告列表
	 */
	@RequiresPermissions("system:showAdve:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ShowAdve showAdve)
	{
		startPage();
        List<ShowAdve> list = showAdveService.selectShowAdveList(showAdve);
		return getDataTable(list);
	}
	
}
