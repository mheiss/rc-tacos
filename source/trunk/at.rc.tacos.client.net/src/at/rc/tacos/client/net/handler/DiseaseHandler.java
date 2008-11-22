package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>DiseaseHandler</code> manages the locally chached {@link Disease}
 * instances.
 * 
 * @author Michael
 */
public class DiseaseHandler implements Handler<Disease> {

	private List<Disease> diseaseList = Collections.synchronizedList(new LinkedList<Disease>());
	private Logger log = LoggerFactory.getLogger(DialysisHandler.class);

	@Override
	public void add(MessageIoSession session, Message<Disease> message) throws SQLException, ServiceException {
		synchronized (diseaseList) {
			diseaseList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<Disease> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<Disease> message) throws SQLException, ServiceException {
		synchronized (diseaseList) {
			diseaseList.clear();
			diseaseList.addAll(message.getObjects());
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<Disease> message) throws SQLException, ServiceException {
		synchronized (diseaseList) {
			diseaseList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<Disease> message) throws SQLException, ServiceException {
		synchronized (diseaseList) {
			for (Disease updatedDisease : message.getObjects()) {
				if (!diseaseList.contains(updatedDisease)) {
					continue;
				}
				int index = diseaseList.indexOf(updatedDisease);
				diseaseList.set(index, updatedDisease);
			}
		}
	}

	/**
	 * Returns the first <code>Disease</code> instance that exactly matches the
	 * string returned by {@link Disease#getDiseaseName()}.
	 * 
	 * @param diseaseName
	 *            the name of the <code>Disease</code> to search for
	 * @return the matched object or null if nothing found
	 */
	public Disease getDiseaseByName(String diseaseName) {
		synchronized (diseaseList) {
			for (Disease disease : diseaseList) {
				if (disease.getDiseaseName().equalsIgnoreCase(diseaseName))
					return disease;
			}
			// nothing found
			return null;
		}
	}

	/**
	 * Returns a new array containing the managed <code>Disease</code>
	 * instances.
	 * 
	 * @return an array containing the <code>Disease</code> instances.
	 */
	public Disease[] toArray() {
		return diseaseList.toArray(new Disease[diseaseList.size()]);
	}
}
