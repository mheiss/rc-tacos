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
import at.rc.tacos.client.controller.EditorNewJobAction;
import at.rc.tacos.client.controller.EditorSaveAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Job;

public class JobEditor extends EditorPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.editors.jobEditor";

	//properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;
	
	private ImageHyperlink saveHyperlink,addHyperlink,removeHyperlink;
	private Text id,name;

	//managed data
	private Job job;
	private boolean isNew;
	
	/**
	 * Default class constructor
	 */
	public JobEditor()
	{
		ModelFactory.getInstance().getJobList().addPropertyChangeListener(this);
	}
	
	/**
	 * Cleanup
	 */
	@Override
	public void dispose()
	{
		ModelFactory.getInstance().getJobList().removePropertyChangeListener(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{	
		job = ((JobEditorInput)getEditorInput()).getJob();
		isNew = ((JobEditorInput)getEditorInput()).isNew();

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
			form.setText("Neue Verwendung anlegen");

		//force redraw
		form.pack(true);
	}
	
	/**
	 * Creates the section to manage the changes
	 */
	private void createManageSection(Composite parent)
	{
		Composite client = createSection(parent, "Verwendung verwalten");

		//create info label and hyperlinks to save and revert the changes
		CLabel infoLabel = new CLabel(client,SWT.NONE);
		infoLabel.setText("Hier können sie die aktuelle Verwendung verwalten und die Änderungen speichern.");
		infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));

		//Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Neue Verwendung anlegen");
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
		
		//create the hyperlink to add a new job
		addHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		addHyperlink.setText("Verwendung anlegen");
		addHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.jobAdd"));
		addHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				EditorNewJobAction newAction = new EditorNewJobAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				newAction.run();
			}
		});
		//Create the hyperlink to remove the competence
		removeHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		removeHyperlink.setText("Verwendung löschen");
		removeHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.jobRemove"));
		removeHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				boolean result = MessageDialog.openConfirm(getSite().getShell(), 
						"Löschen der Verwendung bestätigen", 
						"Möchten sie die Verwendung " +job.getJobName()+" wirklich löschen?");
				if(!result)
					return;
				//send the remove request
				EditorDeleteAction deleteAction = new EditorDeleteAction(Job.ID,job);
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
		Composite client = createSection(parent, "Verwendungs Details");
		
		//label and the text field
		final Label labelId = toolkit.createLabel(client, "Verwendungs ID");
		id = toolkit.createText(client, "");
		id.setEditable(false);
		id.setBackground(CustomColors.GREY_COLOR);
		id.setToolTipText("Die ID wird automatisch generiert");
		
		final Label labelCompName = toolkit.createLabel(client, "Verwendungs Bezeichnung");
		name = toolkit.createText(client, "");
		
		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelId.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelCompName.setLayoutData(data);
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		id.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		name.setLayoutData(data2);	
	}

	/**
	 * Loads the data and shows them in the view
	 */
	private void loadData()
	{
		form.setText("Details der Verwendung "+job.getJobName());
		if(!isNew)
		{
			//adjust the links
			saveHyperlink.setText("Änderungen speichern");
			addHyperlink.setVisible(true);
		}
		id.setText(String.valueOf(job.getId()));
		name.setText(job.getJobName());
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
			form.setMessage("Bitte geben sie eine Bezeichnung für die Verwendung an", IMessageProvider.ERROR);
			return;
		}
		job.setJobName(name.getText());
		
		//add or update the job
		if(isNew)
			NetWrapper.getDefault().sendAddMessage(Job.ID, job);
		else
			NetWrapper.getDefault().sendUpdateMessage(Job.ID, job);
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
		if("JOB_UPDATE".equals(evt.getPropertyName())
				|| "JOB_ADD".equalsIgnoreCase(evt.getPropertyName()))
		{
			Job updateJob = null;
			//get the new value
			if(evt.getNewValue() instanceof Job)
				updateJob = (Job)evt.getNewValue();

			//assert we have a value
			if(updateJob == null)
				return;

			//is this job the current -> update it
			if(job.equals(updateJob)
					|| job.getJobName().equals(updateJob.getJobName()))
			{
				//save the updated job
				setInput(new JobEditorInput(updateJob,false));
				setPartName(updateJob.getJobName());
				job = updateJob;
				isNew = false;
				//update the editor
				loadData();
			}
		}
		if("JOB_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the removed job
			Job removedJob = (Job)evt.getOldValue();
			//current open
			if(job.equals(removedJob))
			{
				MessageDialog.openInformation(getSite().getShell(), 
						"Verwendung wurde gelöscht",
						"Die Verwendung, welches Sie gerade editieren, wurde gelöscht");
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