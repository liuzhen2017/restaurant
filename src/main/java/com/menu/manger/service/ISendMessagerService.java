package com.menu.manger.service;

import java.util.List;

import com.menu.manger.dto.SendMessager;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.SMSDto;

/**
 * 短信發送記錄 服务层
 * 
 * @author liuzhen
 * @date 2019-01-25
 */
public interface ISendMessagerService 
{
	/**
     * 查询短信發送記錄信息
     * 
     * @param id 短信發送記錄ID
     * @return 短信發送記錄信息
     */
	public SendMessager selectSendMessagerById(Integer id);
	
	/**
     * 查询短信發送記錄列表
     * 
     * @param sendMessager 短信發送記錄信息
     * @return 短信發送記錄集合
     */
	public List<SendMessager> selectSendMessagerList(SendMessager sendMessager);
	
	/**
     * 新增短信發送記錄
     * 
     * @param sendMessager 短信發送記錄信息
     * @return 结果
     */
	public AjaxResult sendSMS(String phone);

	public AjaxResult invite(String phone);
	/**
	 * 發送郵件
	 * @param sendMessager
	 * @return
	 */
	public AjaxResult sendSMs(SMSDto sendMessager);

	/**
	 * @param email
	 * @return
	 */
	public AjaxResult sendEmail(String email);
	
	
}
