package com.menu.manger.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.BranchStore;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.IBranchStoreService;

/**
 * 分店管理 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Controller
@RequestMapping("/api/branchStore")
public class BranchStoreController extends BaseController
{
	
	@Autowired
	private IBranchStoreService branchStoreService;
	
	
	/**
	 * 查询分店管理列表
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public TableDataInfo list(BranchStore branchStore)
	{
		startPage();
        List<BranchStore> list = branchStoreService.selectBranchStoreList(branchStore);
		return getDataTable(list);
	}
	
	/**
	 * 查询分店管理列表
	 */
	@RequestMapping("/queryList.do")
	@ResponseBody
	public TableDataInfo queryList(Integer regionId,Integer id)
	{
		startPage();
        List<Map<String, Object>> list = branchStoreService.queryList(regionId,getUserInfo().getId(),id);
		return getDataTable(list);
	}
	
}
