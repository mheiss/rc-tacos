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
public class VehicleComposite extends Composite
{
	//the parent composite
	private VehicleDetail vehicle;

	//the labels to display
	private Label vehicleNameLabel;
	private Label vehicleTypeLabel;
	private Text driverLabel;
	private Text medicILabel;
	private Text medicIILabel;
	private Label mobilePhoneLabel;
	private Label notesLabel;
	private Label stationLabel;
	private Label readyLabel;
	private Label repairLabel;
	private Label statusLabel;

	/**
	 * Default constructor creating a new car composite
	 * @param parent the parent control 
	 * @param vehicle the vehicle data to use
	 */
	public VehicleComposite(Composite parent, VehicleDetail vehicle)
	{
		//create the composite
		super(parent,SWT.NONE);
		this.vehicle = vehicle;

		//initalize the components
		initialize();
		//databinding
		bindValues();
	}

	/**
	 * Creates and initializes the components
	 */
	private void initialize()
	{
		//the layout for the composite
		setLayout(new FillLayout(SWT.VERTICAL));

		//the values to show
		String vehicleName = vehicle.getVehicleName();
		String vehicleType = vehicle.getVehicleType();
		//the images to visualize the status
		Image imageMobilePhone = vehicle.getMobilePhoneImage();
		Image imageVehicleNotes = vehicle.getVehicleNotesImage();
		Image imageCurrentStation = vehicle.getStationImage();
		Image imageReady = vehicle.getReadyForActionImage();
		Image imageOutOfOrder = vehicle.getOutOfOrderImage();
		Image imageTransportStatus = vehicle.getTransportStatusImage();

		//top composite (name of the ambulance, type of the ambulance)
		final Composite compositeCarTop = new Composite(this,SWT.NONE);
		compositeCarTop.setLayout(new FillLayout());
		// .. name of the ambulance
		vehicleNameLabel = new Label(compositeCarTop, SWT.NONE);
		vehicleNameLabel.setForeground(SWTResourceManager.getColor(0, 0, 128));
		vehicleNameLabel.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		vehicleNameLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));
		vehicleNameLabel.setText(vehicleName);

		// .. type of the ambulance
		final Composite compositeCarType = new Composite(compositeCarTop, SWT.NONE);
		compositeCarType.setLayout(new FormLayout());
		compositeCarType.setBackground(SWTResourceManager.getColor(209, 229, 249));

		vehicleTypeLabel = new Label(compositeCarType, SWT.CENTER);

		final FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(0, 15);
		fd_label.top = new FormAttachment(0, 0);
		fd_label.right = new FormAttachment(0, 68);
		fd_label.left = new FormAttachment(0, 15);
		vehicleTypeLabel.setLayoutData(fd_label);
		vehicleTypeLabel.setForeground(SWTResourceManager.getColor(255, 255, 255));
		vehicleTypeLabel.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		vehicleTypeLabel.setBackground(SWTResourceManager.getColor(228, 236, 238));
		vehicleTypeLabel.setText(vehicleType);

		//bottom composite (icons, staff of the ambulance)
		final Composite compositeCarBottom = new Composite(this, SWT.NONE);
		compositeCarBottom.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite compositeCarIcons = new Composite(compositeCarBottom, SWT.NONE);
		compositeCarIcons.setLayout(new FillLayout());

		// .. icons
		readyLabel = new Label(compositeCarIcons, SWT.NONE);
		readyLabel.setImage(imageReady);
		readyLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));

		mobilePhoneLabel = new Label(compositeCarIcons, SWT.NONE);
		mobilePhoneLabel.setImage(imageMobilePhone);
		mobilePhoneLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));

		stationLabel = new Label(compositeCarIcons, SWT.NONE);
		stationLabel.setImage(imageCurrentStation);
		stationLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));

		repairLabel = new Label(compositeCarIcons, SWT.NONE);
		repairLabel.setImage(imageOutOfOrder);
		repairLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));

		notesLabel = new Label(compositeCarIcons, SWT.NONE);
		notesLabel.setImage(imageVehicleNotes);
		notesLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));

		statusLabel = new Label(compositeCarIcons, SWT.NONE);
		statusLabel.setImage(imageTransportStatus);
		statusLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));

		// .. staff
		final Composite compositeCarStaff = new Composite(compositeCarBottom, SWT.NONE);
		compositeCarStaff.setLayout(new FillLayout());

		driverLabel = new Text(compositeCarStaff, SWT.NONE);
		driverLabel.setForeground(SWTResourceManager.getColor(0, 0, 102));
		driverLabel.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		driverLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));

		medicILabel = new Text(compositeCarStaff, SWT.NONE);
		medicILabel.setForeground(SWTResourceManager.getColor(0, 0, 102));
		medicILabel.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		medicILabel.setBackground(SWTResourceManager.getColor(209, 229, 249));

		medicIILabel = new Text(compositeCarStaff, SWT.NONE);
		medicIILabel.setForeground(SWTResourceManager.getColor(0, 0, 102));
		medicIILabel.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		medicIILabel.setBackground(SWTResourceManager.getColor(209, 229, 249));
	}

	/**
	 * Binds the values form the model to the labels and text fields.
	 */
	public void bindValues()
	{
		//create a new databinding context
		DataBindingContext bindingContext = new DataBindingContext();
		
		//observes and binds the driver name 
        bindingContext.bindValue(
        		SWTObservables.observeText(driverLabel, SWT.FocusOut), 
        		BeansObservables.observeValue(vehicle.getDriverName(), "userName"), null, null);
		//observes and binds the medic name 
        bindingContext.bindValue(
        		SWTObservables.observeText(medicILabel, SWT.FocusOut), 
        		BeansObservables.observeValue(vehicle.getParamedicIName(), "userName"), null, null);
		//observes and binds the medic2 name 
        bindingContext.bindValue(
        		SWTObservables.observeText(medicIILabel, SWT.FocusOut), 
        		BeansObservables.observeValue(vehicle.getParamedicIIName(), "userName"), null, null);
	}
}
