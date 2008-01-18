package at.rc.tacos.client.view;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import at.rc.tacos.client.controller.VehicleUpdateAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.MobilePhoneContentProvider;
import at.rc.tacos.client.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.providers.StaffMemberContentProvider;
import at.rc.tacos.client.providers.StaffMemberLabelProvider;
import at.rc.tacos.client.providers.StationContentProvider;
import at.rc.tacos.client.providers.StationLabelProvider;
import at.rc.tacos.client.providers.VehicleContentProvider;
import at.rc.tacos.client.providers.VehicleLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.Constants;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;

/**
 * The gui to manage a vehicle
 * @author Michael
 */
public class VehicleForm extends TitleAreaDialog
{
    //properties
    private FormToolkit toolkit;
    private ComboViewer vehicleComboViewer;
    private ComboViewer mobilePhoneComboViewer;
    private Button readyButton;
    private Button outOfOrder;
    private ComboViewer stationComboViewer;
    private ComboViewer driverComboViewer;
    private ComboViewer medic1ComboViewer;
    private ComboViewer medic2ComboViewer;
    private TextViewer noteEditor;  

    //the vehicle
    private VehicleDetail vehicleDetail;

    /**
     * Default class constructor for the vehicle form
     * @param parentShell the parent shell
     */
    public VehicleForm(Shell parentShell)
    {
        super(parentShell);
    }

    /**
     * Default class constructor for the vehicle form to edit a vehicle
     * @param parentShell the parent shell
     */
    public VehicleForm(Shell parentShell,VehicleDetail vehicle)
    {
        super(parentShell);
        //save
        this.vehicleDetail = vehicle;
    }

    /**
     * Creates the dialog's contents
     * @param parent the parent composite
     * @return Control
     */
    protected Control createContents(Composite parent) 
    {
        Control contents = super.createContents(parent);
        setTitle("Fahrzeugverwaltung");
        setMessage("Hier k�nnen Sie Fahrzeug und deren Besatzung verwalten", IMessageProvider.INFORMATION);
        setTitleImage(ImageFactory.getInstance().getRegisteredImage("application.logo.small"));
        //drwa the content
        contents.redraw();
        Composite client = ((Composite)contents);
        client.layout(true);
        getShell().setSize(500, 600);
        return contents;
    }
    
