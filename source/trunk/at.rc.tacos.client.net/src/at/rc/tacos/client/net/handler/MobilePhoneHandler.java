package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>MobilePhoneHandler</code> manages the locally chached
 * {@link MobilePhoneDetail} instances.
 * 
 * @author Michael
 */
public class MobilePhoneHandler implements Handler<MobilePhoneDetail> {

	private List<MobilePhoneDetail> phoneList = Collections.synchronizedList(new ArrayList<MobilePhoneDetail>());
	private Logger log = LoggerFactory.getLogger(MobilePhoneHandler.class);

	@Override
	public void add(MessageIoSession session, Message<MobilePhoneDetail> message) throws SQLException, ServiceException {
		synchronized (phoneList) {
			phoneList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<MobilePhoneDetail> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<MobilePhoneDetail> message) throws SQLException, ServiceException {
		synchronized (phoneList) {
			// add or update the mobile phone
			for (MobilePhoneDetail mobilePhoneDetail : message.getObjects()) {
				int index = phoneList.indexOf(mobilePhoneDetail);
				if (index == -1) {
					phoneList.add(mobilePhoneDetail);
				}
				else {
					phoneList.add(mobilePhoneDetail);
				}
			}
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<MobilePhoneDetail> message) throws SQLException, ServiceException {
		synchronized (phoneList) {
			phoneList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<MobilePhoneDetail> message) throws SQLException, ServiceException {
		synchronized (phoneList) {
			for (MobilePhoneDetail updatedPhone : message.getObjects()) {
				if (!phoneList.contains(updatedPhone)) {
					continue;
				}
				int index = phoneList.indexOf(updatedPhone);
				phoneList.set(index, updatedPhone);
			}
		}
	}

	/**
	 * Returns a new array containing the managed <code>MobilePhoneDetail</code>
	 * instances.
	 * 
	 * @return an array containing the <code>MobilePhoneDetail</code> instances.
	 */
	@Override
	public MobilePhoneDetail[] toArray() {
		return phoneList.toArray(new MobilePhoneDetail[phoneList.size()]);
	}
}
