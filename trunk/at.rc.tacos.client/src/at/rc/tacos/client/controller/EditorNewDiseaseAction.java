package at.rc.tacos.client.controller;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.editors.DiseaseEditor;
import at.rc.tacos.client.editors.DiseaseEditorInput;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Disease;

/**
 * Opens a new editor to create a disease
 * @author Michael
 */
public class EditorNewDiseaseAction extends Action
{
	private IWorkbenchWindow window;
	
	/**
	 * Default class constructor for creating the editor
	 */
	public EditorNewDiseaseAction(IWorkbenchWindow window)
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
       return "Öffnen ein neues Fenster um eine Erkrankung anzulegen";
   }

   /**
    * Retruns the text to show in the toolbar
    * @return the text to render
    */
   @Override
   public String getText()
   {
       return "Erkrankung hinzufügen";
   }

   /**
    * Returns the image to use for this action.
    * @return the image to use
    */
   @Override
   public ImageDescriptor getImageDescriptor() 
   {
       return ImageFactory.getInstance().getRegisteredImageDescriptor("admin.diseaseAdd");
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
       	Disease newDisease = new Disease();
       	//create the editor input
       	DiseaseEditorInput input = new DiseaseEditorInput(newDisease,true);
       	//try to open the editor
           page.openEditor(input, DiseaseEditor.ID);
       } 
       catch (PartInitException e) 
       {
           Activator.getDefault().log("Failed to create a new disease editor", IStatus.ERROR);
       }
   }
}
