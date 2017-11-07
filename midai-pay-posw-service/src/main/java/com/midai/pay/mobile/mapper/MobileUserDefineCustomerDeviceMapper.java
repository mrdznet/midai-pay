package com.midai.pay.mobile.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.midai.pay.mobile.vo.DeviceCustomerVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MobileUserDefineCustomerDeviceMapper extends Mapper<DeviceCustomerVo> ,MySqlMapper<DeviceCustomerVo>{

	@Select(" SELECT d.device_no deviceid, cd.id cdid, cd.merc_no mercno, c.STATE state FROM tbl_bo_device d "
			+ " LEFT JOIN tbl_bo_customer_device cd ON cd.body_no = d.device_no "
			+ " LEFT JOIN tbl_bo_customer c ON cd.merc_no = c.merc_no "
			+ " WHERE d.device_no = #{termno}")
	List<DeviceCustomerVo> getDeviceCustomerByTermNo(String termno);

	@Select(" SELECT  c.merc_no mercno, cd.id sboid, cd.body_no bodyno,c.state FROM tbl_bo_customer c "
			+ " left join tbl_bo_customer_device cd on c.merc_no=cd.merc_no where c.mobile=#{phonenumber}")
	List<DeviceCustomerVo> getDeviceCustomerByMobile(String phonenumber);

	@Select("  select c.merc_no customerid, c.agent_id agentid, c.agent_name agentname, cd.body_no bodyno  "
			+ " from tbl_bo_customer c left join  tbl_bo_customer_device cd on c.merc_no=cd.merc_no where cd.body_no=#{bodyNo}")
	List<DeviceCustomerVo>  getMobileCustomerDeviceVobyBodyNo(String bodyNo);

	@Select("   select c.merc_no customerid, c.agent_id agentid, c.agent_name agentname, cd.body_no bodyno  "
			+ " from tbl_bo_customer c left join  tbl_bo_customer_device cd on c.merc_no=cd.merc_no where cd.id=#{boid} ")
	List<DeviceCustomerVo> getMobileCustomerDeviceVobyDoid(int boid);

	@Select(" select d.device_no deviceid, ad.agent_id agentid, cd.merc_no mercno, cd.merc_id mercid, cd.id customerid "
			+ " from tbl_bo_device d left join tbl_bo_agent_device ad on d.device_no=ad.device_no left join tbl_bo_customer_device cd on cd.body_no=d.device_no "
			+ " where d.device_no=#{deviceno}")
	List<DeviceCustomerVo> getMobileCustomerDeviceAgentVobyBodyNo(String deviceno);

}
