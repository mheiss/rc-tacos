package at.rc.tacos.client.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

import at.rc.tacos.client.controller.EditorCloseAction;
import at.rc.tacos.client.controller.EditorDeleteAction;
import at.rc.tacos.client.controller.EditorNewLocationAction;
import at.rc.tacos.client.controller.EditorSaveAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.MobilePhoneContentProvider;
import at.rc.tacos.client.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;

public class LocationEditor extends EditorPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.editors.locationEditor";

	//properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;
	
	private ImageHyperlink saveHyperlink,addHyperlink,removeHyperlink;
	private Text locationName,street,streetNumber,zipCode,city;
	private TextViewer notesViewer;
	private ComboViewer phoneViewer;

	//managed data
	private Location location;
	private boolean isNew;
	
	/**
	 * Default class constructor
	 */
	public LocationEditor()
	{
		ModelFactory.getInstance().getLocationList().addPropertyChangeListener(this);
		ModelFactory.getInstance().getPhoneList().addPropertyChangeListener(this);
	}
	
	/**
	 * Cleanup
	 */
	@Override
	public void dispose()
	{
		ModelFactory.getInstance().getLocationList().removePropertyChangeListener(this);
		ModelFactory.getInstance().getPhoneList().removePropertyChangeListener(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{	
		location = ((LocationEditorInput)getEditorInput()).getLocation();
		isNew = ((LocationEditorInput)getEditorInput()).isNew();

		//Create the form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new GridLayout());
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//create the content
		createManageSection(form.getBody());
		createDetailSection(form.getBody());

		//load the data
		if(!isNew)
			loadData();
		else
			form.setText("Neue Dienststelle anlegen");

		//force redraw
		form.pack(true);
	}
	
	/**
	 * Creates the section to manage the changes
	 */
	private void createManageSection(Composite parent)
	{
		Composite client = createSection(parent, "Ortsstelle verwalten");

		//create info label and hyperlinks to save and revert the changes
		CLabel infoLabel = new CLabel(client,SWT.NONE);
		infoLabel.setText("Hier können sie die aktuelle Ortsstelle verwalten und die Änderungen speichern.");
		infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));

		//Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Neue Ortsstelle anlegen");
		saveHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.save"));
		saveHyperlink.addHyperlinkListener(new HyperlinkAdapter() 
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				EditorSaveAction saveAction = new EditorSaveAction();
				saveAction.run();
			}
		});

		//create the hyperlink to add a new staff member
		addHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		addHyperlink.setText("Ortsstelle anlegen");
		addHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.locationAdd"));
		addHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				EditorNewLocationAction newAction = new EditorNewLocationAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				newAction.run();
			}
		});
		//Create the hyperlink to remove the competence
		removeHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		removeHyperlink.setText("Ortsstelle löschen");
		removeHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.locationRemove"));
		removeHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				boolean result = MessageDialog.openConfirm(getSite().getShell(), 
						"Löschen der Ortsstelle bestätigen", 
						"Möchten sie die Ortsstelle " +location.getLocationName()+" wirklich löschen?");
				if(!result)
					return;
				//send the remove request
				EditorDeleteAction deleteAction = new EditorDeleteAction(Location.ID,location);
				deleteAction.run();
			}
		});

		//info label should span over two
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		infoLabel.setLayoutData(data);
		//save hyperlink should span over two
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		saveHyperlink.setLayoutData(data);
	}
	
	/**
	 * Creates the section containing the job details
	 * @param parent the parent composite
	 */
	private void createDetailSection(Composite parent)
	{
		Composite client = createSection(parent, "Dienststellen Details");
		
		//label and the text field
		final Label labelLocationName = toolkit.createLabel(client, "Name der Ortsstelle");
		locationName = toolkit.createText(client, "");
		final Label labelStreet = toolkit.createLabel(client, "Addresse");
		street = toolkit.createText(client, "");
		final Label labelStreetNumber = toolkit.createLabel(client, "Hausnummer");
		streetNumber = toolkit.createText(client, "");
		final Label labelZip = toolkit.createLabel(client, "Postleitzahl");
		zipCode = toolkit.createText(client, "");
		final Label labelCity = toolkit.createLabel(client, "Stadt");
		city = toolkit.createText(client, "");
		//the phone viewer
		final Label labelPhone = toolkit.createLabel(client,"Telefon der Ortstelle");
		Combo comboPhone = new Combo(client, SWT.READ_ONLY);
		phoneViewer = new ComboViewer(comboPhone);
		phoneViewer.setContentProvider(new MobilePhoneContentProvider());
		phoneViewer.setLabelProvider(new MobilePhoneLabelProvider());
		phoneViewer.setInput(ModelFactory.getInstance().getPhoneList().getMobilePhoneList());
		//the notes section
		final Label labelNotes = toolkit.createLabel(client,"Notizen zur Ortsstelle");
		notesViewer = new TextViewer(client, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		notesViewer.setDocument(new Document());
		notesViewer.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		notesViewer.setEditable(true);
		
		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelLocationName.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelStreet.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelStreetNumber.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelZip.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelCity.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelPhone.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelNotes.setLayoutData(data);
		
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		locationName.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		street.setLayoutData(data2);	
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		streetNumber.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		zipCode.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		city.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		phoneViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.heightHint = 100;
		notesViewer.getTextWidget().setLayoutData(data2);
	}

	/**
	 * Loads the data and shows them in the view
	 */
	private void loadData()
	{
		form.setText("Details des Ortsstelle "+location.getLocationName());
		if(!isNew)
		{
			//adjust the links
			saveHyperlink.setText("Änderungen speichern");
			addHyperlink.setVisible(true);
		}
		locationName.setText(location.getLocationName());
		street.setText(location.getStreet());
		streetNumber.setText(location.getStreetNumber());
		zipCode.setText(String.valueOf(location.getZipcode()));
		city.setText(location.getCity());
		notesViewer.getTextWidget().setText(location.getNotes());
		phoneViewer.setSelection(new StructuredSelection(location.getPhone()));
	}

	@Override
	public void doSave(IProgressMonitor monitor) 
	{
		//reset error message
		form.setMessage(null, IMessageProvider.NONE);
		
		//save the name
		if(locationName.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie die Bezeichnung der Ortsstelle ein", IMessageProvider.ERROR);
			return;
		}
		location.setLocationName(locationName.getText());
		
		//save the street
		if(street.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine Straße ein", IMessageProvider.ERROR);
			return;
		}
		location.setStreet(street.getText());
		
		//save the street number
		if(streetNumber.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine Hausnummer ein", IMessageProvider.ERROR);
			return;
		}
		location.setStreetNumber(streetNumber.getText());
		
		//save the city
		if(zipCode.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine Postleitzahl ein", IMessageProvider.ERROR);
			return;
		}
		//validate the number
		String pattern =  "\\d{4}";
		if(!zipCode.getText().matches(pattern))
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine gültige Postleitzahl ein", IMessageProvider.ERROR);
			return;
		}
		location.setZipcode(Integer.valueOf(zipCode.getText()));
		
		//the notes can be empty
		location.setNotes(notesViewer.getTextWidget().getText());
		
		int index = phoneViewer.getCombo().getSelectionIndex();
		if(index == -1)
		{
			form.getDisplay().beep();
			form.setMessage("Bitte wählen Sie ein Telefon für die Ortsstelle aus.\n" +
					"Neue Telefone können Sie im Administrationsbereich unter \"Mobiltelefone\" anlegen und dann der Ortsstelle zuweisen", IMessageProvider.ERROR);
			return;
		}
		location.setPhone((MobilePhoneDetail)phoneViewer.getElementAt(index));
		
		//add or update the location
		if(isNew)
			NetWrapper.getDefault().sendAddMessage(Location.ID, location);
		else
			NetWrapper.getDefault().sendUpdateMessage(Location.ID, location);
	}

	@Override
	public void doSaveAs() 
	{
		//not supported
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException 
	{
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	@Override
	public void setFocus() 
	{
		form.setFocus();
	}

	@Override
	public boolean isDirty() 
	{
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() 
	{
		//not supported
		return false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		if("LOCATION_UPDATE".equals(evt.getPropertyName())
				|| "LOCATION_ADD".equalsIgnoreCase(evt.getPropertyName()))
		{
			Location updateLocation = null;
			//get the new value
			if(evt.getNewValue() instanceof Location)
				updateLocation = (Location)evt.getNewValue();

			//assert we have a value
			if(updateLocation == null)
				return;

			//is this location is the current -> update it
			if(location.equals(updateLocation) 
					|| location.getLocationName().equals(updateLocation.getLocationName()))
			{
				//save the updated location
				setInput(new LocationEditorInput(updateLocation,false));
				setPartName(location.getLocationName());
				location = updateLocation;
				isNew = false;
				//update the editor
				loadData();
			}
		}
		if("LOCATION_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the removed location
			Location removedLocation = (Location)evt.getOldValue();
			//current open?
			if(location.equals(removedLocation))
			{
				MessageDialog.openInformation(getSite().getShell(), 
						"Ortsstelle wurde gelöscht",
						"Die Ortsstelle, welches Sie gerade editieren, wurde gelöscht");
				EditorCloseAction closeAction = new EditorCloseAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				closeAction.run();
			}
		}
		if("PHONE_ADD".equalsIgnoreCase(evt.getPropertyName())
				|| "PHONE_UPDATE".equalsIgnoreCase(evt.getPropertyName())
				|| "PHONE_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//just refresh the viewer to update the combo
			phoneViewer.refresh(true);
		}
	}
	
	//Helper methods
	/**
	 * Creates and returns a section and a composite with two colums
	 * @param parent the parent composite
	 * @param sectionName the title of the section
	 * @return the created composite to hold the other widgets
	 */
	private Composite createSection(Composite parent,String sectionName)
	{
		//create the section
		Section section = toolkit.createSection(parent,ExpandableComposite.TITLE_BAR | Section.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText(sectionName);
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
		section.setExpanded(true);
		//composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		//layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH);
		client.setLayoutData(clientDataLayout);

		return client;
	}
}