package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.modelManager.VehicleManager;
import at.rc.tacos.client.util.CustomColors;

public class PersonalViewLabelProvider implements ITableLabelProvider, ITableColorProvider
{
    //define the columns
    public static final int COLUMN_LOCK = 0;
    public static final int COLUMN_STANDBY = 1;
    public static final int COLUMN_NOTES = 2;
    public static final int COLUMN_NAME = 3;
    public static final int COLUMN_PLANED_WORK_TIME = 4;
    public static final int COLUMN_CHECK_IN = 5;
    public static final int COLUMN_CHECK_OUT = 6;
    public static final int COLUMN_SERVICE_TYPE = 7;
    public static final int COLUMN_JOB = 8;
    public static final int COLUMN_VEHICLE = 9;  
    
    //the vehicle manager
    private VehicleManager vehicleManager = ModelFactory.getInstance().getVehicleManager(); 
    

    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
        RosterEntry entry = (RosterEntry)element;
        //determine the colum and return a image if needed
        switch(columnIndex)
        {
        //show lock if the entry is locked
        case COLUMN_LOCK: return null;
        //show house symbol if the person is at home
        case COLUMN_STANDBY: 
            if(entry.getStandby())
                return ImageFactory.getInstance().getRegisteredImage("resource.location");
            else
                return null;
        //show info symbol if there are notes
        case COLUMN_NOTES: 
            if(entry.hasNotes())
                return ImageFactory.getInstance().getRegisteredImage("resource.info");
            return null;
        //show a symbol in front of the name 
        case COLUMN_NAME: 
            if(vehicleManager.getVehicleOfStaff(entry.getStaffMember().getStaffMemberId()) == null)
                return ImageFactory.getInstance().getRegisteredImage("resource.user");
            else
                return ImageFactory.getInstance().getRegisteredImage("resource.userVehicle");
        case COLUMN_PLANED_WORK_TIME:
            if(entry.isSplitEntry())
                return ImageFactory.getInstance().getRegisteredImage("resource.timesplit");
            else
                return ImageFactory.getInstance().getRegisteredImage("resource.time");
        
        //show a symbol if the planned time is not handled
        case COLUMN_CHECK_IN: 
            //user is not cheked in but he should
            if(entry.getRealStartOfWork() != 0)
            	return null;       
            if (entry.getPlannedStartOfWork() < new Date().getTime())
            	return ImageFactory.getInstance().getRegisteredImage("resource.warning");
            else
            return null;
        case COLUMN_CHECK_OUT: 
            //user is not cheked out but he should
            if(entry.getRealEndOfWork() != 0)
            	return null;
            if(entry.getPlannedEndOfWork() < new Date().getTime())
                return ImageFactory.getInstance().getRegisteredImage("resource.warning");
            return null;
        default: return null;
        }
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
        RosterEntry entry = (RosterEntry)element;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        long displayedDate = SessionManager.getInstance().getDisplayedDate();
        String plannedStart;
        String plannedEnd;
        
        switch(columnIndex)
        {
	        case COLUMN_LOCK: return null;
	        case COLUMN_STANDBY: return null;
	        case COLUMN_NOTES: return null;
	        case COLUMN_NAME: return entry.getStaffMember().getLastName()+ " " + entry.getStaffMember().getFirstName();
	        
	        case COLUMN_PLANED_WORK_TIME: 
	        	if(entry.getPlannedStartOfWork() <displayedDate)
	        		plannedStart = "00:00";
	        	else 
	        		plannedStart = sdf.format(entry.getPlannedStartOfWork());
	        	if(entry.getPlannedEndOfWork() > displayedDate)
	        		plannedEnd = "00:00";
	        	else
	        		plannedEnd = sdf.format(entry.getPlannedEndOfWork());
	        	return plannedStart +" - " +plannedEnd;
	        case COLUMN_CHECK_IN: 
	        	if (entry.getRealStartOfWork()!= 0)
	        		return sdf.format(entry.getRealStartOfWork());
	        	else 
	        		return "";
	        case COLUMN_CHECK_OUT: 
	        	if (entry.getRealEndOfWork() != 0)
	        		return sdf.format(entry.getRealEndOfWork());
	        	else
	        		return "";
	        case COLUMN_SERVICE_TYPE: return entry.getServicetype().getServiceName();
	        case COLUMN_JOB: return entry.getJob().getJobName();
	        case COLUMN_VEHICLE: 
	        	VehicleDetail detail = vehicleManager.getVehicleOfStaff(entry.getStaffMember().getStaffMemberId());
	        	//assert valid
	        	if(detail != null)
	        		return detail.getVehicleName();
	        	return null;
	        default: return null;
        }   
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
	public Color getBackground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) 
	{
		RosterEntry entry = (RosterEntry)element;
        if (entry.getRealStartOfWork() != 0 && entry.getRealEndOfWork() == 0 && vehicleManager.getVehicleOfStaff(entry.getStaffMember().getStaffMemberId()) == null)
		    return null;//black
		if (entry.getRealStartOfWork() == 0 && entry.getRealEndOfWork() == 0)
		    return CustomColors.DARK_GREY_COLOR;
		if (entry.getRealStartOfWork() != 0 && entry.getRealEndOfWork() != 0)
		    return CustomColors.DARK_GREY_COLOR;
		if(entry.getRealStartOfWork() != 0 && entry.getRealEndOfWork() == 0 && vehicleManager.getVehicleOfStaff(entry.getStaffMember().getStaffMemberId()) != null)
			return CustomColors.BACKGROUND_BLUE;
		
		return CustomColors.BACKGROUND_RED;
	}
}
