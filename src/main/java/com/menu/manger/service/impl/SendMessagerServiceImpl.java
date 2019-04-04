package com.menu.manger.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.SendMessager;
import com.menu.manger.dto.SysConfig;
import com.menu.manger.mapper.MembersMapper;
import com.menu.manger.mapper.SendMessagerMapper;
import com.menu.manger.service.ISendMessagerService;
import com.menu.manger.service.ISysConfigService;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.SMSDto;
import com.menu.manger.util.SMSUtil;
import com.menu.manger.util.SendEmailUtil;
import com.menu.manger.util.SendEmaill;

/**
 * 短信發送記錄 服务层实现
 * 
 * @author liuzhen
 * @date 2019-01-25
 */
@Service
public class SendMessagerServiceImpl implements ISendMessagerService 
{
	
	@Autowired
	private SendMessagerMapper sendMessagerMapper;
	@Autowired
	private MembersMapper membersMapper;
	@Autowired
	private ISysConfigService configService; 
	
	/**
     * 查询短信發送記錄信息
     * 
     * @param id 短信發送記錄ID
     * @return 短信發送記錄信息
     */
    @Override
	public SendMessager selectSendMessagerById(Integer id)
	{
	    return sendMessagerMapper.selectSendMessagerById(id);
	}
	
	/**
     * 查询短信發送記錄列表
     * 
     * @param sendMessager 短信發送記錄信息
     * @return 短信發送記錄集合
     */
	@Override
	public List<SendMessager> selectSendMessagerList(SendMessager sendMessager)
	{
	    return sendMessagerMapper.selectSendMessagerList(sendMessager);
	}
	
