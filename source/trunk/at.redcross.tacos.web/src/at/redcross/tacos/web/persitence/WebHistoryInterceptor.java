package at.redcross.tacos.web.persitence;

import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.entity.listener.HistoryInterceptor;
import at.redcross.tacos.web.beans.LoginBean;
import at.redcross.tacos.web.faces.FacesUtils;

public class WebHistoryInterceptor extends HistoryInterceptor {

    private static final long serialVersionUID = -3127982722416199008L;

    @Override
    protected String getAuthenticatedUserId() {
        LoginBean loginBean = (LoginBean) FacesUtils.lookupBean("loginBean");
        SystemUser systemUser = loginBean.getUser();
        return systemUser.getLastName() + " " + systemUser.getFirstName();
    }

}
