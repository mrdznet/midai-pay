package com.midai.pay.activiti.test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;

public class SimpleTest {

	public static void main(String[] args) throws ClientProtocolException, IOException  {
		// http://localhost:7878/msg/multiple		text, to
		
		/*CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost("http://localhost:7878/msg/multiple");
		
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("text", "he000"));
		nvps.add(new BasicNameValuePair("to", "test"));
		
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		
		CloseableHttpResponse response = httpclient.execute(httpPost);

		try {
		    System.out.println(response.getStatusLine());
		    HttpEntity entity2 = response.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    EntityUtils.consume(entity2);
		} finally {
		    response.close();
		}*/
		
		Double d = 0.68;
		System.out.println(new BigDecimal(d.toString()).divide(new BigDecimal(100),4,BigDecimal.ROUND_HALF_UP));
	}
	
	// 写图片
	void imgCreat() throws IOException{
		InputStream is = new BufferedInputStream(new FileInputStream("G:/img/2.png"));//主图
		
		BufferedImage im = ImageIO.read(is);
		InputStream is2 = new BufferedInputStream(new FileInputStream("G:/img/logo.png"));//签名图
		
		Graphics2D g = im.createGraphics();
		g.setColor(Color.BLACK);
		g.setFont(new Font("宋体",Font.BOLD,40));
		
		if(null != is2){
			BufferedImage im2 = ImageIO.read(is2);
			g.drawImage(im2,300,1750,300,300,null);
		}
		
		//表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
         g.drawString("商户名称001",500,486);//商户名称
         g.drawString("商户编号001",500,575);//商户编号
         g.drawString("终端编号001",500,695);//终端编号
         g.drawString("01",500,815);//操作员号  
         g.drawString("卡号001",500,915);//卡号
         g.drawString("卡有效期001",500,1050);//卡有效期
         g.drawString("发卡行001",250,1165);//发卡行
         g.drawString("批次号001",820,1165);//批次号
         g.drawString("交易类型001",250,1285);//交易类型
         g.drawString("参考号001",820,1285);//参考号
         g.drawString("凭证号001",250,1400);//凭证号
         g.drawString("000001",820,1400);//授权码
         g.drawString("时间/日期",350,1520);//时间/日期
         g.drawString("RMB: 100000",250,1645);//金额
         
         g.dispose();
         
         File outputfile = new File("G:/img/2-b.png");
         
         ImageIO.write(im, "png", outputfile);
         
	     is.close();
         if(is2!=null)is2.close();
	      
	      System.out.println("------------------end");
	}

}
	