/**
 * Project Name:midai-monitor
 * File Name:MidaiLogSpan.java
 * Package Name:com.midai.monitor
 * Date:2016年8月10日上午11:07:45
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * ClassName:MidaiLogSpan <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年8月10日 上午11:07:45 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
public class MidaiLogSpan  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3525336389463300452L;


	public enum Result{
		SUCCESS,FAIL;
	}
	
	
	private Result result;
	
	private String clientIp;
	
	private int clientPort;
	
	private String servierIp;
	
	private int servicePort;
	
	private String url;
	
	private String interfaceDesc;
	
	private long start;
	
	private long end;
	
	
	private String traceId;
	


}

