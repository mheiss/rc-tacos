package at.rc.tacos.client.view.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import at.rc.tacos.client.view.VehiclesView;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.swtdesigner.SWTResourceManager;

/**
 * Creates CarComposite for the class VehiclesView, called from the CarCompositeManager
 * @author b.thek
 *
 */

//TODO: set ToolTips for the Components
//TODO: use Interface Transport Status for mostImportantTransportStatus - for details, please see ITransportStatus


public class CarComposite extends Composite
{
	private int mostImportantTransportStatus;
	private String vehicleName;
	private String driverName;
	private String vehicleType;
	private String paramedicIName;
	private String paramedicIIName;

	
	//paths to the grey pictures that means there is no information available
	final String PICTURE_MOBILE_PHONE_PATH_GREY = "/image/Handy.gif";
	final String PICTURE_VEHICLE_NOTES_PATH_GREY = "/image/TXT.gif";
	final String PICTURE_CURRENT_STATION_PATH_GREY = "/image/Haus.gif";
	final String PICTURE_READY_FOR_ACTION_PATH_GREY = "/image/OK.gif";
	final String PICTURE_OUT_OF_ORDER_PATH_GREY = "/image/Reparatur.gif";
	
	//paths to the black pictures that means there is some information available
	final String PICTURE_MOBILE_PHONE_PATH_BLACK = "/image/Handy2.gif";
	final String PICTURE_VEHICLE_NOTES_PATH_BLACK = "/image/TXT2.gif";
	final String PICTURE_CURRENT_STATION_PATH_BLACK = "/image/Haus2.gif";
	final String PICTURE_READY_FOR_ACTION_PATH_BLACK = "/image/OK2.gif";
	final String PICTURE_OUT_OF_ORDER_PATH_BLACK = "/image/Reparatur2.gif";
	
	//paths to the light status
	final String PICTURE_MOST_IMPORTANT_TRANSPORT_STATUS_PATH_RED = "/image/Ampel_rot.gif";
	final String PICTURE_MOST_IMPORTANT_TRANSPORT_STATUS_PATH_YELLOW = "/image/Ampel_gelb.gif";
	final String PICTURE_MOST_IMPORTANT_TRANSPORT_STATUS_PATH_GREEN = "/image/Ampel_grün.gif";
	final String PICTURE_MOST_IMPORTANT_TRANSPORT_STATUS_PATH_GREY = "/image/Ampel.gif";
	
	//default paths (no information available)
	private String picture_mobile_phone_path = PICTURE_MOBILE_PHONE_PATH_GREY;
	private String picture_vehicle_notes_path = PICTURE_VEHICLE_NOTES_PATH_GREY;
	private String picture_current_station_path = PICTURE_CURRENT_STATION_PATH_GREY;
	private String picture_ready_for_action_path = PICTURE_READY_FOR_ACTION_PATH_GREY;
	private String picture_out_of_order_path = PICTURE_OUT_OF_ORDER_PATH_GREY;
	private String picture_most_importang_transport_status_path =PICTURE_MOST_IMPORTANT_TRANSPORT_STATUS_PATH_GREY;
	
	
	
