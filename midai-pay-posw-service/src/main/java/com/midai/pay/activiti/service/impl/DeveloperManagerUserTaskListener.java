package com.midai.pay.activiti.service.impl;

import org.activiti.engine.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midai.pay.activiti.service.UserTaskListenerService;

/** 
 * execute方法的参数DelegateExecution execution可以在流程中各个结点之间传递流程变量。  
 * <activiti:taskListener>元素的event属性，它一共包含三种事件："create"、"assignment"、"complete"，分别表示结点执行处理逻辑的时机为：在处理类实例化时、在结点处理逻辑被指派时、在结点处理逻辑执行完成时，可以根据自己的需要进行指定。  
  *上述流程定义中，4个任务结点对应的处理类 
  * 
  *<activiti:taskListener>元素的event属性，它一共包含三种事件："create"、"assignment"、"complete"，分别表示结点执行处理逻辑的时机为：在处理类实例化时、在结点处理逻辑被指派时、在结点处理逻辑执行完成时，可以根据自己的需要进行指定。  
  * 
 *<userTask id="servicetask2" name="产品经理同意"> 
       <extensionElements> 
          <activiti:taskListener event="complete" class="com.midai.activiti.service.ProductManagerUserTaskListener"/> 
       </extensionElements> 
    </userTask> 
 * 
 * 
 * 项目经理审批过程 
 * @author chenxun 
 *  
 *  
 */  
//@Component("userTaskListenerService")
public class DeveloperManagerUserTaskListener implements   UserTaskListenerService  {




	/**
	 * 
	 */
	private static final long serialVersionUID = 5117554667083368360L;
	private final Logger log = LoggerFactory.getLogger(DeveloperManagerUserTaskListener.class);
	@Override
	public void test1(DelegateTask delegateTask) {
		delegateTask.setAssignee("10");
		System.out.println("do test1   ..................");
		
	}
	@Override
	public void test2(DelegateTask delegateTask) {
		System.out.println("do test2   ..................");
		
		
	}
	@Override
	public void test3(DelegateTask delegateTask) {
		System.out.println("do test3   ..................");
		
		
	}
	@Override
	public void test4(DelegateTask delegateTask) {
		System.out.println("do test4   ..................");
		
		
	}
	@Override
	public void test5(DelegateTask delegateTask) {
		System.out.println("do test5   ..................");
	}



}
