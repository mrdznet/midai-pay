package com.midai.pay.mobile.service.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.dealtotal.service.DealtotalService;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.service.MobileTradeTicketService;
import com.midai.pay.trade.entity.SaleeSlip;
import com.midai.pay.trade.mapper.TradeReviewMapper;




/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：MobileTradeTicketServiceImpl   
* 类描述：   海贝小票
* 创建人：wrt   
* 创建时间：2016年11月14日 下午1:46:44   
* 修改人：wrt   
* 修改时间：2016年11月14日 下午1:46:44   
* 修改备注：   
* @version    
*    
*/
@Service
public class MobileTradeTicketServiceImpl implements MobileTradeTicketService {

	private Logger logger = LoggerFactory.getLogger(MobileTradeTicketServiceImpl.class);

	@Value("${mobile.ticket.sertch.url}")
	private String url;
	@Reference
	private DealtotalService dss;
	
	@Autowired
	private TradeReviewMapper tradeReviewMapper;
	/**
	 * 海贝小票
	 */
	@Override
	public Object execute(String content) {
		

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
			if ( !json.has("TRANSNO") || !json.has("TRANSDATE")) {
				return new AppBaseEntity("01", "参数定义错误");
			} 
			String transno = json.getString("TRANSNO");// 交易流水
			String transdate = json.getString("TRANSDATE");
			int count = dss.queryCountByTransnoAndTransdate(transno, transdate);
			if (count <= 0) {
				return new AppBaseEntity("03", "获取交易数据失败，请联系客服!");
			}
			
			url += transno;
			logger.info("####################URL链接:" + url + "######################");
			Map<String, String> map = new HashMap<String, String>();
			map.put("RSPCOD", "00");
			map.put("RSPMSG", "成功");
			map.put("URL", url);
			return map;
		}
		return new AppBaseEntity(pcode, pmsg);
	}

	@Override
	public Object executeRT(String content) {
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
			if ( !json.has("TRANSNO")) {
				return new AppBaseEntity("01", "参数定义错误");
			} 
			String hostTransSsn = json.getString("TRANSNO");
			SaleeSlip cardInfo = tradeReviewMapper.findCardInfo(hostTransSsn);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("MERCID", cardInfo==null?"":cardInfo.getMercNo()==null?"":cardInfo.getMercNo());
			map.put("MERCNAME", cardInfo==null?"":cardInfo.getMchntName()==null?"":cardInfo.getMchntName());
			map.put("TERMIDIN", cardInfo==null?"":cardInfo.getDeviceNoIn()==null?"":cardInfo.getDeviceNoIn()); //终端编号
			map.put("ISSINSID", cardInfo==null?"":cardInfo.getAccountName()==null?"":cardInfo.getAccountName()); //发卡行id
			map.put("PRIACCTNO", cardInfo==null?"":cardInfo.getTransCardNo()==null?"":cardInfo.getTransCardNo()); //卡号
			map.put("TRANSCODENAME", cardInfo==null?"":cardInfo.getTransCodeName()==null?"":cardInfo.getTransCodeName()); //交易类型
			map.put("CARDEXPIREDATE", cardInfo==null?"":cardInfo.getCardValid()==null?"2023":cardInfo.getCardValid()); //有效期
			map.put("TERMBATCHNO", cardInfo==null?"":cardInfo.getBatchNum()==null?"":cardInfo.getBatchNum());//批次号
			map.put("TERMSSN", cardInfo==null?"":cardInfo.getDeviceSsnOut()==null?"":cardInfo.getDeviceSsnOut());  //凭证号
			map.put("RESPAUTHID", cardInfo==null?"":cardInfo.getRespAuthId()==null?"":cardInfo.getRespAuthId()); //授权码
			map.put("RETRIEVALNOOUT", cardInfo==null?"":cardInfo.getHostTransSsn()==null?"":cardInfo.getHostTransSsn()); //交易参考号
			map.put("TRANSRECVDATE", cardInfo==null?"":cardInfo.getTransTime()==null?"":cardInfo.getTransTime());
			map.put("TRANSAMT", cardInfo==null?"":cardInfo.getTransAmt()==null?"": new DecimalFormat("##,###,##0.00").format(cardInfo.getTransAmt()));
			map.put("ISSINSRESV", cardInfo==null?"":cardInfo.getAccountName()==null?"":cardInfo.getAccountName());
			map.put("RSPCOD", "00");
			map.put("RSPMSG", "查询成功");
			return map;
		}
		return new AppBaseEntity(pcode, pmsg);
	}


}
