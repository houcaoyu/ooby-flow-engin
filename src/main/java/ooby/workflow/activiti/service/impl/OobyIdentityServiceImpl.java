package ooby.workflow.activiti.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;

import ooby.workflow.activiti.command.CreateDepMembershipCommand;
import ooby.workflow.activiti.command.GetGroupCommand;
import ooby.workflow.activiti.command.GetDepartmentsOfUser;
import ooby.workflow.activiti.command.GetSubGroupCommand;
import ooby.workflow.activiti.command.GetUserByDepPostCommand;
import ooby.workflow.activiti.command.GetUserByPostCommand;
import ooby.workflow.activiti.command.GetUserInGroupsSameTimeCommand;
import ooby.workflow.activiti.entity.OobyGroupEntity;
import ooby.workflow.activiti.service.OobyIdentityService;

public class OobyIdentityServiceImpl implements OobyIdentityService {
	private ManagementService managementService;
	private IdentityService identityService;

	public void saveGroup(OobyGroupEntity group) {
		identityService.saveGroup(group);
	}

	public void createDepMembership(String userId, String groupId, String postId) {
		identityService.createMembership(userId, groupId);
		managementService.executeCommand(new CreateDepMembershipCommand(userId, groupId, postId));
	}
	
	public List<User> getUserByDepPost(String depId,String postId){
		return managementService.executeCommand(new GetUserByDepPostCommand(depId, postId));
	}
	
	public List<User> getUserByPost(String postId){
		return managementService.executeCommand(new GetUserByPostCommand(postId));
	}
	
	public OobyGroupEntity getGroup(String groupId){
		return managementService.executeCommand(new GetGroupCommand(groupId));
	}

	public List<Group> getSubGroupOf(String groupId) {
		return managementService.executeCommand(new GetSubGroupCommand(groupId));
	}
	
	
	public List<Group> getAllSubGroupOf(String groupId) {
		List<Group> allSubGroups = new ArrayList<Group>();
		List<Group> list = getSubGroupOf(groupId);
		for (Group group : list) {
			allSubGroups.add(group);
			List<Group> subGroups = getAllSubGroupOf(group.getId());
			allSubGroups.addAll(subGroups);
		}
		return allSubGroups;
	}
	public Group getSuperiorGroupOf(String groupId) {
		OobyGroupEntity group=getGroup(groupId);
		if(group.getParent()==null)
			return null;
		else
			return getGroup(group.getParent());
	}
	public List<Group> getAllSuperiorGroupOf(String groupId) {
		List<Group> list=new ArrayList<Group>();
		OobyGroupEntity group=(OobyGroupEntity)getSuperiorGroupOf(groupId);
		while(group!=null){
			list.add(group);
			group=(OobyGroupEntity)getSuperiorGroupOf(group.getId());
		}
		return list;
	}
	public List<User> getUserInGroupsSameTime(String groupId1,String groupId2) {
		return managementService.executeCommand(new GetUserInGroupsSameTimeCommand(groupId1, groupId2));
	}
	public List<Group> getDepartmentsOfUser(String userId){
		return managementService.executeCommand(new GetDepartmentsOfUser(userId));
	}

	public ManagementService getManagementService() {
		return managementService;
	}

	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}

	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

}
