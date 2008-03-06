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
        setText("Rücktransport erstellen");
        setToolTipText("Erstellt den Rücktransport zum ausgewählten Transport");
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
        t2.setHelicopterAlarming(t1.isHelicopterAlarming());
        t2.setPoliceAlarming(t1.isPoliceAlarming());
        t2.setAssistantPerson(t1.isAssistantPerson());
        t2.setBackTransport(t1.isBackTransport());
        t2.setBlueLightToGoal(t1.isBlueLightToGoal());
        t2.setBrkdtAlarming(t1.isBrkdtAlarming());
        t2.setFirebrigadeAlarming(t1.isFirebrigadeAlarming());
        t2.setDfAlarming(t1.isDfAlarming());
        t2.setEmergencyDoctorAlarming(t1.isEmergencyDoctorAlarming());
        t2.setEmergencyPhone(t1.isEmergencyPhone());
        t2.setLongDistanceTrip(t1.isLongDistanceTrip());
        t2.setMountainRescueServiceAlarming(t1.isMountainRescueServiceAlarming());
        //assert valid
        t2.setKindOfIllness(t1.getKindOfIllness());
        t2.setKindOfTransport(t1.getKindOfTransport());
        if(t1.getCallerDetail() != null)
            t2.setCallerDetail(t1.getCallerDetail());
        if(t1.getFeedback() != null)
            t2.setFeedback(t1.getFeedback());
        //destionation and target
        t2.setPlanedLocation(t1.getPlanedLocation());
        t2.setPatient(t1.getPatient());
        //switch the address for the back transport
        t2.setFromStreet(t1.getToStreet()); 
        t2.setFromCity(t1.getToCity());      
        t2.setToStreet(t1.getFromStreet()); 
        t2.setToCity(t1.getFromCity()); 

        NetWrapper.getDefault().sendAddMessage(Transport.ID, t2);
    }
}
