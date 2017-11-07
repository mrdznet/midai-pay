package com.midai.pay.web.controller.customer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.common.ExcelException;
import com.midai.framework.common.ExcelUtil;
import com.midai.framework.query.PageVo;
import com.midai.pay.common.po.TasksInfo;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerDevice;
import com.midai.pay.customer.entity.BoCustomerReview;
import com.midai.pay.customer.query.BoCustomerCountQuery;
import com.midai.pay.customer.query.BoCustomerQuery;
import com.midai.pay.customer.query.CustomerApplyQuery;
import com.midai.pay.customer.query.CustomerPendingTaskQuery;
import com.midai.pay.customer.service.BoCustomerDeviceService;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.customer.vo.BoCustomerAddVo;
import com.midai.pay.customer.vo.BoCustomerVo;
import com.midai.pay.customer.vo.CustomerApplyVo;
import com.midai.pay.customer.vo.CustomerFirstReviewVo;
import com.midai.pay.customer.vo.CustomerSecondReviewVo;
import com.midai.pay.device.vo.DeviceDetailVo;
import com.midai.pay.qrcode.service.BoAgentQrCodeCustomerService;
import com.midai.pay.qrcode.vo.AgentQrCodeCustomerVo;
import com.midai.pay.user.service.SystemUserService;
import com.midai.pay.web.AliyunOSS.service.OssService;
import com.midai.pay.web.AliyunOSS.service.vo.OssZipDirVo;
import com.midai.pay.web.vo.device.CustomerDeviceVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

/**
 * ClassName:User <br/>
 * Function: 商户管理  <br/>
 * Date: 2016年9月21日 <br/>
 * 
 * @author cjy
 * @version
 * @since JDK 1.7
 * @see
 */
