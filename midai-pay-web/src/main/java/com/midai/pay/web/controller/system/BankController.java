package com.midai.pay.web.controller.system;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.user.entity.Bank;
import com.midai.pay.user.entity.BankBranch;
import com.midai.pay.user.query.BankBranchQuery;
import com.midai.pay.user.service.BankBranchService;
import com.midai.pay.user.service.BankService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("银行查询相关接口")
@RestController
@RequestMapping("/system/bank")
public class BankController {

	@Reference
	private BankService bankService;
	
	@Reference
	private BankBranchService bankBranchService;
	
	
	@ApiOperation(value="总行列表")
	@PostMapping("/loadAllBank")
	public List<Bank> loadAllBank(){
		List<Bank> bankList = bankService.findAll();
		
		return bankList;
	}
	
	@ApiOperation(value="支行查询")
	@PostMapping("/bankBranchList")
	public PageVo<BankBranch> bankBranchList(@RequestBody BankBranchQuery query){
		PageVo<BankBranch> vo = new PageVo<BankBranch>();
		vo.setRows(bankBranchService.pageQuery(query));
		vo.setTotal(bankBranchService.pageCount(query));
		
		return vo;
	}
}
