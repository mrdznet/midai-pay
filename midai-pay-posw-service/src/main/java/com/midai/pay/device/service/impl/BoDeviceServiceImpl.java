package com.midai.pay.device.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.common.po.ResultVal;
import com.midai.framework.query.PageVo;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.mapper.AgentMapper;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.common.constants.Constants;
import com.midai.pay.common.utils.ServiceUtils;
import com.midai.pay.customer.entity.BoCustomerDevice;
import com.midai.pay.customer.service.BoCustomerDeviceService;
import com.midai.pay.device.entity.BoAgentDevice;
import com.midai.pay.device.entity.BoDevice;
import com.midai.pay.device.entity.BoDeviceInstorage;
import com.midai.pay.device.entity.BoDeviceOperateRecord;
import com.midai.pay.device.entity.BoDeviceOutstorage;
import com.midai.pay.device.mapper.BoAgentDeviceMapper;
import com.midai.pay.device.mapper.BoDeviceInstorageMapper;
import com.midai.pay.device.mapper.BoDeviceMapper;
import com.midai.pay.device.mapper.BoDeviceOperateRecordMapper;
import com.midai.pay.device.mapper.BoDeviceOutstorageMapper;
import com.midai.pay.device.query.BoAgentDeviceQuery;
import com.midai.pay.device.query.BoDeviceExcelQuery;
import com.midai.pay.device.query.BoDeviceQuery;
import com.midai.pay.device.service.BoDeviceService;
import com.midai.pay.device.service.BoIostorageService;
import com.midai.pay.device.vo.DeviceDetailExcelExportVo;
import com.midai.pay.device.vo.DeviceDetailVo;
import com.midai.pay.device.vo.DeviceInstorageDetailVo;
import com.midai.pay.device.vo.DeviceInstorageVo;
import com.midai.pay.device.vo.DeviceOutstorageDetailVo;
import com.midai.pay.device.vo.DeviceOutstorageVo;
import com.midai.pay.user.mapper.SystemUserMapper;
import com.midai.pay.user.service.SystemOrgUserForAgentService;
import com.midai.pay.user.service.SystemUserService;

import tk.mybatis.mapper.entity.Example;

@Service
public class BoDeviceServiceImpl extends BaseServiceImpl<BoDevice> implements BoDeviceService {

    private final BoDeviceMapper deviceMapper;

    @Autowired
    private BoDeviceInstorageMapper deviceInstorageMapper;
    @Autowired
    private BoAgentDeviceMapper agentDeviceMapper;
    @Autowired
    private BoDeviceOperateRecordMapper deviceOperateRecordMapper;
    @Autowired
    private BoDeviceOutstorageMapper deviceOutstorageMapper;
    @Autowired
    private SystemOrgUserForAgentService userForAgentService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private BoCustomerDeviceService boCustomerDeviceService;
    @Autowired
    private SystemUserService  systemUserService;
    @Reference
    private BoIostorageService bos;
    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private AgentMapper agentMapper;
    
    public BoDeviceServiceImpl(BoDeviceMapper mapper) {
        super(mapper);
        this.deviceMapper=mapper;
    }

    @Override
    public String buildBatchNo(Integer receiptType) {
        String currentDate = new SimpleDateFormat("yyMMdd").format(new Date());
        int number=0;
        String prefix="";
        if(receiptType==1){
            prefix="RK-";
            number = deviceMapper.findCountInstorageBatch();
        }else if(receiptType==2){
            prefix="CK-";
            number = deviceMapper.findCountOutstorageBatch();
        }else if(receiptType==3){
            prefix="BG-";
            number = deviceMapper.findCountChangeBatch();
        }else{
            throw new RuntimeException("{\"errorMsg\":\"单据类型不存在,操作终止！\"}");
        }
        String increment = String.format("%03d", number);
        return prefix + currentDate + "-" +increment;
    }

