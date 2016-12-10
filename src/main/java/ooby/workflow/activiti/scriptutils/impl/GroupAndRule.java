package ooby.workflow.activiti.scriptutils.impl;

import java.util.List;

public class GroupAndRule implements AssigneeRule{
	private String group1;
	private String group2;
	public String getGroup1() {
		return group1;
	}
	public void setGroup1(String group1) {
		this.group1 = group1;
	}
	public String getGroup2() {
		return group2;
	}
	public void setGroup2(String group2) {
		this.group2 = group2;
	}
	
	public GroupAndRule(String group1, String group2) {
		this.group1 = group1;
		this.group2 = group2;
	}
	public List<String> getAuthDeparment(String user) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
