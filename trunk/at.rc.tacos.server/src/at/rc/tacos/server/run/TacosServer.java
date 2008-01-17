package at.rc.tacos.server.run;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import at.rc.tacos.core.net.internal.MyServer;
import at.rc.tacos.server.controller.ClientHandler;
import at.rc.tacos.server.controller.ServerController;

/**
 * Tacos server
 * @author Michael
 */
public class TacosServer
{
    //path for the properties file
    public final static String SERVER_CONFIG = "at.rc.tacos.server.config.server";

    //the server object
    private MyServer myServer = null;

    public TacosServer(int port)
    {
        ServerController.getDefault();
        //listen for client request
        myServer = new MyServer(port);
        myServer.addNetListener(new ClientHandler());
    }
    
    /**
     * Starts the server thread to listen for client requests.
     */
    private void startServer()
    {
        //start the server thread to listen to client connections
        Thread t = new Thread(myServer);
        t.start();
    }

    /**
     * The main method to read the configuration and start the server
     * @param args - not used
     */
    public static void main(String[] args)  
    {  
        try
        {
            //load the settings from the file
            String strPort = ResourceBundle.getBundle(SERVER_CONFIG).getString("server.port");
            int port = -1;
            //parse
            port = Integer.parseInt(strPort);
            //start the server
            System.out.println("TACOS-Server Build: 18.01.2008");
            System.out.println("Listening for client request at port: "+port);
            System.out.println("-------------------------------------------");
            TacosServer server = new TacosServer(port);
            server.startServer();
            
        }
        catch(MissingResourceException mre)
        {
            System.out.println("Missing resource, cannot init startup server");
            System.out.println(mre.getMessage());
        }
        catch(NumberFormatException nfe)
        {
            System.out.println("Port number must be a integer");
            System.out.println(nfe.getMessage());
        }
        catch(NullPointerException npe)
        {
            System.out.println("Configuration file for the server is missing");
            System.out.println(npe.getMessage());
            npe.printStackTrace();
        }
    }
}
