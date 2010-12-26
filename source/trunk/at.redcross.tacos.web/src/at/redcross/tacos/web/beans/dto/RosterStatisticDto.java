package at.redcross.tacos.web.beans.dto;

import java.math.BigDecimal;

import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.web.beans.WebPermissionBean;
import at.redcross.tacos.web.faces.FacesUtils;

public class RosterStatisticDto extends GenericGroupDto<SystemUser, RosterStatisticEntry> {

    private static final long serialVersionUID = 3542146567592476801L;

    /** the total amount of hours */
    private double amount;

    /**
     * Creates a new statistic entry for the given user
     */
    public RosterStatisticDto(SystemUser systemUser) {
        super(systemUser);
    }

    @Override
    public void addElement(RosterStatisticEntry element) {
        super.addElement(element);
        amount += element.getAmount();
        // rounding is done by using a helper
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(amount));
        amount = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Returns whether or not the current authenticated user can change the user
     * The following restrictions are considered:
     * <ul>
     * <li>Principal must have the permission of the statistic administrator</li>
     * </ul>
     */
    public boolean isStatisticAdminAllowed() {
        // editing is allowed for principals with permission
        if (FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToViewAdminStatistic()) {
            return true;
        }
        // edit denied
        return false;
    }

    public double getAmount() {
        return amount;
    }
}
