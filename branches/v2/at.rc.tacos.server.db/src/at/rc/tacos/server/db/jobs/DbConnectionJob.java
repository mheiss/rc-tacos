package at.rc.tacos.server.db.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.server.db.DbWrapper;

/**
 * This job will helpt to establish a connection to the database
 * @author Michael
 */
public class DbConnectionJob extends Job
{
	/**
	 * Default class constructor
	 */
	public DbConnectionJob()
	{
		super("DbConnectionJob");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		try
		{
			//create and init the job
			monitor.beginTask("Verbindung zur Datenbank wird hergestellt", IProgressMonitor.UNKNOWN);
			
			//check the connection
			if(DbWrapper.getDefault().isConnected())
				return Status.OK_STATUS;
			
			//open a connection to the database
			DbWrapper.getDefault().initDatabaseConnection();
			
			//assert valid connection
			if(DbWrapper.getDefault().getConnection() == null)
				return Status.CANCEL_STATUS;
			
			//everything worked fine
			return Status.OK_STATUS;
		} 
		catch (Exception e) 
		{
			DbWrapper.log("Verbindung zur Datenbank konnte nicht hergestellt werden: "+e.getMessage(), Status.ERROR,e.getCause());
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
