package com.menu.manger.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.zxing.WriterException;
import com.menu.manger.dto.Members;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.service.ICouponMangerService;
import com.menu.manger.service.IMembersService;
import com.menu.manger.service.IMenuFoodService;
import com.menu.manger.util.AESUtil;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.JsonWebTokenUtil;
import com.menu.manger.util.QrCodeCreateUtil;


/**
 * 会员管理 信息操作处理
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Controller
@RequestMapping("/api/members/")
@ResponseBody
public class MembersAPIController extends BaseController
{
    private String prefix = "system/members";
	@Autowired
	private IMembersService membersService;
	
	@Autowired
	private ICouponMangerService couponService;
	@Autowired
	private IMenuFoodService menuFoodService;
	
	/**
	 * 登陆
	 * @param email
	 * @param passWord
	 * @return
	 */
	@RequestMapping("MembersLogin.do")
	public AjaxResult membersLogin(@RequestParam(required=true) String email,@RequestParam(required=true) String passWord,HttpServletRequest request) {
		try {
			//获取I派
//			String ip =NetworkUtil.getIpAddress(request);
		    return	membersService.login(email,passWord);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("获取IP错误!");
		}
	}
	/**
	 * 获取用户信息
	 * @param email
	 * @param passWord
	 * @return
	 */
	@RequestMapping("getMenbersInfo.do")
	public AjaxResult getMenbersInfo() {
		try {
			
		    Members mem =membersService.selectMembersById(getUserInfo().getId());
		    mem.setPwd(null);
		    mem.setSalt(null);
		    Map<String, Object> data = new HashMap<String, Object>();
			data.put("webToken", JsonWebTokenUtil.sign(mem));
			data.put("membersInfo", mem);
			data.put("myscorp", mem.getScore());
		    return AjaxResult.success(0,"",data);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("获取IP错误!");
		}
	}
	/**
	 * 註冊
	 * @param email
	 * @param passWord
	 * @return
	 */
	@PostMapping("regist.do")
	public AjaxResult memberRegist(@RequestParam("members")String members) {
		try {
			members=members.replace("\\", "");
			members=members.substring(1,members.length()-1);
			Members mem =JSONObject.parseObject(members,Members.class);
			if(mem ==null){
				return AjaxResult.error("請求參數爲空!");
			}
			if(mem.getName() ==null){
				return AjaxResult.error("姓名不能爲空!");
			}
			if(mem.getEmail() ==null){
				return AjaxResult.error("郵箱不能爲空!");
			}
			if(mem.getPhone() ==null){
				return AjaxResult.error("電話不能爲空!");
			}
		    return	membersService.regist(mem);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("系统错误!"+e.getMessage());
		}
	}
	/**
	 * 發送短信,驗證短信驗證碼
	 * <li>查詢該手機號是否存在</li>
	 * <li>發送短信</li>
	 * @param phone
	 * @return
	 */
	@RequestMapping("forgetEmaill.do")
	public AjaxResult forgetEmaill(@RequestParam(required=true)String phone) {
		try {
		    return	membersService.forgetEmaill(phone);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("系统错误!"+e.getMessage());
		}
	}
	/**
	 * 驗證短信驗證碼
	 * <li>查詢數據驗證碼是否正確</li>
	 * @param phone
	 * @return
	 */
	@RequestMapping("vaildCode.do")
	public AjaxResult vaildCode(@RequestParam(required=true)String phone,@RequestParam(required=true)String code) {
		try {
		    return membersService.vaildCode(phone,code);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("系统错误!"+e.getMessage());
		}
	}
	/**
	 * 驗證短信驗證碼
	 * <li>查詢數據驗證碼是否正確</li>
	 * @param phone
	 * @return
	 */
	@RequestMapping("findEmail.do")
	public AjaxResult findEmail(@RequestParam(required=true)String phone,@RequestParam(required=true)String code) {
		try {
		    return membersService.findEmail(phone,code);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("系统错误!"+e.getMessage());
		}
	}
	/**
	 * 发送邮件
	 * <li>查找该会员邮箱</li>
	 * <li>封装openId</li>
	 * <li>发送邮件,携带连接</li>
	 * <li>解析openID,获取该会员ID</li>
	 * <li>重置密码</li>
	 * @param email
	 * @param passWord
	 * @param request
	 * @return
	 */
	@RequestMapping("sendEmail.do")
	public AjaxResult sendEmail(@RequestParam(required=true)String email) {
		try {
		    return	membersService.sendEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("系统错误!"+e.getMessage());
		}
	}
	/**
	 * 发送邮件
	 * <li>查找该会员邮箱</li>
	 * <li>封装openId</li>
	 * <li>发送邮件,携带连接</li>
	 * <li>解析openID,获取该会员ID</li>
	 * <li>重置密码</li>
	 * @param email
	 * @param passWord
	 * @param request
	 * @return
	 */
	@RequestMapping("checkTokenEmail.do")
	public AjaxResult checkTokenEmail(@RequestParam(required=true)String token) {
		try {
		    return	membersService.checkTokenEmail(token);
		}catch(TokenExpiredException tokenExpired){
			return AjaxResult.error("郵件已經過期,請重新發送郵件");
		}catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("系统错误!"+e.getMessage());
		}
	}
	/**
	 * 发送邮件
	 * <li>解析openID,获取该会员ID</li>
	 * <li>判断openID失效没,如果失效了</li>
	 * <li>重置密码</li>
	 * @param email
	 * @param passWord
	 * @param request
	 * @return
	 */
	@GetMapping("restPwd.do")
	public AjaxResult restPwd(String oepnId) {
		try {
		    return	membersService.restPwd(oepnId);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("系统错误!"+e.getMessage());
		}
	}
	/**
	 * 修改密码
	 * @param passWord
	 * @param request
	 * @return
	 */
	@GetMapping("updatePwdPage")
	public String updatePwdPage() {
		 return prefix + "/updatePwdPage";
	}
	
	/**
	 * 修改密码
	 * @param passWord
	 * @param request
	 * @return
	 */
	@PostMapping("updatePwd.do")
	public AjaxResult updatePwd(@RequestParam(required=true)String passWord,@RequestParam(required=true)String passWord1) {
		try {
			if(!passWord.equals(passWord1)){
				return AjaxResult.error("兩次輸入的密碼不一致!");
			}
		    return	membersService.updatePwd(passWord);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("修改密碼錯誤!"+e.getMessage());
		}
	}
	
	
	/**
	 * 修改会员信息
	 * @param request
	 * @return
	 */
	@GetMapping("updateMembersPage")
	public String updateMembersPage() {
		 return prefix + "/updateMembersPage";
	}
	
	/**
	 * 修改密码
	 * @param passWord
	 * @param request
	 * @return
	 */
	@PostMapping("updateMembers.do")
	public AjaxResult updateMembers(Members members) {
		try {
			
		    return	toAjax(membersService.updateMembers(members));
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("系统错误!"+e.getMessage());
		}
	}
	
	/**
	 * 查询会员
	 */
	@GetMapping("/queryInfo.do")
	public AjaxResult queryInfo()
	{
		Members members2 = super.getUserInfo();
		Members members = membersService.selectMembersById(members2.getId());
		members.setPwd(null);
		members.setSalt(null);
		return AjaxResult.success("查詢成功",members);
	}
	
	/**
	 * 查询会员二維碼
	 */
	@GetMapping("/queryMembersQC.do")
	public void queryMembersQC(HttpServletResponse response)
	{
		Members members2 = super.getUserInfo();
		Members members = membersService.selectMembersById(members2.getId());
		members.setPwd(null);
		members.setSalt(null);
		OutputStream outputStream;
		try {
			outputStream = response.getOutputStream();
			String content = members.getEmail();
			QrCodeCreateUtil.createQrCode(outputStream, content, 900, "JPEG");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	
	
	 /**
     * 解析二維碼
     */
    @RequestMapping("/common/readMembers.do")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
		String str =null;
    	try {
			
    		str =QrCodeCreateUtil.readQrCode(file.getInputStream());
		} catch (Exception e) {
			return AjaxResult.error("無法識別該二維碼!");
		}
    	logger.info("解析二维码: {}",str);
    	if(!StringUtils.isEmpty(str) && str.contains("http://www.storellet.com/a/qrCode/s=")){
    		return	membersService.saveIntegral(str);
    	}
    	str =AESUtil.AES_CBC_Decrypt(str);
    	if(!StringUtils.isEmpty(str) && str.contains("type=menuFood;")){
    		return	menuFoodService.qcMenuFood(str);
    	}
    	return   couponService.selectCouponMangerByCouponCode(str);
    }
	/**
	 * 查询会员管理列表
	 */
	@PostMapping("/list.do")
	@ResponseBody
	public TableDataInfo list(Members members)
	{
		startPage();
        List<Members> list = membersService.selectMembersList(members);
		return getDataTable(list);
	}
	
	
	
	/**
	 * 新增会员管理
	 */
	@GetMapping("/add.do")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存会员管理
	 */
	@PostMapping("/add.do")
	@ResponseBody
	public AjaxResult addSave(Members members)
	{		
		return toAjax(membersService.insertMembers(members));
	}

	/**
	 * 修改会员管理
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Members members = membersService.selectMembersById(id);
		mmap.put("members", members);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存会员管理
	 */
	@PostMapping("/edit.do")
	@ResponseBody
	public AjaxResult editSave(Members members)
	{		
		return toAjax(membersService.updateMembers(members));
	}
	
	/**
	 * 删除会员管理
	 */
	@PostMapping( "/remove.do")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(membersService.deleteMembersByIds(ids));
	}
	
}
