package com.midai.pay.customer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.customer.entity.CustomerImg;
import com.midai.pay.customer.vo.CustomerImgVo;

public interface BoCustomerImgMapper extends MyMapper<CustomerImg> {
	
	@Select(" SELECT id, merc_no mercNo, type, url from tbl_bo_customer_img p where exists (select 1 from (select max(t.id) id from tbl_bo_customer_img t where  t.merc_no=#{mercNo} group by type) d where p.id=d.id)  order by type")
	List<CustomerImgVo> findByMercNo(String mercNo);
	
	@Insert(" Insert into tbl_bo_customer_img(merc_no, type, url) values(#{mercNo},#{type},#{url})")
	int insertImg(CustomerImgVo vo);
	
	@Delete(" DELETE FROM tbl_bo_customer_img WHERE merc_no=#{mercNo} ")
	int deleteImgByMercNo(String mercNo);
	
	/*海贝商户申请审核通过更新图片*/
	@Update(" update tbl_bo_customer_img  set  url=#{url} where merc_no=#{mercNo} and type=4 ")
	int updateBank(@Param("url")String url, @Param("mercNo") String mercNo);
	
	
	@Select("select url from tbl_bo_customer_img  where merc_no=#{mercNo} and type=4  ")
	String selectUrlBymercNo(String mercNo);
}
