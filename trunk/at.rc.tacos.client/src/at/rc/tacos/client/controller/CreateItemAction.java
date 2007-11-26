package at.rc.tacos.client.controller;

//rcp
import org.eclipse.jface.action.Action;

//client
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.*;

/**
 * The action that is triggered by creating a new item
 * @author Michael
 */
public class CreateItemAction extends Action 
{
    private String id;
    
    public CreateItemAction()
    {
        this.id = "item";
    }

    public CreateItemAction(String id) 
    {
        this.id = id;
    }
    
    public void run() 
    {
    	//Item
        Item newItem = new Item(id);
        //send
        NetWrapper.getDefault().sendAddMessage(newItem);
    }
}
