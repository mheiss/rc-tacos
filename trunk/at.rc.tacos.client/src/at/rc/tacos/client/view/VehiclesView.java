package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.ui.part.*;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.Constants;
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

    //the composites to group the vehicles
    private Composite compositeKapfenberg;
    private Composite compositeBruck;
    private Composite compositeStMarein;
    private Composite compositeThoerl;
    private Composite compositeThurnau;
    private Composite compositeBreitenau;

    /**
     * Default class constructor
     */
    public VehiclesView()
    {
        // add listener to model to keep on track. 
        ModelFactory.getInstance().getVehicleList().addPropertyChangeListener(this);
    }
    
    /**
     * Cleanup and remove the listener
     */
    @Override 
    public void dispose()
    {
        // add listener to model to keep on track. 
        ModelFactory.getInstance().getVehicleList().removePropertyChangeListener(this);
    }
    
    /**
     * Create contents of the window.
     * @param parent the parent frame to insert the controlls
     */
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

        // Create the sections for each station
        compositeKapfenberg = createSection(form,toolkit,Constants.STATION_KAPFENBERG,"Fahzeuge von Kapfenberg");
        compositeBruck = createSection(form,toolkit,Constants.STATION_BRUCK,"Fahzeuge von Bruck/Mur");
        compositeStMarein = createSection(form,toolkit,Constants.STATION_MAREIN,"Fahzeuge von St.Marein");
        compositeThoerl = createSection(form,toolkit,Constants.STATION_THOERL,"Fahzeuge von Thörl");
        compositeThurnau = createSection(form,toolkit,Constants.STATION_TURNAU,"Fahzeuge von Thurnau");
        compositeBreitenau = createSection(form,toolkit,Constants.STATION_BREITENAU,"Breitenau");  
    }

    @Override
    public void setFocus() { }

    /**
     * Notification that the data model has changed so the view
     * must be updated.
     * @param evt the fired property event
     */
    public void propertyChange(final PropertyChangeEvent evt) 
    {
        // create the new composite
        if ("VEHICLE_ADD".equals(evt.getPropertyName())) 
        { 
            //the vehicle added
            VehicleDetail detail = (VehicleDetail)evt.getNewValue();
            //get the station to categorize the vehicle
            final String basicStation = detail.getBasicStation().getLocationName();
            //insert the vehicle to the according station
            if(Constants.STATION_KAPFENBERG.equalsIgnoreCase(basicStation))
            {
                addVehicleToStation(compositeKapfenberg,detail);
                updateSection(Constants.STATION_KAPFENBERG,compositeKapfenberg);
            }
            else if(Constants.STATION_BRUCK.equalsIgnoreCase(basicStation))
            {
                addVehicleToStation(compositeBruck,detail);
                updateSection(Constants.STATION_BRUCK,compositeBruck);
            }
            else if(Constants.STATION_MAREIN.equalsIgnoreCase(basicStation))
            {
                addVehicleToStation(compositeStMarein,detail);
                updateSection(Constants.STATION_MAREIN,compositeStMarein);
            }
            else if(Constants.STATION_THOERL.equalsIgnoreCase(basicStation))
            {
                addVehicleToStation(compositeThoerl,detail);
                updateSection(Constants.STATION_THOERL,compositeThoerl);
            }
            else if(Constants.STATION_TURNAU.equalsIgnoreCase(basicStation))
            {
                addVehicleToStation(compositeThurnau,detail);
                updateSection(Constants.STATION_TURNAU,compositeThurnau);
            }
            else if(Constants.STATION_BREITENAU.equalsIgnoreCase(basicStation))
            {
                addVehicleToStation(compositeBreitenau,detail);
                updateSection(Constants.STATION_BREITENAU,compositeBreitenau);
            }
            else
                System.out.println("Failed to add vehicle to non existing station: " + basicStation);
        }
        //remove all children of the sections
        if("VEHICLE_CLEAR".equalsIgnoreCase(evt.getPropertyName()))
        {
            clearStation(compositeKapfenberg);
        	clearStation(compositeBruck);
        	clearStation(compositeStMarein);
        	clearStation(compositeThoerl);
        	clearStation(compositeThurnau);
        	clearStation(compositeBreitenau);
        	//update
        	updateSection(Constants.STATION_KAPFENBERG,compositeKapfenberg);
        	updateSection(Constants.STATION_BRUCK,compositeBruck);
        	updateSection(Constants.STATION_MAREIN,compositeStMarein);
        	updateSection(Constants.STATION_THOERL,compositeThoerl);
        	updateSection(Constants.STATION_TURNAU,compositeThurnau);
        	updateSection(Constants.STATION_BREITENAU,compositeBreitenau);
        }
        //update the vehicle description
        if("VEHICLE_UPDATE".equalsIgnoreCase(evt.getPropertyName()))
        {
            updateSection(Constants.STATION_KAPFENBERG,compositeKapfenberg);
            updateSection(Constants.STATION_BRUCK,compositeBruck);
            updateSection(Constants.STATION_MAREIN,compositeStMarein);
            updateSection(Constants.STATION_THOERL,compositeThoerl);
            updateSection(Constants.STATION_TURNAU,compositeThurnau);
            updateSection(Constants.STATION_BREITENAU,compositeBreitenau);
        }
    }

    /**
     * Creates the section and returns the client component to add 
     * the content.
     * @param form the form to add the section
     * @param toolkit the toolkit to create the client component
     * @param title the title of the section
     * @param description the description of the section
     * @return the container of the section
     */
    private Composite createSection(ScrolledForm form,FormToolkit toolkit,String title,String description)
    {
        // Create the section
        Section section = toolkit.createSection(form.getBody(), Section.TWISTIE
                | Section.TITLE_BAR | Section.DESCRIPTION | Section.EXPANDED);
        toolkit.createCompositeSeparator(section);
        // Add the title and the description
        section.setText(title);
        section.setDescription(description);
        section.setExpanded(false);
        section.setLayout(new GridLayout());
        GridData data = new GridData();
        data.widthHint = GridData.GRAB_HORIZONTAL;
        section.setLayoutData(data);
        //the content of the section
        Composite client = toolkit.createComposite(section,SWT.WRAP);
        GridLayout layout = new GridLayout();
        layout.numColumns = 4;
        client.setLayout(layout);
        //add the client to the section
        section.setClient(client);
        //return the client
        return client;
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
     * Clears the vehicles in the given station.
     * @param stationComposite the composite to clear
     */
    private void clearStation(Composite stationComposite)
    {
        for(Control cont:stationComposite.getChildren())
        {
            VehicleComposite vehicle = (VehicleComposite)cont;
            vehicle.dispose(); 
        }
    }
    
    /**
     * Updates the description in the vehicle section 
     * to show the vehicles that are ready for action
     * @param station the name of the station the vehicle is assigned to
     * @param composite the composite to check
     */
    private void updateSection(String station,Composite stationComposite)
    {
        //the section of this composite
        Section section = (Section)stationComposite.getParent();
        //the number of vehicles in the composite
        int numOfVehicles = stationComposite.getChildren().length;
        
        //get the list of vehicles that are ready for action
        List<VehicleDetail> vehicleList = ModelFactory.getInstance().getVehicleList().getReadyVehicleListbyStation(station);
        
        //update the description
        section.setText(station +" - ("+ vehicleList.size() +" / "+numOfVehicles+")");
        section.setDescription("Zur Zeit sind in "+station +" "+ vehicleList.size() +" Fahrzeuge einsatzbereit von insgesammt "+numOfVehicles);
        
        //expand if we have at least one
        if(numOfVehicles > 0)
            section.setExpanded(true);
        else
            section.setExpanded(false);
    }
}