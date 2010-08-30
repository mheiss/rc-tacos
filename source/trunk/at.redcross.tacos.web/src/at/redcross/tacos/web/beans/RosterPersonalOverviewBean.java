package at.redcross.tacos.web.beans;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.query.RosterQueryParam;
import at.redcross.tacos.web.beans.dto.RosterDto;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

@KeepAlive
@ManagedBean(name = "rosterPersonalOverviewBean")
public class RosterPersonalOverviewBean extends RosterOverviewBean {

	private static final long serialVersionUID = 8817078489086816724L;

	@Override
	protected List<RosterDto> getEntries(EntityManager manager, RosterQueryParam params) {
		return Collections.emptyList();
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
