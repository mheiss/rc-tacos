package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.DayInfoMessage;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>DayInfoHandler</code> manages the locally chached
 * {@link DayInfoMessage} instances.
 * 
 * @author Michael
 */
public class DayInfoHandler implements Handler<DayInfoMessage> {

	private List<DayInfoMessage> infoList = Collections.synchronizedList(new LinkedList<DayInfoMessage>());
	private Logger log = LoggerFactory.getLogger(DayInfoHandler.class);

	@Override
	public void add(MessageIoSession session, Message<DayInfoMessage> message) throws SQLException, ServiceException {
		log.debug(MessageType.ADD + " called but currently not implemented");
	}

	@Override
	public void execute(MessageIoSession session, Message<DayInfoMessage> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<DayInfoMessage> message) throws SQLException, ServiceException {
		synchronized (infoList) {
			infoList.clear();
			infoList.addAll(message.getObjects());
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<DayInfoMessage> message) throws SQLException, ServiceException {
		log.debug(MessageType.REMOVE + " called but currently not implemented");
	}

	@Override
	public void update(MessageIoSession session, Message<DayInfoMessage> message) throws SQLException, ServiceException {
		synchronized (infoList) {
			for (DayInfoMessage updatedMessage : message.getObjects()) {
				if (!infoList.contains(updatedMessage)) {
					continue;
				}
				int index = infoList.indexOf(updatedMessage);
				infoList.set(index, updatedMessage);
			}
		}
	}

	/**
	 * Returns the first <code>DayInfoMessage</code> instance that exactly
	 * matches the date returned by {@link DayInfoMessage#getTimestamp()}.
	 * <p>
	 * When checking the returned date only the day, month or year is checked
	 * </p>
	 * 
	 * @param date
	 *            the timestamp of the <code>DayInfoMessage</code> to search for
	 * @return the matched object or null if nothing found
	 */
	public DayInfoMessage getMessageByDate(long date) {
		for (DayInfoMessage message : infoList) {
			if (message.getTimestamp() == date) {
				return message;
			}
		}
		// nothing found
		return null;
	}
}
