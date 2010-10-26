package at.redcross.tacos.web.beans;

import java.util.ArrayList;




import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.Category;
import at.redcross.tacos.dbal.entity.Info;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.helper.CategoryHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.LocationInfo;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.reporting.ReportRenderer;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

public abstract class InfoOverviewBean extends BaseBean {

	private static final long serialVersionUID = 6781733157974000763L;

	/** the id of the selected info entry */
	private long infoId;

	/* the entry to remove */
	private Info info;

	// filter by location
	protected Location location;
	protected List<SelectItem> locationItems;
	
	protected Category category;
	protected List<SelectItem> categoryItems;

	// queried result
	protected List<Info> infos;
	protected List<LocationInfo> locationInfo;

	// sign in and sign out date
	protected Date now;

	// ---------------------------------
	// Initialization
	// ---------------------------------
	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			locationItems = DropDownHelper.convertToItems(LocationHelper.list(manager));
			categoryItems = DropDownHelper.convertToItems(CategoryHelper.list(manager));
			loadfromDatabase(manager, location);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Actions
	// ---------------------------------
	public void filterChanged(ActionEvent event) {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, location);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------
	public void createPdfReport(ActionEvent event) {
		try {
			// define the parameters for the report
			ReportRenderParameters params = getReportParams();

			// render the report
			ReportRenderer.getInstance().renderReport(params);
		} catch (Exception e) {
			FacesUtils.addErrorMessage("Failed to create report");
		}
	}


	public String markToDelete(ActionEvent event) {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, infoId);
			info.setToDelete(true);
			manager.merge(info);
			EntityManagerHelper.commit(manager);
			loadfromDatabase(manager, location);
			return FacesUtils.pretty("info-currentOverview");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Die Information konnte nicht gelöscht werden");
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

	/** Loads the info entries using the given filter parameters */
	protected abstract List<Info> getEntries(EntityManager manager, Location location);

	/** Returns the parameters for the report generation */
	protected abstract ReportRenderParameters getReportParams();

	// ---------------------------------
	// Private API
	// ---------------------------------
	protected void loadfromDatabase(EntityManager manager, Location filterLocation) {
		// build a structure containing all results grouped by locations
		infos = new ArrayList<Info>();
		Map<Location, List<Info>> mappedResult = new HashMap<Location, List<Info>>();
		for (Info info : getEntries(manager, filterLocation)) {
			infos.add(info);

			Location location = info.getLocation();
			List<Info> list = mappedResult.get(location);
			if (list == null) {
				list = new ArrayList<Info>();
				mappedResult.put(location, list);
			}
			list.add(info);
		}
		// map this structure again for visualization
		locationInfo = new ArrayList<LocationInfo>();
		for (Map.Entry<Location, List<Info>> info : mappedResult.entrySet()) {
			LocationInfo value = new LocationInfo(info.getKey(), info.getValue());
			locationInfo.add(value);
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getInfoId() {
		return infoId;
	}

	public Location getLocation() {
		return location;
	}

	public List<Info> getInfos() {
		return infos;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}

	public List<LocationInfo> getLocationInfo() {
		return locationInfo;
	}
	
	public List<SelectItem> getCategoryItems(){
		return categoryItems;
	}
}
