package com.menu.manger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menu.manger.dto.AccountFlow;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.IAccountFlowService;

/**
 * 賬戶流水 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-02-21
 */
@Controller
@RequestMapping("/api/accountFlow")
public class AccountFlowController extends BaseController
{
	
	@Autowired
	private IAccountFlowService accountFlowService;
	
	/**
	 * 查询賬戶流水列表
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public TableDataInfo list(AccountFlow accountFlow)
	{
		startPage();
		//accountFlow.setMenuId(getUserInfo().getId());
		accountFlow.setIsVaild("yes");
        List<AccountFlow> list = accountFlowService.selectAccountFlowList(accountFlow);
		return getDataTable(list);
	}
	
	
}
