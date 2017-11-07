package com.midai.pay.mobile.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.midai.pay.mobile.vo.CustomerDeviceExtendVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface CustomerDeviceExtendMapper extends Mapper<CustomerDeviceExtendVo> ,MySqlMapper<CustomerDeviceExtendVo>{

	@Select( "  select cd.is_first isfirst, cd.state states, device_no deviceno, dm.name devicetype,dm.image_path imagepath,0 money, 3  paystate  "
			+ " from tbl_bo_customer_device cd "
			+ " left join tbl_bo_device d on cd.body_no=d.device_no "
			+ " left join tbl_bo_device_mode dm on d.devicemode_id=dm.id "
			+ "  where cd.merc_no=#{mercNo}" )
	List<CustomerDeviceExtendVo> getCustomerDeviceExtendVoByMercNo(String mercNo);

}
