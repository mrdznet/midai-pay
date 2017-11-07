package com.midai.reqbean;

import lombok.Data;

/**
 * Created by justin on 2017/6/28.
 */

@Data
public class AgentAuthReqBean {

    private String type;                 // 固定值：insData
    private String instCode;            // 所属收单机构
    private String mcc32;               // 收单机构32域
    private String traderAreaCode;        // 商户号地区编码
    private String mcc42;               // 42域MCC
    private String busLicenseName;      // 商户工商注册名称
    private String busName;             // 营业名称
    private String busLicense;          // 营业执照号码
    private String busScope;            // 经营范围
    private String mainbusiness;        // 主营业务
    private String legalName;           // 法人代表
    private String cerType;             //法人证件类型，默认01
    private String cerNum;               // 法人代表身份证号
    private String contactName;           // 法人代表名称
    private String contactPhone;        // 商户联系人电话
    private String addCountryCode;       // 商户注册地址国家代码：默认CHN
    private String addCoProvinceCode;     // 商户注册地址省代码
    private String addCoCityCode;        // 商户注册地址市代码
    private String addCoAreaCode;        // 商户注册地址区县代码
    private String reqAddress;          // 商户注册地址
    private String settleWay;           // 商户结算途径，默认1
    private String settleCycle;          // 商户结算周期，默认0
    private String chargeType;           // 商户计费类型，默认00
    private String chargingGrade;        // 商户计费档次，默认0
    private String accountName;          // 结算账户名称
    private String bankNum;              // 结算账户帐号
    private String bankCodeThreeCode;      // 结算账户开户行代码
    private String bankName;             // 结算账户开户行名称
    private String bankCode;              // 结算账户开户行支付系统行号
    private String holidaysSettle;         // 节假日合并结算

    private String wxRfeeType;             //手续费收取方式，默认0
    private String wxRrate;               //手续费收取额度
    private String wxRtop;                //手续费额度封顶
    private String wxRrateMin;            //手续费额度保底

    private String zfbRfeeType;             //手续费收取方式，默认0
    private String zfbRrate;               //手续费收取额度
    private String zfbRtop;                //手续费额度封顶
    private String zfbRrateMin;            //手续费额度保底

}
