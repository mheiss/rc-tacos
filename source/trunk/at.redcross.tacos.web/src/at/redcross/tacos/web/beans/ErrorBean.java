package at.redcross.tacos.web.beans;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "errorBean")
public class ErrorBean extends BaseBean {

	// the injected URI
	private String cameFrom;

	@Override
	protected void init() throws Exception {
	}

	public void setCameFrom(String cameFrom) {
		this.cameFrom = cameFrom;
	}

	public String getCameFrom() {
		return cameFrom;
	}
}
