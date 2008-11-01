package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.VehicleService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.INetHandler;

public class VehicleHandler implements INetHandler<VehicleDetail> {

	@Service(clazz = VehicleService.class)
	private VehicleService vehicleService;

	@Override
	public VehicleDetail add(VehicleDetail model) throws ServiceException, SQLException {
		if (!vehicleService.addVehicle(model))
			throw new ServiceException("Failed to add the vehicle: " + model);
		return model;
	}

	@Override
	public List<VehicleDetail> execute(String command, List<VehicleDetail> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<VehicleDetail> get(Map<String, String> params) throws ServiceException, SQLException {
		List<VehicleDetail> list = vehicleService.listVehicles();
		if (list == null)
			throw new ServiceException("Failed to list the vehicles");
		return list;
	}

	@Override
	public VehicleDetail remove(VehicleDetail model) throws ServiceException, SQLException {
		if (!vehicleService.removeVehicle(model))
			throw new ServiceException("Failed to remove the vehicle " + model);
		return model;
	}

	@Override
	public VehicleDetail update(VehicleDetail model) throws ServiceException, SQLException {
		if (!vehicleService.updateVehicle(model))
			throw new ServiceException("Failed to update the vehicle " + model);
		return model;
	}

}
