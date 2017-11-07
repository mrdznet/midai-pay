/**
 * Project Name:midai-pay-common
 * File Name:PayMidaiXmlBean.java
 * Package Name:com.midai.framework.xml.pay
 * Date:2016年11月11日上午11:21:23
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.xml.pay;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.midai.framework.xml.MidaiXmlBean;

/**
 * ClassName:PayMidaiXmlBean <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月11日 上午11:21:23 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@XmlRootElement(name=PayMidaiXmlDefined.ROOT)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "filedType", "message" })  
public class PayMidaiXmlBean implements MidaiXmlBean {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 876666390624420710L;
	
	@XmlElement(name = "field_type")  
	private List<PayMidaiXmlFiledTypeBean> filedType;
	
	@XmlElement(name = "message")  
	private List<PayMidaiXmlMessageBean>  message;
	

	public List<PayMidaiXmlFiledTypeBean> getFiledType() {
		return filedType;
	}


	public void setFiledType(List<PayMidaiXmlFiledTypeBean> filedType) {
		this.filedType = filedType;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<PayMidaiXmlMessageBean> getMessage() {
		return message;
	}


	public void setMessage(List<PayMidaiXmlMessageBean> message) {
		this.message = message;
	}
	
	
	

}

