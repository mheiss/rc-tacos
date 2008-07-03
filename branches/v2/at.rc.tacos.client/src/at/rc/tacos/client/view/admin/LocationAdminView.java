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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.controller.EditorNewLocationAction;
import at.rc.tacos.client.controller.RefreshViewAction;
import at.rc.tacos.client.editors.LocationEditor;
import at.rc.tacos.client.editors.LocationEditorInput;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.providers.StationContentProvider;
import at.rc.tacos.client.providers.StationLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.Location;

public class LocationAdminView extends ViewPart implements PropertyChangeListener
{
    public static final String ID = "at.rc.tacos.client.view.admin.locationAdminView";  
    
    //properties
    private TableViewer viewer;
    private FormToolkit toolkit;
    private ScrolledForm form;
    
    /**
     * Default class constructor
     */
    public LocationAdminView()
    {
    	ModelFactory.getInstance().getLocationManager().addPropertyChangeListener(this);
    }
    
    /**
     * Cleanup the view
     */
    @Override
    public void dispose()
    {
    	ModelFactory.getInstance().getLocationManager().removePropertyChangeListener(this);
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
        form.setText("Liste der Dienststellen"); 
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
        
        viewer = new TableViewer(form.getBody(), SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.getTable().setLayout(new GridLayout());
        viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        viewer.addDoubleClickListener(new IDoubleClickListener()
        {
            @Override
            public void doubleClick(DoubleClickEvent dce) 
            {
            	//get the selected location
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                Location location = (Location)obj;
                //create the editor input and open
                LocationEditorInput input = new LocationEditorInput(location,false);
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try 
                {
                    page.openEditor(input, LocationEditor.ID);
                } 
                catch (PartInitException e) 
                {
                    Activator.getDefault().log("Failed to open the editor for the location "+location, IStatus.ERROR);
                }
            }
        });
        viewer.setContentProvider(new StationContentProvider());
        viewer.setLabelProvider(new StationLabelProvider());
        viewer.setInput(ModelFactory.getInstance().getLocationManager().toArray());
        getViewSite().setSelectionProvider(viewer);
        
        //add actions to the toolbar
        createToolBarActions();
        
        //set this table as a selection provider
        getViewSite().setSelectionProvider(viewer);
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
		if("LOCATION_ADD".equalsIgnoreCase(event) ||
				"LOCATION_REMOVE".equalsIgnoreCase(event) ||
				"LOCATION_UPDATE".equalsIgnoreCase(event) ||
				"LOCATION_CLEARED".equalsIgnoreCase(event))
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
		EditorNewLocationAction addAction = new EditorNewLocationAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());		
		RefreshViewAction viewAction = new RefreshViewAction(Location.ID);
		//add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().add(viewAction);
		form.getToolBarManager().update(true);
	}
}