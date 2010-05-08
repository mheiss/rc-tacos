package at.rc.tacos.web.controller;

import java.util.ArrayList;
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

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.socket.WebClient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.Transport;
import at.rc.tacos.web.container.TransportsToContainer;
import at.rc.tacos.web.container.TransportsToContainerListContainer;
import at.rc.tacos.web.session.UserSession;

/**
 * Transports To Controller
 * @author Birgit
 * @version 1.0
 */
public class TransportsToController extends Controller {

	private static final String TRANSPORTSTO_CONTAINER_LIST_CONTAINER = "transportsToContainerListContainer";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		// TODO Auto-generated method stub
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		// Check authorization
		final String authorization = userSession.getLoginInformation().getAuthorization();
		if (!authorization.equals(Login.AUTH_ADMIN)) {
			throw new IllegalArgumentException("Error: User has no permission for functionality.");
		}
	     
		QueryFilter filter = new QueryFilter();
		filter.add(IFilterTypes.TRANSPORT_TODO_FILTER, "dummy");
		final List<AbstractMessage> abstractTransportsToList = connection.sendListingRequest(Transport.ID, filter);//TODO - set FilterType
		if (!connection.getContentType().equalsIgnoreCase(Transport.ID)) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}	
		final ArrayList<TransportsToContainer> transportList = new ArrayList<TransportsToContainer>();
		for (final Iterator<AbstractMessage> itTransportList = abstractTransportsToList.iterator(); itTransportList.hasNext();) {
			final Transport transport = (Transport)itTransportList.next();
			final TransportsToContainer transportsToContainer = new TransportsToContainer();
			transportsToContainer.setPlannedLocation(transport.getPlanedLocation());
			transportsToContainer.setFromStreet(transport.getFromStreet());
			transportsToContainer.setFromCity(transport.getFromCity());
			transportsToContainer.setPatient(transport.getPatient());
			transportsToContainer.setToStreet(transport.getToStreet());
			transportsToContainer.setToCity(transport.getToCity());
			transportsToContainer.setAssistantPerson(transport.isAssistantPerson());
			transportsToContainer.setNotes(transport.getNotes());
			if(transport.getPlannedStartOfTransport() == 0) 
			{
				transportsToContainer.setPlannedStartOfTransport(null);
			} else 
			{
				transportsToContainer.setPlannedStartOfTransport(new Date(transport.getPlannedStartOfTransport()));
			}
			if(transport.getPlannedTimeAtPatient() == 0)
			{
				transportsToContainer.setPlannedTimeAtPatient(null);
			}else
			{
				transportsToContainer.setPlannedTimeAtPatient(new Date(transport.getPlannedTimeAtPatient()));
			}
			if(transport.getAppointmentTimeAtDestination() == 0)
			{
				transportsToContainer.setAppointmentTimeAtDestination(null);
			}else
			{
				transportsToContainer.setAppointmentTimeAtDestination(new Date(transport.getAppointmentTimeAtDestination()));
			}
			transportsToContainer.setKindOfTransport(transport.getKindOfTransport());
			
			transportList.add(transportsToContainer);
		}
		
		// Group
		final TransportsToContainerListContainer transportsToContainerListContainer = new TransportsToContainerListContainer(transportList);	
		final Comparator<Location> locationComparator = new PropertyComparator("locationName", true, true);
//		final Comparator<TransportsToContainer> transportToContainerComparator = new PropertyComparator("streetFrom", true, true);
		transportsToContainerListContainer.groupTransportsToBy(locationComparator);
//		transportsToContainerListContainer.sortTransportsTo(transportToContainerComparator);
		params.put(TRANSPORTSTO_CONTAINER_LIST_CONTAINER, transportsToContainerListContainer);
		
		return params;
	}
}
