package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import at.rc.tacos.client.controller.VehicleEditAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.util.Util;
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
       	//update the images to display
    	vehicle.updateImages();
		
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
		final Composite compositeCarTop = new Composite(this,SWT.NONE);
		compositeCarTop.setLayout(new FillLayout());
		// .. name of the ambulance
		vehicleNameLabel = new Label(compositeCarTop, SWT.NONE);
		vehicleNameLabel.setForeground(Util.getColor(0, 0, 128));
		vehicleNameLabel.setFont(new Font(null,"Arial", 18, SWT.BOLD));
		vehicleNameLabel.setBackground(Util.getColor(209, 229, 249));

		// .. type of the ambulance
		final Composite compositeCarType = new Composite(compositeCarTop, SWT.NONE);
		compositeCarType.setLayout(new FormLayout());
		compositeCarType.setBackground(Util.getColor(209, 229, 249));

		vehicleTypeLabel = new Label(compositeCarType, SWT.CENTER);

		final FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(0, 15);
		fd_label.top = new FormAttachment(0, 0);
		fd_label.right = new FormAttachment(0, 68);
		fd_label.left = new FormAttachment(0, 15);
		vehicleTypeLabel.setLayoutData(fd_label);
		vehicleTypeLabel.setForeground(Util.getColor(255, 255, 255));
		vehicleTypeLabel.setFont(new Font(null,"Arial", 10, SWT.BOLD));
		vehicleTypeLabel.setBackground(Util.getColor(228, 236, 238));

		//bottom composite (icons, staff of the ambulance)
		final Composite compositeCarBottom = new Composite(this, SWT.NONE);
		compositeCarBottom.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite compositeCarIcons = new Composite(compositeCarBottom, SWT.NONE);
		compositeCarIcons.setLayout(new FillLayout());

		// .. icons
		readyLabel = new Label(compositeCarIcons, SWT.NONE);
		readyLabel.setBackground(Util.getColor(209, 229, 249));

		mobilePhoneLabel = new Label(compositeCarIcons, SWT.NONE);
		mobilePhoneLabel.setBackground(Util.getColor(209, 229, 249));

		stationLabel = new Label(compositeCarIcons, SWT.NONE);
		stationLabel.setBackground(Util.getColor(209, 229, 249));

		repairLabel = new Label(compositeCarIcons, SWT.NONE);
		repairLabel.setBackground(Util.getColor(209, 229, 249));

		notesLabel = new Label(compositeCarIcons, SWT.NONE);
		notesLabel.setBackground(Util.getColor(209, 229, 249));

		statusLabel = new Label(compositeCarIcons, SWT.NONE);
		statusLabel.setBackground(Util.getColor(209, 229, 249));

		// .. staff
		final Composite compositeCarStaff = new Composite(compositeCarBottom, SWT.NONE);
		compositeCarStaff.setLayout(new FillLayout());

		driverLabel = new Label(compositeCarStaff, SWT.NONE);
		driverLabel.setForeground(Util.getColor(0, 0, 102));
		driverLabel.setFont(new Font(null,"Arial", 8, SWT.NONE));
		driverLabel.setBackground(Util.getColor(209, 229, 249));

		medicILabel = new Label(compositeCarStaff, SWT.NONE);
		medicILabel.setForeground(Util.getColor(0, 0, 102));
		medicILabel.setFont(new Font(null,"Arial", 8, SWT.NONE));
		medicILabel.setBackground(Util.getColor(209, 229, 249));

		medicIILabel = new Label(compositeCarStaff, SWT.NONE);
		medicIILabel.setForeground(Util.getColor(0, 0, 102));
		medicIILabel.setFont(new Font(null,"Arial", 8, SWT.NONE));
		medicIILabel.setBackground(Util.getColor(209, 229, 249));

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
		bindingContext.bindValue(
				SWTObservables.observeText(driverLabel), 
				BeansObservables.observeValue(vehicle.getDriverName(), "userName"), null, null);
		//bind the name of the medic
		bindingContext.bindValue(
				SWTObservables.observeText(medicILabel), 
				BeansObservables.observeValue(vehicle.getParamedicIName(), "userName"), null, null);
		//bind the name of the second medic
		bindingContext.bindValue(
				SWTObservables.observeText(medicIILabel), 
				BeansObservables.observeValue(vehicle.getParamedicIIName(), "userName"), null, null);

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

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
	    //update the bindung
        bindValues();
        
		if("VEHICLE_UPDATE".equalsIgnoreCase(evt.getPropertyName()))
		{
			if(vehicle.isOutOfOrder())
			{
				System.out.println("update");
				setBackground(CustomColors.GREY_COLOR);
			}
		}
		update();
		redraw();
		getDisplay().update();
		layout(true);
	}
}
