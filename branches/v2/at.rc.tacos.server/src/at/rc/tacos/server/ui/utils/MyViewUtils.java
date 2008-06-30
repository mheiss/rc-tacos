package at.rc.tacos.server.ui.utils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Common methods to use
 * @author Michael
 */
public class MyViewUtils 
{
	/**
	 * Displays a error message dialog. This dialog will be thread save
	 */
	public static void showError(final String title,final String message)
	{
		//show a error when the job failed
		Display.getDefault().syncExec(new Runnable() 
		{
			@Override
			public void run() 
			{
				Display.getDefault().beep();
				MessageDialog.openError(Display.getDefault().getActiveShell(),title,message);
			}
		});
	}

}
