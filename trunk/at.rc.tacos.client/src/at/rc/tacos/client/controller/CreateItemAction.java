package at.rc.tacos.client.controller;

//rcp
import org.eclipse.jface.action.Action;
//client
import at.rc.tacos.core.model.*;
import at.rc.tacos.core.service.ServiceWrapper;

/**
 * The action that is triggered by creating a new item
 * @author Michael
 */
public class CreateItemAction extends Action 
{
    private String id;

    public CreateItemAction(String id) 
    {
        this.id = id;
    }
    
    public void run() 
    {
        Item newItem = new Item(id);
        System.out.println("Create item action");
        ServiceWrapper.getDefault().getNetService().requestAddItem(newItem.getId());
    }
}
