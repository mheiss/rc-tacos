package at.rc.tacos.client.modelManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.dialogs.ConnectionDialog;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.common.Message;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.ServerInfo;
import at.rc.tacos.util.MyUtils;

/**
 * This class manages the logged in user and the data accociated with the current session.
 * The day info message is also managed here.
 * @author Michael
 */
public class SessionManager extends PropertyManager implements PropertyChangeListener
{
	//the unique id
	public final static String ID = "sessionManager";

	//the instance
	private static SessionManager instance;

	//the logged in user
	private Login loginInformation;

	//daily info
	private DayInfoMessage dayInfo;
	private long displayedDate;
	
	/**
	 * Default class constructor
	 */
	private SessionManager() 
	{ 
		//initialize the default values
		displayedDate = Calendar.getInstance().getTimeInMillis();
		dayInfo = new DayInfoMessage("",displayedDate,"<keine Änderungen>");
		
		//attach a listener for network events
		NetWrapper.getDefault().addPropertyChangeListener(this);
	} 

	/**
	 * Returns the shared instance
	 */
	public static SessionManager getInstance()
	{
		if( instance == null)
			instance = new SessionManager();
		return instance;
	}
	
    /**
     *  informs all listeners about new locations
     */
    public void initViews(PropertyChangeListener listener)
    {
    	listener.propertyChange(new PropertyChangeEvent(this,"DAY_INFO_CHANGED",null,dayInfo));
    }

	/**
	 * Returns whether or not the user is logged in
	 * @return true if the user is logged in
	 */
	public boolean isAuthenticated()
	{
		if(loginInformation != null)
			return true;
		//not authenticated
		return false;
	}

	/**
	 * Returns the login information about the currently logged in user
	 * @param the login information or null if no user is authenticated
	 */
	public Login getLoginInformation()
	{
		return loginInformation;
	}

	/**
	 * Returns the dayInfo message to display
	 * @return the dayInfo message
	 */
	public DayInfoMessage getDayInfoMessage()
	{
		return dayInfo;
	}

	/**
	 * Sets the day info message to display in the info view
	 * @param dayInfo the info to display
	 */
	public void setDayInfoMessage(final DayInfoMessage dayInfo)
	{
		//check if current displayed date is equal to the date of the day info update message
		//--> display the message if equal
		if(MyUtils.isEqualDate(dayInfo.getTimestamp(), displayedDate))
		{
			this.dayInfo = dayInfo;
			Display.getDefault().syncExec(new Runnable ()    
			{
				public void run ()       
				{
					firePropertyChange("DAY_INFO_CHANGED", null, dayInfo);
				}
			});
		}
	}
	
	/**
	 * Updates the day info message when there are local changes
	 * @param message the message to set
	 */
	public void updateLocalDayInfoMessage(String message)
	{
		//check if the messages are the same
		if(dayInfo.getMessage().equals(message))
			dayInfo.setDirty(false);
		else
			dayInfo.setDirty(true);
		//fire change
		firePropertyChange("DAY_INFO_LOCAL_CHANGED", null, dayInfo);
	}

	/**
	 * Returns the currently selected date in the day select field
	 * @return the selected date
	 */
	public long getDisplayedDate()
	{
		return displayedDate;
	}

	/**
	 * Sets the currently selected date.
	 * @param displayedDate the selected date
	 */
	public void setDisplayedDate(long displayedDate)
	{
		this.displayedDate = displayedDate;
	}

	/**
	 * Fired when the login of the user was successfully.
	 * @param loginInformation the login information 
	 */
	public void fireLoginSuccessfully(final Login loginInformation)
	{
		//store the login information
		this.loginInformation = loginInformation;
		//set the username in the client session
		NetWrapper.getDefault().setNetSessionUsername(loginInformation.getUsername());
		firePropertyChange("AUTHENTICATION_SUCCESS", null, loginInformation);
	}

	/**
	 * Fired when the login was denied.
	 * @param loginInformation the login information 
	 */
	public void fireLoginDenied(final Login loginInformation)
	{
		firePropertyChange("AUTHENTICATION_FAILED", null, loginInformation);
	}

	/**
	 * This method is called when the connection to the server is lost
	 */
	protected void connectionLost(final ServerInfo info)
	{
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{
				//show the dialog to reconnect to the server
				int result = ConnectionDialog.openDialog(Display.getCurrent().getActiveShell());
				System.out.println("RESULT: "+result);
			}
		});
	}
	
	/**
	 * This method is called when the network cannot send the message to the server
	 * @param info the message that could not been send
	 */
	protected void transferFailed(final Message info) 
	{
		//the message to display
		final StringBuffer msg = new StringBuffer();
		msg.append("Die folgende Nachricht konnte nicht an den Server übertragen werden. (Zeitüberschreitung).\n");
		msg.append(info.getContentType()+" -> "+info.getQueryString());

		//show a message
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{
				//show the message to the user
				MessageDialog.openError(
						PlatformUI.getWorkbench().getDisplay().getActiveShell(), 
						"Netzwerkfehler",
						msg.toString());
				//retry
				boolean retryConfirmed = MessageDialog.openConfirm(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						"Senden wiederholen",
				"Wollen sie die Nachricht noch einmal senden?");
				if (!retryConfirmed) 
					return;
				NetWrapper.getDefault().sheduleAndSend(info);
			}
		});
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) 
	{
		//the name of the event
		final String name = event.getPropertyName();
		
		if("NET_TRANSFER_FAILED".equalsIgnoreCase(name))
		{
			//get the message that was failed to send
			Message message = (Message)event.getNewValue();
			transferFailed(message);
		}
		if("NET_CONNECTION_LOST".equalsIgnoreCase(name))
		{
			//get the server info of the lost connection
			ServerInfo info = (ServerInfo)event.getNewValue();
			connectionLost(info);
		}
	} 
}
