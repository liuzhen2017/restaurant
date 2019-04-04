package com.menu.mangerw;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.menu.manger.MangerApplication;
import com.menu.manger.service.IMembersService;
import com.menu.manger.service.IPosTransactionSerivce;
import com.menu.manger.service.impl.PosTransactionSerivce;
import com.menu.manger.util.AESUtil;
import com.menu.manger.util.AjaxResult;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=MangerApplication.class)
public class MangerwApplicationTests {
	 private static final Logger log = LoggerFactory.getLogger(PosTransactionSerivce.class);
	@Autowired
	IPosTransactionSerivce transactionService;
	@Autowired
	private IMembersService membersService;
	@Test
	public void closeTransaction() throws Exception {
		long beginTime =System.currentTimeMillis();
		log.info("开始调用..");
		String brandID ="8";
		String transactionDatetime="201903151432";
		String shopCode="819";
		String invoiceNo="0006500";
		String invoiceAmount ="15";
		String  netAmount ="15";
		String pax ="9";
		String coupons ="";
		String memberID ="";
		String t="1554371515446";
		String h ="fd40a402d8af9ff871ec8aec0a104be8";
		transactionService.closeTransaction(brandID, transactionDatetime, shopCode, invoiceNo, invoiceAmount, netAmount, pax, coupons, memberID, t, h);
		long end =System.currentTimeMillis();
		log.info("结束调用调用:"+(end-beginTime));
	}
	@Test
	public void decryptQC() throws Exception {
		long beginTime =System.currentTimeMillis();
		log.info("开始调用..");
	//扫描獲取優惠代碼 積分
    String str ="http://www.storellet.com/a/qrCode/s=CIs9wzASU03LkPp58In0uTb8rmxU2X2y5IxvFhX+AmU=";		
	if(!StringUtils.isEmpty(str) && str.contains("http://www.storellet.com/a/qrCode/s=")){
			AjaxResult saveIntegral = membersService.saveIntegral(str);
			System.out.println(JSONObject.toJSONString(saveIntegral));
	}
		long end =System.currentTimeMillis();
		log.info("结束调用调用:"+(end-beginTime));
	}
}

