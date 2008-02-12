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
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.editors.StaffMemberEditor;
import at.rc.tacos.client.editors.StaffMemberEditorInput;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.StaffMemberContentProvider;
import at.rc.tacos.client.providers.StaffMemberLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.StaffMember;

public class StaffMemberView extends ViewPart implements PropertyChangeListener
{
    public static final String ID = "at.rc.tacos.client.view.admin.staffMemberView";  
    
    //properties
    private TableViewer viewer;
    private FormToolkit toolkit;
    private ScrolledForm form;
    
    /**
     * Default class constructor
     */
    public StaffMemberView()
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
        final Composite comp = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(1, false);
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        comp.setLayout(layout);

        toolkit = new FormToolkit(CustomColors.FORM_COLOR(comp.getDisplay()));
        form = this.toolkit.createScrolledForm(comp);
        layout = new GridLayout(1, false);
        form.getBody().setLayout(layout);

        form.setText("Liste der Mitarbeiter"); 
        toolkit.decorateFormHeading(this.form.getForm());
     
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true ,true);

        final Composite client = this.toolkit.createComposite(this.form.getBody(), SWT.WRAP);
        layout = new GridLayout(1, false);
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        client.setLayoutData(gd);
        form.setLayout(layout);
        
        final Table browseTree = new Table(client, SWT.V_SCROLL);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        browseTree.setLayoutData(gd);
        viewer = new TableViewer(browseTree);
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
        form.setLayoutData(gd);
        
        //set this table as a selection provider
        getViewSite().setSelectionProvider(viewer);
    }

    /**
     * Passes the focus to the view
     */
    @Override
    public void setFocus() { }

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
}