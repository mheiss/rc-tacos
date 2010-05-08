package at.redcross.tacos.web.faces;

import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

/**
 * The <code>CacheControlPhaseListener</code> will pervent caching in the whole webapplication.
 */
public class CacheControlPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1L;

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        ExternalContext extContext = event.getFacesContext().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) extContext.getResponse();
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.addHeader("Cache-Control", "no-cache"); // HTTP 1.1
        response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Expires", "Sun, 2 Dec 1984 02:00:00 GMT"); // in the past
        response.setDateHeader("Expires", 0); // prevents caching at the proxy server
    }
}