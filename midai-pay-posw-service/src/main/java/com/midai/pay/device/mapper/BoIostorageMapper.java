package com.midai.pay.device.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.device.entity.BoIostorage;
import com.midai.pay.device.vo.BoIostorageCustomerVo;

public interface BoIostorageMapper extends MyMapper<BoIostorage> {


	String insert="device_id,device_no,create_time,device_flag,agent_id,agent_name,destagent_id,destagent_name,destmerc_id,state,operator_id";
	String insertProperty="#{deviceId},#{deviceNo},#{createTime},#{deviceFlag},#{agentId},#{agentName},#{destagentId},#{destagentName},#{destmercId},#{state},#{operatorId}";

	@Insert("insert into tbl_bo_iostorage ( "+insert+" ) values ("+insertProperty+")")
	int insertBoIostorage(BoIostorage boIostorage);
	

	/**
	 * 历史商户设备信息绑定的查询
	 * @param deviceNo
	 * @return
	 */
 @Select("select b.merc_name mercName,b.merc_id mercId,a.create_time createTime,a.destmerc_id destmercId,a.device_no deviceNo,a.state state from tbl_bo_iostorage a "
		+ "LEFT JOIN tbl_bo_customer b ON a.destmerc_id=b.merc_no where a.device_no=#{deviceNo} and a.state in('解绑','使用', '绑定') order by a.id asc ")
  List<BoIostorageCustomerVo>  fetchByDeviceNo (String deviceNo);
/**
 * 历史商户设备信息解绑的信息
 */
 @Select("select b.merc_name mercName,b.merc_id mercId,a.create_time createTime,a.destmerc_id destmercId,a.device_no deviceNo,a.state state from tbl_bo_iostorage a "
 		+ " LEFT JOIN tbl_bo_customer b ON a.destmerc_id=b.merc_no where a.device_no=#{deviceNo} AND a.destmerc_id =#{destmercId} and a.state in('解绑')")
 
 List<BoIostorageCustomerVo> fetchByDeviceAndmercNo(@Param("deviceNo") String deviceNo, @Param("destmercId") String destmercId);

@InsertProvider(type=com.midai.pay.device.provider.BoIostorageProvider.class, method="batchInsertBIo")
int batchInsertBIo(Map<String, List<BoIostorage>> map);
}
