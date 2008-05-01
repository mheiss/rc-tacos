package at.rc.tacos.web.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;

public class RosterController extends Controller {

	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Location List
		final String paramLocationId = request.getParameter("locationId");
		int locationId = 0;
		Location location = userSession.getLoginInformation().getUserInformation().getPrimaryLocation();
		if (paramLocationId != null && !paramLocationId.equals("")) {
			if (paramLocationId.equalsIgnoreCase("noValue")) {
				location = null;
			} else {
				locationId = Integer.parseInt(paramLocationId);
			}
		}
		final List<AbstractMessage> locationList = connection.sendListingRequest(Location.ID, null);
		if (Location.ID.equalsIgnoreCase(connection.getContentType())) {
			params.put("locationList", locationList);
			for (final Iterator<AbstractMessage> itLoactionList = locationList.iterator(); itLoactionList.hasNext();) {
				final Location l = (Location)itLoactionList.next();
				if (l.getId() == locationId) {
					location = l;
				}
			}
		}
		params.put("location", location);
		
		// Create Calendar for DatePicker
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - 10;
		final int rangeEnd = calendar.get(Calendar.YEAR) + 1;
		params.put("calendarDefaultDateMilliseconds", calendar.getTimeInMillis());
		params.put("calendarRangeStart", rangeStart);
		params.put("calendarRangeEnd", rangeEnd);
		
		// Get Date
		final Calendar rangeStartCalendar = Calendar.getInstance();
		rangeStartCalendar.set(Calendar.YEAR, rangeStartCalendar.get(Calendar.YEAR) - 10);
		
		final Calendar rangeEndCalendar = Calendar.getInstance();
		rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR) + 1);
		final String paramDate = request.getParameter("date");
		
		final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		final SimpleDateFormat formatDateForServer = new SimpleDateFormat("dd-MM-yyyy");

		Date date = new Date();			
		if (paramDate != null) {		
			date = df.parse(paramDate);
			if (date.getTime() < rangeStartCalendar.getTimeInMillis() || date.getTime() > rangeEndCalendar.getTimeInMillis()) {
				throw new IllegalArgumentException();
			}
		}
		params.put("date", date);
		
		final String dateString = formatDateForServer.format(date);
		
		// Get Roster Entries
		QueryFilter rosterFilter = new QueryFilter();
		rosterFilter.add(IFilterTypes.DATE_FILTER, dateString);
		if (location != null) {
			rosterFilter.add(IFilterTypes.ROSTER_LOCATION_FILTER, Integer.toString(location.getId()));
		}
		
		// Form RosterEntryContainer for Table
		final List<AbstractMessage> rosterEntryList = connection.sendListingRequest(RosterEntry.ID, rosterFilter);
		final List<RosterEntryContainer> rosterEntryContainerList = new ArrayList<RosterEntryContainer>();
		if (RosterEntry.ID.equalsIgnoreCase(connection.getContentType())) {
			for (Iterator<AbstractMessage> itRosterEntryList = rosterEntryList.iterator(); itRosterEntryList.hasNext();) {
				final RosterEntry rosterEntry = (RosterEntry)itRosterEntryList.next();
				final RosterEntryContainer rosterEntryContainer = new RosterEntryContainer();
				rosterEntryContainer.setRosterEntry(rosterEntry);
				rosterEntryContainer.setPlannedStartOfWork(new Date(rosterEntry.getPlannedStartOfWork()));
				rosterEntryContainer.setPlannedEndOfWork(new Date(rosterEntry.getPlannedEndOfWork()));
				if (rosterEntry.getRealStartOfWork() == 0) {
					rosterEntryContainer.setRealStartOfWork(null);
				} else {
					rosterEntryContainer.setRealStartOfWork(new Date(rosterEntry.getRealStartOfWork()));
				}
				if (rosterEntry.getRealEndOfWork() == 0) {
					rosterEntryContainer.setRealEndOfWork(null);
				} else {
					rosterEntryContainer.setRealEndOfWork(new Date(rosterEntry.getRealEndOfWork()));
				}
				rosterEntryContainerList.add(rosterEntryContainer);
			}
			params.put("rosterEntryContainerList", rosterEntryContainerList);
		}
		
		return params;
		
	}

}
