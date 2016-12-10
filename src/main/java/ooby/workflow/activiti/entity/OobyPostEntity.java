package ooby.workflow.activiti.entity;

import org.activiti.engine.impl.persistence.entity.MembershipEntity;

public class OobyPostEntity extends MembershipEntity{

	private static final long serialVersionUID = 4148137918289005884L;
	private String postId;
	@Override
	public Object getPersistentState() {
		return OobyPostEntity.class;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	
}
