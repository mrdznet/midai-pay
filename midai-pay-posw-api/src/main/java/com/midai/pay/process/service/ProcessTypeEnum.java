package com.midai.pay.process.service;

public enum ProcessTypeEnum {
	merchant_process("merchant_process"),	//商户申请
	merchant_bank_process("merchant_bank_process");	//商户更改发卡行
	
	private String processDefID;
	
	private ProcessTypeEnum(String processDefID){
		this.processDefID = processDefID;
	}

	public String getProcessDefID() {
		return processDefID;
	}
}
