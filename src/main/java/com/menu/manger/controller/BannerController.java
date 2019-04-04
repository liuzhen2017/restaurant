package com.menu.manger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.Banner;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.IBannerService;

/**
 * 首頁banner 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-02-21
 */
@Controller
@RequestMapping("/api/banner")
public class BannerController extends BaseController
{
	
	@Autowired
	private IBannerService bannerService;
	
	
	/**
	 * 查询首頁banner列表
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Banner banner)
	{
		startPage();
		banner.setIsVaild("yes");
		banner.setEffectiveTimeBegin("yes");
        List<Banner> list = bannerService.selectBannerList(banner);
		return getDataTable(list);
	}
	
	
}