@Api("商户管理")
@RestController
@RequestMapping("/customer")
public class BoCustomerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoCustomerController.class);
	@Reference
	private BoCustomerService boCustomerService;
	@Reference
	private BoCustomerDeviceService boCustomerDeviceService;
	@Reference
	private SystemUserService systemUserService;
	@Reference
	private BoAgentQrCodeCustomerService agentQrCodeCustomerService;
	@Reference
	private OssService ossService;
	
	@ApiOperation("商户申请列表")
    @PostMapping("/applyList")
	public PageVo<CustomerApplyVo> applyQuery(CustomerApplyQuery query){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		PageVo<CustomerApplyVo> vo = new PageVo<CustomerApplyVo>();
		vo.setRows(boCustomerService.applyQuery(query));
		vo.setTotal(boCustomerService.applyCount(query));
		
		return vo;
	}
	
	@ApiOperation("加载初审信息")
    @PostMapping("/loadFirstReview/{mercNo}")
	public CustomerFirstReviewVo loadFirstReview(@PathVariable("mercNo") @ApiParam("商户编号") String mercNo){
		if(StringUtils.isEmpty(mercNo)){
			throw new RuntimeException("请传入商户号");
		}
		return boCustomerService.loadFirstReview(mercNo);
	}
	
	@ApiOperation("初审信息保存")
    @PostMapping("/saveFirstReview")
	public int saveFirstReview(@RequestBody CustomerFirstReviewVo vo, BindingResult result){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		
		return boCustomerService.updateFirstReview(vo, userDetails.getUsername());
	}
	
	@ApiOperation("加载风控审信息")
    @PostMapping("/loadSecondReview/{mercNo}")
	public CustomerSecondReviewVo loadSecondReview(@PathVariable("mercNo") @ApiParam("商户编号") String mercNo){
		if(StringUtils.isEmpty(mercNo)){
			throw new RuntimeException("请传入商户号");
		}
		return boCustomerService.loadSecondReview(mercNo);
	}
	
	@ApiOperation("风控审信息保存")
    @PostMapping("/saveSecondReview")
	public int saveSecondReview(@RequestBody BoCustomerReview vo, BindingResult result){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		
		return boCustomerService.updateSecondReview(vo, userDetails.getUsername());
	}
	
	@ApiOperation("查看商户二维码")
    @PostMapping("/showCustomerQrCode/{mercNo}")
	public JSONObject queryCustomerQrCode(@PathVariable("mercNo") @ApiParam("商户编号") String mercNo) {
		BoCustomer bc = new BoCustomer();
		bc.setMercNo(mercNo);
		bc = boCustomerService.findOne(bc);
		String imgPath = bc.getQrCodeAddr();
		if(StringUtils.isEmpty(imgPath)) {
			imgPath = boCustomerService.createCustomerQrCode(mercNo, bc.getMercName());
		} 

		JSONObject jsonObject = new JSONObject();
		if(!StringUtils.isEmpty(imgPath)) {
    		jsonObject.put("code","00");
    		jsonObject.put("imgpath", imgPath);
    	} else {
    		jsonObject.put("code","99");
    	}
		return jsonObject;
	}
	
	@ApiOperation("商户申请")
	@PostMapping("/save")
	public int save(@Valid @RequestBody BoCustomerAddVo vo, BindingResult result) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int id=systemUserService.loadByUserLoginname(userDetails.getUsername()).getId();
		return boCustomerService.insertBoCustomer(vo, userDetails.getUsername(),id);
	}

	@ApiOperation("商户查询")
	@PostMapping("/list")
	public PageVo<BoCustomerVo> list(@RequestBody BoCustomerQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		PageVo<BoCustomerVo> pagevo = new PageVo<BoCustomerVo>();
		pagevo.setRows(boCustomerService.findQueryBoCustomer(query));
		pagevo.setTotal(boCustomerService.findQueryBoCustomerCount(query));
		return pagevo;
	}
	
	@ApiOperation("商户更新前的查询")
	@GetMapping("/find/{mercNo}")
	public CustomerSecondReviewVo findByBoCustomer(@PathVariable("mercNo") @ApiParam("商户编号") String mercNo)
	{
		return boCustomerService.loadByBoCustomer(mercNo);
	}

	@ApiOperation("商户信息更新")
	@PostMapping("/update")
	public int Update(@RequestBody CustomerFirstReviewVo vo) {

		return boCustomerService.update(vo);
	}
	
	@ApiOperation("设备新增")
	@PostMapping("/insert")
	public DeviceDetailVo insert(@RequestBody DeviceDetailVo vo)
	{  
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		int id=systemUserService.loadByUserLoginname(userDetails.getUsername()).getId();
		List<BoCustomerDevice> boCustomerDevice =boCustomerDeviceService.getByMercNo(vo.getCustomerId());
		int num=0;
		for (BoCustomerDevice bo : boCustomerDevice) {
			if (bo.getIsFirst()==1) {
				num+=1;
			}
		}
		if (num == 0) {
			vo.setIsFirst(1);
		}
		return boCustomerDeviceService.insert(vo.getDeviceNo(),vo.getCustomerId(),vo.getIsFirst(),vo.getTypeName(),vo.getModeName(),id);
	}
	
	@ApiOperation("设备删除")
	@PostMapping("/batchDelete/{mercNo}")
	public int batchDelete(@PathVariable("mercNo") @ApiParam("商户编号")String mercNo,@RequestBody String[] bodyNos)
	{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		int id=systemUserService.loadByUserLoginname(userDetails.getUsername()).getId();
		
		// 删除设备
		boCustomerDeviceService.batchDelete(mercNo, bodyNos,id);
		
		//判断已绑定设备是否有首选项, 如无则选择一个设备为首选项
		List<BoCustomerDevice> exitDevice = boCustomerDeviceService.getByMercNo(mercNo);
		if(exitDevice.size() > 0){
			boolean hasFirst = false;
			String firstBodyNo = null;
			
			for(BoCustomerDevice device : exitDevice){
				firstBodyNo = device.getBodyNo();
				
				if(device.getIsFirst().equals(1)) hasFirst = true;
			}
			
			if(!hasFirst){ //没有首选项则选择一个为首选项
				boCustomerDeviceService.updateIsFirst(mercNo, firstBodyNo);
			}
		}
		
		return 1;
	}
	
	@ApiOperation("商户excel导出总记录数")
	@PostMapping("/businessStatisticsCount")
	public int businessStatisticsCount(@RequestBody BoCustomerCountQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		query.setLoginName(userDetails.getUsername());
		
		int result = boCustomerService.ExcelDownloadBoCustomerCount(query);
		if (result > 0) {
			LOGGER.info("导出的商户表有记录数");
		} else {
			LOGGER.info("导出商户表的数据为空");
		}

		return result;
	}

	@ApiOperation("商户excel导出")
	@GetMapping("/excelExport")
	public void businessStatisticsExcelExport(HttpServletRequest request,
			HttpServletResponse response, BoCustomerQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		query.setLoginName(userDetails.getUsername());
		
		List<BoCustomerVo> list = boCustomerService.ExcelDownloadBoCustomer(query);
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("商户信息表为空");
		}
		try {
			ExcelUtil.listToExcel(list, getFieldMap(), "商户信息表", response, null);
			LOGGER.info("导出商户表成功");
		} catch (ExcelException e) {
			LOGGER.info("导出商户表失败");
			e.printStackTrace();
		}

	}

	private LinkedHashMap<String, String> getFieldMap() {
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("mercName", "商户名称");
		fieldMap.put("mercNo", "商户编号");
		fieldMap.put("mercId", "小票号");
//		fieldMap.put("mobile", "电话号码");
//		fieldMap.put("idCard", "身份证号");
//		fieldMap.put("address", "地址");
		fieldMap.put("accountName", "开户名");
		fieldMap.put("accountNo", "开户账号");
		fieldMap.put("branchBankName", "开户行");
//		fieldMap.put("createTime", "创建时间");
//		fieldMap.put("merlinkman", "商户联系人");
//		fieldMap.put("agentId", "代理商编号");
		fieldMap.put("merAutoStr", "商户类型");
		fieldMap.put("agentName", "代理商名称");
//		fieldMap.put("topAgentId", "顶级代理商编号");
//		fieldMap.put("topAgentName", "顶级代理商名称");
//		fieldMap.put("peovinceId", "省");
//		fieldMap.put("cityId", "市");
		fieldMap.put("statestr", "商户状态");
		fieldMap.put("firstInstancePerson", "初审人员");
		fieldMap.put("windControlPerson", "风控人员");
		return fieldMap;
	}
	
	@ApiOperation("加载商户设备")
    @PostMapping("/loadCustomerDevice")
	public List<DeviceDetailVo> loadCustomerDevice(@RequestBody CustomerDeviceVo vo){
		if(null==vo || StringUtils.isEmpty(vo.getMercNo())){
			throw new RuntimeException("请传入商户号");
		}
		return boCustomerService.loadCustomerDevice(vo.getMercNo());
	}
	
	@ApiOperation("检验手机唯一性")
	@GetMapping("/selectbymobile/{mobile}")
	public int selectBymobile(@PathVariable("mobile") @ApiParam("商户手机号")  String mobile)
	{
		if (mobile==null) {
			throw new RuntimeException("请传入商户手机号");
		}
		return boCustomerService.selectBymobile(mobile);
	}
	
	@ApiOperation("商户修改时检验手机唯一性")
	@GetMapping("/updatebymobile/{mobile}/{mercNo}")
	public String updatebymobile(@PathVariable("mobile") @ApiParam("商户手机号")  String mobile,@PathVariable("mercNo") @ApiParam("商户编号")  String mercNo)
	{
		if (mobile==null) {
			throw new RuntimeException("请传入商户手机号");
		}
		return boCustomerService.updatebymobile(mobile,mercNo);
	}
	
	
	@SuppressWarnings("unchecked")
	@ApiOperation("待办事项")
	@PostMapping("/dolist")
	public PageVo<TasksInfo> dolist(@RequestBody CustomerPendingTaskQuery query) 
	{ 
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		PageVo<TasksInfo> vo=new PageVo<TasksInfo>();
		
		CustomerPendingTaskQuery taskQuery = boCustomerService.dolist(query, userDetails.getUsername());
		
		vo.setRows((List<TasksInfo>) taskQuery.getResult());
		
		vo.setTotal(taskQuery.getTotal());
		
		return vo;
	}
	
	@ApiOperation("代理商用户登录商户查询")
	@PostMapping("/agentlist")
	public PageVo<BoCustomerVo> agentlist(@RequestBody BoCustomerQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String allAgents = systemUserService.getAllChildAgents(userDetails.getUsername());
	    query.setAllAgents(allAgents);
	    query.setLoginName(userDetails.getUsername());
	    
		PageVo<BoCustomerVo> pagevo = new PageVo<BoCustomerVo>();
		pagevo.setRows(boCustomerService.findQueryBoCustomer(query));
		pagevo.setTotal(boCustomerService.findQueryBoCustomerCount(query));
		return pagevo;
	}
	
	
	
	@ApiOperation("代理商用户登录商户excel导出总记录数")
	@PostMapping("/agentlistExcelExportCount")
	public int agentlistExcelExportCount(@RequestBody BoCustomerCountQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String allAgents = systemUserService.getAllChildAgents(userDetails.getUsername());
	    query.setAllAgents(allAgents);
	    query.setLoginName(userDetails.getUsername());
	    
		int result = boCustomerService.ExcelDownloadBoCustomerCount(query);
		if (result > 0) {
			LOGGER.info("导出的商户表有记录数");
		} else {
			LOGGER.info("导出商户表的数据为空");
		}

		return result;
	}

	@ApiOperation("代理商用户登录商户excel导出")
	@GetMapping("/agentlistExcelExport")
	public void agentlistExcelExport(HttpServletRequest request,
			HttpServletResponse response, BoCustomerQuery query) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String allAgents = systemUserService.getAllChildAgents(userDetails.getUsername());
	    query.setAllAgents(allAgents);
	    query.setLoginName(userDetails.getUsername());
	    
		List<BoCustomerVo> list = boCustomerService.ExcelDownloadBoCustomer(query);
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("商户信息表为空");
		}
		try {
			ExcelUtil.listToExcel(list, getFieldMap(), "商户信息表", response, null);
			LOGGER.info("导出商户表成功");
		} catch (ExcelException e) {
			LOGGER.info("导出商户表失败");
			e.printStackTrace();
		}

	}

	@ApiOperation(value="商户二维码多文件打包下载")
	@RequestMapping(path="qr-oss-zip-file",method={RequestMethod.POST})
	public OssZipDirVo qrZipFile(@ApiParam(value="商户编号") @RequestBody List<String> customerKeys) {
		
		if(customerKeys.isEmpty()) {
			throw new RuntimeException("商户编号不能为空");
		}
		
		Random r = new Random();
		String fileName = "" + System.currentTimeMillis() + r.nextInt(1000)
				+ ".zip";
		String zipPath = null;
		String tmp = "tmp" + System.currentTimeMillis() + r.nextInt(1000);
		
		StringBuffer sql = new StringBuffer();
		for(String key : customerKeys) {
			
			sql.append("'" + key + "'").append(",");
		}
		
		sql.setLength(sql.length() - 1);
		List<AgentQrCodeCustomerVo> list = agentQrCodeCustomerService.queryByCustomerNos(sql.toString());
		
		List<String> listPath = new ArrayList<>();

		for(AgentQrCodeCustomerVo aq : list) {
			listPath.add(aq.getQrcodePath());
		}
		zipPath = ossService.downLoadFileToZip(listPath, tmp, fileName);
		
		OssZipDirVo vo=new OssZipDirVo();
		vo.setZipPath(zipPath);
		return vo;
	} 
	
}
