package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.VehicleService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class VehicleHandler implements Handler<VehicleDetail> {

	@Service(clazz = VehicleService.class)
	private VehicleService vehicleService;

	@Override
	public void add(MessageIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		List<VehicleDetail> vehicleList = message.getObjects();
		// loop and add the vehicles
		for (VehicleDetail vehicle : vehicleList) {
			if (!vehicleService.addVehicle(vehicle))
				throw new ServiceException("Failed to add the vehicle: " + vehicle);
		}
		// brodcast the added transports
		session.writeBrodcast(message, vehicleList);
	}

	@Override
	public void get(MessageIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		List<VehicleDetail> list = vehicleService.listVehicles();
		if (list == null)
			throw new ServiceException("Failed to list the vehicles");
		// return the requested vehicles
		session.write(message, list);
	}

	@Override
	public void remove(MessageIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		List<VehicleDetail> vehicleList = message.getObjects();
		// loop and remove the vehicles
		for (VehicleDetail vehicle : vehicleList) {
			if (!vehicleService.removeVehicle(vehicle))
				throw new ServiceException("Failed to remove the vehicle " + vehicle);
		}
		// brodcast the removed vehicles
		session.writeBrodcast(message, vehicleList);
	}

	@Override
	public void update(MessageIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		List<VehicleDetail> vehicleList = message.getObjects();
		// loop and update the vehicles
		for (VehicleDetail vehicle : vehicleList) {
			if (!vehicleService.updateVehicle(vehicle))
				throw new ServiceException("Failed to update the vehicle " + vehicle);
		}
		// brodcast the updated vehicles
		session.writeBrodcast(message, vehicleList);
	}

	@Override
	public void execute(MessageIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
