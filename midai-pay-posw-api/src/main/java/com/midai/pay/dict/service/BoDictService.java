package com.midai.pay.dict.service;

import com.midai.framework.common.BaseService;
import com.midai.pay.dict.entity.Dict;

public interface BoDictService  extends BaseService<Dict>{

	Dict getDictByInsCode(String insCode);


}
