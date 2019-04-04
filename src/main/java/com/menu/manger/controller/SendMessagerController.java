package com.menu.manger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.SendMessager;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.ISendMessagerService;
import com.menu.manger.util.AjaxResult;

/**
 * 短信發送記錄 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-01-25
 */
@Controller
@RequestMapping("/api/sendMessager")
@ResponseBody
public class SendMessagerController extends BaseController
{
    private String prefix = "api/sendMessager";
	
	@Autowired
	private ISendMessagerService sendMessagerService;
	
	@GetMapping()
	public String sendMessager()
	{
	    return prefix + "/sendMessager";
	}
	
	/**
	 * 查询短信發送記錄列表
	 */
	@PostMapping("/list.do")
	public TableDataInfo list(SendMessager sendMessager)
	{
		startPage();
        List<SendMessager> list = sendMessagerService.selectSendMessagerList(sendMessager);
		return getDataTable(list);
	}
	
	
	
	/**
	 * 新增保存短信發送記錄
	 */

	
	@PostMapping("/sendSMS.do")
	public AjaxResult sendSMS(@RequestParam(required=true)String phone)
	{	
		return sendMessagerService.sendSMS(phone);
	}

	/**
	 * 新增保存短信發送記錄
	 */

	
	@PostMapping("/invite.do")
	public AjaxResult invite(@RequestParam(required=true)String phone)
	{	
		return sendMessagerService.invite(phone);
	}
	/**
	 * 發送找回密碼郵件
	 */
	@PostMapping("/sendEmail.do")
	public AjaxResult sendEmail(@RequestParam(required=true)String email)
	{	
		return sendMessagerService.sendEmail(email);
	}
}
