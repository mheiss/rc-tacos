package at.rc.tacos.server.controller;

import java.util.ArrayList;
import java.util.Date;

import at.rc.tacos.core.net.event.INetListener;
import at.rc.tacos.core.net.event.NetEvent;
import at.rc.tacos.core.net.internal.MyClient;

import at.rc.tacos.factory.*;
import at.rc.tacos.common.*;

/**
 * Handles the communication with a client
 * @author Michael
 */
public class ClientHandler implements INetListener
{
    @Override
    public synchronized void dataReceived(NetEvent ne)
    {
        //set up the factory to decode
        XMLFactory factory = new XMLFactory();
        System.out.println("Server - data received");
        factory.setupDecodeFactory(ne.getMessage());
        //decode the message
        ArrayList<AbstractMessage> objects = factory.decode();
        System.out.println("Server - after decode");
        //get the type of the item
        String type = factory.getType(); 
        String action = factory.getAction();
        String user = factory.getUserId();
        long timestamp = factory.getTimestamp();
        //pass the message
        handleNetMessage(user,timestamp, type, action, objects);
    }

    @Override
    public synchronized void dataTransferFailed(NetEvent ne)
    {
        System.out.println("Failed to send the message to the client");
        System.out.println("Message: "+ne.getMessage());
        System.out.println("Client: "+ne.getClient());
    }

    @Override
    public synchronized void socketStatusChanged(MyClient client, int status)
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
    private void handleNetMessage(String user,long timestamp,String type,String action,ArrayList<AbstractMessage> objects)
    {
        //write the action in the log table
        System.out.println("handle net message");
        //do variouse thinks like querying the database . . . .       
        
        //encode the message in xml again and brodcast ist
        sendMessage(user,type,action,objects);
    }
    
    /**
     * Encode the message and send it to the clients
     */
    private void sendMessage(String user,String type,String action,ArrayList<AbstractMessage> objects)
    {
        System.out.println("in sending");
        long now = new Date().getTime();
        //set up the factory
        XMLFactory factory = new XMLFactory();
        factory.setupEncodeFactory(user,now,type,action);
        //encode the message
        String xmlMessage = factory.encode(objects);
        System.out.println("Server sending message: " +xmlMessage);
        //send the message
        ServerController.getDefault().brodcastMessage(factory.encode(objects));
    }
}
