/**
 * Project Name:demo-dubbo-provider
 * File Name:UserMapper.java
 * Package Name:com.midai.demo.mapper
 * Date:2016年7月27日下午6:31:38
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.wechat.mapper;

import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.wechat.entity.BoWechatEntity;


/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：BoWechatMapper   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月4日 下午5:30:48   
* 修改人：wrt   
* 修改时间：2016年11月4日 下午5:30:48   
* 修改备注：   
* @version    
*    
*/
public interface BoWechatMapper extends MyMapper<BoWechatEntity> {

	@Update(" update  tbl_bo_wechat   set   paystate= #{paystate} ,paytime=#{paytime},payperson=#{payperson}, "
			+ "  channelcode = #{channelcode}, cjcodechannel = #{cjcodechannel},errorcode=#{errorcode},errormsg=#{errormsg}  ,autohitflag = #{autohitflag} where  tixilogno = #{tixilogno} ")
	int updateTixilognos(BoWechatEntity entity);

}

