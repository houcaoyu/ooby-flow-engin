package ooby.workflow.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.task.Task;

import ooby.workflow.activiti.entity.SendBackDefinitionEntity;
import ooby.workflow.activiti.scriptutils.impl.AssigneeRule;
import ooby.workflow.activiti.service.OobyIdentityService;
import ooby.workflow.activiti.service.OobyRuntimeService;
import ooby.workflow.activiti.service.OobyTaskService;

/**
 * @author caoyu
 *
 */

public class OobyTaskServiceImpl implements OobyTaskService {

	private TaskService taskService;
	private RepositoryService repositoryService;
	private HistoryService historyService;
	private OobyRuntimeService obrs;
	private OobyIdentityService obis;

	// 请求
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hou.helloactiviti.service.TaskHelper#claim(java.lang.String,
	 * java.lang.String)
	 */
	public void claim(String taskId, String userId) {
		taskService.claim(taskId, userId);
	}

	// 申请
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hou.helloactiviti.service.TaskHelper#apply(java.lang.String,
	 * java.lang.String)
	 */
	public void apply(String taskId, String userId, String depId) {
		taskService.setAssignee(taskId, userId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_OPERATION, OPERATION_VALUE_APPLY);
		map.put(KEY_DEPARTMENT_ID, depId);
		obrs.setApplicant(getTask(taskId).getProcessInstanceId(), userId);
		complete(taskId, map);
	}

	// 承认
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hou.helloactiviti.service.TaskHelper#approve(java.lang.String)
	 */
	public void approve(String taskId,String userId,String depId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_OPERATION, OPERATION_VALUE_APPROVE);
		map.put(KEY_DEPARTMENT_ID, depId);
		complete(taskId, map);
	}

	// 退回(重新流转)
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hou.helloactiviti.service.TaskHelper#sendBack(java.lang.String,
	 * java.lang.String)
	 */
	public void sendBack(String currentTaskId,String userId, String targetActivityId) {
		if(!canSendBack(currentTaskId))
			throw new RuntimeException("Task "+currentTaskId+" has no send back definition.");
		
		Map<String,SendBackDefinitionEntity> sendBackMap=getSendBackTargets(currentTaskId);
		if(!sendBackMap.containsKey(targetActivityId))
			throw new RuntimeException("Task "+currentTaskId+" can not be send back to activity "+targetActivityId+" , because no definition.");
		
		boolean directSubmit=sendBackMap.get(targetActivityId).isDirect();
		
		Task currentTask = getTask(currentTaskId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_OPERATION, OPERATION_VALUE_SEND_BACK);
		commit(currentTaskId, targetActivityId, map);

		// set assignee of target task
		HistoricTaskInstance hisTask = historyService.createHistoricTaskInstanceQuery()
				.taskDefinitionKey(targetActivityId).finished().list().get(0);
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(currentTask.getProcessInstanceId())
				.list();

		for (Task task : taskList) {
			taskService.setAssignee(task.getId(), hisTask.getAssignee());
			taskService.setVariableLocal(task.getId(),KEY_DEPARTMENT_ID,getDepartment(hisTask.getId()));
			taskService.setVariableLocal(task.getId(), KEY_SEND_BACK_TYPE,
					directSubmit ? SEND_BACK_TYPE_VALUE_DIRECT_SUBMIT : SEND_BACK_TYPE_VALUE_RECIRCLE);
		}
	}

	
	// (打回以后)重新提交
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hou.helloactiviti.service.TaskHelper#resubmit(java.lang.String)
	 */
	public void resubmit(String taskId,String userId) {
		Task task = getTask(taskId);
		String type = (String)getVariable(taskId, KEY_SEND_BACK_TYPE);
		String preAssignee = getPreAssignee(taskId);
		String preActivity = getPreActivityId(taskId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_OPERATION, "Resubmit");
		if (SEND_BACK_TYPE_VALUE_DIRECT_SUBMIT.equals(type)) {
			commit(taskId, preActivity, map);
			List<Task> taskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
			for (Task i : taskList) {
				taskService.setAssignee(i.getId(), preAssignee);
			}
		} else {
			complete(taskId, map);
		}
	}

	// 撤销
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hou.helloactiviti.service.TaskHelper#cancel()
	 */
	public void cancel() {

	}

	// 会签
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hou.helloactiviti.service.TaskHelper#huiqian()
	 */
	public void huiqian() {

	}

	// 终止
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hou.helloactiviti.service.TaskHelper#stop()
	 */
	public void stop() {

	}

	// 拒绝
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hou.helloactiviti.service.TaskHelper#reject()
	 */
	public void reject() {

	}

	public boolean canSendBack(String taskId) {
		return getVariable(taskId, KEY_SEND_BACK_TARGET) != null;
	}
	@SuppressWarnings("unchecked")
	public Map<String,SendBackDefinitionEntity> getSendBackTargets(String taskId) {
		return (Map<String,SendBackDefinitionEntity>) getVariable(taskId, KEY_SEND_BACK_TARGET);
	}
	
	public String getAssignee(String taskId){
		HistoricTaskInstance task=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		return task.getAssignee();
	}
	
	public String getActivityId(String taskId){
		HistoricTaskInstance task=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		return task.getTaskDefinitionKey();
	}
	
	public String getPreTaskId(String taskId){
		return (String)getVariable(taskId,KEY_PRE_TASK_ID);
	}
	
	public String getPreAssignee(String taskId) {
		return getAssignee(getPreTaskId(taskId));
	}

	public String getPreActivityId(String taskId) {
		return getActivityId(getPreTaskId(taskId));
	}

	public String getOperation(String taskId) {
		return (String)getVariable(taskId,KEY_OPERATION);
	}
	public String getDepartment(String taskId){
		return (String)getVariable(taskId,KEY_DEPARTMENT_ID);
	}
	public String getPreDepartment(String taskId) {
		return getDepartment(getPreTaskId(taskId));
	}
	public Object getVariable(String executionId,String variableName){
		HistoricVariableInstance var=historyService.createHistoricVariableInstanceQuery().taskId(executionId).variableName(variableName).singleResult();
		return var==null?null:var.getValue();
	}

	/**
	 * 自由流提交
	 * 
	 * @param currentTaskId
	 *            当前任务ID
	 * @param targetActivityId
	 *            下一个任务节点ID
	 */
	private void commit(String currentTaskId, String targetActivityId, Map<String, String> variables) {
		Task currentTask = getTask(currentTaskId);
		String processDefId = currentTask.getProcessDefinitionId();
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefId);
		ActivityImpl activity = def.findActivity(currentTask.getTaskDefinitionKey());
		List<PvmTransition> oriPvmTransitionList = clearTransition(activity);
		// 创建新流向
		TransitionImpl newTransition = activity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = def.findActivity(targetActivityId);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);

		complete(currentTaskId, variables);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);

		// 还原以前流向
		restoreTransition(activity, oriPvmTransitionList);

	}

	private void complete(String currentTaskId, Map<String, String> variables) {

		Task currentTask = taskService.createTaskQuery().taskId(currentTaskId).singleResult();

		// set task operation object to the local variable of current task
		if (variables != null)
			for (Entry<String, String> entry : variables.entrySet()) {
				taskService.setVariableLocal(currentTaskId, entry.getKey(), entry.getValue());
			}

		// 执行转向任务
		taskService.complete(currentTaskId);

		List<Task> taskList = taskService.createTaskQuery().processInstanceId(currentTask.getProcessInstanceId())
				.list();
		for (Task task : taskList) {
			taskService.setVariableLocal(task.getId(), KEY_PRE_TASK_ID, currentTask.getId());
		}
	}

	/**
	 * get task instance by task id
	 * 
	 * @param taskId
	 *            the task id
	 * @return task instance or null, if task is exist.
	 */
	private Task getTask(String taskId) {
		if (taskId == null)
			throw new NullPointerException("taskId is null.");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

		return task;
	}

	/**
	 * 清空指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
	private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
	}

	/**
	 * 还原指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
	private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAuthDeparment(String taskId,String userId){
		List<String> result=new ArrayList<String>();
		
		List<Group> groups=obis.getDepartmentsOfUser(userId);
		List<String> groupIds=new ArrayList<String>();
		for(Group group:groups){
			groupIds.add(group.getId());
		}
		List<AssigneeRule> rules=(List<AssigneeRule>)taskService.getVariableLocal(taskId, "_assignee_rule");
		for(AssigneeRule rule:rules){
			List<String> ruleGroups=rule.getAuthDeparment(userId);
			for(String ruleGroup:ruleGroups){
				if(groupIds.contains(ruleGroup)){
					groupIds.remove(ruleGroup);
					result.add(ruleGroup);
				}
			}
			if(groupIds.size()==0)
				break;
		}
		return result;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public OobyRuntimeService getObrs() {
		return obrs;
	}

	public void setObrs(OobyRuntimeService obrs) {
		this.obrs = obrs;
	}

	public OobyIdentityService getObis() {
		return obis;
	}

	public void setObis(OobyIdentityService obis) {
		this.obis = obis;
	}
	
}
