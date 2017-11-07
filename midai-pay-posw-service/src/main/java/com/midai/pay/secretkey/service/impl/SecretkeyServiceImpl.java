package com.midai.pay.secretkey.service.impl;

import java.util.ArrayList;
import java.util.List;



import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.posp.service.PospService;
import com.midai.pay.secretkey.entity.Secretkey;
import com.midai.pay.secretkey.mapper.SecretkeyMapper;
import com.midai.pay.secretkey.service.SecretkeyService;
import com.midai.pay.secretkey.vo.SecretkeyVo;

@Service
@Transactional
public class SecretkeyServiceImpl extends BaseServiceImpl<Secretkey> implements
		SecretkeyService {

	private final SecretkeyMapper secretkeyMapper;

	@Reference
	private PospService pospService;
	
	public SecretkeyServiceImpl(SecretkeyMapper mapper) {
		super(mapper);
		this.secretkeyMapper = mapper;
	}

	@Override
	public SecretkeyVo createSecretkey(String facture, int num) {

		SecretkeyVo vo = new SecretkeyVo();
		// 第一步查询最大的批次号
		String batch = secretkeyMapper.findMaxBatch(facture);
		if (batch == null || batch.isEmpty()) {
			batch = "0";
		}
		batch = String.format("%04d", (Integer.parseInt(batch) + 1));
		String begin = facture + batch;

		// 第二步根据DEVICENO like %begin% 查询 SECRETKEY表的总数
		int cc = secretkeyMapper.findCount(begin);
		if (cc > 0) {
			vo.setBatch(batch);
			vo.setFacture(facture);
			vo.setNum(num);
			vo.setMessage("该密钥已存在，不能重新生成！");
			return vo;
		}
		// 找到最大的机身号
		String c = secretkeyMapper.findMaxDeviceNo(begin);
		if (c == null || c.isEmpty()) {
			c = begin + "00000000";
		}
		int no = Integer.parseInt(c.substring(8));
		// StringBuffer out= new StringBuffer();
		for (int i = 0; i < num; i++) {
			no += 1;
			String deviceno = begin + String.format("%08d", (no));
			String[] str = pospService.generateTermianlKey();
			String pmk = str[0];
			String tmk = str[1];
			String tmkcheckvalue = str[2];
			Secretkey secretkey = new Secretkey();
			secretkey.setDeviceNo(deviceno);
			secretkey.setPtmk(pmk);
			secretkey.setTmk(tmk);
			secretkey.setTmkCheckValue(tmkcheckvalue);
			secretkeyMapper.insertSecretkey(secretkey);
			// out.append(deviceno).append("=").append(tmk).append("\n");
		}
		vo.setBatch(batch);
		vo.setFacture(facture);
		vo.setNum(num);
		vo.setMessage("密钥生成成功！");
		secretkeyMapper.insertBoSecretkey(facture, batch);
		return vo;
	}

	@Override
	public String exportSecretkey(String facture, String batch, int num) {
		List<Secretkey> rset = new ArrayList<Secretkey>();

		String begin = facture + batch;
		rset = secretkeyMapper.findSecretkey(begin, num);
		if (rset== null) {
			return "没有查到任何数据";
		}
		StringBuffer out = new StringBuffer();
		if (rset != null) {
			for (Secretkey s : rset) {
				String deviceno = s.getDeviceNo();
				String tmk = s.getTmk();
				String tmkcheckvalue = s.getTmkCheckValue();
				if (deviceno.contains("CC"))
					out.append(deviceno).append("|").append(tmk).append("|")
							.append(tmkcheckvalue).append("\n");
				else if (deviceno.contains("CD"))
					out.append(deviceno).append("|").append(tmk).append("\n");
				
				else out.append(deviceno).append("|").append(tmk).append("\n");
			
			}

		}

		return out.toString();
	}
}
