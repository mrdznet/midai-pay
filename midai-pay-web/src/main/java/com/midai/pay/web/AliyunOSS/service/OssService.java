/**
 * Project Name:midai-integration
 * File Name:OssService.java
 * Package Name:com.midai.integration.service
 * Date:2016年5月31日下午2:28:15
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.midai.pay.web.AliyunOSS.service;

import java.util.List;
import java.util.Map;

/**
 * ClassName:OssService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年5月31日 下午2:28:15 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
public interface OssService {
	/**
	 * 
	 * downLoadFile:(下载阿里云OSS文件). <br/>
	 *
	 * @author 陈勋
	 * @param dirNode
	 *            文件节点名称（目录名称）
	 * @param targetDir
	 *            目标文件夹
	 * @param recursion
	 *            是否递归，true 递归，false 反之
	 * @return 下载文件总数
	 * @since JDK 1.7
	 */
	public int downLoadFile(String dirNode, String targetDir, boolean recursion);

	/**
	 * 
	 * downLoadFileToZip:(下载阿里云OSS文件并且生成zip文件). <br/>
	 *
	 * @author 陈勋
	 * @param dirNode
	 *            文件节点名称（目录名称）
	 * @param targetDir
	 *            目标文件夹
	 * @param recursion
	 *            是否递归，true 递归，false 反之
	 * @param fileName
	 *            zip文件名称 
	 * @return zip文件路径（服务器本地路径）
	 * @since JDK 1.7
	 */
	public String downLoadFileToZip(List<String> dirNode, String targetDir,
			boolean recursion, String fileName);
	/**
	 * 
	 * downLoadFileToZip:(把指定文件打包成一个压缩文件). <br/>
	 * @author 陈勋
	 * @param fileKey    需要打包的文件key(路径)
	 * @param targetDir  目标文件夹
	 * @param fileName   zip文件名称 
	 * @return
	 * @since JDK 1.7
	 */
	public String downLoadFileToZip(List<String>  fileKey,String targetDir,String fileName  );
	
	

	/**
	 * 
	 * uploadFile:(上传之前设置相关参数). 默认不设置回调接口<br/>
	 * @param parentDir  父级别目录
	 * @author 陈勋
	 * @return
	 * @since JDK 1.7
	 */
	public Map<String, String> uploadFile(String parentDir);
	
	
	/**
	 * 
	 * uploadFile:(上传之前设置相关参数). <br/>
	 * 
	 * @param openCallBack 是否开启回调
	 * @param parentDir  父级别目录
	 * @author 陈勋
	 * @return
	 * @since JDK 1.7
	 */
	public Map<String, String> uploadFile(String parentDir,boolean openCallBack);

	/**
	 * 
	 * fielList:(列出当前目录下所有的文件和文件夹). <br/>
	 * 
	 * @author 陈勋
	 * @param dirNode
	 * @return
	 * @since JDK 1.7
	 */
	public List<OssFile> fielList(String dirNode);
	
    /**
     * 
     * delFileBatch:(批量删除). <br/>
     *
     * @author 陈勋
     * @param keyList 删除对象key(路径)
     * @return  已删除对象信息
     * @since JDK 1.7
     */
	public List<String> delFileBatch(List<String> keyList);
	/**
	 * 
	 * createDir:(创建模拟目录). <br/>
	 * @author 陈勋
	 * @param dirKey  目录路径(key)
	 * @since JDK 1.7
	 */
	public void createDir(String dirKey);
	/**
	 * 
	 * createDirList:(批量创建模拟目录). <br/>
	 *
	 * @author 陈勋
	 * @param dirKey
	 * @since JDK 1.7
	 */
	public void createDirList(List<String> dirKey);

	/**
	 * 判断是否存在
	 * @param dirKey
	 * @return  存在true 反之 false
	 */
	public boolean isExistFile(String dirKey);

	/**
	 * 判断多个文件是否存在
	 * @param dirKey
	 * @return 不存在的文件集合
	 */
	public List<String> notExistFile(List<String> dirKey);
	
	
	/**
	 * 
	 * setMeta:(设置文件元信息). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author 屈志刚
	 * @param key
	 * @since JDK 1.7
	 */
	public String setMeta(String key);

}
