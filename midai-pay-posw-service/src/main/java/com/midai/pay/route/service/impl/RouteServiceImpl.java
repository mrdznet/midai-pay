package com.midai.pay.route.service.impl;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.activiti.service.MidaiActivity;
import com.midai.pay.activiti.service.MidaiActivityParam;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.mapper.AgentMapper;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.route.entity.RouteMercInGroup;
import com.midai.pay.route.entity.RouteMercOutGroup;
import com.midai.pay.route.entity.RouteMercOutGroupConfig;
import com.midai.pay.route.mapper.RouteMercOutGroupConfigMapper;
import com.midai.pay.route.mapper.RouteMercOutGroupMapper;
import com.midai.pay.route.service.ChannelChoiceService;
import com.midai.pay.route.service.RouteService;
import com.midai.pay.routetransactionInformation.TransactionInformation;
import com.midai.pay.user.entity.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RouteServiceImpl implements RouteService {
	@Autowired
	private RouteMercOutGroupMapper routeMercOutGroupMapper;
	@Autowired
	private BoCustomerMapper boCustomerMapper;
	@Autowired
	private AgentMapper agentMapper;
	@Autowired
	private RouteMercOutGroupConfigMapper routeMercOutGroupConfigMapper;
	
	public static final String DEAL_SHENXIN="00000081";	//申鑫
	public static final String DEAL_JIDIAN = "301000000001";	//翰鑫
	
	@Reference
	private ChannelChoiceService channelChoiceService;
	
	/**
	 * 默认类型，商户
	 */
	public static final String DEFAULT_TYPE="merc";
	/**
	 * 代理商类型
	 */
	public static final String AGENT_TYPE="agent";
	
	public static final String DEFAULT_UNIT_ID="***";

	@Override
	public String route(String moblie,double money) {
		Assert.hasText(moblie);		
		//商户号
		BoCustomer customer=new BoCustomer();
		customer.setMobile(moblie);
		customer=boCustomerMapper.selectOne(customer);
		
//		route(DEFAULT_TYPE,customer.getMercId(),customer.getAgentId(), money, customer.getCityId());
		log.info("路由通道，手机号----金额：" + moblie + "----" + money);
		return route_2(customer, money);
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	private String randomMercOutId(List<RouteMercOutGroup>  list){
		if(list==null||list.isEmpty()){
			return null;
		}
		Random r=new Random();
		int index=r.nextInt(list.size());
		
		return list.get(index).getMercOutId();
	}
	
	private String route(String unitType,String unitId,String agentId,double money,String code){
		//外部商户号
		String mercOutId=null;
		
		RouteMercInGroup inGroup=new RouteMercInGroup();
		inGroup.setUnitType(unitType);
		inGroup.setUnitId(unitId);
		List<RouteMercOutGroupConfig> configlist=  routeMercOutGroupConfigMapper.selectByInGroup(inGroup);//外部商户组
			
		int outConfigId=0;
		for(int i=0;i<configlist.size();i++){
			//表达式匹配
		   if(check(configlist.get(i).getRuleExp(),money,code)){
			   outConfigId=configlist.get(i).getId();
			   break;
		   }
		}
		//直属代理商或者上级代理商
		String tmp=null;			
		if(!DEFAULT_UNIT_ID.equals(unitId)){
			Agent agent=new Agent();	
			agent.setAgentNo(agentId);
			agent=agentMapper.selectOne(agent);		
			if(DEFAULT_TYPE.equals(unitType)){
				tmp=agentId;
			}else{
				tmp=agent.getParentAgentId();
			}
			if(tmp==null||"0".equals(tmp)){		
				tmp=DEFAULT_UNIT_ID;
			}
			
			if(outConfigId==0){
				//递归
			 return	route(AGENT_TYPE,tmp,tmp,money,code);			
			}
		}

		//外部商户获取
		List<RouteMercOutGroup> outGroupList=routeMercOutGroupMapper.selectByOutConfigId(outConfigId);
		mercOutId =randomMercOutId(outGroupList);
		if(mercOutId==null){
				throw new RuntimeException("全局策略异常,请检查！");
		}
		
		return mercOutId;
	}
	
	//简单规则
	private String route_2(BoCustomer customer, double money){
		String instCode = null;
		
		/**
		 * 通道和路由规则
		 * 		通道规则: 申鑫( money<5000元 ), 吉点( money>=5000元 )
		 *  	路由规则:申鑫: 随机选某一通道的商家, 吉点:已报备的商户号
		 *  
		 */
		TransactionInformation ti = new TransactionInformation();
		ti.setMoble(customer.getMobile());
		ti.setMoney(money);
		Set<String> channelSet = channelChoiceService.getChannelList(ti);
		if(channelSet.isEmpty()) {
			throw new RuntimeException("未找到符合标准的通道");
		} else {
			Iterator<String> it = channelSet.iterator();
			int i = 0;
			while(it.hasNext()) {
				String logInstCode = it.next().toString();
				log.info("路由通道，选择通道：" + i + "----" + logInstCode);
				if(i == 0) {
					instCode = logInstCode;
				}
				i ++;
			}
		}
		
		
		/*this.instCheck();
		
		switch (inst) {
		
		case DEAL_SHENXIN:
			instCode = DEAL_SHENXIN;
			break;
			
		case  DEAL_JIDIAN:
			instCode = DEAL_JIDIAN;
			break;
			
		case "all":
			if(Double.compare(money, 5000*100) >-1) instCode = DEAL_JIDIAN;
			else  instCode = DEAL_SHENXIN;
			break;
			
		default:
			break;
		}
		*/
		return this.inst_select(instCode, customer.getMercId());
	}
	
	private String instCheck(){
		String inst = "all";
		List<String> openInsts = routeMercOutGroupMapper.selectAllOpenInst();
		
		if(openInsts.isEmpty() || openInsts.size()==0){
			throw new RuntimeException("通道都已关闭");
		}
		
		if(openInsts.size()==1){	//只有一个通道开通
			return openInsts.get(0);
		}
		
		return inst;
	};
	
	private String inst_select(String instCode, String mercId){
		String mercOutId = null;
		log.info("路由通道编码：" + instCode + "----商户小票号：" + mercId);
		if(!StringUtils.isEmpty(instCode) && !StringUtils.isEmpty(mercId)){
			mercOutId = routeMercOutGroupMapper.selectInst(instCode, mercId);
			
			if(StringUtils.isEmpty(mercOutId)) return null;
		}
		
		log.info("路由返回大商户号：" + mercOutId);
		return mercOutId;
	}
	private boolean check(String exp,double money,String code ){
		ScriptEngine engine=new ScriptEngineManager().getEngineByName("JavaScript");
		//变量设置
		engine.put("money", money);
		engine.put("code", code);
		
		//解析表达式
		try {
			return  (boolean)engine.eval(exp);
		} catch (ScriptException e) {
			log.error(e.getMessage());
		}
		
		return false;
	}
	
	@MidaiActivity(task="#{name}")
	@Override
	public String testActivity(@MidaiActivityParam User user) {
		return user==null?null:user.getName();
	}

	public static void main(String[] args) {
		/*ScriptEngine engine=new ScriptEngineManager().getEngineByName("JavaScript");
		//变量设置
		engine.put("money",100);
		engine.put("code", 200);
		//解析表达式
		String exp = "money==100 && code==200";
		try {
			System.out.println((boolean)engine.eval(exp));
		} catch (ScriptException e) {
			log.error(e.getMessage());
		}*/
	}

}
