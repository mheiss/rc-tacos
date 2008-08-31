package at.rc.tacos.server.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.server.Activator;
import at.rc.tacos.server.net.NetWrapper;
import at.rc.tacos.server.preferences.PreferenceConstants;

/**
 * Wraps the network connection into a thread to provide monitoring
 * @author Michael
 */
public class NetConnectionJob extends Job
{
	/**
	 * Default class constructor
	 */
	public NetConnectionJob()
	{
		super("NetConnectionJob");
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		//create and init the job
		monitor.beginTask("Netzwerk wird initialisiert", IProgressMonitor.UNKNOWN);
		
		//Load the preferences
		Integer clientListenPort = Activator.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_CLIENT_PORT);
		Integer serverListenPort = Activator.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_SERVER_PORT);
		String failoverHost = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_FAILOVER_HOST);
		Integer failoverServerPort = Activator.getDefault().getPluginPreferences().getInt(PreferenceConstants.P_FAILOVER_SERVER_PORT);
		
		//try to startup the server
		try
		{
			NetWrapper.getDefault().init(clientListenPort,serverListenPort,failoverHost,failoverServerPort);
			NetWrapper.getDefault().startServer(monitor);
			return Status.OK_STATUS;
		}
		catch(Exception e)
		{
			NetWrapper.log("Failed to startup the server: "+e.getMessage(), Status.ERROR, e.getCause());
		}
		
		//startup was not successfully, so shutdown
		try
		{
			NetWrapper.getDefault().shutdownServer(monitor);
			return Status.CANCEL_STATUS;
		}
		catch(Exception e)
		{
			NetWrapper.log("Failed to shutdown the server: "+e.getMessage(), Status.ERROR, e.getCause());
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
