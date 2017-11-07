package com.midai.pay.user.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.midai.pay.PoswRunner;
import com.midai.pay.agent.common.enums.AgentProfitTransTypeEnum;
import com.midai.pay.agent.service.AgentProfitService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=PoswRunner.class)
public class TestAgentProfit {

	@Autowired
	private AgentProfitService agentProfitService;
	
	@Test
	public void saveAgentSwingCardProfitTest() {
		agentProfitService.saveAgentSwingCardProfit("2017080309465184362", AgentProfitTransTypeEnum.TRANSTYPE_SCANWX.getCode());
	}
}
