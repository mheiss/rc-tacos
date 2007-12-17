package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.RosterEntry;

public class PersonalViewLabelProvider implements ITableLabelProvider 
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
    public static final int COLUMN_STATION = 9;
    public static final int COLUMN_VEHICLE = 10;  
    


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
                return ImageFactory.getInstance().getRegisteredImage("image.personal.home");
            else
                return null;
        //show info symbol if there are notes
        case COLUMN_NOTES: 
            if(entry.hasNotes())
                return ImageFactory.getInstance().getRegisteredImage("image.personal.info");
            return null;
        //show a symbol in front of the name 
        case COLUMN_NAME: return ImageFactory.getInstance().getRegisteredImage("image.personal.user");
        case COLUMN_PLANED_WORK_TIME:
            if(entry.isSplitEntry())
                return ImageFactory.getInstance().getRegisteredImage("image.personal.timesplit");
            else
                return ImageFactory.getInstance().getRegisteredImage("image.personal.time");
        
        //show a symbol if the planned time is not handled
        case COLUMN_CHECK_IN: 
            //user is not cheked in but he should
            if(entry.getRealStartOfWork() == 0 && entry.getPlannedStartOfWork() > new Date().getTime())
                return ImageFactory.getInstance().getRegisteredImage("image.personal.alert");
            return null;
        case COLUMN_CHECK_OUT: 
            //user is not cheked out but he should
            if(entry.getPlannedEndOfWork() == 0 && entry.getPlannedEndOfWork() < new Date().getTime())
                return ImageFactory.getInstance().getRegisteredImage("image.personal.alert");
            return null;
        default: return null;
        }
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
        RosterEntry entry = (RosterEntry)element;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        
        switch(columnIndex)
        {
        case COLUMN_LOCK: return null;
        case COLUMN_STANDBY: return null;
        case COLUMN_NOTES: return null;
        case COLUMN_NAME: return entry.getStaffMember().getLastname()+ " " + entry.getStaffMember().getFirstName();
        case COLUMN_PLANED_WORK_TIME: return sdf.format(entry.getPlannedStartOfWork()) + " - " + sdf.format(entry.getPlannedEndOfWork());
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
        case COLUMN_SERVICE_TYPE: return entry.getServicetype();
        case COLUMN_JOB: return entry.getJob();
        case COLUMN_STATION: return entry.getStation();
        case COLUMN_VEHICLE: return "Auto";
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
}
