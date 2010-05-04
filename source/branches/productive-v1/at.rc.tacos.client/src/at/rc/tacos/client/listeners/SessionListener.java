/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.client.listeners;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.LoginManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.AbstractMessageInfo;
import at.rc.tacos.common.IConnectionStates;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.SystemMessage;

public class SessionListener extends ClientListenerAdapter {

	protected SessionManager session = SessionManager.getInstance();
	protected LoginManager manager = ModelFactory.getInstance().getLoginManager();

	@Override
	public void add(AbstractMessage addMessage) {
		if (addMessage instanceof Login) {
			Login addLogin = (Login) addMessage;
			manager.add(addLogin);
		}
	}

	@Override
	public void remove(AbstractMessage removeMessage) {
		if (removeMessage instanceof Login) {
			Login removeLogin = (Login) removeMessage;
			manager.remove(removeLogin);
		}
	}

	@Override
	public void loginMessage(AbstractMessage message) {
		// cast to a login message
		Login login = (Login) message;
		// check the login
		if (login.isLoggedIn()) {
			session.fireLoginSuccessfully(login);
			// log the message
			Status status = new Status(IStatus.OK, Activator.PLUGIN_ID, "Successfully authenticated " + login.getUsername());
			Activator.getDefault().getLog().log(status);
		}
		else if (login.isIslocked()) {
			session.fireLoginDenied(login);
			Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "User is locked and not allowed to login:" + login.getUsername());
			Activator.getDefault().getLog().log(status);
		}
		else {
			session.fireLoginDenied(login);
			Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Failed to authenticate " + login.getUsername());
			Activator.getDefault().getLog().log(status);
		}
	}

	@Override
	public void logoutMessage(AbstractMessage message) {
		// get the result and cast
		final Logout logout = (Logout) message;
		// check if the user is logged out
		if (logout.isLoggedOut()) {
			Status status = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Successfully logged out the user: " + logout.getUsername());
			Activator.getDefault().getLog().log(status);
		}
	}

	@Override
	public void connectionChange(int status) {
		if (status == IConnectionStates.STATE_DISCONNECTED)
			session.fireConnectionLost();
	}

	@Override
	public void transferFailed(final AbstractMessageInfo info) {
		// the message to display
		final StringBuffer msg = new StringBuffer();
		msg.append("Die folgende Nachricht konnte nicht an den Server übertragen werden. (Zeitüberschreitung).\n");
		msg.append(info.getContentType() + " -> " + info.getQueryString());

		// show a message
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				// show the message to the user
				MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Netzwerkfehler", msg.toString());
				// retry
				boolean retryConfirmed = MessageDialog.openConfirm(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Senden wiederholen",
						"Wollen sie die Nachricht noch einmal senden?");
				if (!retryConfirmed)
					return;
				NetWrapper.getDefault().sheduleAndSend(info);
			}
		});
	}

	@Override
	public void systemMessage(final AbstractMessage message) {
		final SystemMessage sysMessage = (SystemMessage) message;

		// show a message
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Schwerwiegender Fehler", sysMessage.getMessage());
			}
		});

		// the log message
		Status status;

		if (sysMessage.getType() == SystemMessage.TYPE_INFO)
			status = new Status(IStatus.INFO, Activator.PLUGIN_ID, sysMessage.getMessage());
		else if (sysMessage.getType() == SystemMessage.TYPE_ERROR)
			status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, sysMessage.getMessage());
		// log as an error message
		else
			status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, sysMessage.getMessage());
		// log the message
		Activator.getDefault().getLog().log(status);
	}

	@Override
	public void update(AbstractMessage updateMessage) {
		// do we have a dayInfo message?
		if (updateMessage instanceof DayInfoMessage) {
			DayInfoMessage dayInfo = (DayInfoMessage) updateMessage;
			session.setDayInfoMessage(dayInfo);
		}
		// do we have a login message?
		if (updateMessage instanceof Login) {
			Login login = (Login) updateMessage;
			manager.update(login);
		}
	}

	@Override
	public void list(List<AbstractMessage> listMessage) {
		// get the first item to check the type
		AbstractMessage listObject = listMessage.get(0);
		if (listObject instanceof DayInfoMessage) {
			update(listObject);
		}
		if (listObject instanceof Login) {
			// loop and add all logins
			for (AbstractMessage abstractObject : listMessage) {
				Login login = (Login) abstractObject;
				// assert we do not have this login
				if (manager.contains(login))
					manager.update(login);
				else
					manager.add(login);
			}
		}
	}

	@Override
	public void log(String message, int status) {
		Status statusMessage = new Status(status, NetWrapper.PLUGIN_ID, message);
		Activator.getDefault().getLog().log(statusMessage);
	}
}