    @Override
    public ResultVal<List<DeviceInstorageDetailVo>> instorageCheck(String bodyNoStart,BoDeviceInstorage instorageM,String beforeBodyNos) {
        List<DeviceInstorageDetailVo> deviceList=new ArrayList<DeviceInstorageDetailVo>();
        List<String> deviceNoList=ServiceUtils.buildDeviceNo(bodyNoStart,""+instorageM.getNum());

        DeviceInstorageDetailVo device=null;
        StringBuffer deviceNoStr=new StringBuffer("");
        StringBuffer simNoStr=new StringBuffer("");
        for (int i=0;i<instorageM.getNum();i++){
            device=new DeviceInstorageDetailVo();
            device.setDeviceStatus("启用");
            String deviceNo=deviceNoList.get(i);
            device.setSimNo(deviceNo);
            simNoStr.append("'"+deviceNo+"',");
            if(StringUtils.equals("支付通",instorageM.getFactoryName())&&StringUtils.equals("刷卡器",instorageM.getDeviceTypeName())){
                deviceNo="0000"+deviceNo;
            }
            device.setDeviceNo(deviceNo);
            deviceNoStr.append("'" + deviceNo + "',");
            device.setCreateTime(new Date());
            deviceList.add(device);
        }

        deviceNoStr.setLength(deviceNoStr.length()-1);
        simNoStr.setLength(simNoStr.length()-1);
        List<String> existsDeviceNoList=deviceMapper.fetchByDeviceNos(deviceNoStr.toString());
        if(existsDeviceNoList!=null&&!existsDeviceNoList.isEmpty()){
            return ResultVal.FAIL("设备已入库",existsDeviceNoList);
        }
        List<String> existsSimNoList=deviceMapper.fetchBySimNos(simNoStr.toString());
        if(existsSimNoList!=null&&!existsSimNoList.isEmpty()){
            return ResultVal.FAIL("SIM卡号已存在",existsSimNoList);
        }
        if(StringUtils.isNotBlank(beforeBodyNos)){
            List<String> currDeviceNoList=Arrays.asList(beforeBodyNos.split(","));
            List<String> existsSameDeviceNoList=new ArrayList<String>();
            for (DeviceInstorageDetailVo newDevice:deviceList){
                if(currDeviceNoList.contains(newDevice.getDeviceNo())){
                    existsSameDeviceNoList.add(newDevice.getDeviceNo());
                }
            }
            if(!existsSameDeviceNoList.isEmpty()){
                return ResultVal.FAIL("终端存在重复数据",existsSameDeviceNoList);
            }

        }
        return ResultVal.SUCCESS(deviceList);
    }

    @Override
    public ResultVal<DeviceInstorageDetailVo> singleInstorageCheck(DeviceInstorageDetailVo instorageDetailM, String beforeBodyNos) {
        //入库单条效验
        instorageDetailM.setCreateTime(new Date());
        List<String> existsDeviceNoList=deviceMapper.fetchByDeviceNos("'"+instorageDetailM.getDeviceNo()+"'");
        if(existsDeviceNoList!=null&&!existsDeviceNoList.isEmpty()){
            return ResultVal.FAIL("设备已入库",existsDeviceNoList);
        }
        List<String> existsSimNoList=deviceMapper.fetchBySimNos("'"+instorageDetailM.getSimNo()+"'");
        if(existsSimNoList!=null&&!existsSimNoList.isEmpty()){
            return ResultVal.FAIL("SIM卡号已存在",existsSimNoList);
        }
        if(StringUtils.isNotBlank(beforeBodyNos)){
            List<String> currDeviceNoList=Arrays.asList(beforeBodyNos.split(","));
            List<String> existsSameDeviceNoList=new ArrayList<String>();
            if(currDeviceNoList.contains(instorageDetailM.getDeviceNo())){
                existsSameDeviceNoList.add(instorageDetailM.getDeviceNo());
            }
            if(!existsSameDeviceNoList.isEmpty()){
                return ResultVal.FAIL("终端存在重复数据",existsSameDeviceNoList);
            }

        }
        return ResultVal.SUCCESS(instorageDetailM);
    }

