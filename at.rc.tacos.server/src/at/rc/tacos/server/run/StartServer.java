package at.rc.tacos.server.run;

import at.rc.tacos.server.controller.ServerController;

/**
 * Tacos server
 * @author Michael
 */
public class StartServer
{
    public static void main(String[] args)  
    {  
        int port = 0;
        //get the port
        if (args.length == 0 )
        {
            System.out.println("Cannot start the Server, port number required");
            System.out.println("Usage: Server <PORT>");
            System.exit(0);
        }
        try
        {
            port = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException nfe)
        {
            System.out.println("This is not a valid number");
            System.exit(0);
        } 

        System.out.print("Startup server. . . ");
        ServerController serverController = new ServerController(port);
        serverController.startServer();
    }
}
