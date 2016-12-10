package ooby.workflow.activiti;

import static org.junit.Assert.*;

import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.test.TestHelper;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.hamcrest.CoreMatchers.*;

import ooby.workflow.activiti.entity.SendBackDefinitionEntity;
import ooby.workflow.activiti.service.OobyTaskService;
import ooby.workflow.activiti.test.OobyTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti.cfg.xml")
public class TestSendBack extends OobyTest{

	
	@Test
	@Deployment(resources = {"testsendback.bpmn"})
	public void testInsertGroup(){
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("hello");
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		obts.apply(task.getId(), "caoyu", "dep3");
		
		task = taskService.createTaskQuery().taskCandidateUser("wang").singleResult();
		obts.claim(task.getId(), "wang");
		obts.approve(task.getId(),"wang","dep1");
		
		task = taskService.createTaskQuery().taskCandidateUser("zhang").singleResult();
		obts.claim(task.getId(), "zhang");
		Map<String,SendBackDefinitionEntity> map=obts.getSendBackTargets(task.getId());
		assertThat(map, not(nullValue()));
		assertThat(map.get("usertask1"),not(nullValue()));
		assertThat(map.get("usertask1").getActivitiId(),is("usertask1"));
		assertThat(map.get("usertask1").isDirect(),is(false));
		assertThat(map.get("usertask2"),not(nullValue()));
		assertThat(map.get("usertask2").getActivitiId(),is("usertask2"));
		assertThat(map.get("usertask2").isDirect(),is(true));
		
		obts.sendBack(task.getId(),"zhang", "usertask1");
		
		task=taskService.createTaskQuery().taskAssignee("caoyu").singleResult();
		assertThat(obts.getDepartment(task.getId()),is("dep3"));
		assertThat(task,not(nullValue()));
		obts.resubmit(task.getId(),"caoyu");
		assertThat(obts.getDepartment(task.getId()),is("dep3"));
		task = taskService.createTaskQuery().taskAssignee("zhang").singleResult();
		assertThat(task,nullValue());
		task = taskService.createTaskQuery().taskCandidateUser("wang").singleResult();
		assertThat(task,not(nullValue()));
		obts.approve(task.getId(),"wang","dep1");
		assertThat(obts.getOperation(task.getId()),is("Approve"));
		
		task = taskService.createTaskQuery().taskCandidateUser("zhang").singleResult();
		assertThat(task,not(nullValue()));
		
		obts.claim(task.getId(), "zhang");
		obts.sendBack(task.getId(),"zhang", "usertask2");
		task=taskService.createTaskQuery().taskAssignee("wang").singleResult();
		assertThat(task,not(nullValue()));
		obts.resubmit(task.getId(),"wang");
		
		
		task = taskService.createTaskQuery().taskAssignee("zhang").singleResult();
		assertThat(task,not(nullValue()));
		assertThat(obrs.getApplicant(processInstance.getId()),is("caoyu"));
		obts.approve(task.getId(), "zhang", "dep1");
		TestHelper.assertProcessEnded(activitiRule.getProcessEngine(), processInstance.getId());
		assertThat(obrs.getApplicant(processInstance.getId()),is("caoyu"));
	}

}
