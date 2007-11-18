package at.rc.tacos.server.controller;

import java.util.ArrayList;
import java.util.Date;

import at.rc.tacos.core.net.event.INetListener;
import at.rc.tacos.core.net.event.NetEvent;
import at.rc.tacos.core.net.internal.MyClient;
import at.rc.tacos.core.service.ServiceWrapper;
import at.rc.tacos.core.xml.XMLFactory;
import at.rc.tacos.common.*;

/**
 * Handles the communication with a client
 * @author Michael
 */
public class ClientHandler implements INetListener
{
    @Override
    public void dataReceived(NetEvent ne)
    {
        //set up the factory to decode
        XMLFactory factory = new XMLFactory();
        System.out.println(ne.getMessage());
        factory.setupDecodeFactory(ne.getMessage());
        //decode the message
        ArrayList<AbstractMessage> objects = factory.decode();
        //get the type of the item
        String type = factory.getType(); 
        String action = factory.getAction();
        String user = factory.getUserId();
        long timestamp = factory.getTimestamp();
        //pass the message
        handleNetMessage(user,timestamp, type, action, objects);
    }

    @Override
    public void dataTransferFailed(NetEvent ne)
    {
        System.out.println("Failed to send the message to the client");
        System.out.println("Message: "+ne.getMessage());
        System.out.println("Client: "+ne.getClient());
    }

    @Override
    public void socketStatusChanged(MyClient client, int status)
    {
        //check the status
        if (status == IConnectionStates.STATE_CONNECTED)
        {
            System.out.println("New client connected");
            //create the streams and start the receive thread
            client.connect();
            //set the listener for netEvents
            client.addNetListener(this);
            //add the client to the list
            ServerController.getDefault().clientConnected(client);
        }
        if (status == IConnectionStates.STATE_DISCONNECTED)
        {
            System.out.println("Client quit");
            ServerController.getDefault().clientDisconnected(client);
        }
    }
    
    /**
     *  Manages the received objects and sets the needed actions
     */
    public void handleNetMessage(String user,long timestamp, String type,String action,ArrayList<AbstractMessage> objects)
    {
        //write the action in the log table
        
        //do variouse thinks like querying the database . . . .
        ServiceWrapper.getDefault().getDatabaseLayer().queryItem();
        
        
        //encode the message in xml again and brodcast ist
        XMLFactory factory = new XMLFactory();
        factory.setupEncodeFactory(
                user,
                new Date().getTime(),
                type,
                action,
                0);
        //encode
        ServerController.getDefault().brodcastMessage(factory.encode(objects));
    }
}
