package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>JobHandler</code> manages the locally chached {@link Job}
 * instances.
 * 
 * @author Michael
 */
public class JobHandler implements Handler<Job> {

	private List<Job> jobList = Collections.synchronizedList(new ArrayList<Job>());
	private Logger log = LoggerFactory.getLogger(JobHandler.class);

	@Override
	public void add(MessageIoSession session, Message<Job> message) throws SQLException, ServiceException {
		synchronized (jobList) {
			jobList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<Job> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<Job> message) throws SQLException, ServiceException {
		synchronized (jobList) {
			// add or update the job
			for (Job job : message.getObjects()) {
				int index = jobList.indexOf(job);
				if (index == -1) {
					jobList.add(job);
				}
				else {
					jobList.set(index, job);
				}
			}
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<Job> message) throws SQLException, ServiceException {
		synchronized (jobList) {
			jobList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<Job> message) throws SQLException, ServiceException {
		synchronized (jobList) {
			for (Job updatedJob : message.getObjects()) {
				if (!jobList.contains(updatedJob)) {
					continue;
				}
				int index = jobList.indexOf(updatedJob);
				jobList.set(index, updatedJob);
			}
		}
	}

	/**
	 * Returns the first <code>Job</code> instance that exactly matches the
	 * string returned by {@link Job#getJobName()}.
	 * 
	 * @param jobName
	 *            the name of the <code>Job</code> to search for
	 * @return the matched object or null if nothing found
	 */
	public Job getJobByName(String jobName) {
		synchronized (jobList) {
			for (Job job : jobList) {
				if (job.getJobName().equalsIgnoreCase(jobName))
					return job;
			}
			// nothing found
			return null;
		}
	}

	/**
	 * Returns a new array containing the managed <code>Job</code> instances.
	 * 
	 * @return an array containing the <code>Job</code> instances.
	 */
	@Override
	public Job[] toArray() {
		return jobList.toArray(new Job[jobList.size()]);
	}

}