    @Override
    @Transactional(propagation=Propagation.NESTED)
    public int instorage(DeviceInstorageVo instorageVo,String userName) {
        //设备入库-保存
        Map<String,List<BoAgentDevice>> batchInsertDeviceAgent=new HashMap<String,List<BoAgentDevice>>();
        List<BoAgentDevice> agentDeviceList=new ArrayList<BoAgentDevice>();

        Map<String,List<BoDevice>> batchInsertDevice=new HashMap<String,List<BoDevice>>();
        BoDeviceInstorage instorageM=new BoDeviceInstorage();
        List<BoDevice> deviceList=new ArrayList<BoDevice>();
        BeanUtils.copyProperties(instorageVo, instorageM);
        instorageM.setCreateTime(new Date());
        instorageM.setAppUser(userName);

        //TODO 入库时做密钥判断-未写
        //TODO 入库批处理没有

        BoDevice device=null;
        BoAgentDevice agentDevice=null;
        List<String> deviceNos =  new ArrayList<String>();
        for (DeviceInstorageDetailVo instorageDetailV:instorageVo.getDeviceDetailList()){
            device=new BoDevice();
            BeanUtils.copyProperties(instorageDetailV, device);
            deviceNos.add(device.getDeviceNo());
            device.setState(Constants.DEVICE_NO_OPEN);
            device.setDeviceStatus("1");
            device.setRkNo(instorageM.getRkNo());
            device.setDeviceModeId(instorageM.getDeviceModeId());
            deviceList.add(device);

            agentDevice=new BoAgentDevice();
            agentDevice.setCreateTime(instorageDetailV.getCreateTime());
            agentDevice.setAgentId(instorageVo.getAgentId());
            agentDevice.setDeviceNo(instorageDetailV.getDeviceNo());
            //agentDevice.setIsInventory(Constants.IS_INVENTORY);
            agentDeviceList.add(agentDevice);
        }
        //批量添加数据
        batchInsertDevice.put("list",deviceList);
        if(deviceMapper.batchInsert(batchInsertDevice) > 0) {
        	bos.batchInsertBIo(deviceNos, userName, null, null, instorageVo.getAgentName(), instorageVo.getAgentId(), "入库");
        }
        batchInsertDeviceAgent.put("list",agentDeviceList);
        agentDeviceMapper.batchInsert(batchInsertDeviceAgent);
        instorageM.setNum(deviceList.size());
        return deviceInstorageMapper.insertSelective(instorageM);
    }

    @Override
    @Transactional(propagation=Propagation.NESTED)
    public void outstorage(DeviceOutstorageVo outstorageVo,String userName) {
        Map<String,List<BoAgentDevice>> batchInsertDeviceAgent=new HashMap<String,List<BoAgentDevice>>();
        List<BoAgentDevice> agentDeviceList=new ArrayList<BoAgentDevice>();

        Map<String,List<BoDeviceOperateRecord>> batchInsertDeviceOperateRecord=new HashMap<String,List<BoDeviceOperateRecord>>();
        List<BoDeviceOperateRecord> deviceOperateRecordList=new ArrayList<BoDeviceOperateRecord>();
        BoDeviceOutstorage outstorageM=new BoDeviceOutstorage();
        BeanUtils.copyProperties(outstorageVo, outstorageM);
        outstorageM.setAppUser(userName);
        outstorageM.setCreateTime(new Date());
        outstorageM.setOperateState(Constants.OPERATE_STATE_OUTSTORAGE);

        BoAgentDevice agentDevice=null;
        BoDeviceOperateRecord deviceOperateRecord=null;
        StringBuffer deviceNoStr=new StringBuffer("");
        List<String> deviceNos = new ArrayList<String>();	
        for (DeviceOutstorageDetailVo outstorageDetailV:outstorageVo.getOutstorageDetailList()){
            deviceOperateRecord=new BoDeviceOperateRecord();
            BeanUtils.copyProperties(outstorageDetailV, deviceOperateRecord);
            deviceOperateRecord.setBatchNo(outstorageVo.getCkNo());
            deviceOperateRecordList.add(deviceOperateRecord);

            agentDevice=new BoAgentDevice();
            agentDevice.setCreateTime(outstorageDetailV.getCreateTime());
            agentDevice.setAgentId(outstorageVo.getDestagentId());
            agentDevice.setDeviceNo(outstorageDetailV.getDeviceNo());
          //agentDevice.setIsInventory(Constants.IS_INVENTORY);
            agentDeviceList.add(agentDevice);
            deviceNoStr.append("'"+outstorageDetailV.getDeviceNo()+"',");
            deviceNos.add(outstorageDetailV.getDeviceNo());
        }
        batchInsertDeviceOperateRecord.put("list",deviceOperateRecordList);
        deviceOperateRecordMapper.batchInsert(batchInsertDeviceOperateRecord);
        deviceNoStr.setLength(deviceNoStr.length()-1);
        agentDeviceMapper.delByBodyNosAndAgentNo(deviceNoStr.toString(),outstorageVo.getAgentId());
        //出库前更新-源代理商库存
        //agentDeviceMapper.updateInventoryStateByBodyNos(deviceNoStr.toString(),outstorageVo.getAgentId());
        //添加代理商-设备关联信息
        batchInsertDeviceAgent.put("list",agentDeviceList);
        agentDeviceMapper.batchInsert(batchInsertDeviceAgent);
        deviceOutstorageMapper.insertSelective(outstorageM);
        bos.batchInsertBIo(deviceNos, userName, outstorageVo.getAgentName(), outstorageVo.getAgentId(), outstorageVo.getDestagentName(), outstorageVo.getDestagentId(), "出库");
    }

