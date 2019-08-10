package cn.xxxx.activiti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.xxxx.activiti.api.ActivitiServiceClient;
import io.seata.spring.annotation.GlobalTransactional;

@Component
public class SimpleService {

	@Autowired
	private ActivitiServiceClient activitiServiceClient;

	@GlobalTransactional
	public void test() {

		activitiServiceClient.startProcess();

		if ("".equals("")) {
			throw new RuntimeException("测试回滚");
		}
	}

}
