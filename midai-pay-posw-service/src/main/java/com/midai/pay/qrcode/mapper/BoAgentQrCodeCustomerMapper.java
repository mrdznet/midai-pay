package com.midai.pay.qrcode.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.qrcode.entity.BoAgentQrCodeCustomerEntity;
import com.midai.pay.qrcode.query.AgentQrCodeCustomerQuery;
import com.midai.pay.qrcode.vo.AgentQrCodeCustomerVo;

public interface BoAgentQrCodeCustomerMapper extends MyMapper<BoAgentQrCodeCustomerEntity> {
	
	public final String vocolumns = " id, CONCAT(agent_no,'-',id) qrCodeId, merc_no mercNo, merc_name mercName, agent_no agentNo, qrcode_path qrcodePath, file_name fileName ";
	
	@SelectProvider(type=com.midai.pay.qrcode.provider.BoAgentQrCodeCustomerProvider.class, method="paginateListAgentQrCodeCustomer")
	List<AgentQrCodeCustomerVo> paginateListAgentQrCodeCustomer(AgentQrCodeCustomerQuery query);

	@SelectProvider(type=com.midai.pay.qrcode.provider.BoAgentQrCodeCustomerProvider.class, method="paginateCountAgentQrCodeCustomer")
	int paginateCountAgentQrCodeCustomer(AgentQrCodeCustomerQuery query);

	@InsertProvider(type=com.midai.pay.qrcode.provider.BoAgentQrCodeCustomerProvider.class, method="insertBatchData")
	int insertBatchData(Map<String, List<BoAgentQrCodeCustomerEntity>> mapData);

	@DeleteProvider(type=com.midai.pay.qrcode.provider.BoAgentQrCodeCustomerProvider.class, method="deleteBatch")
	int deleteBatch(@Param("ids") String ids);

	@UpdateProvider(type=com.midai.pay.qrcode.provider.BoAgentQrCodeCustomerProvider.class, method="updateCustomerQrcode")
	int updateCustomerQrcode(Map<String, String> map);

	@Select("select " + vocolumns + " from tbl_bo_agent_qrcode_customer where merc_no=#{mercNo}")
	List<AgentQrCodeCustomerVo> queryCustomerQrCode(@Param("mercNo") String mercNo);

	@Select("select " + vocolumns + " from tbl_bo_agent_qrcode_customer where id=#{id}")
	AgentQrCodeCustomerVo selectById(@Param("id") Integer id);

	@SelectProvider(type=com.midai.pay.qrcode.provider.BoAgentQrCodeCustomerProvider.class, method="queryByCustomerNos")
	List<AgentQrCodeCustomerVo> queryByCustomerNos(String mercNos);


}
