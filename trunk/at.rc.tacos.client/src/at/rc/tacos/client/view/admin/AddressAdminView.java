package at.rc.tacos.client.view.admin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import at.rc.tacos.client.Activator;
import at.rc.tacos.client.controller.EditorNewAddressAction;
import at.rc.tacos.client.controller.ImportAddressAction;
import at.rc.tacos.client.controller.RefreshViewAction;
import at.rc.tacos.client.editors.AddressEditor;
import at.rc.tacos.client.editors.AddressEditorInput;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.AddressAdminViewFilter;
import at.rc.tacos.client.providers.AddressContentProvider;
import at.rc.tacos.client.providers.AddressLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.AddressViewSorter;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Address;

public class AddressAdminView  extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.view.admin.addressAdminView";  

	//properties
	private TableViewer viewer;
	private FormToolkit toolkit;
	private ScrolledForm form;
	//text fields for the filter
	private Text zip,city,street;
	
	//to apply the filter
	private ImageHyperlink applyFilter,resetFilter;

	/**
	 * Default class constructor
	 */
	public AddressAdminView()
	{
		ModelFactory.getInstance().getAddressList().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose()
	{
		ModelFactory.getInstance().getAddressList().removePropertyChangeListener(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{
		//the scrolled form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Liste der Adressen"); 
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
		final Label labelStreet = toolkit.createLabel(filter, "Straﬂe");
		street = toolkit.createText(filter, "");
		
		//the city
		final Label labelCity = toolkit.createLabel(filter, "Stadt");
		city = toolkit.createText(filter, "");

		//the zip code
		final Label labelZip = toolkit.createLabel(filter, "GKZ");
		zip = toolkit.createText(filter, "");
		
		//Create the hyperlink to import the data
		applyFilter = toolkit.createImageHyperlink(filter, SWT.NONE);
		applyFilter.setText("Adresstabelle filtern");
		applyFilter.setImage(ImageFactory.getInstance().getRegisteredImage("resource.import"));
		applyFilter.addHyperlinkListener(new HyperlinkAdapter() 
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				inputChanged();
			}
		});

		//create the hyperlink to add a new job
		resetFilter = toolkit.createImageHyperlink(filter, SWT.NONE);
		resetFilter.setText("Einschr‰nkungen entfernen");
		resetFilter.setImage(ImageFactory.getInstance().getRegisteredImage("admin.addressAdd"));
		resetFilter.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				//reset the fields
				street.setText("");
				city.setText("");
				zip.setText("");
				//apply the filter
				inputChanged();
			}
		});

		//create the section to hold the table
		Composite tableComp = createSection(form.getBody(), "Filter") ;
		Table table = new Table(tableComp, SWT.SINGLE | SWT.BORDER);
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
				Address address = (Address)obj;
				//create the editor input and open
				AddressEditorInput input = new AddressEditorInput(address,false);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try 
				{
					page.openEditor(input, AddressEditor.ID);
				} 
				catch (PartInitException e) 
				{
					Activator.getDefault().log("Failed to open the editor for the address "+address, IStatus.ERROR);
				}
			}
		});
		viewer.setContentProvider(new AddressContentProvider());
		viewer.setLabelProvider(new AddressLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getAddressList().toArray());
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		getViewSite().setSelectionProvider(viewer);
		
		//create the columns
		final TableColumn imageColumn = new TableColumn(table, SWT.NONE);
		imageColumn.setToolTipText("");
		imageColumn.setWidth(30);
		imageColumn.setText("");
		
		final TableColumn zipColumn = new TableColumn(table, SWT.NONE);
		zipColumn.setToolTipText("Gemeindekennzeichen");
		zipColumn.setWidth(60);
		zipColumn.setText("GKZ");

		final TableColumn cityColumn = new TableColumn(table, SWT.NONE);
		cityColumn.setToolTipText("Name der Stadt");
		cityColumn.setWidth(180);
		cityColumn.setText("Stadt");

		final TableColumn streetColumn = new TableColumn(table, SWT.NONE);
		streetColumn.setToolTipText("Name der Straﬂe");
		streetColumn.setWidth(180);
		streetColumn.setText("Straﬂe");
		
		//make the columns sortable
		Listener sortListener = new Listener() 
		{
			public void handleEvent(Event e) 
			{
				// determine new sort column and direction
				TableColumn sortColumn = viewer.getTable().getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = viewer.getTable().getSortDirection();
				//revert the sortorder if the column is the same
				if (sortColumn == currentColumn) 
				{
					if(dir == SWT.UP)
						dir = SWT.DOWN;
					else
						dir = SWT.UP;
				} 
				else 
				{
					viewer.getTable().setSortColumn(currentColumn);
					dir = SWT.UP;
				}
				// sort the data based on column and direction
				String sortIdentifier = null;
				if (currentColumn == zipColumn) 
					sortIdentifier = AddressViewSorter.ZIP_SORTER;
				if (currentColumn == cityColumn) 
					sortIdentifier = AddressViewSorter.CITY_SORTER;
				if (currentColumn == streetColumn) 
					sortIdentifier = AddressViewSorter.STREET_SORTER;
				//apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new AddressViewSorter(sortIdentifier,dir));
			}
		};

		//attach the listener
		zipColumn.addListener(SWT.Selection, sortListener);
		cityColumn.addListener(SWT.Selection, sortListener);
		streetColumn.addListener(SWT.Selection, sortListener);

		//add actions to the toolbar
		createToolBarActions();

		//set this table as a selection provider
		getViewSite().setSelectionProvider(viewer);

		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 80;
		labelStreet.setLayoutData(data);
		data = new GridData();
		data.widthHint = 80;
		labelCity.setLayoutData(data);
		data.widthHint = 80;
		labelZip.setLayoutData(data);
		labelCity.setLayoutData(data);
		data.widthHint = 80;
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		street.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		city.setLayoutData(data2);	
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		zip.setLayoutData(data2);	
		data2 = new GridData(GridData.FILL_BOTH);
		viewer.getTable().setLayoutData(data2);
		//the section of the table
		data2 = new GridData(GridData.FILL_BOTH);
		Section tableSection = (Section)tableComp.getParent();
		tableSection.setLayoutData(data2);
		
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
		if("ADDRESS_ADD".equalsIgnoreCase(event) ||
				"ADDRESS_REMOVE".equalsIgnoreCase(event) ||
				"ADDRESS_UPDATE".equalsIgnoreCase(event) ||
				"ADDRESS_CLEARED".equalsIgnoreCase(event) ||
				"ADDRESS_ADD_ALL".equalsIgnoreCase(event))
		{
			//just refresh the viewer
			viewer.refresh();
		}
	}

	/**
	 * Creates and adds the actions for the toolbar
	 */
	private void createToolBarActions()
	{
		//create the action
		EditorNewAddressAction addAction = new EditorNewAddressAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());		
		RefreshViewAction refreshAction = new RefreshViewAction(Address.ID);
		ImportAddressAction importAction = new ImportAddressAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());	
		//add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().add(refreshAction);
		form.getToolBarManager().add(importAction);
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
		Section section = toolkit.createSection(parent,ExpandableComposite.TITLE_BAR | Section.TWISTIE);
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
		WorkspaceJob job = new WorkspaceJob("AddressMonitor")
		{
			@Override
			public IStatus runInWorkspace(IProgressMonitor arg0) throws CoreException 
			{
				//get the values and create the filter
				viewer.resetFilters();
				//create new filter and apply
				AddressAdminViewFilter filter = new AddressAdminViewFilter(street.getText(),city.getText(),zip.getText());
				viewer.addFilter(filter);
				return Status.OK_STATUS;
			}
		};
		job.addJobChangeListener(new JobChangeAdapter() 
		{
			public void done(IJobChangeEvent event) 
			{
				if (!event.getResult().isOK())
					Activator.getDefault().log("Failed to filter the addresses",IStatus.ERROR);
			}
		});
		job.setUser(true);
		job.schedule();
	}
}