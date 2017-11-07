package com.midai.pay.wechat.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.wechat.entity.BoWechatEntity;
import com.midai.pay.wechat.mapper.BoWechatMapper;

import tk.mybatis.mapper.entity.Example;


/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：BoWechatServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月4日 下午6:03:39   
* 修改人：wrt   
* 修改时间：2016年11月4日 下午6:03:39   
* 修改备注：   
* @version    
*    
*/
@Service
public class BoWechatServiceImpl extends BaseServiceImpl<BoWechatEntity>  implements BoWechatService{

	private Logger logger = LoggerFactory.getLogger(BoWechatServiceImpl.class);

	private final BoWechatMapper mapper;
	
	public BoWechatServiceImpl(BoWechatMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}


	@Override
	public BoWechatEntity getBoWeChatByTixilogno(String outTradeNo) {
		Example example = new Example(BoWechatEntity.class);
		example.createCriteria().andEqualTo("tixilogno", outTradeNo);
		List<BoWechatEntity> list = mapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


}
