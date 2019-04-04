package com.menu.manger.util;
/**
 @author liuzhen
 @version 1.0.0
 @date: 2019年2月14日
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.DecoderException;
/**
 * 生产短链接
 * API 请访问: https://www.ft12.com/
 * @author liiuzhen
 *
 * 2019年2月14日下午2:46:54
 */
public class ShortUrlTest {
	public static void main(String[] args) throws DecoderException {
		try {
			String info = createShortUrl(
					"http://192.168.200.244:8080/SupplyChainWeb/pagePath/drawer/home.do?number=0.18473908114628879");
			System.out.println(info);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	final static String CREATE_API = "http://api.ft12.com/api.php";
	final static String TOKEN = "zz8SdsfDFAg1566@ddd"; 

	/**
	 * 创建短网址
	 *
	 * @param longUrl
	 *            长网址：即原网址
	 * @return 成功：短网址 失败：返回空字符串
	 * @throws IOException
	 * @throws DecoderException 
	 */
	public static String createShortUrl(String longUrl) throws IOException, DecoderException {
		String urls=CREATE_API+"?url="+longUrl+"&apikey="+TOKEN;
		BufferedReader reader = null;
		// 创建连接
		URL url = new URL(urls);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);

		// 发起请求
		connection.connect();
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
		out.flush();
		out.close();

		// 读取响应
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		String line;
		String res = "";
		while ((line = reader.readLine()) != null) {
			res += line;
		}
		reader.close();
		System.out.println(res);
		return res;
	}

}
