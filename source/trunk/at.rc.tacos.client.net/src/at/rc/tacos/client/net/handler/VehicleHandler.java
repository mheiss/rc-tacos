package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>VehicleHandler</code> manages the locally chached
 * {@link VehicleDetail} instances.
 * 
 * @author Michael
 */
public class VehicleHandler implements Handler<VehicleDetail> {

	private List<VehicleDetail> vehicleList = Collections.synchronizedList(new LinkedList<VehicleDetail>());
	private Logger log = LoggerFactory.getLogger(VehicleHandler.class);

	@Override
	public void add(MessageIoSession session, Message<VehicleDetail> message) throws SQLException, ServiceException {
		synchronized (vehicleList) {
			vehicleList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<VehicleDetail> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<VehicleDetail> message) throws SQLException, ServiceException {
		synchronized (vehicleList) {
			vehicleList.clear();
			vehicleList.addAll(message.getObjects());
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<VehicleDetail> message) throws SQLException, ServiceException {
		synchronized (vehicleList) {
			vehicleList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<VehicleDetail> message) throws SQLException, ServiceException {
		synchronized (vehicleList) {
			for (VehicleDetail updatedVehicle : vehicleList) {
				if (!vehicleList.contains(updatedVehicle)) {
					continue;
				}
				int index = vehicleList.indexOf(updatedVehicle);
				vehicleList.set(index, updatedVehicle);
			}
		}
	}

	/**
	 * Returns the vehicle to which the staff member is currently assigned to
	 * 
	 * @param staffId
	 *            the id of the staff to check
	 * @return the vehicle or null if the staff member is not assigned to a
	 *         vehicle
	 */
	public VehicleDetail getVehicleOfStaff(int staffId) {
		synchronized (vehicleList) {
			for (VehicleDetail detail : vehicleList) {
				// assert valid
				if (detail.getDriver() != null) {
					if (detail.getDriver().getStaffMemberId() == staffId)
						return detail;
				}
				// assert valid
				if (detail.getFirstParamedic() != null) {
					if (detail.getFirstParamedic().getStaffMemberId() == staffId)
						return detail;
				}
				// assert valid
				if (detail.getSecondParamedic() != null) {
					if (detail.getSecondParamedic().getStaffMemberId() == staffId)
						return detail;
				}
			}
			// no assigned vehicle
			return null;
		}
	}

	/**
	 * Returns the first <code>VehicleDetail</code> instance that exactly
	 * matches the string returned by {@link VehicleDetail#getVehicleName()}.
	 * 
	 * @param vehicleName
	 *            the name of the <code>VehicleDetail</code> instance to search
	 * @return the matched <code>VehicleDetail</code> or null if nothing found
	 */
	public VehicleDetail getVehicleByName(String vehicleName) {
		synchronized (vehicleName) {
			for (VehicleDetail detail : vehicleList) {
				if (detail.getVehicleName().equalsIgnoreCase(vehicleName))
					return detail;
			}
			// no vehicle
			return null;
		}
	}

	/**
	 * Returns a list of all vehicles which have NOT the status
	 * {@link VehicleDetail#isOutOfOrder()} and and the status the status
	 * {@link VehicleDetail#isReadyForAction()} set.
	 * <p>
	 * In fact this will return a list of all vehicles which can be used.
	 * </p>
	 * 
	 * @return the list of vehicles ready for action
	 */
	public List<VehicleDetail> getReadyVehicleList() {
		synchronized (vehicleList) {
			List<VehicleDetail> filteredList = new ArrayList<VehicleDetail>();
			// loop over all vehicles
			for (VehicleDetail detail : vehicleList) {
				if (!detail.isOutOfOrder() && detail.isReadyForAction())
					filteredList.add(detail);
			}
			return filteredList;
		}
	}

	/**
	 * This method is similar to {@link #getReadyVehicleList()}. In addition it
	 * filters the <code>VehicleDetail</code> instances by the location returned
	 * by {@link VehicleDetail#getBasicStation()}
	 * 
	 * @param location
	 *            the location of the <code>VehicleDetail</code> instance to
	 *            search
	 * @return list of vehicles ready for action
	 */
	public List<VehicleDetail> getReadyVehicleListbyLocation(Location location) {
		synchronized (vehicleList) {
			List<VehicleDetail> filteredList = new ArrayList<VehicleDetail>();
			// loop over all vehicles
			for (VehicleDetail detail : vehicleList) {
				if (!detail.isOutOfOrder() && detail.isReadyForAction() && detail.getBasicStation().equals(location))
					filteredList.add(detail);
			}
			return filteredList;
		}
	}

	/**
	 * Returns the first <i>NEF</i> <code>VehicleDetail</code> instance found.
	 * <p>
	 * The vehicle is identified by the {@link VehicleDetail#getVehicleName()}
	 * that must exactly match the string 'NEF'
	 * </p>
	 * 
	 * @return the <i>NEF</i> vehicle or null if not found
	 */
	public VehicleDetail getNEFVehicle() {
		synchronized (log) {
			for (VehicleDetail detail : vehicleList) {
				if (detail.getVehicleName().equalsIgnoreCase("NEF"))
					return detail;
			}
			return null;
		}
	}

	/**
	 * Returns a new array containing the managed <code>VehicleDetail</code>
	 * instances.
	 * 
	 * @return an array containing the <code>VehicleDetail</code> instances.
	 */
	public VehicleDetail[] toArray() {
		return vehicleList.toArray(new VehicleDetail[vehicleList.size()]);
	}
}
