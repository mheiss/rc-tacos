package at.rc.tacos.client.view;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.swtdesigner.SWTResourceManager;

//TODO: set ToolTips for the Components
/**
 * Creates CarComposite for the class VehiclesView, called from the CarCompositeManager
 * @author b.thek
 */
public class CarComposite extends Composite
{
	private String vehicleName;
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
		
//		//the values to show
		vehicleName = vehicle.getVehicleName();
		vehicleType = vehicle.getVehicleType();
		paramedicIName = vehicle.getParamedicIName().getUserName();
		paramedicIIName = vehicle.getParamedicIIName().getUserName();
//		//the images to visualize the status
		imageMobilePhone = vehicle.getMobilePhoneImage();
		imageVehicleNotes = vehicle.getVehicleNotesImage();
		imageCurrentStation = vehicle.getStationImage();
		imageReady = vehicle.getReadyForActionImage();
		imageOutOfOrder = vehicle.getOutOfOrderImage();
		imageTransportStatus = vehicle.getTransportStatusImage();
		
		//top composite (name of the ambulance, type of the ambulance)
		final Composite compositeCarTop = new Composite(this,parent.getStyle());
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
	
		final Text labelDriverName = new Text(compositeCarStaff, SWT.NONE);
		labelDriverName.setForeground(SWTResourceManager.getColor(0, 0, 102));
		labelDriverName.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		labelDriverName.setBackground(SWTResourceManager.getColor(209, 229, 249));
	
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
		
		// Bind it
        DataBindingContext bindingContext = new DataBindingContext();
        bindingContext.bindValue(
                SWTObservables.observeText(labelDriverName,SWT.None), 
                BeansObservables.observeValue(vehicle.getDriverName(), "userName"),
                null, null);
	}
}
