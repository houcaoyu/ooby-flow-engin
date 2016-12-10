package ooby.workflow.activiti.test;


import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.test.ActivitiRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ooby.workflow.activiti.service.OobyIdentityService;
import ooby.workflow.activiti.service.OobyRuntimeService;
import ooby.workflow.activiti.service.OobyTaskService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti.cfg.xml")
public class OobyTest {
	@Autowired
	@Rule
	public ActivitiRule activitiRule;

	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;

	@Autowired
	protected HistoryService historyService;

	@Autowired
	protected IdentityService identityService;

	@Autowired
	protected RepositoryService repositoryService;

	@Autowired
	protected ManagementService managementService;

	@Autowired
	protected OobyIdentityService obis;
	
	@Autowired
	protected OobyRuntimeService obrs;
	
	@Autowired
	protected OobyTaskService obts;

	@Before
	public void before() {
		managementService.executeCommand(new Command<Void>() {

			public Void execute(CommandContext commandContext) {
				commandContext.getDbSqlSession().executeSchemaResource("update", "identity", "expand.sql", false);
				return null;
			}
		});
	}

	@After
	public void after() {
		managementService.executeCommand(new Command<Void>() {

			public Void execute(CommandContext commandContext) {
				commandContext.getDbSqlSession().executeSchemaResource("drop", "identity", "drop.sql", false);
				return null;
			}
		});
		managementService.executeCommand(new Command<Object>() {
			public Object execute(CommandContext commandContext) {
				DbSqlSession dbSqlSession = commandContext.getSession(DbSqlSession.class);
				dbSqlSession.dbSchemaDrop();
				dbSqlSession.dbSchemaCreate();
				return null;
			}
		});
	}
}
