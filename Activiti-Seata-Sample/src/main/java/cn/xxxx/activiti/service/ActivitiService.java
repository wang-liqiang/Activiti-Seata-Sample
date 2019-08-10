package cn.xxxx.activiti.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivitiService {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RepositoryService repositoryService;

	public void startProcess(String processKey, String businessKey) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("description", "流程变量");
		runtimeService.startProcessInstanceByKey(processKey, businessKey, vars);
	}

	public boolean CustomizeProcess(String processKey, List<String> userIds) {
		// 检查是否已经有相同的流程
		if (repositoryService.createDeploymentQuery().processDefinitionKey(processKey).list().size() > 0) {
			return true;
		}
		// 创建模型
		BpmnModel bpmnModel = new BpmnModel();
		// 创建流程
		Process process = new Process();
		bpmnModel.addProcess(process);
		// 设置流程KEY
		process.setId(processKey);
		// 添加开始节点
		process.addFlowElement(createStartEvent());
		// 添加结束节点
		process.addFlowElement(createEndEvent());
		// 按顺序保存用户任务，以便后续连接
		List<UserTask> tasks = new ArrayList<UserTask>();
		// 添加节点
		int index = 1;
		for (String userId : userIds) {
			// 创建用户任务，是否审核节点以是否指定用户名角色决定
			UserTask task = createUserTask("UT" + index, "用户[" + userId + "]的任务", userId);
			// 添加节点
			process.addFlowElement(task);
			tasks.add(task);
			index++;
		}
		// 按顺序根据用户任务创建连接线
		index = 1;
		for (int i = 0; i < tasks.size(); i++) {
			UserTask task = tasks.get(i);
			// 起点
			if (i == 0) {
				// 创建连接线
				SequenceFlow sf = createSequenceFlow("SF" + index, "S", task.getId());
				process.addFlowElement(sf);
				index++;
			}
			if (i == tasks.size() - 1) {
				// 创建连接线
				SequenceFlow sf = createSequenceFlow("SF" + index, task.getId(), "E");
				process.addFlowElement(sf);
				index++;
			}
			// 中间节点
			if (i != tasks.size() - 1) {
				SequenceFlow sf = createSequenceFlow("SF" + index, task.getId(), tasks.get(tasks.size() - 1).getId());
				process.addFlowElement(sf);
				index++;
			}
		}
		// 模型自动布局
		BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(bpmnModel);
		bpmnAutoLayout.execute();
		// 部署流程
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().addBpmnModel(processKey + ".bpmn", bpmnModel)
				.name(processKey + "定制动态流程");
		deploymentBuilder.deploy();
		return true;
	}

	/**
	 * 创建用户任务，审核人或用户不为空的情况下则为审核任务
	 * 
	 * @param id 唯一标识
	 * @param name 名称，将显示在流程图上
	 * @param assignee 指定审核人
	 * @param userIds 候选审核人
	 * @param roleIds 候选审核角色
	 * @return
	 */
	public UserTask createUserTask(String id, String name, String userId) {
		UserTask userTask = new UserTask();
		userTask.setId(id);
		userTask.setName(name);
		userTask.setNotExclusive(false);
		userTask.setAssignee(userId);
		return userTask;
	}

	/**
	 * 创建流程（连接线）
	 * 
	 * @param id 唯一标识
	 * @param from 起点
	 * @param to 终点
	 * @param conditionExpression 流程条件
	 * @return
	 */
	public SequenceFlow createSequenceFlow(String id, String from, String to) {
		SequenceFlow flow = new SequenceFlow();
		flow.setSourceRef(from);
		flow.setTargetRef(to);
		flow.setId(id);
		return flow;
	}

	/**
	 * 创建开始节点
	 * 
	 * @return
	 */
	public StartEvent createStartEvent() {
		StartEvent startEvent = new StartEvent();
		startEvent.setId("S");
		return startEvent;
	}

	/**
	 * 创建结束节点
	 * 
	 * @return
	 */
	public EndEvent createEndEvent() {
		EndEvent endEvent = new EndEvent();
		endEvent.setId("E");
		return endEvent;
	}

}
