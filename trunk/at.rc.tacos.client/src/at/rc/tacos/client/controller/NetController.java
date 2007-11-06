package at.rc.tacos.client.controller;

import org.eclipse.swt.widgets.Display;

import at.rc.tacos.client.Activator;
import at.rc.tacos.common.INetClientLayer;
import at.rc.tacos.core.model.*;

/**
 * Manages the update of the model.
 * @author Michael
 */
public class NetController implements INetClientLayer
{
    /**
     * Default class constructor
     */
    public NetController() {}

    //OVERRIDEN METHODS 
    @Override
    public void itemAdded(String item)
    {
       final Item newItem = new Item(item);
       System.out.println(item + " added");
       //Update the UI
       Display.getDefault().syncExec(new Runnable ()    
       {
           public void run ()        
           {
               Activator.getDefault().getItemList().add(newItem);
           }
        }); 
       
    }
}
