package com.midai.pay.psop.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.common.po.ResultVal;
import com.midai.pay.posp.PospServicesRunner;
import com.midai.pay.posp.entity.ConSumptionEntity;
import com.midai.pay.posp.entity.MiShuaPayParam;
import com.midai.pay.posp.entity.MiShuaPayQueryParam;
import com.midai.pay.posp.entity.SignParam;
import com.midai.pay.posp.service.PospService;
import com.midai.pay.route.service.RouteService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=PospServicesRunner.class)
public class PospServiceTest {
	
	@Autowired
	private PospService  pospService;
	
//	@Autowired
	private RouteService routeService;
	
   private SignParam signParam;
   
   private ConSumptionEntity conSumptionEntity;
   
   private MiShuaPayParam replacePayParam;
   
   
   // @Before
   public void init(){
	   signParam=new SignParam();
	   signParam.setPHONENUMBER("13391168315");
	   signParam.setPSAMCARDNO("CD07000400000546");
	   
/*	   conSumptionEntity=new ConSumptionEntity();
	   conSumptionEntity.setAPPTOKEN("apptoken");
	   conSumptionEntity.setCHECKX("31.22"); // 纬度
	   conSumptionEntity.setCHECKY("121.52");	//经度
	   conSumptionEntity.setCRDNO("6236681210001162770");	//卡号
	   conSumptionEntity.setCRDSQN("001");	// cardSeqNum
	   conSumptionEntity.setCTXNAT("000000001000");	//金额 
	   conSumptionEntity.setELESIGNA("string");		// 图片string 未写
	   conSumptionEntity.setICDAT("9F2608A2CA1723B26228579F2701809F101307010103A0A000010A0100000000004359602C9F3704001C2D189F3602037D950580000008009A031609209C01009F02060000000010005F2A02015682027C009F1A0201569F03060000000000009F330390C8C09F34033F00009F3501229F1E0831323334353637388408A0000003330101019F090200209F410400000013");
	   conSumptionEntity.setIDFID("4");	//扣率
	   conSumptionEntity.setInMod("051");
	   conSumptionEntity.setMAC("7AD83650");
	   conSumptionEntity.setPCSIM("pcsim");
	   conSumptionEntity.setPHONENUMBER("18860971839");	// 手机号
	   conSumptionEntity.setPSAMCARDNO("5011603100000349");
	   conSumptionEntity.setSPECIALFEETYPE("1");
	   conSumptionEntity.setTERMINALNUMBER("5011603100000349");
	   conSumptionEntity.setTPINBLK("B5821A50ABB8D80A");	// pin
	   conSumptionEntity.setTRACK("ABED764B4DE80BD43FE2B1E100A7312770B8D0F5BEA81BF7");
	   conSumptionEntity.setTRANCODE("180001");	//接口
	   conSumptionEntity.setTSEQNO("000001"); //流水号
	   conSumptionEntity.setTTXNDT("0920");
	   conSumptionEntity.setTTXNTM("155400");*/
	   
	   
	   // --2 <>
	   conSumptionEntity=new ConSumptionEntity();
	   conSumptionEntity.setAPPTOKEN("apptoken");
	   conSumptionEntity.setCRDNO("6236681210001162770");	//卡号
	   conSumptionEntity.setCTXNAT("000000001000");
	   conSumptionEntity.setELESIGNA("string");
	   conSumptionEntity.setMAC("A404F81C");
	   conSumptionEntity.setPCSIM("pcsim");
	   conSumptionEntity.setPHONENUMBER("18806298203");	// 手机号
	   conSumptionEntity.setPSAMCARDNO("5021603040000470");
	   conSumptionEntity.setSPECIALFEETYPE("1");
	   conSumptionEntity.setTERMINALNUMBER("5021603040000470");
	   conSumptionEntity.setTPINBLK("E2885274002CE070");
	   conSumptionEntity.setTRACK("AFEB4673D6D328F1F47051E6621609056EB88607D2A53904AD6DB168780DDBA2AD6DB168780DDBA2AD6DB168780DDBA2AD6DB168780DDBA2AD6DB168780DDBA2AD6DB168780DDBA2AD6DB168780DDBA2");
	   conSumptionEntity.setTRANCODE("180001");	//接口
	   conSumptionEntity.setTSEQNO("000001"); //流水号
	   conSumptionEntity.setTTXNDT("0511");
	   conSumptionEntity.setTTXNTM("023923");
	   // --2 <>
	   
/*	   replacePayParam=new ReplacePayParam();
	   replacePayParam.setCardNo("6214830217712859");
	   replacePayParam.setBlankNo("403100000004");
	   replacePayParam.setName("王艳峰");
	   replacePayParam.setMoney("10");
	   replacePayParam.setSeqNo("100084196350");*/
	   
   }
	
	
	/*@Test
	public void test(){
		MiShuaPayParam rpp = new MiShuaPayParam();
		rpp.setCardNo("6214920601193970");
		rpp.setBlankNo("303290000569");
		rpp.setName("吕夏令");
		rpp.setMoney("792");
		rpp.setTranSsn("000000991121");
		ResultVal<String> rv = pospService.miShuaPay(rpp);
		System.out.println(rv.isSuccess());
		System.out.println(rv.getMsg());
		System.out.println(rv.getValue());
	}*/
	
/*	@Test
	public void test2(){
		MiShuaPayQueryParam qrpp = new MiShuaPayQueryParam();
		qrpp.setDealSsn("000000991121");
		qrpp.setPaySsn("006010");
		ResultVal<String> rv = pospService.miShuaPayQuery(qrpp);
		System.out.println(rv.isSuccess());
		System.out.println(rv.getMsg());
		System.out.println(rv.getValue());
	} */
	
