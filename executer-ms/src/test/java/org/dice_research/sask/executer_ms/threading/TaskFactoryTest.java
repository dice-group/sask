package org.dice_research.sask.executer_ms.threading;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dice_research.sask_commons.workflow.Link;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.junit.*;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author André Sonntag
 *
 */
public class TaskFactoryTest {
	
	RestTemplate restTemplate;
	Workflow wf;
	Set<Operator> opSet;
	Operator fredOp;
	
	@Before
	public void setUp() {
		
		restTemplate = new RestTemplate();
		
		Map<String, String> outputs1 = new HashMap<>();
		outputs1.put("id1_1", "NL");

		Operator o1 = new Operator();
		o1.setContent("/text.txt");
		o1.setId("id1");
		o1.setType("file");
		o1.setOutputs(outputs1);
		
		// o2
		Map<String, String> inputs2 = new HashMap<>();
		inputs2.put("id2_1", "NL");
		
		Map<String, String> outputs2 = new HashMap<>();
		outputs2.put("id2_1", "RDF");
		
		fredOp = new Operator();
		fredOp.setContent("FRED-MS");
		fredOp.setId("id2");
		fredOp.setType("extractor");
		fredOp.setInputs(inputs2);
		fredOp.setOutputs(outputs2);
		
		// o3
		Map<String, String> inputs3 = new HashMap<>();
		inputs3.put("id3_1", "RDF");
		
		Operator o3 = new Operator();
		o3.setContent("sask");
		o3.setId("id3");
		o3.setType("db");
		o3.setInputs(inputs3);
		
		// l1
		Link l1 = new Link();
		l1.setFromOperator("id1");
		l1.setToOperator("id2");
		l1.setFromConnector("id1_1");
		l1.setToConnector("id2_1");
		
		// l2
		Link l2 = new Link();
		l2.setFromOperator("id2");
		l2.setToOperator("id3");
		l2.setFromConnector("id2_1");
		l2.setToConnector("id3_1");
		
		// workflow
		wf = new Workflow();
		wf.getOperators().put("id1", o1);
		wf.getOperators().put("id2", fredOp);
		wf.getOperators().put("id3", o3);
		wf.getLinks().add(l1);
		wf.getLinks().add(l2);
		
		opSet = wf.getStartOperatorSet();

	}

	@Test
	public void testReturnType() {
		Set<Runnable> taskSet = TaskFactory.createTasks(restTemplate, wf, opSet);
		assertTrue(taskSet.iterator().next() instanceof PullTask);
	}
	
	@Test
	public void testInvokeWithAdditionalParameter() {
		Set<Runnable> taskSet = TaskFactory.createTasks(restTemplate, wf, opSet, new String[] {"test"});
		assertTrue(taskSet.iterator().next() instanceof PullTask);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullParameter() {
		TaskFactory.createTasks(null, wf, opSet);
	}
	
	@Test
	public void testEmptyOperatorSetParameter() {
		Set<Runnable> taskSet = TaskFactory.createTasks(restTemplate, wf, new HashSet<Operator>());
		assertTrue(taskSet.size()==0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testToLessParameter() {
		opSet = new HashSet<>();
		opSet.add(fredOp);
		TaskFactory.createTasks(restTemplate, wf, opSet);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyOperatorInputParameter() {
		opSet = new HashSet<>();
		opSet.add(fredOp);
		TaskFactory.createTasks(restTemplate, wf, opSet, new String[] {});
	}
	
}
