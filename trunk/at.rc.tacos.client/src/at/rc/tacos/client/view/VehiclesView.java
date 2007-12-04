package at.rc.tacos.client.view;

import org.eclipse.ui.part.*;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.VehicleManager;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.composite.CarComposite;
import at.rc.tacos.factory.ImageFactory;
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
        new CarComposite(compositeKapfenberg,vehicleManager.getVehicleList().get(2));
        new CarComposite(compositeKapfenberg,vehicleManager.getVehicleList().get(3));
        new CarComposite(compositeKapfenberg,vehicleManager.getVehicleList().get(4));
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
        ColumnLayout layout = new ColumnLayout();
        layout.maxNumColumns = 4;
        layout.minNumColumns = 4;
        client.setLayout(layout);
        //add the client to the section
        section.setClient(client);
        //return the client
        return client;
    }
    
    /**
     * Creates the custom vehicle.
     * @return the created vehicle
     */
    public void createVehicle(Composite parent,VehicleDetail detail)
    {
        FormToolkit toolkit = new FormToolkit(parent.getDisplay());
        FormColors colors = toolkit.getColors();
        Color top = colors.getColor(IFormColors.H_GRADIENT_END);
        Color bot = colors.getColor(IFormColors.H_GRADIENT_START);

        // create the base form
        Form form = toolkit.createForm(parent);
        form.setText(detail.getVehicleName()+ " - "+detail.getVehicleType());
        form.setImage(ImageFactory.getInstance().getRegisteredImage("image.vehicle.status.green"));
        form.setTextBackground(new Color[] { top, bot }, new int[] { 100 }, true);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        form.getBody().setLayout(layout);

        // create the text for user information
        FormText text = toolkit.createFormText(form.getBody(), true);
        GridData td = new GridData();        
        text.setLayoutData(td);
        text.setText("<form>" +
        		"<p>Driver:" + detail.getDriverName().getUserName() + "</p>" +
        		"<p>Sani1:" + detail.getParamedicIName().getUserName() + "</p>" +
                "<p>Sani2:" + detail.getParamedicIIName().getUserName() + "</p>" +
        		"</form>", true, false);
    }
}