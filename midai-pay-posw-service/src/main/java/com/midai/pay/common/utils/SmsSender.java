package com.midai.pay.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.midai.pay.mobile.utils.SmsSenderProperties;
import com.midai.pay.mobile.utils.ZUtil;

public class SmsSender {
	
	private Logger logger = LoggerFactory.getLogger(SmsSender.class);
	
	private final SmsSenderProperties smsSenderProperties;
	
	private final CCPRestSDK ccpRestSDK;
	
	public SmsSender(SmsSenderProperties smsSenderProperties, CCPRestSDK ccpRestSDK){
		ccpRestSDK.init(smsSenderProperties.getUrl(), smsSenderProperties.getPort());
		ccpRestSDK.setAccount(smsSenderProperties.getAccountSid(), smsSenderProperties.getAccountToken());
		ccpRestSDK.setAppId(smsSenderProperties.getAppId());
		
		this.smsSenderProperties = smsSenderProperties;
		this.ccpRestSDK = ccpRestSDK;
	}
	
    /*private static final CCPRestSDK restAPI = new CCPRestSDK();
    
    private static String url;// 请求地址
    private static String port;// 请求端口
    private static String accountSid;// 主账号
    private static String accountToken; // 主账号token
    private static String appId; // 应用ID
    private static String callCenter;// 客服中心号
    private static String isSwitch;
    private static void loadProperties(){
        try {
            InputStream instream = SmsSender.class.getClassLoader().getResourceAsStream("application.properties");
            Properties pp = null;
            pp = new Properties();
            pp.load(instream);

            url = pp.getProperty("mifu.sms.url");
            port = pp.getProperty("mifu.sms.port");
            accountSid = pp.getProperty("mifu.sms.accountSid");
            accountToken = pp.getProperty("mifu.sms.accountToken");
            appId = pp.getProperty("mifu.sms.appId");
            callCenter = pp.getProperty("mifu.sms.callCenter");
            isSwitch = pp.getProperty("mifu.sms.switch");
            
            restAPI.init(url,port);
            restAPI.setAccount(accountSid, accountToken);// 初始化主帐号和主帐号TOKEN
            restAPI.setAppId(appId);
        }catch (FileNotFoundException fne){
            System.out.println("sms-config file not find:"+ fne.getMessage());
        }catch (Exception e){
            System.out.println("load conf-properties err:"+e.getMessage());
        }
    }
    
    static {
        loadProperties();
    }
    
    public static void reloadProperties(){
        loadProperties();
    }*/
    
    /**
     * 发送验证码类型短信
     * @param phoneNumber 手机号
     * @param roundCode 验证码
     * @param verifyModeId 短信模板ID
     * @return statusCode = 000000位发送成功
     */
    public HashMap<String, Object>  sendVerifyCode(String phoneNumber,String roundCode,String verifyModeId){
    	if(StringUtils.isBlank(smsSenderProperties.getUrl())){
            logger.info("sms-config-file reload");
        }
    	
        return ccpRestSDK.sendTemplateSMS(phoneNumber.trim(),verifyModeId ,new String[]{ZUtil.getDTimes(),roundCode,"5", smsSenderProperties.getCallCenter()});
    }

    /**
     *
     * @param phoneNumber 手机号
     * @param params 参数
     * @param noticeModeId 短信通知模板ID
     * @return statusCode = 000000位发送成功
     */
    public HashMap<String, Object> sendNotice(String phoneNumber,ArrayList<String> params,String noticeModeId){
		if(StringUtils.isBlank(smsSenderProperties.getUrl())){
			logger.info("sms-config-file reload");
		}
		
		if(params == null){
			params = new ArrayList<String>(1);
		}
		params.add(smsSenderProperties.getCallCenter());
		String [] data=  params.toArray(new String[params.size()]);
		
		return ccpRestSDK.sendTemplateSMS(phoneNumber.trim(),noticeModeId ,data);
    }

    public static String getRoundCode(){
        StringBuilder sb =new StringBuilder();
        Random random = new Random();
        int codeLength = 6;
        for ( int i = 1; i <= codeLength; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}