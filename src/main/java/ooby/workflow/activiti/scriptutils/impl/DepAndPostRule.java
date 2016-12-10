package ooby.workflow.activiti.scriptutils.impl;

import java.util.List;

public class DepAndPostRule implements AssigneeRule{
	private String dep;
	private String post;
	public String getDep() {
		return dep;
	}
	public void setDep(String dep) {
		this.dep = dep;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	
	public DepAndPostRule(String dep, String post) {
		this.dep = dep;
		this.post = post;
	}
	public List<String> getAuthDeparment(String user) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
