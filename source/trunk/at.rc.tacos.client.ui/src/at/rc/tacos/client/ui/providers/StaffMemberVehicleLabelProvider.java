package at.rc.tacos.client.ui.providers;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jface.viewers.LabelProvider;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.RosterHandler;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.util.MyUtils;

public class StaffMemberVehicleLabelProvider extends LabelProvider {

	// the roster entry handler
	private RosterHandler rosterHandler = (RosterHandler) NetWrapper.getHandler(RosterEntry.class);

	/**
	 * Returns the text to render.
	 */
	@Override
	public String getText(Object object) {
		StaffMember member = (StaffMember) object;
		RosterEntry entry = rosterHandler.getCheckedInRosterEntryByStaffId(member.getStaffMemberId());
		String plannedStart, plannedEnd;

		Date thisDay = DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.DAY_OF_MONTH);
		Date nextDay = DateUtils.addDays(thisDay, 1);

		if (entry.getPlannedStartOfWork() < thisDay.getTime())
			plannedStart = "00:00";
		else
			plannedStart = MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.timeFormat);

		if (entry.getPlannedEndOfWork() > nextDay.getTime())
			plannedEnd = "00:00";
		else
			plannedEnd = MyUtils.timestampToString(entry.getPlannedEndOfWork(), MyUtils.timeFormat);

		return member.getLastName() + " " + member.getFirstName() + "     " + plannedStart + " - " + plannedEnd;
	}
}
