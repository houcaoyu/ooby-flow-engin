package ooby.workflow.activiti.command;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class GetSubGroupCommand implements Command<List<Group>>{
	
	private String groupId;
	

	public GetSubGroupCommand( String groupId) {
		this.groupId = groupId;
	}

	
	@SuppressWarnings("unchecked")
	public List<Group> execute(CommandContext commandContext) {
		return commandContext.getDbSqlSession().selectList("getSubGroup", groupId);
	}

}
