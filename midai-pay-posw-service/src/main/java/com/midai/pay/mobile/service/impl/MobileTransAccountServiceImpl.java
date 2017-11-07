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

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.autopaymoney.service.AutoPayMoneyService;
import com.midai.pay.autopaymoney.service.QuickPayMoneyService;
import com.midai.pay.autopaymoney.vo.AutoPayMoneyVo;
import com.midai.pay.common.pay.DateUtils;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.entity.MobileListsEntity;
import com.midai.pay.mobile.service.MobileTransAccountService;


/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：MobileTransAccountServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月11日 上午10:45:41   
* 修改人：wrt   
* 修改时间：2016年11月11日 上午10:45:41   
* 修改备注：   
* @version    
*    
*/
@Service
public class MobileTransAccountServiceImpl implements MobileTransAccountService {

	private Logger logger = LoggerFactory.getLogger(MobileTransAccountServiceImpl.class);

	@Reference
	private AutoPayMoneyService aps;
	
	@Reference
	private QuickPayMoneyService qps;
	
	
	@Override
	public Object queryTransAccount(String value,String code) {
		String pcode = "";
		String pmsg = "";
		JSONObject json = null;
		boolean isErr = true;
		try {
			json = new JSONObject(value);
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

			String lineNumber = "10";
			//String pageNow = "0";
			
			String phonenumber = json.getString("PHONENUMBER");
			
			//判断手机号是否为空
			if(phonenumber==null || phonenumber.isEmpty()){
				return new AppBaseEntity("02", "手机号不能为空");
			}
			
			Map<String, String> map = new HashMap<String, String>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String queryDate = sdf.format(new Date());
			if (json.has("QUERYDATE") && StringUtils.isNotEmpty(json.getString("QUERYDATE"))) {
				queryDate = json.getString("QUERYDATE");
			}
			map.put("queryDate", queryDate);
			map.put("phonenumber", phonenumber);
			List<AutoPayMoneyVo> apmls = new ArrayList<AutoPayMoneyVo>();
			if ("399003".equals(code)) {
				apmls = aps.queryPaginationList(map);
			}else if ("399005".equals(code)) {
				if (!json.has("TYPE")) {
					return new AppBaseEntity("01", "参数定义错误");
				}
				map.put("transChanel", json.getString("TYPE"));
				apmls = qps.queryPaginationList(map);
			}else{
				apmls = aps.queryPaginationWithQuickPayList(map);
			}
			Map<String, Map<String, Object>> dayMap = new HashMap<>();
			for(int i=0;i<31;i++){
				List<Map<String, String>> list = new ArrayList<>();
				Map<String, Object> oneRecord = new HashMap<>();
				Double sum = 0d;
				for(AutoPayMoneyVo am : apmls) {
					
					String day = DateUtils.format(am.getPayTime(), "dd");
					if ((i+1)==Integer.parseInt(day)) {
						Map<String, String> returnMap = new HashMap<String, String>();
						
						returnMap.put("TIXIFLAG", "0");				
						returnMap.put("PAYTIME", sdf.format(am.getPayTime()));			
						String txnamt = String.format("%d", new Double(am.getPayMoney()).intValue());//金额
						returnMap.put("PAYMONEY", txnamt);
						returnMap.put("PAYTYPE", am.getPayType());
						returnMap.put("ACCOUNTNO", am.getAccountNo());
						returnMap.put("PAYSTATE", am.getState());
						returnMap.put("MESSAGE", am.getPayStatestr());
						if ("00".equals(am.getState())) {
							sum = sum + am.getPayMoney();
						}
						list.add(returnMap);
					}
					oneRecord.put("record", list);
					oneRecord.put("total", String.format("%d", new Double(sum).intValue()));
					dayMap.put(String.format("%02d", i), oneRecord);
				}
			}
			
			long totalMoney = aps.queryTotalMoneyWithDate(map);
			MobileListsEntity entity = new MobileListsEntity();
			entity.setTOTALMONEY(totalMoney);
			entity.setRSPCOD("00");
			entity.setRSPMSG("到账记录查询成功");
			entity.setDAYRECORD(dayMap);
			entity.setNOWDATE(DateUtils.format(new Date(), "yyyy年MM月"));
			return entity;
		}
		return new AppBaseEntity(pcode, pmsg);
	}
	
}
