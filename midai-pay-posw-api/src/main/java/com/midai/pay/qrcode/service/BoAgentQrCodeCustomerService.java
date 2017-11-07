package com.midai.pay.qrcode.service;

import java.util.List;
import java.util.Map;

import com.midai.framework.query.PageVo;
import com.midai.pay.qrcode.query.AgentQrCodeCustomerQuery;
import com.midai.pay.qrcode.vo.AgentQrCodeCustomerVo;

public interface BoAgentQrCodeCustomerService {

	PageVo<AgentQrCodeCustomerVo> paginateAgentQrCodeCustomer(AgentQrCodeCustomerQuery query);

	/**
	 * 批量生成二维码
	 * @param agentNo 代理商编号
	 * @param count	数量
	 * @return
	 */
	Map<String, String> createBatchCustomerQrCodePath(String agentNo, Integer count);

	/**
	 * 批量添加代理商二维码
	 * @param agentNo
	 * @param map
	 * @return
	 */
	Boolean insert(String agentNo, Map<String, String> map);

	/**
	 * 批量删除代理商二维码
	 * @param string
	 * @return
	 */
	Boolean delete(String ids);

	/**
	 * 二维码绑定商户
	 * @param mercNo 商户编码
	 * @param ids	所有二维码id
	 * @return
	 */
	Boolean updateCustomerQrcode(String mercNo, List<String> ids);

	
	/**
	 * 删除绑定到商户的二维码
	 * @param idsb
	 * @return
	 */
	Boolean deleteCustomerQrcode(String ids);

	/**
	 * 查询商户绑定二维码
	 * @param mercNo 商户编号
	 * @return
	 */
	List<AgentQrCodeCustomerVo> queryCustomerQrCode(String mercNo);

	AgentQrCodeCustomerVo selectById(Integer id);

	/**
	 * 根据商户编号获取商户信息
	 * @param string
	 * @return
	 */
	List<AgentQrCodeCustomerVo> queryByCustomerNos(String mercNos);
}
