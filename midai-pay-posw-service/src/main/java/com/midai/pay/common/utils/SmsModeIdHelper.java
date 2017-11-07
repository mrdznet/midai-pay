package com.midai.pay.common.utils;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

public class SmsModeIdHelper {
	private static HashMap<Integer,String> map =new HashMap<Integer, String>(8);

    /**
     * 获取短信模板ID
     * @param type 短信业务类型
     * @return
     */
    public static String getSmsModeId(Integer type){
        String modeId = map.get(type);
        if(StringUtils.isEmpty(modeId)){
            throw new SmsTypeErrException("短信业务类型错误");
        }
        return modeId;
    }


    static {
        map.put(SmsType.REGISTER,"73435");
        map.put(SmsType.RETRIEVE_PWD,"73435");
        map.put(SmsType.PAYMENT_FAILURE,"72634");
        map.put(SmsType.ATTESTATION_SUCCESS,"71869");
        map.put(SmsType.ATTESTATION_FAILURE,"71868");
        map.put(SmsType.TRADE_AUDIT_FAILURE,"69934");
        map.put(SmsType.TRADE_DRAW_FAILURE,"72633");
        map.put(SmsType.TRADE_TICKET_NOTICE,"72646");
        map.put(SmsType.AGENT_REG_SUCCESS,"86420");
        map.put(SmsType.TICKET_ERROR,"90442");
        
        //gy add
        map.put(SmsType.CARD_INFO_EDIT_FAILURE,"112943");
        map.put(SmsType.CARD_INFO_EDIT_SUCCESS, "112942");
        
    }



    public static class SmsType{

        /** 注册--验证码类 */
        public static final Integer REGISTER =1;

        /** 忘记密码 */
        public static final Integer RETRIEVE_PWD = 2;

        /** 支付失败通知 */
        public static final Integer PAYMENT_FAILURE =3 ;

        /** 实名认证通过 */
        public static final Integer ATTESTATION_SUCCESS =4 ;

        /** 实名认证失败 */
        public static final Integer ATTESTATION_FAILURE =5 ;

        /** 交易审核拒绝 */
        public static final Integer TRADE_AUDIT_FAILURE =6;

        /** 快速提款拒绝 */
        public static final Integer TRADE_DRAW_FAILURE =7;

        /** 交易小票通知 */
        public static final Integer TRADE_TICKET_NOTICE = 8;
        
        /** 代理商审核通过通知 */
        public static final Integer AGENT_REG_SUCCESS = 9;
        
        /**
         *小票审核拒绝
         */
        public static final Integer TICKET_ERROR = 10;
        
        
        /**
         * gy add
         * 结算信息（卡）修改审核通过
         */
        public static final Integer CARD_INFO_EDIT_SUCCESS=11;
        
        /**
         * gy add
         * 结算信息（卡）修改审核未通过
         */
        public static final Integer CARD_INFO_EDIT_FAILURE=12;
    }

}