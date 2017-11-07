package com.midai.pay.dict.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.dict.entity.Dict;
import com.midai.pay.dict.mapper.DictMapper;
import com.midai.pay.dict.service.BoDictService;

import tk.mybatis.mapper.entity.Example;



/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：FreezeMoneyServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月3日 下午4:26:51   
* 修改人：wrt   
* 修改时间：2016年11月3日 下午4:26:51   
* 修改备注：   
* @version    
*    
*/
@Service
public class BoDictServiceImpl extends BaseServiceImpl<Dict> implements BoDictService {

	private Logger logger = LoggerFactory.getLogger(BoDictServiceImpl.class);
	
	private final DictMapper mapper;
	
	public BoDictServiceImpl(DictMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public Dict getDictByInsCode(String insCode) {
		Example example = new Example(Dict.class);
		example.createCriteria().andEqualTo("inscode", insCode);
		List<Dict> list = mapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


	

}
