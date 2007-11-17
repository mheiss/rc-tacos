package at.rc.tacos.client.view;

//rcp
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
//client
import at.rc.tacos.client.*;
//service
import at.rc.tacos.model.*;

public class View extends ViewPart implements PropertyChangeListener 
{
    //the ID of the view
    public static final String ID = "at.rc.tacos.client.view.item_view"; 

	//the table to show the data
	private TableViewer viewer;
	
	class ViewContentProvider implements IStructuredContentProvider 
	{
		public void inputChanged(Viewer v, Object oldInput, Object newInput) 
		{
            // do nothing
		}

		public void dispose() 
		{
            // do nothing
		}

		public Object[] getElements(Object parent) 
		{
			return Activator.getDefault().getItemList().toArray();
		}
	}

	class ViewLabelProvider extends LabelProvider 
	{
		public Image getImage(Object obj) 
		{
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
        public String getText(Object element) 
        {
            return ((Item)element).getName();
        }
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) 
	{
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(Activator.getDefault().getItemList());
        // add listener to model to keep on track. 
        Activator.getDefault().getItemList().addPropertyChangeListener(this);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() 
	{
		this.viewer.getControl().setFocus();
	}
	
    /**
     * Remove all listeners and hide the visible views
     */
    public void dispose() 
    {
        // deregister listener
        Activator.getDefault().getItemList().removePropertyChangeListener(this);
        super.dispose();
    }

   /**
    * Notification that the data model has changed so the view
    * must be updated.
    * @param evt the fired property event
    */
    public void propertyChange(PropertyChangeEvent evt) 
    {
        // the viewer represents simple model. refresh should be enough.
        if ("ITEM_ADD".equals(evt.getPropertyName())) 
        { 
            viewer.refresh();
        }
        // event on deletion --> also just refresh
        if ("ITEM_REMOVE".equals(evt.getPropertyName())) 
        { 
            viewer.refresh();
        }
    }
}