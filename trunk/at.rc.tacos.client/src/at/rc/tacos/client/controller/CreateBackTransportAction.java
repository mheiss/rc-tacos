package at.rc.tacos.client.controller;

import java.util.Calendar;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

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

        //create the back transport
        Transport t2 = t1;
        t2.setTransportId(0);
        //switch the address for the back transport
        t2.setFromStreet(t1.getToStreet()); 
        t2.setFromCity(t1.getToCity());      
        t2.setToStreet(t1.getFromStreet()); 
        t2.setToCity(t1.getFromCity()); 
        t2.setCreationTime(Calendar.getInstance().getTimeInMillis());
        t2.setDateOfTransport(Calendar.getInstance().getTimeInMillis());
        t2.setPlannedStartOfTransport(Calendar.getInstance().getTimeInMillis());
        t2.setTransportPriority("D");
        t2.setDirection(0);
        t2.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);

        NetWrapper.getDefault().sendAddMessage(Transport.ID, t2);
    }
}
