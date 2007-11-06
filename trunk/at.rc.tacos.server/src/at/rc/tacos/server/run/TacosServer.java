package at.rc.tacos.server.run;

import java.util.ResourceBundle;
import at.rc.tacos.server.controller.ServerController;

/**
 * Tacos server
 * @author Michael
 */
public class TacosServer
{
    //path for the properties file
    public final static String SERVER_CONFIG = "at.rc.tacos.server.config.server";
    
    public static void main(String[] args)  
    {  
        int port = 0;
        //get the port out of the properties
        ResourceBundle bundle = ResourceBundle.getBundle(SERVER_CONFIG);
        //assert valid
        if (bundle == null)
        {
            System.out.println("Cant find server.properties file");
            System.exit(0); 
        }
        //load the port
        String strPort = bundle.getString("server.port");
        try
        {
            port = Integer.parseInt(strPort);
        }
        catch(NumberFormatException nfe)
        {
            System.out.println("Invalid argument for port");
            System.exit(0);
        } 
        //start the server
        System.out.print("Startup server. . . ");
        ServerController.getDefault().startServer(port);
    }
}
