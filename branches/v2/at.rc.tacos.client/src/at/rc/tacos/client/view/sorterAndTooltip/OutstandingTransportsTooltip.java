/** not in use at the time*/
//
//package at.rc.tacos.client.view.sorterAndTooltip;
//
//import org.eclipse.jface.window.ToolTip;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Image;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Table;
//import org.eclipse.swt.widgets.Widget;
//
//import at.rc.tacos.common.IDirectness;
//import at.rc.tacos.factory.ImageFactory;
//import at.rc.tacos.model.Transport;
//
///**
// * This shows the tool tip for the transports of the outstanding transports view
// * @author b.thek
// */
//public class OutstandingTransportsTooltip extends ToolTip implements IDirectness
//{	
//	//properties
//	private Transport transport;
//
//
//	/**
//	 * Creates a new tool tip for the outstanding transport
//	 * @param control the control for the tool tip to show
//	 */
//	public OutstandingTransportsTooltip(Control control) 
//	{
//		super(control);
//		setShift(new Point(1, 1));
//	}
//
//
//	/**
//	 * Returns whether or not the tooltip should be created.
//	 * @param event the triggered event
//	 * @return true if the tooltip should be created
//	 */
//	@Override
//	protected boolean shouldCreateToolTip(Event event) 
//	{
//		//Get the element
//		Widget hoverWidget = getTipWidget(event);
//		transport = getTaskListElement(hoverWidget);
//		//assert valid
//		if (transport != null)
//			return true;
//		//no valid element selected
//		return false;
//	}
//
//
//
//
//	@Override
//	protected Composite createToolTipContentArea(Event event, Composite parent) 
//	{		
//		//get the selected transport
//		Composite composite = createToolTipContentAreaComposite(parent);	
//
//		String alarming = "";
//		
//		if (transport.isFirebrigadeAlarming())
//			alarming = "Bergrettung";
//		if (transport.isBrkdtAlarming())
//			alarming = alarming +" Bezirksrettungskommandant";
//		if (transport.isDfAlarming())
//			alarming = alarming +" Dienstführender";
//		if (transport.isEmergencyDoctorAlarming())
//			alarming = alarming +" Notarzt";
//		if (transport.isEmergencyPhone())
//			alarming = alarming +" Rufhilfe";
//		if (transport.isHelicopterAlarming())
//			alarming = alarming +" Notarzthubschrauber";
//		if (transport.isPoliceAlarming())
//			alarming = alarming +" Polizei";
//
//		//directness
//		int direction = transport.getDirection();
//		String directness;
//		if (TOWARDS_KAPFENBERG == direction)
//		{
//			directness = "Kapfenberg";
//		}
//		else if (TOWARDS_GRAZ == direction)
//		{
//			directness = "Graz";
//		}
//		else if (TOWARDS_LEOBEN == direction)
//		{
//			directness = "Leoben";
//		}
//		else if (TOWARDS_MARIAZELL== direction)
//		{
//			directness = "Mariazell";
//		}
//		else if (TOWARDS_VIENNA == direction)
//		{
//			directness = "Wien";
//		}
//		else directness = "Bruck"; //default
//
//		Image image = ImageFactory.getInstance().getRegisteredImage("transport.directness");
//		String title = transport.getFromStreet() +"/" +transport.getFromCity() +" " 
//		+transport.getPatient().getLastname() +" " +transport.getPatient().getFirstname() +" "
//		+transport.getToStreet() +"/" +transport.getToCity();
//		addIconAndLabel(composite, image, title);
//
//		//the notes
//		if(transport.hasNotes())
//		{
//			image = ImageFactory.getInstance().getRegisteredImage("resource.user");
//			title = transport.getNotes();
//			addIconAndLabel(composite,image,title);
//		}
//
//		//directness
//		image = ImageFactory.getInstance().getRegisteredImage("transport.directness");
//		title = directness;
//		addIconAndLabel(composite,image,title);
//
//		//caller
//		if (transport.getCallerDetail() != null)
//		{
//			image = ImageFactory.getInstance().getRegisteredImage("transport.callerDetail");
//			title = transport.getCallerDetail().getCallerName() +" " +transport.getCallerDetail().getCallerTelephoneNumber();
//			addIconAndLabel(composite,image,title);
//		}
//
//		//notified
//		if (!alarming.equalsIgnoreCase(""))
//		{
//			image = ImageFactory.getInstance().getRegisteredImage("transport.exclamation");
//			title = alarming;
//			addIconAndLabel(composite,image,title);
//		}
//
//		//rufhilfe
//		if(transport.isEmergencyPhone())
//		{
//			image = ImageFactory.getInstance().getRegisteredImage("resource.phone");
//			title = "Rufhilfepatient";
//			addIconAndLabel(composite,image,title);
//		}
//
//		if(transport.isBackTransport())
//		{
//			image = ImageFactory.getInstance().getRegisteredImage("transport.backtransport");
//			title = "Rücktransport möglich";
//			addIconAndLabel(composite,image,title);
//		}
//
//		return composite;
//	}  
//
//	protected void addIconAndLabel(Composite parent, Image image, String text) 
//	{
//		Label imageLabel = new Label(parent, SWT.NONE);
//		imageLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
//		imageLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
//		imageLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
//		imageLabel.setImage(image);
//
//		Label textLabel = new Label(parent, SWT.NONE);
//		textLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
//		textLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
//		textLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
//		textLabel.setText(text);
//	}
//
//	/**
//	 * Creates the tool tip content area for the tool tip
//	 * @param parent the parent window
//	 * @return the created composite
//	 */
//	protected Composite createToolTipContentAreaComposite(Composite parent) 
//	{
//		Composite composite = new Composite(parent, SWT.NONE);
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 2;
//		gridLayout.marginWidth = 5;
//		gridLayout.marginHeight = 2;
//		composite.setLayout(gridLayout);
//		composite.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
//		return composite;
//	}
//
//	/**
//	 * Returns the widget source for this tool tip
//	 * @param event the event triggered
//	 * @return the source widget
//	 */
//	protected Widget getTipWidget(Event event) 
//	{
//		Point widgetPosition = new Point(event.x, event.y);
//		Widget widget = event.widget;
//
//		if (widget instanceof Table) 
//		{
//			Table w = (Table) widget;
//			return w.getItem(widgetPosition);
//		}
//
//		return widget;
//	}
//
//	/**
//	 * Returns the element for this tool tip
//	 * @param hoverObject the object under hover
//	 * @return the element under the hover
//	 */
//	private Transport getTaskListElement(Object hoverObject) 
//	{
//		if (hoverObject instanceof Widget) 
//		{
//			Object data = ((Widget) hoverObject).getData();
//			if (data != null) 
//			{
//				return (Transport)data;
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * Hides the tool tip window
//	 */
//	public void dispose() 
//	{
//		hide();
//	}
//}
