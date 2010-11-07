package at.redcross.tacos.web.beans;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import at.redcross.tacos.dbal.query.InfoQueryParam;
import at.redcross.tacos.web.beans.dto.InfoDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.reporting.ReportRenderer;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

public abstract class InfoOverviewBean extends BaseBean {

	private static final long serialVersionUID = 6781733157974000763L;

	/** the id of the selected info entry */
	private long infoId;

	/** the available locations */
	protected List<Location> locations;
	protected List<Category> categories;
	
	/** filter by category */
	protected String locationName = "*";
	protected String categoryName = "*";
	
	/** filter by location*/
	protected Location location;
	protected List<SelectItem> locationItems;
	
	/** filter by category */
	protected Category category;
	protected List<SelectItem> categoryItems;

	/** query result */
	protected List<InfoDto> infos;
	
	/** filter by */
	protected String shortName;
	protected Date displayStartDate;
	protected Date displayEndDate;
	

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
			categories = CategoryHelper.list(manager);
			categoryItems = DropDownHelper.convertToItems(categories);
			infos = getEntries(manager, getParamForQuery());
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
            // provide default value if category is null
            if (category == null) {
                category = getCategoryByName(categoryName);
            }
            manager = EntityManagerFactory.createEntityManager();
            infos = getEntries(manager, getParamForQuery());
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void tabChanged(ValueChangeEvent event) {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            infos = getEntries(manager, getParamForQuery());
        }
        finally {
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
	
	public void markToDelete(ActionEvent event) {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            Iterator<InfoDto> iter = infos.iterator();
            while (iter.hasNext()) {
                Info info = iter.next().getEntity();
                if (info.getId() != infoId) {
                    continue;
                }
                info.setToDelete(true);
                manager.merge(info);
                iter.remove();
            }
            EntityManagerHelper.commit(manager);
        }
        catch (Exception ex) {
            FacesUtils.addErrorMessage("Der Info-Eintrag konnte nicht gelöscht werden");
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

	/** Loads the info entries using the given filter parameters */
	protected abstract List<InfoDto> getEntries(EntityManager manager, InfoQueryParam param);

	/** Returns the parameters for the report generation */
	protected abstract ReportRenderParameters getReportParams();
	
	// ---------------------------------
	// Helper methods
	// ---------------------------------
	protected InfoQueryParam getParamForQuery() {
        InfoQueryParam param = new InfoQueryParam();
        param.location = getLocationByName(locationName);
        param.category = category;
        return param;
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
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getInfoId() {
		return infoId;
	}
	
	public InfoDto getInfo() {
        for (InfoDto dto : infos) {
            if (dto.getEntity().getId() != infoId) {
                continue;
            }
            return dto;
        }
        return null;
    }

	public Location getLocation() {
		return location;
	}

	public List<InfoDto> getInfos() {
		return infos;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
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
}
