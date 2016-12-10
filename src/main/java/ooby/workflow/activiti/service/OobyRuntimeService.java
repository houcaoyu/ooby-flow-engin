package ooby.workflow.activiti.service;

public interface OobyRuntimeService {
	public String KEY_APPLICANT="_applicant";
	public String KEY_DEPARTMENT_OF_APPLICANT="_depofapplicant";
	public String getApplicant(String executionId);
	public void setApplicant(String executionId,String userId);
	public String getDepOfApplicant(String executionId);
	public void setDepOfApplicant(String executionId,String deparmentId);
}
