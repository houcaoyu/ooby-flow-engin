package ooby.workflow.activiti.scriptutils.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;

import ooby.workflow.activiti.entity.SendBackDefinitionEntity;
import ooby.workflow.activiti.scriptutils.ScriptUtil;
import ooby.workflow.activiti.service.OobyIdentityService;
import ooby.workflow.activiti.service.OobyRuntimeService;
import ooby.workflow.activiti.service.OobyTaskService;

public class ScriptUtilImpl implements ScriptUtil {

	private OobyIdentityService obis;
	private OobyRuntimeService obrs;
	private OobyTaskService obts;

	public void senback(DelegateTask task, String activitiId) {
		senback(task, activitiId, false);

	}

	public void senback(DelegateTask task, String activitiId, boolean direct) {
		@SuppressWarnings("unchecked")
		Map<String, SendBackDefinitionEntity> map = (Map<String, SendBackDefinitionEntity>) task
				.getVariableLocal(OobyTaskService.KEY_SEND_BACK_TARGET);
		if (map == null) {
			map = new HashMap<String, SendBackDefinitionEntity>();
		}
		SendBackDefinitionEntity entity = new SendBackDefinitionEntity();
		entity.setActivitiId(activitiId);
		entity.setDirect(direct);
		map.put(activitiId, entity);
		task.setVariableLocal(OobyTaskService.KEY_SEND_BACK_TARGET, map);
	}

	public void user(DelegateTask task, String userId) {
		user(task, userId, true);

	}

	public void group(DelegateTask task, String groupId) {
		group(task, groupId, true);

	}

	protected void user(DelegateTask task, String userId, boolean registerRule) {
		task.addCandidateUser(userId);
		if (registerRule)
			registerRule(task, new UserRule(userId));
	}

	protected void group(DelegateTask task, String groupId, boolean registerRule) {
		task.addCandidateGroup(groupId);
		if (registerRule)
			registerRule(task, new UserRule(groupId));
	}

	public void users(DelegateTask task, Collection<String> users) {
		users(task, users, true);
	}

	public void groups(DelegateTask task, Collection<String> groups) {
		groups(task, groups, true);
	}

	@SuppressWarnings("unchecked")
	private void registerRule(DelegateTask task, AssigneeRule rule) {
		List<AssigneeRule> list = (List<AssigneeRule>) task.getVariableLocal("_assignee_rule");
		if (list == null) {
			list = new ArrayList<AssigneeRule>();
		}
		list.add(rule);
		task.setVariableLocal("_assignee_rule", list);
	}

	protected void users(DelegateTask task, Collection<String> users, boolean registerRule) {
		for (String user : users) {
			user(task, user, registerRule);
		}
	}

	protected void groups(DelegateTask task, Collection<String> groups, boolean registerRule) {
		for (String group : groups)
			group(task, group, registerRule);
	}

	public void post(DelegateTask task, String postId) {
		List<User> list = obis.getUserByPost(postId);
		users(task, userIds(list), false);
		registerRule(task,new PostRule(postId));
	}

	public void groupWithAllSubGroups(DelegateTask task, String groupId) {
		task.addCandidateGroup(groupId);
		allSubGroupsUnder(task, groupId);
	}

	public void allSubGroupsUnder(DelegateTask task, String groupId) {
		List<Group> list = obis.getAllSubGroupOf(groupId);
		groups(task, groupIds(list));
	}

	public void subGroupsUnder(DelegateTask task, String groupId) {
		List<Group> list = obis.getSubGroupOf(groupId);
		groups(task, groupIds(list));
	}

	public void depAndPost(DelegateTask task, String departmentId, String postId) {
		List<User> list = obis.getUserByDepPost(departmentId, postId);
		users(task, userIds(list), false);
		registerRule(task,new DepAndPostRule(departmentId, postId));
	}

	private List<String> userIds(List<User> list) {
		List<String> ids = new ArrayList<String>();
		for (User user : list) {
			ids.add(user.getId());
		}
		return ids;
	}

	private List<String> groupIds(List<Group> list) {
		List<String> ids = new ArrayList<String>();
		for (Group group : list) {
			ids.add(group.getId());
		}
		return ids;
	}

