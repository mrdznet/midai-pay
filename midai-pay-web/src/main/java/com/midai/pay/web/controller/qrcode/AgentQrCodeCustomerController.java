package com.midai.pay.web.controller.qrcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.qrcode.query.AgentQrCodeCustomerQuery;
import com.midai.pay.qrcode.service.BoAgentQrCodeCustomerService;
import com.midai.pay.qrcode.vo.AgentQrCodeCustomerVo;
import com.midai.pay.web.vo.qrcode.AgentQrCodeAssVo;
import com.midai.pay.web.vo.qrcode.CustomerQrCodeVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Api("代理商商户二维码")
@RestController
@RequestMapping("/qrcode")
public class AgentQrCodeCustomerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AgentQrCodeCustomerController.class);
	
	@Reference
	private BoAgentQrCodeCustomerService agentQrCodeCustomerService;
	@Reference
	private AgentService agentService;

	@ApiOperation("代理商二维码分页")
	@PostMapping("/agent/paginate")
	public PageVo<AgentQrCodeCustomerVo> queryList(@RequestBody AgentQrCodeCustomerQuery query) {
		if(StringUtils.isEmpty(query.getAgentNo())) {
			throw new RuntimeException("代理商编号不能为空");
		}
		return agentQrCodeCustomerService.paginateAgentQrCodeCustomer(query);
	}
	
	@ApiOperation("代理商二维码保存")
	@PostMapping("/agent/save")
	public JSONObject save(@RequestBody AgentQrCodeAssVo ac) {
		
		if(StringUtils.isEmpty(ac.getAgentNo())) {
			throw new RuntimeException("代理商编号不能为空");
		}
		JSONObject json = new JSONObject();
		String agentNo = ac.getAgentNo();
		Integer count = ac.getCount();
		
		if(StringUtils.isEmpty(agentNo)) {
			json.put("result", "fail");
			json.put("msg", "代理商编号不能为空");
			return json;
		}
		
		if(count == null || count > 50) {
			json.put("result", "fail");
			json.put("msg", "数量不能大于50");
			return json;
		}
		
		Map<String, String> map = agentQrCodeCustomerService.createBatchCustomerQrCodePath(agentNo, count);
		
		if(map.isEmpty()) {
			json.put("result", "fail");
			json.put("msg", "二维码生成失败");
			return json;
		}
		Boolean isSuc = agentQrCodeCustomerService.insert(agentNo, map);
		
		if(isSuc) {
			json.put("result", "success");
			json.put("msg", "二维码生成成功");
		} else {
			json.put("result", "fail");
			json.put("msg", "二维码生成失败");
		}
		return json;
	}
	
	@ApiOperation("代理商二维码保存")
	@PostMapping("/agent/delete")
	public JSONObject delete(@RequestBody List<String> qrcodes) {
		JSONObject json = new JSONObject();
		
		if(qrcodes.isEmpty()) {
			json.put("result", "fail");
			json.put("msg", "二维码删除参数为空");
			return json;
		}
		
		
		StringBuffer sb = new StringBuffer();
		
		for(String id : qrcodes) {
			sb.append(id.trim()).append(",");
		}
		
		sb.setLength(sb.length() - 1);
		
		Boolean isSuc = agentQrCodeCustomerService.delete(sb.toString());
		if(isSuc) {
			json.put("result", "success");
			json.put("msg", "二维码删除成功");
		} else {
			json.put("result", "fail");
			json.put("msg", "二维码删除失败");
		}
		return json;
	}
	
	@ApiOperation("商户二维码查询")
	@PostMapping("/customer/query")
	public List<AgentQrCodeCustomerVo> queryCustomerQrCode(@RequestBody String mercNo) {
		if(StringUtils.isEmpty(mercNo)) {
			throw new RuntimeException("参数为空");
		}
		JSONObject json = JSONObject.fromObject(mercNo);
		mercNo = json.get("mercNo").toString();
		List<AgentQrCodeCustomerVo> list = new ArrayList<AgentQrCodeCustomerVo>();
		list = agentQrCodeCustomerService.queryCustomerQrCode(mercNo);
		
		return list;
	}
	
	@ApiOperation("商户二维码保存")
	@PostMapping("/customer/save")
	public JSONObject updateQrcode(@RequestBody CustomerQrCodeVo cc) {
		JSONObject json = new JSONObject();
		
		if(StringUtils.isNotEmpty(cc.getMercNo()) && !cc.getQrCodes().isEmpty()) {
			
			Boolean isSuc = agentQrCodeCustomerService.updateCustomerQrcode(cc.getMercNo(), cc.getQrCodes());
			if(isSuc) {
				json.put("result", "success");
				json.put("msg", "商户二维码添加成功");
			} else {
				json.put("result", "fail");
				json.put("msg", "商户二维码添加失败");
			}
		} else {
			json.put("result", "fail");
			json.put("msg", "商户二维码保存参数为空");
		}
		
		return json;
	}
	
	@ApiOperation("商户二维码删除")
	@PostMapping("/customer/delete")
	public JSONObject deleteCustomerQrcode(@RequestBody List<String> qrcodes) {
		JSONObject json = new JSONObject();
		
		if(!qrcodes.isEmpty()) {
			StringBuffer idsb = new StringBuffer();
			for(String id : qrcodes) {
				idsb.append(id.trim()).append(",");
			}
			idsb.setLength(idsb.length() - 1);
			Boolean isSuc = agentQrCodeCustomerService.deleteCustomerQrcode(idsb.toString());
			if(isSuc) {
				json.put("result", "success");
				json.put("msg", "商户二维码删除成功");
			} else {
				json.put("result", "fail");
				json.put("msg", "商户二维码删除失败");
			}
		} else {
			json.put("result", "fail");
			json.put("msg", "商户二维码删除参为空");
		}
		
		return json;
	}
	
	
}
