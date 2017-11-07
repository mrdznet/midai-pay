package com.midai.pay.web.controller.agent;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.pay.agent.service.AgentTempletService;
import com.midai.pay.agent.vo.AgentTempletVo;
import com.midai.pay.agent.vo.CustomerTempletVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Api("代理商/商户-扣率模板")
@RestController
@RequestMapping("/agent/templet")
public class AgentTempletController {
	
	@Reference
	private AgentTempletService agentTempletService;
	
	//------------------------------------------------------ 代理商扣率模板
	@ApiOperation("代理商扣率模板-保存")
    @PostMapping("/updateAgentTemplet")
	public JSONObject updateAgentTemplet(@RequestBody AgentTempletVo agentTempletVo) {
		JSONObject jsonObject = new JSONObject();
		
    	jsonObject.put("result", agentTempletService.updateAgentTemplet(agentTempletVo));
		
    	return jsonObject;
	}
	
	@ApiOperation("获取代理商扣率模板")
    @GetMapping("/getAgentTemplet")
	public AgentTempletVo getAgentTemplet() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return agentTempletService.getAgentTemplet(userDetails.getUsername());
	}
	
	@ApiOperation("获取代理商扣率模板")
    @GetMapping("/getAgentTempletByAgentNo")
	public AgentTempletVo getAgentTemplet(@RequestParam String agentNo) {
		return agentTempletService.getAgentTempletByAgentNo(agentNo);
	}
	
	
//------------------------------------------------------商户扣率模板	
	@ApiOperation("商户扣率模板-更新")
    @PostMapping("/updateCustomerTemplet")
	public JSONObject updateCustomerTemplet(@RequestBody CustomerTempletVo customerTempletVo) {
		JSONObject jsonObject = new JSONObject();
		
    	jsonObject.put("result", agentTempletService.updateCustomerTemplet(customerTempletVo));
		
    	return jsonObject;
	}
	
	@ApiOperation("获取商户扣率模板")
    @GetMapping("/getCustomerTemplet")
	public CustomerTempletVo getCustomerTemplet() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return agentTempletService.getCustomerTemplet(userDetails.getUsername());
	}
	
	
	@ApiOperation("获取商户扣率模板")
	@GetMapping("/getCustomerTempletByAgentNo")
	public CustomerTempletVo getCustomerTemplet(@RequestParam String agentNo) {
		return agentTempletService.getCustomerTempletByAgentNo(agentNo);
	}
	
}
