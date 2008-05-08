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
import at.rc.tacos.web.form.VehicleListContainer;
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
		final List<VehicleDetail> vehicleDetailList = new ArrayList<VehicleDetail>();
		for (final Iterator<AbstractMessage> itVehicleDetailList = abstractVehicleList.iterator(); itVehicleDetailList.hasNext();) {
			final VehicleDetail vehicleDetail = (VehicleDetail)itVehicleDetailList.next();
			vehicleDetailList.add(vehicleDetail);
		}
		
		// Group
		final VehicleListContainer vehicleListContainer = new VehicleListContainer(vehicleDetailList);	
		final Comparator<Location> locationComparator = new PropertyComparator("locationName", true, true);
		final Comparator<VehicleDetail> vehicleDetailComparator = new PropertyComparator("vehicleName", true, true);
		vehicleListContainer.groupVehiclesBy(locationComparator);
		vehicleListContainer.sortVehicles(vehicleDetailComparator);
		params.put("vehicleListContainer", vehicleListContainer);
		
		return params;
	}

}
