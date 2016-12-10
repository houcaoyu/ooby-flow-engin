package ooby.workflow.activiti.scriptutils.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupRule implements AssigneeRule{
	private String group;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public GroupRule(String group) {
		this.group = group;
	}
	@Autowired
	private IdentityService identityService;

	public List<String> getAuthDeparment(String user) {
		List<String> result=new ArrayList<String>();
		Group g=identityService.createGroupQuery().groupId(group).singleResult();
		if(g!=null&&"dep".equals(g.getType()))
			result.add(group);
		
		//TODO check the group belong to the user
		return result;
	}
	
}
