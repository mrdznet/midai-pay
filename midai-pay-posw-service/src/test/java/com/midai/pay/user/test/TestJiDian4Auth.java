package com.midai.pay.user.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.midai.pay.PoswRunner;
import com.midai.pay.handpay.service.JiDian4AuthService;
import com.midai.pay.handpay.vo.AuthBean;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=PoswRunner.class)
public class TestJiDian4Auth {
	
	@Autowired
	JiDian4AuthService jiDian4AuthService;
	
	
	//四要素认证
//	@Test
	public void auth4(){
		AuthBean auth = new AuthBean();
		auth.setCustomerNm("王艳峰");
		auth.setCertifId("412725198806055714");
		auth.setPhoneNo("15618703352");
		auth.setAccNo("6214830217712859");
		auth.setOrderId(String.valueOf(System.currentTimeMillis()));
		
		jiDian4AuthService.auth4(auth);
	}
	
	//认证查询
//	@Test
	public void authQuery(){
		jiDian4AuthService.authQuery("1494904131395");
	}
}
