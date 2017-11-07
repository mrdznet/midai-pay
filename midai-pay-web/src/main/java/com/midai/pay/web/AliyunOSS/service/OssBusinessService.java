/**
 * Project Name:midai-integration
 * File Name:OssBusinessService.java
 * Package Name:com.midai.integration.service
 * Date:2016年6月13日上午11:38:25
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.AliyunOSS.service;

import java.util.List;

/**
 * ClassName:OssBusinessService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年6月13日 上午11:38:25 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public interface OssBusinessService {
	
	/**
	 * 
	 * checkCarFilExist:(检查车贷业务所需文件是否存在). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author 陈勋
	 * @param userId
	 * @return true表示存在,false表示不存在
	 * @since JDK 1.7
	 */
	public boolean checkCarFilExist(String userId);
		
	/**
	 * 
	 * dirList:(返回左侧菜单列表). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author 陈勋
	 * @param orderId 订单ID
	 * @return  订单目录
	 * @since JDK 1.7
	 */
	public List<OssFile> dirList(String orderId,  List<OssFile> arr);
	
	/**
	 * 
	 * createDirList:(基于订单ID创建文件夹). <br/>
	 *
	 * @author 陈勋
	 * @param orderId 订单ID
	 * @since JDK 1.7
	 */

	public List<OssFile> dirListByOrderIdAndRoles(String orderId, List<OssFile> dirArr);

	public void createDirList(String orderId, String[] arr, String[] childArr);

}

