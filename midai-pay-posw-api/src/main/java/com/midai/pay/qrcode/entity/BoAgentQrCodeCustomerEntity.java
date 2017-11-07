package com.midai.pay.qrcode.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
*    
* 项目名称：midai-pay-posw-api   
* 类名称：BoAgentQrCodeCustomerEntity   
* 类描述：   代理商商户二维码类
* 创建人：wrt   
* 创建时间：2017年8月3日 下午12:22:34   
* 修改人：wrt   
* 修改时间：2017年8月3日 下午12:22:34   
* 修改备注：   
* @version    
*
 */
@Data
@Table(name = "tbl_bo_agent_qrcode_customer")
@EqualsAndHashCode(callSuper=false)
public class BoAgentQrCodeCustomerEntity extends BaseEntity {

	@Id
	@Column(name="id")
	private Integer id;

	/**代理商编码*/
	@Column(name="agent_no")
	private String agentNo;

	/**商户编号*/
	@Column(name="merc_no")
	private String mercNo;

	/**商户名称*/
	@Column(name="merc_name")
	private String mercName;

	/**二维码地址*/
	@Column(name="qrcode_path")
	private String qrcodePath;
	
	/**二维码名称*/
	@Column(name="file_name")
	private String fileName;


}
