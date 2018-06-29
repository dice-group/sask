package org.dice_research.sask.executer_ms.threading;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is responsible to execute/run the threads.
 * 
 * @author André Sonntag
 */
public class TaskExecuter {

	private final ExecutorService executorService;
	private final Set<Runnable> taskSet;
	
	public TaskExecuter(Runnable task) {
		if(null == task) throw new IllegalArgumentException("task is null");

		this.executorService = Executors.newSingleThreadExecutor();
		this.taskSet = new HashSet<>();
		this.taskSet.add(task);
	}
	
	public TaskExecuter(Set<Runnable> taskSet) {
		
		if(null == taskSet) throw new IllegalArgumentException("taskSet is null");
			
		this.executorService = Executors.newFixedThreadPool(taskSet.size());
		this.taskSet = taskSet;
	}
	
	/**
	 * This method execute the task/threads
	 */
	public void execute() {

		for(Runnable task : taskSet) {
			executorService.submit(task);
		}
		
		executorService.shutdown();
	}	
}
