package com.midai.pay.app.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.midai.pay.app.aes.AES2;
import com.midai.pay.app.aes.CryptUtils;
import com.midai.pay.app.vo.CurrentDateVo;
import com.midai.pay.customer.service.MobileDeviceService;
import com.midai.pay.handpay.service.JiDian4AuthService;
import com.midai.pay.handpay.vo.AuthBean;
import com.midai.pay.handpay.vo.CustomerAuthBean;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.service.FreezeMoneyService;
import com.midai.pay.mobile.service.MobileCCBDService;
import com.midai.pay.mobile.service.MobileHislogService;
import com.midai.pay.mobile.service.MobileOSSService;
import com.midai.pay.mobile.service.MobilePaymentService;
import com.midai.pay.mobile.service.MobileRateQuotaService;
import com.midai.pay.mobile.service.MobileSMSCodeService;
import com.midai.pay.mobile.service.MobileSuggestionService;
import com.midai.pay.mobile.service.MobileTicketReplaceService;
import com.midai.pay.mobile.service.MobileTradeTicketService;
import com.midai.pay.mobile.service.MobileTransAccountService;
import com.midai.pay.mobile.service.MobileUserAuthService;
import com.midai.pay.mobile.service.MobileUserService;
import com.midai.pay.mobile.service.MobileUserUpdateCardService;
import com.midai.pay.mobile.service.MobileVersionService;
import com.midai.pay.mobile.service.WeChatPayService;
import com.midai.pay.posp.entity.ConSumptionEntity;
import com.midai.pay.posp.entity.SignParam;
import com.midai.pay.posp.service.PospService;

/**
 * Created with midai-pay-parent User: midai Date: 2016/10/17 Time: 9:43
 */
@Controller
public class PayController {

    private static final String PARAM = "requestParam";

    private Logger logger = LoggerFactory.getLogger(PayController.class);

