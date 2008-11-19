package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.platform.model.VehicleDetail;

/**
 * Creates CarComposite for the class VehiclesView, called from the CarCompositeManager
 * no actions are provided in this view (problems with the given objects and not required)
 * @author b.thek
 */
public class VehicleComposite extends Composite implements PropertyChangeListener
{
	//the parent composite
	private VehicleDetail vehicle;

	//the labels to display
	private Composite compositeCarBottom;
	private Composite compositeCarIcons;
	private Label vehicleNameLabel;
	private Label vehicleTypeLabel;
	private Label driverLabel;
	private Label medicILabel;
	private Label medicIILabel;
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

		//store the vehicle
		setData(vehicle);

		//initalize the components
		initialize();
		updateColors();

		ModelFactory.getInstance().getVehicleManager().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup
	 */
	@Override
	public void dispose()
	{
		super.dispose();
		ModelFactory.getInstance().getVehicleManager().removePropertyChangeListener(this);
	}

	/**
	 * Creates and initializes the components
	 */
	private void initialize()
	{
		//the layout for the composite
		setLayout(new FillLayout(SWT.VERTICAL));

		//top composite (name of the ambulance, type of the ambulance)
		Composite compositeCarTop = new Composite(this,SWT.NONE);
		compositeCarTop.setLayout(new FillLayout());
		
		// .. name of the ambulance
		vehicleNameLabel = new Label(compositeCarTop, SWT.NONE);
		vehicleNameLabel.setForeground(CustomColors.COLOR_LINK);
		vehicleNameLabel.setFont(CustomColors.VEHICLE_NAME);
		vehicleNameLabel.setBackground(CustomColors.COLOR_BLUE);

		// .. type of the ambulance
		vehicleTypeLabel = new Label(compositeCarTop, SWT.CENTER);
		vehicleTypeLabel.setForeground(CustomColors.COLOR_WHITE);
		vehicleTypeLabel.setFont(CustomColors.VEHICLE_TEXT);
		vehicleTypeLabel.setBackground(CustomColors.COLOR_GRAY);

		//bottom composite (icons, staff of the ambulance)
		compositeCarBottom = new Composite(this, SWT.NONE);
		compositeCarBottom.setLayout(new FillLayout(SWT.VERTICAL));

		compositeCarIcons = new Composite(compositeCarBottom, SWT.NONE);

		// .. icons
		readyLabel = new Label(compositeCarIcons, SWT.NONE);
		readyLabel.setBackground(CustomColors.COLOR_BLUE);
		readyLabel.setBounds(0, 0, 14, 19);

		mobilePhoneLabel = new Label(compositeCarIcons, SWT.NONE);
		mobilePhoneLabel.setBackground(CustomColors.COLOR_BLUE);
		mobilePhoneLabel.setBounds(20, 0, 14, 19);

		stationLabel = new Label(compositeCarIcons, SWT.NONE);
		stationLabel.setBackground(CustomColors.COLOR_BLUE);
		stationLabel.setBounds(40, 0, 14, 19);

		repairLabel = new Label(compositeCarIcons, SWT.NONE);
		repairLabel.setBackground(CustomColors.COLOR_BLUE);
		repairLabel.setBounds(60, 0, 14, 19);

		notesLabel = new Label(compositeCarIcons, SWT.NONE);
		notesLabel.setBackground(CustomColors.COLOR_BLUE);
		notesLabel.setBounds(80, 0, 14, 19);

		statusLabel = new Label(compositeCarIcons, SWT.NONE);
		statusLabel.setBackground(CustomColors.COLOR_BLUE);
		statusLabel.setBounds(115, 0, 16, 22);

		// .. staff
		final Composite compositeCarStaff = new Composite(compositeCarBottom, SWT.NONE);
		compositeCarBottom.setLayout(new FormLayout());

		driverLabel = new Label(compositeCarStaff, SWT.NONE);
		driverLabel.setForeground(CustomColors.COLOR_NAME);
		driverLabel.setFont(CustomColors.VEHICLE_TEXT);
		driverLabel.setBackground(CustomColors.COLOR_BLUE);
		driverLabel.setAlignment(SWT.LEFT);
		driverLabel.setBounds(0, 19, 44, 13);

		medicILabel = new Label(compositeCarStaff, SWT.NONE);
		medicILabel.setForeground(CustomColors.COLOR_NAME);
		medicILabel.setFont(CustomColors.VEHICLE_TEXT);
		medicILabel.setBackground(CustomColors.COLOR_BLUE);
		medicILabel.setAlignment(SWT.CENTER);
		medicILabel.setBounds(44, 19, 44, 13);

		medicIILabel = new Label(compositeCarStaff, SWT.NONE);
		medicIILabel.setForeground(CustomColors.COLOR_NAME);
		medicIILabel.setFont(CustomColors.VEHICLE_TEXT);
		medicIILabel.setBackground(CustomColors.COLOR_BLUE);
		medicIILabel.setAlignment(SWT.RIGHT);
		medicIILabel.setBounds(88, 19, 44, 13);
	}
	
	/**
	 * Updates the colors of the vehicle
	 */
	private void updateColors()
	{
		//change the background color
		if(vehicle.getDriver() == null)
		{
			compositeCarIcons.setBackground(CustomColors.COLOR_GRAY);
			vehicleNameLabel.setBackground(CustomColors.COLOR_GRAY);
			vehicleTypeLabel.setBackground(CustomColors.COLOR_GRAY);
			driverLabel.setBackground(CustomColors.COLOR_GRAY);
			medicILabel.setBackground(CustomColors.COLOR_GRAY);
			medicIILabel.setBackground(CustomColors.COLOR_GRAY);
			mobilePhoneLabel.setBackground(CustomColors.COLOR_GRAY);
			notesLabel.setBackground(CustomColors.COLOR_GRAY);
			stationLabel.setBackground(CustomColors.COLOR_GRAY);
			readyLabel.setBackground(CustomColors.COLOR_GRAY);
			repairLabel.setBackground(CustomColors.COLOR_GRAY);
			statusLabel.setBackground(CustomColors.COLOR_GRAY);
		}
		else
		{
			compositeCarIcons.setBackground(CustomColors.COLOR_BLUE);
			vehicleNameLabel.setBackground(CustomColors.COLOR_BLUE);
			vehicleTypeLabel.setBackground(CustomColors.COLOR_BLUE);
			driverLabel.setBackground(CustomColors.COLOR_BLUE);
			medicILabel.setBackground(CustomColors.COLOR_BLUE);
			medicIILabel.setBackground(CustomColors.COLOR_BLUE);
			mobilePhoneLabel.setBackground(CustomColors.COLOR_BLUE);
			notesLabel.setBackground(CustomColors.COLOR_BLUE);
			stationLabel.setBackground(CustomColors.COLOR_BLUE);
			readyLabel.setBackground(CustomColors.COLOR_BLUE);
			repairLabel.setBackground(CustomColors.COLOR_BLUE);
			statusLabel.setBackground(CustomColors.COLOR_BLUE);
		}
		//change the forground color
		if(vehicle.isOutOfOrder())
			vehicleNameLabel.setForeground(CustomColors.COLOR_WHITE);
		else
			vehicleNameLabel.setForeground(CustomColors.COLOR_LINK);
		//redraw the composite
		layout(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{        
		if("VEHICLE_UPDATE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the updated vehicle
			VehicleDetail updatedVehicle = (VehicleDetail)evt.getNewValue();
			//check the vehicle
			if(vehicle.equals(updatedVehicle))
			{
				this.vehicle = updatedVehicle;
				updateColors();
			}
		}
	}
}
