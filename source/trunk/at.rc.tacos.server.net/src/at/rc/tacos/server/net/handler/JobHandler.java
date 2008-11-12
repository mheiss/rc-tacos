package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.JobService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class JobHandler implements Handler<Job> {

	@Service(clazz = JobService.class)
	private JobService jobService;

	@Override
	public void add(ServerIoSession session, Message<Job> message) throws ServiceException, SQLException {
		// add the job into the dao
		int id = jobService.addJob(model);
		if (id == -1)
			throw new ServiceException("Failed to add the job " + model);
		// set the returned id
		model.setId(id);
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<Job> message) throws ServiceException, SQLException {
		List<Job> jobList = jobService.listJobs();
		if (jobList == null)
			throw new ServiceException("Failed to list the jobs");
		return jobList;
	}

	@Override
	public void remove(ServerIoSession session, Message<Job> message) throws ServiceException, SQLException {
		if (!jobService.removeJob(model.getId()))
			throw new ServiceException("Failed to remove the job: " + model);
		return model;
	}

	@Override
	public void update(ServerIoSession session, Message<Job> message) throws ServiceException, SQLException {
		if (!jobService.updateJob(model))
			throw new ServiceException("Failed to update the job: " + model);
		return model;
	}

	@Override
	public void execute(ServerIoSession session, Message<Job> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
