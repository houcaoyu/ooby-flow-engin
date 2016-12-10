package ooby.workflow.activiti.scriptutils.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;

import ooby.workflow.activiti.service.OobyIdentityService;

public class UserRule implements AssigneeRule {
	@Autowired
	private OobyIdentityService obis;
	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public UserRule(String user) {
		this.user = user;
	}

	public List<String> getAuthDeparment(String user) {
		List<String> result = new ArrayList<String>();
		if (user.equals(this.user)) {
			List<Group> groups = obis.getDepartmentsOfUser(user);
			for (Group group : groups) {
				result.add(group.getId());
			}
		}
		return result;
	}

}
