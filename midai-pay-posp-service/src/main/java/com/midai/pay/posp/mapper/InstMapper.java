package com.midai.pay.posp.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.posp.entity.Inst;
import com.midai.pay.posp.entity.SeqEntity;

public interface InstMapper extends MyMapper<Inst> {
	

	@Select(" select inst_seq_nextval('${instCode}') ")
	@Options(statementType= StatementType.STATEMENT )
	String instSeqNextval( SeqEntity seqEntity);
	
	@Select(" select trans_ssn_nextval() ")
	@Options(statementType= StatementType.STATEMENT )
	String transSsnNextval();
	
     
	

}
