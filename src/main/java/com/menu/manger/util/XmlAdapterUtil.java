package com.menu.manger.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.alibaba.fastjson.JSONObject;

import storelle.api.pos.MemberEnquiryResponse;

public class XmlAdapterUtil extends XmlAdapter<String, MemberEnquiryResponse> {

	@Override
	public MemberEnquiryResponse unmarshal(String v) throws Exception {
		return JSONObject.parseObject(v,MemberEnquiryResponse.class);
	}

	@Override
	public String marshal(MemberEnquiryResponse v) throws Exception {
		JAXBContext context =JAXBContext.newInstance(MemberEnquiryResponse.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");
		StringWriter writer = new StringWriter();
		marshaller.marshal(v,writer);
		String str =writer.toString();
		str=str.replace("<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"yes\"?>", "");
		 return str;
	}


}
