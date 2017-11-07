package com.midai.pay.mobile.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import com.midai.pay.mobile.vo.DeviceModeTypeFactoryVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：MobileCCBDMapper   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年10月31日 下午3:46:42   
* 修改人：wrt   
* 修改时间：2016年10月31日 下午3:46:42   
* 修改备注：   
* @version    
*    
*/
public interface MobileCCBDMapper extends Mapper<DeviceModeTypeFactoryVo> ,MySqlMapper<DeviceModeTypeFactoryVo>{

	@SelectProvider(type=com.midai.pay.mobile.provider.MobileCCBDProvider.class, method="getDeviceModeTypeFactoryVoByPostalCode")
	List<DeviceModeTypeFactoryVo> getDeviceModeTypeFactoryVoByPostalCode(Map<String, String> map);

}
