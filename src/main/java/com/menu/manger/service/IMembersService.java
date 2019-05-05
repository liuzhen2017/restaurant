package com.menu.manger.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.menu.manger.dto.Members;
import com.menu.manger.util.AjaxResult;

/**
 * 会员管理 服务层
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
public interface IMembersService 
{
	/**
     * 查询会员管理信息
     * 
     * @param id 会员管理ID
     * @return 会员管理信息
     */
	public Members selectMembersById(Integer id);
	
	/**
     * 查询会员管理列表
     * 
     * @param members 会员管理信息
     * @return 会员管理集合
     */
	public List<Members> selectMembersList(Members members);
	
	/**
     * 新增会员管理
     * 
     * @param members 会员管理信息
     * @return 结果
     */
	public int insertMembers(Members members);
	
	/**
     * 修改会员管理
     * 
     * @param members 会员管理信息
     * @return 结果
     */
	public int updateMembers(Members members);
		
	/**
     * 删除会员管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMembersByIds(String ids);
	/**
	 * 登陆
	 * @param email
	 * @param passWord
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JWTCreationException 
	 * @throws IllegalArgumentException 
	 * @throws JsonProcessingException 
	 */
	public AjaxResult login(String email, String passWord) throws JsonProcessingException, IllegalArgumentException, JWTCreationException, UnsupportedEncodingException;
	/**
	 * 注册
	 * @param members
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JWTCreationException 
	 * @throws IllegalArgumentException 
	 * @throws JsonProcessingException 
	 */
	public AjaxResult regist(Members members) throws JsonProcessingException, IllegalArgumentException, JWTCreationException, UnsupportedEncodingException;
	/**
	 * 发送邮件
	 * @param tokenId
	 * @return
	 */
	public AjaxResult sendEmail(String email);
	/**
	 * 重置密码
	 * @param oepnId
	 * @return
	 */
	public AjaxResult restPwd(String oepnId);
	/**
	 * 修改密码
	 * @param passWord
	 * @return
	 */
	public AjaxResult updatePwd(String passWord);

	public AjaxResult forgetEmaill(String phone);

	public AjaxResult vaildCode(String phone, String code);

	public AjaxResult checkTokenEmail(String token) throws JsonParseException, JsonMappingException, IllegalArgumentException, IOException;

	public AjaxResult findEmail(String phone, String code);

	/**
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	public AjaxResult saveIntegral(String str) throws UnsupportedEncodingException, NumberFormatException, ParseException;

	/**
	 * @param mem
	 * @return
	 */
	public AjaxResult cheCkRegist(Members mem);
	
}
