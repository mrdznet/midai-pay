/**
 * Project Name:demo-dubbo-provider
 * File Name:UserMapper.java
 * Package Name:com.midai.demo.mapper
 * Date:2016年7月27日下午6:31:38
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.sms.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.sms.entity.BoSMS;

/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：BoSMSMapper   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年10月17日 下午2:35:34   
* 修改人：wrt   
* 修改时间：2016年10月17日 下午2:35:34   
* 修改备注：   
* @version    
*    
*/
public interface BoSMSMapper extends MyMapper<BoSMS> {

	public String columns = " id, phonenumber, smscode, smscontent, create_time, stype, state, update_time ";
	
	@Select("select " + columns + " from tbl_bo_sms where phonenumber=#{phoneN} and STYPE=#{stype} and #{currenttime}<=date_add(create_time, interval 0.5 hour) and state='0'  order by id desc limit 0,1")
	BoSMS getByPhoneNumCur(@Param("phoneN") String phoneN, @Param("currenttime") String currenttime, @Param("stype") Integer stype);

	@Update(" update tbl_bo_sms set state=1 where id=#{smid}")
	int updateStateById(int smid);

	
}

