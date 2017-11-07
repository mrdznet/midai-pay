package com.midai.pay.customer.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.customer.entity.BoCustomerDevice;

public interface BoCustomerDeviceMapper extends MyMapper<BoCustomerDevice> {
	

	public String insert="merc_id,merc_no,body_no,state,bunding_time,create_time,is_first";
	public String insertProperty="#{mercId},#{mercNo},#{bodyNo},#{state},#{bundingTime},#{createTime},#{isFirst}";
	public String column = "id Id, merc_id mercId, device_id deviceId, merc_no mercNo,body_no bodyNo,state,bunding_time bundingTime, unbunding_time unbundingTime, create_time createTime,is_first isFirst";
	
	
    @Update(" update tbl_bo_customer_device set state=0, unbunding_time=now() WHERE merc_no=#{mercNo} AND body_no = #{bodyNo} ")
    public int updateState(@Param("mercNo")String mercNo,@Param("bodyNo")String bodyNo);
    
    @UpdateProvider(type=com.midai.pay.customer.provider.BoCustomerDeviceProvider.class,method="updateStateAndTime")
    public int updateStateAndTime(@Param("mercNo")String mercNo,@Param("bodyNos")String bodyNo);
    
    
    @Select("select count(1) from tbl_bo_customer_device where merc_no=#{mercNo} AND body_no = #{bodyNo} AND state=1")
    public int selectCustomerDeviceCount(@Param("mercNo") String mercNo,@Param("bodyNo") String bodyNo);
    
    @Insert(" insert into tbl_bo_customer_device("+insert+") values("+insertProperty+")")
    public int insertBoCustomerDevice(BoCustomerDevice  boCustomerDevice);
    
    @Delete("delete from tbl_bo_customer_device where  merc_no=#{mercNo} AND body_no in ( ${ids})")
    public int deleteBoCustomerDevice(@Param("mercNo") String mercNo,@Param("ids")String ids);
    
    @Update(" update tbl_bo_customer_device set is_first=1 WHERE merc_no=#{mercNo} AND body_no = #{bodyNo} ")
    public int updateIsFirst(@Param("mercNo") String mercNo,@Param("bodyNo") String bodyNo);
    
    @Delete(" delete from tbl_bo_customer_device where  merc_no=#{mercNo} ")
    public int deleteBoCustomerDeviceByMercNo(@Param("mercNo") String mercNo);

    @SelectProvider(type=com.midai.pay.customer.provider.BoCustomerDeviceProvider.class,method="getByodyNos")
	public List<BoCustomerDevice> getByodyNos(String bodyNos);

    @DeleteProvider(type=com.midai.pay.customer.provider.BoCustomerDeviceProvider.class,method="deleteBybodyNOs")
	public int deleteBybodyNOs(String bodyNOs);
    
    @InsertProvider(type=com.midai.pay.customer.provider.BoCustomerDeviceProvider.class,method="batchInsertCustomerDevices")
	public void batchInsertBoCustomerDevice(@Param("list")List<BoCustomerDevice> list);
}
