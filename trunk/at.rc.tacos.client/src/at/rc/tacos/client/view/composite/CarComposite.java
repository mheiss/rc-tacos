package at.rc.tacos.client.view.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import at.rc.tacos.client.util.Util;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.swtdesigner.SWTResourceManager;

//TODO: set ToolTips for the Components
//TODO: use Interface Transport Status for mostImportantTransportStatus - for details, please see ITransportStatus

/**
 * Creates CarComposite for the class VehiclesView, called from the CarCompositeManager
 * @author b.thek
 */
public class CarComposite extends Composite
{
	private String vehicleName;
	private String driverName;
	private String vehicleType;
	private String paramedicIName;
	private String paramedicIIName;
	
	//the image files to use
	private Image imageMobilePhone;
	private Image imageVehicleNotes;
	private Image imageCurrentStation;
	private Image imageReady;
	private Image imageOutOfOrder;
	private Image imageTransportStatus;
	
	
	/**
	 * Default constructor creating a new car composite
	 * @param parent the parent control 
	 * @param vehicle the vehicle data to use
	 */
	public CarComposite(Composite parent, VehicleDetail vehicle)
	{
	    //create the composite
		super(parent,SWT.NONE);
		setLayout(new FillLayout(SWT.VERTICAL));
		
		vehicleName = vehicle.getVehicleName();
		vehicleType = vehicle.getVehicleType();
		driverName = vehicle.getDriverName().getUserName();
		paramedicIName = vehicle.getParamedicIName().getUserName();
		paramedicIIName = vehicle.getParamedicIIName().getUserName();
		System.out.println("Paramedic I Name: " +paramedicIName);
		
		//other mobile phone than default
		if (!vehicle.getMobilePhone().getMobilePhoneId().equalsIgnoreCase(vehicle.getVehicleName()))
			imageMobilePhone = Util.getImagePath(Util.IMAGE_MOBILE_PHONE_PATH_BLACK);
		else
		    imageMobilePhone = Util.getImagePath(Util.IMAGE_MOBILE_PHONE_PATH_GREY);
		
		//show that there are notes
		if (!vehicle.getVehicleNotes().isEmpty())   
			imageVehicleNotes = Util.getImagePath(Util.IMAGE_VEHICLE_NOTES_PATH_BLACK);
		else
		    imageVehicleNotes = Util.getImagePath(Util.IMAGE_VEHICLE_NOTES_PATH_GREY);
		
		//if the basis station is different to the current station
		if (!vehicle.getBasicStation().equalsIgnoreCase(vehicle.getCurrentStation()))
			imageCurrentStation = Util.getImagePath(Util.IMAGE_CURRENT_STATION_PATH_BLACK);
		else
		    imageCurrentStation = Util.getImagePath(Util.IMAGE_CURRENT_STATION_PATH_GREY);
				
		//vehicle is ready for action
		if (vehicle.isReadyForAction())
			imageReady = Util.getImagePath(Util.IMAGE_READY_FOR_ACTION_PATH_BLACK);
		else
		    imageReady = Util.getImagePath(Util.IMAGE_READY_FOR_ACTION_PATH_GREY);
		
		//vehicle is out of order
		if (vehicle.isOutOfOrder())
			imageOutOfOrder = Util.getImagePath(Util.IMAGE_OUT_OF_ORDER_PATH_BLACK);
		else
		    imageOutOfOrder = Util.getImagePath(Util.IMAGE_OUT_OF_ORDER_PATH_GREY);
		
		//transport status
		switch(vehicle.getMostImportantTransportStatus())
		{
    		case 1: imageTransportStatus = Util.getImagePath(Util.IMAGE_TRANSPORT_STATUS_PATH_RED); break;
    		case 2: imageTransportStatus = Util.getImagePath(Util.IMAGE_TRANSPORT_STATUS_PATH_YELLOW); break;
    		case 3: imageTransportStatus = Util.getImagePath(Util.IMAGE_TRANSPORT_STATUS_PATH_GREEN); break;
    		default: imageTransportStatus = Util.getImagePath(Util.IMAGE_TRANSPORT_STATUS_PATH_RED); break;
		}		
		
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
		labelReadyForAction.setImage(imageReady);
		labelReadyForAction.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelKindOfMobilePhone = new Label(compositeCarIcons, SWT.NONE);
		labelKindOfMobilePhone.setImage(imageMobilePhone);
		labelKindOfMobilePhone.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelCurrentStation = new Label(compositeCarIcons, SWT.NONE);
		labelCurrentStation.setImage(imageCurrentStation);
		labelCurrentStation.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelOutOfOrder = new Label(compositeCarIcons, SWT.NONE);
		labelOutOfOrder.setImage(imageOutOfOrder);
		labelOutOfOrder.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelVehicleNotes = new Label(compositeCarIcons, SWT.NONE);
		labelVehicleNotes.setImage(imageVehicleNotes);
		labelVehicleNotes.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
		final Label labelMostImportantTransportStatus = new Label(compositeCarIcons, SWT.NONE);
		labelMostImportantTransportStatus.setImage(imageTransportStatus);
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
