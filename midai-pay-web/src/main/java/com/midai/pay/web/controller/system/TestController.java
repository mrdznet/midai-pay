package com.midai.pay.web.controller.system;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.pay.user.entity.SystemOrganizationModel;
import com.midai.pay.user.service.SystemOrgUserForAgentService;
import com.midai.pay.user.service.SystemOrganizationService;
import com.midai.pay.user.vo.SystemOrgUserForAgentVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("测试")
@RestController
@RequestMapping("/system/test")
public class TestController {

    @Reference
    private SystemOrgUserForAgentService systemOrgUserForAgentService;
    @Reference
    private SystemOrganizationService sos;

    @ApiOperation("根据")
    @PostMapping("/add")
    public int addIn(@RequestBody SystemOrgUserForAgentVo uv) {
    	return systemOrgUserForAgentService.insertSystemOrgUserByAgent(uv);
    }
    
    @ApiOperation("根据")
    @PostMapping("/agentcode/{loginname}")
    public String AgentCode(@PathVariable String loginname) {
    	return systemOrgUserForAgentService.getAgentCodeByLoginname(loginname);
    }
    
    @ApiOperation("根据")
    @PostMapping("/org/findf/{id}")
    public List<SystemOrganizationModel> findThemselvesAndElderGenerationNode(@PathVariable int id) {
    	return sos.findThemselvesAndElderGenerationNode(id);
    }
    
    @ApiOperation("根据")
    @PostMapping("/org/find/{id}")
    public String findThemselves(@PathVariable int id) {
    	return sos.findOrgCatalogue(id);
    }
}
