/**
 * Project Name:midai-integration
 * File Name:OssBusinessServiceImpl.java
 * Package Name:com.midai.integration.service.impl
 * Date:2016年6月13日下午1:34:00
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.midai.pay.web.AliyunOSS.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.web.AliyunOSS.service.OssBusinessService;
import com.midai.pay.web.AliyunOSS.service.OssFile;
import com.midai.pay.web.AliyunOSS.service.OssService;

/**
 * ClassName:OssBusinessServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年6月13日 下午1:34:00 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@Service
public class OssBusinessServiceImpl implements OssBusinessService {

	@Autowired
	private OssService ossService;

	// 融资租赁文件
	// private final String[] arr = new String[] { "借款申请表", "身份证","企业证件",
	// "企业征信报告", "对公银行流水" };

	@Override
	public boolean checkCarFilExist(String userId) {
		return false;
	}

	@Override
	public List<OssFile> dirList(String orderId, List<OssFile> arr) {

		List<OssFile> list = ossService.fielList(orderId + "/");
		List<OssFile> result = new ArrayList<>();

		for (OssFile shoss : arr) {
			for (OssFile oss : list) {
				if (oss.getName().equals(shoss.getName())) {
					oss.setAccessList(shoss.getAccessList());
					result.add(oss);
					break;
				}
			}
		}

		return result;
	}

	@Override
	public void createDirList(String orderId, String[] arr, String[] childArr) {

		if (StringUtils.isBlank(orderId)) {
			throw new IllegalArgumentException("orderId must not be null");
		}

		// 创建订单目录
		ossService.createDir(orderId + "/");

		List<String> dirList = new ArrayList<>();
		// 创建子目录
		for (String dir : arr) {
			dirList.add(orderId + "/" + dir + "/");
		}
		if(!dirList.isEmpty()){
			ossService.createDirList(dirList);
		}
		

		dirList.clear();
		// 创建二级目录
		for (String dir : childArr) {

			dirList.add(orderId + "/" + dir + "/");
		}
		if(!dirList.isEmpty()) {
			ossService.createDirList(dirList);
		}

	}

	@Override
	public List<OssFile> dirListByOrderIdAndRoles(String orderId, List<OssFile> dirArr) {
		List<OssFile> ossFiles = dirList(orderId, dirArr);
		return ossFiles;
	}

}
