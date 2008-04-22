package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.controller.VehicleAtStationAction;
import at.rc.tacos.client.controller.VehicleDetachAllStaffMembersAction;
import at.rc.tacos.client.controller.VehicleEditAction;
import at.rc.tacos.client.controller.VehicleSetReadyAction;
import at.rc.tacos.client.controller.VehicleSetRepairStatus;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.VehicleDetail;

/**
 * Creates CarComposite for the class VehiclesView, called from the CarCompositeManager
 * @author b.thek
 */
public class VehicleComposite extends Composite implements PropertyChangeListener
{
	//the parent composite
	private VehicleDetail vehicle;

	//the labels to display
	private Composite compositeCarType;
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

	//the actions
	private VehicleEditAction editAction;
	private VehicleDetachAllStaffMembersAction detachAction;
	private VehicleSetReadyAction readyStatus;
	private VehicleSetRepairStatus repairStatus;
	private VehicleAtStationAction vehicleAtStationAction;

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

		vehicle.updateImages();
		bindValues();
		updateColors();

		//context menue
		makeActions();
		hookContextMenu();

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
		vehicleNameLabel.setForeground(CustomColors.COLOR_DARK);
		vehicleNameLabel.setFont(CustomColors.VEHICLE_NAME);
		vehicleNameLabel.setBackground(CustomColors.COLOR_BLUE);

		// .. type of the ambulance
		compositeCarType = new Composite(compositeCarTop, SWT.NONE);
		compositeCarType.setLayout(new FormLayout());
		compositeCarType.setBackground(CustomColors.COLOR_BLUE);

		vehicleTypeLabel = new Label(compositeCarType, SWT.CENTER);

		final FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(0, 15);
		fd_label.top = new FormAttachment(0, 0);
		fd_label.right = new FormAttachment(0, 68);
		fd_label.left = new FormAttachment(0, 15);
		vehicleTypeLabel.setLayoutData(fd_label);
		vehicleTypeLabel.setForeground(CustomColors.COLOR_WHITE);
		vehicleTypeLabel.setFont(CustomColors.VEHICLE_TEXT);
		vehicleTypeLabel.setBackground(CustomColors.COLOR_GRAY);

		//bottom composite (icons, staff of the ambulance)
		compositeCarBottom = new Composite(this, SWT.NONE);
		compositeCarBottom.setLayout(new FillLayout(SWT.VERTICAL));

		compositeCarIcons = new Composite(compositeCarBottom, SWT.NONE);
//		compositeCarIcons.setLayout(new FormLayout());

		// .. icons
		readyLabel = new Label(compositeCarIcons, SWT.NONE);
		readyLabel.setBackground(CustomColors.COLOR_BLUE);
		readyLabel.setBounds(0, 0, 19, 19);

		mobilePhoneLabel = new Label(compositeCarIcons, SWT.NONE);
		mobilePhoneLabel.setBackground(CustomColors.COLOR_BLUE);
		mobilePhoneLabel.setBounds(29, 0, 19, 19);

		stationLabel = new Label(compositeCarIcons, SWT.NONE);
		stationLabel.setBackground(CustomColors.COLOR_BLUE);
		stationLabel.setBounds(54, 0, 19, 19);

		repairLabel = new Label(compositeCarIcons, SWT.NONE);
		repairLabel.setBackground(CustomColors.COLOR_BLUE);
		repairLabel.setBounds(83, 0, 19, 19);

		notesLabel = new Label(compositeCarIcons, SWT.NONE);
		notesLabel.setBackground(CustomColors.COLOR_BLUE);
		notesLabel.setBounds(112, 0, 19, 19);

		statusLabel = new Label(compositeCarIcons, SWT.NONE);
		statusLabel.setBackground(CustomColors.COLOR_BLUE);
		statusLabel.setBounds(141, 0, 21, 19);

		// .. staff
		final Composite compositeCarStaff = new Composite(compositeCarBottom, SWT.NONE);
		compositeCarBottom.setLayout(new FormLayout());

		driverLabel = new Label(compositeCarStaff, SWT.NONE);
		driverLabel.setForeground(CustomColors.COLOR_NAME);
		driverLabel.setFont(CustomColors.VEHICLE_TEXT);
		driverLabel.setBackground(CustomColors.COLOR_BLUE);
		driverLabel.setAlignment(SWT.LEFT);
		driverLabel.setBounds(0, 19, 54, 19);

		medicILabel = new Label(compositeCarStaff, SWT.NONE);
		medicILabel.setForeground(CustomColors.COLOR_NAME);
		medicILabel.setFont(CustomColors.VEHICLE_TEXT);
		medicILabel.setBackground(CustomColors.COLOR_BLUE);
		medicILabel.setAlignment(SWT.CENTER);
		medicILabel.setBounds(54, 19, 54, 19);

