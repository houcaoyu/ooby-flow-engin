package ooby.workflow.activiti.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.User;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class GetUserInGroupsSameTimeCommand implements Command<List<User>>{
	private Map<String,String> map=new HashMap<String, String>();
	
	public GetUserInGroupsSameTimeCommand(String groupId1, String groupId2) {
		map.put("groupId1", groupId1);
		map.put("groupId2", groupId2);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> execute(CommandContext commandContext) {
		return commandContext.getDbSqlSession().selectList("getUserInGroupsSameTime", map);
	}
	
}
