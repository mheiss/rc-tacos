package at.rc.tacos.server.run;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import at.rc.tacos.core.db.DataSource;
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
    //the logging instance
    private static Logger logger = Logger.getRootLogger();

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
        	PropertyConfigurator.configureAndWatch("conf/log4j.properties", 60*1000 );
            //load the settings from the file
            String strPort = ResourceBundle.getBundle(SERVER_CONFIG).getString("server.port");
            int port = -1;
            //parse
            port = Integer.parseInt(strPort);
            //start the server
            logger.info("TACOS-Server Build: 16.03.2008"); 
            logger.info("Open a connection to the database server");
            //try to get a connection to the database
            if(DataSource.getInstance().getConnection() == null)
            {
            	logger.error("Failed to connect to the database");
            	logger.error("Shuting down the server");
                System.exit(1);
            }
            logger.info("Listening for client request at port: "+port);
            TacosServer server = new TacosServer(port);
            server.startServer();
            
        }
        catch(MissingResourceException mre)
        {
        	logger.error("Missing resource, cannot init startup server");
        	logger.error(mre.getMessage());
        }
        catch(NumberFormatException nfe)
        {
        	logger.info("Port number must be a integer");
        	logger.info(nfe.getMessage());
        }
        catch(NullPointerException npe)
        {
        	logger.info("Configuration file for the server is missing");
        	logger.info(npe.getMessage());
            npe.printStackTrace();
        }
    }
}
