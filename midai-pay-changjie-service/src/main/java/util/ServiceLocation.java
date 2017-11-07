/**
 * 文件名： ServiceLocation.java
 * 建立时间： 2012-4-15 上午11:51:11
 * 创建人： SongCheng
 */
package util;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.midai.pay.changjie.service.impl.CjMsgSendServiceImpl;

/**
 * 服务定位器类，取得spring中定义的Bean对象
 * 
 *
 * @author SongCheng
 */
public class ServiceLocation implements ApplicationContextAware {
	private static Logger LOG = Logger.getLogger(ServiceLocation.class);

	private static ApplicationContext applicationContext = null;

	public ServiceLocation() {
		LOG.info("初始化：" + this);
	}

	public synchronized static ApplicationContext getApplicationContext() {
		if (applicationContext != null)
			return applicationContext;
		//----------------------------------------------------

		//新建 applicationContext
		LOG.warn("------->> 执行内部新建ApplicationContext");
		//applicationContext = new ClassPathXmlApplicationContext(S.SPRING_XML_FILE_PATH);

		return applicationContext;
	}//method

	public void setApplicationContext(ApplicationContext ac) {
		applicationContext = ac;
	}

	public static Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}


	public static CjMsgSendServiceImpl getCjMsgSendService() {
		return (CjMsgSendServiceImpl) getBean("cjMsgSendService");
	}

}//class