	public void groupAnd(DelegateTask task, String groupId1, String groupId2) {
		List<User> list = obis.getUserInGroupsSameTime(groupId1, groupId2);
		users(task, userIds(list), false);
		registerRule(task,new GroupAndRule(groupId1, groupId2));
	}

	public void applicant(DelegateTask task) {
		user(task, obrs.getApplicant(task.getId()));

	}

	public void depOfApplicant(DelegateTask task) {
		group(task, obrs.getDepOfApplicant(task.getId()));

	}

	public void depOfApplicantAndPost(DelegateTask task, String postId) {
		String dep = obrs.getDepOfApplicant(task.getId());
		depAndPost(task, dep, postId);

	}

	public void depOfApplicantAndGroup(DelegateTask task, String groupId) {
		String dep = obrs.getDepOfApplicant(task.getId());
		groupAnd(task, dep, groupId);
	}

	private String superiorDepOfApp(DelegateTask task) {
		String dep = obrs.getDepOfApplicant(task.getId());
		String superiorDep = obis.getGroup(dep).getParent();
		return superiorDep;
	}

	public void superiorDepOfApplicant(DelegateTask task) {
		group(task, superiorDepOfApp(task));
	}

	public void superiorDepOfApplicantAndPost(DelegateTask task, String postId) {
		depAndPost(task, superiorDepOfApp(task), postId);

	}

	public void superiorDepOfApplicantAndGroup(DelegateTask task, String groupId) {
		groupAnd(task, groupId, superiorDepOfApp(task));
	}

	public void preAssinee(DelegateTask task) {
		user(task, obts.getPreAssignee(task.getId()));
	}

	public void depOfPreAssinee(DelegateTask task) {
		group(task, obts.getPreDepartment(task.getId()));

	}

	public void depOfPreAssineeAndPost(DelegateTask task, String postId) {
		String dep = obts.getPreDepartment(task.getId());
		depAndPost(task, dep, postId);
	}

	public void depOfPreAssineeAndGroup(DelegateTask task, String groupId) {
		groupAnd(task, groupId, obts.getPreDepartment(task.getId()));
	}

	private String superiorDepOfPreAsignee(DelegateTask task) {
		String dep = obts.getPreDepartment(task.getId());
		String superiorDep = obis.getGroup(dep).getParent();
		return superiorDep;
	}

	public void superiorDepOfPreAssinee(DelegateTask task) {
		group(task, superiorDepOfPreAsignee(task));
	}

	public void superiorDepOfPreAssineeAndPost(DelegateTask task, String postId) {
		depAndPost(task, superiorDepOfPreAsignee(task), postId);
	}

	public void superiorDepOfPreAssineeAndGroup(DelegateTask task, String groupId) {
		groupAnd(task, groupId, superiorDepOfPreAsignee(task));
	}

	public void allSuperiorDepOfApplicant(DelegateTask task) {
		String dep = obrs.getDepOfApplicant(task.getId());
		List<Group> list = obis.getAllSuperiorGroupOf(dep);
		groups(task, groupIds(list));
	}

	public void allSuperiorDepOfApplicantAndPost(DelegateTask task, String postId) {
		String dep = obrs.getDepOfApplicant(task.getId());
		List<Group> list = obis.getAllSuperiorGroupOf(dep);
		for (Group group : list) {
			depAndPost(task, group.getId(), postId);
		}

	}

	public void allSuperiorDepOfApplicantAndGroup(DelegateTask task, String groupId) {
		String dep = obrs.getDepOfApplicant(task.getId());
		List<Group> list = obis.getAllSuperiorGroupOf(dep);
		for (Group group : list) {
			groupAnd(task, group.getId(), groupId);
		}

	}

	public void subDepOfApplicant(DelegateTask task) {
		String dep = obrs.getDepOfApplicant(task.getId());
		subGroupsUnder(task, dep);
	}

	public void subDepOfApplicantAndPost(DelegateTask task, String postId) {
		String dep = obrs.getDepOfApplicant(task.getId());
		List<Group> list = obis.getSubGroupOf(dep);
		for (Group group : list) {
			depAndPost(task, group.getId(), postId);
		}

	}

