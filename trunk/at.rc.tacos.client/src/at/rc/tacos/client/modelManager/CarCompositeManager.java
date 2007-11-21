package at.rc.tacos.client.modelManager;

import org.eclipse.swt.layout.grouplayout.GroupLayout.Group;
import org.eclipse.swt.widgets.Composite;

import at.rc.tacos.client.view.composite.CarComposite;
import at.rc.tacos.model.VehicleDetail;

/**
 * logic for creating of CarComposite
 * @author b.thek
 *
 */
//TODO: change background and foreground color depending on the status

public class CarCompositeManager 
{
	Composite composite; 
	
	private String vehicleName;
	private String driverName;
	private String vehicleType;
	private String paramedicIName;
	private String paramedicIIName;
	private boolean mobilePhoneStatus = false;
	private boolean notesStatus = false;
	private boolean stationStatus = false;
	private boolean readyForAction;
	private boolean outOfOrder;
	private int mostImportantTransportStatus;
	
	
	public CarCompositeManager()
	{
		
	}
	
	public CarComposite getCarComposite(Composite composite, VehicleDetail vehicle)
	{
		this.composite = composite;
		this.vehicleName = vehicle.getVehicleName();
		this.vehicleType = vehicle.getVehicleType();
		this.driverName = vehicle.getDriverName().toString();
		this.paramedicIName = vehicle.getParamedicIName().toString();
		this.paramedicIIName = vehicle.getParamedicIIName().toString();
		
		if (!vehicle.getMobilePhone().getMobilePhoneId().equalsIgnoreCase(vehicle.getVehicleName()))
		{
			mobilePhoneStatus = true;//other mobile phone than default
		}
		
		if (!vehicle.getVehicleNotes().isEmpty())
		{
			notesStatus = true;//show that there are notes
		}
		
		if (!vehicle.getBasicStation().equalsIgnoreCase(vehicle.getCurrentStation()))
		{
			stationStatus = true;//if the basis station is different to the current station
		}
		
		this.readyForAction = vehicle.isReadyForAction();
		this.outOfOrder = vehicle.isOutOfOrder();
		this.mostImportantTransportStatus=vehicle.getMostImportantTransportStatus();
	
		
		//create the composite
		CarComposite carComposite = new CarComposite(composite, vehicleName, vehicleType, driverName, paramedicIName,
				paramedicIIName, mobilePhoneStatus, notesStatus, stationStatus, readyForAction, outOfOrder, 
				mostImportantTransportStatus);
		
		return carComposite;
	}
}
