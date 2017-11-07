package com.midai.pay.web.controller.handpay;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midai.framework.query.PageVo;
import com.midai.pay.handpay.entity.HyTztxEntity;
import com.midai.pay.handpay.query.HyTztxQuery;
import com.midai.pay.handpay.service.HandpayService;
import com.midai.pay.handpay.service.HyTztxService;
import com.midai.pay.handpay.vo.ResponseBean;
import com.midai.pay.handpay.vo.TransQueryRequestBean;
import com.midai.pay.handpay.vo.WithdrawRequestBean;

import io.swagger.annotations.Api;

@Api("翰银T0提现")
@RestController
@RequestMapping("/hytztx")
public class HyTztxController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HyTztxController.class);

    @Reference
	private HandpayService handpayService;
    @Reference
    private HyTztxService hyTztxService;

    @PostMapping("/list")
    public PageVo<HyTztxEntity> list(@RequestBody HyTztxQuery query){
    	
    	PageVo<HyTztxEntity> vo = new PageVo<HyTztxEntity>();
		vo.setRows(hyTztxService.query(query));
		vo.setTotal(hyTztxService.queryCount(query));
		return vo;
    }

    @GetMapping("/query")
    public String queryTransaction( @RequestParam Integer id) {
    	String resultJson = "";
    	HyTztxEntity entity = hyTztxService.findByPrimaryKey(id);
    	String resp = entity.getRespData();
    	ObjectMapper objectMapper  = new ObjectMapper();
    	ResponseBean bean = null;
    	try {
			bean = objectMapper.readValue(resp, ResponseBean.class);
		} catch (Exception e) {
			LOGGER.error("转化JSON失败", e);
		}  
    	TransQueryRequestBean req = new TransQueryRequestBean();
		req.setSubMerId(bean.getSubMerId());
		req.setOrderNo(bean.getOrderNo());
		req.setTransDate(bean.getTransDate());
		req.setTransSeq(bean.getTransSeq());
		
		resultJson = handpayService.tranQueryTransQueryResponseBean(req);
    	return resultJson;
    }
    
    @GetMapping("/tx")
    public String txOperate( @RequestParam Integer id) {
    	
    	String resultJson = "";
    	HyTztxEntity entity = hyTztxService.findByPrimaryKey(id);
    	String resp = entity.getRespData();
    	ObjectMapper objectMapper  = new ObjectMapper();
    	ResponseBean bean = null;
    	try {
			bean = objectMapper.readValue(resp, ResponseBean.class);
		} catch (Exception e) {
			LOGGER.error("转化JSON失败", e);
		}  
    	
    	WithdrawRequestBean req = new WithdrawRequestBean();
		req.setSubMerId(bean.getSubMerId());
		
		SimpleDateFormat df_time = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = df_time.format(new Date());
		//交易流水号
		req.setOrderNo(date);
		req.setTransAmount(" ");	//分
		req.setTransDate(date.substring(0, 7));
		
		resultJson =  handpayService.t0WithdrawStr(req);
		return resultJson;
    }
}
