package cn.xxxx.activiti.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.xxxx.activiti.service.ActivitiService;

@RestController
@RequestMapping("/service")
public class ActivitiServiceClientImpl {

	@Autowired
	private ActivitiService activitiService;

	@RequestMapping("/start")
	public boolean startProcess() {
		activitiService.startProcess("SEATA-TEST", "1");
		return true;
	}

}
