package com.midai.pay.device.service;

import com.midai.framework.common.BaseService;
import com.midai.framework.common.po.ResultVal;
import com.midai.framework.query.PageVo;
import com.midai.pay.device.entity.BoDevice;
import com.midai.pay.device.entity.BoDeviceInstorage;
import com.midai.pay.device.query.BoAgentDeviceQuery;
import com.midai.pay.device.query.BoDeviceExcelQuery;
import com.midai.pay.device.query.BoDeviceQuery;
import com.midai.pay.device.vo.*;

import java.util.List;

public interface BoDeviceService extends BaseService<BoDevice> {

    /** 构建批次号 */
    public String buildBatchNo(Integer receiptType);

    /** 入库前设备检查 */
    public ResultVal<List<DeviceInstorageDetailVo>> instorageCheck(String bodyNoStart,BoDeviceInstorage instorageM,String beforeBodyNos);

    /** 入库前设备检查 */
    public ResultVal<DeviceInstorageDetailVo> singleInstorageCheck(DeviceInstorageDetailVo instorageDetailM,String beforeBodyNos);

    /** 入库 */
    public int instorage(DeviceInstorageVo instorageVo,String userName);

    /** 出库 */
    public void outstorage(DeviceOutstorageVo outstorageVo,String userName);

    /** 变更 */
    public void changeDevice(DeviceOutstorageVo outstorageVo,String userName);

    /** 分页获取指定代理商下的设备(未绑定的库存-登录用户所属的代理商) */
    public PageVo<DeviceDetailVo> paginateAgentInventoryDevice(BoAgentDeviceQuery query,String userName);

    /** 分页获取指定代理商下的未绑定的设备 
     * @param userName */
    public PageVo<DeviceDetailVo> paginateAgentInventoryUnbindDevice(BoAgentDeviceQuery query, String userName);

    /** 分页获取指定代理商下的所有设备 */
    public PageVo<DeviceDetailVo> paginateAgentDevice(BoDeviceQuery query,String userName);

    public PageVo<DeviceDetailVo> paginateDevice(BoAgentDeviceQuery query);

    /** execl导出设备详细信息 */
    public List<DeviceDetailExcelExportVo> excelExportDeviceDetails(BoDeviceQuery query,String userName);

    /** execl导出设备详细信息统计 */
    public int excelExportDeviceDetailsCount(BoDeviceExcelQuery query,String userName);

    /** execl导出选取设备详细信息 */
    public List<DeviceDetailExcelExportVo> excelExportSelectedDeviceDetails(List<String> deviceNos);

	public List<BoDevice> getByDeviceNo(String deviceid);

}
