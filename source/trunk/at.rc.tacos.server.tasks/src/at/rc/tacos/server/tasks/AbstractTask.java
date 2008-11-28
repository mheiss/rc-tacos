package at.rc.tacos.server.tasks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A <code>Task</code> defines a operation that should be executed in the background on a regular
 * base.
 * <p>
 * All subtasks will belong to the job family <b>at.rc.tacos.server.tasks</b>
 * </p>
 * 
 * @author mheiss
 * 
 */
public abstract class AbstractTask extends Job {

    private Logger log = LoggerFactory.getLogger(AbstractTask.class);

    private int interval;
    private final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm");

    /**
     * Creates a new task instance with the given name
     * 
     * @param name
     *            the identification string of the task
     * @param interval
     *            the interval in minutes to wait until the task will be scheduled again
     */
    public AbstractTask(String name, int interval) {
        super(name);
        this.interval = interval;
        init();
    }

    /**
     * Initializes the job and setup the scheduling
     */
    private void init() {
        setSystem(true);
        setUser(false);
        setPriority(Job.SHORT);
        addJobChangeListener(new JobChangeAdapter() {
            @Override
            public void done(IJobChangeEvent event) {
                // schedule the job again
                schedule(TimeUnit.MINUTES.toMillis(interval));
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, interval);
                log.info("Scheduled job '" + getName() + "' to run @"
                        + FORMATTER.format(cal.getTime()));
            }
        });
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            // check if the task is cancled
            if (monitor.isCanceled())
                return Status.CANCEL_STATUS;

            // execute the task
            log.info("Running task '" + getName() + "'");
            long start = System.currentTimeMillis();
            runTask();
            long end = System.currentTimeMillis();

            // debug some information
            log.debug("Task '" + getName() + "' finished in " + (end - start) + "ms");
        } catch (Exception e) {
            log.error("The task '" + getName() + "' throwed an exception.", e);
        }
        return Status.OK_STATUS;
    }

    @Override
    public boolean belongsTo(Object family) {
        return "at.rc.tacos.server.tasks".equals(family) ? true : false;
    }

    /**
     * Executes the task
     */
    public abstract void runTask();
}
