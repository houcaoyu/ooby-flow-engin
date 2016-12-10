package ooby.workflow.activiti.command;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import ooby.workflow.activiti.entity.OobyGroupEntity;

public class GetGroupCommand implements Command<OobyGroupEntity>{
	
	private String groupId;
	

	public GetGroupCommand( String groupId) {
		this.groupId = groupId;
	}

	
	public OobyGroupEntity execute(CommandContext commandContext) {
		return commandContext.getDbSqlSession().selectById(OobyGroupEntity.class, groupId);
	}

}
