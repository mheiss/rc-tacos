package at.redcross.tacos.web.beans.dto;

import java.util.ArrayList;

import java.util.List;

import at.redcross.tacos.dbal.entity.Info;

/**
 * The {@code InfoDto} is an extended {@linkplain GenericDto DTO} that
 * provides additional information about a info that is needed during
 * rendering
 */
public class InfoDto extends GenericDto<Info> {

	private static final long serialVersionUID = -2767854944455206058L;

	public InfoDto(Info info) {
		super(info);
	}

	/**
	 * Converts the provided list of infos into a list of info DTO
	 * objects
	 * 
	 * @param entities
	 *            the list of info to convert
	 * @return the wrapped entries
	 */
	public static List<InfoDto> fromList(List<Info> entities) {
		List<InfoDto> resultList = new ArrayList<InfoDto>();
		for (Info entity : entities) {
			resultList.add(new InfoDto(entity));
		}
		return resultList;
	}

	/**
	 * Returns whether or not the current authenticated user can edit a info
	 * entry. The following restrictions are considered:
	 * <ul>
	 * <li>The entry must not be deleted</li>
	 * <li>Principal must have the permission to edit the entry</li>
	 * </ul>
	 */
	public boolean isEditEnabled() {
		// info is already deleted
		if (entity.isToDelete()) {
			return false;
		}
//		// editing is allowed for principals with permission
//		if (FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToEditInfo()) {
//			return true;
//		}
		// edit denied
		return false;
	}

	/**
	 * Returns whether or not the current authenticated user can delete a info
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
//		return FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToDeleteInfo();
		return true;
	}

}
