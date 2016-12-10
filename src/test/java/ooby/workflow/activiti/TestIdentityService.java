package ooby.workflow.activiti;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ooby.workflow.activiti.entity.OobyGroupEntity;
import ooby.workflow.activiti.entity.OobyPostEntity;
import ooby.workflow.activiti.test.OobyTest;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti.cfg.xml")
public class TestIdentityService extends OobyTest{
	
	
	
	@Test
	public void testInsertGroup(){
		OobyGroupEntity group=new OobyGroupEntity();
		group.setId("1");
		group.setName("a");
		group.setParent("0");
		identityService.saveGroup(group);
		group=new OobyGroupEntity();
		group.setId("1.1");
		group.setName("b");
		group.setParent("1");
		identityService.saveGroup(group);
	}
	
	@Test
	public void testPost(){
		User user=identityService.newUser("u1");
		user.setFirstName("hou");
		identityService.saveUser(user);
		
		OobyGroupEntity group=new OobyGroupEntity();
		group.setId("dep1");
		group.setName("a");
		group.setType("dep");
		group.setParent("0");
		identityService.saveGroup(group);
		
		Group g=identityService.newGroup("post1");
		g.setName("leader");
		g.setType("post");
		identityService.saveGroup(g);
		
		obis.createDepMembership("u1", "dep1", "post1");
		
		OobyPostEntity post=new OobyPostEntity();
		post.setGroupId("dep1");
		post.setUserId("u1");
		post.setPostId("post1");
		
		List<User> list=obis.getUserByDepPost("dep1", "post1");
		assertThat(list.size(),is(1));
		assertThat(list.get(0).getId(),is("u1"));
		assertThat(list.get(0).getFirstName(),is("hou"));
		
		
	}
	
	@Test
	public void testSubGroup(){
		
		preparedata();
		
		List<Group> list=obis.getSubGroupOf("d1");
		assertThat(list.size(),is(2));
		Group g=list.get(0);
		assertThat(g.getName(),is("b1"));
		g=list.get(1);
		assertThat(g.getName(),is("b2"));
		
		list=obis.getAllSubGroupOf("d1");
		assertThat(list.size(),is(5));
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
	}
	
	@Test
	public void testSuperniorGroup(){
		preparedata();
		
		Group group=obis.getSuperiorGroupOf("d3.3");
		assertThat(group.getName(), is("b2"));
		
		List<Group> list=obis.getAllSuperiorGroupOf("d3.2");
		assertThat(list.size(),is(2));
		assertThat(list.get(0).getName(),is("b1"));
		assertThat(list.get(1).getName(),is("a"));
	}
	
	@Test
	public void testUserInGroupSameTime(){
		preparedata();
		Group group=identityService.newGroup("role1");
		group.setName("somerole");
		group.setType("role");
		identityService.saveGroup(group);
		group=identityService.newGroup("role2");
		group.setName("anotherrole");
		group.setType("role");
		identityService.saveGroup(group);
		
		User user=identityService.newUser("u1");
		identityService.saveUser(user);
		user=identityService.newUser("u2");
		identityService.saveUser(user);
		user=identityService.newUser("u3");
		identityService.saveUser(user);
		
		identityService.createMembership("u1", "role1");
		identityService.createMembership("u2", "role2");
		identityService.createMembership("u3", "role2");
		
		identityService.createMembership("u1", "d2.1");
		identityService.createMembership("u2", "d2.1");
		identityService.createMembership("u2", "d2.2");
		identityService.createMembership("u3", "d2.2");
		
		List<User> list=obis.getUserInGroupsSameTime("d2.1", "role1");
		assertThat(list.size(),is(1));
		assertThat(list.get(0).getId(), is("u1"));
		
		list=obis.getUserInGroupsSameTime("d2.1", "role2");
		assertThat(list.size(),is(1));
		assertThat(list.get(0).getId(), is("u2"));
		
		list=obis.getUserInGroupsSameTime("d2.2", "role2");
		assertThat(list.size(),is(2));
		assertThat(list.get(0).getId(), is("u2"));
		assertThat(list.get(1).getId(), is("u3"));
		
	}
}
