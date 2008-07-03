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
		try
		{
			//create and init the job
			monitor.beginTask("Netzwerk wird initialisiert", IProgressMonitor.UNKNOWN);
			
			//Load the preferences
			Integer listenPort = Activator.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_SERVER_PORT);
			String failoverHost = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_FAILOVER_HOST);
			Integer failoverPort = Activator.getDefault().getPluginPreferences().getInt(PreferenceConstants.P_FAILOVER_PORT);
			
			NetWrapper.getDefault().init(listenPort,failoverHost,failoverPort);
			
			//try to create the server socket
			NetWrapper.getDefault().startServer(monitor);
			
			//check the connection
			if(!NetWrapper.getDefault().isListening())
				return Status.CANCEL_STATUS;
			
			return Status.OK_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
