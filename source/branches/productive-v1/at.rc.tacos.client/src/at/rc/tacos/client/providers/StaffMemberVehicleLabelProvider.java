/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.client.providers;

import java.util.Calendar;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.RosterEntryManager;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.util.MyUtils;

public class StaffMemberVehicleLabelProvider extends LabelProvider {

	// the roster entry manager
	private RosterEntryManager rosterManager = ModelFactory.getInstance().getRosterEntryManager();

	private long dateToday;
	private long dateTomorrow;

	private RosterEntry entry;
	private String plannedStart;
	private String plannedEnd;

	/**
	 * Returns the image to use for this element.
	 * 
	 * @param object
	 *            the object to get the image for
	 * @return the image to use
	 */
	@Override
	public Image getImage(Object object) {
		return null;
	}

	/**
	 * Returns the text to render.
	 */
	@Override
	public String getText(Object object) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		dateToday = cal.getTimeInMillis();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		dateTomorrow = cal.getTimeInMillis();

		StaffMember member = (StaffMember) object;
		entry = rosterManager.getCheckedInRosterEntryByStaffId(member.getStaffMemberId());

		if (entry.getPlannedStartOfWork() < dateToday)
			plannedStart = "00:00";
		else
			plannedStart = MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.timeFormat);

		if (entry.getPlannedEndOfWork() > dateTomorrow)
			plannedEnd = "00:00";
		else
			plannedEnd = MyUtils.timestampToString(entry.getPlannedEndOfWork(), MyUtils.timeFormat);

		return member.getLastName() + " " + member.getFirstName() + "     " + plannedStart + " - " + plannedEnd;
	}
}
