package com.menu.manger.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.stereotype.Component;

@Component
public class CFXFiter extends AbstractPhaseInterceptor<SoapMessage> {

	public CFXFiter() {
		super(Phase.RECEIVE);
	}
	

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		message.put("org.apache.cxf.message.Message.QUERY_STRING", "wsdl");
		String serviceUrl =(String)message.get("http.base.path");
		InputStream is = message.getContent(InputStream.class);
		if (is != null) {
			String str = toString(is);
			// 原请求报文
			System.out.println("====> request xml=\r\n" + str);
			// 把siebel格式的报文替换成符合cxf带前缀的命名空间
			str = repiyStr("memberEnquiry",str,serviceUrl);
			//把siebel格式的报文替换成符合cxf带前缀的命名空间
			str = repiyStr("closeTransaction",str,serviceUrl);
			//把siebel格式的报文替换成符合cxf带前缀的命名空间
			str = repiyStr("submitRedeemption",str,serviceUrl);
			//把siebel格式的报文替换成符合cxf带前缀的命名空间
			str = repiyStr("reverseTransaction",str,serviceUrl);
			// 替换后的报文
			System.out.println("====> replace xml=\r\n" + str);
			InputStream ism = new ByteArrayInputStream(str.getBytes());
			message.setContent(InputStream.class, ism);
		}
	}

	public String toString(InputStream ips) {
		StringBuffer out = new StringBuffer();
		try {
			byte[] b = new byte[4096];
			for (int n; (n = ips.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString();

	}
	
	public String  repiyStr( String key,String msg,String serviceUrl ){
		 
	      // 按指定模式在字符串查找
	      String line ="<"+ key+">";
	      msg =msg.replace(line,"<rs:"+key+" xmlns:rs=\""+serviceUrl+"/storelle/api/pos\">");
	      return msg.replace("</"+ key+">","</rs:"+key+">");
	   }
}
