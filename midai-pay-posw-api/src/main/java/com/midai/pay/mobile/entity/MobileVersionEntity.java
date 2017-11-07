package com.midai.pay.mobile.entity;

import com.midai.pay.mobile.AppBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
@Data
public class MobileVersionEntity extends AppBaseEntity {

	/* 版本 */
	private String versionID;
	
	/* 版本内容 */
	private String contents;
	
	/* ios下载地址 */
	private String iosLoadURL;
	
	/* android下载地址 */
	private String androidLoadURL;
	
	/* ios强制更新标志：1-强制更新 */
	private String iosFlag;
	
	/* android强制更新标志：1-强制更新*/
	private String androidFlag;
	
	/* 大小*/
	private String versionSize;
}
