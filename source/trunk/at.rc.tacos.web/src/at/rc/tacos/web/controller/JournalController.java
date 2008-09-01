package at.rc.tacos.web.controller;

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

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.QueryFilter;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.util.MyUtils;
import at.rc.tacos.web.container.JournalContainer;
import at.rc.tacos.web.container.JournalContainerListContainer;
import at.rc.tacos.web.container.VehicleContainer;
import at.rc.tacos.web.net.WebClient;
import at.rc.tacos.web.session.UserSession;

/**
 * Journal Controller.
 * @author Payer Martin
 * @version 1.1
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
	private static final String MODEL_RESTRICTED_DATE_NAME = "restrictedDate";
	private static final String MODEL_RESTRICTED_DATE_LIST_NAME = "restrictedDateList";
		
	private static final String JOURNAL_CONTAINER_LIST_CONTAINER = "journalContainerListContainer";
		
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
		
		//Date
		final String paramRestrictedDateId = request.getParameter(PARAM_RESTRICTED_DATE_NAME);
		
		final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
		final SimpleDateFormat formatDateForServer = new SimpleDateFormat("dd-MM-yyyy");
		
		Date requestDate = new Date();
		
		if(paramRestrictedDateId != null && !paramRestrictedDateId.equals("")) {
			requestDate = df.parse(paramRestrictedDateId);		
		}
		
		final String dateForServerString = formatDateForServer.format(requestDate);
		
		final List<Date> restrictedDateList = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		restrictedDateList.add(cal.getTime());
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		restrictedDateList.add(cal.getTime());
		params.put(MODEL_RESTRICTED_DATE_LIST_NAME, restrictedDateList);
			
		if((!MyUtils.isEqualDate(requestDate.getTime(), restrictedDateList.get(0).getTime()) &! (MyUtils.isEqualDate(requestDate.getTime(), restrictedDateList.get(1).getTime())))) {
			throw new IllegalArgumentException("Error: Date is not in the Datelist. Access denied.");
		}
		
		params.put(MODEL_RESTRICTED_DATE_NAME, requestDate);
				
		// Vehicle
		final String paramVehicleDetailName = request.getParameter(PARAM_VEHICLEDETAIL_NAME);
		VehicleContainer vehicleContainer = null;
		if (paramLocationId != null && !paramLocationId.equals("")) 
		{
			if (paramLocationId.equalsIgnoreCase(PARAM_VEHICLEDETAIL_NO_VALUE)) {
				vehicleContainer = null;
			}
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
			if (vC.getVehicleName().equals(paramVehicleDetailName)) {
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
		journalContainerListContainer.groupJournalBy(locationComparator);
		params.put(JOURNAL_CONTAINER_LIST_CONTAINER, journalContainerListContainer);
		
		return params;
	}
}
