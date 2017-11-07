package com.midai.pay.trade.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.StringUtil;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.common.PageEnum;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.mobile.utils.MassageUtil;
import com.midai.pay.mobile.vo.EticketSignVo;
import com.midai.pay.oss.PayOssService;
import com.midai.pay.trade.entity.BoEticket;
import com.midai.pay.trade.entity.DealTotal;
import com.midai.pay.trade.entity.SaleeSlip;
import com.midai.pay.trade.mapper.BoEticketMapper;
import com.midai.pay.trade.mapper.TradeReviewMapper;
import com.midai.pay.trade.query.TradeReviewQuery;
import com.midai.pay.trade.service.TradeReviewService;
import com.midai.pay.trade.vo.TradeReviewVo;

@Service
public class TradeReviewServiceImpl extends BaseServiceImpl<DealTotal> implements TradeReviewService {
	
	private Logger logger = LoggerFactory.getLogger(TradeReviewServiceImpl.class);
	
	private final TradeReviewMapper tradeReviewMapper;
	
	@Value("${mifu.oss.endpoint}")
	private String endpoint;
	
	@Value("${mifu.oss.bucket}")
	private String bucket;
	
	@Value("${big.img.pre}")
	private String bigImgPre;
	
	@Value("${sml.img.pre}")
	private String smImgPre;
	
	public TradeReviewServiceImpl(TradeReviewMapper mapper) {
		super(mapper);
		this.tradeReviewMapper = mapper;
	}
	
	@Autowired
	private PayOssService payOssService;
	
	@Autowired
	private BoEticketMapper boEticketMapper;
	
	@Autowired
	private MassageUtil massageUtil;
	
	@Autowired
	BoCustomerMapper boCustomerMapper;
	
	@Override
	public List<TradeReviewVo> findQuery(TradeReviewQuery query){
		List<TradeReviewVo> list = tradeReviewMapper.findQuery(query);
		
		for(TradeReviewVo vo : list){
			if(StringUtils.isEmpty(vo.getBigImgUrl())){	//未生成小票
				String url = this.composePic(vo.getHostTransSsn());
				vo.setSmallImgUrl(smImgPre + url);
				vo.setBigImgUrl(bigImgPre + url);
				
				//更新上传字段
				tradeReviewMapper.updateEticketPath(vo.getHostTransSsn(), url);
			}else{
				vo.setSmallImgUrl(smImgPre + vo.getBigImgUrl());
				vo.setBigImgUrl(bigImgPre + vo.getBigImgUrl());
			}
		}
		
		return list;
	}

	@Override
	public int findQueryCount(TradeReviewQuery query) {
		
		return tradeReviewMapper.findQueryCount(query);
	}
	
	public String createDeviceNo(String deviceNo){
		String tagDeviceNo = "";
		
		if(StringUtils.isNotBlank(deviceNo)){
			/**
			 * 1. 去除字符串中的所有字母
			 */
			Pattern pattern = Pattern.compile("[^0-9]");
			Matcher mat =pattern.matcher(deviceNo);
			
			String tag_1 = mat.replaceAll("").trim();
			
			/**
			 * 2. 如果长度超过8位, 则末尾截取; 如长度小于8位,则首位补0
			 */
			int length = tag_1.length();
			
			if(length >= 8){
				tagDeviceNo = tag_1.substring(length-8);
			}else{
				NumberFormat nf = NumberFormat.getInstance();
		        nf.setGroupingUsed(false);
		        nf.setMaximumIntegerDigits(8);
		        nf.setMinimumIntegerDigits(8);
		        
			    tagDeviceNo = nf.format(Double.valueOf(tag_1)); 
			}
		}
		
		return tagDeviceNo;
	}
	
