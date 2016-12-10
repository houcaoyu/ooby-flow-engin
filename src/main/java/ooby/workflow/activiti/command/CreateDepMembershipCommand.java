package ooby.workflow.activiti.command;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import ooby.workflow.activiti.entity.OobyPostEntity;

public class CreateDepMembershipCommand implements Command<Void>{
	private String userId;
	private String groupId;
	private String postId;

	
	public CreateDepMembershipCommand(String userId, String groupId, String postId) {
		this.userId = userId;
		this.groupId = groupId;
		this.postId = postId;
	}


	public Void execute(CommandContext commandContext) {
		OobyPostEntity entity=new OobyPostEntity();
		entity.setUserId(userId);
		entity.setGroupId(groupId);
		entity.setPostId(postId);
		commandContext.getDbSqlSession().insert(entity);
		return null;
	}

	

}
