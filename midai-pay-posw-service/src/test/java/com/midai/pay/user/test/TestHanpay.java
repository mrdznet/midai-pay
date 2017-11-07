package com.midai.pay.user.test;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.midai.pay.PoswRunner;
import com.midai.pay.handpay.service.HandpayService;
import com.midai.pay.handpay.vo.InstiMerchantRequestBean;
import com.midai.pay.handpay.vo.InstiMerchantResponseBean;
import com.midai.pay.handpay.vo.TransQueryRequestBean;
import com.midai.pay.handpay.vo.WithdrawRequestBean;
import com.midai.pay.handpay.vo.WithdrawResponseBean;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=PoswRunner.class)
public class TestHanpay {
	
	@Autowired
	private HandpayService handpayService;
	
	
	// 商户进件
//	@Test
	public void instiMerchantTest(){
		InstiMerchantRequestBean req = new InstiMerchantRequestBean();
		req.setMerName("重庆小面");
		req.setRealName("王艳峰");
		req.setMerState("上海市");
		req.setMerCity("上海市");
		req.setMerAddress("上海市浦东新区峨山路155号");
		req.setCertType("01");
		req.setCertId("412725198806055714");
		req.setMobile("15618703344");
		req.setAccountId("6214830217712859");
		req.setAccountName("王艳峰");
		req.setBankName("中国招商银行");
		req.setBankCode("303100000006");
		req.setOpenBankName("中国招商银行新客站支行");
		req.setOpenBankState("上海市");
		req.setOpenBankCity("上海市");
		req.setT0drawFee("0.2");
		req.setT0drawRate("0.006");
		req.setT1consFee("0.2");
		req.setT1consRate("0.006");
		
		handpayService.instiMerchant(null, req);
	}
	
	//T+0 提现申请
	@Test
	public void t0WithdrawTest(){
		WithdrawRequestBean req = new WithdrawRequestBean();
		req.setSubMerId("880293829119177");
		
		SimpleDateFormat df_time = new SimpleDateFormat("yyyyMMddHHmmss");
		req.setOrderNo(df_time.format(new Date()));
		req.setTransAmount(" ");	//分
		req.setTransDate("20170605");
		
		handpayService.t0Withdraw(req);
	}
	
	//T+0 提现交易查询
//	@Test
	public void transQueryTest(){
		TransQueryRequestBean req = new TransQueryRequestBean();
		req.setSubMerId("880293829119130");
		req.setOrderNo("20170516102749");
		req.setTransDate("102749");
		req.setTransSeq("99311865");
		
		handpayService.transQuery(req);
	}
	
}
