package at.rc.tacos.core.net.internal;

import java.io.*;
import java.net.*;

/**
 * Specialized server socket class to connect with a custom
 * implementation of a client socket.
 * @author Michael
 */
public class MyServerSocket extends ServerSocket 
{
    public MyServerSocket(int port) throws IOException 
    {
        super(port);
    }
    
    //Override the ServerSocket methode accept() 
    //It is overriden to initiate a custom Socket (MySocket) instad of a standard socket object
    @Override
    public MySocket accept() throws IOException
    {
        MySocket s = new MySocket();
        //this methode waits until a new client is connected
        implAccept(s);
        return s;
    }
}
