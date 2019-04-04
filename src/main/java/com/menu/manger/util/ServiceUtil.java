/**
 * 
 */
package com.menu.manger.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.menu.manger.dto.Members;

/**
 * @author liuzhen
 *
 */
public class ServiceUtil {
	/**
	 * 更新用户数据
	 * @param member
	 * @return
	 * @throws JsonProcessingException
	 * @throws IllegalArgumentException
	 * @throws JWTCreationException
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, Object> tokenByUser(Members member) throws JsonProcessingException, IllegalArgumentException, JWTCreationException, UnsupportedEncodingException{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("webToken", JsonWebTokenUtil.sign(member));
		data.put("membersInfo", member);
		data.put("myscorp", member.getScore());
		return data;
	}
}
