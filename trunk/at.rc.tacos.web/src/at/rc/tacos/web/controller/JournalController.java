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
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.web.container.JournalContainer;
import at.rc.tacos.web.container.JournalContainerListContainer;
import at.rc.tacos.web.container.RosterEntryContainer;
import at.rc.tacos.web.container.RosterEntryContainerListContainer;
import at.rc.tacos.web.container.TransportsToContainerListContainer;
import at.rc.tacos.web.container.VehicleContainer;
import at.rc.tacos.web.session.UserSession;

/**
 * Journal Controller.
 * @author Birgit
 * @version 1.0
 * TODO: Einträge mit falschem Datum herausfiltern
 */
public class JournalController extends Controller {

	private static final String PARAM_CURRENT_DATE_NAME = "currentDate";
	
	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";
	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
	
	private static final String PARAM_VEHICLEDETAIL_NAME = "vehicleName";
	private static final String PARAM_VEHICLEDETAIL_NO_VALUE = "noValue";
	private static final String MODEL_VEHICLEDETAIL_LIST_NAME = "vehicleDetailList";	
	private static final String MODEL_VEHICLE_CONTAINER_NAME = "vehicleContainer";
	
	private static final String PARAM_RESTRICTED_DATE_NAME = "restrictedDate";
	private static final String PARAM_RESTRICTED_DATE_NO_VALUE = "noValue";
	private static final String MODEL_RESTRICTED_DATE_NAME = "restricted";
	private static final String MODEL_RESTRICTED_DATE_LIST_NAME = "restrictedDateList";
	
	
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
		//param location name holen
		final String paramLocationId = request.getParameter(PARAM_LOCATION_NAME);
		System.out.println("paramLocationId: " +paramLocationId);
		int locationId = 0;
		Location location = userSession.getDefaultFormValues().getDefaultLocation();
		//wenn param location leer ist, location null setzen, sonst locationId = paramLocationId
		if (paramLocationId != null && !paramLocationId.equals("")) 
		{
			if (paramLocationId.equalsIgnoreCase(PARAM_LOCATION_NO_VALUE)) {
				location = null;
			} else {
				locationId = Integer.parseInt(paramLocationId);
			}
		}
		
		//locationList enthält alle Locations
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
		
		//set the possible dates
		final String paramRestrictedDateId = request.getParameter(PARAM_RESTRICTED_DATE_NAME);
		System.out.println("......paramRestrictedDateId: " +paramRestrictedDateId);
		
		final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
		final SimpleDateFormat formatDateForServer = new SimpleDateFormat("dd-MM-yyyy");
		
		Date requestDate = new Date();
		
		if(paramRestrictedDateId != null && !paramRestrictedDateId.equals(""))
		{
			requestDate = df.parse(paramRestrictedDateId);		
		}
		
		System.out.println("'''''''' requestDate: " +requestDate);
		params.put(MODEL_RESTRICTED_DATE_NAME, requestDate);
		
		final String dateForServerString = formatDateForServer.format(requestDate);
		System.out.println("############### dateForServerString: " +dateForServerString);
		
		final List<Date> restrictedDateList = new ArrayList<Date>();
		restrictedDateList.add(new Date());
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) -1);
		Date yesterday = cal.getTime();
		restrictedDateList.add(yesterday);
		params.put(MODEL_RESTRICTED_DATE_LIST_NAME, restrictedDateList);
		
		
		

		
		// Vehicle
		final String paramVehicleDetailId = request.getParameter(PARAM_VEHICLEDETAIL_NAME);
		VehicleContainer vehicleContainer = null;
		System.out.println("-- journalController, paramVehicleDetailId: " +paramVehicleDetailId);
		String vehicleDetailName = null;
		if (paramVehicleDetailId != null && !paramVehicleDetailId.equals("")) 
		{
				vehicleDetailName = paramVehicleDetailId;
		}
		final List<AbstractMessage> abstractVehicleDetailList = connection.sendListingRequest(VehicleDetail.ID, null);
		if (!VehicleDetail.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		final List<VehicleContainer> vehicleDetailList = new ArrayList<VehicleContainer>();
		for (final Iterator<AbstractMessage> itVehicleDetailList = abstractVehicleDetailList.iterator(); itVehicleDetailList.hasNext();) {
			final VehicleDetail vehicleDetail = (VehicleDetail)itVehicleDetailList.next();
			final VehicleContainer vC = new VehicleContainer();
			vC.setVehicleName(vehicleDetail.getVehicleName());
			vehicleDetailList.add(vC);
			if (vC.getVehicleName().equals(paramVehicleDetailId)) {
				vehicleContainer = vC;
			}
		}
		params.put(MODEL_VEHICLEDETAIL_LIST_NAME, vehicleDetailList);
		params.put(MODEL_VEHICLE_CONTAINER_NAME, vehicleContainer);
		
	
		
		
		//get transports	        
		QueryFilter filter = new QueryFilter();
		filter.add(IFilterTypes.TRANSPORT_ARCHIVED_FILTER, dateForServerString);
		System.out.println("locationId: " +locationId);
		if(locationId > 0)
		{
			filter.add(IFilterTypes.TRANSPORT_LOCATION_FILTER, String.valueOf(locationId));
		}
		if(vehicleContainer != null)
			filter.add(IFilterTypes.TRANSPORT_JOURNAL_SHORT_VEHICLE_FILTER, vehicleContainer.getVehicleName());
		final List<AbstractMessage> abstractJournalList = connection.sendListingRequest(Transport.ID, filter);//TODO - set FilterType
		if (!connection.getContentType().equalsIgnoreCase(Transport.ID)) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}	
		//TODO
		final ArrayList<JournalContainer> journalList = new ArrayList<JournalContainer>();
		for (final Iterator<AbstractMessage> itJournalList = abstractJournalList.iterator(); itJournalList.hasNext();) {
			final Transport transport = (Transport)itJournalList.next();
			final JournalContainer journalContainer = new JournalContainer();
			final VehicleContainer vCo = new VehicleContainer();
			if(transport.getVehicleDetail() != null)
			{
				vCo.setVehicleName(transport.getVehicleDetail().getVehicleName());
				vCo.setDriver(transport.getVehicleDetail().getDriver());
				vCo.setFirstParamedic(transport.getVehicleDetail().getFirstParamedic());
				vCo.setSecondParamedic(transport.getVehicleDetail().getSecondParamedic());
			}
			journalContainer.setVehicleContainer(vCo);
			if(transport.getVehicleDetail() != null)
				journalContainer.setRealLocation(transport.getVehicleDetail().getCurrentStation());
			else
				journalContainer.setRealLocation(transport.getPlanedLocation());
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
			//S1
			if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY))
				journalContainer.setS1(new Date(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY)));
			else
				journalContainer.setS1(null);
			//S2
			if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT))
				journalContainer.setS2(new Date(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT)));
			else
				journalContainer.setS2(null);
			//S3
			if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT))
				journalContainer.setS3(new Date(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT)));
			else
				journalContainer.setS3(null);
			//S4
			if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION))
				journalContainer.setS4(new Date(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION)));
			else
				journalContainer.setS4(null);
			//S5
			if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE))
				journalContainer.setS5(new Date(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE)));
			else
				journalContainer.setS5(null);
			//S6
			if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION))
				journalContainer.setS6(new Date(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION)));
			else
				journalContainer.setS6(null);
			journalContainer.setKindOfTransport(transport.getKindOfTransport());
			
			journalList.add(journalContainer);
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
