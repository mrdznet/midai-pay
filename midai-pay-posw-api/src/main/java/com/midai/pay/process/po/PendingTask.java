package com.midai.pay.process.po;

import org.apache.commons.lang3.StringUtils;

public class PendingTask extends ProcessTask{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getFormKey() {
		return StringUtils.isEmpty(this.formKey)?this.taskKey:this.formKey;
	}
	
}
