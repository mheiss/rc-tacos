package at.redcross.tacos.web.persitence;

import at.redcross.tacos.dbal.entity.listener.HistoryInterceptor;

public class WebHistoryInterceptor extends HistoryInterceptor {

    private static final long serialVersionUID = -3127982722416199008L;

    @Override
    protected String getAuthenticatedUserId() {
        return "WebUser";
    }

}
