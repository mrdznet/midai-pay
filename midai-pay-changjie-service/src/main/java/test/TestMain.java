package test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.midai.pay.changjie.bean.G10001Bean;
import com.midai.pay.changjie.bean.G20001Bean;
import com.midai.pay.changjie.bean.G20004Bean;
import com.midai.pay.changjie.bean.G20015Bean;
import com.midai.pay.changjie.service.impl.CjMsgSendServiceImpl;
import com.midai.pay.changjie.service.impl.CjMsgSendServiceImpl.sendAndGetBytes_Response;

import util.Cj;
import util.FileUtil;



public class TestMain {
	public static SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmssSSS");
	public static void main(String[] args) throws Exception {
		//System.out.println(new Random(2).nextInt(99)+1);
		g10002Test();
//		g20001Test();
		//g20004Test();
		//g20015Test();
		//g40003Test();
	}
	
	public static void g10002Test(){ //代付
		CjMsgSendServiceImpl service = new CjMsgSendServiceImpl();
		String time = sf.format(new Date());
		G10001Bean data = new G10001Bean();
		data.setReqSn(Cj.CJPARAM_mertid+time + "01");
		//对私0  对公1
		data.setAccountProp("0");
		data.setCurrency("CNY");
		data.setAmount(993); //金额 单位分
		data.setAccountNo("6214850216480027");//账号
		data.setAccountName("龚贇");//账户名称
		data.setBankName("招商银行股份有限公司上海张江支行");//开户行名称  对私对应银行全称
		data.setBankCode("308290003492");//开户行号
		data.setDrctBankCode("308584000013");//清算行号
		data.setBankGeneralName("招商银行股份有限公司");//银行全称
		
		service.G10002SendMessage(data);
		Gson g = new Gson();
		System.out.println(g.toJson(data));
		
	}
	
	public static void g20001Test(){ //实时查询交易结果
		CjMsgSendServiceImpl service = new CjMsgSendServiceImpl();
		G20001Bean data = new G20001Bean();
		String time = sf.format(new Date());
		data.setReqSn(Cj.CJPARAM_mertid+time + "01");//此次交易号
		data.setQryReqSn("cp201610138297416101717333469901");//查询交易流水号
		service.G20001SendMessage(data);
		
		Gson g = new Gson();
		System.out.println(g.toJson(data));
	}
	public static void g20004Test(){ //交易明细查询
		CjMsgSendServiceImpl service = new CjMsgSendServiceImpl();
		G20004Bean data = new G20004Bean();
		String time = sf.format(new Date());
		data.setReqSn(Cj.CJPARAM_mertid+time + "01"); //此次交易号
		data.setStartDay("20161017000000");//查询开始时间
		data.setEndDay("20161017230000");//查询结束时间
		data.setStatus("2");//交易状态条件：0=成功；1=失败；2=全部

		data.setQryPage(1);//页码  第一次查询必须传1
		service.G20004SendMessage(data);
		
		Gson g = new Gson();
		System.out.println(g.toJson(data));
	}
	public static void g20015Test(){ //查询商户余额
		CjMsgSendServiceImpl service = new CjMsgSendServiceImpl();
		G20015Bean data = new G20015Bean();
		String time = sf.format(new Date());
		data.setReqSn(Cj.CJPARAM_mertid+time + "01"); //此次交易号
		service.G20015SendMessage(data);
		
		Gson g = new Gson();
		System.out.println(g.toJson(data));
	}
	
	public static void g40003Test() throws Exception{ //对账文件
		CjMsgSendServiceImpl service = new CjMsgSendServiceImpl();
		String time = sf.format(new Date());
		String req = Cj.CJPARAM_mertid+time + "01"; //此次交易号
		sendAndGetBytes_Response ret = service.G40003SendMessage("00","","20161018",req);
		//第一个参数是  固定 00   ，第二个是按月-yyyyMM 第三个是按日yyyyMMdd  ，第四个是交易号
		try {
			
			InputStream sbs = new ByteArrayInputStream(ret.data); 
			OutputStream out = new FileOutputStream(new File("d:\\"+req+".zip"));
			boolean bl = FileUtil.zipFile(sbs, out, "mytxt.txt");
			
			OutputStream out2 = new FileOutputStream(new File("d:\\1111.zip"));
			out2.write(ret.data);
			out.flush();
			out2.close();
			Gson g = new Gson();
			System.out.println(g.toJson(ret));
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*File file = new File("d:\\"+req+".txt");
		OutputStream output = new FileOutputStream(file);
	    BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);*/
		
	}
	
	
}