    @Override
    @Transactional
    public void changeDevice(DeviceOutstorageVo outstorageVo,String userName) {
        Map<String,List<BoAgentDevice>> batchInsertDeviceAgent=new HashMap<String,List<BoAgentDevice>>();
        List<BoAgentDevice> agentDeviceList=new ArrayList<BoAgentDevice>();

        Map<String,List<BoDeviceOperateRecord>> batchInsertDeviceOperateRecord=new HashMap<String,List<BoDeviceOperateRecord>>();
        List<BoDeviceOperateRecord> deviceOperateRecordList=new ArrayList<BoDeviceOperateRecord>();
        BoDeviceOutstorage outstorageM=new BoDeviceOutstorage();
        BeanUtils.copyProperties(outstorageVo, outstorageM);
        outstorageM.setAppUser(userName);
        outstorageM.setCreateTime(new Date());
        outstorageM.setOperateState(Constants.OPERATE_STATE_CHANGE);

        BoAgentDevice agentDevice=null;
        BoDeviceOperateRecord deviceOperateRecord=null;
        StringBuffer deviceNoStr=new StringBuffer("");
        List<String> deviceNos = new ArrayList<String>();
        for (DeviceOutstorageDetailVo outstorageDetailV:outstorageVo.getOutstorageDetailList()){
            outstorageDetailV.setCreateTime(new Date());

            deviceOperateRecord=new BoDeviceOperateRecord();
            BeanUtils.copyProperties(outstorageDetailV, deviceOperateRecord);
            deviceOperateRecord.setBatchNo(outstorageVo.getCkNo());
            deviceOperateRecordList.add(deviceOperateRecord);
            deviceNoStr.append("'"+outstorageDetailV.getDeviceNo()+"',");

            //目标代理商-所有的父代理商编号-添加数据
           /* List<String> parentAgentNoList=agentService.fetchAllParentAgentNo(outstorageVo.getDestagentId());
            for (String agentNo:parentAgentNoList){
                agentDevice=new BoAgentDevice();
                agentDevice.setDeviceNo(outstorageDetailV.getDeviceNo());
                agentDevice.setIsInventory(Constants.NOT_INVENTORY);
                agentDevice.setAgentId(agentNo);
                agentDevice.setCreateTime(outstorageDetailV.getCreateTime());
                agentDeviceList.add(agentDevice);
            }*/
            agentDevice=new BoAgentDevice();
            agentDevice.setDeviceNo(outstorageDetailV.getDeviceNo());
          //agentDevice.setIsInventory(Constants.IS_INVENTORY);
            agentDevice.setAgentId(outstorageVo.getDestagentId());
            agentDevice.setCreateTime(outstorageDetailV.getCreateTime());
            agentDeviceList.add(agentDevice);
            deviceNos.add(outstorageDetailV.getDeviceNo());
        }
        batchInsertDeviceOperateRecord.put("list",deviceOperateRecordList);
        deviceOperateRecordMapper.batchInsert(batchInsertDeviceOperateRecord);
        deviceNoStr.setLength(deviceNoStr.length()-1);
        agentDeviceMapper.delByBodyNo(deviceNoStr.toString());
       
        //设置商户设备信息
        List<BoCustomerDevice> bcdList = boCustomerDeviceService.getByodyNos(deviceNoStr.toString());
        if(bcdList != null && bcdList.size() > 0) {
        	boCustomerDeviceService.deleteBybodyNOs(deviceNoStr.toString());
        	
        	for(BoCustomerDevice bd : bcdList) {
        		
        		//判断已绑定设备是否有首选项, 如无则选择一个设备为首选项
        		List<BoCustomerDevice> exitDevice = boCustomerDeviceService.getByMercNo(bd.getMercNo());
        		if(exitDevice!= null && exitDevice.size() > 0){
        			boolean hasFirst = false;
        			String firstBodyNo = null;
        			
        			for(BoCustomerDevice device : exitDevice){
        				firstBodyNo = device.getBodyNo();
        				
        				if(device.getIsFirst().equals(1)) hasFirst = true;
        			}
        			
        			if(!hasFirst){ //没有首选项则选择一个为首选项
        				boCustomerDeviceService.updateIsFirst(bd.getMercNo(), firstBodyNo);
        			}
        		}
        		
        	}
        	
        }
        //设置设备和商户关系
        if(StringUtils.isNotEmpty(deviceNoStr.toString())) {
        	deviceMapper.bacthUpdateCustomerIdAndState(deviceNoStr.toString());
        }
		
        //添加代理商-设备关联信息
        batchInsertDeviceAgent.put("list",agentDeviceList);
        agentDeviceMapper.batchInsert(batchInsertDeviceAgent);
        deviceOutstorageMapper.insertSelective(outstorageM);
        bos.batchInsertBIo(deviceNos, userName, outstorageVo.getAgentName(), outstorageVo.getAgentId(), outstorageVo.getDestagentName(), outstorageVo.getDestagentId(), "变更");
    }

