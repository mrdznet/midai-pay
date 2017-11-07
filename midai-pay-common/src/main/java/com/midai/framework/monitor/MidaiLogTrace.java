/**
 * Project Name:midai-monitor
 * File Name:MidaiLogTrace.java
 * Package Name:com.midai.monitor
 * Date:2016年8月3日下午12:22:17
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.monitor;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * ClassName:MidaiLogTrace <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年8月3日 下午12:22:17 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
public class MidaiLogTrace implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3163779129936657601L;

	public enum Result{
		SUCCESS,FAIL;
	}
	
	private Result result;

	private   String url;

	private   String traceId;
	
	private   Long start;
	
	private   Long end;	

	private List<MidaiLogSpan> spans=new ArrayList<MidaiLogSpan>();



	






	 


}

