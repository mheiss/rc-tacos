package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>RosterHandler</code> manages the locally chached
 * {@link RosterEntry} instances.
 * 
 * @author Michael
 */
public class RosterHandler implements Handler<RosterEntry> {

	private List<RosterEntry> rosterList = Collections.synchronizedList(new LinkedList<RosterEntry>());
	private Logger log = LoggerFactory.getLogger(RosterHandler.class);

	@Override
	public void add(MessageIoSession session, Message<RosterEntry> message) throws SQLException, ServiceException {
		synchronized (rosterList) {
			rosterList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<RosterEntry> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<RosterEntry> message) throws SQLException, ServiceException {
		synchronized (rosterList) {
			rosterList.clear();
			rosterList.addAll(message.getObjects());
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<RosterEntry> message) throws SQLException, ServiceException {
		synchronized (rosterList) {
			rosterList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<RosterEntry> message) throws SQLException, ServiceException {
		synchronized (rosterList) {
			for (RosterEntry updatedEntry : message.getObjects()) {
				if (!rosterList.contains(updatedEntry)) {
					continue;
				}
				int index = rosterList.indexOf(updatedEntry);
				rosterList.set(index, updatedEntry);
			}
		}
	}

	/**
	 * Returns a list of all checked in roster entries by location.
	 * <p>
	 * If the {@link RosterEntry#getPlannedEndOfWork()} is older than two days
	 * the entry is ignored, even if the person is checked in. longer be
	 * assigned to a vehicle
	 * 
	 * @param location
	 *            the location to filter the <code>RosterEntry</code>
	 * @return the filtered list
	 */
	public List<RosterEntry> getCheckedInRosterEntriesByLocation(Location location) {
		synchronized (rosterList) {
			List<RosterEntry> filteredList = new ArrayList<RosterEntry>();
			for (RosterEntry entry : rosterList) {
				// check the location
				if (!entry.getStation().equals(location))
					continue;
				// check if the staff has signed in
				if (entry.getRealStartOfWork() == 0 || entry.getRealEndOfWork() != 0)
					continue;

				// do not add entries if they are older than 2 days even if the
				// member is signed in
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 2);
				long before2Days = cal.getTimeInMillis();
				if (entry.getPlannedEndOfWork() > before2Days)
					filteredList.add(entry);
			}
			return filteredList;
		}
	}

	/**
	 * Returns the first <code>RosterEntry</code> of the {@link StaffMember}
	 * identified by the {@link StaffMember#getStaffMemberId()} that is marked
	 * as checkd in.
	 * <p>
	 * An <code>RosterEntry</code> is checked in when the
	 * {@link RosterEntry#getRealStartOfWork()} is set and the
	 * {@link RosterEntry#getRealEndOfWork()} is not 0.
	 * </p>
	 * 
	 * @param staffId
	 *            the staffMember to filter
	 * @return the first matched <code>RosterEntry</code> instance or null if
	 *         nothing found
	 */
	public RosterEntry getCheckedInRosterEntryByStaffId(int staffId) {
		for (RosterEntry entry : rosterList) {
			// filter by the roster id
			if (entry.getStaffMember().getStaffMemberId() != staffId) {
				continue;
			}
			// checked in?
			if (entry.getRealStartOfWork() != 0 && entry.getRealEndOfWork() == 0)
				return entry;
		}
		// nothing found
		return null;
	}

	/**
	 * Returns a new array containing the managed <code>RosterEntry</code>
	 * instances.
	 * 
	 * @return an array containing the <code>RosterEntry</code> instances.
	 */
	public RosterEntry[] toArray() {
		return rosterList.toArray(new RosterEntry[rosterList.size()]);
	}

}