    /**
     * Create contents of the window
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        //setup the composite
        Composite composite = (Composite) super.createDialogArea(parent);
        toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
        
        //create the sections
        createDetailSection(composite);
        createStatusSection(composite);
        createCrewSection(composite);
        createNotesSection(composite);
        
        //init if we have a vehicle
        if(vehicleDetail != null)
        {
            vehicleComboViewer.setSelection(new StructuredSelection(vehicleDetail));
            mobilePhoneComboViewer.setSelection(new StructuredSelection(vehicleDetail.getMobilePhone()));
            stationComboViewer.getCombo().setText(vehicleDetail.getCurrentStation());
            if(vehicleDetail.getDriverName() != null)
                driverComboViewer.setSelection(new StructuredSelection(vehicleDetail.getDriverName()));
            if(vehicleDetail.getParamedicIName() != null)
                medic1ComboViewer.setSelection(new StructuredSelection(vehicleDetail.getParamedicIName()));
            if(vehicleDetail.getParamedicIIName() != null)
                medic2ComboViewer.setSelection(new StructuredSelection(vehicleDetail.getParamedicIIName()));
            readyButton.setSelection(vehicleDetail.isReadyForAction());
            outOfOrder.setSelection(vehicleDetail.isOutOfOrder());
            noteEditor.getDocument().set(vehicleDetail.getVehicleNotes());
        }
        
        //force redraw
        composite.redraw();
        composite.layout(true);
        return composite;
    }

    /**
     * The user pressed the cancel button
     */
    @Override
    protected void cancelPressed()
    {
        MessageBox dialog = new MessageBox(getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
        dialog.setText("Abbrechen");
        dialog.setMessage("Wollen Sie wirklich abbrechen?");
        //check the result
        if (dialog.open() != SWT.NO)
        {
            getShell().dispose();
            super.cancelPressed();
        }
    }

    /**
     * The user pressed the ok button
     */
    @Override
    protected void okPressed()
    {
        //check the required fileds
        if(checkRequiredFields())
        {
            //driver
            int index = driverComboViewer.getCombo().getSelectionIndex();
            vehicleDetail.setDriverName((StaffMember)driverComboViewer.getElementAt(index));
            //medic
            index = medic1ComboViewer.getCombo().getSelectionIndex();
            vehicleDetail.setParamedicIName((StaffMember)medic1ComboViewer.getElementAt(index));
            //medic1
            index = medic2ComboViewer.getCombo().getSelectionIndex();
            vehicleDetail.setParamedicIIName((StaffMember)medic2ComboViewer.getElementAt(index));
            //notes
            vehicleDetail.setVehicleNotes(noteEditor.getTextWidget().getText());
            //status
            vehicleDetail.setOutOfOrder(outOfOrder.getSelection());
            vehicleDetail.setReadyForAction(readyButton.getSelection());
            //phone
            index = mobilePhoneComboViewer.getCombo().getSelectionIndex();
            vehicleDetail.setMobilPhone((MobilePhoneDetail)mobilePhoneComboViewer.getElementAt(index));
            //station
            vehicleDetail.setCurrentStation(stationComboViewer.getCombo().getText());
            
            //Send the update message
            VehicleUpdateAction updateAction = new VehicleUpdateAction(vehicleDetail);
            updateAction.run();
            super.okPressed();
        }
        //indicate a error
        getShell().getDisplay().beep();
    }
    
    /**
     * Creates the detail section for the vehicle
     * @parent the parent composite
     */
    private void createDetailSection(Composite parent)
    {
        Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.TWISTIE);
        toolkit.createCompositeSeparator(section);
        section.setText("Fahrzeugdetails");
        section.setLayout(new GridLayout());
        section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        section.setExpanded(true);

        Composite client = new Composite(section, SWT.NONE);
        section.setClient(client);
        //layout
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        client.setLayout(gridLayout);
        
        final Label labelStaff = new Label(client, SWT.NONE);
        labelStaff.setText("Fahrzeug:");

        Combo vehicleCombo = new Combo(client, SWT.READ_ONLY);
        vehicleCombo.setEnabled(false);
        vehicleComboViewer = new ComboViewer(vehicleCombo);
        vehicleComboViewer.setContentProvider(new VehicleContentProvider());
        vehicleComboViewer.setLabelProvider(new VehicleLabelProvider());
        vehicleComboViewer.setInput(ModelFactory.getInstance().getVehicleManager());
        
        //Mobile Phone
        final Label labelPhone = new Label(client, SWT.NONE);
        labelPhone.setText("Handy :");

        Combo mobilePhoneCombo = new Combo(client, SWT.READ_ONLY);
        mobilePhoneComboViewer = new ComboViewer(mobilePhoneCombo);
        mobilePhoneComboViewer.setContentProvider(new MobilePhoneContentProvider());
        mobilePhoneComboViewer.setLabelProvider(new MobilePhoneLabelProvider());
        mobilePhoneComboViewer.setInput(ModelFactory.getInstance().getMobilePhoneManager());
        
        //Station
        final Label labelStation = new Label(client, SWT.NONE);
        labelStation.setText("Aktuelle Ortsstelle :");

        Combo stationCombo = new Combo(client, SWT.READ_ONLY);
        stationCombo.setToolTipText("Ist das Fahrzeug einer anderen Dienststelle zugeordnet, kann dies hier ausgew�hlt werden.");
        stationComboViewer = new ComboViewer(stationCombo);
        stationComboViewer.setContentProvider(new StationContentProvider());
        stationComboViewer.setLabelProvider(new StationLabelProvider());
        stationComboViewer.setInput(Constants.stations);
        
        //layout for the labels
        GridData data = new GridData();
        data.widthHint = 100;
        labelStaff.setLayoutData(data);
        data = new GridData();
        data.widthHint = 100;
        labelPhone.setLayoutData(data);
        data = new GridData();
        data.widthHint = 100;
        labelStation.setLayoutData(data);
        //layout for the text fields
        GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
        data2.widthHint= 200;
        vehicleCombo.setLayoutData(data2);
        data2 = new GridData(GridData.FILL_HORIZONTAL);
        data2.widthHint= 200;
        mobilePhoneCombo.setLayoutData(data2);
        data2 = new GridData(GridData.FILL_HORIZONTAL);
        data2.widthHint= 200;
        stationCombo.setLayoutData(data2);
    }
    
    /**
     * Creates the status section for the vehicle
     * @param parent the parent composite
     */
    private void createStatusSection(Composite parent)
    {
        //create the section
        Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.TWISTIE);
        toolkit.createCompositeSeparator(section);
        section.setText("Einsatzstatus");
        section.setDescription("Der aktuelle Einsatzstatus des Fahrzeuges");
        section.setLayout(new GridLayout());
        section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        section.setExpanded(true);
        
        //composite to add the client area
        Composite client = new Composite(section, SWT.NONE);
        section.setClient(client);

