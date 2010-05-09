package at.redcross.tacos.web.beans;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "errorBean")
public class ErrorBean extends BaseBean {

	// the injected URI
	private String cameFrom;

	// the injected error code;
	private String errorCode;

	@Override
	protected void init() throws Exception {
	}

	public void setCameFrom(String cameFrom) {
		this.cameFrom = cameFrom;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getCameFrom() {
		return cameFrom;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
