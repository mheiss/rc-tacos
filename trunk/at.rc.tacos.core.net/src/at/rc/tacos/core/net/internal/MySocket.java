package at.rc.tacos.core.net.internal;

import java.net.*;
import java.io.*;
/**
 * Spezielle Socket Klasse<br>
 * @author Michael
 */
public class MySocket extends Socket 
{           
    //Streams for this socket
    protected PrintWriter out = null;
    protected BufferedReader in = null;

    /**
     * Constructor to create an empty socket object;
     */
    public MySocket()
    {   
        //create the object
        super(); 
        //initialize it
        init();
    }
    
    /**
     * Constructor to create a MyClient object with the given host and port values
     * @param host the host (name or address) to connect to
     * @param port the port number
     * @throws IOException when a io error occured during the socket init
     * @throws UnknownHostException when the host is unknown
     */
    public MySocket(String host,int port) throws IOException,UnknownHostException
    {
        //create the object
        super(host,port);
        //initialize it
        init();
    }
    
    /**
     * Initalize the socke, this will set an timeout of 100 mili seconds
     */
    private void init()
    {
        try
        {
            setSoTimeout(5000);
        }
        catch(SocketException se)
        {
            System.out.println("Error setting timeout for the socket");
        }
    }
    
    /**
     * Method to create the inputStream to receive data with.<br>
     * Creates an buffered stream reader.
     */
    public void createInputStream()
    {
        try
        {
            //assert we have a input stream
            if (in != null)
                return;
            //establish a reader
            in = new BufferedReader(new InputStreamReader(getInputStream()));    
        }
        catch(IOException ioe)
        {
            System.out.println("Failed to create a valid input stream");
        }   
    }
    
    /**
     * Method to create the output stream to send data through the socket<br>
     * Creates an buffered print writer stream.
     */
    public void createOutputStream()
    {
        try
        {
            //assert we have a input stream
            if (out != null)
                return;
            //establish a writer
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(getOutputStream())),true);
        }
        catch(IOException ioe)
        {
            System.out.println("Failed to create a valid output stream");
            out = null;
        }   
    }
           
    /**
     * Method to send data throught the socket
     * @param message the message to send 
     * @return true if the sending was successfully otherwise false
     */
    public boolean sendMessage(String message) 
    {
        //assert we have a valid output stream
        if (out != null)
        {
            out.flush();
            //check errors
            if (out.checkError())
            {
                System.out.println("Error while sending the message");
                return false;
            }
            //successfull
            return true;
        }
        else
        {
            System.out.println("No socket to send with");
            return false;
        }
    }

    /**
     * Method to reveive messages throught the socket.
     * @return The message that has been reveived throught the socket.
     * @throws IOException thrown when there is an error while reading the data
     * @throws NullPointerException thrown when there is no valid input stream
     */
    protected String receiveMessage() throws IOException, NullPointerException
    {      
        //read a line
        String tmp = in.readLine();
        //do we get a valid input?
        if (tmp == null)
            throw new NullPointerException();
        else
            return tmp;
    }
      
    /**
     * This method informs all that the socket is closed, closes all open streams and the socket.
     */
    public void cleanup()
    {
        //close the streams
        closeInputStream();
        closeOutputStream();
        //close the socket
        try
        {
            close();
        }
        catch(IOException ioe)
        {
            System.out.println("Failed to close socket.");
        }
    }
        
    /**
     * Method to close the input stream.
     */
    private void closeInputStream()
    {
        if (in != null)
        {
            try
            {
                in.close();
                in = null;
            }
            catch(IOException ioe)
            {
                System.out.println("Failed to close inputStream");
            }
        }
    }
    
    /**
     * Method to close the output stream
     */
    private void closeOutputStream()
    {
        if (out != null)
        {
            out.close();
            out = null;
        }
    }
    
    //SETTERS AND GETTERS
    /**
     * Method to get the current socket
     * @return the socket object
     */
    public MySocket getSocket()
    {
        return this;
    }
    
    /**
     * Method to determine the IP adress of the socket.
     * @return the ip address
     */
    public String getHost()
    {
        return getInetAddress().getHostAddress();
    }
}
