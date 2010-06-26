package at.redcross.tacos.web.beans;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.web.security.WebUserDetails;

@ManagedBean(name = "loginBean")
public class LoginBean extends BaseBean {

    private static final long serialVersionUID = -4477225555616492426L;

    /** Request parameter that is delegated to SpringSecurity */
    private String username;
    private String password;

    @Override
    protected void init() throws Exception {
    }

    // ---------------------------------
    // Actions
    // ---------------------------------
    public void login(ActionEvent event) throws IOException, ServletException {
        // setup the request URL that is passed to the security check
        StringBuilder builder = new StringBuilder("/j_spring_security_check");
        builder.append("?j_username=").append(username);
        builder.append("&j_password=").append(password);

        // password field will NOT be restored
        password = null;

        // now delegate to SpringSecurity
        redirectToPage(builder.toString());
    }

    // ---------------------------------
    // Properties
    // ---------------------------------
    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Login getLogin() {
        if (!isAuthenticated()) {
            return null;
        }
        WebUserDetails details = (WebUserDetails) getAuthentication().getPrincipal();
        return details.getLogin();
    }

    public SystemUser getUser() {
        if (!isAuthenticated()) {
            return null;
        }
        return getLogin().getSystemUser();
    }

    public boolean isAuthenticated() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            if (authentication instanceof AnonymousAuthenticationToken) {
                return false;
            }
        }
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // ---------------------------------
    // Private helpers
    // ---------------------------------
    private void redirectToPage(String page) throws IOException, ServletException {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        ExternalContext context = currentInstance.getExternalContext();
        ServletRequest request = (ServletRequest) context.getRequest();
        ServletResponse response = (ServletResponse) context.getResponse();
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
        currentInstance.responseComplete();
    }

}
