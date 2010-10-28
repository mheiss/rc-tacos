package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Category;
import at.redcross.tacos.dbal.entity.Info;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.helper.InfoHelper;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

@KeepAlive
@ManagedBean(name = "infoFutureOverviewBean")
public class InfoFutureOverviewBean extends InfoOverviewBean {
	
	private static final long serialVersionUID = 6173956046035896088L;

	@Override
	protected List<Info> getEntries(EntityManager manager, Location location) {
		return InfoHelper.listFuture(manager, location);
	}

	@Override
	protected ReportRenderParameters getReportParams() {
		ReportRenderParameters params = new ReportRenderParameters();
		params.reportName = "Info" + ".pdf";
		params.reportFile = "rosterDayReport.rptdesign";
		params.arguments.put("rosterList", infos);
		return params;
	}

	@Override
	protected List<Info> getEntries(EntityManager manager, Category category) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
