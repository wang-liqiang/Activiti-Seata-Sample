package cn.xxxx.activiti.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

	@Autowired
	private DataSource dataSrouce;

	@Bean
	public SpringProcessEngineConfiguration springProcessEngineConfiguration(PlatformTransactionManager transactionManager,
			SpringAsyncExecutor springAsyncExecutor) throws IOException {
		return baseSpringProcessEngineConfiguration(dataSrouce, transactionManager, springAsyncExecutor);
	}

	@Override
	public List<Resource> discoverProcessDefinitionResources(ResourcePatternResolver applicationContext, String prefix,
			List<String> suffixes, boolean checkPDs) throws IOException {
		return new ArrayList<Resource>();
	}

	@Bean
	public RepositoryService repositoryService(ProcessEngine processEngine) {
		return processEngine.getRepositoryService();
	}

	@Bean
	public RuntimeService runtimeService(ProcessEngine processEngine) {
		return processEngine.getRuntimeService();
	}

	@Bean
	public TaskService taskService(ProcessEngine processEngine) {
		return processEngine.getTaskService();
	}

	@Bean
	public HistoryService historyService(ProcessEngine processEngine) {
		return processEngine.getHistoryService();
	}

	@Bean
	public ManagementService managementService(ProcessEngine processEngine) {
		return processEngine.getManagementService();
	}

	@Bean
	public IdentityService identityService(ProcessEngine processEngine) {
		return processEngine.getIdentityService();
	}

}
