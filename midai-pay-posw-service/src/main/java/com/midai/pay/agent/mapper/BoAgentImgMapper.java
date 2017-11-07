package com.midai.pay.agent.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.agent.entity.AgentImg;
import com.midai.pay.agent.vo.AgentImgVo;

public interface BoAgentImgMapper extends MyMapper<AgentImg> {
	@Select(" SELECT id, agent_no agentNo, type, url from tbl_bo_agent_img p where exists (select 1 from (select max(t.id) id from tbl_bo_agent_img t where  t.agent_no=#{agentNo} group by type) d where p.id=d.id)  order by type")
	List<AgentImgVo> findByAgentNo(String agentNo);
	
	@Insert(" Insert into tbl_bo_agent_img(agent_no, type, url) values(#{agentNo},#{type},#{url})")
	int insertImg(AgentImgVo vo);
	
	@Delete(" DELETE FROM tbl_bo_agent_img WHERE agent_no=#{agentNo} ")
	int deleteImgByAgentNo(String agentNo);
	
	/*海贝商户申请审核通过更新图片*/
	@Update(" update tbl_bo_agent_img  set  url=#{url} where agent_no=#{agentNo} and type=4 ")
	int updateBank(@Param("url")String url, @Param("agentNo") String agentNo);
	
	
	@Select("select url from tbl_bo_agent_img  where agent_no=#{agentNo} and type=4  ")
	String selectUrlByAgentNo(String agentNo);
}
