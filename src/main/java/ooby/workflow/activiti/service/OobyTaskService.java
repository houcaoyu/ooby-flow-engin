package ooby.workflow.activiti.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;

import ooby.workflow.activiti.entity.SendBackDefinitionEntity;
import ooby.workflow.activiti.scriptutils.impl.AssigneeRule;

public interface OobyTaskService {
	String KEY_DEPARTMENT_ID="_pre_department_id";
	String KEY_PRE_TASK_ID="_pre_task_id"; 
	String KEY_SEND_BACK_TARGET = "_send_back_target";
	String KEY_SEND_BACK_TYPE = "_send_type";
	String SEND_BACK_TYPE_VALUE_RECIRCLE = "recircle";
	String SEND_BACK_TYPE_VALUE_DIRECT_SUBMIT = "direct_submit";
	String KEY_OPERATION = "_operation";
	String OPERATION_VALUE_SEND_BACK = "Send Back";
	String OPERATION_VALUE_APPROVE = "Approve";
	String OPERATION_VALUE_APPLY = "Apply";

		//请求
		void claim(String taskId, String userId);

		//申请
		void apply(String taskId, String userId, String depId);

		// 承认
		void approve(String taskId,String userId,String depId);

		// 退回
		void sendBack(String currentTaskId,String userId, String targetActivityId);

		//(打回以后)重新提交
		void resubmit(String taskId,String userId);

		// 撤销
		void cancel();

		// 会签
		void huiqian();

		// 终止
		void stop();

		// 拒绝
		void reject();

		Map<String,SendBackDefinitionEntity> getSendBackTargets(String taskId);

		boolean canSendBack(String taskId);

		String getPreAssignee(String taskId);

		String getPreActivityId(String taskId);

		String getOperation(String taskId);

		String getPreDepartment(String taskId);

		String getPreTaskId(String taskId);

		String getDepartment(String taskId);

		String getAssignee(String taskId);

		String getActivityId(String taskId);

		List<String> getAuthDeparment(String taskId, String userId);
}
