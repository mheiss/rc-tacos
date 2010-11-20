package at.redcross.tacos.tests.query;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.redcross.tacos.dbal.entity.DataState;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.AssignmentHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;

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
public class RosterDayQueryTest extends BaseRosterTest {

	@Before
	public void setup() throws Exception {
		// entry spans a single day
		{
			RosterEntry entry = new RosterEntry();
			entry.setNotes("note");
			entry.setAssignment(AssignmentHelper.getByName(manager, "Fahrer"));
			entry.setLocation(LocationHelper.getByName(manager, "Location_A"));
			entry.setServiceType(ServiceTypeHelper.getByName(manager, "Hauptamtlich"));
			entry.setSystemUser(SystemUserHelper.getByLogin(manager, "tacos"));
			entry.setPlannedStartDateTime(DateHelper.parseDateTime("20.07.2010 06:30"));
			entry.setPlannedEndDateTime(DateHelper.parseDateTime("20.07.2010 18:30"));
			manager.persist(entry);
		}
		// entry spans a single day but is DELETED
		{
			RosterEntry entry = new RosterEntry();
			entry.setNotes("note");
			entry.setAssignment(AssignmentHelper.getByName(manager, "Fahrer"));
			entry.setLocation(LocationHelper.getByName(manager, "Location_A"));
			entry.setServiceType(ServiceTypeHelper.getByName(manager, "Hauptamtlich"));
			entry.setSystemUser(SystemUserHelper.getByLogin(manager, "tacos"));
			entry.setPlannedStartDateTime(DateHelper.parseDateTime("20.07.2010 04:30"));
			entry.setPlannedEndDateTime(DateHelper.parseDateTime("20.07.2010 18:30"));
			entry.setState(DataState.DELETE);
			manager.persist(entry);
		}
		// entry spans tow days
		{
			RosterEntry entry = new RosterEntry();
			entry.setNotes("note");
			entry.setAssignment(AssignmentHelper.getByName(manager, "Fahrer"));
			entry.setLocation(LocationHelper.getByName(manager, "Location_A"));
			entry.setServiceType(ServiceTypeHelper.getByName(manager, "Hauptamtlich"));
			entry.setSystemUser(SystemUserHelper.getByLogin(manager, "tacos"));
			entry.setPlannedStartDateTime(DateHelper.parseDateTime("01.07.2010 12:30"));
			entry.setPlannedEndDateTime(DateHelper.parseDateTime("02.07.2010 14:30"));
			manager.persist(entry);
		}
		// entry spans a range of days
		{
			RosterEntry entry = new RosterEntry();
			entry.setNotes("note");
			entry.setAssignment(AssignmentHelper.getByName(manager, "Fahrer"));
			entry.setLocation(LocationHelper.getByName(manager, "Location_A"));
			entry.setServiceType(ServiceTypeHelper.getByName(manager, "Hauptamtlich"));
			entry.setSystemUser(SystemUserHelper.getByLogin(manager, "tacos"));
			entry.setPlannedStartDateTime(DateHelper.parseDateTime("10.07.2010 12:30"));
			entry.setPlannedEndDateTime(DateHelper.parseDateTime("15.07.2010 14:30"));
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
	public void testRangeBefore() throws Exception {
		Date date = DateHelper.parseDate("09.07.2010");
		List<RosterEntry> rosterList = RosterEntryHelper.list(manager, getParam(date));
		Assert.assertEquals(0, rosterList.size());
	}

	@Test
	public void testRangeStart() throws Exception {
		Date date = DateHelper.parseDate("10.07.2010");
		List<RosterEntry> rosterList = RosterEntryHelper.list(manager, getParam(date));
		Assert.assertEquals(1, rosterList.size());
		Assert.assertTrue(checkStartDate("10.07.2010 12:30", rosterList.get(0)));
	}

	@Test
	public void testRangeBetween() throws Exception {
		Date date = DateHelper.parseDate("12.07.2010");
		List<RosterEntry> rosterList = RosterEntryHelper.list(manager, getParam(date));
		Assert.assertEquals(1, rosterList.size());
		Assert.assertTrue(checkStartDate("10.07.2010 12:30", rosterList.get(0)));
	}

	@Test
	public void testRangeEnd() throws Exception {
		Date date = DateHelper.parseDate("15.07.2010");
		List<RosterEntry> rosterList = RosterEntryHelper.list(manager, getParam(date));
		Assert.assertEquals(1, rosterList.size());
		Assert.assertTrue(checkStartDate("10.07.2010 12:30", rosterList.get(0)));
	}

	@Test
	public void testRangeAfter() throws Exception {
		Date date = DateHelper.parseDate("16.07.2010");
		List<RosterEntry> rosterList = RosterEntryHelper.list(manager, getParam(date));
		Assert.assertEquals(0, rosterList.size());
	}

	@Test
	public void testSingleDay() throws Exception {
		Date date = DateHelper.parseDate("20.07.2010");
		List<RosterEntry> rosterList = RosterEntryHelper.list(manager, getParam(date));
		Assert.assertEquals(1, rosterList.size());
		Assert.assertTrue(checkStartDate("20.07.2010 06:30", rosterList.get(0)));
	}

	@Test
	public void testSingleDeleted() throws Exception {
		Date date = DateHelper.parseDate("20.07.2010");
		RosterQueryParam param = getParam(date);
		param.stateDelete = true;
		param.stateNormal = false;
		List<RosterEntry> rosterList = RosterEntryHelper.list(manager, param);
		Assert.assertEquals(1, rosterList.size());
		Assert.assertTrue(checkStartDate("20.07.2010 04:30", rosterList.get(0)));
	}

	@Test
	public void testSingleNormalAndDeleted() throws Exception {
		Date date = DateHelper.parseDate("20.07.2010");
		RosterQueryParam param = getParam(date);
		param.stateDelete = true;
		param.stateNormal = true;
		List<RosterEntry> rosterList = RosterEntryHelper.list(manager, param);
		Assert.assertEquals(2, rosterList.size());
		Assert.assertTrue(checkStartDate("20.07.2010 06:30", rosterList.get(0)));
		Assert.assertTrue(checkStartDate("20.07.2010 04:30", rosterList.get(1)));
	}

	@Test
	public void testSpanStart() throws Exception {
		Date date = DateHelper.parseDate("01.07.2010");
		List<RosterEntry> rosterList = RosterEntryHelper.list(manager, getParam(date));
		Assert.assertEquals(1, rosterList.size());
		Assert.assertTrue(checkStartDate("01.07.2010 12:30", rosterList.get(0)));
	}

	@Test
	public void testSpanEnd() throws Exception {
		Date date = DateHelper.parseDate("02.07.2010");
		List<RosterEntry> rosterList = RosterEntryHelper.list(manager, getParam(date));
		Assert.assertEquals(1, rosterList.size());
		Assert.assertTrue(checkStartDate("01.07.2010 12:30", rosterList.get(0)));
	}

}
