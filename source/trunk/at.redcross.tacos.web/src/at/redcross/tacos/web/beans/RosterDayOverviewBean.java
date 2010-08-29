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
@ManagedBean(name = "rosterDayOverviewBean")
public class RosterDayOverviewBean extends RosterOverviewBean {

	private static final long serialVersionUID = 8817078489086816724L;

	private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

	@Override
	protected Date getPreviousDate(Date date) {
		return DateUtils.addDays(date, -1);
	}

	@Override
	protected Date getNextDate(Date date) {
		return DateUtils.addDays(date, +1);
	}

	@Override
	protected List<RosterEntry> getEntries(EntityManager manager, RosterQueryParam params) {
		return RosterEntryHelper.listByDay(manager, params);
	}

	@Override
	protected ReportRenderParameters getReportParams() {
		ReportRenderParameters params = new ReportRenderParameters();
		params.reportName = "Dienstplan_" + sdf.format(date) + ".pdf";
		params.reportFile = "rosterDayReport.rptdesign";
		params.arguments.put("reportParam", getParamForReport());
		params.arguments.put("reportDate", date);
		return params;
	}

}
