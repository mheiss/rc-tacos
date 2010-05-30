package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Address;
import at.redcross.tacos.dbal.entity.Competence;
import at.redcross.tacos.dbal.entity.Gender;
import at.redcross.tacos.dbal.entity.Group;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "systemUserEntryBean")
public class SystemUserEntryBean extends BaseBean {

    private static final long serialVersionUID = 3440196753805921232L;

    // the sytem user to create
    private long sysUserId = -1;
    private SystemUser systemUser;

    
    // the suggested values for the UI
    private String selectedAddress;
    private List<Address> addresses;
    private List<SelectItem> addressItems;
    
    private String selectedLogin;
    private List<Login> logins;
    private List<SelectItem> loginItems;
    
    private String selectedLocation;
    private List<Location> locations;
    private List<SelectItem> locationItems;
    
    //?? TODO
    private Collection<Competence> selectedCompetences;
    private List <Competence> competences;
    private List<SelectItem> competenceItems;
    
    private Collection<Group> selectedGroups;   
    
    
    //other
    private String lastname;
    private Calendar birthday;
    private Gender gender;
    private String firstname;
    private String phoneI;
    private String phoneII;
    private int pnr;
    private String notes;
    

    @Override
    public void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            systemUser = loadSystemUser(manager, sysUserId);

            addresses = manager.createQuery("from Address", Address.class).getResultList();
            logins = manager.createQuery("from Login", Login.class).getResultList();
            locations = manager.createQuery("from Location", Location.class).getResultList();

            loginItems = new ArrayList<SelectItem>();
            for (Login login : logins) {
                loginItems.add(new SelectItem(login.getAlias()));
            }  
            
            addressItems = new ArrayList<SelectItem>();
            for (Address address : addresses) {
            addressItems.add(new SelectItem(address.getStreet() +" " +address.getZipCode() + " " +address.getCity()));
            }
            
            locationItems = new ArrayList<SelectItem>();
            for (Location location : locations) {
            locationItems.add(new SelectItem(location.getName()));
            }
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
        // set the missing attributes
    	systemUser.setAddress(lookupAddress(selectedAddress));
    	//systemUser.setCompetences(selectedCompetences);
    	//systemUser.setGroups(selectedGroups);
    	systemUser.setLogin(lookupLogin(selectedLogin));
    	systemUser.setLocation(lookupLocation(selectedLocation));

        // write to the database
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            if (isNew()) {
                manager.persist(systemUser);
            }
            else {
                manager.merge(systemUser);
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
            systemUser = loadSystemUser(manager, systemUser.getId());
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
    private SystemUser loadSystemUser(EntityManager manager, long id) {
        systemUser = manager.find(SystemUser.class, id);
        if (systemUser == null) {
            sysUserId = -1;
            return new SystemUser();
        }
        selectedAddress = systemUser.getAddress().getStreet() +" " +systemUser.getAddress().getZipCode() +" " +systemUser.getAddress().getCity();
        selectedCompetences =systemUser.getCompetences();
        selectedGroups = systemUser.getGroups();
        selectedLogin = systemUser.getLogin().getAlias();
        selectedLocation = systemUser.getLocation().getName();
        lastname = systemUser.getLastName();
        gender = systemUser.getGender();
        birthday = systemUser.getBirthday();
        firstname = systemUser.getFirstName();
        phoneI = systemUser.getPhoneI();
        phoneII = systemUser.getPhoneII();
        pnr = systemUser.getPNr();
        notes = systemUser.getNotes();
        return systemUser;
    }
    
    private Address lookupAddress(Object value) {
        for (Address address : addresses) {
            String addressId = address.getStreet() + " " + address.getZipCode() + " " +address.getCity();
            if (addressId.equals(value)) {
                return address;
            }
        }
        return null;
    }
    private Login lookupLogin(Object value) {
        for (Login login : logins) {
            String loginId = login.getAlias();
            if (loginId.equals(value)) {
                return login;
            }
        }
        return null;
    }
    private Location lookupLocation(Object value) {
        for (Location location : locations) {
            String locationId = location.getName();
            if (locationId.equals(value)) {
                return location;
            }
        }
        return null;
    }

    

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public long getSystemUserId() {
        return sysUserId;
    }
    
    public void setSystemUserId(long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public void setSelectedLogin(String selectedLogin) {
        this.selectedLogin = selectedLogin;
    }

    public void setSelectedAddress(String selectedAddress) {
        this.selectedAddress = selectedAddress;
    } 
    
    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    } 
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }
    
    public void setPhoneI(String phoneI) {
        this.phoneI = phoneI;
    }
    
    public void setPhoneII(String phoneII) {
        this.phoneII = phoneII;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public void setPNr(int pnr) {
        this.pnr = pnr;
    }
    

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public boolean isNew() {
        return sysUserId == -1;
    }

    public String getSelectedLogin() {
        return selectedLogin;
    }

    public String getSelectedAddress() {
        return selectedAddress;
    }


    public List<SelectItem> getLoginItems() {
        return loginItems;
    }

    public List<SelectItem> getAddressItems() {
        return addressItems;
    }
    
    public List<SelectItem> getLocationItems() {
        return addressItems;
    }

    public Calendar getBirthday() {
        return birthday;
    }
    
    public String getPhoneI() {
        return phoneI;
    }
    
    public String getPhoneII() {
        return phoneII;
    }

    public Gender getgender() {
        return gender;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }
    
    public int getPNr() {
    	return pnr;
    }
    
    public String getNotes() {
    	return notes;
    }
    
    public SystemUser getSystemUser() {
        return systemUser;
    }
}
