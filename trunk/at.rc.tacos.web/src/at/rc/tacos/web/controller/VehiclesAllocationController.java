package at.rc.tacos.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.support.PropertyComparator;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.web.form.VehicleContainer;
import at.rc.tacos.web.form.VehicleContainerListContainer;
import at.rc.tacos.web.session.UserSession;

/**
 * Vehicles Allocation Controller
 * @author Payer Martin
 * @version 1.0
 */
public class VehiclesAllocationController extends Controller {

	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		// TODO Auto-generated method stub
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final List<AbstractMessage> abstractVehicleList = connection.sendListingRequest(VehicleDetail.ID, null);
		if (!connection.getContentType().equalsIgnoreCase(VehicleDetail.ID)) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}		
		final List<VehicleContainer> vehicleDetailList = new ArrayList<VehicleContainer>();
		for (final Iterator<AbstractMessage> itVehicleDetailList = abstractVehicleList.iterator(); itVehicleDetailList.hasNext();) {
			final VehicleDetail vehicleDetail = (VehicleDetail)itVehicleDetailList.next();
			final VehicleContainer vehicleContainer = new VehicleContainer();
			vehicleContainer.setBasicStation(vehicleDetail.getBasicStation());
			vehicleContainer.setCurrentStation(vehicleDetail.getCurrentStation());
			vehicleContainer.setDriver(vehicleDetail.getDriver());
			vehicleContainer.setFirstParamedic(vehicleDetail.getFirstParamedic());
			vehicleContainer.setMobilePhone(vehicleDetail.getMobilePhone());
			vehicleContainer.setOutOfOrder(vehicleDetail.isOutOfOrder());
			vehicleContainer.setReadyForAction(vehicleDetail.isReadyForAction());
			vehicleContainer.setSecondParamedic(vehicleDetail.getSecondParamedic());
			vehicleContainer.setTransportStatus(vehicleDetail.getTransportStatus());
			vehicleContainer.setVehicleName(vehicleDetail.getVehicleName());
			vehicleContainer.setVehicleNotes(vehicleDetail.getVehicleNotes());
			vehicleContainer.setVehicleType(vehicleDetail.getVehicleType());
			vehicleDetailList.add(vehicleContainer);
		}
		
		
		// Group
		final VehicleContainerListContainer vehicleContainerListContainer = new VehicleContainerListContainer(vehicleDetailList);	
		final Comparator<Location> locationComparator = new PropertyComparator("locationName", true, true);
		final Comparator<VehicleContainer> vehicleContainerComparator = new PropertyComparator("vehicleName", true, true);
		vehicleContainerListContainer.groupVehiclesBy(locationComparator);
		vehicleContainerListContainer.sortVehicles(vehicleContainerComparator);
		params.put("vehicleContainerListContainer", vehicleContainerListContainer);
		
		return params;
	}

}
