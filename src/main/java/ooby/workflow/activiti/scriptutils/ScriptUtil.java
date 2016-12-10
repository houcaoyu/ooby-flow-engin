package ooby.workflow.activiti.scriptutils;

import java.util.Collection;

import org.activiti.engine.delegate.DelegateTask;

public interface ScriptUtil {
	public void senback(DelegateTask delegateTask, String activitiId);

	public void senback(DelegateTask delegateTask, String activitiId, boolean direct);

	// user
	public void user(DelegateTask delegateTask, String userId);

	// group
	public void group(DelegateTask delegateTask, String groupId);

	// user
	public void post(DelegateTask delegateTask, String postId);

	// group
	public void groupWithAllSubGroups(DelegateTask delegateTask, String groupId);

	// group
	public void allSubGroupsUnder(DelegateTask delegateTask, String groupId);

	// user
	public void depAndPost(DelegateTask delegateTask, String departmentId, String postId);


	// user
	public void applicant(DelegateTask delegateTask);

	// group
	public void depOfApplicant(DelegateTask delegateTask);

	// user
	public void depOfApplicantAndPost(DelegateTask delegateTask, String postId);

	// user
	public void depOfApplicantAndGroup(DelegateTask delegateTask, String groupId);

	// group
	public void superiorDepOfApplicant(DelegateTask delegateTask);

	// user
	public void superiorDepOfApplicantAndPost(DelegateTask delegateTask,String postId);

	// user
	public void superiorDepOfApplicantAndGroup(DelegateTask delegateTask,String groupId);

	// group
	public void allSuperiorDepOfApplicant(DelegateTask delegateTask);

	// user
	public void allSuperiorDepOfApplicantAndPost(DelegateTask delegateTask,String postId);

	// user
	public void allSuperiorDepOfApplicantAndGroup(DelegateTask delegateTask,String groupId);

	// group
	public void subDepOfApplicant(DelegateTask delegateTask);

	// user
	public void subDepOfApplicantAndPost(DelegateTask delegateTask,String postId);

	// user
	public void subDepOfApplicantAndGroup(DelegateTask delegateTask,String groupId);

	// group
	public void allSubDepOfApplicant(DelegateTask delegateTask);

	// user
	public void allSubDepOfApplicantAndPost(DelegateTask delegateTask,String postId);

	// user
	public void allSubDepOfApplicantAndGroup(DelegateTask delegateTask,String groupId);

	// user
	public void preAssinee(DelegateTask delegateTask);

	// group
	public void depOfPreAssinee(DelegateTask delegateTask);

	// user
	public void depOfPreAssineeAndPost(DelegateTask delegateTask, String postId);

	// user
	public void depOfPreAssineeAndGroup(DelegateTask delegateTask, String groupId);

	// group
	public void superiorDepOfPreAssinee(DelegateTask delegateTask);

	// user
	public void superiorDepOfPreAssineeAndPost(DelegateTask delegateTask,String postId);

	// user
	public void superiorDepOfPreAssineeAndGroup(DelegateTask delegateTask,String groupId);

	// group
	public void allSuperiorDepOfPreAssinee(DelegateTask delegateTask);

	// user
	public void allSuperiorDepOfPreAssineeAndPost(DelegateTask delegateTask,String postId);

	// user
	public void allSuperiorDepOfPreAssineeAndGroup(DelegateTask delegateTask,String group);
	// group
	public void subDepOfPreAssinee(DelegateTask delegateTask);

	// user
	public void subDepOfPreAssineeAndPost(DelegateTask delegateTask,String postId);

	// user
	public void subDepOfPreAssineeAndGroup(DelegateTask delegateTask,String groupId);
	// group
	public void allSubDepOfPreAssinee(DelegateTask delegateTask);

	// user
	public void allSubDepOfPreAssineeAndPost(DelegateTask delegateTask,String postId);

	// user
	public void allSubDepOfPreAssineeAndGroup(DelegateTask delegateTask,String groupId);

	void groups(DelegateTask task, Collection<String> groups);

	void users(DelegateTask task, Collection<String> users);

	void subGroupsUnder(DelegateTask task, String groupId);
}