	@Reference
	private MobileUserService mobileUser;
	@Reference
	private MobileSMSCodeService mobileSMSCode;
	@Reference
	private MobileUserAuthService mobileUserAuth;
	@Reference
	private MobileTicketReplaceService mobileTicketReplace;
	@Reference
	private MobileOSSService mobileOSS;
    @Reference
    private PospService pospService;
    @Reference
    private MobileDeviceService mobileDevice;
    @Reference
    private MobileCCBDService mobileCCBD;
    @Reference
    private MobileVersionService mobileVersionCheck;
    @Reference
    private MobileHislogService mobileHislog;
    @Reference
    private FreezeMoneyService freezeMoney;
    @Reference
    private WeChatPayService weChatPay;
    @Reference
    private MobileSuggestionService mobileSuggestion;
    @Reference
    private MobileTransAccountService mobileTransAccount;
    @Reference
    private MobileRateQuotaService mobileRateQuota;
    @Reference
    private MobilePaymentService mobilePayment;
    @Reference
    private MobileUserUpdateCardService mobileUserUpdateCard;
    @Reference
    private MobileTradeTicketService mobileTradeTicket;
    @Reference
    private JiDian4AuthService jiDian4AuthService;

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<String> index(@RequestParam(PARAM) String requestParam) throws Exception {
       
    	
    	Assert.notNull(requestParam, "参数requestParam的值不存在");
        // 获取接口码
        String code = requestParam.substring(0, 6);
        // 获取参数
        String json = requestParam.substring(6);
        // 解密参数
        logger.info("未解密参数:"+requestParam);
        String value = AES2.decrypt(json, CryptUtils.encryptToMD5(CryptUtils.AES_Password));
        logger.info("接口码: " + code + "---参数值:" + value);
        // 返回值
        String returnValue = null;
        Object obj = null;
        Gson gson = new Gson();
        try {
            switch (code) {
            case "199001":// 手机注册
                obj = mobileUser.registerExecute(value);
                break;
            case "399001"://海贝手机注册步骤一
            	obj = mobileUser.registerHaiBeiExecuteOne(value);
			    break;
            case "399002"://海贝手机注册步骤二
            	obj= mobileUser.registerHaiBeiExecuteTwo(value);
			     break;
            case "199102":// 手机登录
            case "399102":// 海贝手机登录
                obj = mobileUser.login(value, code);
                break;
            case "199003"://修改密码
            	obj = mobileUser.modifyPassword (value);
		        break;
            case "199018":// 获取验证码
            	obj = mobileSMSCode.execute(value, code);
                break;
            case "399018":// 海贝获取验证码
                obj = mobileSMSCode.execute(value, code);
                break;
            case "199014"://意见反馈
            	obj= mobileSuggestion.submitNotice(value);
		         break;
            case "199725"://实名认证
            case "300003"://海贝实名认证
				 obj= mobileUserAuth.authentication(value, code);
		         break;
            case "199726"://实名认证完成提交
            case "300004"://海贝实名认证完成提交
            	obj = mobileUserAuth.authSubmitCompleted(value, code);
		    	 break;
            case "199143"://获取设备类型
            case "399143"://海贝获取设备类型
            	obj = mobileCCBD.getDeviceModes(value, code);
		         break;
            case "199031"://获取省份
            	obj= mobileCCBD.getProvinces();
		    	 break;
            case "199032"://根据省份获取市
            	obj= mobileCCBD.getCitys(value);
		         break;
            case "199033"://根据市份获取区县
            	obj= mobileCCBD.getAreas(value);
		         break;
            case "199035"://获取总行
            	obj =  mobileCCBD.getBigBanks();
		         break;
            case "199022"://获取手机商户基本信息
            case "300002"://海贝获取手机商户基本信息
            	obj =  mobileUserAuth.getAuthSimpleInfo(value, code);
		         break;
            case "199015": //版本更新检测
		    	 obj = mobileVersionCheck.checkMobileVersion(value);
		    	 break;
            case "199016": //绑定多设备，和更改首选项 
            	obj = mobileDevice.addDevice(value);
		    	 break;
            case "199017": //终端解绑
            	obj = mobileDevice.mulDevice(value);
		    	 break;
            case "199070": //冻结金额
            	obj = freezeMoney.freeze(value);
		    	 break;
            case "199079"://小票
            	obj=  mobileTradeTicket.executeRT(value);
		         break;
            case "199029"://小票重签查询
            	obj = mobileTicketReplace.excuteQuery(value);
		    	 break;
            case "199019"://短信获取小票
            case "399019"://海贝短信获取小票
            	obj = mobileSMSCode.ticket(value, code);
		         break;
            case "199030"://小票重签更改状态
            	obj=  mobileTicketReplace.excute(value);
		         break;
            case "199008": //流水查询
            case "399008": //海贝流水查询
            	 obj = mobileHislog.searchHislog(value, code);
		    	 break;
            case "199066": //流水明细查询
            case "399066": //海贝流水明细查询
		     	 obj = mobileHislog.searchHisDetail(value, code);
		     	 break; 
            case "199036"://获取手机商户实名认证信息
            	obj =  mobileUserAuth.getAuthUserInfor(value, code);
		         break;
            case "199037":  //下一步
            	obj=  mobileUserAuth.checkFirstStep(value);
		         break;
            case "199822"://获取上传图片凭证
            	obj = mobileOSS.getOSSDocument(value);
		         break;
            case "170000": // 签到
                SignParam param = gson.fromJson(value, SignParam.class);
                obj = pospService.sgin(param);
                break;
            case "180001": // 刷卡交易
                ConSumptionEntity consumptionEntity = gson.fromJson(value, ConSumptionEntity.class);
                obj = pospService.consumption(consumptionEntity);
                break;
            case "310012"://海贝商户修改手机号
            	obj = mobileUser.updatePhone(value);
		    	break;
            case "399003": //海贝到账记录查询
            	obj = mobileTransAccount.queryTransAccount(value,code);
		    	break;
            case "399005": //海贝到账记录查询(微信、支付宝)
            	obj = mobileTransAccount.queryTransAccount(value,code);
            	break;
            case "399006": //海贝到账记录查询(全部)
            	obj = mobileTransAccount.queryTransAccount(value,code);
            	break;
            case "399004": //海贝费率限额
            	obj = mobileRateQuota.mchntRateQuota(value);
		    	break;
            case "310011"://海贝获取支付方式
            	obj = mobilePayment.excute(value);
		        break;
            case "310010"://海贝修改商户收款信息申请
            	obj = mobileUserUpdateCard.execute(value);
		    	 break; 
            case "399009"://海贝小票
            	obj =  mobileTradeTicket.execute(value);
           /* case "210002"://畅捷签名
            	obj = weChatPay.changJieSign(value); 
		          break;
            case "210001"://畅捷扫码
            	obj = weChatPay.getCodeImage(value); 
		          break;
            case "210003"://畅捷查询
            	obj = weChatPay.executeQuery(value); 
		          break;*/
            case "999999" :
            	 SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
            	 CurrentDateVo vo=new CurrentDateVo();
                 vo.setDATE(sdf.format(new Date()));
                 vo.setRSPCOD("00");
                 vo.setRSPMSG("当前时间");
                 obj=vo;
            	break;
            case "200000": //四要素认证
            	AuthBean auth = gson.fromJson(value, AuthBean.class);
            	obj = jiDian4AuthService.mobileAuth4(auth);
            	break;
            case "200001": //判断是否已经四要素认证
            	CustomerAuthBean authBean = gson.fromJson(value, CustomerAuthBean.class);
            	obj = jiDian4AuthService.ifAlreadyAuth(authBean);
            	break;
            case "400000"://商户所属行业
            	obj= mobileCCBD.getIndustry();
		    	 break;
            case "400001"://商户类别
            	obj= mobileCCBD.getCategory(value);
		         break;	
            	
            default:
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            AppBaseEntity entity = new AppBaseEntity();
            entity.setRSPCOD("99");
            entity.setRSPMSG("数据异常，请检查！");
            obj = entity;
        }

        returnValue = gson.toJson(obj);
        logger.info("响应值:" + returnValue);
        // 加密响应值
        returnValue = AES2.encrypt(returnValue, CryptUtils.encryptToMD5(CryptUtils.AES_Password));
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(returnValue, HttpStatus.OK);
        return responseEntity;

    }

//    public static void main(String[] args) {
//
//        Gson gson = new Gson();
//        UserRegisterEntity u = new UserRegisterEntity();
//      TestQuickPay u = new TestQuickPay();
//        u.setPHONENUMBER("13918513474");
////        u.setIDCARD("150404198508302613");
////        u.setACCOUNTNO("872483776213424343");
////        u.setNEWPHONENUMBER("13838389438");
////        u.setPASSWORD("123456");
//        u.setPageNow("1");
////        u.setMONTHTTYPE("");
//        u.setLINENUM("10");
//        u.setQUERYDATE("2017-08-01");
//        u.setMONTHTYPE("2");
//        u.setTRANSCHANNEL("1");
////        u.setCONTENT("阿萨斯分");
    
////        u.setSign_type("M5");
////        u.setACCOUNTNAME("吴厚东");
////        u.setACCOUNTNO("6228480405390474576");
////        u.setMERTYPE("1");
////        u.setDEVICENO("1235678909998501");
////        u.setIp("127.0.0.1");;
////        u.setOs("Android");
////        u.setVersion("2.0.0");
////        u.setLOGNO("000000000034");
////        u.setPARCOD("");
////        u.setPHONENUMBER("17605696589");
////        u.setPASSWORD("123456");
////        
////        u.setCPASSWORD("123456");
////        u.setMSGCODE("814254");
////        u.setEXTEND1("13812345678");
//        u.setTYPE("1");
////        u.setIP("127.0.0.1");
////        u.setTRANSNO("000000000022");
////        u.setTRANSDATE("1027");
////        u.setPARENTDIR("pay");
////        u.setINSCODE("");
////        u.setNEWPASSWORD("123456");
////        u.setOLDPASSWORD("123");
////        u.setMSGCODE("814254");
////        u.setURL1("http://test1.com");
////        u.setURL2("http://test1.com");
////        u.setURL3("http://test1.com");
////        u.setURL4("http://test1.com");
////        u.setURL5("http://test1.com");
////        u.setURL6("http://test1.com");
////        u.setTYPE("1");
////        u.setPASSWORD("123456");
////        u.setIP("839814");
////        u.setCPASSWORD("123456");
////        u.setMSGCODE("839814");
////        u.setTYPE("1");
////        u.setUSERNAME("我是测试");
////        u.setIDNUMBER("342401199308078891");
////        u.setMERNAME("我是测试");
////        u.setSCOBUS("食品类批发零售");
////       u.setMERADDRESS("上海市浦东新区");
////        u.setACCOUNTNAME("账户测试");
////        u.setACCOUNTNO("4563516206013716082");
////        u.setPROVINCEID("310000");
////        u.setCITYID("150400");
////        u.setBANKID("301290000007");
////        u.setBRANCHBANKNAME("中国银行上海市浦东新区分行");
////        u.setTERMNO("cd07000400000916");
////        u.setMERAUTO("1");
////        u.setCompanyNo("101003");
//        CustomerAuthBean u = new CustomerAuthBean();
//        u.setBankCard("6227612145830440");
//        u.setLoginTel("17684768508");
//        AuthBean u = new AuthBean();
//        u.setAccNo("6259588692596504");
//        u.setCertifId("341226198705102374");
//        u.setCustomerNm("黄岩");
//        u.setLoginPhone("13162044163");
//        u.setPhoneNo("13162044163");
//        u.setMscode("846605");
//        String returnValue = gson.toJson(u);
//        // 加密响应值
//        returnValue = AES2.encrypt(returnValue, CryptUtils.encryptToMD5(CryptUtils.AES_Password));
//        //399102
//        System.out.println("399006" + returnValue);
//    }
    
    @GetMapping("/check")
    @ResponseBody
    public String check(){
    	return "OK";
    }
    
}
