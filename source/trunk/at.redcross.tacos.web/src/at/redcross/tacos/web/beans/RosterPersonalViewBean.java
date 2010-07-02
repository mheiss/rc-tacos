package at.redcross.tacos.web.beans;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

@KeepAlive
@ManagedBean(name = "rosterPersonalViewBean")
public class RosterPersonalViewBean extends RosterViewBean {

    private static final long serialVersionUID = 8817078489086816724L;

    @Override
    protected List<RosterEntry> getEntries(EntityManager manager, Location location, Date date) {
        return null;
    }

    @Override
    protected Date getNextDate(Date date) {
        return null;
    }

    @Override
    protected Date getPreviousDate(Date date) {
        return null;
    }

    @Override
    protected ReportRenderParameters getReportParams() {
        return null;
    }

}
