package at.rc.tacos.server.dbal.sqlserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.services.dbal.JobService;

/**
 * Provides CRUD operation for job.
 * 
 * @author Michael
 */
public class JobSqlService extends BaseSqlService implements JobService {

	@Override
	public int addJob(Job job) throws SQLException {
		int id = 0;
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextJobID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;

		id = rs.getInt(1);

		// jobname
		final PreparedStatement insertstmt = connection.prepareStatement(queries.getStatment("insert.job"));
		insertstmt.setInt(1, id);
		insertstmt.setString(2, job.getJobName());

		if (insertstmt.executeUpdate() == 0)
			return -1;

		return id;
	}

	@Override
	public Job getJobById(int id) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.jobByID"));
		stmt.setInt(1, id);
		final ResultSet rs = stmt.executeQuery();
		// assert we have the job
		if (!rs.next())
			return null;

		Job job = new Job();
		job.setId(id);
		job.setJobName(rs.getString("jobname"));
		return job;
	}

	@Override
	public List<Job> listJobs() throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.jobs"));
		final ResultSet rs = stmt.executeQuery();
		// create the returned list and loop over the result set
		List<Job> jobs = new ArrayList<Job>();
		while (rs.next()) {
			Job job = new Job();
			job.setId(rs.getInt("job_ID"));
			job.setJobName(rs.getString("jobname"));
			jobs.add(job);
		}
		return jobs;
	}

	@Override
	public boolean removeJob(int id) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("remove.job"));
		stmt.setInt(1, id);
		if (stmt.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updateJob(Job job) throws SQLException {
		// jobname, job_ID
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("update.job"));
		stmt.setString(1, job.getJobName());
		stmt.setInt(2, job.getId());
		// assert the update was successfully
		if (stmt.executeUpdate() == 0)
			return false;
		return true;
	}
}
