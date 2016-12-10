package ooby.workflow.activiti.command;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class GetDepartmentsOfUser implements Command<List<Group>>{
	
	private String userId;
	

	public GetDepartmentsOfUser(String userId) {
		this.userId = userId;
	}

	@SuppressWarnings("unchecked")
	public List<Group> execute(CommandContext commandContext) {
		return commandContext.getDbSqlSession().selectList("getDepartmentsOfUser", userId);
	}

}