   // 向通道签到
//	@Test
	public void test3(){
		//申鑫
		String instCode = "00000081";
		String mercId = "201702230002729";
		
		pospService.signToInst(instCode, mercId);
	}
	
	/*@Test
	public void testRoute(){
		String str = routeService.route("18516120709", 300);
		System.out.println(str);
	}*/
   
//   @Test
	public void test4(){
	   //签到
/*	   SignParam signParam=new SignParam();
	   signParam.setPHONENUMBER("18806298203");
	   signParam.setPSAMCARDNO("5021603040000470");
	   pospService.sgin(signParam);*/
	   
	   //交易
	   ConSumptionEntity conSumptionEntity = new ConSumptionEntity();
	   // 磁条卡
	  /* conSumptionEntity.setAPPTOKEN("apptoken");
	   conSumptionEntity.setCRDNO("6236681210001162770");	//卡号
	   conSumptionEntity.setCRDSQN("000");
	   conSumptionEntity.setCTXNAT("000000000100");
	   conSumptionEntity.setELESIGNA("string");
	   conSumptionEntity.setMAC("A404F81C");
	   conSumptionEntity.setPCSIM("pcsim");
	   conSumptionEntity.setPHONENUMBER("18806298203");	// 手机号
	   conSumptionEntity.setPSAMCARDNO("5021603040000470");
	   conSumptionEntity.setSPECIALFEETYPE("1");
	   conSumptionEntity.setTERMINALNUMBER("5021603040000470");
	   conSumptionEntity.setTPINBLK("E2885274002CE070");
	   conSumptionEntity.setTRACK("AFEB4673D6D328F1F47051E6621609056EB88607D2A53904AD6DB168780DDBA2AD6DB168780DDBA2AD6DB168780DDBA2AD6DB168780DDBA2AD6DB168780DDBA2AD6DB168780DDBA2AD6DB168780DDBA2");
	   conSumptionEntity.setTRANCODE("180001");	//接口
	   conSumptionEntity.setTSEQNO("000001"); //流水号
	   conSumptionEntity.setTTXNDT("0511");
	   conSumptionEntity.setTTXNTM("023923");*/
	   
	   
	   //IC卡
	   conSumptionEntity.setAPPTOKEN("apptoken");
	   conSumptionEntity.setCRDNO("6214830217712859");	//卡号
	   conSumptionEntity.setCRDSQN("000");
	   conSumptionEntity.setCTXNAT("000000000100");
	   conSumptionEntity.setELESIGNA("string");
	   conSumptionEntity.setICDAT("9F26089EFB0B635186D9219F2701809F101307010103A0A000010A0100000000006456459F9F3704000508029F36020051950580000008009A031705229C01009F02060000000010005F2A02015682027C009F1A0201569F03060000000000009F330390C8C09F34033F00009F3501229F1E0831323334353637388408A0000003330101019F090200209F410400000002");
	   conSumptionEntity.setInMod("051");
	   conSumptionEntity.setMAC("5B0D35F8");
	   conSumptionEntity.setPCSIM("pcsim");
	   conSumptionEntity.setPHONENUMBER("18806298203");	// 手机号
	   conSumptionEntity.setPSAMCARDNO("5021603040000470");
	   conSumptionEntity.setSPECIALFEETYPE("1");
	   conSumptionEntity.setTERMINALNUMBER("5021603040000470");
	   conSumptionEntity.setTPINBLK("3501A8514B5D8443");
	   conSumptionEntity.setTRACK("053BAEC680F58515D3726ACFCCF14684C020839113480591");
	   conSumptionEntity.setTRANCODE("180001");	//接口
	   conSumptionEntity.setTSEQNO("000001"); //流水号
	   conSumptionEntity.setTTXNDT("0522");
	   conSumptionEntity.setTTXNTM("180515");
	   
	   pospService.consumption(conSumptionEntity);
	}
	
   
   // 商户向吉点(翰鑫)签到
	@Test
	public void testSignToJidian(){
		String mercId = "880293829119155";
		String mercCode = "29119155";
		
		pospService.signToJidian(mercId, mercCode);
	}
}
