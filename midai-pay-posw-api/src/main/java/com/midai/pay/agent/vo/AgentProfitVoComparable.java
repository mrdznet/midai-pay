package com.midai.pay.agent.vo;

import java.util.Comparator;

public class AgentProfitVoComparable implements Comparator<AgentProfitVo> {

	// 对象的排序方式[升、降]
	public static boolean sortASC = true;

	@Override
	public int compare(AgentProfitVo apv1, AgentProfitVo apv2) {

		int result = 0;

		if (sortASC) {
			result = apv1.getSettlementTime().compareTo(apv2.getSettlementTime());
			if (result == 0) {
				apv1.getAgentId().compareTo(apv2.getAgentId());
			}

		} else {
			result = apv2.getSettlementTime().compareTo(apv1.getSettlementTime());
			if (result == 0) {
				apv2.getAgentId().compareTo(apv1.getAgentId());
			}
		}
		
		return result;
	}
}
