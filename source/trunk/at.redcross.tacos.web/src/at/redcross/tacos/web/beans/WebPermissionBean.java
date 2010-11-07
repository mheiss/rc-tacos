package at.redcross.tacos.web.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.web.security.WebPermissionManager;
import at.redcross.tacos.web.security.WebUserDetails;

@ManagedBean(name = "permissionBean")
public class WebPermissionBean extends WebPermissionManager {

    @PostConstruct
    protected void init() {
        initManager();
    }

    // ---------------------------------
    // Principal requests
    // ---------------------------------
    public SystemUser getSystemUser() {
        WebUserDetails principal = getPrincipal();
        if (principal == null) {
            return null;
        }
        return principal.getLogin().getSystemUser();
    }

    public WebUserDetails getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof WebUserDetails) {
            return (WebUserDetails) principal;
        }
        return null;
    }

    // ---------------------------------
    // Resource requests
    // ---------------------------------
    public boolean isAuthorizedToAccessAdminArea() {
        return canAccessResource("admin-home");
    }

    public boolean isAuthorizedToAccessRosterArea() {
        return canAccessResource("roster-dayOverview");
    }

    // ---------------------------------
    // Action requests
    // ---------------------------------
    public boolean isAuthorizedToDeleteRoster() {
        return canExecuteAction("roster-deleteEntry");
    }

    public boolean isAuthorizedToEditRoster() {
        return canExecuteAction("roster-editEntry");
    }

    public boolean isAuthorizedToAssignCar() {
        return canExecuteAction("roster-assignCar");
    }

    public boolean isAuthorizedToCreateOutdatedRoster() {
        return canExecuteAction("roster-createOutdated");
    }

    public boolean isAuthorizedToDeleteInfo() {
        return canExecuteAction("info-deleteEntry");
    }

    public boolean isAuthorizedToEditInfo() {
        return canExecuteAction("info-editEntry");
    }
    
    public boolean isAuthorizedToEditRole() {
        return canExecuteAction("user-editRole");
    }
}
