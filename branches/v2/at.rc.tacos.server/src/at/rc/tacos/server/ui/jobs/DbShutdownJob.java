package at.rc.tacos.server.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.server.db.DbWrapper;

public class DbShutdownJob extends Job
{
	/**
	 * Default class constructor
	 */
	public DbShutdownJob() 
	{
		super("DBShutdown");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		try
		{
			monitor.beginTask("Datenbankverbindung wird getrennt", IProgressMonitor.UNKNOWN);
			
			//check the connection
			if(!DbWrapper.getDefault().isConnected())
				return Status.OK_STATUS;
			
			//shutdown
			DbWrapper.getDefault().closeDatabaseConnection();

			return Status.OK_STATUS;
		}
		catch(Exception e)
		{
			DbWrapper.log("Datenbankverbindung konnte nicht getrennt werden "+e.getMessage(), Status.ERROR,e.getCause());
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
