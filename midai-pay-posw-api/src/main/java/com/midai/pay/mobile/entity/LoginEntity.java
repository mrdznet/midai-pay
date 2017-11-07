package com.midai.pay.mobile.entity;

import java.util.List;

import com.midai.pay.mobile.AppBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
@Data
public class LoginEntity extends AppBaseEntity {

	private String MERUNID;
	private String MERCID;
	private String MERCNAME;
	private String STATE;
	private String STATE0;
	private String OPSTATE;
	
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
	
	private List DEVICE;
}
