package com.midai.pay.mobile.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.midai.pay.mobile.entity.CustomerDeviceEntity;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface CustomerDeviceMapper extends Mapper<CustomerDeviceEntity> ,MySqlMapper<CustomerDeviceEntity>{

	@Select( "  select c.merc_no UNID, c.merc_id MERCID, c.state STATE, c.opstate OPSTATE, cd.is_first ISFIRST, d.device_no DEVICENO, dm.`name` DEVICETYPE "
			+ "  from tbl_bo_customer c "
			+ "  left join tbl_bo_customer_device cd on c.merc_no=cd.merc_no "
			+ "  left join tbl_bo_device d on cd.body_no=d.device_no "
			+ "  left join tbl_bo_device_mode dm on d.devicemode_id=dm.id "
			+ "  where  c.MOBILE=#{userId}" )
	List<CustomerDeviceEntity> getCustomerDeviceEntityByUserId(String userId);

}
