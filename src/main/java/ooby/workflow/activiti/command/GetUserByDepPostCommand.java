package ooby.workflow.activiti.command;

import java.util.List;

import org.activiti.engine.identity.User;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import ooby.workflow.activiti.entity.OobyPostEntity;

public class GetUserByDepPostCommand implements Command<List<User>>{
	
	private String groupId;
	private String postId;
	

	public GetUserByDepPostCommand(String groupId, String postId) {
		super();
		this.groupId = groupId;
		this.postId = postId;
	}

	
	@SuppressWarnings("unchecked")
	public List<User> execute(CommandContext commandContext) {
		OobyPostEntity post=new OobyPostEntity();
		post.setGroupId(groupId);
		post.setPostId(postId);
		return commandContext.getDbSqlSession().selectList("getUserByDepPost", post);
	}

}
