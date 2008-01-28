package at.rc.tacos.client.view.admin;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.StaffMemberContentProvider;
import at.rc.tacos.client.providers.StaffMemberLabelProvider;
import at.rc.tacos.client.util.CustomColors;

public class StaffMemberView extends ViewPart
{
    public static final String ID = "at.rc.tacos.client.view.admin.staffMemberView";  
    
    //properties
    private TableViewer viewer;
    private FormToolkit toolkit;
    private ScrolledForm form;

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

        this.toolkit = new FormToolkit(CustomColors.FORM_COLOR(comp.getDisplay()));
        this.form = this.toolkit.createScrolledForm(comp);
        layout = new GridLayout(1, false);
        this.form.getBody().setLayout(layout);

        this.form.setText("Liste der registrierten Benutzer"); 
        this.toolkit.decorateFormHeading(this.form.getForm());
     
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true ,true);

        final Composite client = this.toolkit.createComposite(this.form.getBody(), SWT.WRAP);
        layout = new GridLayout(1, false);
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        client.setLayout(layout);
        client.setLayoutData(gd);
        this.form.setLayout(layout);
        
        final Table browseTree = new Table(client, SWT.V_SCROLL);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        browseTree.setLayoutData(gd);
        this.viewer = new TableViewer(browseTree);
       
        this.viewer.setContentProvider(new StaffMemberContentProvider());
        this.viewer.setLabelProvider(new StaffMemberLabelProvider());
        this.viewer.setInput(ModelFactory.getInstance().getStaffList().getStaffList());
        getViewSite().setSelectionProvider(this.viewer);
        this.form.setLayoutData(gd);
        
        //set this table as a selection provider
        getViewSite().setSelectionProvider(this.viewer);
    }

    /**
     * Passes the focus to the view
     */
    @Override
    public void setFocus() { }
}