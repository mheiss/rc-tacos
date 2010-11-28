package at.redcross.tacos.web.beans.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.DataState;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.web.beans.WebPermissionBean;
import at.redcross.tacos.web.faces.FacesUtils;

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
		if (entity.getState().equals(DataState.DELETE)) {
			return false;
		}
		// editing is allowed for principals with permission
		if (FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToEditRoster()) {
			return true;
		}
		// beginning must be greater than 24hours
		Date current = DateUtils.addDays(new Date(), 1);
		if (current.before(entity.getPlannedStartDateTime())) {
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
	 * T
	 */
	public boolean isDeleteEnabled() {
		// entry is already deleted
		if (entity.getState().equals(DataState.DELETE)) {
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
		if (entity.getState().equals(DataState.DELETE)) {
			return false;
		}
		return FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToAssignCar();
	}

	/**
	 * Returns whether or not the roster entry could checked in or out. The
	 * following restrictions are considered:
	 * <ul>
	 * <li>The service type must allow the signIn/signOut operation</li>
	 * </ul>
	 * T
	 */
	public boolean isCheckInOutEnabled() {
		// entry is already deleted
		if (entity.getState().equals(DataState.DELETE)) {
			return false;
		}
		return entity.getServiceType().isSignInOut();
	}

	/**
	 * Returns the appropriate style class to decorate the resulting list.
	 * Please note that you have to update the <tt>CSS</tt> file when any of the
	 * returned string values is changed.
	 * 
	 * @return the style to apply to this record
	 */
	public String getStyleClass() {
		if (entity.getState().equals(DataState.DELETE)) {
			return "rosterDelete";
		}
		if (entity.isSpecialService()) {
			return "rosterSpecial";
		}
		if (entity.isStandby()) {
			return "rosterStandby";
		}
		return "";
	}

}