        //layout
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.horizontalSpacing = 15;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);
        GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        client.setLayoutData(clientDataLayout);
        
        //Selection listener for the combos
        SelectionListener listener = new SelectionListener()
        {
            @Override
            public void widgetSelected(SelectionEvent se)
            {
                checkRequiredFields();
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent se) { }
        };
        
        //Ready for action
        readyButton = new Button(client, SWT.CHECK);
        readyButton.addSelectionListener(listener);
        readyButton.setText("Einsatzbereit");
        
        
        //Out of Order
        outOfOrder = new Button(client, SWT.CHECK); 
        outOfOrder.addSelectionListener(listener);
        outOfOrder.setText("Au�er Dienst");
    }
    
    /**
     * Creates the crew section for the vehicle
     * @parent the parent composite
     */
    private void createCrewSection(Composite parent)
    {
        //create the section
        Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.TWISTIE);
        toolkit.createCompositeSeparator(section);
        section.setText("Fahrzeugbesatzung");
        section.setLayout(new GridLayout());
        section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        section.setExpanded(true);
        
        //composite to add the client area
        Composite client = new Composite(section, SWT.NONE);
        section.setClient(client);

        //layout
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.horizontalSpacing = 15;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);
        GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        client.setLayoutData(clientDataLayout);
        
        //driver
        final Label labelDriver = new Label(client, SWT.NONE);
        labelDriver.setText("Fahrer :");

        Combo driverCombo = new Combo(client, SWT.READ_ONLY);
        driverComboViewer = new ComboViewer(driverCombo);
        driverComboViewer.setContentProvider(new StaffMemberContentProvider());
        driverComboViewer.setLabelProvider(new StaffMemberLabelProvider());
        driverComboViewer.setInput(ModelFactory.getInstance().getStaffManager());
        
        //medic1
        final Label labelMedic1 = new Label(client, SWT.NONE);
        labelMedic1.setText("Sanit�ter :");

        Combo medic1Combo = new Combo(client, SWT.READ_ONLY);
        medic1ComboViewer = new ComboViewer(medic1Combo);
        medic1ComboViewer.setContentProvider(new StaffMemberContentProvider());
        medic1ComboViewer.setLabelProvider(new StaffMemberLabelProvider());
        medic1ComboViewer.setInput(ModelFactory.getInstance().getStaffManager());
        
        //medic2
        final Label labelMedic2 = new Label(client, SWT.NONE);
        labelMedic2.setText("Sanit�ter :");

        Combo medic2Combo = new Combo(client, SWT.READ_ONLY);
        medic2ComboViewer = new ComboViewer(medic2Combo);
        medic2ComboViewer.setContentProvider(new StaffMemberContentProvider());
        medic2ComboViewer.setLabelProvider(new StaffMemberLabelProvider());
        medic2ComboViewer.setInput(ModelFactory.getInstance().getStaffManager());
        
        //layout for the labels
        GridData data = new GridData();
        data.widthHint = 100;
        labelDriver.setLayoutData(data);
        data = new GridData();
        data.widthHint = 100;
        labelMedic1.setLayoutData(data);
        data = new GridData();
        data.widthHint = 100;
        labelMedic2.setLayoutData(data);
        //layout for the text fields
        GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
        driverCombo.setLayoutData(data2);
        data2 = new GridData(GridData.FILL_HORIZONTAL);
        medic1Combo.setLayoutData(data2);
        data2 = new GridData(GridData.FILL_HORIZONTAL);
        medic2Combo.setLayoutData(data2);
    }
    
    /**
     * Creates the notes section
     * @param parent the parent composite
     */
    private void createNotesSection(Composite parent)
    {
        //create the section
        Section dayInfoSection = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR);
        toolkit.createCompositeSeparator(dayInfoSection);
        dayInfoSection.setText("Anmerkungen");
        dayInfoSection.setExpanded(true);
        dayInfoSection.setLayout(new GridLayout());
        //info should span over two
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        dayInfoSection.setLayoutData(data);

        //create the container for the notes
        Composite notesField = toolkit.createComposite(dayInfoSection);
        dayInfoSection.setClient(notesField);
        notesField.setLayout(new GridLayout());
        GridData notesData = new GridData(GridData.FILL_BOTH);
        notesData.heightHint = 80;
        notesData.grabExcessVerticalSpace = true;
        notesData.grabExcessHorizontalSpace = true;
        notesField.setLayoutData(notesData);

        noteEditor = new TextViewer(notesField, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        noteEditor.setDocument(new Document());
        noteEditor.getControl().setLayoutData(notesData);
        noteEditor.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        noteEditor.setEditable(true);
    }

    /**
     * Helper method to determine wheter all fields are valid
     * @return true if all fields are valid, otherwise false
     */
    private boolean checkRequiredFields()
    {
        //Check the crew
        if(driverComboViewer.getSelection().isEmpty())
        {
            setMessage("Das Fahrzeug hat noch keinen Fahrer.", IMessageProvider.WARNING);
        }
        if(medic1ComboViewer.getSelection().isEmpty())
        {
            setMessage("Das Fahrzeug hat noch keinen, oder es fehlt ein Sanit�ter.", IMessageProvider.WARNING);
        }
        if(medic2ComboViewer.getSelection().isEmpty())
        {
            setMessage("Das Fahrzeug hat noch keinen, oder es fehlt ein Sanit�ter.", IMessageProvider.WARNING);
        }
        if(outOfOrder.getSelection())
        {
            setMessage("Das aktuelle Fahrzeug kann keinem Transport zugewiesen werden.", IMessageProvider.WARNING);
        }
        if(!readyButton.getSelection())
        {
            setMessage("Das aktuelle Fahrzeug kann keinem Transport zugewiesen werden da es nicht als Einsatzbereit markiert ist", IMessageProvider.WARNING);
        }
        return true;
    }
}
