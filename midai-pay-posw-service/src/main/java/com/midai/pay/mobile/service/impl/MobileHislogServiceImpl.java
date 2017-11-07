package com.midai.pay.mobile.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.entity.MobileHislogDetailEntity;
import com.midai.pay.mobile.entity.MobileListsEntity;
import com.midai.pay.mobile.mapper.MobileHislogMapper;
import com.midai.pay.mobile.service.MobileHislogService;
import com.midai.pay.mobile.vo.HistroylogVo;





/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：MobileHislogServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月1日 上午11:16:02   
* 修改人：wrt   
* 修改时间：2016年11月1日 上午11:16:02   
* 修改备注：   
* @version    
*    
*/
@Service
public class MobileHislogServiceImpl implements MobileHislogService {

	private Logger logger = LoggerFactory.getLogger(MobileHislogServiceImpl.class);
	
	@Autowired
	private MobileHislogMapper mapper;

	@Override
	public Object searchHisDetail(String content, String code) {
		String pcode = "";
		String pmsg = "";
		JSONObject json = null;
		boolean isErr = true;
		try {
			json = new JSONObject(content);
			isErr = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		if (!isErr) {
			// 判断传人参数
			if (!json.has("LOGNO")) {
				return new AppBaseEntity("01", "参数定义错误");
			} 

			String logno = json.getString("LOGNO");
			//判断手机号是否为空
			if(StringUtils.isEmpty(logno)){
				return new AppBaseEntity("02", "不存在该流水，查询失败");
			} 
			
			List<HistroylogVo> hdvs = mapper.getMobileHislogByLogno(logno);
			HistroylogVo hdv = null;
			if(hdvs != null && hdvs.size() > 0) {
				hdv = hdvs.get(0);
			}
			MobileHislogDetailEntity entity = new MobileHislogDetailEntity();
			entity.setMERCNAM(hdv==null?"":hdv.getMercname()==null?"":hdv.getMercname());
			entity.setMERCID(hdv==null?"":hdv.getMchntcodein()==null?"":hdv.getMchntcodein());
			entity.setTXNTYP(hdv==null?"":hdv.getTranscodename()==null?"":hdv.getTranscodename());
			entity.setTXNAMT( String.format("%012d",Integer.parseInt(hdv==null?"0":hdv.getTransamt()==null?"0":hdv.getTransamt())));
			Date date = hdv==null?null:hdv.getTransrecvdate();
			if(date != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				entity.setTXNDATE(sdf.format(date));
			} else {
				entity.setTXNDATE("");
			}
			entity.setRSPCOD("00");
			entity.setRSPMSG("查询历史明细成功");
			return entity;
		
		
		}
		return new AppBaseEntity(pcode, pmsg);
	}

	@Override
	public Object searchHislog(String content, String code) {
		String pcode = "";
		String pmsg = "";
		JSONObject json = null;
		boolean isErr = true;
		try {
			json = new JSONObject(content);
			isErr = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		
		if (!isErr) {
			// 判断传人参数
			if (!json.has("PHONENUMBER")) {
				return new AppBaseEntity("01", "参数定义错误");
			} 
			
			if(code.equals("399008") && !json.has("pageNow")) {
				return new AppBaseEntity("01", "参数定义错误");
			}
			int  lineNumber=10;
			int monthNum = 1;
			int  pageNow=0;
			
			//每页显示的条数
			if(json.has("LINENUM")){
				String num = json.getString("LINENUM");
				if(!StringUtils.isEmpty(num)){
					lineNumber = Integer.parseInt(num);
				}
			}
			
			//查看记录的月份
			if(json.has("MONTHTTYPE")){
				monthNum = json.getInt("MONTHTTYPE");
				if(monthNum>3){
					return new AppBaseEntity("02", "查询记录暂只支持3个月内数据");
				}
			}
			
			String phonenumber = json.getString("PHONENUMBER");
			String pageNows = "0";
			
			//判断手机号是否为空
			if(StringUtils.isEmpty(phonenumber)){
				return new AppBaseEntity("02", "手机号不能为空");
			} 

			if(code.equals("399008") ){
				pageNows =json.getString("pageNow");
				if(pageNows==null || pageNows.isEmpty()) {
					return new AppBaseEntity("03", "分页的页数不能为空");
				} else {
					pageNow=Integer.parseInt(pageNows);
				}
			}
			
			Date current = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("code", code);
			paramMap.put("phonenumber", phonenumber);
			paramMap.put("current", sdf.format(current));
			paramMap.put("lineNumber", lineNumber + "");
			paramMap.put("pageNow", pageNow + "");
			paramMap.put("monthNum", monthNum + "");
			List<HistroylogVo> hdvs = new ArrayList<HistroylogVo>();
			hdvs = mapper.searchHislog(paramMap);
			List<Map<String, String>> lists = new ArrayList<Map<String, String>>(); 
			
			for(HistroylogVo hv : hdvs) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("LOGNO", hv.getHosttransssn()==null?"":hv.getHosttransssn());		
				Date date = hv.getTransrecvdate();
				if(date != null) {
					map.put("SYSDAT", sdf.format(date));
				} else {
					map.put("SYSDAT", "");
				}
				map.put("MERCNAM", hv.getMercname()==null?"":hv.getMercname());				
				map.put("TXNCD", hv.getTranscodename()==null?"":hv.getTranscodename());								
				map.put("TXNSTS", hv.getTransst()==null?"":hv.getTransst());
				map.put("TXNAMT", String.format("%012d",Integer.parseInt(hv.getTransamt())));
				map.put("CRDNO", hv.getPriacctno()==null?"":hv.getPriacctno());
				map.put("RSPCOD", hv.getRespcdloc()==null?"":hv.getRespcdloc());
				map.put("RSPMSG", hv.getRespcdlocdsp()==null?"":hv.getRespcdlocdsp());
				lists.add(map);
			}
			MobileListsEntity entity = new MobileListsEntity();
			entity.setLISTS(lists);
			entity.setRSPCOD("00");
			entity.setRSPMSG("历史流水查询成功");
			return entity;
		
		
		}
		return new AppBaseEntity(pcode, pmsg);
	}


}
