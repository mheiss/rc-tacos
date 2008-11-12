package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.VehicleService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class VehicleHandler implements Handler<VehicleDetail> {

	@Service(clazz = VehicleService.class)
	private VehicleService vehicleService;

	@Override
	public void add(ServerIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		if (!vehicleService.addVehicle(model))
			throw new ServiceException("Failed to add the vehicle: " + model);
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		List<VehicleDetail> list = vehicleService.listVehicles();
		if (list == null)
			throw new ServiceException("Failed to list the vehicles");
		return list;
	}

	@Override
	public void remove(ServerIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		if (!vehicleService.removeVehicle(model))
			throw new ServiceException("Failed to remove the vehicle " + model);
		return model;
	}

	@Override
	public void update(ServerIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		if (!vehicleService.updateVehicle(model))
			throw new ServiceException("Failed to update the vehicle " + model);
		return model;
	}

	@Override
	public void execute(ServerIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
