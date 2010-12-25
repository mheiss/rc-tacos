package at.redcross.tacos.dbal.query;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.ServiceType;
import at.redcross.tacos.dbal.entity.SystemUser;

/** Extended query parameters for statistic */
public class RosterStatisticQueryParam extends RosterQueryParam {

    private static final long serialVersionUID = 4784695967395357186L;

    /** the home location of the user */
    public Location homeLocation;

    /** the month of the entry */
    public int month;

    /** the year of the entry */
    public int year;

    /** the service type of the entry */
    public ServiceType serviceType;

    /** the user of the entry */
    public SystemUser systemUser;

    // ---------------------------------
    // Getters and Setters
    // ---------------------------------
    public Location getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }
}
