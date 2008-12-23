package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.JobService;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class JobHandler implements Handler<Job> {

	@Service(clazz = JobService.class)
	private JobService jobService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<Job> message) throws ServiceException, SQLException {
		// loop and try to update each object
		List<Job> jobList = message.getObjects();
		for (Job job : jobList) {
			int id = jobService.addJob(job);
			if (id == -1)
				throw new ServiceException("Failed to add the job " + job);
			// set the returned id
			job.setId(id);
		}
		session.writeBrodcast(message, jobList);
	}

	@Override
	public void get(MessageIoSession session, Message<Job> message) throws ServiceException, SQLException {
		// request a listing of all jobs in the database
		List<Job> jobList = jobService.listJobs();
		if (jobList == null)
			throw new ServiceException("Failed to list the jobs");
		// check for locks
		for (Job job : jobList) {
			if (!lockableService.containsLock(job)) {
				continue;
			}
			Lockable lock = lockableService.getLock(job);
			job.setLocked(lock.isLocked());
			job.setLockedBy(lock.getLockedBy());
		}

		// send the jobs back
		session.write(message, jobList);
	}

	@Override
	public void remove(MessageIoSession session, Message<Job> message) throws ServiceException, SQLException {
		List<Job> jobList = message.getObjects();
		// loop and try to remove each object
		for (Job job : jobList) {
			if (!jobService.removeJob(job.getId()))
				throw new ServiceException("Failed to remove the job: " + job);
			// remove the lock
			lockableService.removeLock(job);
		}
		session.writeBrodcast(message, jobList);
	}

	@Override
	public void update(MessageIoSession session, Message<Job> message) throws ServiceException, SQLException {
		List<Job> jobList = message.getObjects();
		// loop and try to update each object
		for (Job job : jobList) {
			if (!jobService.updateJob(job))
				throw new ServiceException("Failed to update the job: " + job);
			// update the lock
			lockableService.updateLock(job);
		}
		session.writeBrodcast(message, jobList);
	}

	@Override
	public void execute(MessageIoSession session, Message<Job> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			return;
		}
		throw new NoSuchCommandException(handler, command);
	}
}
