package at.redcross.tacos.web.beans;

import java.util.List;


import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.helper.InfoHelper;
import at.redcross.tacos.dbal.query.InfoQueryParam;
import at.redcross.tacos.web.beans.dto.InfoDto;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

@KeepAlive
@ManagedBean(name = "infoFutureOverviewBean")
public class InfoFutureOverviewBean extends InfoOverviewBean {
	
	private static final long serialVersionUID = 6173956046035896088L;

	@Override
	protected List<InfoDto> getEntries(EntityManager manager, InfoQueryParam params) {
		return InfoDto.fromList(InfoHelper.listFuture(manager, params));
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