    @Override
    public PageVo<DeviceDetailVo> paginateAgentInventoryDevice(BoAgentDeviceQuery query,String userName) {
        PageVo<DeviceDetailVo> pageObj=new PageVo<DeviceDetailVo>();
        if(StringUtils.isNotBlank(query.getBodyNoStart())&&(query.getNum()!=null&&query.getNum()>0)){
            String bodyNoStart=query.getBodyNoStart();
            if(query.getBodyNoStart().startsWith("0000")){
                bodyNoStart=query.getBodyNoStart().substring(4);
            }
            List<String> bodyNosList=ServiceUtils.buildDeviceNo(bodyNoStart, "" + query.getNum());
            StringBuffer bodyNos=new StringBuffer("");
            for (String bodyNo:bodyNosList){
                if(query.getBodyNoStart().startsWith("0000")){
                    bodyNos.append("'0000"+bodyNo+"',");
                }else{
                    bodyNos.append("'"+bodyNo+"',");
                }
            }
            bodyNos.setLength(bodyNos.length()-1);
            query.setBodyNos(bodyNos.toString());
        }
        
        String agentNo = userForAgentService.getAgentCodeByLoginname(userName);
        if(StringUtils.isBlank(agentNo)){
			String inscode = systemUserMapper.getInscode(userName);
			
			if(StringUtils.isNotEmpty(inscode)){
				agentNo = agentMapper.findTopAgentNo(inscode);
			}
        }
        
        query.setAgentNo(agentNo);
        
        pageObj.setRows(deviceMapper.paginateAgentInventoryDevice(query));
        pageObj.setTotal(deviceMapper.paginateAgentInventoryDeviceCount(query));
        return pageObj;
    }

