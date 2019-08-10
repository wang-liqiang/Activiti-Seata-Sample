package cn.xxxx.activiti.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "activiti-sample/service")
public interface ActivitiServiceClient {

	@RequestMapping(value = "start")
	public boolean startProcess();

}
