package at.rc.tacos.client.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;

public class AuthenticationListener extends ClientListenerAdapter
{
    @Override
    public void loginMessage(AbstractMessage message)
    {
        Login login = (Login)message;
        System.out.println("Login: "+login);
    }

    @Override
    public void logoutMessage(AbstractMessage message)
    {
        //get the result and cast
        Logout logout = (Logout)message;
        //check if the user is logged out
        if(logout.isLoggedOut())
        {
            //show that the connection is closed
            MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
            mb.setText("Verbindungsfehler");
            mb.setMessage("Die Verbindung zum Server wurde getrennt"
                        +"\nFehler: "+ logout.getErrorMessage());
        }
        
        System.out.println("Logout: "+logout);
    }
}
