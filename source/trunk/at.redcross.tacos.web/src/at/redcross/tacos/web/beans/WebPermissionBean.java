package at.redcross.tacos.web.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.ajax4jsf.model.KeepAlive;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.web.security.WebPermissionManager;
import at.redcross.tacos.web.security.WebUserDetails;

@KeepAlive
@ManagedBean(name = "permissionBean")
public class WebPermissionBean extends WebPermissionManager implements Serializable {

    private static final long serialVersionUID = 1368029789717485124L;

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

    public boolean isAuthorizedToAccessStatisticArea() {
        return canAccessResource("statistic-home");
    }

    public boolean isAuthorizedToAccessRosterArea() {
        return canAccessResource("roster-home");
    }

    public boolean isAuthorizedToAccessInfoArea() {
        return canAccessResource("info-home");
    }

    // ---------------------------------
    // Action requests
    // ---------------------------------
    public boolean isAuthorizedToViewHistory() {
        return canExecuteAction("view-history");
    }

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

    public boolean isAuthorizedToEditCompetence() {
        return canExecuteAction("user-editCompetence");
    }

    public boolean isAuthorizedToEditUser() {
        return canExecuteAction("user-editEntry");
    }

    public boolean isAuthorizedToEditVehicle() {
        return canExecuteAction("vehicle-editEntry");
    }
}
