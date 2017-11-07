package com.midai.pay.customer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.customer.entity.BoCustomerReview;

public interface BoCustomerReviewMapper extends MyMapper<BoCustomerReview> {
	
	String column = " id, merc_no mercNo, review_level reviewLevel, review_result reviewResult, advice, create_time createTime, update_time updateTime ";
	
	@Select(" SELECT " + column + " FROM tbl_bo_customer_review  WHERE merc_no=#{mercNo} AND review_level=#{reviewLevel} ORDER BY id DESC LIMIT 0,1 ")
	BoCustomerReview findLatestReview(@Param("mercNo")String mercNo, @Param("reviewLevel")int reviewLevel);
	
	@Select(" SELECT " + column + " FROM tbl_bo_customer_review  WHERE merc_no=#{mercNo} AND review_level=#{reviewLevel} ORDER BY id ASC")
	List<BoCustomerReview> findReviewsByLevel(@Param("mercNo")String mercNo, @Param("reviewLevel")int reviewLevel);
	
	@Insert(" Insert into tbl_bo_customer_review(merc_no,review_level,review_result,advice,create_time,oper_user) values(#{mercNo},#{reviewLevel},#{reviewResult},#{advice},#{createTime},#{operUser})")
	int insertCustomerReview(BoCustomerReview review);
}
