package ooby.workflow.activiti.command;

import java.util.List;

import org.activiti.engine.identity.User;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class GetUserByPostCommand implements Command<List<User>>{
	
	private String postId;
	

	public GetUserByPostCommand( String postId) {
		this.postId = postId;
	}

	
	@SuppressWarnings("unchecked")
	public List<User> execute(CommandContext commandContext) {
		return commandContext.getDbSqlSession().selectList("getUserByPost", postId);
	}

}