    @Override
    public PageVo<DeviceDetailVo> paginateAgentInventoryUnbindDevice(BoAgentDeviceQuery query,String username) {
    	Agent agent = agentService.fetchAgentByUserName(username);
    	if (agent == null || StringUtils.isEmpty(agent.getAgentNo())) {
    		if (StringUtils.isEmpty(query.getAgentNo())) {
				throw new RuntimeException("非代理商用户登录，代理商编号不能为空！");
			}
    	}else{
    		query.setAgentNo(agent.getAgentNo());
    	}
        PageVo<DeviceDetailVo> pageObj=new PageVo<DeviceDetailVo>();
        pageObj.setRows(deviceMapper.paginateAgentInventoryDevice(query));
        pageObj.setTotal(deviceMapper.paginateAgentInventoryDeviceCount(query));
        return pageObj;
    }

    @Override
    public PageVo<DeviceDetailVo> paginateAgentDevice(BoDeviceQuery query,String userName) {
        PageVo<DeviceDetailVo> pageObj=new PageVo<DeviceDetailVo>();
        String agentNo=userForAgentService.getAgentCodeByLoginname(userName);
        /*设备始和终*/
        if(StringUtils.isNotBlank(query.getDeviceNoStart()) && StringUtils.isNotBlank(query.getDeviceNoEnd())){
        	String  deviceBegine = query.getDeviceNoStart();
        	String  deviceEnd =query.getDeviceNoEnd();
        	
        	int index=-1;
	        char[] charArr=deviceBegine.toCharArray();
	        for (int i=0;i<charArr.length;i++){
	            if(Character.isLetter(charArr[i])){
	                index=i;
	            }
	        }
	        long deviceNoStartInt=Long.parseLong(deviceBegine.substring(index+1));
	        long deviceNoEndInt=Long.parseLong(deviceEnd.substring(index+1));
	        long deviceNoCount=deviceNoEndInt-deviceNoStartInt+1;
	        List<String> deviceNoList=ServiceUtils.buildDeviceNo(deviceBegine,""+deviceNoCount);
	        
	        StringBuffer str =new StringBuffer();
	        for(String deviceno:deviceNoList){
	        	str.append("'"+deviceno+"',");
	        }
	        str.setLength(str.length()-1);
	        query.setDeviceNobn(str.toString());
        }
        
        
        if (agentNo==null) {
        	String inscode = systemUserService.getInscode(userName);
        	if (StringUtils.isNotEmpty(inscode)) {
        		agentNo=agentService.findAllAgentNoByInscode(inscode);
			}
		}
       
        query.setAgentNo(agentNo);
        String agentNos = agentService.getAgentNoParam(query.getAgentNo());
       
        query.setAgentNo(agentNos);
        pageObj.setRows(deviceMapper.paginateAgentDevice(query));
        pageObj.setTotal(deviceMapper.paginateAgentDeviceCount(query));
        return pageObj;
    }

    @Override
    public PageVo<DeviceDetailVo> paginateDevice(BoAgentDeviceQuery query) {
        PageVo<DeviceDetailVo> pageObj=new PageVo<DeviceDetailVo>();
        if(StringUtils.isNotBlank(query.getBodyNoStart())&&(query.getNum()!=null&&query.getNum()>0)){
            String bodyNoStart=query.getBodyNoStart();
            if(query.getBodyNoStart().startsWith("0000")){
                bodyNoStart=query.getBodyNoStart().substring(4);
            }
            List<String> bodyNosList=ServiceUtils.buildDeviceNo(bodyNoStart, "" + query.getNum());
            StringBuffer bodyNos=new StringBuffer("");
            for (String bodyNo:bodyNosList){
                if(query.getBodyNoStart().startsWith("0000")){
                    bodyNos.append("'0000"+bodyNo+"',");
                }else{
                    bodyNos.append("'"+bodyNo+"',");
                }
            }
            bodyNos.setLength(bodyNos.length()-1);
            query.setBodyNos(bodyNos.toString());
        }
        String agentNos = agentService.getAgentNoParam(query.getAgentNo());
        query.setAgentNo(agentNos);
        pageObj.setRows(deviceMapper.paginateDevice(query));
        pageObj.setTotal(deviceMapper.paginateDeviceCount(query));
        return pageObj;
    }

