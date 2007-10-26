package at.rc.tacos.client.controller;

//rcp
import org.eclipse.swt.widgets.Display;
//client
import at.rc.tacos.client.*;
import at.rc.tacos.client.model.*;
//net-plugin
import at.rc.tacos.core.net.*;
import at.rc.tacos.core.net.event.*;

/**
 * Handles the network communication
 * @author Michael
 */
public class NetController implements INetListener,IConnectionStates
{
    /**
     * Default class constructor
     */
    public NetController() { }
    
    /**
     * Notification that new data is available.<br>
     * The <code>MyClient</code> forwards this message to notify the listeners that new data is available.
     * @param ne the triggered net event
     */
    @Override
    public void dataReceived(NetEvent ne)
    {
        final Item item = new Item(ne.getMessage());
        //get the current display
        Display.getDefault().syncExec(new Runnable () 
        {
            public void run () 
            {
                Activator.getDefault().getObjectList().add(item);
            }
        });
    }

    /**
     * Notification that the status of the socket changed.<br>
     * <p>The <code>MyClient</code> forwards this message to notify
     * the listeners that the connection status of the socket has changed.<br>
     * @param status the new status of the socket.
     **/
    @Override
    public void socketStatusChanged(MySocket s, int status)
    {
        
            
    }
}
