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
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.editors.MobilePhoneEditor;
import at.rc.tacos.client.editors.MobilePhoneEditorInput;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.MobilePhoneContentProvider;
import at.rc.tacos.client.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.MobilePhoneDetail;

public class PhoneAdminView extends ViewPart implements PropertyChangeListener
{
    public static final String ID = "at.rc.tacos.client.view.admin.phoneAdminView";  
    
    //properties
    private TableViewer viewer;
    private FormToolkit toolkit;
    private ScrolledForm form;
    
    /**
     * Default class constructor
     */
    public PhoneAdminView()
    {
    	ModelFactory.getInstance().getPhoneList().addPropertyChangeListener(this);
    }
    
    /**
     * Cleanup the view
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

        form.setText("Liste der Mobiltelefone"); 
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
                MobilePhoneDetail phone = (MobilePhoneDetail)obj;
                MobilePhoneEditorInput input = new MobilePhoneEditorInput(phone);
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try 
                {
                    page.openEditor(input, MobilePhoneEditor.ID);
                    IWorkbenchPart active = page.getActivePart();
                    ((MobilePhoneEditor)active).selectionChanged(active, selection);
                } 
                catch (PartInitException e) 
                {
                    Activator.getDefault().log("Failed to open the editor for the mobile phone "+phone, IStatus.ERROR);
                }
            }
        });
        viewer.setContentProvider(new MobilePhoneContentProvider());
        viewer.setLabelProvider(new MobilePhoneLabelProvider());
        viewer.setInput(ModelFactory.getInstance().getPhoneList().getMobilePhoneList());
        getViewSite().setSelectionProvider(this.viewer);
        form.setLayoutData(gd);
        
        //set this table as a selection provider
        getViewSite().setSelectionProvider(this.viewer);
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
		if("PHONE_CLEAR".equalsIgnoreCase(event) ||
				"PHONE_UPDATE".equalsIgnoreCase(event) ||
				"PHONE_REMOVE".equalsIgnoreCase(event) ||
				"PHONE_ADD".equalsIgnoreCase(event))
		{
			//just refresh the viewer
			viewer.refresh();
		}
	}
}