package at.rc.tacos.core.db.dao.mysql;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Competence;

public class CompetenceDAOMySQLTest extends DBTestBase
{
    //the dao class
    private CompetenceDAO compDao = DaoFactory.MYSQL.createCompetenceDAO();
    
    //test data
    Competence comp_1 = new Competence("test_comp1");
    Competence comp_2 = new Competence("test_comp2");
    
    @Before
    public void setUp() 
    {
        //insert test data
        int id_1 = compDao.addCompetence(comp_1);
        int id_2 = compDao.addCompetence(comp_2);
        comp_1.setId(id_1);
        comp_2.setId(id_2);
    }
    
    @After
    public void tearDown()
    {
        deleteTable(CompetenceDAO.TABLE_NAME);
    }
    
    @Test
    public void testFindById()
    {
        Competence comp = compDao.getCompetenceById(comp_1.getId());   
        Assert.assertEquals("test_comp1", comp.getCompetenceName());
    }
    
    @Test
    public void testRemoveCompetence()
    {
        compDao.removeCompetence(comp_1.getId());
        //list all
        List<Competence> list = compDao.listCompetences();
        Assert.assertEquals(1, list.size());
    }
    
    @Test
    public void testListCompetence()
    {
        List<Competence> list = compDao.listCompetences();
        Assert.assertEquals(2, list.size());
    }
    
    @Test
    public void testUpdateCompetence()
    {
        //create two indivdual block
        {
            Competence comp = compDao.getCompetenceById(comp_1.getId());   
            comp.setCompetenceName("newCompName");
            compDao.updateCompetence(comp);
        }
        {
            Competence comp = compDao.getCompetenceById(comp_1.getId());
            Assert.assertEquals("newCompName", comp.getCompetenceName());
        }
    }
}
