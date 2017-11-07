package com.midai.pay.web.controller.device;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.common.ExcelException;
import com.midai.framework.common.ExcelUtil;
import com.midai.framework.common.po.ResultVal;
import com.midai.framework.query.PageVo;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.device.entity.BoDeviceInstorage;
import com.midai.pay.device.query.BoAgentDeviceQuery;
import com.midai.pay.device.query.BoDeviceExcelQuery;
import com.midai.pay.device.query.BoDeviceQuery;
import com.midai.pay.device.service.BoDeviceService;
import com.midai.pay.device.vo.*;
import com.midai.pay.web.vo.device.DeviceIostorageCheckResult;
import com.midai.pay.web.vo.device.DeviceOperateVo;
import com.midai.pay.web.vo.device.SingleInstorageCheckVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;

@Api("设备管理")
@RestController
@RequestMapping("/device")
public class DeviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    private static final Integer RECEIPT_TYPE_OUTSTORAGE=2; /* 单据类型-出库 */

    @Reference
    private BoDeviceService boDeviceService;
    @Reference
    private AgentService agentService;
    @Reference
    private BoCustomerService customerService;

    @ApiOperation("构建设配入库批次")
    @GetMapping("/receiptno")
    public DeviceOperateVo buildBatchNo(@ApiParam("单据类型1 入库 2 出库 3变更") @RequestParam Integer receiptType){
        DeviceOperateVo operateV=new DeviceOperateVo();
        String batchNo=boDeviceService.buildBatchNo(receiptType);
        operateV.setBatchNo(batchNo);
        if(receiptType==RECEIPT_TYPE_OUTSTORAGE){
        	String userName=((UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal()).getUsername();
            Agent agent=agentService.fetchAgentByUserName(userName);
            operateV.setAgentId( (agent!=null) ? agent.getAgentNo() : null);
            operateV.setAgentName( (agent!=null) ? agent.getName() : null);
            
            //暂不用: 根据不同登录用户获取原代理商
//            operateV.setAgentList(agentService.findAgentsByUserName(userName));
        }
        return operateV;
    }

    @ApiOperation("单个设备入库检查")
    @PostMapping("/instorage/singlecheck")
    public SingleInstorageCheckVo singleInstorageCheck(@RequestBody SingleInstorageCheckVo instorageDetail){
        if(instorageDetail==null){
            throw new RuntimeException("单个设备入库参数非法,值instorageDetail为"+instorageDetail);
        }

        SingleInstorageCheckVo resultParam=new SingleInstorageCheckVo();
        ResultVal<DeviceInstorageDetailVo> resultObj=boDeviceService.singleInstorageCheck(instorageDetail.getInstorageDetail(), instorageDetail.getBeforeBodyNos());
        if(resultObj.isSuccess()){
            resultParam.setInstorageDetail(resultObj.getValue());
            resultParam.setResult("SUCCESS");
            return resultParam;
        }
        resultParam.setResult("FAIL");
        resultParam.setErrorMsg(resultObj.getMsg());
        return resultParam;
    }

    @ApiOperation("设备入库检查")
    @PostMapping("/instorage/check")
    public DeviceIostorageCheckResult instorageCheck(@Valid @RequestBody DeviceInstorageVo instorageVo){
        if(instorageVo.getNum()==null&&instorageVo.getNum()<=0){
            throw new RuntimeException("添加记录数量值必须大于0,值num="+instorageVo.getNum());
        }

        DeviceIostorageCheckResult checkResult=new DeviceIostorageCheckResult();
        BoDeviceInstorage deviceInstorageM=new BoDeviceInstorage();
        BeanUtils.copyProperties(instorageVo, deviceInstorageM);
        ResultVal<List<DeviceInstorageDetailVo>> resultObj=boDeviceService.instorageCheck(instorageVo.getBodyNoStart(), deviceInstorageM, instorageVo.getBeforeBodyNos());
        if(resultObj.isSuccess()){
            checkResult.setResult(DeviceIostorageCheckResult.ResultType.SUCCESS.toString());
            checkResult.setSuccessResult(resultObj.getValue());
        }else{
            checkResult.setResult(DeviceIostorageCheckResult.ResultType.FAIL.toString());
            checkResult.setExistsResult(resultObj.getExistsList());
            checkResult.setErrorMsg(resultObj.getMsg());
        }
        return checkResult;
    }

    @ApiOperation("设备入库")
    @PostMapping("/instorage")
    public String instorage(@Valid @RequestBody DeviceInstorageVo instorageVo){
        if(instorageVo.getDeviceDetailList().isEmpty()){
            throw new RuntimeException("设备入库不允许提交，请添加入库明细！");
        }
        String userName=((UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal()).getUsername();
        boDeviceService.instorage(instorageVo,userName);
        return "SUCCESS";
    }

    @ApiOperation("设备出库")
    @PostMapping("/outdevice")
    public String outstorage(@Valid @RequestBody DeviceOutstorageVo outstorageVo){
        if(outstorageVo.getOutstorageDetailList().isEmpty()){
            throw new RuntimeException("设备出库不允许提交，请添加出库明细！");
        }
        String userName=((UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal()).getUsername();
        boDeviceService.outstorage(outstorageVo,userName);
        return "SUCCESS";
    }

    @ApiOperation("设备变更")
    @PostMapping("/changedevice")
    public String changedevice(@Valid @RequestBody DeviceOutstorageVo outstorageVo){
        if(outstorageVo.getOutstorageDetailList().isEmpty()){
            throw new RuntimeException("设备变更不允许提交，请添加出库明细！");
        }
        String userName=((UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal()).getUsername();
        boDeviceService.changeDevice(outstorageVo, userName);
        return "SUCCESS";
    }

    @ApiOperation("代理商库存设备列表")
    @PostMapping("/storage/list")
    public PageVo<DeviceDetailVo> inventorylist(@RequestBody BoAgentDeviceQuery query){
        if(query==null){
            throw new RuntimeException("代理商库存设备列表查询参数非法：值为:"+query);
        }

        String userName=((UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal()).getUsername();
        return boDeviceService.paginateAgentInventoryDevice(query,userName);
    }

    @ApiOperation("代理商库存未绑商户设备列表")
    @PostMapping("/storage/unbind/list")
    public PageVo<DeviceDetailVo> inventoryUnbindList(@RequestBody BoAgentDeviceQuery query){
        if(query==null){
            throw new RuntimeException("代理商库存设备列表查询参数非法：值为:"+query);
        }
        String userName=((UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal()).getUsername();
        return boDeviceService.paginateAgentInventoryUnbindDevice(query,userName);
    }

    @ApiOperation("代理商设备列表")
    @PostMapping("/agent/list")
    public PageVo<DeviceDetailVo> deviceAgentList(@RequestBody BoDeviceQuery query){
        if(query==null){
            throw new RuntimeException("代理商设备列表查询参数非法：值为:"+query);
        }

        String userName=((UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal()).getUsername();
        return boDeviceService.paginateAgentDevice(query, userName);
    }

    @ApiOperation("变更设备选取列表")
    @PostMapping("/list")
    public PageVo<DeviceDetailVo> deviceList(@RequestBody BoAgentDeviceQuery query){
        if(query==null){
            throw new RuntimeException("变更设备选取列表查询参数非法：值为:"+query);
        }
        return boDeviceService.paginateDevice(query);
    }

    @ApiOperation("设备历史绑定商户")
    @GetMapping("/hirstory/bund")
    public List<BoIostorageCustomerVo> findByDeviceNo(@RequestParam String deviceNo){
        if(StringUtils.isBlank(deviceNo)){
            throw new RuntimeException("设备编号参数非法：值为:"+deviceNo);
        }

        return customerService.findByDeviceNo(deviceNo);
    }

    @ApiOperation("代理商设备列表信息统计")
    @PostMapping("/excelExportCount")
    public String excelExportDeviceDetailsCount(@RequestBody BoDeviceExcelQuery query){
        if(query==null){
            throw new RuntimeException("代理商设备列表导出查询参数非法：值为:"+query);
        }

        String userName=((UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal()).getUsername();
        int count=boDeviceService.excelExportDeviceDetailsCount(query,userName);
        if(count<=0){
            return "代理商设备信息为空";
        }
        return "SUCCESS";
    }

    @ApiOperation("代理商设备列表信息导出")
    @GetMapping("/excelExport")
    public void excelExportDeviceDetails(BoDeviceQuery query,HttpServletResponse response,HttpServletRequest request){
        if(query==null){
            throw new RuntimeException("代理商设备列表导出查询参数非法：值为:"+query);
        }

        String userName=((UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal()).getUsername();
        List<DeviceDetailExcelExportVo> resultList=boDeviceService.excelExportDeviceDetails(query,userName);
        if(resultList==null||resultList.isEmpty()){
            throw new RuntimeException("代理商设备信息为空");
        }
        try {
            ExcelUtil.listToExcel(resultList, getFieldMap(), "商户信息表", response, null);
            LOGGER.info("导出代理商设备信息成功");
        } catch (ExcelException e) {
            LOGGER.info("导出代理商设备信息失败");
            e.printStackTrace();
        }
    }

    @ApiOperation("代理商设备选取设备信息导出")
    @GetMapping("/excelExport/selected")
    public void excelExportDeviceDetails(HttpServletResponse response,HttpServletRequest request,@RequestParam("deviceNos")List<String> deviceNos){
        if(deviceNos==null||deviceNos.size()<=0){
            throw new RuntimeException("代理商选取设备导出查询参数非法：值为:"+deviceNos);
        }

        List<DeviceDetailExcelExportVo> resultList=boDeviceService.excelExportSelectedDeviceDetails(deviceNos);
        if(resultList==null||resultList.isEmpty()){
            throw new RuntimeException("代理商设备信息不存在");
        }
        try {
            ExcelUtil.listToExcel(resultList, getFieldMap(), "商户信息表", response, null);
            LOGGER.info("导出代理商设备信息成功");
        } catch (ExcelException e) {
            LOGGER.info("导出代理商设备信息失败");
            e.printStackTrace();
        }
    }

    private LinkedHashMap<String, String> getFieldMap() {
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("bodyNo", "机身号");
        fieldMap.put("modeName", "型号");
        fieldMap.put("typeName", "类型");
        fieldMap.put("simNo", "sim卡");
        fieldMap.put("mercNo", "商户编号");
        fieldMap.put("mobile", "手机号");
        fieldMap.put("mercName", "商户名称");
        fieldMap.put("mercId", "小票号");
        fieldMap.put("agentId", "直属代理商编号");
        fieldMap.put("agentName", "直属代理商名称");
        fieldMap.put("topagentId", "顶级代理商编号");
        fieldMap.put("topagentName", "顶级代理商名称");
        fieldMap.put("bindState", "绑定状态");
        fieldMap.put("deviceStatus", "终端状态");
        return fieldMap;
    }

}
