package at.rc.tacos.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.support.PropertyComparator;
import org.springframework.util.comparator.CompoundComparator;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.Transport;
import at.rc.tacos.web.form.JournalContainer;
import at.rc.tacos.web.form.JournalContainerListContainer;
import at.rc.tacos.web.form.RosterEntryContainer;
import at.rc.tacos.web.form.RosterEntryContainerListContainer;
import at.rc.tacos.web.form.TransportsToContainerListContainer;
import at.rc.tacos.web.session.UserSession;

/**
 * Roster Controller.
 * @author Birgit
 * @version 1.0
 * TODO: Eintr�ge mit falschem Datum herausfiltern
 */
public class JournalController extends Controller {

	private static final String PARAM_CURRENT_DATE_NAME = "currentDate";
	
	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";
	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
	
	private static final String MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME = "calendarDefaultDateMilliseconds";
	private static final String MODEL_CALENDAR_RANGE_START_NAME = "calendarRangeStart";
	private static final String MODEL_CALENDAR_RANGE_END_NAME = "calendarRangeEnd";
	private static final int MODEL_CALENDAR_RANGE_START_OFFSET = 10;
	private static final int MODEL_CALENDAR_RANGE_END_OFFSET = 1;
	
	private static final String PARAM_DATE_NAME = "date";
	private static final String MODEL_DATE_NAME = "date";
	private static final String MODEL_DATE_DAY_OF_YEAR_NAME = "dateDayOfYear";
	private static final String MODEL_DATE_MONTH_NAME = "dateMonth";
	private static final String MODEL_DATE_YEAR_NAME = "dateYear";
	
	private static final String JOURNAL_CONTAINER_LIST_CONTAINER = "journalContainerListContainer";
	
	private static final String PARAM_MESSAGE_CODE_NAME = "messageCode";
	private static final String MODEL_MESSAGE_CODE_NAME = "messageCode";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		// Put current date to parameter to parameter list
		params.put(PARAM_CURRENT_DATE_NAME, new Date());
		