	/**
	 * Constructor
	 * @param composite
	 * @param vehicle
	 */
	public CarComposite(Composite composite, VehicleDetail vehicle)
	{
		super(composite,SWT.NONE);
		
		this.vehicleName = vehicle.getVehicleName();
		this.vehicleType = vehicle.getVehicleType();
		this.driverName = vehicle.getDriverName().getUserName();
		this.paramedicIName = vehicle.getParamedicIName().getUserName();
		this.paramedicIIName = vehicle.getParamedicIIName().getUserName();
		System.out.println("Paramedic I Name: " +paramedicIName);
		
		//assign the right picture
		if (!vehicle.getMobilePhone().getMobilePhoneId().equalsIgnoreCase(vehicle.getVehicleName()))
		{
			picture_mobile_phone_path = PICTURE_MOBILE_PHONE_PATH_BLACK;//other mobile phone than default
		}
		
		if (!vehicle.getVehicleNotes().isEmpty())
		{
			picture_vehicle_notes_path = PICTURE_VEHICLE_NOTES_PATH_BLACK;//show that there are notes
		}
		
		if (!vehicle.getBasicStation().equalsIgnoreCase(vehicle.getCurrentStation()))
		{
			picture_current_station_path = PICTURE_CURRENT_STATION_PATH_BLACK;//if the basis station is different to the current station
		}
				
		if (vehicle.isReadyForAction())
		{
			picture_ready_for_action_path = PICTURE_READY_FOR_ACTION_PATH_BLACK;
		}
		
		if (vehicle.isOutOfOrder())
		{
			picture_out_of_order_path = PICTURE_OUT_OF_ORDER_PATH_BLACK;
		}
		
		this.mostImportantTransportStatus=vehicle.getMostImportantTransportStatus();
		if (mostImportantTransportStatus == 1)
		{
			picture_most_importang_transport_status_path = PICTURE_MOST_IMPORTANT_TRANSPORT_STATUS_PATH_RED;
		}
		else if (mostImportantTransportStatus == 2)
		{
			picture_most_importang_transport_status_path = PICTURE_MOST_IMPORTANT_TRANSPORT_STATUS_PATH_YELLOW;
		}
		else if (mostImportantTransportStatus == 3)
		{
			picture_most_importang_transport_status_path = PICTURE_MOST_IMPORTANT_TRANSPORT_STATUS_PATH_GREEN;
		}
		
		
		//create the car composite
		final RowData rd_compositeACar = new RowData();
		rd_compositeACar.width = 136;
		rd_compositeACar.height = 61;
		
		this.setLayoutData(rd_compositeACar);
		this.setLayout(new FillLayout(SWT.VERTICAL));
		
		//top composite (name of the ambulance, type of the ambulance)
		final Composite compositeCarTop = new Composite(this, SWT.NONE);
		compositeCarTop.setLayout(new FillLayout());
	
		// .. name of the ambulance
		final Label labelAmbulanceName = new Label(compositeCarTop, SWT.NONE);
		labelAmbulanceName.setForeground(SWTResourceManager.getColor(0, 0, 128));
		labelAmbulanceName.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		labelAmbulanceName.setBackground(SWTResourceManager.getColor(209, 229, 249));
		labelAmbulanceName.setText(vehicleName);
	
		// .. type of the ambulance
		final Composite compositeCarType = new Composite(compositeCarTop, SWT.NONE);
		compositeCarType.setLayout(new FormLayout());
		compositeCarType.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelAmbulanceType = new Label(compositeCarType, SWT.CENTER);
		final FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(0, 15);
		fd_label.top = new FormAttachment(0, 0);
		fd_label.right = new FormAttachment(0, 68);
		fd_label.left = new FormAttachment(0, 15);
		labelAmbulanceType.setLayoutData(fd_label);
		labelAmbulanceType.setForeground(SWTResourceManager.getColor(255, 255, 255));
		labelAmbulanceType.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		labelAmbulanceType.setBackground(SWTResourceManager.getColor(228, 236, 238));
		labelAmbulanceType.setText(vehicleType);
	
		
		//bottom composite (icons, staff of the ambulance)
		final Composite compositeCarBottom = new Composite(this, SWT.NONE);
		compositeCarBottom.setLayout(new FillLayout(SWT.VERTICAL));
	
		final Composite compositeCarIcons = new Composite(compositeCarBottom, SWT.NONE);
		compositeCarIcons.setLayout(new FillLayout());
	
		// .. icons
		final Label labelReadyForAction = new Label(compositeCarIcons, SWT.NONE);
		labelReadyForAction.setImage(SWTResourceManager.getImage(VehiclesView.class, picture_ready_for_action_path));
		labelReadyForAction.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelKindOfMobilePhone = new Label(compositeCarIcons, SWT.NONE);
		labelKindOfMobilePhone.setImage(SWTResourceManager.getImage(VehiclesView.class, picture_mobile_phone_path));
		labelKindOfMobilePhone.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelCurrentStation = new Label(compositeCarIcons, SWT.NONE);
		labelCurrentStation.setImage(SWTResourceManager.getImage(VehiclesView.class, picture_current_station_path));
		labelCurrentStation.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelOutOfOrder = new Label(compositeCarIcons, SWT.NONE);
		labelOutOfOrder.setImage(SWTResourceManager.getImage(VehiclesView.class, picture_out_of_order_path));
		labelOutOfOrder.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelVehicleNotes = new Label(compositeCarIcons, SWT.NONE);
		labelVehicleNotes.setImage(SWTResourceManager.getImage(VehiclesView.class, picture_vehicle_notes_path));
		labelVehicleNotes.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelMostImportantTransportStatus = new Label(compositeCarIcons, SWT.NONE);
		labelMostImportantTransportStatus.setImage(SWTResourceManager.getImage(VehiclesView.class, picture_most_importang_transport_status_path));
		labelMostImportantTransportStatus.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		// .. staff
		final Composite compositeCarStaff = new Composite(compositeCarBottom, SWT.NONE);
		compositeCarStaff.setLayout(new FillLayout());
	
		final Label labelDriverName = new Label(compositeCarStaff, SWT.NONE);
		labelDriverName.setForeground(SWTResourceManager.getColor(0, 0, 102));
		labelDriverName.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		labelDriverName.setBackground(SWTResourceManager.getColor(209, 229, 249));
		labelDriverName.setText(this.driverName);
	
		final Label labelParamedicIName = new Label(compositeCarStaff, SWT.NONE);
		labelParamedicIName.setForeground(SWTResourceManager.getColor(0, 0, 102));
		labelParamedicIName.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		labelParamedicIName.setBackground(SWTResourceManager.getColor(209, 229, 249));
		labelParamedicIName.setText(this.paramedicIName);
	
		final Label labelParamedicIIName = new Label(compositeCarStaff, SWT.NONE);
		labelParamedicIIName.setForeground(SWTResourceManager.getColor(0, 0, 102));
		labelParamedicIIName.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		labelParamedicIIName.setBackground(SWTResourceManager.getColor(209, 229, 249));
		labelParamedicIIName.setText(this.paramedicIIName);		
	}
	
}
