package cn.xxxx.activiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.xxxx.activiti.service.SimpleService;

@RestController
@RequestMapping(value = "/")
public class SimpleController {

	@Autowired
	private SimpleService simpleService;

	@RequestMapping("/test")
	public boolean test() {
		simpleService.test();
		return true;
	}

}