		// Location
		final String paramLocationId = request.getParameter(PARAM_LOCATION_NAME);
		int locationId = 0;
		Location location = userSession.getDefaultFormValues().getDefaultLocation();
		if (paramLocationId != null && !paramLocationId.equals("")) 
		{
			if (paramLocationId.equalsIgnoreCase(PARAM_LOCATION_NO_VALUE)) {
				location = null;
			} else {
				locationId = Integer.parseInt(paramLocationId);
			}
		}
		final List<AbstractMessage> locationList = connection.sendListingRequest(Location.ID, null);
		if (!Location.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		params.put(MODEL_LOCATION_LIST_NAME, locationList);
		for (final Iterator<AbstractMessage> itLoactionList = locationList.iterator(); itLoactionList.hasNext();) {
			final Location l = (Location)itLoactionList.next();
			if (l.getId() == locationId) {
				location = l;
			}
		}
		params.put(MODEL_LOCATION_NAME, location);
		
		// Get Date and create calendar for datepicker
		Date date = userSession.getDefaultFormValues().getDefaultDate();
		if (date == null) {
			date = new Date();
		}
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - MODEL_CALENDAR_RANGE_START_OFFSET;
		final int rangeEnd = calendar.get(Calendar.YEAR) + MODEL_CALENDAR_RANGE_END_OFFSET;
		

		params.put(MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME, date.getTime());
		
		params.put(MODEL_CALENDAR_RANGE_START_NAME, rangeStart);
		params.put(MODEL_CALENDAR_RANGE_END_NAME, rangeEnd);
		
		final Calendar rangeStartCalendar = Calendar.getInstance();
		rangeStartCalendar.set(Calendar.YEAR, rangeStartCalendar.get(Calendar.YEAR) - MODEL_CALENDAR_RANGE_START_OFFSET);
		
		final Calendar rangeEndCalendar = Calendar.getInstance();
		rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR) + MODEL_CALENDAR_RANGE_END_OFFSET);
		final String paramDate = request.getParameter(PARAM_DATE_NAME);
		
		final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		final SimpleDateFormat formatDateForServer = new SimpleDateFormat("dd-MM-yyyy");
		
		Date dateTemp = null;
		if (paramDate != null) {
			try {
				dateTemp = df.parse(paramDate);
			}
			catch (ParseException e) {
				
			}
			if (dateTemp!= null && dateTemp.getTime() < rangeStartCalendar.getTimeInMillis() || dateTemp.getTime() > rangeEndCalendar.getTimeInMillis()) {
				//throw new IllegalArgumentException();
			} else {
				date = dateTemp;
			}
		}
		params.put(MODEL_DATE_NAME, date);
		

		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		params.put(MODEL_DATE_DAY_OF_YEAR_NAME, c.get(Calendar.DAY_OF_YEAR));
		params.put(MODEL_DATE_MONTH_NAME, c.get(Calendar.MONTH));
		params.put(MODEL_DATE_YEAR_NAME, c.get(Calendar.YEAR));
				
		final String dateForServerString = formatDateForServer.format(date);
		
		
		//get transports
		Calendar cal = Calendar.getInstance();
		long date1 = cal.getTimeInMillis();
		
		 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	     String strDate = sdf.format(date1);
	        
	     System.out.println(".................strDate: " +strDate);
		QueryFilter filter = new QueryFilter();
		filter.add(IFilterTypes.DATE_FILTER, strDate);
		final List<AbstractMessage> abstractJournalList = connection.sendListingRequest(Transport.ID, filter);//TODO - set FilterType
		if (!connection.getContentType().equalsIgnoreCase(Transport.ID)) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}	
		System.out.println("*****JournalController, size of abstractJournalList: " +abstractJournalList.size());
		final ArrayList<JournalContainer> journalList = new ArrayList<JournalContainer>();
		for (final Iterator<AbstractMessage> itJournalList = abstractJournalList.iterator(); itJournalList.hasNext();) {
			final Transport transport = (Transport)itJournalList.next();
			final JournalContainer journalContainer = new JournalContainer();
			journalContainer.setVehicleDetail(transport.getVehicleDetail());
//			if(transport.getVehicleDetail() != null)
				journalContainer.setRealLocation(transport.getPlanedLocation());//!!! real station- got from the vehicle
			journalContainer.setTransportNumber(transport.getTransportNumber());
			journalContainer.setDisposedByUser(transport.getDisposedByUsername());
			journalContainer.setFeedback(transport.getFeedback());
			journalContainer.setCaller(transport.getCallerDetail());
			journalContainer.setKindOfIllness(transport.getKindOfIllness());
			journalContainer.setFromStreet(transport.getFromStreet());
			journalContainer.setFromCity(transport.getFromCity());
			journalContainer.setPatient(transport.getPatient());
			journalContainer.setToStreet(transport.getToStreet());
			journalContainer.setToCity(transport.getToCity());
			journalContainer.setNotes(transport.getNotes());
			//TODO S1 - S6
			journalContainer.setKindOfTransport(transport.getKindOfTransport());
			
			journalList.add(journalContainer);
			System.out.println("*****JournalController, size of journalList: " +journalList.size());
		}
		
		// Group
		final JournalContainerListContainer journalContainerListContainer = new JournalContainerListContainer(journalList);	
		final Comparator<Location> locationComparator = new PropertyComparator("locationName", true, true);
//		final Comparator<TransportsToContainer> transportToContainerComparator = new PropertyComparator("streetFrom", true, true);
		journalContainerListContainer.groupJournalBy(locationComparator);
