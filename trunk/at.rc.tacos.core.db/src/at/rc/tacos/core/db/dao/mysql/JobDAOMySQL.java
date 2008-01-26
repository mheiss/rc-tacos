package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.model.Job;

public class JobDAOMySQL implements JobDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addJob(Job job)
	{
		int jobId = 0;
		try
		{	
			// job_ID, jobname
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.job"));
			query.setInt(1, job.getId());
			query.setString(2, job.getJobName());
			query.executeUpdate();

			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.jobID"));
			query1.setString(1, job.getJobName());
			final ResultSet rs = query1.executeQuery();

			if(rs.first())
				jobId = rs.getInt("job_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return jobId;
	}

	@Override
	public Job getJobById(int id)
	{
		Job job = new Job();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.jobByID"));
			query1.setInt(1, id);
			final ResultSet rs = query1.executeQuery();

			if(rs.first())
			{
			job.setId(id);
			job.setJobName(rs.getString("jobname"));
			}
			else return null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return job;
	}

	@Override
	public List<Job> listJobs()
	{
		List<Job> jobs = new ArrayList<Job>();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.jobs"));
			final ResultSet rs = query1.executeQuery();

			while(rs.next())
			{
				Job job = new Job();
				job.setId(rs.getInt("job_ID"));
				job.setJobName(rs.getString("jobname"));
				jobs.add(job);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return jobs;
	}

	@Override
	public boolean removeJob(int id)
	{
    	try
    	{
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.job"));
    		query.setInt(1, id);
    		query.executeUpdate();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    		return false;
    	}
    	return true;
	}

	@Override
	public boolean updateJob(Job job)
	{
    	try
		{
    	// jobname, job_ID
    	final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.job"));
		query.setString(1, job.getJobName());
		query.setInt(2, job.getId());		
		query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
