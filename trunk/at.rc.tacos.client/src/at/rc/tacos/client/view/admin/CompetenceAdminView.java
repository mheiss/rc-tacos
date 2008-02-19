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
import at.rc.tacos.client.controller.EditorNewCompetenceAction;
import at.rc.tacos.client.controller.RefreshViewAction;
import at.rc.tacos.client.editors.CompetenceEditor;
import at.rc.tacos.client.editors.CompetenceEditorInput;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.CompetenceContentProvider;
import at.rc.tacos.client.providers.CompetenceLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.Competence;

public class CompetenceAdminView extends ViewPart implements PropertyChangeListener
{
    public static final String ID = "at.rc.tacos.client.view.admin.competenceAdminView";  
    
    //properties
    private TableViewer viewer;
    private FormToolkit toolkit;
    private ScrolledForm form;
    
	/**
     * Default class constructor
     */
    public CompetenceAdminView()
    {
    	ModelFactory.getInstance().getCompetenceList().addPropertyChangeListener(this);
    }
    
    /**
     * Cleanup the view
     */
    @Override
    public void dispose()
    {
    	ModelFactory.getInstance().getCompetenceList().removePropertyChangeListener(this);
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
        form.setText("Liste der Kompetenzen"); 
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
            	//get the selected competence
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                Competence competence = (Competence)obj;
                //create the editor input and open
                CompetenceEditorInput input = new CompetenceEditorInput(competence,false);
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try 
                {
                    page.openEditor(input, CompetenceEditor.ID);
                } 
                catch (PartInitException e) 
                {
                    Activator.getDefault().log("Failed to open the editor for the competence "+competence, IStatus.ERROR);
                }
            }
        });
        viewer.setContentProvider(new CompetenceContentProvider());
        viewer.setLabelProvider(new CompetenceLabelProvider());
        viewer.setInput(ModelFactory.getInstance().getCompetenceList().toArray());
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
		if("COMPETENCE_ADD".equalsIgnoreCase(event) ||
				"COMPETENCE_REMOVE".equalsIgnoreCase(event) ||
				"COMPETENCE_UPDATE".equalsIgnoreCase(event) ||
				"COMPETENCE_CLEARED".equalsIgnoreCase(event))
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
		EditorNewCompetenceAction addAction = new EditorNewCompetenceAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());		
		RefreshViewAction viewAction = new RefreshViewAction(Competence.ID);
		//add to the toolbar
		form.getToolBarManager().add(addAction);
		form.getToolBarManager().add(viewAction);
		form.getToolBarManager().update(true);
	}
}