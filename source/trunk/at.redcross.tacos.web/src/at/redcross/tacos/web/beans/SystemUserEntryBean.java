package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Competence;
import at.redcross.tacos.dbal.entity.Gender;
import at.redcross.tacos.dbal.entity.Group;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.faces.combo.DropDownItem;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "systemUserEntryBean")
public class SystemUserEntryBean extends BaseBean {

    private static final long serialVersionUID = 3440196753805921232L;

    // the system user and login
    private long sysUserId = -1;
    private SystemUser systemUser;
    private Login login;

    private List<SelectItem> genderItems;
    private List<SelectItem> competenceItems;
    private List<SelectItem> locationItems;

    // TODO groups?
    private Collection<Group> selectedGroups;

    @Override
    public void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            loadfromDatabase(manager, sysUserId);

            // competences
            List<Competence> competences = manager.createQuery("from Competence", Competence.class)
                    .getResultList();
            competenceItems = DropDownHelper.convertToItems(competences);

            // location
            List<Location> locations = manager.createQuery("from Location", Location.class)
                    .getResultList();
            locationItems = DropDownHelper.convertToItems(locations);

            // gender
            genderItems = new ArrayList<SelectItem>();
            genderItems.add(new DropDownItem("männlich", Gender.MALE).getItem());
            genderItems.add(new DropDownItem("weiblich", Gender.FEMALE).getItem());
            genderItems.add(new DropDownItem("unbekannt", Gender.UNKNOWN).getItem());

        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Actions
    // ---------------------------------
    /**
     * Persists the current entity in the database
     */
    public String persist() {
        systemUser.setLogin(login);
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            if (isNew()) {
                manager.persist(systemUser);
                manager.persist(login);
            }
            else {
                manager.merge(systemUser);
                manager.merge(login);
            }
            EntityManagerHelper.commit(manager);
            return "pretty:employee-systemUserView";
        }
        catch (Exception ex) {
            FacesUtils.addErrorMessage("Der Mitarbeitereintrag konnte nicht gespeichert werden");
            return null;
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    /**
     * Reverts any changes that may have been done
     */
    public String revert() {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            loadfromDatabase(manager, systemUser.getId());
            return "pretty:employee-edit";
        }
        catch (Exception ex) {
            FacesUtils.addErrorMessage("Der Mitarbeitereintrag konnte nicht zurückgesetzt werden");
            return null;
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Helper methods
    // ---------------------------------
    private void loadfromDatabase(EntityManager manager, long id) {
        systemUser = manager.find(SystemUser.class, id);
        if (systemUser == null) {
            sysUserId = -1;
            systemUser = new SystemUser();
        }
        selectedGroups = systemUser.getGroups();
        login = systemUser.getLogin();
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public boolean isNew() {
        return sysUserId == -1;
    }

    public List<SelectItem> getLocationItems() {
        return locationItems;
    }

    public List<SelectItem> getCompetenceItems() {
        return competenceItems;
    }

    public List<SelectItem> getGenderItems() {
        return genderItems;
    }

    public Login getLogin() {
        return login;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }
}
