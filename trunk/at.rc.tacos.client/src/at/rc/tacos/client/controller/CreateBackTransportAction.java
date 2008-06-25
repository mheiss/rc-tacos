package at.rc.tacos.client.controller;

import java.util.Calendar;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Creates a backtransport for the selected transport
 * @author b.thek
 */
public class CreateBackTransportAction extends Action implements IProgramStatus
{
    //properties
    private TableViewer viewer;

    /**
     * Default class constructor.
     * @param viewer the table viewer
     */
    public CreateBackTransportAction(TableViewer viewer)
    {
        this.viewer = viewer;
        setText("R�cktransport erstellen");
        setToolTipText("Erstellt den R�cktransport zum ausgew�hlten Transport");
    }

    @Override
    public void run()
    {
        //the selection
        ISelection selection = viewer.getSelection();
        
        //get the selected transport
        Transport t1 = (Transport)((IStructuredSelection)selection).getFirstElement();  
        
        //copy the transport
        Transport t2 = new Transport();
        
        //reset the values for the second transport
        t2.setCreatedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
        t2.setTransportId(0);
        t2.setTransportNumber(0);
        t2.clearVehicleDetail();
        t2.setCreationTime(Calendar.getInstance().getTimeInMillis());
        t2.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);
        t2.setTransportPriority("D");
        t2.getStatusMessages().clear();
        t2.setDirection(0);
        
        //date and time
        t2.setYear(Calendar.getInstance().get(Calendar.YEAR));
        t2.setDateOfTransport(Calendar.getInstance().getTimeInMillis());
        t2.setPlannedStartOfTransport(Calendar.getInstance().getTimeInMillis());
        
        //alarming
        t2.setAssistantPerson(t1.isAssistantPerson());
        t2.setBackTransport(false);
        t2.setEmergencyPhone(t1.isEmergencyPhone());
        t2.setLongDistanceTrip(t1.isLongDistanceTrip());
        
        //assert valid
        if(t1.getKindOfIllness() != null)
        	t2.setKindOfIllness(t1.getKindOfIllness());
        if(t1.getKindOfTransport() != null)
        	t2.setKindOfTransport(t1.getKindOfTransport());
        if(t1.getCallerDetail() != null)
            t2.setCallerDetail(t1.getCallerDetail());
        if(t1.getFeedback() != null)
            t2.setFeedback(t1.getFeedback());
        
        //destination and target
        t2.setPlanedLocation(t1.getPlanedLocation());
        t2.setPatient(t1.getPatient());
        
        //switch the address for the back transport
        if(t1.getToStreet() != null &! t1.getToStreet().trim().equalsIgnoreCase(""))
        	t2.setFromStreet(t1.getToStreet());
        else
        	t2.setFromStreet("kein Eintrag");
        if(t1.getToCity() != null &! t1.getToCity().trim().equalsIgnoreCase(""))
        	t2.setFromCity(t1.getToCity()); 
        else
        	t2.setFromCity("kein Eintrag");
        t2.setToStreet(t1.getFromStreet()); 
        t2.setToCity(t1.getFromCity()); 
        
        //view the arrow for the back transport no longer, so set the field "R�cktransport m�glich" to false
        t1.setBackTransport(false);

        NetWrapper.getDefault().sendAddMessage(Transport.ID, t2);
        NetWrapper.getDefault().sendUpdateMessage(Transport.ID, t1);
    }
}
