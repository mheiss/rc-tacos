package at.rc.tacos.core.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.model.Job;

public class JobDAOMySQL implements JobDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final Queries queries = Queries.getInstance();

	@Override
	public int addJob(Job job) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			// jobname
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("insert.job"));
			stmt.setString(1, job.getJobName());
			stmt.executeUpdate();
			//get the last inserted id
			final ResultSet rs = stmt.getGeneratedKeys();
			//assert we have a auto generated id
			if (rs.next()) 
				return rs.getInt(1);
			return -1;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public Job getJobById(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.jobByID"));
			stmt.setInt(1, id);
			final ResultSet rs = stmt.executeQuery();
			//assert we have the job
			if(rs.first())
			{
				Job job = new Job();
				job.setId(id);
				job.setJobName(rs.getString("jobname"));
				return job;
			}
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<Job> listJobs() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("list.jobs"));
			final ResultSet rs = stmt.executeQuery();
			//create the returned list and loop over the result set
			List<Job> jobs = new ArrayList<Job>();
			while(rs.next())
			{
				Job job = new Job();
				job.setId(rs.getInt("job_ID"));
				job.setJobName(rs.getString("jobname"));
				jobs.add(job);
			}
			return jobs;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeJob(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("remove.job"));
			stmt.setInt(1, id);
			if(stmt.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateJob(Job job) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// jobname, job_ID
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("update.job"));
			stmt.setString(1, job.getJobName());
			stmt.setInt(2, job.getId());	
			//assert the update was successfully
			if(stmt.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}
}