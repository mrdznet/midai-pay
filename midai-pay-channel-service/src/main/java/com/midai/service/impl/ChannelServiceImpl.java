//package com.midai.service.impl;
//
//import com.midai.encry.MrAzuEncryptionUtil;
//import com.midai.reqbean.AgentAuthReqBean;
//import com.midai.reqbean.QRCodePayReqBean;
//import com.midai.reqbean.QRCodeSignReqBean;
//import com.midai.reqbean.ReqBean;
//import com.midai.resbean.AgentAuthResBean;
//import com.midai.resbean.QRCodePayResBean;
//import com.midai.resbean.QRCodeSignResBean;
//import com.midai.resbean.ResBean;
//import com.midai.service.AgentAuthService;
//import com.midai.service.ChannelService;
//import com.midai.service.QRCodePayService;
//import com.midai.utils.GsonUtils;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//
///**
// * Created by justin on 2017/6/29.
// */
//public class ChannelServiceImpl implements ChannelService {
//
//
//    private AgentAuthService agentAuthService = new QBAgentAuthServiceImpl();
//
//    private QRCodePayService qrCodeService = new QRCodePayServiceImpl();
//
//    @Override
//    public ResBean applyForAgent(ReqBean reqBean) {
//
//        try {
//
//            String deThreeEncry = URLDecoder.decode(MrAzuEncryptionUtil.getDeThreeEncry(reqBean.getData()), "UTF-8");//对最后的参数进行三重加密
//
//            AgentAuthReqBean agentAuthReqBean = GsonUtils.fromJson(deThreeEncry, AgentAuthReqBean.class);
//            AgentAuthResBean agentAuthResBean = agentAuthService.applyForAgent(agentAuthReqBean);
//
//            return new ResBean(URLEncoder.encode(GsonUtils.toJson(agentAuthResBean), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            try {
//                return new ResBean(URLEncoder.encode("数据解析失败", "UTF-8"));
//            } catch (UnsupportedEncodingException e1) {
//                e1.printStackTrace();
//            }
//
//        }
//        return null;
//    }
//
//    @Override
//    public ResBean getQRCode(ReqBean reqBean) {
//        try {
//            String deThreeEncry = URLDecoder.decode(MrAzuEncryptionUtil.getDeThreeEncry(reqBean.getData()), "UTF-8");//对最后的参数进行三重加密
//
//            QRCodePayReqBean qrCodePayReqBean = GsonUtils.fromJson(deThreeEncry, QRCodePayReqBean.class);
//            QRCodePayResBean agentAuthResBean = qrCodeService.getQRCode(qrCodePayReqBean);
//
//            return new ResBean(URLEncoder.encode(GsonUtils.toJson(agentAuthResBean), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            try {
//                return new ResBean(URLEncoder.encode("数据解析失败", "UTF-8"));
//            } catch (UnsupportedEncodingException e1) {
//                e1.printStackTrace();
//            }
//
//        }
//
//        return null;
//    }
//
//    @Override
//    public ResBean signForMacKey(ReqBean reqBean) {
//
//        try {
//            String deThreeEncry = URLDecoder.decode(MrAzuEncryptionUtil.getDeThreeEncry(reqBean.getData()), "UTF-8");//对最后的参数进行三重加密
//
//            System.out.println(deThreeEncry);
//            QRCodeSignReqBean qrCodeSignReqBean = GsonUtils.fromJson(deThreeEncry, QRCodeSignReqBean.class);
//            QRCodeSignResBean qrCodeSignResBean = qrCodeService.signForMacKey(qrCodeSignReqBean);
//
//            return new ResBean(URLEncoder.encode(GsonUtils.toJson(qrCodeSignResBean), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            try {
//                return new ResBean(URLEncoder.encode("数据解析失败", "UTF-8"));
//            } catch (UnsupportedEncodingException e1) {
//                e1.printStackTrace();
//            }
//
//        }
//        return null;
//    }
//}
