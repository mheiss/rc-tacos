package at.rc.tacos.server.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.mina.transport.socket.SocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.ServerContext;
import at.rc.tacos.server.tasks.impl.DialysisPatientTask;
import at.rc.tacos.server.tasks.impl.TransportTask;

/**
 * The task factory manages live cylce the available tasks
 * 
 * @author mheiss
 */
public class TaskFactory {

	// the list of managed tasks
	private static final List<AbstractTask> TASK_LIST = Collections.synchronizedList(new ArrayList<AbstractTask>());

	// populate the default tasks
	static {
		TASK_LIST.add(new TransportTask());
		TASK_LIST.add(new DialysisPatientTask());
	}

	// the list of tasks
	private List<AbstractTask> taskList;
	private Logger log = LoggerFactory.getLogger(TaskFactory.class);

	/**
	 * Default class constructor to setup the tasks
	 */
	public TaskFactory() {
		taskList = Collections.synchronizedList(new ArrayList<AbstractTask>());
		taskList.addAll(TASK_LIST);
	}

	/**
	 * Initializes and setup all tasks.
	 * <p>
	 * All tasks that throwed an exception during the <code>setupTasks</code>
	 * will be removed from the list of scheduled tasks and will <b>NOT</b> be
	 * executed.
	 * </p>
	 */
	public void setupTasks(ServerContext serverContext, SocketAcceptor acceptor) {
		Iterator<AbstractTask> iter = taskList.iterator();
		while (iter.hasNext()) {
			AbstractTask task = iter.next();
			try {
				task.setupTask(serverContext, acceptor);
			}
			catch (Exception e) {
				iter.remove();
				log.error("Failed to setup the task '" + task.getName() + "' removing from the list", e);
			}
		}
	}

	/**
	 * Schedule all tasks to run
	 */
	public void scheduleTasks() {
		synchronized (taskList) {
			for (AbstractTask task : taskList) {
				task.schedule();
				log.info("scheduling task " + task.getName());
			}
		}
	}

	/**
	 * Stops all scheduled tasks
	 */
	public void stopTasks() {
		synchronized (taskList) {
			for (AbstractTask task : taskList) {
				task.cancel();
				log.info("Stopping task " + task.getName());
			}
		}
	}

}
