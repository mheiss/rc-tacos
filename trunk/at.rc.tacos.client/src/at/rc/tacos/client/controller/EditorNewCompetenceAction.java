package at.rc.tacos.client.controller;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import at.rc.tacos.client.Activator;
import at.rc.tacos.client.editors.CompetenceEditor;
import at.rc.tacos.client.editors.CompetenceEditorInput;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Competence;

/**
 * Opens an empty editor to insert a new competence.
 * @author Michael
 */
public class EditorNewCompetenceAction extends Action
{
	private IWorkbenchWindow window;
	
	/**
     * Default class constructor for creating the editor
     */
    public EditorNewCompetenceAction(IWorkbenchWindow window)
	{
		this.window = window;
	}

    /**
     * Returns the tooltip text for the action
     * @return the tooltip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Öffnen ein neues Fenster um eine Kompetenz anzulegen";
    }

    /**
     * Retruns the text to show in the toolbar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Kompetenz hinzufügen";
    }

    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("admin.create");
    }

    /**
     * Opens the editor to create the competence
     */
    @Override
    public void run()
    {
    	IWorkbenchPage page = window.getActivePage();
        try 
        {
        	//create a new competence
        	Competence newCompetence = new Competence();
        	//create the editor input
        	CompetenceEditorInput input = new CompetenceEditorInput(newCompetence,true);
        	//try to open the editor
            page.openEditor(input, CompetenceEditor.ID);
        } 
        catch (PartInitException e) 
        {
            Activator.getDefault().log("Failed to create a new competence editor", IStatus.ERROR);
        }
    }
}