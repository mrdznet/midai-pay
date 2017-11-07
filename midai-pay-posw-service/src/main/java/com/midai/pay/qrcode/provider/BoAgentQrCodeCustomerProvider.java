package com.midai.pay.qrcode.provider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.qrcode.entity.BoAgentQrCodeCustomerEntity;
import com.midai.pay.qrcode.query.AgentQrCodeCustomerQuery;

public class BoAgentQrCodeCustomerProvider {

	private final String columns = " id, CONCAT(agent_no,'-',id) qrCodeId, merc_no mercNo, merc_name mercName, agent_no agentNo, qrcode_path qrcodePath, file_name fileName ";
	
	public String paginateListAgentQrCodeCustomer(AgentQrCodeCustomerQuery query) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select " + columns );
		sql.append(commonSql(query));
		
		if (!StringUtils.isEmpty(query.getOrder())) {
			sql.append(" order by create_time " + query.getOrder());
		} else {
			sql.append(" order by create_time desc ");
		}
		if (query.getPageSize() > 0) {
			sql.append(" limit " + (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
		}
		return sql.toString();
	}
	
	private StringBuffer commonSql(AgentQrCodeCustomerQuery query) {
		StringBuffer sw = new StringBuffer();
		sw.append(" from tbl_bo_agent_qrcode_customer t where 1=1 ");
		if(query != null) {
			if(StringUtils.isNotEmpty(query.getAgentNo())) {
				sw.append(" and t.agent_no = #{agentNo} ");
			}
			
			if(StringUtils.isNotEmpty(query.getUnbound())) {
				if(query.getUnbound().equals("0")) { //未绑定
					
					sw.append(" and (t.merc_no is null or t.merc_no = '') ");
				} else if(query.getUnbound().equals("1")) { //已绑定
					sw.append(" and t.merc_no is not null and t.merc_no != '' ");
				}
			}
			
			if(StringUtils.isNotEmpty(query.getQrCodeNo())) {
				String codeNo = query.getQrCodeNo();
				String id = codeNo.substring(codeNo.lastIndexOf("-") + 1);
				sw.append(" and id= " + id);
			}
			
			
		}
		return sw;
	}

	public String paginateCountAgentQrCodeCustomer(AgentQrCodeCustomerQuery query) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) " );
		sql.append(commonSql(query));
		return sql.toString();
	}
	
	public String insertBatchData(Map<String, List<BoAgentQrCodeCustomerEntity>> map) {
		

		List<BoAgentQrCodeCustomerEntity> list = map.get("list");
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("{\"errorMsg\":\"保存代理商二维码为空！\"}");
		}
		StringBuffer sql = new StringBuffer();
		
		sql.append(" insert into tbl_bo_agent_qrcode_customer(agent_no, qrcode_path, file_name, create_time ) values");
		
	        MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].agentNo},#'{'list[{0}].qrcodePath},#'{'list[{0}].fileName}"
	        		+ " ,#'{'list[{0}].createTime})");
	        for (int i = 0; i < list.size(); i++) {
	            sql.append(messageFormat.format(new Integer[]{i}));
	            sql.append(",");
	        }
	        sql.setLength(sql.length() - 1);
	        return sql.toString();
	}
	
	public String deleteBatch(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from tbl_bo_agent_qrcode_customer where id in (" + map.get("ids") + ")");
		return sql.toString();
	}
	
	public String updateCustomerQrcode(Map<String, List<BoAgentQrCodeCustomerEntity>> map) {
		StringBuffer sql = new StringBuffer();
		sql.append("update tbl_bo_agent_qrcode_customer set merc_no='" + map.get("mercNo") + "', merc_name='" + map.get("mercName") + "' where id in (" + map.get("ids") + ")");
		return sql.toString();
	}
	
	public String queryByCustomerNos(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		sql.append("select " + columns + " from tbl_bo_agent_qrcode_customer where merc_no in (" + map.get("mercNos") + ")");
		return sql.toString();
	}
}
