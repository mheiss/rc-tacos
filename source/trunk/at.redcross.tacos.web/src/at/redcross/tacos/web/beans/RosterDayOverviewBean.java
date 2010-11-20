package at.redcross.tacos.web.beans;

import java.util.Date;

import javax.faces.bean.ManagedBean;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;
import at.redcross.tacos.web.utils.TacosDateUtils;

@KeepAlive
@ManagedBean(name = "rosterDayOverviewBean")
public class RosterDayOverviewBean extends RosterOverviewBean {

    private static final long serialVersionUID = 8817078489086816724L;

    @Override
    protected Date getInitialDate() {
        return TacosDateUtils.getCalendar(System.currentTimeMillis()).getTime();
    }

    @Override
    protected Date getPreviousDate(Date date) {
        return DateUtils.addDays(date, -1);
    }

    @Override
    protected Date getNextDate(Date date) {
        return DateUtils.addDays(date, +1);
    }

    @Override
    protected ReportRenderParameters getReportParams() {
        ReportRenderParameters params = new ReportRenderParameters();
        params.reportName = "Dienstplan_" + sdfFile.format(date) + ".pdf";
        params.reportFile = "rosterReport.rptdesign";
        params.arguments.put("reportParam", getFilteredEntries());
        params.arguments.put("reportName", String.format("Dienstplan für %1$s", sdfDisplay
                .format(date)));
        return params;
    }
}
