package com.midai.pay.user.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_mobile_app_version")
public class Mobileversion extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name="version_id")
	private String versionId;
	
	@Column(name="andriod_version_id")
	private String andriodVersionId;
	
	@Column(name="contents")
	private String contents;
	
	@Column(name="ios_load_url")
	private String iosLoadUrl;
	
	@Column(name="android_load_url")
	private String androidLoadUrl;
	
	@Column(name="ios_flag")
	private String iosFlag;
	
	@Column(name="android_flag")
	private String androidFlag;
	
	@Column(name="active_ind")
	private String activeInd;
	
	@Column(name="version_size")
	private String versionSize;
	
}
