package at.rc.tacos.core.db.dao.mysql;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Job;

public class JobDAOMySQLTest extends DBTestBase
{
    //the dao class
    private JobDAO jobDao = DaoFactory.MYSQL.createJobDAO();
    
    //test data
    Job job1,job2;
    
    @Before
    public void setUp() throws SQLException
    {
    	job1 = new Job("job2");
    	job2 = new Job("job2");
        //insert test data
        int id1 = jobDao.addJob(job1);
        int id2 = jobDao.addJob(job2);
        job1.setId(id1);
        job2.setId(id2);
    }
    
    @After
    public void tearDown() throws SQLException
    {
        deleteTable(JobDAO.TABLE_NAME);
    }
    
    @Test
    public void testFindById() throws SQLException
    {
        Job job = jobDao.getJobById(job1.getId());   
        Assert.assertEquals("job1", job.getJobName());
    }
    
    @Test
    public void testRemoveJob() throws SQLException
    {
        jobDao.removeJob(job1.getId());
        //list all
        List<Job> list = jobDao.listJobs();
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testListJob() throws SQLException
    {
        List<Job> list = jobDao.listJobs();
        Assert.assertEquals(2, list.size());
    }
    
    @Test
    public void testUpdateJob() throws SQLException
    {
        //create two indivdual block
        {
            Job job = jobDao.getJobById(job1.getId());   
            job.setJobName("newJobName");
            jobDao.updateJob(job);
        }
        {
            Job job = jobDao.getJobById(job1.getId());
            Assert.assertEquals("newJobName", job.getJobName());
        }
    }
}
