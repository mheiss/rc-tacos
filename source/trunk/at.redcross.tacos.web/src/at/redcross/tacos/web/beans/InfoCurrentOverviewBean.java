package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Info;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.helper.InfoHelper;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

@KeepAlive
@ManagedBean(name = "infoCurrentOverviewBean")
public class InfoCurrentOverviewBean extends InfoOverviewBean {

	private static final long serialVersionUID = 8817078489086816724L;

	@Override
	protected List<Info> getEntries(EntityManager manager, Location location) {
		return InfoHelper.listCurrent(manager, location);
	}

	@Override
	protected ReportRenderParameters getReportParams() {
		ReportRenderParameters params = new ReportRenderParameters();
		params.reportName = "Info" + ".pdf";
		params.reportFile = "rosterDayReport.rptdesign";
		params.arguments.put("rosterList", infos);
		return params;
	}

}