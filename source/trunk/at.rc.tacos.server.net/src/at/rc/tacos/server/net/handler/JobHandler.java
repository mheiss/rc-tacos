package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.JobService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.INetHandler;

public class JobHandler implements INetHandler<Job> {

	@Service(clazz = JobService.class)
	private JobService jobService;

	@Override
	public Job add(Job model) throws ServiceException, SQLException {
		// add the job into the dao
		int id = jobService.addJob(model);
		if (id == -1)
			throw new ServiceException("Failed to add the job " + model);
		// set the returned id
		model.setId(id);
		return model;
	}

	@Override
	public List<Job> execute(String command, List<Job> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<Job> get(Map<String, String> params) throws ServiceException, SQLException {
		List<Job> jobList = jobService.listJobs();
		if (jobList == null)
			throw new ServiceException("Failed to list the jobs");
		return jobList;
	}

	@Override
	public Job remove(Job model) throws ServiceException, SQLException {
		if (!jobService.removeJob(model.getId()))
			throw new ServiceException("Failed to remove the job: " + model);
		return model;
	}

	@Override
	public Job update(Job model) throws ServiceException, SQLException {
		if (!jobService.updateJob(model))
			throw new ServiceException("Failed to update the job: " + model);
		return model;
	}

}
