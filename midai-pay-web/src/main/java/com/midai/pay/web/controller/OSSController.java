package com.midai.pay.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.pay.web.AliyunOSS.service.OssBusinessService;
import com.midai.pay.web.AliyunOSS.service.OssFile;
import com.midai.pay.web.AliyunOSS.service.OssFile.Access;
import com.midai.pay.web.AliyunOSS.service.OssService;
import com.midai.pay.web.AliyunOSS.service.vo.OssFileListVo;
import com.midai.pay.web.AliyunOSS.service.vo.OssZipDirVo;
import com.midai.pay.web.config.OSSFolderProperties;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("OSS配置")
@RestController
@RequestMapping("/auth")
public class OSSController{
	
	private static final Logger LOGGER= LoggerFactory.getLogger(OSSController.class);

	@Reference
	private OssService ossService;

	@Reference
	private OssBusinessService ossBusinessService;
	
	@Autowired
    private Environment env;
	
	@Autowired
	private OSSFolderProperties oSSFolderProperties;
	
	
	@ApiOperation(value="设置上传信息")
	@GetMapping("/oss-upload/{parentDir}")
	public Map<String, String> upload(@PathVariable("parentDir") String parentDir) {
		return ossService.uploadFile(parentDir);
	}

	@ApiOperation(value="OSS文件夹下的文件列表")
	@PostMapping("/oss-file-list/{dirNode}")
	public OssFileListVo listFile(@PathVariable("dirNode")@ApiParam(value="文件列表",example="mf000160924/商户资质/") String dirNode){	
		OssFileListVo vo=new OssFileListVo();
		vo.setList(ossService.fielList(dirNode));
		
		return vo;
	}

	@ApiOperation(value="压缩OSS文件")
	@RequestMapping(path="oss-zip-dir",method={RequestMethod.GET,RequestMethod.POST})
	public OssZipDirVo zipFile(@ApiParam(value="文件目录列表,逗号分隔",example="a/,b/,c/") String dirNode, 
			 @ApiParam("是否递归")String recursion) {
		Random r = new Random();
		String fileName = "" + System.currentTimeMillis() + r.nextInt(1000)
				+ ".zip";
		String zipPath = null;
		String tmp = "tmp" + System.currentTimeMillis() + r.nextInt(1000);
		if (dirNode.contains(",")) {
			zipPath = ossService.downLoadFileToZip(
					Arrays.asList(dirNode.split(",")), tmp,
					"true".equals(recursion), fileName);
		} else {
			List<String> list = new ArrayList<>();
			list.add(dirNode);
			zipPath = ossService.downLoadFileToZip(list, tmp,
					"true".equals(recursion), fileName);
		}

		OssZipDirVo vo=new OssZipDirVo();
		vo.setZipPath(zipPath);
		
		return vo;
	}

	@ApiOperation(value="多文件打包下载")
	@RequestMapping(path="oss-zip-file",method={RequestMethod.GET,RequestMethod.POST})
	public OssZipDirVo zipFile(@ApiParam(value="文件列表,逗号分隔",example="a.txt,b.png,c.doc") @RequestParam String key) {
		Random r = new Random();
		String fileName = "" + System.currentTimeMillis() + r.nextInt(1000)
				+ ".zip";
		String zipPath = null;
		String tmp = "tmp" + System.currentTimeMillis() + r.nextInt(1000);
		if (key.contains(",")) {
			zipPath = ossService.downLoadFileToZip(
					Arrays.asList(key.split(",")), tmp, fileName);
		} else {
			List<String> list = new ArrayList<>();
			list.add(key);
			zipPath = ossService.downLoadFileToZip(list, tmp, fileName);
		}
		
		OssZipDirVo vo=new OssZipDirVo();
		vo.setZipPath(zipPath);
		return vo;
	}
	
	@ApiOperation(value="OSS文件目录列表")
	@PostMapping("/oss-dir-list/{orderId}/{type}/{designated}")
	public List<OssFile> ossDirList(@PathVariable("orderId")String orderId, @PathVariable("type")String type, @PathVariable("designated")String designated) {
		if(StringUtils.isEmpty(type)) {
			throw new RuntimeException("type为空");
		}
		if(StringUtils.isEmpty(orderId) || orderId.equals("undefined")) {
			throw new RuntimeException("orderId为空");
		}
		if (StringUtils.isNotEmpty(orderId) && StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(designated)) {
			List<OssFile> orderOssFileList = new ArrayList<OssFile>();
			String foldProp = "";
			
			if(StringUtils.isNotEmpty(type)) {
				foldProp = "oss.folder." + type;
			}
			
			orderOssFileList = commOssFileList(foldProp, orderId, designated);
			
			return orderOssFileList;
		} else {
			throw new RuntimeException("订单ID、类型、页面指定参数为空！");
		}
	}
	private List<OssFile> commOssFileList(String foldProp, String orderId, String designated) {
		List<String> allFolder = new ArrayList<String>();
		List<String> folder = new ArrayList<String>();
		
		List<OssFile> ossFileList = new ArrayList<OssFile>();
		
		String[] allArr = oSSFolderProperties.getCustomerComm().split(",");
		for (String fo : allArr) {
			allFolder.add(fo.trim());
		}
		
		ossBusinessService.createDirList(orderId, allArr, new String[] {});
		
		String folderS = env.getProperty(foldProp + "-" + designated).toString();
		if (StringUtils.isNotEmpty(folderS)) {
			for (String fo : folderS.split(",")) {
				folder.add(fo.trim());
			}
		}
		List<OssFile> results = getOssFileList(allFolder, folder);
		ossFileList = ossBusinessService.dirList(orderId, results);

		return ossBusinessService.dirList(orderId, ossFileList);
	}
	private List<OssFile> getOssFileList(List<String> allFolder, List<String> folder) {
		List<OssFile> results = new ArrayList<OssFile>();
		for (String f : allFolder) {
			OssFile oss = new OssFile();
			oss.setName(f);
			if (folder.contains(f)) {
				oss.setAccessList(Arrays.asList(Access.READ, Access.WRITE, Access.DELETE));
			} else {
				oss.setAccessList(Arrays.asList(Access.READ));
			}
			results.add(oss);
		}
		return results;
	}

	@ApiOperation(value="批量删除OSS文件")
	@PostMapping("/oss-del-list/{key}")
	public List<String> delDirList(@PathVariable("key") String key) {
		if (key.contains(",")) {
			
			return ossService.delFileBatch(Arrays.asList(key.split(",")));
		} else {
			List<String> list = new ArrayList<>();
			list.add(key);
			
			return ossService.delFileBatch(list);
		}
	}

	@ApiOperation(value="基于ID创建OSS文件目录")
	@PostMapping("/oss-create-dir/{orderId}")
	public void DirList(@PathVariable("orderId")String orderId) {
		if (StringUtils.isBlank(orderId)) {
			throw new IllegalArgumentException("orderId must not be null");
		}
		ossBusinessService.createDirList(orderId, 
				env.getProperty("oss.folder.customer-comm").toString().split(","), null);
	}
	
	@ApiOperation(value="设置OSS-MATE")
	@PostMapping("/setOssMeta")
	public String refundCount(String key) {
		String newKey = "";
		
		if(!StringUtils.isEmpty(key)){
			 newKey = ossService.setMeta(key);
		}else{
			LOGGER.info("设置OSS-MATE失败; key为空！");
		}
		
		return newKey;
	}
	
}
