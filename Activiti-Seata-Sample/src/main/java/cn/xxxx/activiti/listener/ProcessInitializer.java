package cn.xxxx.activiti.listener;

import java.util.Arrays;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cn.xxxx.activiti.service.ActivitiService;

@Component
public class ProcessInitializer implements ApplicationListener<ApplicationStartedEvent> {

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		ActivitiService activitiService = event.getApplicationContext().getBean(ActivitiService.class);
		activitiService.CustomizeProcess("SEATA-TEST", Arrays.asList("1"));
	}

}
