/**
 * Project Name:midai-pay-common
 * File Name:PayMidaiXmlFieldBean.java
 * Package Name:com.midai.framework.xml.pay
 * Date:2016年11月11日下午1:52:35
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.xml.pay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.midai.framework.xml.MidaiXmlBean;

/**
 * ClassName:PayMidaiXmlFieldBean <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月11日 下午1:52:35 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(propOrder = { "index", "required","value" })  
public class PayMidaiXmlFieldBean implements MidaiXmlBean {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 8307998424438730284L;

	@XmlAttribute
	private int index;

	@XmlAttribute
	private boolean required;

	@XmlAttribute
	private String value;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}


	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}

