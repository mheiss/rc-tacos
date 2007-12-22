package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.ui.part.*;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
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
        
        // add listener to model to keep on track. 
        ModelFactory.getInstance().getVehicleManager().addPropertyChangeListener(this);

        // Create the sections for each station
        compositeKapfenberg = createSection(form,toolkit,"Kapfenberg","Fahzeuge von Kapfenberg");
        compositeBruck = createSection(form,toolkit,"Bruck","Fahzeuge von Bruck/Mur");
        compositeStMarein = createSection(form,toolkit,"St.Marein","Fahzeuge von St.Marein");
        compositeThoerl = createSection(form,toolkit,"Thörl","Fahzeuge von Thörl");
        compositeThurnau = createSection(form,toolkit,"Thurnau","Fahzeuge von Thurnau");
        compositeBreitenau = createSection(form,toolkit,"Breitenau","Breitenau");
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
            final String basicStation = detail.getBasicStation();
            //Add and update the section for Kapfenberg 
            if("KA".equalsIgnoreCase(basicStation))
            {
                new CarComposite(compositeKapfenberg,detail);
                compositeKapfenberg.layout(true);
            }
            //Add and update the section for Bruck 
            else if("BM".equalsIgnoreCase(basicStation))
            {
                new CarComposite(compositeBruck,detail);
                compositeBruck.layout(true);
            }
            //Add and update the section for St.Marein
            else if("MA".equalsIgnoreCase(basicStation))
            {
                new CarComposite(compositeStMarein,detail);
                compositeStMarein.layout(true);
            }
            //Add and update the section for Thoerl
            else if("TH".equalsIgnoreCase(basicStation))
            {
                new CarComposite(compositeThoerl,detail);
                compositeThoerl.layout(true);
            }
            // Add and update the section for Turnau
            else if("TU".equalsIgnoreCase(basicStation))
            {
                new CarComposite(compositeThurnau,detail);
            }
            else if("BR".equalsIgnoreCase(basicStation))
            {
                new CarComposite(compositeBreitenau,detail);
            }
            else
                System.out.println("Failed to add vehicle to non existing station: " + basicStation);
        }
        //force a redraw of the complete section so that the new composites are drwan
        form.getDisplay().update();
        form.getBody().layout(true);
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
}