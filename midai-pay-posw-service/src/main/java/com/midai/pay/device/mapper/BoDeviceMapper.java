package com.midai.pay.device.mapper;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.device.entity.BoDevice;
import com.midai.pay.device.query.BoAgentDeviceQuery;
import com.midai.pay.device.query.BoDeviceExcelQuery;
import com.midai.pay.device.query.BoDeviceQuery;
import com.midai.pay.device.vo.DeviceDetailExcelExportVo;
import com.midai.pay.device.vo.DeviceDetailVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

public interface BoDeviceMapper extends MyMapper<BoDevice> {

    @InsertProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="batchInsert")
    public long batchInsert(Map<String,List<BoDevice>> batchData);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="fetchByDeviceNos")
    public List<String> fetchByDeviceNos(String deviceNos);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="fetchBySimNos")
    public List<String> fetchBySimNos(String simNos);

    @Select(" select _nextval('RK') ")
    @Options(statementType= StatementType.STATEMENT )
    public int findCountInstorageBatch();

    @Select(" select _nextval('CK') ")
    @Options(statementType= StatementType.STATEMENT )
    public int findCountOutstorageBatch();

    @Select(" select _nextval('BG') ")
    @Options(statementType= StatementType.STATEMENT )
    public int findCountChangeBatch();

    @UpdateProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="updateByBodyNos")
    public long updateByBodyNos(@Param("bodyNos")String bodyNos,@Param("state")Integer state);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="paginateAgentInventoryDevice")
    public List<DeviceDetailVo> paginateAgentInventoryDevice(BoAgentDeviceQuery query);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="paginateAgentInventoryDeviceCount")
    public int paginateAgentInventoryDeviceCount(BoAgentDeviceQuery query);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="paginateAgentDevice")
    public List<DeviceDetailVo> paginateAgentDevice(BoDeviceQuery query);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="paginateAgentDeviceCount")
    public int paginateAgentDeviceCount(BoDeviceQuery query);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="paginateDevice")
    public List<DeviceDetailVo> paginateDevice(BoAgentDeviceQuery query);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="paginateDeviceCount")
    public int paginateDeviceCount(BoAgentDeviceQuery query);

    @Update("update tbl_bo_device set is_first=#{firstValue} where device_no=#{deviceNo}")
    public int updateDeviceFirstValue(@Param("firstValue")Integer firstValue,@Param("deviceNo")String deviceNo);

    @UpdateProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="updateDeviceCustomer")
    public int updateDeviceCustomer(@Param("customerId")String customerId,@Param("deviceNos")String deviceNos);
    
    @UpdateProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="updateDeviceCustomerAndState")
    public int updateDeviceCustomerAndState(@Param("customerId")String customerId,@Param("deviceNos")String deviceNos,@Param("state")int state);

    @Select(" select bd.device_no AS deviceNo, a.is_first AS isFirst,bd.update_time AS createTime, dt.`name`AS typeName,dm.`name` AS modeName " +
    		" from tbl_bo_customer_device  a INNER JOIN  tbl_bo_device bd   on a.body_no=bd.device_no " + 
    		"LEFT JOIN tbl_bo_device_mode dm ON bd.devicemode_id=dm.id " +
    		"LEFT JOIN tbl_bo_device_type  dt ON dm.device_type_id=dt.id  "+
    		" where a.merc_no=#{customerId} order by bd.update_time ASC " )
    public List<DeviceDetailVo> findByCustomerId(String customerId);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="excelExportDeviceDetails")
    public List<DeviceDetailExcelExportVo> excelExportDeviceDetails(BoDeviceQuery query);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="excelExportDeviceDetailsCount")
    public int excelExportDeviceDetailsCount(BoDeviceExcelQuery query);
    
    @Update("update tbl_bo_device set customer_id=#{customerId} where device_no = #{deviceNo} ")
    public int updateCustomerId(@Param("customerId")String customerId, @Param("deviceNo")String deviceNo);
    
    @Update("update tbl_bo_device set customer_id=#{customerId}, is_first=#{firstValue} where device_no = #{deviceNo} ")
    public int updateCustomerInfo(@Param("customerId")String customerId, @Param("firstValue")int firstValue, @Param("deviceNo")String deviceNo);

    @UpdateProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="bacthUpdateIsFirst")
    public int bacthUpdateIsFirst(String deviceNos);

    @SelectProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="excelExportSelectedDeviceDetails")
    public List<DeviceDetailExcelExportVo> excelExportSelectedDeviceDetails(String deviceNos);

    @Update("update tbl_bo_device set customer_id=#{customerid}, state=#{state} where device_no = #{deviceNo} ")
	public int updateCustomerIdAndStateByBodyNos(@Param("deviceNo")String deviceNo, @Param("customerid")String customerid,  @Param("state")int state);

    @UpdateProvider(type=com.midai.pay.device.provider.BoDeviceProvider.class,method="bacthUpdateCustomerIdAndState")
	public int bacthUpdateCustomerIdAndState(String deviceNos);

}
