package at.redcross.tacos.web.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

@KeepAlive
@ManagedBean(name = "rosterWeekOverviewBean")
public class RosterWeekOverviewBean extends RosterOverviewBean {

	private static final long serialVersionUID = 8106951839383744965L;

	private final SimpleDateFormat sdf = new SimpleDateFormat("w");

	@Override
	protected Date getNextDate(Date date) {
		return DateUtils.addWeeks(date, 1);
	}

	@Override
	protected Date getPreviousDate(Date date) {
		return DateUtils.addWeeks(date, -1);
	}

	@Override
	protected ReportRenderParameters getReportParams() {
		ReportRenderParameters params = new ReportRenderParameters();
		params.reportName = "Dienstplan_KW" + sdf.format(date) + ".pdf";
		params.reportFile = "rosterDayReport.rptdesign";
		params.arguments.put("rosterList", entries);
		params.arguments.put("reportDate", date);
		return params;
	}

	@Override
	protected List<RosterEntry> getEntries(EntityManager manager, RosterQueryParam params) {
		return RosterEntryHelper.listByWeek(manager, params);
	}
}
