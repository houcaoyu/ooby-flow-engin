package ooby.workflow.activiti.service.impl;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricVariableInstance;

import ooby.workflow.activiti.service.OobyRuntimeService;

public class OobyRuntimeServiceImpl implements OobyRuntimeService{
	private RuntimeService runtimeService;
	private HistoryService historyService;
	public String getApplicant(String executionId) {
		return (String)getVariable(executionId,KEY_APPLICANT);
	}
	
	
	public void setApplicant(String executionId, String userId) {
		runtimeService.setVariable(executionId, KEY_APPLICANT, userId);
	}


	public String getDepOfApplicant(String executionId) {
		return (String)getVariable(executionId, KEY_DEPARTMENT_OF_APPLICANT);
	}


	public void setDepOfApplicant(String executionId,String deparmentId) {
		runtimeService.setVariable(executionId, KEY_DEPARTMENT_OF_APPLICANT, deparmentId);
	}
	public Object getVariable(String executionId,String variableName){
		HistoricVariableInstance var=historyService.createHistoricVariableInstanceQuery().processInstanceId(executionId).variableName(variableName).singleResult();
		return var==null?null:var.getValue();
	}
	public RuntimeService getRuntimeService() {
		return runtimeService;
	}
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}


	public HistoryService getHistoryService() {
		return historyService;
	}


	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	
	
}
