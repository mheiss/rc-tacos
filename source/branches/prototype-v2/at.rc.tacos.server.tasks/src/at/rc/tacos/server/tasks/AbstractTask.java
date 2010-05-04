package at.rc.tacos.server.tasks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.mina.transport.socket.SocketAcceptor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.ServerContext;

/**
 * A <code>Task</code> defines a operation that should be executed in the
 * background on a regular base. The task will be automatically scheduled to run
 * again after the specified time.</p>
 * <p>
 * All subtasks belong to the job family <b>at.rc.tacos.server.tasks</b>
 * </p>
 * 
 * @author mheiss
 */
public abstract class AbstractTask extends Job {

	private Logger log = LoggerFactory.getLogger(AbstractTask.class);
	private final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm");

	private int interval;
	private TimeUnit timeUnit;

	/**
	 * Creates a new task instance with the given name.
	 * 
	 * @param name
	 *            the identification string of the task
	 * @param interval
	 *            the time to wait until the taks should be scheduled again
	 * @param timeUnit
	 *            the time unit of the {@code interval} argument
	 */
	public AbstractTask(String name, int interval, TimeUnit timeUnit) {
		super(name);
		this.interval = interval;
		this.timeUnit = timeUnit;
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

			// schedule the job again
			@Override
			public void done(IJobChangeEvent event) {
				schedule(TimeUnit.MILLISECONDS.convert(interval, timeUnit));
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MINUTE, (int) TimeUnit.MINUTES.convert(interval, timeUnit));
				log.info("Scheduling job '" + getName() + "' to run @ " + FORMATTER.format(cal.getTime()));
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
		}
		catch (Exception e) {
			log.error("The task '" + getName() + "' throwed an exception.", e);
		}
		return Status.OK_STATUS;
	}

	@Override
	public boolean belongsTo(Object family) {
		return "at.rc.tacos.server.tasks".equals(family) ? true : false;
	}

	/**
	 * This method will do the actual action and execute the task. It will be
	 * called every time the taks is scheduled again.
	 * <p>
	 * A task is allowed to throw an exception during the execution, this
	 * execption will be caught and logged.
	 * </p>
	 * <b>NOTE:</b> Even if the task throwed a exception during the execution it
	 * will be scheduled again
	 */
	public abstract void runTask() throws Exception;

	/**
	 * Setup the needed resources that a task need during {@link #runTask()}
	 * action.
	 * <p>
	 * Note that this method will be invoked only once before the job is
	 * executed the first time.
	 * </p>
	 * <p>
	 * <b>NOTE:</b> If a task throws a excpetion during the setup phase the task
	 * will not be scheudled to run!
	 * </p>
	 * 
	 * @param serverContext
	 *            the server context to get the needed resources
	 * @param acceptor
	 *            the socket acceptor to get access to the sessions
	 */
	public abstract void setupTask(ServerContext serverContext, SocketAcceptor acceptor) throws Exception;
}
