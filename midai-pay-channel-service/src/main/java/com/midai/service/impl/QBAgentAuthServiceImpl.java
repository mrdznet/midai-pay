package com.midai.service.impl;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midai.constants.QBConstants;
import com.midai.encry.MrAzuEncryptionUtil;
import com.midai.reqbean.AgentAuthReqBean;
import com.midai.resbean.AgentAuthResBean;
import com.midai.service.AgentAuthService;
import com.midai.utils.GsonUtils;
import com.midai.utils.HttpClientUtil;
import com.midai.utils.PropUtil;

import net.sf.json.JSONObject;

/**
 * Created by justin on 2017/6/28.
 */
@SuppressWarnings("Duplicates")

public class QBAgentAuthServiceImpl implements AgentAuthService {

    private static final Logger LOGGER= LoggerFactory.getLogger(QBAgentAuthServiceImpl.class);

    private Properties properties;
    
    public QBAgentAuthServiceImpl() {
    	 if(properties == null) {
     		properties = PropUtil.getProperties("channel-config.properties");
     	}
    }
    
    @Override
    public AgentAuthResBean applyForAgent(AgentAuthReqBean bean) {


        try {
            String url_token = properties.getProperty("qb.customer.url-token");
            String url_openApi = properties.getProperty("qb.customer.url-open");

            Map<String, String> condition = new HashMap<String, String>();
            condition.put("inscode", properties.getProperty("qb.inst-code"));  //分配的代理商编号
            condition.put("rep", "start");
            DefaultHttpClient httpclient = new DefaultHttpClient();
            String result = HttpClientUtil.post(httpclient, url_token, condition); //获取token
            System.out.println(result);
            condition.clear();
            String key = result.trim().split(":")[1];
            condition.put("token", key);

            JSONObject json = new JSONObject();
            json.put("type", bean.getType());//报件固定值                             // 固定值：insData
            json.put("instCode", properties.getProperty("qb.inst-code"));// 分配的代理商编号              // 所属收单机构
            json.put("mcc32", bean.getMcc32());                                      // 收单机构32域
            json.put("traderAreaCode", bean.getTraderAreaCode());                     // 商户号地区编码
            json.put("mcc42", bean.getMcc42());                                       // 42域MCC
            json.put("busLicenseName", bean.getBusLicenseName());                        // 商户工商注册名称
            json.put("busName", bean.getBusName());                                      // 营业名称
            json.put("busLicense", bean.getBusLicense());                               // 营业执照号码
            json.put("busScope", bean.getBusScope());                                    // 经营范围
            json.put("mainbusiness", bean.getMainbusiness());                            // 主营业务
            json.put("legalName", bean.getLegalName());                                   // 法人代表
            json.put("cerType", bean.getCerType());// 固定值，仅支持身份证号               // 法人证件类型，默认01
            json.put("cerNum", bean.getCerNum());                                       // 法人代表身份证号
            json.put("contactName", bean.getContactName());                             // 法人代表名称
            json.put("contactPhone", bean.getContactPhone());                              // 商户联系人电话
            json.put("addCountryCode", bean.getAddCountryCode());                           // 商户注册地址国家代码：默认CHN
            json.put("addCoProvinceCode", bean.getAddCoProvinceCode());                     // 商户注册地址省代码
            json.put("addCoCityCode", bean.getAddCoCityCode());                         // 商户注册地址市代码
            json.put("addCoAreaCode", bean.getAddCoAreaCode());                         // 商户注册地址区县代码
            json.put("reqAddress", bean.getReqAddress());                                // 商户注册地址
            json.put("settleWay", bean.getSettleWay());// 固定值                           // 商户结算途径，默认1
            json.put("settleCycle", bean.getSettleCycle());// 固定值                         // 商户结算周期，默认0
            json.put("chargeType", bean.getChargeType());// 固定值                            // 商户计费类型，默认00
            json.put("chargingGrade", bean.getChargingGrade());// 固定值                       // 商户计费档次，默认0
            json.put("accountName", bean.getAccountName());                                 // 结算账户名称
            json.put("bankNum", bean.getBankNum());                    // 结算账户帐号
            json.put("bankCodeThreeCode", bean.getBankCodeThreeCode());                         // 结算账户开户行代码
            json.put("bankName", bean.getBankName());                                       // 结算账户开户行名称
            json.put("bankCode", bean.getBankCode());                                     // 结算账户开户行支付系统行号
            json.put("holidaysSettle", bean.getHolidaysSettle());// 固定值                      // 节假日合并结算

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
            temp.put("rbusinessCode", "0000005");     //业务码   微信
            temp.put("rrateTypeCode", "0004001");     //费率类型编码
            temp.put("rfeeType", bean.getWxRfeeType());                //手续费收取方式，默认0
            temp.put("rrate", bean.getWxRrate());               //手续费收取额度
            temp.put("rtop", bean.getWxRtop());                    //手续费额度封顶
            temp.put("rrateMin", bean.getWxRrateMin());                //手续费额度保底
            temps = new String[2];
            temps[0] = temp.toString();


            temp = new JSONObject();
            temp.put("rproductCode", "1171");
            temp.put("rbusinessCode", "0000006");  // 支付宝
            temp.put("rrateTypeCode", "0004002");
            temp.put("rfeeType", bean.getZfbRfeeType());
            temp.put("rrate", bean.getZfbRrate());
            temp.put("rtop", bean.getZfbRtop());
            temp.put("rrateMin", bean.getZfbRrateMin());
            temps[1] = temp.toString();
            json.put("traderbusinessRate", temps);
            //*****************************************************************
            try {
//                System.out.println("报备JSON数据：" + json.toString());
                String sync = MrAzuEncryptionUtil.encodeMD5(URLEncoder.encode(json.toString(), "UTF-8"));//MD5完整性验证加密
                json.put("sync", sync);
//                System.out.println("MD5完整性验证加密sync：" + sync);
                condition.put("data", MrAzuEncryptionUtil.getThreeEncry(URLEncoder.encode(json.toString(), "UTF-8")));//对最后的参数进行三重加密
//                System.out.println("报备DATA数据：" + condition.get("data"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            LOGGER.info(json.toString());
            result = HttpClientUtil.post(httpclient, url_openApi, condition);
            String decode = URLDecoder.decode(result, "UTF-8");
//            System.out.println(decode);

            LOGGER.info(decode);
            condition.clear();
            condition.put("rep", "end");
            condition.put("inscode", properties.getProperty("qb.inst-code"));//此处填写的是代理商编号
            String resulta = HttpClientUtil.post(httpclient, url_token, condition);//销毁token
            System.out.println(resulta);
//            JSONObject jsonObject = JSONObject.fromObject(decode);
//            String data = jsonObject.getString("data");
//{"data":"{"status":"false","type":"insData","msg":"商户报件：商户手机号重复"}"} 非标准json
            String data = decode.substring(9, decode.length() - 2);
            return GsonUtils.fromJson(data, AgentAuthResBean.class);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return new AgentAuthResBean("false", "insData", "数据解析失败");


    }


}
