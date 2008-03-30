package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.VehicleDetail;

public class VehicleViewTableLabelProvider implements ITableLabelProvider, ITableColorProvider, ITransportStatus, IKindOfTransport
{
    //define the columns
    public static final int COLUMN_NAME = 0;
    public static final int COLUMN_STATUS = 1;
    

    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
    	VehicleDetail vehicle = (VehicleDetail)element;
    	

		//determine the column and return a image if needed
		switch(columnIndex)
		{
			case COLUMN_STATUS:
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_GREEN)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.green");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_NA)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.na");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_YELLOW)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.yellow");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPROT_STATUS_RED)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.red");
				
			default: return null; 
		}
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
    	VehicleDetail detail = (VehicleDetail)element;
    	
        switch(columnIndex)
        {
	        case COLUMN_NAME:return detail.getVehicleName();
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
		return null;
	}
}