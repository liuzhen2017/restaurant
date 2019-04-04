package com.menu.manger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.BranchStoreRegion;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.IBranchStoreRegionService;

/**
 * 區域類型 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-02-24
 */
@Controller
@RequestMapping("/api/branchStoreRegion")
public class BranchStoreRegionController extends BaseController
{
	
	@Autowired
	private IBranchStoreRegionService branchStoreRegionService;
	
	
	/**
	 * 查询區域類型列表
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public TableDataInfo list(BranchStoreRegion branchStoreRegion)
	{
		startPage();
        List<BranchStoreRegion> list = branchStoreRegionService.selectBranchStoreRegionList(branchStoreRegion);
		return getDataTable(list);
	}
	
}
