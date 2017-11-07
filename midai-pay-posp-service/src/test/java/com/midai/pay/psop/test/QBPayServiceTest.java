package com.midai.pay.psop.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.midai.pay.posp.PospServicesRunner;
import com.midai.pay.quick.qb.entity.QBQrCodePayReq;
import com.midai.pay.quick.qb.entity.QBQrCodePayResp;
import com.midai.pay.quick.qb.service.QBPayService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=PospServicesRunner.class)
public class QBPayServiceTest {
	
	@Autowired
	private QBPayService  qbPayService;

	
   public void testGenerateQBQRCode() {
	   QBQrCodePayReq req = new QBQrCodePayReq();
	   req.setTransAmt("0.01");
	   req.setMobile("15001120301");
	   qbPayService.generateQBQRCode(req);
   }
   // 商户向吉点(翰鑫)签到
//	@Test
	public void testSignToJidian(){
		String mercId = "880293829119130";
		String mercCode = "29119130";
		
//		pospService.signToJidian(mercId, mercCode);
	}
}
