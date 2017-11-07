package com.midai.pay.posp.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.posp.entity.ParCardBin;
import com.midai.pay.posp.mapper.ParCarBinMapper;
import com.midai.pay.posp.service.ParCarBinService;

import tk.mybatis.mapper.entity.Example;

@Service
public class ParCarBinServiceImpl implements ParCarBinService {

	@Autowired
	private ParCarBinMapper mapper;

	@Override
	public ParCardBin findByCardNo(String cardNo) {
		Example example = new Example(ParCardBin.class);
		example.createCriteria().andEqualTo("cardBin", cardNo.substring(0, 6)).andEqualTo("cardLen",
				cardNo.length());

		RowBounds rowBounds = new RowBounds(0, 1);
		List<ParCardBin> parCardBinList = mapper.selectByExampleAndRowBounds(example, rowBounds);
		if (parCardBinList.isEmpty()) {
			return null;
		}
		
		return parCardBinList.get(0);
		
	}

	@Override
	public Boolean isDebiCard(String cardNo) {
		ParCardBin parBin = findByCardNo(cardNo);
		if(parBin != null) {
			String cardkind = parBin.getCardKindId();
			if("01".equals(cardkind)) {
				return true;
			} else {
				return false;
			}
		} else {
			//卡bin不存在
			throw new RuntimeException("nobin");
		}
	}
	
	
}
