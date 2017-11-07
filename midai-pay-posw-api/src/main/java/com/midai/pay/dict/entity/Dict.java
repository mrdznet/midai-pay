package com.midai.pay.dict.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**   
*    
* 项目名称：midai-pay-posw-api   
* 类名称：Dict   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月11日 下午2:54:04   
* 修改人：wrt   
* 修改时间：2016年11月11日 下午2:54:04   
* 修改备注：   
* @version    
*    
*/
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_dict")
public class Dict extends BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -17180146147442988L;

	/**
	 * 主键
	 */
	@Id
	private Integer id;
	
	private String	inscode;
	
	private Double	dailylimit;
	private Double	singlelimit;
	private Double	cardlimit;
	private String	remark;
	private String	remark1;
	
	
}