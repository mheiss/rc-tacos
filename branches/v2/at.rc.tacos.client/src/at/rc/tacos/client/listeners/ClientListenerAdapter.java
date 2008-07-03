package at.rc.tacos.client.listeners;

import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelListener;
import at.rc.tacos.common.Message;

/**
 * An abstract adapter class for handling server responses.
 * The methods in this class are empty. This class exists as
 * convenience for creating listener objects.
 * @author Michael
 */
public abstract class ClientListenerAdapter implements IModelListener
{
	/**
	 *  Add request to handle
	 */
	@Override
	public void add(AbstractMessage addMessage) { }

	/**
	 * Add request containint more elements
	 */
	@Override
	public void addAll(List<AbstractMessage> addList) { }

	/**
	 * Listing of items to handle
	 */
	@Override
	public void list(List<AbstractMessage> listMessage) { }

	/**
	 * Login response to handle
	 */
	@Override
	public void loginMessage(AbstractMessage message) { }

	/**
	 * Logout response to handle
	 */
	@Override
	public void logoutMessage(AbstractMessage message) { }

	/**
	 * Remove request to handle
	 */
	@Override
	public void remove(AbstractMessage removeMessage) { }
	/**
	 * System message to handle
	 */
	@Override
	public void systemMessage(AbstractMessage message) { }
	/**
	 * Update message to handle
	 */
	@Override
	public void update(AbstractMessage updateMessage) { }

	/**
	 * Connection status change to handle
	 */
	@Override
	public void connectionChange(int status) { }

	/**
	 * Message failed to send
	 */
	@Override
	public void transferFailed(Message info) { }

	/**
	 * Logs the message on the client
	 */
	@Override
	public void log(String message, int stauts) { }
}
