package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>DialysisHandler</code> handles the locally chached
 * {@link DialysisPatient} instances.
 * 
 * @author Michael
 */
public class DialysisHandler implements Handler<DialysisPatient> {

	private List<DialysisPatient> dialysisList = Collections.synchronizedList(new LinkedList<DialysisPatient>());
	private Logger log = LoggerFactory.getLogger(DialysisHandler.class);

	@Override
	public void add(MessageIoSession session, Message<DialysisPatient> message) throws SQLException, ServiceException {
		synchronized (dialysisList) {
			dialysisList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<DialysisPatient> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<DialysisPatient> message) throws SQLException, ServiceException {
		synchronized (dialysisList) {
			dialysisList.clear();
			dialysisList.addAll(message.getObjects());
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<DialysisPatient> message) throws SQLException, ServiceException {
		synchronized (dialysisList) {
			dialysisList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<DialysisPatient> message) throws SQLException, ServiceException {
		synchronized (dialysisList) {
			for (DialysisPatient updatedDialysisPatient : message.getObjects()) {
				if (!dialysisList.contains(updatedDialysisPatient))
					continue;
				int index = dialysisList.indexOf(updatedDialysisPatient);
				dialysisList.set(index, updatedDialysisPatient);
			}
		}
	}

	/**
	 * Returns a new array containing the managed <code>DialysisPatient</code>
	 * instances.
	 * 
	 * @return an array containing the <code>DialysisPatient</code> instances.
	 */
	public DialysisPatient[] toArray() {
		return dialysisList.toArray(new DialysisPatient[dialysisList.size()]);
	}
}
