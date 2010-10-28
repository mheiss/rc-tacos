package at.redcross.tacos.web.beans;

import java.util.ArrayList;




import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.Category;
import at.redcross.tacos.dbal.entity.Info;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.helper.CategoryHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.CategoryInfo;
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

	/** the available locations */
	protected List<Location> locations;
	protected List<Category> categories;
	
	/** filter by category */
	protected String locationName = "*";
	protected String categoryName = "*";
	
	// filter by location
	protected Location location;
	protected List<SelectItem> locationItems;
	
	protected Category category;
	protected List<SelectItem> categoryItems;

	// queried result
	protected List<Info> infos;
	protected List<LocationInfo> locationInfo;
	protected List<CategoryInfo> categoryInfo;
	
	protected String shortName;
	

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
			locations = LocationHelper.list(manager);
			locationItems = DropDownHelper.convertToItems(locations);
			categoryItems = DropDownHelper.convertToItems(CategoryHelper.list(manager));
			location = getLocationByName(locationName);
			category = getCategoryByName(categoryName);
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
			loadfromDatabase(manager, category);
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
	
	protected Location getLocationByName(String locationName) {
		if (locationName == null || "*".equals(locationName)) {
			return null;
		}
		for (Location location : locations) {
			if (location.getName().equals(locationName)) {
				return location;
			}
		}
		return null;
	}
	
	protected Category getCategoryByName(String categoryName) {
		if (categoryName == null || "*".equals(categoryName)) {
			return null;
		}
		for (Category category : categories) {
			if (category.getName().equals(categoryName)) {
				return category;
			}
		}
		return null;
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
	
	protected abstract List<Info> getEntries(EntityManager manager, Location location, Category category);
	
	/** Loads the info entries using the given filter parameters */
	protected abstract List<Info> getEntries(EntityManager manager, Category category);

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
	
	protected void loadfromDatabase(EntityManager manager, Category filterCategory) {
		// build a structure containing all results grouped by categories
		infos = new ArrayList<Info>();
		Map<Category, List<Info>> mappedResult = new HashMap<Category, List<Info>>();
		for (Info info : getEntries(manager, filterCategory)) {
			infos.add(info);

			Category category = info.getCategory();
			List<Info> list = mappedResult.get(category);
			if (list == null) {
				list = new ArrayList<Info>();
				mappedResult.put(category, list);
			}
			list.add(info);
		}
		// map this structure again for visualization
		categoryInfo = new ArrayList<CategoryInfo>();
		for (Map.Entry<Category, List<Info>> info : mappedResult.entrySet()) {
			CategoryInfo value = new CategoryInfo(info.getKey(), info.getValue());
			categoryInfo.add(value);
		}
	}
	
	public void tabChanged(ValueChangeEvent event) {
		EntityManager manager = null;
		try {
			categoryName = "*";
			category = getCategoryByName(categoryName);
			manager = EntityManagerFactory.createEntityManager();
			infos = getEntries(manager, getLocationByName(locationName), getCategoryByName(categoryName));
		} finally {
			manager = EntityManagerHelper.close(manager);
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
	
	public void setLocationName(String locationName) {
		this.locationName = locationName;
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
	
	public List<Location> getLocations() {
		return locations;
	}
	
	public String getLocationName() {
		return locationName;
	}
	
	public String getShortName(){
		return shortName;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	
	public List<Category> getCategories() {
		return categories;
	}
	
	public List<CategoryInfo> getCategoryInfo() {
		return categoryInfo;
	}
}
