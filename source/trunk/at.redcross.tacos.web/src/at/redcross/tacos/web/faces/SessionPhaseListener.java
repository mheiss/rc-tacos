package at.redcross.tacos.web.faces;

import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The <code>SessionPhaseListener</code> will prevent jsf from throwing a
 * {@link ViewExpiredException} when the current session has timed out.
 */
public class SessionPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1L;

    private Log log = LogFactory.getLog(FacesUtils.class);

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();

        // get the current session, but do not force to create a new one
        ExternalContext extContext = context.getExternalContext();
        HttpSession session = (HttpSession) extContext.getSession(false);

        // session timed out and destroyed, so redirect
        if (session == null) {
            log.info("Invalid or new session detected, redirecting");
            FacesUtils.redirectError(new ViewExpiredException("Session timeout"));
        }
    }

    @Override
    public void afterPhase(PhaseEvent arg0) {
    }
}