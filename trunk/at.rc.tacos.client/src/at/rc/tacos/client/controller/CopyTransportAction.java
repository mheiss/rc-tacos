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
 * Duplicates the transport
 * @author b.thek
 */
public class CopyTransportAction extends Action implements IProgramStatus
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public CopyTransportAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Transport kopieren");
		setToolTipText("Dupliziert den ausgewählten Transport");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport t1 = (Transport)((IStructuredSelection)selection).getFirstElement();
		//copy the transport
		Transport t2 = t1;
		t2.setCreationTime(Calendar.getInstance().getTimeInMillis());
    	
    	if(t1.getProgramStatus()== PROGRAM_STATUS_UNDERWAY)
    	{
    		System.out.println("CopyTransportAction, im if, program status of t1: " +t1.getProgramStatus());
    		System.out.println("CopyTransportAction, im if, program status of t2: " +t2.getProgramStatus());
    	}
    		
    		
    	t2.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);
    	
    	System.out.println("CopyTransportAction, danaaaaaach, program status of t1: " +t1.getProgramStatus());
		System.out.println("CopyTransportAction, danaaaaaach, program status of t2: " +t2.getProgramStatus());
    	
    	//add the transport
    	NetWrapper.getDefault().sendAddMessage(Transport.ID, t2);
	}
}