	public void subDepOfApplicantAndGroup(DelegateTask task, String groupId) {
		String dep = obrs.getDepOfApplicant(task.getId());
		List<Group> list = obis.getSubGroupOf(dep);
		for (Group group : list) {
			groupAnd(task, group.getId(), groupId);
		}

	}

	public void allSubDepOfApplicant(DelegateTask task) {
		String dep = obrs.getDepOfApplicant(task.getId());
		List<Group> list = obis.getAllSubGroupOf(dep);
		groups(task, groupIds(list));

	}

	public void allSubDepOfApplicantAndPost(DelegateTask task, String postId) {
		String dep = obrs.getDepOfApplicant(task.getId());
		List<Group> list = obis.getAllSubGroupOf(dep);
		for (Group group : list) {
			depAndPost(task, group.getId(), postId);
		}
	}

	public void allSubDepOfApplicantAndGroup(DelegateTask task, String groupId) {
		String dep = obrs.getDepOfApplicant(task.getId());
		List<Group> list = obis.getAllSubGroupOf(dep);
		for (Group group : list) {
			groupAnd(task, group.getId(), groupId);
		}
	}

	public void allSuperiorDepOfPreAssinee(DelegateTask task) {
		String dep = obts.getPreDepartment(task.getId());
		List<Group> list = obis.getAllSuperiorGroupOf(dep);
		groups(task, groupIds(list));
	}

	public void allSuperiorDepOfPreAssineeAndPost(DelegateTask task, String postId) {
		String dep = obts.getPreDepartment(task.getId());
		List<Group> list = obis.getAllSuperiorGroupOf(dep);
		for (Group group : list) {
			depAndPost(task, group.getId(), postId);
		}
	}

	public void allSuperiorDepOfPreAssineeAndGroup(DelegateTask task, String groupId) {
		String dep = obts.getPreDepartment(task.getId());
		List<Group> list = obis.getAllSuperiorGroupOf(dep);
		for (Group group : list) {
			groupAnd(task, group.getId(), groupId);
		}

	}

	public void subDepOfPreAssinee(DelegateTask task) {
		String dep = obts.getPreDepartment(task.getId());
		subGroupsUnder(task, dep);
	}

	public void subDepOfPreAssineeAndPost(DelegateTask task, String postId) {
		String dep = obts.getPreDepartment(task.getId());
		List<Group> list = obis.getSubGroupOf(dep);
		for (Group group : list) {
			depAndPost(task, group.getId(), postId);
		}
	}

	public void subDepOfPreAssineeAndGroup(DelegateTask task, String groupId) {
		String dep = obts.getPreDepartment(task.getId());
		List<Group> list = obis.getSubGroupOf(dep);
		for (Group group : list) {
			groupAnd(task, group.getId(), groupId);
		}
	}

	public void allSubDepOfPreAssinee(DelegateTask task) {
		String dep = obts.getPreDepartment(task.getId());
		List<Group> list = obis.getAllSubGroupOf(dep);
		groups(task, groupIds(list));
	}

	public void allSubDepOfPreAssineeAndPost(DelegateTask task, String postId) {
		String dep = obts.getPreDepartment(task.getId());
		List<Group> list = obis.getAllSubGroupOf(dep);
		for (Group group : list) {
			depAndPost(task, group.getId(), postId);
		}
	}

	public void allSubDepOfPreAssineeAndGroup(DelegateTask task, String groupId) {
		String dep = obts.getPreDepartment(task.getId());
		List<Group> list = obis.getAllSubGroupOf(dep);
		for (Group group : list) {
			groupAnd(task, group.getId(), groupId);
		}
	}

	public OobyIdentityService getObis() {
		return obis;
	}

	public void setObis(OobyIdentityService obis) {
		this.obis = obis;
	}

	public OobyRuntimeService getObrs() {
		return obrs;
	}

	public void setObrs(OobyRuntimeService obrs) {
		this.obrs = obrs;
	}

	public OobyTaskService getObts() {
		return obts;
	}

	public void setObts(OobyTaskService obts) {
		this.obts = obts;
	}

}
