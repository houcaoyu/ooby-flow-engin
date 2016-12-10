package ooby.workflow.activiti;

import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import ooby.workflow.activiti.entity.OobyGroupEntity;
import ooby.workflow.activiti.test.OobyTest;

public class TestUserGroupSetting extends OobyTest{
	@Test
	@Deployment(resources="testusergroupsetting1.bpmn")
	public void testUserAndGroup(){
		preparedata();
		ProcessInstance instance=runtimeService.startProcessInstanceByKey("hello");
		Task task=taskService.createTaskQuery().taskCandidateUser("u1").singleResult();
		assertThat(task,notNullValue());
		task=taskService.createTaskQuery().taskCandidateUser("u2").singleResult();
		assertThat(task,notNullValue());
		task=taskService.createTaskQuery().taskCandidateUser("u3").singleResult();
		assertThat(task,nullValue());
	}
	@Test
	@Deployment(resources="testusergroupsetting2.bpmn")
	public void testPost(){
		preparedata();
		ProcessInstance instance=runtimeService.startProcessInstanceByKey("hello");
		Task task=taskService.createTaskQuery().taskCandidateUser("u1").singleResult();
		assertThat(task,notNullValue());
		task=taskService.createTaskQuery().taskCandidateUser("u2").singleResult();
		assertThat(task,notNullValue());
		task=taskService.createTaskQuery().taskCandidateUser("u3").singleResult();
		assertThat(task,nullValue());
		
		task=taskService.createTaskQuery().taskCandidateUser("u2").singleResult();
		taskService.complete(task.getId());
		
		task=taskService.createTaskQuery().taskCandidateUser("u1").singleResult();
		assertThat(task,notNullValue());
		task=taskService.createTaskQuery().taskCandidateUser("u3").singleResult();
		assertThat(task,notNullValue());
		task=taskService.createTaskQuery().taskCandidateUser("u2").singleResult();
		assertThat(task,nullValue());
	}
	
	
	private void preparedata() {
		OobyGroupEntity group=new OobyGroupEntity();
		group.setId("d1");
		group.setName("a");
		group.setType("dep");
		obis.saveGroup(group);
		
		group=new OobyGroupEntity();
		group.setId("d2.1");
		group.setName("b1");
		group.setType("dep");
		group.setParent("d1");
		obis.saveGroup(group);
		
		group=new OobyGroupEntity();
		group.setId("d2.2");
		group.setName("b2");
		group.setType("dep");
		group.setParent("d1");
		obis.saveGroup(group);
		
		group=new OobyGroupEntity();
		group.setId("d3.1");
		group.setName("c1");
		group.setType("dep");
		group.setParent("d2.1");
		obis.saveGroup(group);
		
		group=new OobyGroupEntity();
		group.setId("d3.2");
		group.setName("c2");
		group.setType("dep");
		group.setParent("d2.1");
		obis.saveGroup(group);
		
		group=new OobyGroupEntity();
		group.setId("d3.3");
		group.setName("c3");
		group.setType("dep");
		group.setParent("d2.2");
		obis.saveGroup(group);
		
		User user=identityService.newUser("u1");
		user.setFirstName("hou");
		identityService.saveUser(user);
		user=identityService.newUser("u2");
		user.setFirstName("wang");
		identityService.saveUser(user);
		user=identityService.newUser("u3");
		user.setFirstName("zhang");
		identityService.saveUser(user);
		
		group=new OobyGroupEntity();
		group.setId("p1");
		group.setName("post1");
		group.setType("post");
		obis.saveGroup(group);
		group=new OobyGroupEntity();
		group.setId("p2");
		group.setName("post2");
		group.setType("post");
		obis.saveGroup(group);
		
		obis.createDepMembership("u1", "d3.1", "p1");
		obis.createDepMembership("u2", "d1", "p1");
		obis.createDepMembership("u3", "d3.2", "p2");
	}
}
