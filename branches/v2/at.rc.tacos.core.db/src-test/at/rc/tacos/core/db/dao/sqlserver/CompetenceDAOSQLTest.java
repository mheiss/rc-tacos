package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Competence;

public class CompetenceDAOSQLTest extends DBTestBase
{
    //the dao class
    private CompetenceDAO compDao = DaoFactory.SQL.createCompetenceDAO();
    
    //test data
    Competence comp1,comp2;
    
    @Before
    public void setUp() throws SQLException
    {
    	comp1 = new Competence("comp1");
    	comp2 = new Competence("comp2");
        //insert test data
        int id1 = compDao.addCompetence(comp1);
        int id2 = compDao.addCompetence(comp2);
        comp1.setId(id1);
        comp2.setId(id2);
    }
    
    @After
    public void tearDown() throws SQLException
    {
        deleteTable(CompetenceDAO.TABLE_NAME);
    }
    
    @Test
    public void testFindById() throws SQLException
    {
        Competence comp = compDao.getCompetenceById(comp1.getId());   
        Assert.assertEquals("comp1", comp.getCompetenceName());
    }
    
    @Test
    public void testRemoveCompetence() throws SQLException
    {
        compDao.removeCompetence(comp1.getId());
        //list all
        List<Competence> list = compDao.listCompetences();
        Assert.assertEquals(1, list.size());
    }
    
    @Test
    public void testListCompetence() throws SQLException
    {
        List<Competence> list = compDao.listCompetences();
        Assert.assertEquals(2, list.size());
    }
    
    @Test
    public void testUpdateCompetence() throws SQLException
    {
        //create two indivdual block
        {
            Competence comp = compDao.getCompetenceById(comp1.getId());   
            comp.setCompetenceName("newCompName");
            compDao.updateCompetence(comp);
        }
        {
            Competence comp = compDao.getCompetenceById(comp1.getId());
            Assert.assertEquals("newCompName", comp.getCompetenceName());
        }
    }
}
