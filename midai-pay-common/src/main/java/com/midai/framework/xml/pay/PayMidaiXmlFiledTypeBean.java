/**
 * Project Name:midai-pay-common
 * File Name:PayMidaiXmlFiledTypeBean.java
 * Package Name:com.midai.framework.xml.pay
 * Date:2016年11月11日上午11:29:19
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
 * ClassName:PayMidaiXmlFiledTypeBean <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月11日 上午11:29:19 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(propOrder = { "fieldId", "etfName" ,"lengthType","dataType","length","alignMode","varType","convert"})
public class PayMidaiXmlFiledTypeBean implements MidaiXmlBean {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 5828485189877349096L;

	@XmlAttribute(name = "field_id")
	private int fieldId;

	@XmlAttribute(name = "etf_name")
	private String etfName;

	@XmlAttribute(name = "length_type")
	private String lengthType;

	@XmlAttribute(name = "data_type")
	private String dataType;

	@XmlAttribute
	private int length;

	@XmlAttribute(name = "align_mode")
	private String alignMode;

	@XmlAttribute(name = "var_type")
	private String varType;

	@XmlAttribute
	private String convert;

	public String getConvert() {
		return convert;
	}

	public void setConvert(String convert) {
		this.convert = convert;
	}


	public int getFieldId() {
		return fieldId;
	}

	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}


	public String getEtfName() {
		return etfName;
	}

	public void setEtfName(String etfName) {
		this.etfName = etfName;
	}


	public String getLengthType() {
		return lengthType;
	}

	public void setLengthType(String lengthType) {
		this.lengthType = lengthType;
	}

	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	
	public String getAlignMode() {
		return alignMode;
	}

	public void setAlignMode(String alignMode) {
		this.alignMode = alignMode;
	}

	
	public String getVarType() {
		return varType;
	}

	public void setVarType(String varType) {
		this.varType = varType;
	}

}
