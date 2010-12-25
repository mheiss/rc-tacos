package at.redcross.tacos.tests.query;

import java.util.List;

import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.redcross.tacos.dbal.entity.Address;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.CompetenceHelper;
import at.redcross.tacos.dbal.helper.GroupHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.SystemUserQueryParam;

public class SystemUserQueryTest extends BaseEntityTest {

    @Before
    public void setup() throws Exception {
        {
            Address address = new Address();
            address.setEmail("userOne@example.com");

            SystemUser user = new SystemUser();
            user.setPnr(1);
            user.setFirstName("userOneFirstName");
            user.setLastName("userOneLastName");
            user.setAddress(address);
            user.setLocation(LocationHelper.getByName(manager, "Location_A"));
            user.setGroups(GroupHelper.getByName(manager, "ROLE_USER"));
            user.setCompetences(CompetenceHelper.getByName(manager, "Hauptamtlicher"));

            Login login = new Login();
            login.setLoginName("userOneLogin");
            login.setPassword("userOnePassword");

            login.setSystemUser(user);
            user.setLogin(login);

            manager.persist(user);
            manager.persist(login);
        }
        {
            Address address = new Address();
            address.setEmail("userTwo@example.com");

            SystemUser user = new SystemUser();
            user.setPnr(2);
            user.setFirstName("userTwoFirstName");
            user.setLastName("userTwoLastName");
            user.setAddress(address);
            user.setLocation(LocationHelper.getByName(manager, "Location_A"));
            user.setGroups(GroupHelper.getByName(manager, "ROLE_ADMIN"));

            Login login = new Login();
            login.setLoginName("userTwoLogin");
            login.setPassword("userTwoPassword");

            login.setSystemUser(user);
            user.setLogin(login);

            manager.persist(user);
            manager.persist(login);
        }
        EntityManagerHelper.commit(manager);
    }

    @After
    public void tearDown() {
        TypedQuery<SystemUser> query = manager.createQuery("from SystemUser", SystemUser.class);
        for (SystemUser entry : query.getResultList()) {
            manager.remove(entry);
        }
        EntityManagerHelper.commit(manager);
    }

    @Test
    public void testQueryUserByName() {
        SystemUserQueryParam param = new SystemUserQueryParam();
        param.setUserName("user");
        List<SystemUser> list = SystemUserHelper.list(manager, param);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testQueryUserByName2() {
        SystemUserQueryParam param = new SystemUserQueryParam();
        param.setUserName("userOne");
        List<SystemUser> list = SystemUserHelper.list(manager, param);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testQueryDeleted() {
        // delete one user entry
        SystemUser systemUser = SystemUserHelper.getByPersonalNumber(manager, "1");
        systemUser.setToDelete(true);
        manager.merge(systemUser);
        EntityManagerHelper.commit(manager);

        // now query the deleted one
        SystemUserQueryParam param = new SystemUserQueryParam();
        param.setStateDelete(true);
        param.setStateNormal(false);
        List<SystemUser> list = SystemUserHelper.list(manager, param);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(1, list.iterator().next().getPnr());
    }

    @Test
    public void testQueryLocked() {
        // lock one user entry
        SystemUser systemUser = SystemUserHelper.getByPersonalNumber(manager, "1");
        systemUser.getLogin().setLocked(true);
        manager.merge(systemUser);
        EntityManagerHelper.commit(manager);

        // now query the locked one
        SystemUserQueryParam param = new SystemUserQueryParam();
        param.setStateLocked(true);
        List<SystemUser> list = SystemUserHelper.list(manager, param);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(1, list.iterator().next().getPnr());
    }

    @Test
    public void testQueryLocation() {
        SystemUserQueryParam param = new SystemUserQueryParam();
        param.setLocationName("Location_A");
        List<SystemUser> list = SystemUserHelper.list(manager, param);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testQueryLocation2() {
        SystemUserQueryParam param = new SystemUserQueryParam();
        param.setLocationName("Location_B");
        List<SystemUser> list = SystemUserHelper.list(manager, param);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testQueryGroup() {
        SystemUserQueryParam param = new SystemUserQueryParam();
        param.setGroup(GroupHelper.getByName(manager, "ROLE_USER").iterator().next());
        List<SystemUser> list = SystemUserHelper.list(manager, param);
        Assert.assertEquals(1, list.size());
    }
    
    @Test
    public void testQueryCompetence() {
        SystemUserQueryParam param = new SystemUserQueryParam();
        param.setCompetence(CompetenceHelper.getByName(manager, "Hauptamtlicher").iterator().next());
        List<SystemUser> list = SystemUserHelper.list(manager, param);
        Assert.assertEquals(1, list.size());
    }

}
