package com.midai.pay.web.controller.dealtotal;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.dealtotal.query.DealtotalQuickQuery;
import com.midai.pay.dealtotal.service.DealtotalQuickService;
import com.midai.pay.dealtotal.vo.DealtotalQuickQueryVo;
import com.midai.pay.user.service.SystemUserService;
import com.midai.reqbean.QueryStatusReqBean;
import com.midai.reqbean.QueryStatusResBean;
import com.midai.service.QRCodePayService;
import com.midai.service.impl.QRCodePayServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Api("快捷交易")
@RestController
@RequestMapping("/dealtotal/quick")
public class DealtotalQuickController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DealtotalQuickController.class);

	@Reference
	private SystemUserService systemUserService;

	@Reference
	private DealtotalQuickService dealtotalQuickService;

	@Value("${channel.notify.url}")
	private String notifyUrl;
	
	@ApiOperation("快捷交易列表")
	@PostMapping("/list")
	public PageVo<DealtotalQuickQueryVo> angetQueryList(@RequestBody DealtotalQuickQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String allAgents = systemUserService.getAllChildAgents(userDetails.getUsername());
		query.setAgentId(allAgents);

		PageVo<DealtotalQuickQueryVo> pageVo = new PageVo<DealtotalQuickQueryVo>();
		pageVo.setRows(dealtotalQuickService.queryList(query));
		pageVo.setTotal(dealtotalQuickService.queryCount(query));
		return pageVo;
	}

	@ApiOperation("快捷交易查询")
	@PostMapping("/checktrans")
	public JSONObject checkTrans(@RequestBody QueryStatusReqBean req) {
		JSONObject jsonObject = new JSONObject();
		
		QueryStatusResBean queryStatusResBean = new QueryStatusResBean();
		
		if(StringUtils.isNotEmpty(req.getSendSeqId()) && StringUtils.isNotEmpty(req.getTransType())) {
			String transType = ""; //微信：B001 支付宝：Z001
			String payType = ""; //微信：WeChat   支付宝：AliPay

			//交易通道方 (1-微信, 2-支付宝, 3-银联扫码, 4-花呗, 5-京东白条)
			if(req.getTransType().equals("1")) {
				transType = "B001";
				payType = "WeChat";
			} if(req.getTransType().equals("2")) {
				transType = "Z001";
				payType = "AliPay";
			}
			
			req.setPayType(payType);
			req.setTransType(transType);
			QRCodePayService qrCodeService = new QRCodePayServiceImpl(notifyUrl);
			try {
				queryStatusResBean = qrCodeService.queryOrderStatus(req);
				jsonObject.put("result", queryStatusResBean);
			} catch (Exception e) {
				LOGGER.error("交易查询报错", e);
				queryStatusResBean.setPayResult("99");
				queryStatusResBean.setPayDesc("交易查询失败");
				jsonObject.put("result", queryStatusResBean);
			}
		} else {
			queryStatusResBean.setPayResult("99");
			queryStatusResBean.setPayDesc("查询参数不能为空");
			jsonObject.put("result", queryStatusResBean);
		}
		return jsonObject;
	}

}
