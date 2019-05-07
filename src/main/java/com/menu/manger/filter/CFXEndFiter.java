package com.menu.manger.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class CFXEndFiter extends AbstractPhaseInterceptor<SoapMessage> {

	public CFXEndFiter() {
		super(Phase.PRE_STREAM); // 流关闭前
	}
	String serviceUrl=null;
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		message.put("org.apache.cxf.message.Message.QUERY_STRING", "wsdl");
		serviceUrl =(String)message.get("http.base.path");

		try {

			OutputStream os = message.getContent(OutputStream.class);

			CachedStream cs = new CachedStream();

			message.setContent(OutputStream.class, cs);

			message.getInterceptorChain().doIntercept(message);

			CachedOutputStream csnew = (CachedOutputStream) message.getContent(OutputStream.class);
			InputStream in = csnew.getInputStream();

			String xml = toString(in);
			if(xml.indexOf("memberEnquiryResponse" ) > 0) {
			   xml =repiyStr("memberEnquiryResponse",xml);
			   //外面封装一层
			   //<coupons></coupons>
			   xml=xml.replace("</discount>", "</discount><coupons>").replace("</memberEnquiryResponse>", "</coupons></memberEnquiryResponse>");
			}
			if(xml.indexOf("closeTransactionResponse" ) > 0) {
			   xml =repiyStr("closeTransactionResponse",xml);
			}
			if(xml.indexOf("submitRedeemptionResponse" ) > 0) {
				   xml =repiyStr("submitRedeemptionResponse",xml);
			}
			if(xml.indexOf("reverseTransactionResponse" ) > 0) {
				   xml =repiyStr("reverseTransactionResponse",xml);
			}
			xml =xml.replace("<return>","").replace("</return>","").replace("<coupons xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />","");
			
			 System.out.println(xml);
			// 这里对xml做处理，处理完后同理，写回流中
			IOUtils.copy(new ByteArrayInputStream(xml.getBytes()), os);
			 
			cs.close();
			os.flush();

			message.setContent(OutputStream.class, os);

		} catch (Exception e) {
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

	public ByteArrayInputStream parse(final OutputStream out) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos = (ByteArrayOutputStream) out;
		final ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
		return swapStream;
	}

	public  String repiyStr(String key, String msg) {

		// 按指定模式在字符串查找
		msg = msg.replace("<ns2:" + key + " xmlns:ns2=\"http://www.meokbang.com.hk:81/storelle/api/pos\">",
				//msg = msg.replace("<ns2:" + key + " xmlns:ns2=\"http://flqd.majiangyun.com:8899/storelle/api/pos\">",
				"<" + key + ">");
		msg =msg.replaceAll("<soap:Body>", "<SOAP-ENV:Header/><SOAP-ENV:Body>");
		msg =msg.replaceAll("</soap:Body>", "</SOAP-ENV:Body>");
		msg =msg.replace("soap:Envelope", "SOAP-ENV:Envelope");
		return msg.replace("</ns2:" + key + ">", "</" + key + ">");
	}
	
}
