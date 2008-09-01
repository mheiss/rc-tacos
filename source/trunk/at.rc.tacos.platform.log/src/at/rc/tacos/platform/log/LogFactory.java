package at.rc.tacos.platform.log;

import org.eclipse.core.runtime.Status;

/**
 * This class provides logging capabilities for other plugins
 * @author Michael
 */
public class LogFactory 
{
	/**
	 * Default class constructor
	 */
	private LogFactory()
	{
		//prevent instantiation
	}	
	
	public static void log(String pluginId,int severity,String message)
	{
		//log witht the build in loggin mechanism
		Status status = new Status(severity,pluginId,message);
		Activator.getDefault().getLog().log(status);
	}
}
