package at.redcross.tacos.web.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;
import at.redcross.tacos.web.beans.dto.RosterDto;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

@KeepAlive
@ManagedBean(name = "rosterDayOverviewBean")
public class RosterDayOverviewBean extends RosterOverviewBean {

    private static final long serialVersionUID = 8817078489086816724L;

    private final SimpleDateFormat sdfFile = new SimpleDateFormat("ddMMyyyy");
    private final SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    protected Date getPreviousDate(Date date) {
        return DateUtils.addDays(date, -1);
    }

    @Override
    protected Date getNextDate(Date date) {
        return DateUtils.addDays(date, +1);
    }

    @Override
    protected List<RosterDto> getEntries(EntityManager manager, RosterQueryParam params) {
        return RosterDto.fromList(RosterEntryHelper.listByDay(manager, params));
    }

    @Override
    protected ReportRenderParameters getReportParams() {
        ReportRenderParameters params = new ReportRenderParameters();
        params.reportName = "Dienstplan_" + sdfFile.format(date) + ".pdf";
        params.reportFile = "rosterReport.rptdesign";
        params.arguments.put("reportParam", getParamForReport());
        params.arguments.put("reportName", String.format("Dienstplan für %1$s", sdfDisplay
                .format(date)));
        return params;
    }
}