	/**
	 * 生成小票
	 * @param hostTransSsn 流水号
	 * @return
	 * @throws IOException 
	 */
	public String composePic(String hostTransSsn){
		String imgPath = "";
		SaleeSlip cardInfo = tradeReviewMapper.findCardInfo(hostTransSsn);
		
		String deviceNoIn = createDeviceNo(cardInfo.getDeviceNoIn());
		
		//仅显示前后各4位: 1234********4321
		String transCardNo =  cardInfo.getTransCardNo();
		StringBuffer sb = new StringBuffer(transCardNo);
		sb.replace(4, sb.length()-4,"********");
		transCardNo = sb.toString();
		
		InputStream is = null, is2=null; 
		BufferedImage im, im2;
		Graphics2D g = null;
		
		try {
			is = new BufferedInputStream(new ClassPathResource("eticket.png").getInputStream()); //主图
			im = ImageIO.read(is);
			g = im.createGraphics();
			g.setColor(Color.BLACK);
			g.setFont(new Font("宋体",Font.BOLD,40));
			
			if(StringUtils.isNotEmpty(cardInfo.getSignPath())){ //签名图
				is2 = downLoadFromUrl("http://" + bucket + "." + endpoint + "/" + cardInfo.getSignPath());
				
				im2 = ImageIO.read(is2);
				g.drawImage(im2,300,1750,300,300,null);
			}
			g.drawString(StringUtils.isNotEmpty(cardInfo.getMchntName()) ? cardInfo.getMchntName() : "",500,486);//商户名称
			g.drawString(StringUtils.isNotEmpty(cardInfo.getMercNo()) ? cardInfo.getMercNo() : "",500,575);//商户编号
			g.drawString(StringUtils.isNotEmpty(deviceNoIn) ? deviceNoIn : "",500,695);//终端编号
			g.drawString(cardInfo.getOptUser(),500,815);//操作员号  
			g.drawString(transCardNo,500,915);//卡号
			g.drawString(cardInfo.getCardValid(),500,1050);//卡有效期
			g.drawString(cardInfo.getAccountName(),250,1165);//发卡行
			g.drawString(cardInfo.getBatchNum(),820,1165);//批次号
			g.drawString(cardInfo.getTransCodeName(),250,1285);//交易类型
			g.drawString(cardInfo.getHostTransSsn(),820,1285);//参考号
			g.drawString(cardInfo.getDeviceSsnOut(),250,1400);//凭证号
			g.drawString(cardInfo.getRespAuthId(),820,1400);//授权码
			g.drawString(cardInfo.getTransTime(),350,1520);//时间/日期
			g.drawString(" RMB: "+String.format("%1$,.2f", cardInfo.getTransAmt()),250,1645);//金额
			
			g.dispose();
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(im, "png", out);
			
			// oss上传文件
			//生成文件目录
			String dirPath = new SimpleDateFormat("YYYYMM").format(new Date());
			String allPath = "eticket/" + dirPath;

			//文件名称
			SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmss");
			String fileName = df.format(new Date()) + "_" + UUID.randomUUID().toString() + "_phone.png";
			
			imgPath = payOssService.imgUpload(new ByteArrayInputStream(out.toByteArray()), fileName, allPath);
			
		} catch (FileNotFoundException e) {
			logger.info("生成小票失败");
			e.printStackTrace();
		}catch (IOException e) {
			logger.info("生成小票失败");
			e.printStackTrace();
		}finally {
			try {
				is.close();
				if(is2!=null) is2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return imgPath;
	}

	@Override
	public int countReSign(String mobile) {
		return tradeReviewMapper.countReturnSign(mobile);
	}

	@Override
	public List<EticketSignVo> findAllReSign(String mobile) {
		if(StringUtils.isNotEmpty(mobile)){
			return tradeReviewMapper.findAllReSign(mobile.trim());
		}
		
		return null;
	}

	@Override
	@Transactional
	public boolean tickReSign(String logn, String mobile) {
		//更新小票重签状态
		int a = boEticketMapper.updateResign(logn.trim());
		//更新小票
		int b =tradeReviewMapper.updateEticketPath(logn.trim(), null);
		
		massageUtil.sendMsgByResource(PageEnum.jysh.toString(), boCustomerMapper.findInscodeByMobile(mobile));
		
		return ( a + b)>1;
	}
	
	/** 
     * 从网络Url中下载文件 
     * @param urlStr 
     * @param fileName 
     * @param savePath 
     * @throws IOException 
     */  
    public static InputStream  downLoadFromUrl(String urlStr){  
    	try{
    		URL url = new URL(urlStr);
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
	        //设置超时间为3秒  
	        conn.setConnectTimeout(10*1000);  
	        //防止屏蔽程序抓取而返回403错误  
	        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
	  
	        //得到输入流  
	        return conn.getInputStream();    
        }catch(IOException e){
        	e.printStackTrace();
        	throw new RuntimeException();
        }finally {
		}
    }  
  
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }
    
    /**
	 * 判断小票信息是否存在, 如不存在则新增
	 * 
	 * @param hostTransSsn 流水号
	 */
	public void eticketInfoExit(String hostTransSsn, String user){
		int num = boEticketMapper.countByHostTransSsn(hostTransSsn);
		
		if(0 == num){
			BoEticket eticket = new BoEticket();
			eticket.setHostTransSsn(hostTransSsn);
			eticket.setState(0);
			eticket.setCreateDate(new Date());
			eticket.setCreateUser(user);
			
			boEticketMapper.insertEticket(eticket);
		}
	}
	
	@Override
	public List<TradeReviewVo> channelEticketQuery(TradeReviewQuery query, String user) {
		/*
		 * 根据流水号, 查询所有小票信息. 如未生成则重新生成,并更新到表
		 */
		List<TradeReviewVo> voList = tradeReviewMapper.channelEticketQuery(query);
		
		for(TradeReviewVo vo : voList){
			this.eticketInfoExit(vo.getHostTransSsn(), user); 
			
			if(StringUtils.isEmpty(vo.getBigImgUrl())){	//未生成小票
				String url = this.composePic(vo.getHostTransSsn());
				vo.setSmallImgUrl(smImgPre + url);
				vo.setBigImgUrl(bigImgPre + url);
				
				//更新上传字段
				boEticketMapper.updateChannelPic(vo.getHostTransSsn(), url);
			}else{
				vo.setSmallImgUrl(smImgPre + vo.getBigImgUrl());
				vo.setBigImgUrl(bigImgPre + vo.getBigImgUrl());
			}
		}
		return voList;
	}
	@Override
	public int channelEticketCount(TradeReviewQuery query) {
		return tradeReviewMapper.channelEticketCount(query);
	}

	@Override
	public String eticketUrl(String logon) {
		int count = tradeReviewMapper.hostTransSsnExit(logon);
		
		if(count > 0 ){
			String eticketPath = tradeReviewMapper.findEticketPath(logon);
			
			if(StringUtil.isEmpty(eticketPath)){
				String url = this.composePic(logon);
				
				tradeReviewMapper.updateEticketPath(logon, url);
				
				return smImgPre + url;
			}else return smImgPre + eticketPath; 
		}
		return null;
	}
}

