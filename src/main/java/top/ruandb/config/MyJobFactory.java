package top.ruandb.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * quartz job 实现spring注入
 * @author rdb
 *
 */
@Component
public class MyJobFactory extends AdaptableJobFactory {
	
	@Autowired
	private AutowireCapableBeanFactory beanFactory;
	
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle)
			throws Exception {
		// TODO Auto-generated method stub
		Object jobInstance = super.createJobInstance(bundle);
		//AutowireCapableBeanFactory只是让jobInstance具备了自动配置属性的功能，并不是把jobInstance放入到了spring容器中
		beanFactory.autowireBean(jobInstance);
		return jobInstance;
	}

}
