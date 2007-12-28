
package at.rc.tacos.client.view;

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
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

/**
 * This shows the tool tip for the transports of the outstanding transports view
 * @author b.thek
 */
public class OutstandingTransportsTooltip extends ToolTip 
{	
	
	private String backtransport = "";
	private String police = "";
	private String firebrigade = "";
	private String brkdt = "";
	private String df = "";
	private String emergencyDoctor = "";
	private String helicopter= "";
	private String mountainRescue = "";
	private String emergencyPhone = "";
	/**
	 * Creates a new tool tip for the outstanding transport
	 * @param control the control for the tool tip to show
	 */
	public OutstandingTransportsTooltip(Control control) 
	{
		super(control);
		setShift(new Point(1, 1));
	}
	
	/**
	 * Returns whether or not the tool tip should be created.
	 * @param event the triggered event
	 * @return true if the tool tip should be created
	 */
	@Override
	protected boolean shouldCreateToolTip(Event event) 
	{
		return true;
	}

	@Override
	protected Composite createToolTipContentArea(Event event, Composite parent) 
	{		
		//get the selected transport
		Composite composite = createToolTipContentAreaComposite(parent);	
		Widget hoverWidget = getTipWidget(event);
		Transport transport = getTaskListElement(hoverWidget);
		
		//notifying
		if (transport.isBackTransport())
			backtransport = "Rücktransport";
		if (transport.isFirebrigadeAlarming())
			firebrigade = "Bergrettung";
		if (transport.isBrkdtAlarming())
			brkdt = "Bezirksrettungskommandant";
		if (transport.isDfAlarming())
			df = "Dienstführender";
		if (transport.isEmergencyDoctorAlarming())
			emergencyDoctor = "Notarzt";
		if (transport.isEmergencyPhone())
			emergencyPhone = "Rufhilfe";
		if (transport.isHelicopterAlarming())
			helicopter = "Notarzthubschrauber";
		if (transport.isPoliceAlarming())
			police = "Polizei";
			
		Image image = ImageFactory.getInstance().getRegisteredImage("toolbar.transportShort");
		String title = transport.getFromStreet() +" " +transport.getFromCity() +" " 
			+transport.getPatient().getLastname() +" " +transport.getPatient().getFirstname() +" "
//			+transport.getDirection() +" " +transport.getCallerDetail().getCallerName() +" " +transport.getCallerDetail().getCallerTelephoneNumber() +" "
			+backtransport +" " +backtransport +" " +emergencyDoctor +" " +helicopter +" " +police +" " +brkdt +" " +df +" " +emergencyPhone +" "
			+firebrigade;
		addIconAndLabel(composite, image, title);
		//the notes
		if(transport.hasNotes())
		{
			image = ImageFactory.getInstance().getRegisteredImage("image.personal.info");
			title = transport.getDiseaseNotes();
			addIconAndLabel(composite,image,title);
		}
		if(transport.hasFeedback())
		{
			image = ImageFactory.getInstance().getRegisteredImage("image.personal.info");
			title = transport.getFeedback();
			addIconAndLabel(composite,image,title);
		}

		return composite;
	}  
	
	protected void addIconAndLabel(Composite parent, Image image, String text) 
	{
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
	 * Creates the tool tip content area for the tool tip
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
	 * Returns the widget source for this tool tip
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