    /**
     * 新增短信發送記錄
     * 
     * @param sendMessager 短信發送記錄信息
     * @return 结果
     */
	@Override
	public AjaxResult sendSMS(String phone)
	{
		SendMessager sendMessager =new SendMessager();
		sendMessager.setMessageBody(phone);
		int random =0;
		for(int j = 0; j< 100; j++){
			random =((int)((Math.random()*9+1)*100000));
		}
		SysConfig selectByKey = configService.selectByKey(HttpConstants.sendSMSSetting);
		JSONObject jsonObject =JSONObject.parseObject(selectByKey.getConfigValue());
		//{"url":"https://smsc.xgate.com.hk/smshub/sendsms", "userId": "fulumsmsc", "UserPassword": "3gj80q34pc", "content": "One time password", "MessageType": "TEXT"}
		String semdContent =jsonObject.getString("content")+":" +random;
		AjaxResult result =null;
		SMSDto sms=new SMSDto();
		if(StringUtils.isEmpty(sms.getMessageBody())){
			sms.setMessageBody(semdContent);
		}
		sms.setMessageReceiver(phone);
		try {
			if(StringUtils.isEmpty(sms.getUserID())){
				sms.setUserID(jsonObject.getString("userId"));
			}
			if(StringUtils.isEmpty(sms.getUserPassword())){
				sms.setUserPassword(jsonObject.getString("UserPassword"));
			}
			if(StringUtils.isEmpty(sms.getMessageType())){
				sms.setMessageType(jsonObject.getString("MessageType"));
			}
			result =SMSUtil.sendMsg(sms,jsonObject.getString("url"));//
			SMSDto dto = (SMSDto) result.get("data");
			sendMessager.setMessageBody(dto.getMessageBody());
			sendMessager.setMessageReceiver(phone);
			sendMessager.setMessageType(dto.getMessageType());
			sendMessager.setReplyAllMsg(dto.getReplyAllMsg());
			sendMessager.setUserId(dto.getUserID());
			sendMessager.setSendCode(random+"");
			sendMessager.setCreateDate(new Date());
			sendMessagerMapper.insertSendMessager(sendMessager);
			return result;
		} catch (Exception e) {
			System.out.println("發送短信錯誤");
			return  AjaxResult.error();
		}
	    
	}
	@Override
	public AjaxResult invite(String phone) {
		SendMessager sendMessager =new SendMessager();
		sendMessager.setMessageBody(phone);
		int random =0;
		
		//判斷邀請用戶存不存在
		if(membersMapper.selectMembersByPhone(phone) != null){
			return AjaxResult.error("該用戶已經存在了");
		}
		SysConfig selectByKey = configService.selectByKey(HttpConstants.sendSMSSetting);
		JSONObject jsonObject =JSONObject.parseObject(selectByKey.getConfigValue());
		
		SysConfig selectByAppUpload = configService.selectByKey(HttpConstants.sendSMSSetting);
		String semdContent ="尊敬的用戶:" +jsonObject.getString("content")+"<a href='"+selectByAppUpload+"'/>下載APP</a>";
		AjaxResult result =null;
		SMSDto sms=new SMSDto();
		sms.setMessageBody(semdContent);
		sms.setMessageReceiver(phone);
		try {
			if(StringUtils.isEmpty(sms.getUserID())){
				sms.setUserID(jsonObject.getString("userId"));
			}
			if(StringUtils.isEmpty(sms.getUserPassword())){
				sms.setUserPassword(jsonObject.getString("UserPassword"));
			}
			if(StringUtils.isEmpty(sms.getMessageType())){
				sms.setMessageType(jsonObject.getString("MessageType"));
			}
			result =SMSUtil.sendMsg(sms,jsonObject.getString("url"));
			SMSDto dto = (SMSDto) result.get("data");
			sendMessager.setMessageBody(dto.getMessageBody());
			sendMessager.setMessageReceiver(phone);
			sendMessager.setMessageType(dto.getMessageType());
			sendMessager.setReplyAllMsg(dto.getReplyAllMsg());
			sendMessager.setUserId(dto.getUserID());
			sendMessager.setSendCode(random+"");
			sendMessagerMapper.insertSendMessager(sendMessager);
			return result;
		} catch (Exception e) {
			System.out.println("發送短信錯誤");
			return  AjaxResult.error();
		}
	}
	@Autowired
	private SendEmailUtil sendEmailUtil;
	@Override
	public AjaxResult sendEmail(String email) {
		//发送邮件
		SendEmaill sendEmaill =new SendEmaill();
		
		sendEmaill.setSendContent("尊敬的會員，請點擊修改密碼.");
		sendEmaill.setSendTo(email);
		sendEmaill.setTitle(null);
		try {
			sendEmailUtil.sendEmail(sendEmaill);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AjaxResult sendSMs(SMSDto sms) {
		SysConfig selectByKey = configService.selectByKey(HttpConstants.sendSMSSetting);
		JSONObject jsonObject =JSONObject.parseObject(selectByKey.getConfigValue());
		AjaxResult result =null;
		try {
			if(StringUtils.isEmpty(sms.getUserID())){
				sms.setUserID(jsonObject.getString("userId"));
			}
			if(StringUtils.isEmpty(sms.getUserPassword())){
				sms.setUserPassword(jsonObject.getString("UserPassword"));
			}
			if(StringUtils.isEmpty(sms.getMessageType())){
				sms.setMessageType(jsonObject.getString("MessageType"));
			}
			result =SMSUtil.sendMsg(sms,jsonObject.getString("url"));
			SMSDto dto = (SMSDto) result.get("data");
			SendMessager sendMessager =new SendMessager();
			sendMessager.setMessageBody(dto.getMessageBody());
			sendMessager.setMessageReceiver(sms.getMessageReceiver());
			sendMessager.setMessageType(dto.getMessageType());
			sendMessager.setReplyAllMsg(dto.getReplyAllMsg());
			sendMessager.setUserId(dto.getUserID());
			sendMessager.setCreateDate(new Date());
			sendMessagerMapper.insertSendMessager(sendMessager);
			return result;
		} catch (Exception e) {
			System.out.println("發送短信錯誤");
			return  AjaxResult.error();
		}
	}

	
}
