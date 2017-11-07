package com.midai.pay.user.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.midai.pay.PoswRunner;
import com.midai.pay.common.util.CreateParseCode;
import com.midai.pay.oss.PayOssService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=PoswRunner.class)
public class TestOss {

	@Autowired
	private PayOssService payOssService;
	@Value("${mifu.oss.customer-dir}")
	private String customerdir;
	
	@Value("${customer.qrcode.addr}")
	private String customerQrCodeAddr;
	
	@Test
	public void imgUploadTest() {
	
		//生产商户二维码
		String mercNo = "acd_sdd_1";
		//文件名称
		SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmss");
		String fileName = mercNo + df.format(new Date()) + ".png";
		customerQrCodeAddr += "?mercNo=" + mercNo;
		ByteArrayOutputStream baos = CreateParseCode.createQRCodeWithLogWithStream(customerQrCodeAddr, "");
		
		// oss上传文件

		if(baos != null) {
			String imgPath = payOssService.imgUpload(new ByteArrayInputStream(baos.toByteArray()), fileName, customerdir);
			System.out.println(imgPath);
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
