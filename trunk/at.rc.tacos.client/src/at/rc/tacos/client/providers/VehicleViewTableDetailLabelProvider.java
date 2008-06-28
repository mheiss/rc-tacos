package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.VehicleDetail;

public class VehicleViewTableDetailLabelProvider implements ITableLabelProvider, ITableColorProvider, ITransportStatus, IKindOfTransport, ITableFontProvider
{
    //define the columns
	public static final int COLUMN_LOCK = 0;
    public static final int COLUMN_READY = 1;
    public static final int COLUMN_VEHICLE_NAME = 2;
    public static final int COLUMN_STATUS = 3;
    public static final int COLUMN_DRIVER = 4;
    public static final int COLUMN_MEDIC_I = 5;
    public static final int COLUMN_MEDIC_II = 6;
    public static final int COLUMN_PHONE = 7;
    public static final int COLUMN_STATION = 8;
    public static final int COLUMN_OUTOFORDER = 9;
    public static final int COLUMN_NOTES = 10;
    public static final int COLUMN_LAST_DESTINATION_FREE = 11;
    
    //the lock manager
    private LockManager lockManager = ModelFactory.getInstance().getLockManager();
    
    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
    	VehicleDetail vehicle = (VehicleDetail)element;
		//determine the column and return a image if needed
		switch(columnIndex)
		{
			case COLUMN_LOCK:
				if(lockManager.containsLock(VehicleDetail.ID, vehicle.getVehicleName()))
					return ImageFactory.getInstance().getRegisteredImage("resource.lock18");
				else return ImageFactory.getInstance().getRegisteredImage("resource.nothing18");
			case COLUMN_READY:
				if(vehicle.isReadyForAction())
					return ImageFactory.getInstance().getRegisteredImage("vehicle.ready");
				else return null;
		
			case COLUMN_STATUS:
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_GREEN)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.green");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_NA)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.na");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_YELLOW)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.yellow");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPROT_STATUS_RED)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.red");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_BLUE)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.blue");
				else return null;
				
			case COLUMN_PHONE:
				if (!vehicle.getMobilePhone().getMobilePhoneName().equalsIgnoreCase(vehicle.getVehicleName()))
		            return ImageFactory.getInstance().getRegisteredImage("vehicle.phone");
				else return null;
			case COLUMN_STATION:
				if(!vehicle.getBasicStation().getLocationName().equalsIgnoreCase(vehicle.getCurrentStation().getLocationName()))
					return ImageFactory.getInstance().getRegisteredImage("vehicle.house");
				else return null;
			case COLUMN_OUTOFORDER:
				if(vehicle.isOutOfOrder())
					return ImageFactory.getInstance().getRegisteredImage("vehicle.repair");
				else return null;
			case COLUMN_NOTES:
				if(vehicle.getVehicleNotes() != null)
					if(!vehicle.getVehicleNotes().isEmpty())
						return ImageFactory.getInstance().getRegisteredImage("vehicle.notes");
			default: return null; 
		}
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
    	VehicleDetail detail = (VehicleDetail)element;
    	
        switch(columnIndex)
        {
	        case COLUMN_VEHICLE_NAME:return detail.getVehicleName();
	        case COLUMN_DRIVER: 
	        	if(detail.getDriver() != null)
	        		return detail.getDriver().getLastName() + " " +detail.getDriver().getFirstName();
	        	else return null;
	        case COLUMN_MEDIC_I:
	        	if(detail.getFirstParamedic() != null)
	        		return detail.getFirstParamedic().getLastName() + " " +detail.getFirstParamedic().getFirstName();
	        	else return null;
	        case COLUMN_MEDIC_II:
	        	if(detail.getSecondParamedic() != null)
	        		return detail.getSecondParamedic().getLastName() +" " +detail.getSecondParamedic().getFirstName();
	        	else return null;
	        case COLUMN_LAST_DESTINATION_FREE:
	        	if(detail.getLastDestinationFree() != null)
	        		return detail.getLastDestinationFree();
	        	else return null;	
	    }
        return null;
    }

    @Override
    public void addListener(ILabelProviderListener arg0) {  }

    @Override
    public void dispose() {  }

    @Override
    public boolean isLabelProperty(Object arg0, String arg1) 
    {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener arg0)  { }

	@Override
	public Color getBackground(Object element, int columnIndex) 
	{
    	return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) 
	{      
		VehicleDetail detail = (VehicleDetail)element;
		if(detail.isOutOfOrder())
			return CustomColors.GREY_COLOR;
		return null;
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return CustomColors.VEHICLE_TABLE;
	}
}