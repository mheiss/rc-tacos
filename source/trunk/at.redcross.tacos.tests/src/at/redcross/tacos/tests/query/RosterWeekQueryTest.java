package at.redcross.tacos.tests.query;

import java.util.Calendar;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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

public class RosterWeekQueryTest extends BaseTest {

    @Before
    public void setup() throws Exception {
        {
            RosterEntry entry = new RosterEntry();
            entry.setNotes("note");
            entry.setAssignment(manager.find(Assignment.class, "Fahrer"));
            entry.setLocation(manager.find(Location.class, "B"));
            entry.setServiceType(manager.find(ServiceType.class, "Hauptamtlich"));
            entry.setSystemUser(manager.find(SystemUser.class, 1));
            entry.setPlannedStart(DateUtils.parseDate("13.06.2010", new String[] { "dd.MM.yyyy" }));
            entry.setPlannedEnd(DateUtils.parseDate("15.06.2010", new String[] { "dd.MM.yyyy" }));
            manager.persist(entry);
        }
        EntityManagerHelper.commit(manager);
    }

    @After
    public void tearDown() {
        TypedQuery<RosterEntry> query = manager.createQuery("from rosterEntry", RosterEntry.class);
        for (RosterEntry entry : query.getResultList()) {
            if (entry.getNotes().equals("note")) {
                manager.remove(entry);
            }
        }
        EntityManagerHelper.commit(manager);
    }

    @Test
    public void testQueryWeekEntry() {
        Calendar calendar = Calendar.getInstance(Locale.GERMAN);
        calendar.clear();
        calendar.set(Calendar.YEAR, 2010);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        doLoadWeek(calendar, manager);
    }

    private void doLoadWeek(Calendar calendar, EntityManager manager) {
        StringBuilder builder = new StringBuilder();
        builder.append(" select entry from RosterEntry entry ");
        builder.append(" left join entry.location as location ");
        builder.append(" left join location.district as district ");
        builder.append(" left join entry.assignment as assignment ");
        builder.append(" left join entry.serviceType as serviceType ");
        builder.append(" left join entry.systemUser as systemUser ");
        builder.append(" left join systemUser.login as login ");
        builder
                .append(" where :startDay >= day(entry.plannedStart) OR :endDay <= day(entry.plannedEnd)");
        TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);
        // calculate the start of the week (KW xx)
        query.setParameter("startDay", calendar.get(Calendar.DAY_OF_MONTH));
        // query.setParameter("startMonth", calendar.get(Calendar.MONTH) +
        // 1);
        // query.setParameter("startYear", calendar.get(Calendar.YEAR));
        System.out.println(calendar.getTime());

        // go to the end of the week
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        query.setParameter("endDay", calendar.get(Calendar.DAY_OF_MONTH));
        // query.setParameter("endMonth", calendar.get(Calendar.MONTH) + 1);
        // query.setParameter("endYear", calendar.get(Calendar.YEAR));
        System.out.println(calendar.getTime());
        for (RosterEntry entry : query.getResultList()) {
            System.out.println(entry.getPlannedStart());
            System.out.println(entry.getPlannedEnd());
            System.out.println("-----------------");
        }
    }

}
