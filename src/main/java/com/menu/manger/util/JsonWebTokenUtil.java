package com.menu.manger.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 @author liuzhen
 @version 创建时间: 2018年5月8日上午10:36:55
 <br/>
 使用json web token和前段交互<br/>
 1.用户登陆服务器<br/>
 2.服务器生成jwt<br/>
 3.用户请求携带jwt<br/>
 4.服务器解析验证 jwt<br/>
 5.验证成功返回数据,失败,则跳转登陆<br/>
*/
public class JsonWebTokenUtil {
	/**
	 * 秘钥
	 */
	private static final String SECRET="SEjCwRETt";
	/**
	 * jackson
	 */
	private static ObjectMapper mapper= new ObjectMapper();
	
	 /** token 过期时间: 30天 */
    public static final int calendarField = Calendar.DATE;
    public static final int calendarInterval = 30;
	/**
	 * heard数据
	 * @return
	 */
	private static Map<String, Object> createHead(){
		Map<String, Object> map =new HashMap<>();
		map.put("typ", "JWT");
		map.put("alg","HS256");
		return map;
	}
	/**
	 * 生成tokent
	 * @param obj 对象数据
	 * @param maxAge 有效期
	 * @return
	 * @throws JsonProcessingException
	 * @throws IllegalArgumentException
	 * @throws JWTCreationException
	 * @throws UnsupportedEncodingException
	 */
	public static<T> String sign(T obj,long maxAge,Date iatDate) throws JsonProcessingException, IllegalArgumentException, JWTCreationException, UnsupportedEncodingException {
		Builder builder =JWT.create();
		builder.withHeader(createHead()) //header
		.withSubject(mapper.writeValueAsString(obj)); //payload
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(calendarField, calendarInterval); 
		Date expiresDate = nowTime.getTime();
		if(maxAge >=0) {
			long expMillis =System.currentTimeMillis()+maxAge;
			Date exp =new Date(expMillis);
			builder.withIssuedAt(iatDate);
			builder.withExpiresAt(exp);
		}else{
			builder.withIssuedAt(iatDate);
			builder.withExpiresAt(expiresDate);
			
		}
		return builder.sign(Algorithm.HMAC256(SECRET));
	}
	/**
	 * 生成tokent
	 * @param obj 对象数据
	 * @param maxAge 有效期
	 * @return
	 * @throws JsonProcessingException
	 * @throws IllegalArgumentException
	 * @throws JWTCreationException
	 * @throws UnsupportedEncodingException
	 */
	public static<T> String sign(T obj,Date dt) throws JsonProcessingException, IllegalArgumentException, JWTCreationException, UnsupportedEncodingException {
		return sign(obj,60*1000*60*24*7,dt);
	}
	/**
	 * 解密token
	 * @param token token
	 * @param classT 需要解密的类
	 * @return
	 * @throws IllegalArgumentException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T unsign(String token,Class<T> classT) throws IllegalArgumentException, JsonParseException, JsonMappingException, IOException{
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		Verification verifier =JWT.require(Algorithm.HMAC256(SECRET));
		DecodedJWT jwt= verifier.build().verify(token);
		Date exp =jwt.getExpiresAt();
		if(exp != null && exp.after(new Date())) {
			String subject =jwt.getSubject();
			return mapper.readValue(subject, classT);
		}
		return null;
	}
}
