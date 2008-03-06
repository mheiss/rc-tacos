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
import at.rc.tacos.client.controller.EditorNewMobilePhoneAction;
import at.rc.tacos.client.controller.EditorSaveAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.util.NumberValidator;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneEditor extends EditorPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.editors.mobilePhoneEditor";

	//properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;
	
	private ImageHyperlink saveHyperlink,addHyperlink,removeHyperlink;
	private Text id,name,number;

	//managed data
	private MobilePhoneDetail detail;
	private boolean isNew;
	
	/**
	 * Default class constructor
	 */
	public MobilePhoneEditor()
	{
		ModelFactory.getInstance().getPhoneList().addPropertyChangeListener(this);
	}
	
	/**
	 * Cleanup
	 */
	@Override
	public void dispose()
	{
		ModelFactory.getInstance().getPhoneList().removePropertyChangeListener(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{	
		detail = ((MobilePhoneEditorInput)getEditorInput()).getMobilePhone();
		isNew = ((MobilePhoneEditorInput)getEditorInput()).isNew();

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
			form.setText("Neues Mobiltelefon anlegen");

		//force redraw
		form.pack(true);
	}
	
	/**
	 * Creates the section to manage the changes
	 */
	private void createManageSection(Composite parent)
	{
		Composite client = createSection(parent, "Mobiltelefon verwalten");

		//create info label and hyperlinks to save and revert the changes
		CLabel infoLabel = new CLabel(client,SWT.NONE);
		infoLabel.setText("Hier können sie das aktuelle Mobiltelefon verwalten und die Änderungen speichern.");
		infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));

		//Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Neues Mobiltelefon anlegen");
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

		//create the hyperlink to add a new mobile phone
		addHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		addHyperlink.setText("Mobiltelefon anlegen");
		addHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.mobilePhoneAdd"));
		addHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				EditorNewMobilePhoneAction newAction = new EditorNewMobilePhoneAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				newAction.run();
			}
		});
		//Create the hyperlink to remove the competence
		removeHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		removeHyperlink.setText("Mobiltelefon löschen");
		removeHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.mobilePhoneRemove"));
		removeHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				boolean result = MessageDialog.openConfirm(getSite().getShell(), 
						"Löschen des Mobiltelefons bestätigen", 
						"Möchten sie das Mobiltelefon " +detail.getMobilePhoneName() + "-"+detail.getMobilePhoneNumber()+" wirklich löschen?");
				if(!result)
					return;
				//send the remove request
				EditorDeleteAction deleteAction = new EditorDeleteAction(MobilePhoneDetail.ID,detail);
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
	 * Creates the section containing the competence details
	 * @param parent the parent composite
	 */
	private void createDetailSection(Composite parent)
	{
		Composite client = createSection(parent, "Mobiltelefon Details");

		//label and the text field
		final Label labelId = toolkit.createLabel(client, "Mobiltelefon ID");
		id = toolkit.createText(client, "");
		id.setEditable(false);
		id.setBackground(CustomColors.GREY_COLOR);
		id.setToolTipText("Die ID wird automatisch generiert");

		final Label labelPhoneName = toolkit.createLabel(client, "Bezeichnung");
		name = toolkit.createText(client, "");
		
		final Label labelPhoneNumber = toolkit.createLabel(client, "Nummer");
		number = toolkit.createText(client, "");

		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelId.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelPhoneName.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelPhoneNumber.setLayoutData(data);
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		id.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		name.setLayoutData(data2);	
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		number.setLayoutData(data2);
	}

	/**
	 * Loads the data and shows them in the view
	 */
	private void loadData()
	{
		form.setText("Details des Mobiltelefons: " + detail.getMobilePhoneName() + " " + detail.getMobilePhoneNumber());
		if(!isNew)
		{
			//adjust the links
			saveHyperlink.setText("Änderungen speichern");
			addHyperlink.setVisible(true);
		}
		//load the data
		id.setText(String.valueOf(detail.getId()));
		name.setText(detail.getMobilePhoneName());
		number.setText(detail.getMobilePhoneNumber());
	}

	@Override
	public void doSave(IProgressMonitor monitor) 
	{
		//reset error message
		form.setMessage(null, IMessageProvider.NONE);
		
		//name must be provided
		if(name.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben sie eine Bezeichnung für das Mobiltelefon ein", IMessageProvider.ERROR);
			return;
		}
		detail.setMobilePhoneName(name.getText());
		
		//number must be provided
		if(number.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben sie eine Nummer für das Mobiltelefon ein", IMessageProvider.ERROR);
			return;
		}
		//validate the number
		NumberValidator validator = new NumberValidator();
		String validatorResult = validator.isValid(number.getText());
		if(validatorResult != null)
		{
			form.getDisplay().beep();
			form.setMessage(validatorResult, IMessageProvider.ERROR);
			return;
		}
		detail.setMobilePhoneNumber(number.getText());
		
		//add or update the phone
		if(isNew)
			NetWrapper.getDefault().sendAddMessage(MobilePhoneDetail.ID, detail);
		else
			NetWrapper.getDefault().sendUpdateMessage(MobilePhoneDetail.ID, detail);
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
		if("PHONE_UPDATE".equals(evt.getPropertyName())
				|| "PHONE_ADD".equalsIgnoreCase(evt.getPropertyName()))
		{
			MobilePhoneDetail updatePhone = null;
			//get the new value
			if(evt.getNewValue() instanceof MobilePhoneDetail)
				updatePhone = (MobilePhoneDetail)evt.getNewValue();

			//assert we have a value
			if(updatePhone == null)
				return;

			//is this mobile phone is the current one -> update it
			if(detail.equals(updatePhone) 
					|| (detail.getMobilePhoneName().equals(updatePhone.getMobilePhoneName())
							&& detail.getMobilePhoneNumber().equals(updatePhone.getMobilePhoneNumber())))
			{
				//save the updated phone
				setInput(new MobilePhoneEditorInput(updatePhone,false));
				setPartName(updatePhone.getMobilePhoneName() + " "+ detail.getMobilePhoneNumber());
				detail = updatePhone;
				isNew = false;
				//update the editor
				loadData();
			}
		}
		if("PHONE_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the removed phone
			MobilePhoneDetail removedPhone = (MobilePhoneDetail)evt.getOldValue();
			//current edited?
			if(detail.equals(removedPhone))
			{
				MessageDialog.openInformation(getSite().getShell(), 
						"Mobiletelefon wurde gelöscht",
						"Das Mobiltelefon, welches Sie gerade editieren, wurde gelöscht");
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