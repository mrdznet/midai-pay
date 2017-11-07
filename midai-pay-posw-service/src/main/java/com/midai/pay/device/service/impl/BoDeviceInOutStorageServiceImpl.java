package com.midai.pay.device.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.common.utils.ServiceUtils;
import com.midai.pay.device.entity.BoDevice;
import com.midai.pay.device.mapper.BoDeviceInOutStorageMapper;
import com.midai.pay.device.query.BoDeviceInOutStorageQuery;
import com.midai.pay.device.service.BoDeviceInOutStorageService;
import com.midai.pay.device.vo.BoDeviceInOutStorageVo;



@Service
public class BoDeviceInOutStorageServiceImpl  extends BaseServiceImpl<BoDevice> implements BoDeviceInOutStorageService{

	public BoDeviceInOutStorageServiceImpl(BoDeviceInOutStorageMapper mapper){
		
		super(mapper);
		this.mapper=mapper;
	}
	
	
	private final BoDeviceInOutStorageMapper mapper;

	@Override
	public List<BoDeviceInOutStorageVo> findQueryBoDevice(BoDeviceInOutStorageQuery query){
		if(StringUtils.isNotBlank(query.getDeviceNoBegin())&&StringUtils.isNotBlank(query.getDeviceNoEnd())){
			String deviceNoStart=query.getDeviceNoBegin();
			String deviceNoEnd = query.getDeviceNoEnd();
	       if(query.getDeviceNoBegin().startsWith("0000")){
	    	   deviceNoStart=query.getDeviceNoBegin().substring(4);
	       }	
	       if(query.getDeviceNoEnd().startsWith("0000")){
	    	   deviceNoEnd=query.getDeviceNoEnd().substring(4);
	       }
			int index=-1;
	        char[] charArr=deviceNoStart.toCharArray();
	        for (int i=0;i<charArr.length;i++){
	            if(Character.isLetter(charArr[i])){
	                index=i;
	            }
	        }

	        long deviceNoStartInt=Long.parseLong(deviceNoStart.substring(index+1));
	        long deviceNoEndInt=Long.parseLong(deviceNoEnd.substring(index+1));
	        long deviceNoCount=deviceNoEndInt-deviceNoStartInt+1;
	        List<String> deviceNoList=ServiceUtils.buildDeviceNo(deviceNoStart,""+deviceNoCount);

	        StringBuffer str=new StringBuffer("");
	        for (String deviceno:deviceNoList){
	        	
	        	if(query.getDeviceNoBegin().startsWith("0000")){
	        		str.append(" '0000"+deviceno+"',");
	        	}
	        	else{
	            str.append("'"+deviceno+"',");
	        	}
	        }
	        str.setLength(str.length()-1);
	        query.setDeviceNobn(str.toString());
		}
		return mapper.findQueryBoDevice(query);
	}
	
	@Override
	public  int findQueryBoDeviceCount(BoDeviceInOutStorageQuery query){
		if(StringUtils.isNotBlank(query.getDeviceNoBegin())&&StringUtils.isNotBlank(query.getDeviceNoEnd())){
			String deviceNoStart=query.getDeviceNoBegin();
			String deviceNoEnd=query.getDeviceNoEnd();
	       if(query.getDeviceNoBegin().startsWith("0000")){
	    	   deviceNoStart=query.getDeviceNoBegin().substring(4);
	       }		
	       if(query.getDeviceNoEnd().startsWith("0000")){
	    	   deviceNoEnd=query.getDeviceNoEnd().substring(4);
	       }		
			int index=-1;
	        char[] charArr=deviceNoStart.toCharArray();
	        for (int i=0;i<charArr.length;i++){
	            if(Character.isLetter(charArr[i])){
	                index=i;
	            }
	        }

	        long deviceNoStartInt=Long.parseLong(deviceNoStart.substring(index+1));
	        long deviceNoEndInt=Long.parseLong(deviceNoEnd.substring(index+1));
	        long deviceNoCount=deviceNoEndInt-deviceNoStartInt+1;
	        List<String> deviceNoList=ServiceUtils.buildDeviceNo(deviceNoStart,""+deviceNoCount);

	        StringBuffer str=new StringBuffer("");
	        for (String deviceno:deviceNoList){
	        	
	        	if(query.getDeviceNoBegin().startsWith("0000")){
	        		str.append(" '0000"+deviceno+"',");
	        	}
	        	else{
	            str.append("'"+deviceno+"',");
	        	}
	        }
	        str.setLength(str.length()-1);
	        query.setDeviceNobn(str.toString());
		}
		
		return mapper.findQueryBoDeviceCount(query);
		
	}
	
