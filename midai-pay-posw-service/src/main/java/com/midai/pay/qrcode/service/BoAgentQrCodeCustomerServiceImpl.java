package com.midai.pay.qrcode.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.query.PageVo;
import com.midai.pay.common.util.CreateParseCode;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.oss.PayOssService;
import com.midai.pay.qrcode.entity.BoAgentQrCodeCustomerEntity;
import com.midai.pay.qrcode.mapper.BoAgentQrCodeCustomerMapper;
import com.midai.pay.qrcode.query.AgentQrCodeCustomerQuery;
import com.midai.pay.qrcode.vo.AgentQrCodeCustomerVo;

@Service
public class BoAgentQrCodeCustomerServiceImpl implements BoAgentQrCodeCustomerService {

	@Autowired
	private BoAgentQrCodeCustomerMapper mapper;
	
	@Autowired
	private PayOssService payOssService;
	
	@Autowired
	private BoCustomerService boCustomerService;
	
	@Value("${mifu.oss.customer-dir}")
	private String customerdir;

	@Value("${customer.qrcode.addr}")
	private String customerQrCodeAddr;
	
	@Override
	public PageVo<AgentQrCodeCustomerVo> paginateAgentQrCodeCustomer(AgentQrCodeCustomerQuery query) {
		PageVo<AgentQrCodeCustomerVo> pv = new PageVo<AgentQrCodeCustomerVo>();
		
		pv.setRows(mapper.paginateListAgentQrCodeCustomer(query));
		pv.setTotal(mapper.paginateCountAgentQrCodeCustomer(query));
		return pv;
	}

	@Override
	public Map<String, String> createBatchCustomerQrCodePath(String agentNo, Integer count) {
		
		// 文件名称
		SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmss");
		
		Map<String, String> map = new HashMap<String, String>();
		
		for(int i=0; i<count; i++) {
			
			Random random = new Random(); 
			int rannum = (int) (random.nextDouble() * (9999 - 1000 + 1)) + 1000;// 获取4位随机数  
			
			String randomStr = agentNo + "_" + i + rannum + "_" + df.format(new Date());
			
			String fileName = randomStr + ".png";
			String imgPath = "";
			String cqcaddr = customerQrCodeAddr;
			cqcaddr += "?qrcode=" + randomStr;
			ByteArrayOutputStream baos = CreateParseCode.createQRCodeWithLogWithStream(cqcaddr, "");
			
			// oss上传文件
			if (baos != null) {
				try {
					
					imgPath = payOssService.imgUpload(new ByteArrayInputStream(baos.toByteArray()), fileName, customerdir);
					map.put(randomStr, imgPath);
				} finally {
					if (baos != null) {
						try {
							baos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		}

	
		return map;
	}

	@Override
	public Boolean insert(String agentNo, Map<String, String> map) {
	
		List<BoAgentQrCodeCustomerEntity> list = new ArrayList<BoAgentQrCodeCustomerEntity>();
		Iterator<String> iterator = map.keySet().iterator();
		
		while(iterator.hasNext()) {
			BoAgentQrCodeCustomerEntity entity = new BoAgentQrCodeCustomerEntity();
			String key = iterator.next();
			String path = map.get(key);
			entity.setFileName(key);		
			entity.setAgentNo(agentNo);
			entity.setQrcodePath(path);
			entity.setCreateTime(new Date());
			list.add(entity);
		}
		
		Map<String, List<BoAgentQrCodeCustomerEntity>> mapData = new HashMap<String, List<BoAgentQrCodeCustomerEntity>>();
		mapData.put("list", list);
		int i = mapper.insertBatchData(mapData);
		if(i > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean delete(String ids) {
		
		int i = mapper.deleteBatch(ids);
		if(i > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean updateCustomerQrcode(String mercNo, List<String> ids) {
		BoCustomer bc = new BoCustomer();
		bc.setMercNo(mercNo);
		bc = boCustomerService.findOne(bc);
		//更新二维码
		for(String id : ids) {
			id = id.trim();
			BoAgentQrCodeCustomerEntity entity = mapper.selectByPrimaryKey(Integer.parseInt(id));
			String fileName = mercNo + "_" + id;
			String imgPath = payOssService.changeQrCodeAlias(entity.getQrcodePath(), fileName);
			entity.setQrcodePath(imgPath);
			entity.setMercNo(mercNo);
			entity.setMercName(bc.getMercName());
			mapper.updateByPrimaryKeySelective(entity);
		}
	
		
		return true;
	}

	@Override
	public Boolean deleteCustomerQrcode(String ids) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("mercNo", "");
		map.put("mercName", "");
		map.put("ids", ids);
		int i = mapper.updateCustomerQrcode(map);
		if(i > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<AgentQrCodeCustomerVo> queryCustomerQrCode(String mercNo) {
		List<AgentQrCodeCustomerVo> list = mapper.queryCustomerQrCode(mercNo);
		return list;
	}

	@Override
	public AgentQrCodeCustomerVo selectById(Integer id) {
		
		return mapper.selectById(id);
	}

	@Override
	public List<AgentQrCodeCustomerVo> queryByCustomerNos(String mercNos) {
		
		return mapper.queryByCustomerNos(mercNos);
	}

	
}