//		transportsToContainerListContainer.sortTransportsTo(transportToContainerComparator);
		params.put(JOURNAL_CONTAINER_LIST_CONTAINER, journalContainerListContainer);
		
		return params;
	}
}
//package at.rc.tacos.web.controller;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.support.PropertyComparator;
//
//import at.rc.tacos.common.AbstractMessage;
//import at.rc.tacos.common.IFilterTypes;
//import at.rc.tacos.core.net.internal.WebClient;
//import at.rc.tacos.model.Location;
//import at.rc.tacos.model.QueryFilter;
//import at.rc.tacos.model.Transport;
//import at.rc.tacos.web.form.JournalContainer;
//import at.rc.tacos.web.form.JournalContainerListContainer;
//import at.rc.tacos.web.form.TransportsToContainer;
//import at.rc.tacos.web.form.TransportsToContainerListContainer;
//import at.rc.tacos.web.session.UserSession;
//
///**
// * Transports To Controller
// * @author Birgit
// * @version 1.0
// */
//public class JournalController extends Controller {
//
//	private static final String JOURNAL_CONTAINER_LIST_CONTAINER = "journalContainerListContainer";
//	private static final String PARAM_CURRENT_DATE_NAME = "currentDate";
//	
//	private static final String MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME = "calendarDefaultDateMilliseconds";
//	private static final String MODEL_CALENDAR_RANGE_START_NAME = "calendarRangeStart";
//	private static final String MODEL_CALENDAR_RANGE_END_NAME = "calendarRangeEnd";
//	private static final int MODEL_CALENDAR_RANGE_START_OFFSET = 10;
//	private static final int MODEL_CALENDAR_RANGE_END_OFFSET = 1;
//	
//	private static final String PARAM_LOCATION_NAME = "locationId";
//	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
//	private static final String MODEL_LOCATION_NAME = "location";
//	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
//	
//	private static final String PARAM_DATE_NAME = "date";
//	private static final String MODEL_DATE_NAME = "date";
//	private static final String MODEL_DATE_DAY_OF_YEAR_NAME = "dateDayOfYear";
//	private static final String MODEL_DATE_MONTH_NAME = "dateMonth";
//	private static final String MODEL_DATE_YEAR_NAME = "dateYear";
//	
//	@Override
//	public Map<String, Object> handleRequest(HttpServletRequest request,
//			HttpServletResponse response, ServletContext context)
//			throws Exception {
//		// TODO Auto-generated method stub
//		final Map<String, Object> params = new HashMap<String, Object>();
//		
//		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
//		final WebClient connection = userSession.getConnection();
//		
//		// Put current date to parameter to parameter list
//		params.put(PARAM_CURRENT_DATE_NAME, new Date());
//		
//		// Location
//		final String paramLocationId = request.getParameter(PARAM_LOCATION_NAME);
//		int locationId = 0;
//		Location location = userSession.getDefaultFormValues().getDefaultLocation();
//		if (paramLocationId != null && !paramLocationId.equals("")) 
//		{
//			if (paramLocationId.equalsIgnoreCase(PARAM_LOCATION_NO_VALUE)) {
//				location = null;
//			} else {
//				locationId = Integer.parseInt(paramLocationId);
//			}
//		}
//		final List<AbstractMessage> locationList = connection.sendListingRequest(Location.ID, null);
//		if (!Location.ID.equalsIgnoreCase(connection.getContentType())) {
//			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
//		}
//		params.put(MODEL_LOCATION_LIST_NAME, locationList);
//		for (final Iterator<AbstractMessage> itLoactionList = locationList.iterator(); itLoactionList.hasNext();) {
//			final Location l = (Location)itLoactionList.next();
//			if (l.getId() == locationId) {
//				location = l;
//			}
//		}
//		params.put(MODEL_LOCATION_NAME, location);
//		
//		// Get Date and create calendar for datepicker
//		Date date = userSession.getDefaultFormValues().getDefaultDate();
//		if (date == null) {
//			date = new Date();
//		}
//		final Calendar calendar = Calendar.getInstance();
//		final int rangeStart = calendar.get(Calendar.YEAR) - MODEL_CALENDAR_RANGE_START_OFFSET;
//		final int rangeEnd = calendar.get(Calendar.YEAR) + MODEL_CALENDAR_RANGE_END_OFFSET;
//		
//
//		params.put(MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME, date.getTime());
//		
//		params.put(MODEL_CALENDAR_RANGE_START_NAME, rangeStart);
//		params.put(MODEL_CALENDAR_RANGE_END_NAME, rangeEnd);
//		
//		final Calendar rangeStartCalendar = Calendar.getInstance();
//		rangeStartCalendar.set(Calendar.YEAR, rangeStartCalendar.get(Calendar.YEAR) - MODEL_CALENDAR_RANGE_START_OFFSET);
//		
//		final Calendar rangeEndCalendar = Calendar.getInstance();
//		rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR) + MODEL_CALENDAR_RANGE_END_OFFSET);
//		final String paramDate = request.getParameter(PARAM_DATE_NAME);
//		
//		final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//		final SimpleDateFormat formatDateForServer = new SimpleDateFormat("dd-MM-yyyy");
//		
//		Date dateTemp = null;
//		if (paramDate != null) {
//			try {
//				dateTemp = df.parse(paramDate);
//			}
//			catch (ParseException e) {
//				
//			}
//			if (dateTemp!= null && dateTemp.getTime() < rangeStartCalendar.getTimeInMillis() || dateTemp.getTime() > rangeEndCalendar.getTimeInMillis()) {
//				//throw new IllegalArgumentException();
//			} else {
//				date = dateTemp;
//			}
//		}
//		params.put(MODEL_DATE_NAME, date);
//		
//		
//		final Calendar c = Calendar.getInstance();
//		c.setTime(date);
//		params.put(MODEL_DATE_DAY_OF_YEAR_NAME, c.get(Calendar.DAY_OF_YEAR));
//		params.put(MODEL_DATE_MONTH_NAME, c.get(Calendar.MONTH));
//		params.put(MODEL_DATE_YEAR_NAME, c.get(Calendar.YEAR));
//				
//		
//		//get transports
//		Calendar cal = Calendar.getInstance();
//		long date1 = cal.getTimeInMillis();
//		
//		 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//	     String strDate = sdf.format(date1);
//	        
//	     System.out.println(".................strDate: " +strDate);
//		QueryFilter filter = new QueryFilter();
//		filter.add(IFilterTypes.DATE_FILTER, strDate);
//		final List<AbstractMessage> abstractJournalList = connection.sendListingRequest(Transport.ID, filter);//TODO - set FilterType
//		if (!connection.getContentType().equalsIgnoreCase(Transport.ID)) {
//			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
//		}	
//		final ArrayList<JournalContainer> journalList = new ArrayList<JournalContainer>();
//		for (final Iterator<AbstractMessage> itJournalList = abstractJournalList.iterator(); itJournalList.hasNext();) {
//			final Transport transport = (Transport)itJournalList.next();
//			final JournalContainer journalContainer = new JournalContainer();
//			journalContainer.setVehicleDetail(transport.getVehicleDetail());
////			if(transport.getVehicleDetail() != null)
//				journalContainer.setRealLocation(transport.getPlanedLocation());//!!! real station- got from the vehicle
//			journalContainer.setTransportNumber(transport.getTransportNumber());
//			journalContainer.setDisposedByUser(transport.getDisposedByUsername());
//			journalContainer.setFeedback(transport.getFeedback());
//			journalContainer.setCaller(transport.getCallerDetail());
//			journalContainer.setKindOfIllness(transport.getKindOfIllness());
//			journalContainer.setFromStreet(transport.getFromStreet());
//			journalContainer.setFromCity(transport.getFromCity());
//			journalContainer.setPatient(transport.getPatient());
//			journalContainer.setToStreet(transport.getToStreet());
//			journalContainer.setToCity(transport.getToCity());
//			journalContainer.setNotes(transport.getNotes());
//			//TODO S1 - S6
//			journalContainer.setKindOfTransport(transport.getKindOfTransport());
//			
//			journalList.add(journalContainer);
//		}
//		
//		
////		// Group
////		final JournalContainerListContainer journalContainerListContainer = new JournalContainerListContainer(journalList);	
////		final Comparator<Location> locationComparator = new PropertyComparator("locationName", true, true);
//////		final Comparator<TransportsToContainer> transportToContainerComparator = new PropertyComparator("streetFrom", true, true);
////		journalContainerListContainer.groupJournalBy(locationComparator);
//////		transportsToContainerListContainer.sortTransportsTo(transportToContainerComparator);
////		params.put(JOURNAL_CONTAINER_LIST_CONTAINER, journalContainerListContainer);
//		
//		return params;
//	}
//
//}
