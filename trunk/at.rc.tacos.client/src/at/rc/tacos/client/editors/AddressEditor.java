package at.rc.tacos.client.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import at.rc.tacos.client.controller.EditorNewAddressAction;
import at.rc.tacos.client.controller.EditorSaveAction;
import at.rc.tacos.client.controller.ImportAddressAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Address;

public class AddressEditor extends EditorPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.editors.addressEditor";

	//properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;

	private ImageHyperlink saveHyperlink,removeHyperlink,importHyperlink;
	private Text zip,city,street;

	//managed data
	private Address address;
	private boolean isNew;

	/**
	 * Default class constructor
	 */
	public AddressEditor()
	{
		ModelFactory.getInstance().getAddressManager().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup the editor
	 */
	@Override
	public void dispose()
	{
		ModelFactory.getInstance().getAddressManager().removePropertyChangeListener(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{	
		address = ((AddressEditorInput)getEditorInput()).getAddress();
		isNew = ((AddressEditorInput)getEditorInput()).isNew();

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
			form.setText("Neue Addresse anlegen");

		//force redraw
		form.pack(true);
	}

	/**
	 * Creates the section to manage the changes
	 */
	private void createManageSection(Composite parent)
	{
		Composite client = createSection(parent, "Adresse verwalten");

		//create info label and hyperlinks to save and revert the changes
		CLabel infoLabel = new CLabel(client,SWT.NONE);
		infoLabel.setText("Hier können sie die aktuelle Adresse verwalten und die Änderungen speichern.");
		infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));

		//Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Neue Adresse speichern");
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
		
		//Create the hyperlink to import the data
		importHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		importHyperlink.setText("Straßendaten importieren");
		importHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("resource.import"));
		importHyperlink.addHyperlinkListener(new HyperlinkAdapter() 
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				ImportAddressAction importAction = new ImportAddressAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				importAction.run();
			}
		});

		//Create the hyperlink to remove the competence
		removeHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		removeHyperlink.setText("Adresse löschen");
		removeHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.addressRemove"));
		removeHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				boolean result = MessageDialog.openConfirm(getSite().getShell(), 
						"Löschen der Adresse bestätigen", 
						"Möchten sie die Adresse " +address.getZip()+","+address.getCity()+","+address.getStreet()+" wirklich löschen?");
				if(!result)
					return;
				//send the remove request
				EditorDeleteAction deleteAction = new EditorDeleteAction(Address.ID,address);
				deleteAction.run();
			}
		});

		//info label should span over two
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		infoLabel.setLayoutData(data);
	}

	/**
	 * Creates the section containing the address details
	 * @param parent the parent composite
	 */
	private void createDetailSection(Composite parent)
	{
		Composite client = createSection(parent, "Adress Details");

		//label and the text field
		final Label labelStreet = toolkit.createLabel(client, "Straße");
		street = toolkit.createText(client, "");

		final Label labelCity = toolkit.createLabel(client, "Stadt");
		city = toolkit.createText(client, "");
		
		final Label labelZip = toolkit.createLabel(client, "Gemeindekennzeichend");
		zip = toolkit.createText(client, "");

		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelStreet.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelCity.setLayoutData(data);
		data.widthHint = 150;
		labelZip.setLayoutData(data);
		
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		street.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		city.setLayoutData(data2);	
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		zip.setLayoutData(data2);	
	}

	/**
	 * Loads the data and shows them in the view
	 */
	private void loadData()
	{
		form.setText("Details der Adresse "+address.getZip()+","+address.getCity()+","+address.getStreet());
		if(!isNew)
		{
			//adjust the links
			saveHyperlink.setText("Änderungen speichern");
		}
		street.setText(address.getStreet());
		city.setText(address.getCity());
		zip.setText(String.valueOf(address.getCity()));
	}

	@Override
	public void doSave(IProgressMonitor arg0) 
	{
		//reset error message
		form.setMessage(null, IMessageProvider.NONE);
		
		//name must be provided
		if(street.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie den Namen der Straße eine", IMessageProvider.ERROR);
			return;
		}
		address.setStreet(street.getText());
		
		//city must be provided
		if(city.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bittge geben Sie den Namen der Stadt ein", IMessageProvider.ERROR);
			return;
		}
		address.setCity(city.getText());
		
		//zip must be provided and a number
		if(zip.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie das Gemeindekennzeichen ein", IMessageProvider.ERROR);
			return;
		}
		//validate the zip
		String pattern = "6\\d{4}";
		if(!zip.getText().matches(pattern))
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie ein gültiges Gemeindekennzeichen in der Form 6xxxx ein",IMessageProvider.ERROR);
			return;
		}
		address.setZip(Integer.valueOf(zip.getText()));
		
		//add or update the staff member and the login
		if(isNew)
		{
			NetWrapper.getDefault().sendAddMessage(Address.ID, address);
		}
		else
		{
			NetWrapper.getDefault().sendUpdateMessage(Address.ID, address);
		}
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
		if("ADDRESS_ADD".equals(evt.getPropertyName())
				|| "ADDRESS_UPDATE".equals(evt.getPropertyName()))
		{
			Address updateAddress = null;
			//get the new value
			if(evt.getNewValue() instanceof Address)
				updateAddress = (Address)evt.getNewValue();

			//assert we have a value
			if(updateAddress == null)
				return;

			//is this job the current -> update it
			if(address.equals(updateAddress))
			{
				//save the updated address
				setInput(new AddressEditorInput(updateAddress,false));
				setPartName(address.getZip()+","+address.getCity()+","+address.getStreet());
				address = updateAddress;
				isNew = false;
				//update the editor
				loadData();
			}

		}

		if("ADDRESS_REMOVE".equals(evt.getPropertyName()))
		{
			//get the removed address
			Address removedAddress = (Address)evt.getOldValue();
			//current open
			if(address.equals(removedAddress))
			{
				MessageDialog.openInformation(getSite().getShell(), 
						"Adresse wurde gelöscht",
				"Die Adresse, welches Sie gerade editieren, wurde gelöscht");
				EditorCloseAction closeAction = new EditorCloseAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				closeAction.run();
			}
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
		Section section = toolkit.createSection(parent,ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
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
