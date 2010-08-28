package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import at.redcross.tacos.web.faces.FacesMessageExt;

@ManagedBean(name = "errorBean")
public class ErrorBean extends BaseBean {

	private static final long serialVersionUID = -8228546063540602086L;

	// the injected URI
	private String cameFrom;

	// the injected error code;
	private String errorCode;

	@Override
	protected void init() throws Exception {
		// no initialization required
	}

	/**
	 * Returns all currently registered error and warning messages.
	 * 
	 * @return a list of error messages
	 */
	public List<FacesMessageExt> getErrorMessages() {
		List<FacesMessageExt> messages = new ArrayList<FacesMessageExt>();
		Iterator<FacesMessage> messageIter = FacesContext.getCurrentInstance().getMessages(null);
		while (messageIter.hasNext()) {
			FacesMessage next = messageIter.next();
			if (next.getSeverity() == FacesMessage.SEVERITY_INFO) {
				continue;
			}
			if (next.getSeverity() == FacesMessage.SEVERITY_WARN) {
				continue;
			}
			messages.add(new FacesMessageExt(next));
		}
		return messages;
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setCameFrom(String cameFrom) {
		this.cameFrom = cameFrom;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getCameFrom() {
		return cameFrom;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
