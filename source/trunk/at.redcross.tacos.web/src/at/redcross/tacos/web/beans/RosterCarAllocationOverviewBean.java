package at.redcross.tacos.web.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.Notification;
import at.redcross.tacos.dbal.helper.CarHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.NotificationHelper;
import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;
import at.redcross.tacos.web.beans.dto.RosterDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;
import at.redcross.tacos.web.utils.TacosDateUtils;

@KeepAlive
@ManagedBean(name = "rosterCarAllocationOverviewBean")
public class RosterCarAllocationOverviewBean extends RosterOverviewBean {

	private static final long serialVersionUID = -6923595116203096939L;

	private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

	// the suggested values for the drop down boxes
	private List<SelectItem> carItems;
	private List<SelectItem> locationItems;

	// the notification of the day
	private Notification notification;

	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			date = TacosDateUtils.getCalendar(System.currentTimeMillis()).getTime();
			locations = LocationHelper.list(manager);
			locationItems = DropDownHelper.convertToItems(locations);
			carItems = DropDownHelper.convertToItems(CarHelper.list(manager, false));
			entries = getEntries(manager, getParamForQuery());
			notification = getNotification(manager, date);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------
	public void saveEntries(ActionEvent event) {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			for (RosterDto entryDto : entries) {
				manager.merge(entryDto.getEntity());
			}
			manager.merge(notification);
			EntityManagerHelper.commit(manager);
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Die Fahrzeugzuweisung konnte nicht gespeichert werden");
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
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
	protected List<RosterDto> getEntries(EntityManager manager, RosterQueryParam param) {
		notification = getNotification(manager, date);
		return RosterDto.fromList(RosterEntryHelper.listByDay(manager, param));
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

	protected Notification getNotification(EntityManager manager, Date date) {
		notification = NotificationHelper.getByDate(manager, date);
		if (notification != null) {
			return notification;
		}
		Notification notification = new Notification();
		notification.setDate(date);
		return notification;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public List<SelectItem> getCarItems() {
		return carItems;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}

	public Notification getNotification() {
		return notification;
	}
}