    @Override
    public List<DeviceDetailExcelExportVo> excelExportDeviceDetails(BoDeviceQuery query,String userName){
    	  String agentNo=userForAgentService.getAgentCodeByLoginname(userName);
    	  
    	  if(StringUtils.isNotBlank(query.getDeviceNoStart()) && StringUtils.isNotBlank(query.getDeviceNoEnd())){
          	String  deviceBegine = query.getDeviceNoStart();
          	String  deviceEnd =query.getDeviceNoEnd();
          	
          	int index=-1;
  	        char[] charArr=deviceBegine.toCharArray();
  	        for (int i=0;i<charArr.length;i++){
  	            if(Character.isLetter(charArr[i])){
  	                index=i;
  	            }
  	        }
  	        long deviceNoStartInt=Long.parseLong(deviceBegine.substring(index+1));
  	        long deviceNoEndInt=Long.parseLong(deviceEnd.substring(index+1));
  	        long deviceNoCount=deviceNoEndInt-deviceNoStartInt+1;
  	        List<String> deviceNoList=ServiceUtils.buildDeviceNo(deviceBegine,""+deviceNoCount);
  	        
  	        StringBuffer str =new StringBuffer();
  	        for(String deviceno:deviceNoList){
  	        	str.append("'"+deviceno+"',");
  	        }
  	        str.setLength(str.length()-1);
  	        query.setDeviceNobn(str.toString());
          }
          
          if (agentNo==null) {
          	String inscode = systemUserService.getInscode(userName);
          	if (StringUtils.isNotEmpty(inscode)) {
          		agentNo=agentService.findAllAgentNoByInscode(inscode);
  			}
  		}
          else {
          	agentNo="'"+agentNo+"'";
          
          
  		}
          query.setAgentNo(agentNo);
        return deviceMapper.excelExportDeviceDetails(query);
    }

    @Override
    public int excelExportDeviceDetailsCount(BoDeviceExcelQuery query,String userName) {
    	  String agentNo=userForAgentService.getAgentCodeByLoginname(userName);
    	  
    	  if(StringUtils.isNotBlank(query.getDeviceNoStart()) && StringUtils.isNotBlank(query.getDeviceNoEnd())){
          	String  deviceBegine = query.getDeviceNoStart();
          	String  deviceEnd =query.getDeviceNoEnd();
          	
          	int index=-1;
  	        char[] charArr=deviceBegine.toCharArray();
  	        for (int i=0;i<charArr.length;i++){
  	            if(Character.isLetter(charArr[i])){
  	                index=i;
  	            }
  	        }
  	        long deviceNoStartInt=Long.parseLong(deviceBegine.substring(index+1));
  	        long deviceNoEndInt=Long.parseLong(deviceEnd.substring(index+1));
  	        long deviceNoCount=deviceNoEndInt-deviceNoStartInt+1;
  	        List<String> deviceNoList=ServiceUtils.buildDeviceNo(deviceBegine,""+deviceNoCount);
  	        
  	        StringBuffer str =new StringBuffer();
  	        for(String deviceno:deviceNoList){
  	        	str.append("'"+deviceno+"',");
  	        }
  	        str.setLength(str.length()-1);
  	        query.setDeviceNobn(str.toString());
          }
          
          if (agentNo==null) {
          	String inscode = systemUserService.getInscode(userName);
          	if (StringUtils.isNotEmpty(inscode)) {
          		agentNo=agentService.findAllAgentNoByInscode(inscode);
  			}
  		}
          else {
          	agentNo="'"+agentNo+"'";
          
          
  		}
          query.setAgentNo(agentNo);
        return deviceMapper.excelExportDeviceDetailsCount(query);
    }

    @Override
    public List<DeviceDetailExcelExportVo> excelExportSelectedDeviceDetails(List<String> deviceNos) {
        StringBuffer deviceNoStr=new StringBuffer("");
        for (String deviceNo:deviceNos){
            deviceNoStr.append("'"+deviceNo+"',");
        }
        deviceNoStr.setLength(deviceNoStr.length()-1);
        return deviceMapper.excelExportSelectedDeviceDetails(deviceNoStr.toString());
    }

	@Override
	public List<BoDevice>  getByDeviceNo(String deviceid) {
		Example example = new Example(BoDevice.class);
		example.createCriteria().andEqualTo("deviceNo", deviceid);
		return deviceMapper.selectByExample(example);
	}

}
