package at.redcross.tacos.web.security;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.security.auth.login.AccountLockedException;

import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import at.redcross.tacos.web.faces.FacesUtils;

public class LoginErrorPhaseListener implements PhaseListener {

    private static final long serialVersionUID = -1216620620302322995L;

    @Override
    public void beforePhase(final PhaseEvent arg0) {
        String springKey = AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY;
        Exception e = (Exception) FacesUtils.getExternalContext().getSessionMap().get(springKey);
        if (e instanceof BadCredentialsException) {
            FacesUtils.getExternalContext().getSessionMap().put(springKey, null);
            FacesUtils.addErrorMessage("Benutzername oder Passwort sind falsch");
        }
        if (e instanceof AccountStatusException) {
            FacesUtils.getExternalContext().getSessionMap().put(springKey, null);
            FacesUtils.addErrorMessage("Ihr Account wurde gesperrt.");
        }
        if (e instanceof AccountLockedException) {
            FacesUtils.getExternalContext().getSessionMap().put(springKey, null);
            FacesUtils.addErrorMessage("Ihr Account wurde gesperrt.");
        }
    }

    @Override
    public void afterPhase(final PhaseEvent arg0) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
