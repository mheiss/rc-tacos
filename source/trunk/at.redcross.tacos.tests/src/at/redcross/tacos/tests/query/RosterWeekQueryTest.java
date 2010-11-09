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
 * Contains test cases for roster week view query.
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
public class RosterWeekQueryTest extends BaseRosterTest {

	@Before
	public void setup() throws Exception {
		// entry spanning one day in KW30
		{
			RosterEntry entry = new RosterEntry();
			entry.setNotes("note");
			entry.setAssignment(AssignmentHelper.getByName(manager, "Fahrer"));
			entry.setLocation(LocationHelper.getByName(manager, "Location_A"));
			entry.setServiceType(ServiceTypeHelper.getByName(manager, "Hauptamtlich"));
			entry.setSystemUser(SystemUserHelper.getByLogin(manager, "tacos"));
			entry.setPlannedStartDateTime(DateHelper.parseDateTime("27.07.2010 12:30"));
			entry.setPlannedEndDateTime(DateHelper.parseDateTime("27.07.2010 14:30"));
			manager.persist(entry);
		}
		// entry spanning two days in KW30
		{
			RosterEntry entry = new RosterEntry();
			entry.setNotes("note");
			entry.setAssignment(AssignmentHelper.getByName(manager, "Fahrer"));
			entry.setLocation(LocationHelper.getByName(manager, "Location_A"));
			entry.setServiceType(ServiceTypeHelper.getByName(manager, "Hauptamtlich"));
			entry.setSystemUser(SystemUserHelper.getByLogin(manager, "tacos"));
			entry.setPlannedStartDateTime(DateHelper.parseDateTime("27.07.2010 18:30"));
			entry.setPlannedEndDateTime(DateHelper.parseDateTime("28.07.2010 06:00"));
			manager.persist(entry);
		}
		// entry spanning KW30 AND 31
		{
			RosterEntry entry = new RosterEntry();
			entry.setNotes("note");
			entry.setAssignment(AssignmentHelper.getByName(manager, "Fahrer"));
			entry.setLocation(LocationHelper.getByName(manager, "Location_A"));
			entry.setServiceType(ServiceTypeHelper.getByName(manager, "Hauptamtlich"));
			entry.setSystemUser(SystemUserHelper.getByLogin(manager, "tacos"));
			entry.setPlannedStartDateTime(DateHelper.parseDateTime("01.08.2010 18:30"));
			entry.setPlannedEndDateTime(DateHelper.parseDateTime("02.08.2010 06:00"));
			manager.persist(entry);
		}
		// entry spanning one day in KW31
		{
			RosterEntry entry = new RosterEntry();
			entry.setNotes("note");
			entry.setAssignment(AssignmentHelper.getByName(manager, "Fahrer"));
			entry.setLocation(LocationHelper.getByName(manager, "Location_A"));
			entry.setServiceType(ServiceTypeHelper.getByName(manager, "Hauptamtlich"));
			entry.setSystemUser(SystemUserHelper.getByLogin(manager, "tacos"));
			entry.setPlannedStartDateTime(DateHelper.parseDateTime("06.08.2010 12:30"));
			entry.setPlannedEndDateTime(DateHelper.parseDateTime("06.08.2010 18:30"));
			manager.persist(entry);
		}
		EntityManagerHelper.commit(manager);
	}

	@After
	public void tearDown() {
		TypedQuery<RosterEntry> query = manager.createQuery("from RosterEntry", RosterEntry.class);
		for (RosterEntry entry : query.getResultList()) {
			if (entry.getNotes().equals("note")) {
				manager.remove(entry);
			}
		}
		EntityManagerHelper.commit(manager);
	}

	@Test
	public void testQueryKW29NoMatch() throws Exception {
		Date date = DateHelper.parseCustom("2010 29", "yyyy w");
		List<RosterEntry> result = RosterEntryHelper.listByWeek(manager, getParam(date));
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testQueryKW30MultiMatch() throws Exception {
		Date date = DateHelper.parseCustom("2010 30", "yyyy w");
		List<RosterEntry> result = RosterEntryHelper.listByWeek(manager, getParam(date));
		Assert.assertEquals(3, result.size());
		Assert.assertTrue(checkStartDate("27.07.2010 12:30", result.get(0)));
		Assert.assertTrue(checkStartDate("27.07.2010 18:30", result.get(1)));
		Assert.assertTrue(checkStartDate("01.08.2010 18:30", result.get(2)));
	}

	@Test
	public void testQueryKW31MultiMatch() throws Exception {
		Date date = DateHelper.parseCustom("2010 31", "yyyy w");
		List<RosterEntry> result = RosterEntryHelper.listByWeek(manager, getParam(date));
		Assert.assertEquals(2, result.size());
		Assert.assertTrue(checkStartDate("01.08.2010 18:30", result.get(0)));
		Assert.assertTrue(checkStartDate("06.08.2010 12:30", result.get(1)));
	}

	@Test
	public void testQueryKW32NoMatch() throws Exception {
		Date date = DateHelper.parseCustom("2010 32", "yyyy w");
		List<RosterEntry> result = RosterEntryHelper.listByWeek(manager, getParam(date));
		Assert.assertEquals(0, result.size());
	}
}
