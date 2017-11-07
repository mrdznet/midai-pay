package com.midai.pay.web.controller.inst;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.inst.query.InstAllQuery;
import com.midai.pay.inst.query.InstSimpleVo;
import com.midai.pay.inst.service.InstAllService;

@Api("通道相关")
@RestController
@RequestMapping("/inst")
public class InstAllController {

	@Reference
	private InstAllService instService;

	@ApiOperation("配置通道")
	@PostMapping("/save")
	public JSONObject saveInst(@RequestBody InstAll inst) {
		JSONObject jsonObject = new JSONObject();
		if (inst == null) {
			jsonObject.put("result","数据为空,请填写完整之后再次进行提交!");
		}
		int count=instService.selectInstAllCount(inst.getInstCode());
		if (count>0) {
			jsonObject.put("result","该支付商户号已经存在,请重新填写!");
		}
		int a = instService.insertInstAll(inst);
		if (a <= 0) {
			jsonObject.put("result","fail");
		}else {
			jsonObject.put("result","success");
		}
		
		return jsonObject;
	}
	
	@ApiOperation("批量更新状态")
	@PostMapping("/batchUpdate/{instState}")
	public JSONObject batchUpdateInst(@RequestBody String [] instCode,@PathVariable("instState") @ApiParam("通道状态")int instState){
		JSONObject jsonObject = new JSONObject();
		int a=instService.batchUpdateInst(instCode,instState);
		if (a<0) {
			jsonObject.put("result","fail");
		}else {
			jsonObject.put("result","success");
		}
		return jsonObject;
	}
	
	@ApiOperation("通道列表")
	@PostMapping("/instAllList")
	public PageVo<InstAll> instAllList(@RequestBody InstAllQuery query){
		 PageVo<InstAll> vo=new PageVo<InstAll>();
		 vo.setRows(instService.instAllList(query));
		 vo.setTotal(instService.instAllListCount(query));
		 return vo;
	}
	
	@ApiOperation("通道编辑")
	@PostMapping("/updateInstAll")
	public JSONObject updateInstAll(@RequestBody InstAll inst){
		JSONObject jsonObject = new JSONObject();
		int a=instService.updateInstAll(inst);
		if (a<0) {
			jsonObject.put("result","fail");
		}else{
			jsonObject.put("result","success");
		}
		return jsonObject;
	}
	
	@ApiOperation("通道信息加载")
	@PostMapping("/selectInstAllById/{instCode}")
	public InstAll selectInstAllById(@PathVariable("instCode") @ApiParam("通道号") String instCode){
		InstAll instAll=instService.selectInstAllById(instCode);
		return instAll;
	}
	
	@ApiOperation("加载所有通道")
	@GetMapping("/allInst")
	public List<InstSimpleVo> allInst(){
		return instService.findAllSimple();
	}
}
