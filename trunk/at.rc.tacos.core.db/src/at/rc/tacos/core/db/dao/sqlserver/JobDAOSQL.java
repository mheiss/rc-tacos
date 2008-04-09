package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.model.Job;

public class JobDAOSQL implements JobDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addJob(Job job) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			int id = 0;
			//get the next id
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextJobID"));
			final ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return -1;
			
			id = rs.getInt(1);
			System.out.println("DAO:::::::::::::: id:" +id +" " +job.getJobName());
			
			// jobname
			final PreparedStatement insertstmt = connection.prepareStatement(queries.getStatment("insert.job"));
			insertstmt.setInt(1,id);
			insertstmt.setString(2,job.getJobName());
			
			if(insertstmt.executeUpdate() == 0)
				return -1;
			
			return id;
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
			if(!rs.next())
				return null;
			
			Job job = new Job();
			job.setId(id);
			job.setJobName(rs.getString("jobname"));
			return job;
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