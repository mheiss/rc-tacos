package at.redcross.tacos.dbal.query;

import java.io.Serializable;

import at.redcross.tacos.dbal.entity.Competence;
import at.redcross.tacos.dbal.entity.Group;

public class SystemUserQueryParam implements Serializable {

    private static final long serialVersionUID = 4849044426741554528L;

    /** show deleted entries */
    public boolean stateDelete = false;

    /** show normal entries */
    public boolean stateNormal = true;

    /** show locked entries */
    public boolean stateLocked = false;

    /** filter by name */
    public String userName;

    /** filter by group */
    public Group group;

    /** filter by competence */
    public Competence competence;

    /** filter by location */
    public String locationName = "*";

    // ---------------------------------
    // Getters and Setters
    // ---------------------------------
    public boolean isStateDelete() {
        return stateDelete;
    }

    public void setStateDelete(boolean stateDelete) {
        this.stateDelete = stateDelete;
    }

    public boolean isStateLocked() {
        return stateLocked;
    }

    public void setStateLocked(boolean stateLocked) {
        this.stateLocked = stateLocked;
    }

    public boolean isStateNormal() {
        return stateNormal;
    }

    public void setStateNormal(boolean stateNormal) {
        this.stateNormal = stateNormal;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Competence getCompetence() {
        return competence;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}
