package at.rc.tacos.client.controller;

//rcp
import org.eclipse.jface.action.Action;
//client
import at.rc.tacos.client.model.*;
//net
import at.rc.tacos.core.net.*;

/**
 * The action that is triggered by creating a new item
 * @author Michael
 */
public class CreateItemAction extends Action 
{
    private String newItemId;

    public CreateItemAction(String id) 
    {
        this.newItemId = id;
    }
    
    public void run() 
    {
        Item newItem = new Item();
        newItem.setId(this.newItemId);
        NetWrapper.getDefault().sendMessage(newItem.toXML());
    }
}
