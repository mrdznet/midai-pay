/**
 * Project Name:midai-integration
 * File Name:AliyunOssServiceImpl.java
 * Package Name:com.midai.integration.service.impl
 * Date:2016年5月31日下午2:33:13
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.midai.pay.web.AliyunOSS.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.CopyObjectRequest;
import com.aliyun.oss.model.CopyObjectResult;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.midai.pay.web.AliyunOSS.service.OssFile;
import com.midai.pay.web.AliyunOSS.service.OssFile.Type;
import com.midai.pay.web.AliyunOSS.service.OssService;
import com.midai.pay.web.config.OSSProperties;

/**
 * ClassName:AliyunOssServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年5月31日 下午2:33:13 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@Service
public class AliyunOssServiceImpl implements OssService {

	private static final Logger log = LoggerFactory .getLogger(AliyunOssServiceImpl.class);
	
	@Autowired
	private OSSProperties ossProperties;
	
	private String tempDir="", zipDir="";
	
	@PostConstruct
	public void init(){
		String home = System.getProperties().getProperty("user.home");
		String separator = System.getProperties().getProperty("file.separator");
		tempDir=home+separator+ossProperties.getEcsTempDir();
		zipDir=home+separator+ossProperties.getEcsZipDir();
		
	}

	@Override
	public int downLoadFile(String dirNode, String targetDir, boolean recursion) {

		
		String separator = System.getProperties().getProperty("file.separator");
		
		
		Assert.hasText(dirNode,"targetDir must not be blank !");
		// 创建OSSClient实例
		int count = 0;
		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());
		
		// 下载object到文件
		try {

			// 如果不是文件夹直接下载文件
			if (!dirNode.endsWith("/")) {
				client.getObject(new GetObjectRequest(ossProperties.getBucket(), dirNode),
						new File(ossProperties.getEcsTempDir() + separator + targetDir, dirNode));
				return 1;
			}

			// 构造ListObjectsRequest请求
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
					ossProperties.getBucket());
			// "/" 为文件夹的分隔符
			listObjectsRequest.setDelimiter("/");
			// 设置路径前缀
			if (StringUtils.isNotBlank(dirNode)) {
				listObjectsRequest.setPrefix(dirNode);
			}

			ObjectListing listing = client.listObjects(listObjectsRequest);
			// 遍历所有CommonPrefix
			if (recursion) {
				for (String commonPrefix : listing.getCommonPrefixes()) {

					String path = commonPrefix.substring(0,
							commonPrefix.length() - 1);
					if (StringUtils.isNotBlank(ossProperties.getIgnoreZipDir())
							&& path.startsWith(ossProperties.getIgnoreZipDir())) {
						path = path.substring(ossProperties.getIgnoreZipDir().length());
					}
					File dir = new File(ossProperties.getEcsTempDir() + separator + targetDir, path);

					if (!dir.exists() && !dir.isDirectory()) {
						dir.mkdirs();
					}
					count += downLoadFile(commonPrefix, targetDir, recursion);
				}
			}

			// 遍历所有Object
			for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
				String path = objectSummary.getKey().substring(0,
						objectSummary.getKey().lastIndexOf("/"));
				if (StringUtils.isNotBlank(ossProperties.getIgnoreZipDir())
						&& path.startsWith(ossProperties.getIgnoreZipDir())) {
					path = path.substring(ossProperties.getIgnoreZipDir().length());
				}
				File dir = new File(ossProperties.getEcsTempDir(), targetDir + separator + path);
				if (!dir.exists() && !dir.isDirectory()) {
					dir.mkdirs();
				}
				count++;
				if (objectSummary.getKey().endsWith("/")) {
					continue;
				}

				client.getObject(
						new GetObjectRequest(ossProperties.getBucket(), objectSummary.getKey()),
						new File(dir, objectSummary.getKey().substring(
								objectSummary.getKey().lastIndexOf("/") + 1)));
			}

		} finally {
			// 关闭client
			client.shutdown();
		}

		return count;
	}

	@Override
	public String downLoadFileToZip(List<String> dirNode, String targetDir,
			boolean recursion, String fileName) {
		log.info(fileName);
		
		Assert.hasText(targetDir,"targetDir must not be blank !");
		Assert.hasText(fileName,"fileName must not be blank !");
		Assert.state(fileName.toLowerCase().endsWith(".zip"), "fileName must end with .zip !");

		for (String nodeDir : dirNode) {
			downLoadFile(nodeDir, targetDir, recursion);
		}
		
	
		String separator = System.getProperties().getProperty("file.separator");
		
		String sourceFilePath = ossProperties.getEcsTempDir() + separator + targetDir;
		Project prj = new Project();
		File zipFile=new File(ossProperties.getEcsZipDir(), fileName);
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(new File(sourceFilePath));
		// fileSet.setIncludes("**/*.java"); //包括哪些文件或文件夹
		// eg:zip.setIncludes("*.java");
		// fileSet.setExcludes(...); //排除哪些文件或文件夹
		zip.addFileset(fileSet);
		zip.execute();
	
		// 回传到OSS

		// 创建OSSClient实例
		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());
		// 上传文件
		try {
			client.putObject(ossProperties.getBucket(), ossProperties.getZipDir() + fileName, zipFile);
		
		} finally {
			// 清理资源
			File del = new File(sourceFilePath);
			deleteDir(del);
			// 清理web服务端zip文件
			zipFile.delete();
			// 关闭client
			client.shutdown();
		}

		return ossProperties.getZipDir() + fileName;
	}

	@Override
	public Map<String, String> uploadFile(String parentDir) {
		return uploadFile(parentDir, false);
	}

	@Override
	public Map<String, String> uploadFile(String parentDir, boolean openCallBack) {
		Map<String, String> map = new HashMap<String, String>();
		String host = "http://" + ossProperties.getBucket() + "." + ossProperties.getEndpoint();
		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());
		try {

			long expireEndTime = System.currentTimeMillis()
					+ Long.parseLong(ossProperties.getExpireTime()) * 1000;
			Date expiration = new Date(expireEndTime);
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(
					PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
			policyConds.addConditionItem(MatchMode.StartWith,
					PolicyConditions.COND_KEY, ossProperties.getRootDir() + parentDir);

			String postPolicy = client.generatePostPolicy(expiration,
					policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String encodedPolicy = BinaryUtil.toBase64String(binaryData);
			String postSignature = client.calculatePostSignature(postPolicy);

			map.put("accessid", ossProperties.getAccessid());
			map.put("policy", encodedPolicy);
			map.put("signature", postSignature);
			map.put("dir", ossProperties.getRootDir() + parentDir);
			map.put("host", "http://" + ossProperties.getBucket() + "." + ossProperties.getUploadEndpoint());
			map.put("expire", String.valueOf(expireEndTime / 1000));

			// 设置回调

			if (openCallBack) {
				Map<String, String> callBakMap = new LinkedHashMap<String, String>();
				callBakMap.put("callbackUrl", ossProperties.getCallbackUrl());
				callBakMap
						.put("callbackBody",
								"filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
				callBakMap.put("callbackBodyType",
						"application/x-www-form-urlencoded");
				JSONObject ja2 = JSONObject.fromObject(callBakMap);
				map.put("callback",
						Base64.encodeBase64String(ja2.toString().getBytes()));
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		} finally {
			client.shutdown();
		}
		return map;

	}

	private static boolean doCheck(String content, byte[] sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = BinaryUtil.fromBase64String(publicKey);
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature
					.getInstance("MD5withRSA");
			signature.initVerify(pubKey);
			signature.update(content.getBytes());
			boolean bverify = signature.verify(sign);
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@SuppressWarnings({ "finally" })
	private String executeGet(String url) {
		BufferedReader in = null;

		String content = null;
		try {
			// 定义HttpClient
			@SuppressWarnings("resource")
			DefaultHttpClient client = new DefaultHttpClient();
			// 实例化HTTP方法
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			content = sb.toString();
		} catch (Exception e) {
		} finally {
			if (in != null) {
				try {
					in.close();// 最后要关闭BufferedReader
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return content;
		}
	}

	@Override
	public List<OssFile> fielList(String dirNode) {

		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());
		dirNode = ossProperties.getRootDir() + dirNode;
		try {
			// 构造ListObjectsRequest请求
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
					ossProperties.getBucket());

			// "/" 为文件夹的分隔符
			listObjectsRequest.setDelimiter("/");

			// 列出fun目录下的所有文件和文件夹
			if (StringUtils.isNotBlank(dirNode)) {
				listObjectsRequest.setPrefix(dirNode);
			}
			ObjectListing listing = client.listObjects(listObjectsRequest);

			List<OssFile> list = new ArrayList<OssFile>();

			// 遍历所有Object
			for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {

				OssFile file = new OssFile();
				file.setKey(objectSummary.getKey());
				file.setType(Type.FILE);
				file.setName(objectSummary.getKey());
				if (StringUtils.isNotBlank(dirNode)) {
					file.setParentKey(dirNode);
					file.setName(objectSummary.getKey().replaceAll(dirNode, ""));
				}
				if (file.getKey().equals(dirNode)) {
					continue;
				}
				list.add(file);
			}

			// 遍历所有CommonPrefix
			for (String commonPrefix : listing.getCommonPrefixes()) {
				OssFile file = new OssFile();
				file.setKey(commonPrefix);
				file.setName(commonPrefix);
				file.setType(Type.DIRECTORY);
				if (StringUtils.isNotBlank(dirNode)) {
					file.setParentKey(dirNode);
					file.setName(commonPrefix.replaceAll(dirNode, ""));
					file.setName(file.getName().substring(0,
							file.getName().length() - 1));
				}
				list.add(file);
			}
			return list;

		} finally {
			// 关闭client
			client.shutdown();
		}

	}

	/**
	 * 
	 * deleteDir:(递归删除文件夹下所有文件). <br/>
	 *
	 * @author 陈勋
	 * @param dir
	 *            文件夹
	 * @return
	 * @since JDK 1.7
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	@Override
	public List<String> delFileBatch(List<String> keyList) {

		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());
		try {
			DeleteObjectsRequest request = new DeleteObjectsRequest(ossProperties.getBucket());
			request.setQuiet(false);
			request.setKeys(keyList);
			DeleteObjectsResult deleteObjectsResult = client
					.deleteObjects(request);

			return deleteObjectsResult.getDeletedObjects();
		} finally {
			client.shutdown();
		}

	}

	@Override
	public String downLoadFileToZip(List<String> fileKey, String targetDir,
			String fileName) {
		Assert.hasText(targetDir,"targetDir must not be blank !");
		Assert.hasText(fileName,"fileName must not be blank !");
		Assert.state(fileName.toLowerCase().endsWith(".zip"), "fileName must end with .zip !");

	
		
		// 创建OSSClient实例
		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());

		File dir = new File(ossProperties.getEcsTempDir(), targetDir);
		if (!dir.exists() && !dir.isDirectory()) {
			dir.mkdirs();
		}
		try {
			// 下载object到文件
			for (String key : fileKey) {
				String name = key.substring(key.lastIndexOf("/") + 1);
				client.getObject(new GetObjectRequest(ossProperties.getBucket(), key), new File(
						dir, name));
			}

			Project prj = new Project();
			Zip zip = new Zip();
			zip.setProject(prj);
			zip.setDestFile(new File(ossProperties.getEcsZipDir(), fileName));
			FileSet fileSet = new FileSet();
			fileSet.setProject(prj);
			fileSet.setDir(dir);
			// fileSet.setIncludes("**/*.java"); //包括哪些文件或文件夹
			// eg:zip.setIncludes("*.java");
			// fileSet.setExcludes(...); //排除哪些文件或文件夹
			zip.addFileset(fileSet);
			zip.execute();
			
			// 回传到OSS
			client.putObject(ossProperties.getBucket(), ossProperties.getZipDir() + fileName, new File(ossProperties.getEcsZipDir(),
					fileName));
		} finally {		
			// 清理资源
			 deleteDir(dir);
			 deleteDir(new File(ossProperties.getEcsZipDir(), fileName));
			// 关闭client
			 client.shutdown();
			
		  
			
		}

		return ossProperties.getZipDir() + fileName;
	}

	@Override
	public void createDir(String dirKey) {

		if (StringUtils.isBlank(dirKey)) {
			throw new IllegalArgumentException("dirKey must not be null");
		}

		// 创建OSSClient实例
		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());
		final String keySuffixWithSlash = ossProperties.getRootDir() + dirKey;
		try {
			log.info("createDir : start oss client !!!");
			
			boolean found = client.doesObjectExist(ossProperties.getBucket(), keySuffixWithSlash);
			if (!found) {
				client.putObject(ossProperties.getBucket(), keySuffixWithSlash,
						new ByteArrayInputStream(new byte[0]), null);
			}
		} finally {
			log.info("createDir : close oss client !!!");
			// 关闭client
			client.shutdown();
		}

	}
	
	@Override
	public void createDirList(List<String> dirKey) {

		Assert.notEmpty(dirKey);
		

		// 创建OSSClient实例
		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());
		
	
		try {
			log.info("createDir : start oss client !!!");
			for(String dir:dirKey){
				final String keySuffixWithSlash = ossProperties.getRootDir() + dir;
				boolean found = client.doesObjectExist(ossProperties.getBucket(), keySuffixWithSlash);
				if (!found) {
					client.putObject(ossProperties.getBucket(), keySuffixWithSlash,
							new ByteArrayInputStream(new byte[0]), null);
				}
			}
			
		} finally {
			log.info("createDir : close oss client !!!");
			// 关闭client
			client.shutdown();
		}

	}

	@Override
	public boolean isExistFile(String dirKey) {
		Assert.hasText(dirKey);
		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());
		try{
			final String keySuffixWithSlash = ossProperties.getRootDir() + dirKey;
			return client.doesObjectExist(ossProperties.getBucket(),keySuffixWithSlash);
		}finally {
			client.shutdown();
		}
	}

	@Override
	public List<String> notExistFile(List<String> dirKey) {
		Assert.notNull(dirKey);
		List<String> list=new ArrayList<>();
		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());
		try{
			for(String key:dirKey){
				final String keySuffixWithSlash = ossProperties.getRootDir() + key;
				boolean flag=client.doesObjectExist(ossProperties.getBucket(),keySuffixWithSlash);
				if(flag){
					ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossProperties.getBucket());
					listObjectsRequest.setDelimiter("/");
					listObjectsRequest.setPrefix(keySuffixWithSlash);
					//判断是否存在文件
					ObjectListing listing=client.listObjects(listObjectsRequest);
					int count=listing.getObjectSummaries().size();
					//表示空文件夹
					if(count==1){
						flag=false;
					};
					
				}
				
				if(!flag){
					list.add(key);
				}
			}
			return list;
		}finally {
			client.shutdown();
		}
	}
	
	

	public static void main(String[] args) {
         //oss-cn-shanghai.aliyuncs.com
		// oss-cn-shanghai.aliyuncs.com
		// C7zqGkfew2l8mw3h
		// TlZnagwEsBfijR1pee0cOSPLY3kSCi
		// midaitest001
		//
		OSSClient client = new OSSClient("oss-cn-shanghai.aliyuncs.com",
				"C7zqGkfew2l8mw3h", "TlZnagwEsBfijR1pee0cOSPLY3kSCi");  //  car/JJ-CD-160624-006/
		try {
			String key="404error.gif";
			String newKey="";
			if(key.contains(".")){
				newKey=key.substring(0,key.lastIndexOf("."))+"-down"+key.substring(key.lastIndexOf("."));
			}else{
				newKey=key+"-down";
			}

			// 创建CopyObjectRequest对象
			CopyObjectRequest copyObjectRequest = new CopyObjectRequest("midaitest001", key, "midaitest001", newKey);

			// 设置新的Metadata
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentDisposition("attachment");
			copyObjectRequest.setNewObjectMetadata(meta);
			 client.copyObject(copyObjectRequest);
			
		
		
		
		
		
		/**
		boolean flag1 = client.doesBucketExist("midaitest001"); //midaitest001
	//	client.putObject("midaitest001", "test/", new ByteArrayInputStream(
	//			new byte[0]), null);
		
		String ss="car/JJ-CD-160624-006/ 个人信用报告/";
	//  ss= URLEncoder.encode(ss);
		boolean flag2 = client.doesObjectExist("midaitest001",ss);
		System.out.println(flag1 + "----" + flag2);
		String dirNode="car/JJ-CD-160624-006/";
		try {
			// 构造ListObjectsRequest请求
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest(
					"midaitest001");

			// "/" 为文件夹的分隔符
			
			listObjectsRequest.setDelimiter("/");
			listObjectsRequest.setPrefix("car/JJ-RZ-160729-002/职业信息其他相关资料/");
			ObjectListing listing=client.listObjects(listObjectsRequest);
			int count=listing.getObjectSummaries().size();
			System.out.println(count);

//			// 列出fun目录下的所有文件和文件夹
//			if (StringUtils.isNotBlank(dirNode)) {
//				listObjectsRequest.setPrefix(dirNode);
//			}
//			ObjectListing listing = client.listObjects(listObjectsRequest);
//
//			List<OssFile> list = new ArrayList<OssFile>();
//
//			// 遍历所有Object
//			for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
//
//				OssFile file = new OssFile();
//				file.setKey(objectSummary.getKey());
//				file.setType(Type.FILE);
//				file.setName(objectSummary.getKey());
//				if (StringUtils.isNotBlank(dirNode)) {
//					file.setParentKey(dirNode);
//					file.setName(objectSummary.getKey().replaceAll(dirNode, ""));
//				}
//				if (file.getKey().equals(dirNode)) {
//					continue;
//				}
//				list.add(file);
//			}
//
//			// 遍历所有CommonPrefix
//			for (String commonPrefix : listing.getCommonPrefixes()) {
//				OssFile file = new OssFile();
//				file.setKey(commonPrefix);
//				file.setName(commonPrefix);
//				file.setType(Type.DIRECTORY);
//				if (StringUtils.isNotBlank(dirNode)) {
//					file.setParentKey(dirNode);
//					file.setName(commonPrefix.replaceAll(dirNode, ""));
//					file.setName(file.getName().substring(0,
//							file.getName().length() - 1));
//				}
//				list.add(file);
//				System.out.println(file.getKey());
//			}
//			
 * 
 */

		} finally {
			// 关闭client
			client.shutdown();
		}

	}

	@Override
	public  String setMeta(String key) {
		
	     Assert.notNull(key);
		

		// 创建OSSClient实例
		OSSClient client = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessid(), ossProperties.getAccesskey());
		try{
			
			
			String newKey="";
//			if(key.contains(".")){
//				newKey=key.substring(0,key.lastIndexOf("."))+"-down"+key.substring(key.lastIndexOf("."));
//			}else{
//				newKey="down/"+key+"-down";
//			}
			newKey="down/"+key;
			if(client.doesObjectExist(ossProperties.getBucket(),newKey)){
				return newKey;
			}
			
			
			// 创建CopyObjectRequest对象
			CopyObjectRequest copyObjectRequest = new CopyObjectRequest(ossProperties.getBucket(), key, ossProperties.getBucket(), newKey);

			// 设置新的Metadata
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentDisposition("attachment");
			copyObjectRequest.setNewObjectMetadata(meta);
			 client.copyObject(copyObjectRequest);
			 return newKey;
			
		}finally {
			client.shutdown();
		}
	}
	
	

	

}
