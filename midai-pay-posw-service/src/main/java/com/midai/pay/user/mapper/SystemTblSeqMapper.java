package com.midai.pay.user.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.TblSeq;

public interface SystemTblSeqMapper extends MyMapper<TblSeq> {
	
	@Select(" select _nextval('${name}') ")
	@Options(statementType= StatementType.STATEMENT )
	int seqNextval( TblSeq tblSeq);
	
	@Insert(" insert into tbl_seq(name, current_value, incnt, update_time) values(#{name}, 0, 1, NOW()) ")
	int insertTblSeq(TblSeq tblSeq);
	
	@Select(" SELECT TO_DAYS(NOW()) - TO_DAYS(update_time) FROM tbl_seq WHERE name=#{name} ")
	int isCurrentDay(String name);
	
	@Update(" update tbl_seq set current_value=0 where name=#{name} ")
	int updateCurrentValue(String name);
	
    @Select(" select _nextval('Mi') ")
    @Options(statementType= StatementType.STATEMENT )
    public int findCustomeNextValue();
    
    @Select(" select _nextval('PY') ")
    @Options(statementType= StatementType.STATEMENT )
    public int findAgentValue();
}
