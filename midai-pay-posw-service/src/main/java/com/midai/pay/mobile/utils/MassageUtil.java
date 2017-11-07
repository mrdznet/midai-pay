package com.midai.pay.mobile.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.midai.pay.common.constants.Constants;
import com.midai.pay.common.po.SqlParam;
import com.midai.pay.user.mapper.SystemModuleMapper;
import com.midai.pay.user.mapper.SystemUserRoleMapper;

public class MassageUtil {
	
	private  Logger logger = LoggerFactory.getLogger(MassageUtil.class);
	
	private final MassageProperties msgProperties;
	
	@Autowired
	private SystemUserRoleMapper systemUserRoleMapper;
	
	@Autowired
	private SystemModuleMapper systemModuleMapper;
	
	private List<String>  urls=new ArrayList<>();
	
	public MassageUtil(MassageProperties msgProperties){
		this.msgProperties = msgProperties;	
		urls = Arrays.asList(msgProperties.getUrl().split(","));
		
	}
	
	/**
	 * 
	 * @param pageMap	接收的page 
	 * @param toUser	消息接受者(多个接受者以逗号分隔)
	 */
	public void sendMsgByUser(String pageMap, String toUser, String aaa){
		if(StringUtils.isNotEmpty(pageMap) && StringUtils.isNotEmpty(toUser))
			this.sendMsg(pageMap, toUser);
	}
	
	/**
	 * 
	 * @param pageMap 	接收的page	
	 * @param toRole 	消息接受者(此角色下所有人)
	 */
	public void sendMsgByRole(String pageMap, String toRole, String aaa){
		if(StringUtils.isNotEmpty(pageMap) && StringUtils.isNotEmpty(toRole)){
			List<String> userList = systemUserRoleMapper.findAllUserByRoleid(toRole);
			
			if(userList.size() > 0){
				this.sendMsg(pageMap, StringUtils.join(userList, ","));
			}
		}
	}
	
	void sendMsg(String pageMap, String users){
		logger.info("发送消息：" + pageMap + " 给用户：" + users);
		for(String url : urls){
			CloseableHttpClient httpclient = HttpClients.createDefault();
			
			HttpPost httpPost = new HttpPost(url);
			
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("text", pageMap));
			nvps.add(new BasicNameValuePair("to", users));
			
			CloseableHttpResponse response = null;
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
				
				response = httpclient.execute(httpPost);
				logger.info("发送消息：" + pageMap + " 给用户：" + users);
			}catch (IOException e) {
				logger.error(e.getMessage());
			} finally {
			    try {
					if(null != response)	response.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * 按照工作流规则发消息
	 * 
	 * @param pageMap 接收的pag
	 * @param assignee 消息接受者
	 */
	public void sendMsgByProcess(String pageMap, String assignee){
		//assignee: uadmin,|urzzy1,|r41,|r42,.*o20,|r43,.*o20,
		if(StringUtils.isEmpty(assignee)) return;
		
		String[] roleIdOrgIdArr = assignee.split("\\|");
		List<String> userList = new ArrayList<String>();
		Map<String, String> paraMap;
		
		for(String roleId_orgId : roleIdOrgIdArr){
			if(roleId_orgId.startsWith("u")){	// 单个用户
				String user = roleId_orgId.substring(roleId_orgId.indexOf("u")+1, roleId_orgId.length()-1);
				userList.add(user);
			}else if(roleId_orgId.startsWith("r")){
				paraMap = new HashMap<String, String>();
				
				String[] roleOrgArr = roleId_orgId.split(",");
				for(String str : roleOrgArr){
					if(str.contains("r")) paraMap.put("roleId", str.substring(str.indexOf("r")+1));
					if(str.contains("o")) paraMap.put("inscode", str.substring(str.indexOf("o")+1));
				}
				
				List<String> users = systemUserRoleMapper.findUserByRoleAndInscode(paraMap); //根据角色和inscode获取用户
				if(null!=users && users.size()>0) userList.addAll(users);
			}
		}
		
		this.sendMsg(pageMap, StringUtils.join(userList, ","));
	}
	
	/**
	 * 按照资源发送系统消息
	 * 
	 * @param pageMap 接收的pag
	 * @param inscode
	 */
	public void sendMsgByResource(String pageMap, String inscode){
		int moduleId = 0;
		
		switch (pageMap) {
		case "jysh":	//交易审核
			moduleId = Constants.JYSH_MODULE_ID;
			break;
		case "jyjs":	//交易结算
			moduleId = Constants.JYJS_MODULE_ID;
			break;
		case "qfqs":	//清分清算
			moduleId = Constants.QFQS_MODULE_ID;
			break;
		case "zddk":	//自动打款
			moduleId = Constants.ZDDK_MODULE_ID;
			break;
		default:
			break;
		}
		
		/**
		 * 获取有此权限资源的所有用户
		 */
		List<String> userList = new ArrayList<String>();
		
		SqlParam param = new SqlParam();
		param.setParam_1((null==inscode) ? "" : inscode);
		param.setParam_4(moduleId);
		
		userList = systemModuleMapper.findUserByModuleIdAndInscode(param);
			
		this.sendMsg(pageMap, StringUtils.join(userList, ","));
	}
	
}
