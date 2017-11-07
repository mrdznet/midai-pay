/**
 * Project Name:midai-pay-common
 * File Name:PayMidaiXmlMessageBean.java
 * Package Name:com.midai.framework.xml.pay
 * Date:2016年11月11日上午11:38:07
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.xml.pay;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.midai.framework.xml.MidaiXmlBean;

/**
 * ClassName:PayMidaiXmlMessageBean <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月11日 上午11:38:07 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(propOrder = { "tpdu", "head","type","channel" ,"id","field"})  
public class PayMidaiXmlMessageBean implements MidaiXmlBean {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 2530119041753616741L;
	@XmlAttribute
	private String tpdu;
	@XmlAttribute
	private String head;
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String channel;
	@XmlAttribute
	private String id;
	
	
	@XmlElement(name="field")
	private List<PayMidaiXmlFieldBean> field;

	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	
	public String getTpdu() {
		return tpdu;
	}

	public void setTpdu(String tpdu) {
		this.tpdu = tpdu;
	}


	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}





	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	public List<PayMidaiXmlFieldBean> getField() {
		return field;
	}

	public void setField(List<PayMidaiXmlFieldBean> field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}





}
