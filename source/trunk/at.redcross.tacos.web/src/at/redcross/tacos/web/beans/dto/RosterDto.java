package at.redcross.tacos.web.beans.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.web.beans.LocaleBean;
import at.redcross.tacos.web.beans.WebPermissionBean;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.utils.TacosDateUtils;

/**
 * The {@code RosterDto} is an extended {@linkplain GenericDto DTO} that
 * provides additional information about a roster entry that is needed during
 * rendering
 */
public class RosterDto extends GenericDto<RosterEntry> {

	private static final long serialVersionUID = -2887652437985567541L;

	public RosterDto(RosterEntry entry) {
		super(entry);
	}

	/**
	 * Converts the provided list of roster entries into a list of roster DTO
	 * objects
	 * 
	 * @param entities
	 *            the list of roster to convert
	 * @return the wrapped entries
	 */
	public static List<RosterDto> fromList(List<RosterEntry> entities) {
		List<RosterDto> resultList = new ArrayList<RosterDto>();
		for (RosterEntry entity : entities) {
			resultList.add(new RosterDto(entity));
		}
		return resultList;
	}

	/**
	 * Returns whether or not the current authenticated user can edit a roster
	 * entry. The following restrictions are considered:
	 * <ul>
	 * <li>The entry must not be deleted</li>
	 * <li>The beginning date must be greater than 24h</li>
	 * <li>Principal must have the permission to edit the entry</li>
	 * </ul>
	 */
	public boolean isEditEnabled() {
		// entry is already deleted
		if (entity.isToDelete()) {
			return false;
		}
		// editing is allowed for principals with permission
		if (FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToEditRoster()) {
			return true;
		}
		// beginning must be greater than 24hours
		Calendar current = FacesUtils.lookupBean(LocaleBean.class).getCalendar();
		Calendar plannedStart = TacosDateUtils.mergeDateAndTime(entity.getPlannedStartDate(),
				entity.getPlannedStartTime());
		current.add(Calendar.HOUR_OF_DAY, 24);
		if (current.before(plannedStart)) {
			return true;
		}
		// edit denied
		return false;
	}

	/**
	 * Returns whether or not the current authenticated user can delete a roster
	 * entry. The following restrictions are considered:
	 * <ul>
	 * <li>The entry must not be deleted</li>
	 * <li>Principal must have the permission to delete the entry</li>
	 * </ul>
	 */
	public boolean isDeleteEnabled() {
		// entry is already deleted
		if (entity.isToDelete()) {
			return false;
		}
		return FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToDeleteRoster();
	}

	/**
	 * Returns whether or not the current authenticated user can delete a roster
	 * entry. The following restrictions are considered:
	 * <ul>
	 * <li>The entry must not be deleted</li>
	 * <li>Principal must have the permission to delete the entry</li>
	 * </ul>
	 */
	public boolean isAssignCarEnabled() {
		// entry is already deleted
		if (entity.isToDelete()) {
			return false;
		}
		return FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToAssignCar();
	}

}
