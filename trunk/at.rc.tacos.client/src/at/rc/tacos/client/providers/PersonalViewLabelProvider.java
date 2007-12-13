package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import at.rc.tacos.model.RosterEntry;

public class PersonalViewLabelProvider implements ITableLabelProvider 
{
    //define the columns
    public static final int COLUMN_STANDBY = 0;
    public static final int COLUMN_NOTES = 1;
    public static final int COLUMN_NAME = 2;
    public static final int COLUMN_PLANED_WORK_TIME = 3;
    public static final int COLUMN_CHECK_IN = 4;
    public static final int COLUMN_CHECK_OUT = 5;
    public static final int COLUMN_SERVICE_TYPE = 6;
    public static final int COLUMN_COMPETENCE = 7;
    public static final int COLUMN_STATION = 8;
    public static final int COLUMN_VEHICLE = 9;   
    
    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
        RosterEntry entry = (RosterEntry)element;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        switch(columnIndex)
        {
        case COLUMN_STANDBY: return String.valueOf(entry.getStandby()); 
        case COLUMN_NOTES: 
            if (entry.getRosterNotes() == null || entry.getRosterNotes().isEmpty())
                return "keine Notizen";
            else
                return entry.getRosterNotes();
        case COLUMN_NAME: return entry.getStaffMember().getLastname()+ " " + entry.getStaffMember().getFirstName();
        case COLUMN_PLANED_WORK_TIME: return sdf.format(entry.getPlannedStartOfWork()) + " - " + sdf.format(entry.getPlannedEndOfWork());
        case COLUMN_CHECK_IN: return sdf.format(entry.getRealStartOfWork());
        case COLUMN_CHECK_OUT: return sdf.format(entry.getRealEndOfWork());
        case COLUMN_SERVICE_TYPE: return entry.getServicetype();
        case COLUMN_COMPETENCE: return entry.getCompetence();
        case COLUMN_STATION: return entry.getStation();
        case COLUMN_VEHICLE: return "Auto";
        default: return null;
        }
    }

    @Override
    public void addListener(ILabelProviderListener arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isLabelProperty(Object arg0, String arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener arg0)
    {
        // TODO Auto-generated method stub
        
    }
}
