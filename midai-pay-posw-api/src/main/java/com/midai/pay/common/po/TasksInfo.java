package com.midai.pay.common.po;

import lombok.Data;

@Data
public class TasksInfo {
	
	private String taskId;
	
	private String subject;

	private String mercId;
	
	private String mercName;
	
	private String mercNo;
	
	private String agentId;
	
	private String state;
	
	private String createTime;
	
	private Integer merAuto;
}