		medicIILabel = new Label(compositeCarStaff, SWT.NONE);
		medicIILabel.setForeground(CustomColors.COLOR_NAME);
		medicIILabel.setFont(CustomColors.VEHICLE_TEXT);
		medicIILabel.setBackground(CustomColors.COLOR_BLUE);
		medicIILabel.setAlignment(SWT.RIGHT);
		medicIILabel.setBounds(108, 19, 54, 19);

		//create the actions
		makeActions();
		hookContextMenu();
	}

	/**
	 * Creats and initializes all actions
	 */
	private void makeActions()
	{
		editAction = new VehicleEditAction(vehicle);
		detachAction = new VehicleDetachAllStaffMembersAction(vehicle);
		readyStatus = new VehicleSetReadyAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(),vehicle);
		repairStatus = new VehicleSetRepairStatus(PlatformUI.getWorkbench().getActiveWorkbenchWindow(),vehicle);
		vehicleAtStationAction = new VehicleAtStationAction(vehicle);
	}

	/**
	 * Creates and hooks the context menue
	 */
	private void hookContextMenu()
	{
		MenuManager menuManager = new MenuManager("#PopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() 
		{
			public void menuAboutToShow(IMenuManager manager) 
			{
				fillContextMenu(manager);
			}
		});
		Menu menu = menuManager.createContextMenu(vehicleNameLabel);
		vehicleNameLabel.setMenu(menu);
	}

	private void fillContextMenu(IMenuManager manager)
	{
		manager.add(editAction);
		manager.add(detachAction);
		manager.add(new Separator());
		manager.add(vehicleAtStationAction);
		manager.add(new Separator());
		manager.add(readyStatus);
		manager.add(repairStatus);

		//enable or disable the actions
		if(vehicle.isReadyForAction())
			readyStatus.setEnabled(false);
		else
			readyStatus.setEnabled(true);
		if(vehicle.isOutOfOrder())
			repairStatus.setEnabled(false);
		else 
			repairStatus.setEnabled(true);
	}

	/**
	 * Binds the values form the model to the labels and text fields.
	 */
	private void bindValues()
	{
		//create a new databinding context
		DataBindingContext bindingContext = new DataBindingContext();

		//bind the name of the vehicle
		bindingContext.bindValue(
				SWTObservables.observeText(vehicleNameLabel), 
				BeansObservables.observeValue(vehicle, "vehicleName"), null, null);
		//bind the type of the vehicle
		bindingContext.bindValue(
				SWTObservables.observeText(vehicleTypeLabel), 
				BeansObservables.observeValue(vehicle, "vehicleType"), null, null);
		//bind the name of the driver
		if(vehicle.getDriver() != null)
		{
			bindingContext.bindValue(
					SWTObservables.observeText(driverLabel), 
					BeansObservables.observeValue(vehicle.getDriver(), "userName"), null, null);
		}
		else
			driverLabel.setText("");
		//bind the name of the medic
		if(vehicle.getFirstParamedic() != null)
		{
			bindingContext.bindValue(
					SWTObservables.observeText(medicILabel), 
					BeansObservables.observeValue(vehicle.getFirstParamedic(), "userName"), null, null);
		}
		else
			medicILabel.setText("");
		//bind the name of the second medic
		if(vehicle.getSecondParamedic() != null)
		{
			bindingContext.bindValue(
					SWTObservables.observeText(medicIILabel), 
					BeansObservables.observeValue(vehicle.getSecondParamedic(), "userName"), null, null);
		}
		else
			medicIILabel.setText("");
		//bind the notes image label
		bindingContext.bindValue(
				new MyImageLabelObserver(notesLabel), 
				BeansObservables.observeValue(vehicle, "vehicleNotesImage"), null, null);
		//bind the images
		bindingContext.bindValue(
				new MyImageLabelObserver(mobilePhoneLabel), 
				BeansObservables.observeValue(vehicle, "mobilePhoneImage"), null, null);
		//bind the images
		bindingContext.bindValue(
				new MyImageLabelObserver(stationLabel), 
				BeansObservables.observeValue(vehicle, "stationImage"), null, null);
		//bind the images
		bindingContext.bindValue(
				new MyImageLabelObserver(readyLabel), 
				BeansObservables.observeValue(vehicle, "readyForActionImage"), null, null);
		//bind the images
		bindingContext.bindValue(
				new MyImageLabelObserver(repairLabel), 
				BeansObservables.observeValue(vehicle, "outOfOrderImage"), null, null);
		//bind the images
		bindingContext.bindValue(
				new MyImageLabelObserver(statusLabel), 
				BeansObservables.observeValue(vehicle, "transportStatusImage"), null, null);
	}
	
	/**
	 * Updates the colors of the vehicle
	 */
	private void updateColors()
	{
		//change the background color
		if(vehicle.getDriver() == null)
		{
			compositeCarType.setBackground(CustomColors.COLOR_GRAY);
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
			compositeCarType.setBackground(CustomColors.COLOR_BLUE);
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
			vehicleNameLabel.setForeground(CustomColors.COLOR_DARK);
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
				vehicle.updateImages();
				bindValues();  
				updateColors();
			}
		}
	}
}
