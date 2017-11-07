package com.midai.pay.oss.impl;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.midai.pay.oss.PayOssService;

@Service
public class PayOssServiceImpl implements PayOssService{
	private static final Logger log = LoggerFactory .getLogger(PayOssServiceImpl.class);
	
	// 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
	public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
	// 当前 STS API 版本
	public static final String STS_API_VERSION = "2015-04-01";
	  
	@Value("${mifu.oss.endpoint}")
	private String endpoint;
	@Value("${mifu.oss.accessid}")
	private String accessId;
	@Value("${mifu.oss.accesskey}")
	private String accessKey;
	@Value("${mifu.oss.bucket}")
	private String bucket;
	@Value("#{'mifu/'}")
	private String rootDir;
	@Value("${mifu.oss.upload-endpoint}")
	private String uploadEndpoint;
	@Value("${mifu.oss.expire-time}")
	private String expireTime;

	@Value("${accessKeyId_app}")
	private String accessKeyId_app;
	@Value("${accessKeySecret_app}")
	private String accessKeySecret_app;
	@Value("${roleArn_app}")
	private String roleArn_app;
	@Value("${roleSessionName}")
	private String roleSessionName;
	
	

	@Override
	public String imgUpload(ByteArrayInputStream is, String fileName, String dirPath) {
		
		OSSClient ossClient = new OSSClient(endpoint, accessId, accessKey);

		String allPath = this.createDir(dirPath);
		
		String pathName = allPath + fileName;
		
		ossClient.putObject(bucket, pathName, is);

		// 关闭client
		ossClient.shutdown();
		
		return pathName;
	}

	public String createDir(String dirKey) {
		if (StringUtils.isBlank(dirKey)) {
			throw new IllegalArgumentException("dirKey must not be null");
		}

		OSSClient ossClient = new OSSClient(endpoint, accessId, accessKey);
		
		final String keySuffixWithSlash = rootDir + dirKey;
		try {

			boolean found = ossClient.doesObjectExist(bucket, keySuffixWithSlash);
			if (!found) {
				ossClient.putObject(bucket, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]), null);
			}
		} finally {
			log.info("createDir : close oss client !!!");
			ossClient.shutdown();
		}
		
		return keySuffixWithSlash;
	}

	@Override
	public Map<String, String> uploadFile(String parentDir) {
		Map<String, String> map = new HashMap<String, String>();
		
		OSSClient ossClient = new OSSClient(endpoint, accessId, accessKey);
		try {
			
			long expireEndTime = System.currentTimeMillis() + Long.parseLong(expireTime) * 1000;
			Date expiration = new Date(expireEndTime);
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
			policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, rootDir + parentDir);

			String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String encodedPolicy = BinaryUtil.toBase64String(binaryData);
			String postSignature = ossClient.calculatePostSignature(postPolicy);

			map.put("accessid", accessId);
			map.put("policy", encodedPolicy);
			map.put("signature", postSignature);
			map.put("dir", rootDir + parentDir);
			map.put("host", "http://" + bucket + "." + uploadEndpoint);
			map.put("expire", String.valueOf(expireEndTime / 1000));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		} finally {
			ossClient.shutdown();
		}
		return map;
	}

	@Override
	public Map<String, String> appUploadFile(String user) {
		Map<String, String> map = new HashMap<String, String>();
	    String roleSessionName_user = roleSessionName + user;
	    
	    String policy = "{\n" +
	            "    \"Version\": \"1\", \n" +
	            "    \"Statement\": [\n" +
	            "        {\n" +
	            "            \"Action\": \"oss:*\",\n" +
	            "            \"Resource\": \"*\",\n" +
	            "            \"Effect\": \"Allow\"\n" +
	            "        }\n" +
	            "    ]\n" +
	            "}";
	    
	    // 此处必须为 HTTPS
	    ProtocolType protocolType = ProtocolType.HTTPS;
	    
	    try {
	      final AssumeRoleResponse response = assumeRole(accessKeyId_app, accessKeySecret_app, 
	    		  roleArn_app, roleSessionName_user, policy, protocolType);
	      
	      map.put("AccessKeyId", response.getCredentials().getAccessKeyId());
	      map.put("AccessKeySecret", response.getCredentials().getAccessKeySecret());
	      map.put("Expiration", response.getCredentials().getExpiration());
	      map.put("RequestId", response.getRequestId());
	      map.put("SecurityToken", response.getCredentials().getSecurityToken());
	      
	      map.put("bucket", bucket);
	      map.put("rootDir", rootDir);
	    } catch (ClientException e) {
	      log.info("Failed to get a oss app token. Error code: "+ e.getErrCode()+", Error message: " + e.getErrMsg());
	    }
		
		return map;
	}
	
	
	public static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn,
			String roleSessionName, String policy, ProtocolType protocolType) throws ClientException {
		try {
			// 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
			IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
			DefaultAcsClient client = new DefaultAcsClient(profile);
			
			// 创建一个 AssumeRoleRequest 并设置请求参数
			final AssumeRoleRequest request = new AssumeRoleRequest();
			request.setVersion(STS_API_VERSION);
			request.setMethod(MethodType.POST);
			request.setProtocol(protocolType);
			request.setRoleArn(roleArn);
			request.setRoleSessionName(roleSessionName);
			request.setPolicy(policy);
			request.setDurationSeconds(3600L);
			// 发起请求，并得到response
			final AssumeRoleResponse response = client.getAcsResponse(request);
			
			return response;
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public String changeQrCodeAlias(String qrcodePath, String fileName) {
		//创建OSS客户端
		OSSClient ossClient = new OSSClient(endpoint, accessId, accessKey);
		
		OSSObject ossObject = ossClient.getObject(bucket, qrcodePath);
		
		String newFileName = qrcodePath.substring(0, qrcodePath.lastIndexOf("/") +1) + fileName + qrcodePath.substring(qrcodePath.lastIndexOf("."));
		try {
			ossClient.putObject(bucket, newFileName, ossObject.getObjectContent());
			ossClient.deleteObject(bucket, qrcodePath);
		} catch (Exception e) {
			log.error("转换失败", e);
		} finally {
			ossClient.shutdown();
		}
		return newFileName;
	}
	
}
