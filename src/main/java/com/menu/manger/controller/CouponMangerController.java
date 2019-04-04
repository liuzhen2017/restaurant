package com.menu.manger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.CouponManger;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.ICouponMangerService;
import com.menu.manger.util.AjaxResult;

/**
 * 优惠券管理 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Controller
@RequestMapping("/api/couponManger")
public class CouponMangerController extends BaseController
{
	
	@Autowired
	private ICouponMangerService couponMangerService;
	
	
	/**
	 * 查询优惠券管理列表
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(int type)
	{
		CouponManger couponManger =new CouponManger();
		startPage();
        List<CouponManger> list = couponMangerService.selectCouponMangerList(couponManger);
		return getDataTable(list);
	}
	
	
	
	/**
	 * 删除优惠券管理
	 */
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(couponMangerService.deleteCouponMangerByIds(ids));
	}
	@PostMapping( "/analysisQc")
	@ResponseBody
	public  AjaxResult analysisQc(String qcInfo){
		return couponMangerService.analysisQc(qcInfo);
	}
}
