package at.redcross.tacos.web.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.DataState;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;
import at.redcross.tacos.web.beans.dto.RosterDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
public abstract class RosterOverviewBean extends RosterBean {

    private static final long serialVersionUID = -63594513702881676L;

    /** Formatter for a date using <tt>ddMMyyyy</tt> */
    protected final SimpleDateFormat sdfFile = new SimpleDateFormat("ddMMyyyy");

    /** Formatter for a date using <tt>dd.MM.yyyy</tt> */
    protected final SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd.MM.yyyy");

    /** filter by delete flag */
    protected boolean showDeleted;

    /** filter by normal flag */
    protected boolean showNormal = true;
    
    @Override
    protected void init(EntityManager manager) {
        super.init(manager);
        entries = getEntries(manager);
    }

    // ---------------------------------
    // Business methods
    // ---------------------------------
    public void navigateNext(ActionEvent event) {
        EntityManager manager = null;
        try {
            date = getNextDate(date);
            manager = EntityManagerFactory.createEntityManager();
            entries = getEntries(manager);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void navigatePrevious(ActionEvent event) {
        EntityManager manager = null;
        try {
            date = getPreviousDate(date);
            manager = EntityManagerFactory.createEntityManager();
            entries = getEntries(manager);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void markToDelete(ActionEvent event) {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            Iterator<RosterDto> iter = entries.iterator();
            while (iter.hasNext()) {
                RosterEntry entry = iter.next().getEntity();
                if (entry.getId() != entryId) {
                    continue;
                }
                entry.setState(DataState.DELETE);
                manager.merge(entry);
                iter.remove();
            }
            EntityManagerHelper.commit(manager);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gelöscht werden");
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void signIn(ActionEvent event) {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            for (RosterDto entryDto : entries) {
                RosterEntry entry = entryDto.getEntity();
                if (entry.getId() != entryId) {
                    continue;
                }
                entry.setRealStartDateTime(Calendar.getInstance().getTime());
                manager.merge(entry);
            }
            EntityManagerHelper.commit(manager);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gelöscht werden");
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void signOut(ActionEvent event) {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            for (RosterDto entryDto : entries) {
                RosterEntry entry = entryDto.getEntity();
                if (entry.getId() != entryId) {
                    continue;
                }
                entry.setRealEndDateTime(Calendar.getInstance().getTime());
                manager.merge(entry);
            }
            EntityManagerHelper.commit(manager);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gelöscht werden");
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Roster API
    // ---------------------------------
    /** Increments the current date by the requested amount */
    protected abstract Date getNextDate(Date date);

    /** Decrements the current date by the requested amount */
    protected abstract Date getPreviousDate(Date date);
    
    @Override
    protected RosterQueryParam getQueryParams() {
        RosterQueryParam param = new RosterQueryParam();
        param.location = getLocationByName(locationName);
        param.startDate = date;
        param.endDate = getNextDate(date);
        param.stateNormal = showNormal;
        param.stateDelete = showDeleted;
        return param;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setShowDeleted(boolean showDeleted) {
        this.showDeleted = showDeleted;
    }

    public void setShowNormal(boolean showNormal) {
        this.showNormal = showNormal;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------

    public boolean isShowDeleted() {
        return showDeleted;
    }

    public boolean isShowNormal() {
        return showNormal;
    }
}
