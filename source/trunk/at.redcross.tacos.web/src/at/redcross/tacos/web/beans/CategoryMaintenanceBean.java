package at.redcross.tacos.web.beans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.redcross.tacos.dbal.entity.Category;
import at.redcross.tacos.dbal.helper.CategoryHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.EntryState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "categoryMaintenanceBean")
public class CategoryMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = 4578775443747869954L;

	private final static Log logger = LogFactory.getLog(CategoryMaintenanceBean.class);

	/** the available categorys */
	private List<GenericDto<Category>> categorys;

	/** the id of the selected categorys */
	private long categoryId;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			categorys = DtoHelper.fromList(Category.class, CategoryHelper.list(manager));
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------

	public void removeCategory(ActionEvent event) {
		Iterator<GenericDto<Category>> iter = categorys.iterator();
		while (iter.hasNext()) {
			GenericDto<Category> dto = iter.next();
			Category category = dto.getEntity();
			if (category.getId() != categoryId) {
				continue;
			}
			if (dto.getState() == EntryState.NEW) {
				iter.remove();
			}

			dto.setState(EntryState.DELETE);
		}
	}

	public void unremoveCategory(ActionEvent event) {
		for (GenericDto<Category> dto : categorys) {
			Category category = dto.getEntity();
			if (category.getId() != categoryId) {
				continue;
			}
			dto.setState(EntryState.SYNC);
		}
	}

	public void addCategory(ActionEvent event) {
		GenericDto<Category> dto = new GenericDto<Category>(new Category());
		dto.setState(EntryState.NEW);
		categorys.add(dto);
	}

	public void saveCategorys() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			DtoHelper.syncronize(manager, categorys);
			EntityManagerHelper.commit(manager);
			DtoHelper.filter(categorys);
		} catch (Exception ex) {
			logger.error("Failed to remove category '" + categoryId + "'", ex);
			FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
		} finally {
			EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getCategoryId() {
		return categoryId;
	}

	public List<GenericDto<Category>> getCategorys() {
		return categorys;
	}
}
