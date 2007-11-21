package at.rc.tacos.server.run;

import java.util.MissingResourceException;
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
        try
        {
            //load the settings from the file
            String strPort = ResourceBundle.getBundle(SERVER_CONFIG).getString("server.port");
            int port = -1;
            //parse
            port = Integer.parseInt(strPort);
            //start the server
            System.out.print("Startup server. . . ");
            ServerController.getDefault().startServer(port);
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
        }
    }
}
