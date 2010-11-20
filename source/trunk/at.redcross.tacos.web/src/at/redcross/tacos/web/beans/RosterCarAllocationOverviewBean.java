package at.redcross.tacos.web.beans;

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
import at.redcross.tacos.dbal.helper.NotificationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.RosterDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItemHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;
import at.redcross.tacos.web.utils.TacosDateUtils;

@KeepAlive
@ManagedBean(name = "rosterCarAllocationOverviewBean")
public class RosterCarAllocationOverviewBean extends RosterOverviewBean {

    private static final long serialVersionUID = -6923595116203096939L;

    // the suggested values for the drop down boxes
    private List<SelectItem> carItems;

    // the notification of the day
    private Notification notification;

    @Override
    protected void init(EntityManager manager) {
        super.init(manager);
        carItems = SelectableItemHelper.convertToItems(CarHelper.list(manager, false));
        notification = getNotification(manager, date);
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
    protected List<RosterDto> getEntries(EntityManager manager) {
        notification = getNotification(manager, date);
        return super.getEntries();
    }

    @Override
    protected ReportRenderParameters getReportParams() {
        ReportRenderParameters params = new ReportRenderParameters();
        params.reportName = "Fahrzeugzuweisung_" + sdfFile.format(date) + ".pdf";
        params.reportFile = "carReport.rptdesign";
        params.arguments.put("reportParam", getFilteredEntries());
        params.arguments.put("reportName", String.format("Fahrzeugzuweisung für %1$s", sdfDisplay
                .format(date)));
        return params;
    }

    /** Returns the notification entry for the current date or creates a new one */
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

    public Notification getNotification() {
        return notification;
    }
}
