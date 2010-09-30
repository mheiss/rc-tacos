package at.redcross.tacos.web.beans;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import at.redcross.tacos.web.faces.FacesUtils;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.config.PrettyConfig;
import com.ocpsoft.pretty.config.PrettyUrlMapping;

public abstract class PasswordBean implements Serializable {

	private static final long serialVersionUID = -209015194454141136L;

	/** abort and redirect to the login page */
	public void abortRequest(ActionEvent ae) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		try {
			PrettyContext prettyContext = PrettyContext.getCurrentInstance();
			PrettyConfig prettyConfig = prettyContext.getConfig();
			PrettyUrlMapping mapping = prettyConfig.getMappingById("login");
			facesContext.responseComplete();
			externalContext
					.redirect(externalContext.getRequestContextPath() + mapping.getPattern());
		} catch (Exception e) {
			FacesUtils.redirectError(e);
		}
	}
}
