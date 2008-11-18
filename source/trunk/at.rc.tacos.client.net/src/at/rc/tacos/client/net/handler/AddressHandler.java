package at.rc.tacos.client.net.handler;

import java.sql.SQLException;

import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The address handler manages the localy cached address objects
 * 
 * @author Michael
 */
public class AddressHandler implements Handler<Address> {

	@Override
	public void add(MessageIoSession session, Message<Address> message) throws SQLException, ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(MessageIoSession session, Message<Address> message) throws SQLException, ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(MessageIoSession session, Message<Address> message) throws SQLException, ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(MessageIoSession session, Message<Address> message) throws SQLException, ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(MessageIoSession session, Message<Address> message) throws SQLException, ServiceException {
		// TODO Auto-generated method stub

	}
}
