package at.redcross.tacos.tests.query;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.AssignmentHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;

/**
 * Contains test cases for roster day view query.
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
 * are in the range of the given date.
 */
public class RosterDayQueryTest extends BaseTest {

    @Before
    public void setup() throws Exception {
        {
            RosterEntry entry = new RosterEntry();
            entry.setNotes("note");
            entry.setAssignment(AssignmentHelper.getByName(manager, "Fahrer"));
            entry.setLocation(LocationHelper.getByName(manager, "Bruck"));
            entry.setServiceType(ServiceTypeHelper.getByName(manager, "Hauptamtlich"));
            entry.setSystemUser(SystemUserHelper.getByLogin(manager, "m.heiss"));
            entry.setPlannedStartTime(DateHelper.parseTime("12:30"));
            entry.setPlannedStartDate(DateHelper.parseDate("31.07.2010"));

            entry.setPlannedEndTime(DateHelper.parseTime("14:30"));
            entry.setPlannedEndDate(DateHelper.parseDate("01.08.2010"));
            manager.persist(entry);
        }
        EntityManagerHelper.commit(manager);
    }

    @After
    public void tearDown() {
        TypedQuery<RosterEntry> query = manager.createQuery("from RosterEntry", RosterEntry.class);
        for (RosterEntry entry : query.getResultList()) {
            manager.remove(entry);
        }
        EntityManagerHelper.commit(manager);
    }

    @Test
    public void testQueryDayEntry() throws Exception {
        Date date = DateHelper.parseDate("30.07.2010");
        List<RosterEntry> rosterList = RosterEntryHelper.listByDay(manager, null, date);
        Assert.assertEquals(0, rosterList.size());
    }

    @Test
    public void testQueryDayEntry2() throws Exception {
        Date date = DateHelper.parseDate("31.07.2010");
        List<RosterEntry> rosterList = RosterEntryHelper.listByDay(manager, null, date);
        Assert.assertEquals(1, rosterList.size());
    }

    @Test
    public void testQueryDayEntry3() throws Exception {
        Date date = DateHelper.parseDate("01.08.2010");
        List<RosterEntry> rosterList = RosterEntryHelper.listByDay(manager, null, date);
        Assert.assertEquals(1, rosterList.size());
    }

    @Test
    public void testQueryDayEntry4() throws Exception {
        Date date = DateHelper.parseDate("02.08.2010");
        List<RosterEntry> rosterList = RosterEntryHelper.listByDay(manager, null, date);
        Assert.assertEquals(0, rosterList.size());
    }

    @Test
    public void testQueryDayEntry5() throws Exception {
        Date date = DateHelper.parseDate("16.06.2010");
        List<RosterEntry> rosterList = RosterEntryHelper.listByDay(manager, null, date);
        Assert.assertEquals(0, rosterList.size());
    }
}
