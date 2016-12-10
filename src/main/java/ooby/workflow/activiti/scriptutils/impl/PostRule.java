package ooby.workflow.activiti.scriptutils.impl;

import java.util.List;

public class PostRule implements AssigneeRule{
	private String post;

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public PostRule(String post) {
		this.post = post;
	}

	public List<String> getAuthDeparment(String user) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
