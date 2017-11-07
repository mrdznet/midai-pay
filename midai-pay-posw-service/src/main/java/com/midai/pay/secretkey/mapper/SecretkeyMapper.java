package com.midai.pay.secretkey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;






import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.secretkey.entity.Secretkey;

public interface SecretkeyMapper extends MyMapper<Secretkey>  {

	@Select("select max(batch) batch from tbl_bo_secretkey where facture=#{facture}")
	String findMaxBatch(String facture);
	
	@SelectProvider(type=com.midai.pay.secretkey.provider.SecretkeyProvider.class,method="findCount")
	int findCount(String begin);
	
	@SelectProvider(type=com.midai.pay.secretkey.provider.SecretkeyProvider.class,method="findMaxDeviceNo")
	String findMaxDeviceNo(String begin);
	
	String insertProperty="#{deviceNo},#{ptmk},#{tmk},#{tmkCheckValue}";
	
	@Insert("insert into tbl_secretkey(device_no,ptmk,tmk,tmkcheckvalue) values("+insertProperty+")")
	int insertSecretkey(Secretkey secretkey);
	
	@Select("select device_no deviceNo,tmk tmk,tmkcheckvalue tmkCheckValue from (select device_no,tmk,tmkcheckvalue from tbl_secretkey where device_no like '%${begin}%' order by device_no asc) p  limit 0,#{num} ")
	List<Secretkey> findSecretkey(@Param("begin") String begin,@Param("num") int num);
	
	@Insert("insert into tbl_bo_secretkey(facture,batch,create_time) values(#{facture},#{batch},now())")
	int insertBoSecretkey(@Param("facture") String facture,@Param("batch") String batch);
}
