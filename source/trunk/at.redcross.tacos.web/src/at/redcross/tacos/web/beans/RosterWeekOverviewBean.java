package at.redcross.tacos.web.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.bean.ManagedBean;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

@KeepAlive
@ManagedBean(name = "rosterWeekOverviewBean")
public class RosterWeekOverviewBean extends RosterOverviewBean {

    private static final long serialVersionUID = 8106951839383744965L;

    private final SimpleDateFormat sdf = new SimpleDateFormat("w");

    @Override
    protected Date getInitialDate() {
        // determine the current week
        Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        int startWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        // setup the starting calendar
        calendar.clear();
        calendar.set(Calendar.YEAR, startYear);
        calendar.set(Calendar.WEEK_OF_MONTH, startWeek);

        // return the date value
        return calendar.getTime();
    }

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
        params.reportFile = "rosterReport.rptdesign";
        params.arguments.put("reportParam", getFilteredEntries());
        params.arguments.put("reportName", String
                .format("Dienstplan für KW %1$s", sdf.format(date)));
        return params;
    }

}
