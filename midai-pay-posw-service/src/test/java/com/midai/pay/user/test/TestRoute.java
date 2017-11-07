package com.midai.pay.user.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.midai.pay.PoswRunner;
import com.midai.pay.route.service.RouteService;
import com.midai.pay.route.service.ShenXinConfigService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=PoswRunner.class)
public class TestRoute {
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private ShenXinConfigService shenXinConfigService;
	
//	@Test
	public void testRoute(){
		String str = routeService.route("18516120709", 300);
		System.out.println(str);
	}
	
//	@Test
	public void testShenXinConfig(){
		String instCode="00000081";
		String mobile = "13391168315";
		String mercInstId = "adfadfasasdf";
		String mercInstMobile = "adfasfadsfasd";
		shenXinConfigService.configMerc(instCode, mobile, mercInstId, mercInstMobile);
	}
	
	@Test
	public void testShenXinBatchConfig(){
		String instCode = "00000081";
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader("poswtxt"));
			String str = null;
			while((str = br.readLine())!=null){
				String[] values = str.split(",");
				String mobile = values[0];
				String mercInstDeviceId = values[1];
				String mercInstId = values[2];
				shenXinConfigService.configMerc(instCode, mobile, mercInstId, mercInstDeviceId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(br!=null){
					br.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
