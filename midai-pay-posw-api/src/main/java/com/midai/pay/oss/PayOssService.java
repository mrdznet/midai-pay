package com.midai.pay.oss;

import java.io.ByteArrayInputStream;
import java.util.Map;

public interface PayOssService {

	public String imgUpload(ByteArrayInputStream is, String fileName, String dirPath);
	
	/**
	 * web端获取OSS签名
	 * @param parentDir
	 * @return
	 */
	public Map<String, String> uploadFile(String parentDir);
	
	/**
	 * APP端获取OSS签名
	 * @param parentDir
	 * @return
	 */
	public Map<String, String> appUploadFile(String user);

	public String changeQrCodeAlias(String qrcodePath, String mercNo);
}
