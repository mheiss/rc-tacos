package at.rc.tacos.client.view.admin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
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
import at.rc.tacos.client.controller.EditorNewStaffAction;
import at.rc.tacos.client.editors.StaffMemberEditor;
import at.rc.tacos.client.editors.StaffMemberEditorInput;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.StaffMemberContentProvider;
import at.rc.tacos.client.providers.StaffMemberLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.StaffMember;

public class StaffMemberAdminView extends ViewPart implements PropertyChangeListener
{
    public static final String ID = "at.rc.tacos.client.view.admin.staffMemberAdminView";  
    
    //properties
    private TableViewer viewer;
    private FormToolkit toolkit;
    private ScrolledForm form;
    
    /**
     * Default class constructor
     */
    public StaffMemberAdminView()
    {
    	ModelFactory.getInstance().getStaffList().addPropertyChangeListener(this);
    }
    
    /**
     * Cleanup the view
     */
    @Override
    public void dispose()
    {
    	ModelFactory.getInstance().getStaffList().removePropertyChangeListener(this);
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
        form.setText("Liste der Mitarbeiter"); 
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
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                StaffMember member = (StaffMember)obj;
                Login login = ModelFactory.getInstance().getLoginList().getLoginByUsername(member.getUserName());
                //assert valid
                if(login == null)
                {
                	Activator.getDefault().log("Failed to open the editor for the staff member "+member +
                			"\n The login object for the user is null", IStatus.ERROR);
                	MessageDialog.openError(getSite().getShell(),
                			"Fehler beim ändern von "+member.getUserName(),
                			"Der Mitarbeiter" +member.getFirstName()+ " " + member.getLastName() +" kann nicht editiert werden");
                }
                
                StaffMemberEditorInput input = new StaffMemberEditorInput(member,login,false);
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try 
                {
                    page.openEditor(input, StaffMemberEditor.ID);
                } 
                catch (PartInitException e) 
                {
                    Activator.getDefault().log("Failed to open the editor for the staff member "+member, IStatus.ERROR);
                }
            }
        });
        viewer.setContentProvider(new StaffMemberContentProvider());
        viewer.setLabelProvider(new StaffMemberLabelProvider());
        viewer.setInput(ModelFactory.getInstance().getStaffList().getStaffList());
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
		if("STAFF_CLEARED".equalsIgnoreCase(event) ||
				"STAFF_UPDATE".equalsIgnoreCase(event) ||
				"STAFF_REMOVE".equalsIgnoreCase(event) ||
				"STAFF_ADD".equalsIgnoreCase(event))
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
		EditorNewStaffAction addAction = new EditorNewStaffAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());		
		//add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().update(true);
	}
}