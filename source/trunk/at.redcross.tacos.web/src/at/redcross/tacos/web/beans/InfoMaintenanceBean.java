package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Info;
import at.redcross.tacos.dbal.helper.CategoryHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.utils.EntityUtils;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItemHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "infoMaintenanceBean")
public class InfoMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = 1183050998400865811L;
	// the info to create or edit
	private long infoId = -1;
	private Info info;

	// the suggested values for the drop down boxes
	private List<SelectItem> locationItems;
	private List<SelectItem> categoryItems;
	
	// the maximum length of the information
	private int maxDescLength = -1;

	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, infoId);
			if (!isEditEnabled()) {
				FacesUtils.redirectAccessDenied("Entry '" + info + "' cannot be edited");
				return;
			}
			locationItems = SelectableItemHelper.convertToItems(LocationHelper.list(manager));
			categoryItems = SelectableItemHelper.convertToItems(CategoryHelper.list(manager));
			maxDescLength = EntityUtils.getColumnLength(Info.class, "description");
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------
	public String persist() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			if (!isBackedUp()) {
				manager.persist(info);
			} else {
				manager.merge(info);
			}
			EntityManagerHelper.commit(manager);
			return FacesUtils.pretty("info-currentOverview");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Die Information konnte nicht gespeichert werden");
			return null;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	public String revert() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, info.getId());
			return FacesUtils.pretty("info-editMaintenance");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Die Information konnte nicht zurückgesetzt werden");
			return null;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Helper methods
	// ---------------------------------
	private void loadfromDatabase(EntityManager manager, long id) {
		info = manager.find(Info.class, id);
		if (info == null) {
			infoId = -1;
			info = new Info();
		}
	}
	
	/**
	 * Returns whether or not the current authenticated user can edit an info
	 * entry. The following restrictions are considered:
	 * <ul>
	 * <li>The entry must not be deleted</li>
	 * <li>Principal must have the permission to edit the entry</li>
	 * </ul>
	 */
	public boolean isEditEnabled() {
		// info is already deleted
		if (info.isToDelete()) {
			return false;
		}
		// editing is allowed for principals with permission
		if (FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToEditInfo()) {
			return true;
		}
		// edit denied
		return false;
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public boolean isBackedUp() {
		return infoId > 0;
	}
	
	public long getInfoId() {
		return infoId;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}
	
	public List<SelectItem> getCategoryItems() {
		return categoryItems;
	}
	
	public Info getInfo() {
		return info;
	}
    
	public int getMaxDescLength() {
		return maxDescLength;
	}
}
