package at.rc.tacos.server.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The task factory manages live cylce the available tasks
 * 
 * @author mheiss
 * 
 */
public class TaskFactory {

    // the list of managed tasks
    private static final List<AbstractTask> TASK_LIST = Collections
            .synchronizedList(new ArrayList<AbstractTask>());

    // populate the default tasks
    static {

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
     * Schedule all tasks and start them
     */
    public void scheduleTasks() {
        synchronized (taskList) {
            for (AbstractTask task : taskList) {
                task.schedule();
                log.info("Scheudling task " + task.getName());
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
