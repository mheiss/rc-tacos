package at.rc.tacos.client.jobs;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.net.NetSource;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.ServerInfo;
import at.rc.tacos.net.MySocket;

/**
	 * The job that is responsible for the reconnect and the login
	 */
	public class ConnectionJob extends Job implements PropertyChangeListener
	{
		//properties of the job
		private ServerInfo serverInfo;	
		
		//the login information to login again
		private String username,password;
		
		
		//the login response
		private boolean loginResponse;
		
		public ConnectionJob(ServerInfo serverInfo,String username,String password) 
		{
			super("Verbindung zum Server aufbauen");
			//the server to connect to
			this.serverInfo = serverInfo;
			this.username = username;
			this.password = password;
		}
		
		@Override
		protected IStatus run(IProgressMonitor monitor) 
		{
			try
			{
				//add a listener for the network messages
				SessionManager.getInstance().addPropertyChangeListener(this);
				
				//we try to connect to the server for 5 times
				for(int i=0;i<5;i++)
				{
					MySocket socket = NetSource.getInstance().openConnection(serverInfo);
					//check if we have a connection
					if(socket != null)
						break;
					
					//wait for 5 seconds and try it again
					monitor.wait(5000);
				}
				//check if we have a connection
				if(NetSource.getInstance().getConnection() == null)
					return Status.CANCEL_STATUS;
				
				//start the monitor jobs and try to login
				NetWrapper.getDefault().init();
				NetWrapper.getDefault().sendLoginMessage(new Login(username,password,false));
				
				//try to login to the server
				while(!loginResponse)
				{
					monitor.wait(100);
				}
				
				//check the login status
				if(!NetWrapper.getDefault().isAuthenticated())
				{
					return Status.CANCEL_STATUS;
				}
				else
				{
					return Status.OK_STATUS;
				}
			}
			catch(Exception e)
			{
				//log the exception and return
				NetWrapper.log("Failed to reconnect to the server "+serverInfo+". Cause:"+e.getMessage(), IStatus.ERROR, e.getCause());
				return Status.CANCEL_STATUS;
			}
			finally
			{
				//remove the listener again
				SessionManager.getInstance().removePropertyChangeListener(this);
				//set the progress to done
				monitor.done();
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) 
		{
			//connection to the server was successfully
			if("AUTHENTICATION_SUCCESS".equalsIgnoreCase(event.getPropertyName()))
				loginResponse = true;
			if("AUTHENTICATION_FAILED".equalsIgnoreCase(event.getPropertyName()))
				loginResponse = true;
		}
	}