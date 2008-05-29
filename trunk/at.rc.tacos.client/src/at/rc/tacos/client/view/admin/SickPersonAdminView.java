package at.rc.tacos.client.view.admin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import at.rc.tacos.client.Activator;
import at.rc.tacos.client.controller.EditorNewSickPersonAction;
import at.rc.tacos.client.controller.RefreshViewAction;
import at.rc.tacos.client.editors.SickPersonEditor;
import at.rc.tacos.client.editors.SickPersonEditorInput;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.providers.SickPersonAdminTableLabelProvider;
import at.rc.tacos.client.providers.SickPersonAdminViewFilter;
import at.rc.tacos.client.providers.SickPersonContentProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.SickPerson;

public class SickPersonAdminView  extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.view.admin.sickpersonAdminView";  

	//properties
	private TableViewer viewer;
	private FormToolkit toolkit;
	private ScrolledForm form;
	//text fields for the filter
	private Text lastname, firstname, svnr;

	//to apply the filter
	private CLabel infoLabel;

	/**
	 * Default class constructor
	 */
	public SickPersonAdminView()
	{
		ModelFactory.getInstance().getSickPersonManager().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose()
	{
		ModelFactory.getInstance().getSickPersonManager().removePropertyChangeListener(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{
		String authorization = SessionManager.getInstance().getLoginInformation().getAuthorization();
		
		//the scrolled form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Liste der Patienten"); 
		
		if(!authorization.equalsIgnoreCase("Administrator"))
			form.setEnabled(false);
		toolkit.decorateFormHeading(form.getForm());
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		form.getBody().setLayout(layout);
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));

		//create the section to hold the filter
		Composite filter = createSection(form.getBody(), "Filter") ;

		//create the input fields
		final Label labelLastname = toolkit.createLabel(filter, "Nachname");
		lastname = toolkit.createText(filter, "");
		lastname.addModifyListener(new ModifyListener() 
		{
			public void modifyText(final ModifyEvent e) 
			{
				inputChanged();
			}
		});

		//the firstname
		final Label labelFirstname = toolkit.createLabel(filter, "Vorname");
		firstname = toolkit.createText(filter, "");
		firstname.addModifyListener(new ModifyListener() 
		{
			public void modifyText(final ModifyEvent e) 
			{
				inputChanged();
			}
		});

		//the svnr
		final Label labelSVNR = toolkit.createLabel(filter, "SVNR");
		svnr = toolkit.createText(filter, "");
		svnr.addModifyListener(new ModifyListener() 
		{
			public void modifyText(final ModifyEvent e) 
			{
				inputChanged();
			}
		});

		//create the hyperlink to add a new job
		infoLabel = new CLabel(filter,SWT.NONE);
		infoLabel.setText("Bitte geben sie mindestens ein Zeichen des Nachnamens ein");
		infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("resource.info"));

		//create the section to hold the table
		Composite tableComp = createSection(form.getBody(), "Filter") ;
		Table table = new Table(tableComp, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		viewer = new TableViewer(table);
		viewer.setUseHashlookup(true);
		viewer.getTable().setLayout(new GridLayout());
		viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.addDoubleClickListener(new IDoubleClickListener()
		{
			@Override
			public void doubleClick(DoubleClickEvent dce) 
			{
				//get the selected disease
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				SickPerson person = (SickPerson)obj;
				//create the editor input and open
				SickPersonEditorInput input = new SickPersonEditorInput(person,false);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try 
				{
					page.openEditor(input, SickPersonEditor.ID);
				} 
				catch (PartInitException e) 
				{
					Activator.getDefault().log("Failed to open the editor for the sick person "+person, IStatus.ERROR);
				}
			}
		});
		viewer.setContentProvider(new SickPersonContentProvider());
		viewer.setLabelProvider(new SickPersonAdminTableLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getSickPersonManager().toArray());
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		getViewSite().setSelectionProvider(viewer);

		//create the columns
		final TableColumn imageColumn = new TableColumn(table, SWT.NONE);
		imageColumn.setToolTipText("");
		imageColumn.setWidth(30);
		imageColumn.setText("");

		final TableColumn zipColumn = new TableColumn(table, SWT.NONE);
		zipColumn.setToolTipText("Sozialversicherungsnummer");
		zipColumn.setWidth(60);
		zipColumn.setText("SVNR");

		final TableColumn cityColumn = new TableColumn(table, SWT.NONE);
		cityColumn.setToolTipText("Nachname");
		cityColumn.setWidth(180);
		cityColumn.setText("Nachname");

		final TableColumn streetColumn = new TableColumn(table, SWT.NONE);
		streetColumn.setToolTipText("Vorname");
		streetColumn.setWidth(180);
		streetColumn.setText("Vorname");

		//add actions to the toolbar
		createToolBarActions();

		//set this table as a selection provider
		getViewSite().setSelectionProvider(viewer);

		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 80;
		labelLastname.setLayoutData(data);
		data = new GridData();
		data.widthHint = 80;
		labelFirstname.setLayoutData(data);
		data.widthHint = 80;
		labelSVNR.setLayoutData(data);
		labelFirstname.setLayoutData(data);
		data.widthHint = 80;
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		lastname.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		firstname.setLayoutData(data2);	
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		svnr.setLayoutData(data2);	
		data2 = new GridData(GridData.FILL_BOTH);
		viewer.getTable().setLayoutData(data2);
		//the section of the table
		data2 = new GridData(GridData.FILL_BOTH);
		Section tableSection = (Section)tableComp.getParent();
		tableSection.setLayoutData(data2);
		//the info label
		data2 = new GridData(GridData.FILL_BOTH);
		data2.horizontalSpan = 2;
		infoLabel.setLayoutData(data2);
		
		//reflow
		form.reflow(true);
		form.update();
	}

	/**
	 * Passes the focus to the view
	 */
	@Override
	public void setFocus() 
	{ 
		form.setFocus();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		String event = evt.getPropertyName();
		if("SICKPERSON_ADD".equalsIgnoreCase(event) ||
				"SICKPERSON_REMOVE".equalsIgnoreCase(event) ||
				"SICKPERSON_UPDATE".equalsIgnoreCase(event) ||
				"SICKPERSON_CLEARED".equalsIgnoreCase(event))
		{
			//just refresh the viewer
			viewer.refresh();
			infoLabel.setText("Es wurden "+ ModelFactory.getInstance().getSickPersonManager().getSickPersons().size() +" Patienten gefunden");
			infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("resource.info"));
		}
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions()
	{
		//create the action
		EditorNewSickPersonAction addAction = new EditorNewSickPersonAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());		
		RefreshViewAction refreshAction = new RefreshViewAction(SickPerson.ID);	
		//add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().add(refreshAction);
		form.getToolBarManager().update(true);
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
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
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

	/**
	 * Helper method to apply the filer
	 */
	public void inputChanged()
	{
		//get the values
		final String strLastname = lastname.getText().trim().toLowerCase();
		final String strFirstname = firstname.getText().trim().toLowerCase();
		final String strSVNR = svnr.getText().trim().toLowerCase();
		
		if(strLastname.length() < 1)
		{
			infoLabel.setText("Bitte geben Sie mindestens ein Zeichen des Nachnamens ein.");
			infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("resource.error"));
			Display.getCurrent().beep();
			return;
		}

		NetWrapper.getDefault().requestListing(SickPerson.ID,new QueryFilter(IFilterTypes.SICK_PERSON_LASTNAME_FILTER,strLastname));


		//filter the values
		viewer.getTable().setRedraw(false);
		Display.getDefault().asyncExec(new Runnable ()    
		{
			public void run ()       
			{
				//get the values and create the filter
				viewer.resetFilters();
				//create new filter and apply
				SickPersonAdminViewFilter filter = new SickPersonAdminViewFilter(strLastname,strFirstname,strSVNR);
				viewer.addFilter(filter);
			}
		});
		viewer.getTable().setRedraw(true);
	}
}