package at.rc.tacos.client.net.handler;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.SystemMessage;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>SystemMessageHandler</code> displays the {@link SystemMessage} send
 * from the server.
 * 
 * @author Michael
 */
public class SystemMessageHandler implements Handler<SystemMessage> {

	private Logger log = LoggerFactory.getLogger(SystemMessageHandler.class);

	@Override
	public void execute(MessageIoSession session, Message<SystemMessage> message) throws SQLException, ServiceException {
		log.info("Received message from server");
		for (SystemMessage systemMessage : message.getObjects()) {
			System.out.println("SYSTEM:" + systemMessage);
		}
	}

	@Override
	public void add(MessageIoSession session, Message<SystemMessage> message) throws SQLException, ServiceException {
		log.debug(MessageType.ADD + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<SystemMessage> message) throws SQLException, ServiceException {
		log.debug(MessageType.GET + " called but currently not implemented");

	}

	@Override
	public void remove(MessageIoSession session, Message<SystemMessage> message) throws SQLException, ServiceException {
		log.debug(MessageType.REMOVE + " called but currently not implemented");
	}

	@Override
	public void update(MessageIoSession session, Message<SystemMessage> message) throws SQLException, ServiceException {
		log.debug(MessageType.UPDATE + " called but currently not implemented");
	}

	@Override
	public SystemMessage[] toArray() {
		return null;
	}
}
