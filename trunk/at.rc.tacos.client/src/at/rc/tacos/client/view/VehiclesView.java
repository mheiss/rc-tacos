package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.part.*;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.controller.RefreshViewAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.VehicleDetail;

/**
 * GUI to get an overview about the ambulances
 * create CarComposite's using the CarCompositeManager
 * @author b.thek
 */
public class VehiclesView extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.view.ressources_view";

	//the toolkit to use
	private FormToolkit toolkit;
	private ScrolledForm form;

	//the list with the sections
	private List<Section> sectionList;

	/**
	 * Default class constructor
	 */
	public VehiclesView()
	{
		ModelFactory.getInstance().getLocationList().addPropertyChangeListener(this);
		ModelFactory.getInstance().getVehicleList().addPropertyChangeListener(this);
		//create the list for the sections
		sectionList = new ArrayList<Section>();
	}

	/**
	 * Cleanup and remove the listener
	 */
	@Override 
	public void dispose()
	{
		ModelFactory.getInstance().getLocationList().removePropertyChangeListener(this);
		ModelFactory.getInstance().getVehicleList().removePropertyChangeListener(this);
	}

	/**
	 * Create contents of the window.
	 * @param parent the parent frame to insert the controlls
	 */
	@Override
	public void createPartControl(Composite parent) 
	{
		// Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Überblick über die Fahrzeuge des Bezirkes");
		toolkit.decorateFormHeading(form.getForm());
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		form.getBody().setLayout(layout);
		//create and hook the toolbar action
		createToolBarActions();
	}

	@Override
	public void setFocus() { }

	/**
	 * Notification that the data model has changed so the view have to be updated.
	 * @param evt the fired property event
	 */
	public void propertyChange(final PropertyChangeEvent evt) 
	{
		if("LOCATION_ADD".equalsIgnoreCase(evt.getPropertyName()))
		{
			//add the new location to the list
			Location newLocation = (Location)evt.getNewValue();
			//create the location
			Section section = createSection(form,toolkit,newLocation.getLocationName(),"Fahzeuge von "+newLocation.getLocationName());
			//store the location
			section.setData(newLocation);
			sectionList.add(section);           
		}
		//remove all locations
		if("LOCATION_CLEARED".equalsIgnoreCase(evt.getPropertyName()))
		{
			//loop and dispose all locations
			for(Section section:sectionList)
			{
				System.out.println("Removin section"+((Location)section.getData()).getLocationName());
				//assert valid
				if(!section.isDisposed())
				{
					//get the child control
					Composite client = (Composite)section.getClient();
					if(client.isDisposed())
						continue;
					//remove all vehicles
					for(Control cont:client.getChildren())
					{
						VehicleComposite vehicle = (VehicleComposite)cont;
						if(!vehicle.isDisposed())
							vehicle.dispose(); 
					}
					section.dispose();
				}
			}
			//remove all location
			sectionList.clear();
		}
		//update the location
		if("LOCATION_UPDATE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the updated location
			Location updatedLocation = (Location)evt.getNewValue();
			//loop and update the location in the section
			for(Section section:sectionList)
			{
				//get the location in the section and compare
				Location sectionLocation = (Location)section.getData();
				if(sectionLocation.equals(updatedLocation))
				{
					section.setData(updatedLocation);
					updateSection(section);
				}
			}
		}
		//remove a specific location
		if("LOCATION_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the removed location
			Location removedLocation = (Location)evt.getOldValue();
			//loop and remove the section
			for(int i=0;i<  sectionList.size(); i++)
			{
				Section section = sectionList.get(i);
				//get the location in the section and compare
				Location sectionLocation = (Location)section.getData();
				if(sectionLocation.equals(removedLocation) &! section.isDisposed())
				{
					sectionList.remove(i);
					section.dispose();
				}
			}
		}
		// create the new composite
		if ("VEHICLE_ADD".equals(evt.getPropertyName())) 
		{ 
			boolean added = false;
			//the vehicle added
			VehicleDetail detail = (VehicleDetail)evt.getNewValue();
			//get the station to categorize the vehicle
			final String basicStation = detail.getBasicStation().getLocationName();
			//loop and try to get the section to insert the vehicle
			for(Section section:sectionList)
			{
				//get the location from the section
				Location location = (Location)section.getData();
				//add the vehicle to the station
				if(location.equals(detail.getBasicStation()))
				{
					addVehicleToStation((Composite)section.getClient(), detail);
					updateSection(section);
					added = true;
				}
			}
			if(!added)
				Activator.getDefault().log("Failed to add vehicle to non existing station: " + basicStation,IStatus.ERROR);
		}
		if("VEHICLE_ADD_ALL".equalsIgnoreCase(evt.getPropertyName()))
		{
			//the list of new vehicles
			List<?> vehicleList = (List<?>)evt.getNewValue();
			for(Object object:vehicleList)
			{
				boolean added = false;
				//cast to a vehicle
				VehicleDetail detail = (VehicleDetail)object;
				//get the station to categorize the vehicle
				final String basicStation = detail.getBasicStation().getLocationName();
				//loop and try to get the section to insert the vehicle
				for(Section section:sectionList)
				{
					//get the location from the section
					Location location = (Location)section.getData();
					//add the vehicle to the station
					if(location.equals(detail.getBasicStation()))
					{
						addVehicleToStation((Composite)section.getClient(), detail);
						updateSection(section);
						added = true;
					}
				}
				if(!added)
					Activator.getDefault().log("Failed to add vehicle to non existing station: " + basicStation,IStatus.ERROR);
			}
		}
		//remove all children of the sections
		if("VEHICLE_CLEAR".equalsIgnoreCase(evt.getPropertyName()))
		{
			//loop over the sections
			for(Section section:sectionList)
			{
				//get the child control
				Composite client = (Composite)section.getClient();
				if(client.isDisposed())
					return;
				//remove all vehicles
				for(Control cont:client.getChildren())
				{
					VehicleComposite vehicle = (VehicleComposite)cont;
					if(!vehicle.isDisposed())
						vehicle.dispose(); 
				}
				//update
				updateSection(section);
			}
		}
		//update the vehicle description
		if("VEHICLE_UPDATE".equalsIgnoreCase(evt.getPropertyName()))
		{
			for(Section section:sectionList)
				updateSection(section);
		}       
		//remove a specific vehicle
		if("VEHICLE_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the removed vehicle
			VehicleDetail removedVehicle = (VehicleDetail)evt.getOldValue();
			//loop and remove the vehicle
			for(int i=0;i< sectionList.size(); i++)
			{
				//get the child control
				Composite client = (Composite)sectionList.get(i).getClient();
				if(client.isDisposed())
					return;
				//remove all vehicles
				for(Control cont:client.getChildren())
				{
					VehicleComposite vehicleComposite = (VehicleComposite)cont;
					VehicleDetail compositeVehicle = (VehicleDetail)vehicleComposite.getData();
					if(compositeVehicle.equals(removedVehicle) &!vehicleComposite.isDisposed())
					{
						vehicleComposite.dispose();
					}
				}
				//update
				updateSection(sectionList.get(i));
			}
		}
		form.layout(true);
	}

	/**
	 * Creates a new section and returns it.
	 * @param form the form to add the section
	 * @param toolkit the toolkit to create the client component
	 * @param title the title of the section
	 * @param description the description of the section
	 * @return the the created section
	 */
	private Section createSection(ScrolledForm form,FormToolkit toolkit,String title,String description)
	{
		// Create the section
		Section section = toolkit.createSection(form.getBody(), ExpandableComposite.TWISTIE
				| ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | ExpandableComposite.EXPANDED);
		toolkit.createCompositeSeparator(section);
		// Add the title and the description
		section.setText(title);
		section.setDescription(description);
		section.setExpanded(false);
		section.setLayout(new GridLayout());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = GridData.GRAB_HORIZONTAL;
		section.setLayoutData(data);
		//the content of the section
		Composite client = toolkit.createComposite(section,SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		client.setLayout(layout);
		//add the client to the section
		section.setClient(client);
		//return the client
		return section;
	}

	/**
	 * Inserts a new vehicle into the given station section.
	 * @param stationComposite the composite to add to
	 * @param vehicle the vehicle to add
	 */
	private void addVehicleToStation(Composite stationComposite,VehicleDetail vehicle)
	{
		new VehicleComposite(stationComposite,vehicle);
		stationComposite.layout(true);
		//expand if we have a new vehicle
		Section section = (Section)stationComposite.getParent();
		section.setExpanded(true);
	}

	/**
	 * Updates the description in the vehicle section to show the vehicles that are ready for action
	 * @param section the section to update
	 */
	private void updateSection(Section section)
	{
		//get the location
		Location location = (Location)section.getData();
		//get the client composite
		Composite client = (Composite)section.getClient();

		//the number of vehicles in the composite
		int numOfVehicles = client.getChildren().length;

		//get the list of vehicles that are ready for action
		List<VehicleDetail> vehicleList = ModelFactory.getInstance().getVehicleList().getReadyVehicleListbyLocation(location);

		//update the description
		section.setText(location.getLocationName() +" - ("+ vehicleList.size() +" / "+numOfVehicles+")");
		section.setDescription("Zur Zeit sind in "+location.getLocationName() +" "+ vehicleList.size() +" Fahrzeuge einsatzbereit von insgesammt "+numOfVehicles);
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions()
	{
		//create the action
		RefreshViewAction refreshAction = new RefreshViewAction(VehicleDetail.ID);
		//add to the toolbar
		form.getToolBarManager().removeAll();
		form.getToolBarManager().add(refreshAction);
		form.getToolBarManager().update(true);
	}
}