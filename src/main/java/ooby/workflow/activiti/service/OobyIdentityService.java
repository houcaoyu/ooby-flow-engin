package ooby.workflow.activiti.service;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;

import ooby.workflow.activiti.command.GetDepartmentsOfUser;
import ooby.workflow.activiti.entity.OobyGroupEntity;

public interface OobyIdentityService {
	public String GROUP_TYPE_DEPARTMENT="department";
	public String GROUP_TYPE_POST="post";
	void saveGroup(OobyGroupEntity group);
	void createDepMembership(String userId,String groupId,String postId);
	List<User> getUserByDepPost(String groupId, String postId);
	List<User> getUserByPost(String postId);
	List<Group> getSubGroupOf(String groupId);
	List<User> getUserInGroupsSameTime(String groupId1,String groupId2);
	List<Group> getAllSubGroupOf(String groupId);
	OobyGroupEntity getGroup(String groupId);
	Group getSuperiorGroupOf(String groupId);
	List<Group> getAllSuperiorGroupOf(String groupId);
	List<Group> getDepartmentsOfUser(String userId);
	
}
