package com.midai.pay.agent.vo;

import java.util.Comparator;

import com.midai.pay.agent.entity.AgentProfit;

public class AgentProfitComparable implements Comparator<AgentProfit> {

	// 对象的排序方式[升、降]
	public static boolean sortASC = true;

	@Override
	public int compare(AgentProfit apv1, AgentProfit apv2) {

		int result = 0;

		int apv1Count = countStr(apv1.getCode(), "-");
		int apv2Count = countStr(apv2.getCode(), "-");
		if (sortASC) {
			
			result = apv1Count - apv2Count ;
			
		} else {
			result = apv2Count - apv1Count;
			
		}

		return result;
	}

	private int countStr(String str1, String str2) {
		int counter = 0;
		if (str1.indexOf(str2) == -1) {
			return 0;
		}
		while (str1.indexOf(str2) != -1) {
			counter++;
			str1 = str1.substring(str1.indexOf(str2) + str2.length());
		}
		return counter;
	}
}
