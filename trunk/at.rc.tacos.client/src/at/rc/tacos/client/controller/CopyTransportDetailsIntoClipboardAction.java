package at.rc.tacos.client.controller;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.Transport;

/**
 * Class to copy the details of the transport into the windows clipboard
 * @author b.thek
 */
public class CopyTransportDetailsIntoClipboardAction extends Action implements ITransportStatus
{
	//properties
	private TableViewer viewer;
	private String transportNumber;
	private String patient;
	private String toStreet = "";
	private String toCity = "";
	private String to = "";
	private String fromStreet = "";
	private String fromCity = "";
	private String from = "";
	private String kindOfIllness = "";
	private String notes = "";
	private String priority;
	private String al = "";
	private String smsData;	
	private String kindOfTransport = "";	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public CopyTransportDetailsIntoClipboardAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Transportdetails in die Zwischenablage kopieren");
		setToolTipText("Kopiert die Transportdetails in die Windows Zwischenablage");
	}
	
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		//copy the details into the windows clipboard
		transportNumber = "TNr: " +String.valueOf(transport.getTransportNumber());
		
		fromStreet = "von: " +transport.getFromStreet();
		if(transport.getFromCity() != null)
			fromCity = " " +transport.getFromCity();
		from = fromStreet + "/" +fromCity;
	
		if (transport.getPatient() != null)
			patient = transport.getPatient().getLastname() +" " +transport.getPatient().getFirstname();
		
		if(transport.getToStreet() != null)
			toStreet = "nach: " +transport.getToStreet();
		if(transport.getToCity() != null)
			toCity = " " +transport.getToCity();
		if(!toStreet.trim().isEmpty() |! toCity.trim().isEmpty())
			to = toStreet +"/" +toCity;
		
		if(transport.getKindOfIllness() != null)
			kindOfIllness = transport.getKindOfIllness().getDiseaseName();
		
		if(transport.getNotes() != null |! transport.getNotes().trim().isEmpty())
			notes = transport.getNotes();
		
		//transport priority
		if(transport.getTransportPriority().equalsIgnoreCase("A"))
			priority = "1 NEF";
		else if (transport.getTransportPriority().equalsIgnoreCase("B"))
			priority = "2 Transport";
		else if (transport.getTransportPriority().equalsIgnoreCase("C"))
			priority = "3 Terminfahrt";
		else if (transport.getTransportPriority().equalsIgnoreCase("D"))
			priority = "4 Rücktransport";
		else if (transport.getTransportPriority().equalsIgnoreCase("E"))
			priority = "5 Heimtransport";
		else if (transport.getTransportPriority().equalsIgnoreCase("F"))
			priority = "6 Sonstiges";
		else if (transport.getTransportPriority().equalsIgnoreCase("G"))
			priority = "7 NEF extern";
		
		priority = "Pr: " +priority;
		
		if(transport.isBlueLight1())
			priority = priority + " BD1";
		
		if(transport.isBrkdtAlarming())
			al = "BRKDT";
		if(transport.isDfAlarming())
			al = al + " DF";
		if (transport.isEmergencyDoctorAlarming())//extern!!!
			al = al + " NA extern";
		if(transport.isFirebrigadeAlarming())
			al = al + "Feuerwehr";
		if(transport.isHelicopterAlarming())
			al = al + "Hubschrauber";	
		if(transport.isMountainRescueServiceAlarming())
			al = al + "Bergrettung";
		if(transport.isPoliceAlarming())
			al = al + "Polizei";
		if(transport.getKindOfTransport()!= null)
			kindOfTransport = transport.getKindOfTransport();
		
		if(!al.equalsIgnoreCase(""))
			al = "alarmiert: " +al;
		
		smsData = transportNumber +";" +priority +";" +kindOfTransport +";"  +from +";" +patient +";" +to +";"
		+notes +";" +kindOfIllness +";" +al;
		if(transport.isLongDistanceTrip())
			smsData = smsData + " Fernfahrt";
		if(transport.isEmergencyPhone())
			smsData = smsData + " Rufhilfepatient";
		
		//clipboard
		Clipboard clipboard = new Clipboard(PlatformUI.getWorkbench().getDisplay());
		TextTransfer textTransfer = TextTransfer.getInstance();
		clipboard.setContents(new Object[]{smsData}, new Transfer[]{textTransfer});
		clipboard.dispose();	
	}
}
