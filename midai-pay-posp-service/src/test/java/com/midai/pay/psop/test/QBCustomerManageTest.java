package com.midai.pay.psop.test;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;

import com.midai.pay.quick.qb.common.encry.MrAzuEncryptionUtil;
import com.midai.pay.quick.qb.common.util.HttpClientUtil;

import net.sf.json.JSONObject;

/**
 * @author wrt
 * @date 2016年8月17日上午11:13:59
 * @description 钱宝商户报备方法
 */
@SuppressWarnings("deprecation")
public class QBCustomerManageTest {
	
	public static void main(String[] args) throws Exception {
		
		String url_token = "http://60.205.113.140:88/liquidation/appDataToken";
		String url_openApi = "http://60.205.113.140:88/liquidation/openDataApi";
		
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("inscode", "84811000001");  //分配的代理商编号
		condition.put("rep", "start");
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String result = HttpClientUtil.post(httpclient, url_token, condition); //获取token
		System.out.println(result);
		condition.clear();
		String key = result.trim().split(":")[1];
		condition.put("token", key);
		
		JSONObject json = new JSONObject();
		json.put("type", "insData");//报件固定值                     // 固定值：insData
		json.put("instCode", "84811000001");// 分配的代理商编号        // 所属收单机构
        json.put("mcc32", "85603320");                              // 收单机构32域
        json.put("traderAreaCode", "0330000330200");                // 商户号地区编码
        json.put("mcc42", "5411");                                  // 42域MCC
        json.put("busLicenseName", "大象轮");                        // 商户工商注册名称
        json.put("busName", "大象轮");                               // 营业名称
        json.put("busLicense", "3302060574435820");                 // 营业执照号码
        json.put("busScope", "大轮胎、小轮胎");                       // 经营范围
        json.put("mainbusiness", "轮胎");                            // 主营业务
        json.put("legalName", "刘备");                               // 法人代表
        json.put("cerType", "01");// 固定值，仅支持身份证号             // 法人证件类型，默认01
        json.put("cerNum", "110101024303060516");                   // 法人代表身份证号
        json.put("contactName", "刘备");                             // 法人代表名称
        json.put("contactPhone", "18000000004");                     // 商户联系人电话
        json.put("addCountryCode", "CHN");                           // 商户注册地址国家代码：默认CHN
        json.put("addCoProvinceCode", "330000");                     // 商户注册地址省代码
        json.put("addCoCityCode", "330200");                         // 商户注册地址市代码
        json.put("addCoAreaCode", "330211");                         // 商户注册地址区县代码
        json.put("reqAddress", "四川成都城皇宫");                      // 商户注册地址
        json.put("settleWay", "1");// 固定值                           // 商户结算途径，默认1
        json.put("settleCycle", "0");// 固定值                         // 商户结算周期，默认0
        json.put("chargeType", "00");// 固定值                         // 商户计费类型，默认00
        json.put("chargingGrade", "0");// 固定值                       // 商户计费档次，默认0
        json.put("accountName", "刘备");                              // 结算账户名称
        json.put("bankNum", "620001000000003001");                    // 结算账户帐号
        json.put("bankCodeThreeCode", "307");                         // 结算账户开户行代码
        json.put("bankName", "三国银行");                              // 结算账户开户行名称
        json.put("bankCode", "307305027511");                         // 结算账户开户行支付系统行号
        json.put("holidaysSettle", "是");// 固定值                      // 节假日合并结算

        //*******拼接业务权限，有几个拼接几个**********************************************************
        String[] temps = new String[2];//数组长度是开通业务权限的数量
        JSONObject temp = new JSONObject();
        temp.put("bproductCode", "1171");         // 产品码
        temp.put("bbusinessCode", "0000005");     // 业务码
        temp.put("btranCode", "7131");            // 开通交易码
        temps[0]= temp.toString();

        temp = new JSONObject();
        temp.put("bproductCode", "1171");
        temp.put("bbusinessCode", "0000006");
        temp.put("btranCode", "7131");
        temps[1] = temp.toString();

        json.put("traderbusinessAuth", temps);
        //*****************************************************************

        //************** 拼接业务费率  ***************************************************
        temp = new JSONObject();
        temp.put("rproductCode", "1171");         //产品码
        temp.put("rbusinessCode", "0000005");     //业务码
        temp.put("rrateTypeCode", "0004001");     //费率类型编码
        temp.put("rfeeType", "0");                //手续费收取方式，默认0
        temp.put("rrate", "0.006");               //手续费收取额度
        temp.put("rtop", "0");                    //手续费额度封顶
        temp.put("rrateMin", "0");                //手续费额度保底
        temps = new String[2];
        temps[0] = temp.toString();



        temp = new JSONObject();
        temp.put("rproductCode", "1171");
        temp.put("rbusinessCode", "0000006");
        temp.put("rrateTypeCode", "0004002");
        temp.put("rfeeType", "0");
        temp.put("rrate", "0.006");
        temp.put("rtop", "0");
        temp.put("rrateMin", "0");
        temps[1] = temp.toString();
        json.put("traderbusinessRate", temps);
        //*****************************************************************
        try {
            System.out.println("报备JSON数据：" + json.toString());
            String sync = MrAzuEncryptionUtil.encodeMD5(URLEncoder.encode(json.toString(), "UTF-8"));//MD5完整性验证加密
            json.put("sync", sync);
            System.out.println("MD5完整性验证加密sync：" + sync);
            condition.put("data", MrAzuEncryptionUtil.getThreeEncry(URLEncoder.encode(json.toString(), "UTF-8")));//对最后的参数进行三重加密
            System.out.println("报备DATA数据：" + condition.get("data"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = HttpClientUtil.post(httpclient, url_openApi, condition);
        try {
            System.out.println(URLDecoder.decode(result, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        condition.clear();
        condition.put("rep", "end");
        condition.put("inscode", "84811000001");//此处填写的是代理商编号
        String resulta = HttpClientUtil.post(httpclient, url_token, condition);//销毁token
        System.out.println(resulta);
    }
}
