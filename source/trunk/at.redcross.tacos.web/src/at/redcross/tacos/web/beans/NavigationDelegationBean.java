package at.redcross.tacos.web.beans;

import javax.faces.bean.ManagedBean;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.web.faces.FacesUtils;

@KeepAlive
@ManagedBean(name = "navigationDelegationBean")
public class NavigationDelegationBean {

    /** Redirects to the default page for the roster area */
    public void redirectRosterHome() {
        FacesUtils.redirect("roster-dayOverview", "");
    }

    /** Redirects to the default page for the info area */
    public void redirectInfoHome() {
        FacesUtils.redirect("info-currentOverview", "");
    }

    /** Redirects to the default page for the statistic area */
    public void redirectStatisticHome() {
        FacesUtils.redirect("statistic", "");
    }

    /** Redirects to the default page for the administration are */
    public void redirectAdminHome() {
        FacesUtils.redirect("admin-overview", "");
    }
}