	@Override
	public List<BoDeviceInOutStorageVo> ExcelDownBoDevice(BoDeviceInOutStorageQuery query){
		if(StringUtils.isNotBlank(query.getDeviceNoBegin())&&StringUtils.isNotBlank(query.getDeviceNoEnd())){
			String deviceNoStart=query.getDeviceNoBegin();
			String deviceNoEnd=query.getDeviceNoEnd();
		       if(query.getDeviceNoBegin().startsWith("0000")){
		    	   deviceNoStart=query.getDeviceNoBegin().substring(4);
		       }	
		       if(query.getDeviceNoEnd().startsWith("0000")){
		    	   deviceNoEnd=query.getDeviceNoEnd().substring(4);
		       }
				int index=-1;
		        char[] charArr=deviceNoStart.toCharArray();
		        for (int i=0;i<charArr.length;i++){
		            if(Character.isLetter(charArr[i])){
		                index=i;
		            }
		        }

		        long deviceNoStartInt=Long.parseLong(deviceNoStart.substring(index+1));
		        long deviceNoEndInt=Long.parseLong(deviceNoEnd.substring(index+1));
		        long deviceNoCount=deviceNoEndInt-deviceNoStartInt+1;
		        List<String> deviceNoList=ServiceUtils.buildDeviceNo(deviceNoStart,""+deviceNoCount);

		        StringBuffer str=new StringBuffer("");
		        for (String deviceno:deviceNoList){
		        	
		        	if(query.getDeviceNoBegin().startsWith("0000")){
		        		str.append(" '0000"+deviceno+"',");
		        	}
		        	else{
		            str.append("'"+deviceno+"',");
		        	}
		        }
		        str.setLength(str.length()-1);
		        query.setDeviceNobn(str.toString());
			}
		
		return mapper.ExcelDownBoDevice(query);
	}
	
	@Override
	public int ExcelDownloadBoDeviceCount(BoDeviceInOutStorageQuery query){
		if(StringUtils.isNotBlank(query.getDeviceNoBegin())&&StringUtils.isNotBlank(query.getDeviceNoEnd())){
			String deviceNoStart=query.getDeviceNoBegin();
			String deviceNoEnd=query.getDeviceNoEnd();
		       if(query.getDeviceNoBegin().startsWith("0000")){
		    	   deviceNoStart=query.getDeviceNoBegin().substring(4);
		       }	
		       if(query.getDeviceNoEnd().startsWith("0000")){
		    	   deviceNoEnd=query.getDeviceNoEnd().substring(4);
		       }		
				int index=-1;
		        char[] charArr=deviceNoStart.toCharArray();
		        for (int i=0;i<charArr.length;i++){
		            if(Character.isLetter(charArr[i])){
		                index=i;
		            }
		        }

		        long deviceNoStartInt=Long.parseLong(deviceNoStart.substring(index+1));
		        long deviceNoEndInt=Long.parseLong(deviceNoEnd.substring(index+1));
		        long deviceNoCount=deviceNoEndInt-deviceNoStartInt+1;
		        List<String> deviceNoList=ServiceUtils.buildDeviceNo(deviceNoStart,""+deviceNoCount);

		        StringBuffer str=new StringBuffer("");
		        for (String deviceno:deviceNoList){
		        	
		        	if(query.getDeviceNoBegin().startsWith("0000")){
		        		str.append(" '0000"+deviceno+"',");
		        	}
		        	else{
		            str.append("'"+deviceno+"',");
		        	}
		        }
		        str.setLength(str.length()-1);
		        query.setDeviceNobn(str.toString());
			}
		
		return mapper.ExcelDownloadBoDeviceCount(query);
	}
	
	/**
	 * 出入库明细查询Excel下载有复选框条件
	 */
	@Override
	public List<BoDeviceInOutStorageVo> ExcelDownSelectBoDevice(List<String> deviceNos) {
		StringBuffer deviceNoStr=new StringBuffer("");
		for(String deviceNo:deviceNos){
			deviceNoStr.append("'"+deviceNo+"',");
		}
		deviceNoStr.setLength(deviceNoStr.length()-1);
		return mapper.ExcelDownSelectBoDevice(deviceNoStr.toString());
	}
	
	
	@Override
	public List<BoDevice> findall(){
		
		return mapper.selectAll();
	}

	
	

}
