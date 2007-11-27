package at.rc.tacos.client.view;

import org.eclipse.ui.part.*;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.VehicleManager;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.composite.CarComposite;
import at.rc.tacos.model.VehicleDetail;

/**
 * GUI to get an overview about the ambulances
 * create CarComposite's using the CarCompositeManager
 * @author b.thek
 */
public class VehiclesView extends ViewPart
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
    
    //the manager holding the data to display
    private VehicleManager vehicleManager;

    /**
     * Create contents of the window
     */
    public void createPartControl(Composite parent) 
    {
        // Create the scrolled parent component
        toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
        form = toolkit.createScrolledForm(parent);
        form.setText("�berblick �ber die Fahrzeuge des Bezirkes");
        toolkit.decorateFormHeading(form.getForm());
        ColumnLayout layout = new ColumnLayout();
        layout.maxNumColumns = 1;
        layout.minNumColumns = 1;
        form.getBody().setLayout(layout);
        
        //get the values from the activator
        vehicleManager = Activator.getDefault().getVehicleManager();

        // Create the sections for each station
        compositeKapfenberg = createSection(form,toolkit,"Kapfenberg","Fahzeuge von Kapfenberg");
        compositeBruck = createSection(form,toolkit,"Bruck","Fahzeuge von Bruck/Mur");
        compositeStMarein = createSection(form,toolkit,"St.Marein","Fahzeuge von St.Marein");
        compositeThoerl = createSection(form,toolkit,"Th�rl","Fahzeuge von Th�rl");
        compositeThurnau = createSection(form,toolkit,"Thurnau","Fahzeuge von Thurnau");
        compositeBreitenau = createSection(form,toolkit,"Breitenau","Breitenau");
        
        new CarComposite(compositeKapfenberg,vehicleManager.getVehicleList().get(0));
        new CarComposite(compositeKapfenberg,vehicleManager.getVehicleList().get(1));
    }

    @Override
    public void setFocus() { }

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
        client.setLayout(new ColumnLayout());
        //add the client to the section
        section.setClient(client);
        //return the client
        return client;
    }
}