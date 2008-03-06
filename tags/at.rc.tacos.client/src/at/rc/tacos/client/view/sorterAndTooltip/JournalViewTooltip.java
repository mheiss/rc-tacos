
package at.rc.tacos.client.view.sorterAndTooltip;

import java.text.SimpleDateFormat;

import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;

import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

/**
 * This shows the tool tip for journal transport.
 * @author b.thek
 */
public class JournalViewTooltip extends ToolTip implements ITransportStatus
{	
	//properties
	private Transport transport;
	
	private String police = "";
	private String firebrigade = "";
	private String brkdt = "";
	private String df = "";
	private String emergencyDoctor = "";
	private String helicopter= "";
	private String mountainRescue = "";
	
	
	/**
	 * Creates a new tool tip for the journal transport
	 * @param control the control for the tool tip to show
	 */
	public JournalViewTooltip(Control control) 
	{
		super(control);
		setShift(new Point(1, 1));
	}
	
	/**
	 * Returns whether or not the tooltip should be created.
	 * @param event the triggered event
	 * @return true if the tooltip should be created
	 */
	@Override
	protected boolean shouldCreateToolTip(Event event) 
	{
		//Get the element
		Widget hoverWidget = getTipWidget(event);
		transport = getTaskListElement(hoverWidget);
		//assert valid
		if (transport != null)
			return true;
		//no valid element selected
		return false;
	}
	

	@Override
	protected Composite createToolTipContentArea(Event event, Composite parent) 
	{		
		//get the selected transport
		Composite composite = createToolTipContentAreaComposite(parent);	
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String plannedStartTime;
		String plannedTimeAtPatient;
		String term;
		
		//notifying
		if (transport.isFirebrigadeAlarming())
			firebrigade = "Bergrettung";
		if (transport.isBrkdtAlarming())
			brkdt = "Bezirksrettungskommandant";
		if (transport.isDfAlarming())
			df = "Dienstführender";
		if (transport.isEmergencyDoctorAlarming())
			emergencyDoctor = "Notarzt";
		if (transport.isHelicopterAlarming())
			helicopter = "Notarzthubschrauber";
		if (transport.isPoliceAlarming())
			police = "Polizei";
        
		Image image = ImageFactory.getInstance().getRegisteredImage("transport.directness");
		String title = transport.getFromStreet() +"/" +transport.getFromCity() +" " 
			+transport.getPatient().getLastname() +" " +transport.getPatient().getFirstname() +" "
			+transport.getToStreet() +"/" +transport.getToCity();
		addIconAndLabel(composite, image, title);
		
		if(transport.getKindOfTransport() != null)
		{
			image = ImageFactory.getInstance().getRegisteredImage("transport.qmark");
			title = transport.getKindOfTransport();
			addIconAndLabel(composite,image,title);
		}
		
		//the notes
		if(transport.hasNotes())
		{
			image = ImageFactory.getInstance().getRegisteredImage("resource.user");
			title = transport.getNotes();
			addIconAndLabel(composite,image,title);
		}
		
		//real station
		if(transport.getVehicleDetail() != null && transport.getVehicleDetail().getCurrentStation() != null)
		{
			image = ImageFactory.getInstance().getRegisteredImage("transport.stationary");
			title = transport.getVehicleDetail().getCurrentStation().getLocationName();
			addIconAndLabel(composite,image,title);
		}
		
		//planned times
		if(transport.getPlannedStartOfTransport()!= 0)
			plannedStartTime = sdf.format(transport.getPlannedStartOfTransport());
			else
				plannedStartTime = "";
		if(transport.getPlannedTimeAtPatient() != 0)
			plannedTimeAtPatient = sdf.format(transport.getPlannedTimeAtPatient());
			else 
			plannedTimeAtPatient = "";
		if(transport.getAppointmentTimeAtDestination() != 0)
			term = sdf.format(transport.getAppointmentTimeAtDestination());
		else
			term = "";
		if((!plannedStartTime.equalsIgnoreCase("") || !plannedTimeAtPatient.equalsIgnoreCase("") || !term.equalsIgnoreCase("")))
		{
			image = ImageFactory.getInstance().getRegisteredImage("transport.late");
			title = "Abfahrt: " +plannedStartTime
			+" Bei Patient: " +plannedTimeAtPatient
			+" Termin: " +term;
			addIconAndLabel(composite,image,title);
		}
		
		//feedback
		if(transport.hasFeedback())
		{
			image = ImageFactory.getInstance().getRegisteredImage("transport.feedback");
			title = transport.getFeedback();
			addIconAndLabel(composite,image,title);
		}

		
		//caller detail
		if (transport.getCallerDetail() != null)
		{
			image = ImageFactory.getInstance().getRegisteredImage("transport.callerDetail");
			title = transport.getCallerDetail().getCallerName() +" " +transport.getCallerDetail().getCallerTelephoneNumber();
			addIconAndLabel(composite,image,title);
		}
		
		//notified
		if (!(emergencyDoctor.equalsIgnoreCase("") || helicopter.equalsIgnoreCase("")|| police.equalsIgnoreCase("") || brkdt.equalsIgnoreCase("")|| df.equalsIgnoreCase("")
				|| firebrigade.equalsIgnoreCase("")))
		{
			image = ImageFactory.getInstance().getRegisteredImage("transport.exclamation");
			title = emergencyDoctor +" " +helicopter +" " +police +" " +brkdt +" " +df  +" "+firebrigade +mountainRescue;
			addIconAndLabel(composite,image,title);
		}

		return composite;
	}  
	
	protected void addIconAndLabel(Composite parent, Image image, String text) 
	{
		//check if we have something to display
		if(text.trim().isEmpty())
			return;
		
		Label imageLabel = new Label(parent, SWT.NONE);
		imageLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		imageLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		imageLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
		imageLabel.setImage(image);

		Label textLabel = new Label(parent, SWT.NONE);
		textLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		textLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		textLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		textLabel.setText(text);
	}
	
	/**
	 * Creates the tooltip content area for the tooltip
	 * @param parent the parent window
	 * @return the created composite
	 */
	protected Composite createToolTipContentAreaComposite(Composite parent) 
	{
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 2;
		composite.setLayout(gridLayout);
		composite.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		return composite;
	}
	
	/**
	 * Returns the widget source for this tooltip
	 * @param event the event triggered
	 * @return the source widget
	 */
	protected Widget getTipWidget(Event event) 
	{
		Point widgetPosition = new Point(event.x, event.y);
		Widget widget = event.widget;
		
		if (widget instanceof Table) 
		{
			Table w = (Table) widget;
			return w.getItem(widgetPosition);
		}

		return widget;
	}
	
	/**
	 * Returns the element for this tool tip
	 * @param hoverObject the object under hover
	 * @return the element under the hover
	 */
	private Transport getTaskListElement(Object hoverObject) 
	{
		if (hoverObject instanceof Widget) 
		{
			Object data = ((Widget) hoverObject).getData();
			if (data != null) 
			{
				return (Transport)data;
			}
		}
		return null;
	}
	
	/**
	 * Hides the tool tip window
	 */
	public void dispose() 
	{
		hide();
	}
}
