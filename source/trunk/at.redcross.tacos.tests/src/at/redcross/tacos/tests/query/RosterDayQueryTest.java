package at.redcross.tacos.tests.query;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.redcross.tacos.dbal.entity.Assignment;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.entity.ServiceType;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;

/**
 * Contains test for roster day view query.
 * <p>
 * The entry is created with the following parameters:
 * </p>
 * 
 * <pre>
 * START:    13.06.2010
 * END:      15.06.2010
 * </pre>
 * 
 * The query is expected to return a single result when the START and the END
 * matches exactly.
 */
public class RosterDayQueryTest extends BaseTest {

    @Before
    public void setup() throws Exception {
        {
            RosterEntry entry = new RosterEntry();
            entry.setNotes("note");
            entry.setAssignment(manager.find(Assignment.class, "Fahrer"));
            entry.setLocation(manager.find(Location.class, "B"));
            entry.setServiceType(manager.find(ServiceType.class, "Hauptamtlich"));
            entry.setSystemUser(manager.find(SystemUser.class, 1L));
            entry.setPlannedStart(DateUtils.parseDate("13.06.2010", new String[] { "dd.MM.yyyy" }));
            entry.setPlannedEnd(DateUtils.parseDate("15.06.2010", new String[] { "dd.MM.yyyy" }));
            manager.persist(entry);
        }
        EntityManagerHelper.commit(manager);
    }

    @Test
    public void testQueryDayEntry() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate("12.06.2010", new String[] { "dd.MM.yyyy" }));
        List<RosterEntry> rosterList = doLoadDay(calendar, manager);
        Assert.assertEquals(0, rosterList.size());
    }

    @Test
    public void testQueryDayEntry2() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate("13.06.2010", new String[] { "dd.MM.yyyy" }));
        List<RosterEntry> rosterList = doLoadDay(calendar, manager);
        Assert.assertEquals(1, rosterList.size());
    }

    @Test
    public void testQueryDayEntry3() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate("14.06.2010", new String[] { "dd.MM.yyyy" }));
        List<RosterEntry> rosterList = doLoadDay(calendar, manager);
        Assert.assertEquals(1, rosterList.size());
    }

    @Test
    public void testQueryDayEntry4() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate("15.06.2010", new String[] { "dd.MM.yyyy" }));
        List<RosterEntry> rosterList = doLoadDay(calendar, manager);
        Assert.assertEquals(1, rosterList.size());
    }

    @Test
    public void testQueryDayEntry5() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate("16.06.2010", new String[] { "dd.MM.yyyy" }));
        List<RosterEntry> rosterList = doLoadDay(calendar, manager);
        Assert.assertEquals(0, rosterList.size());
    }

    @After
    public void tearDown() {
        TypedQuery<RosterEntry> query = manager.createQuery("from RosterEntry", RosterEntry.class);
        for (RosterEntry entry : query.getResultList()) {
            manager.remove(entry);
        }
        EntityManagerHelper.commit(manager);
    }

    private List<RosterEntry> doLoadDay(Calendar calendar, EntityManager manager) {
        StringBuilder builder = new StringBuilder();
        builder.append(" select entry from RosterEntry entry ");
        builder.append(" where :date1 >= entry.plannedStart AND :date1 <= entry.plannedEnd ");
        TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);
        query.setParameter("date1", calendar.getTime());
        return query.getResultList();
    }
}
