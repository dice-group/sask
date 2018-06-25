package org.dice_research.sask.executer_ms.threading;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PullTaskTest {

	@Mock
	RestTemplate restTemplate;

	Map<String, String> outputs1 = null;
	Operator o1 = new Operator();
	Set<Runnable> taskSet = null;
	Workflow wf = null;
	Set<Operator> opSet = null;
	PullTask task = null;
	ExecutorService executorService = null;

	@Before
	public void setUp() throws Exception {

		outputs1 = new HashMap<>();
		outputs1.put("id1_1", "NL");
		o1.setContent("/text.txt");
		o1.setId("id1");
		o1.setType("file");
		o1.setOutputs(outputs1);

		wf = new Workflow();
		wf.getOperators().put("id1", o1);

		task = new PullTask(restTemplate, wf, o1);
		executorService = Executors.newSingleThreadExecutor();

	}

	@Test
	public void test() throws InterruptedException {
		Mockito.when(restTemplate.getForObject(Mockito.startsWith("http://REPO-MS/readFile?location=repo&path="),
				Matchers.any(Class.class), Mockito.startsWith("/text.txt")))
				.thenReturn("Barack Obama is married to Michelle Obama.");
		
		executorService.submit(task);
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

		String content = task.getResponse();
		assertEquals("Barack Obama is married to Michelle Obama.", content);
	}

}
