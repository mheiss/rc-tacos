package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
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
        //send
        NetWrapper.getDefault().sendAddMessage(Item.ID,new Item(id));        
    }
}
