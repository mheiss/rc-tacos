package at.redcross.tacos.web.beans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.redcross.tacos.dbal.entity.Equipment;
import at.redcross.tacos.dbal.helper.EquipmentHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.EntryState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "equipmentMaintenanceBean")
public class EquipmentMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = 9185867004047296041L;

	private final static Log logger = LogFactory.getLog(EquipmentMaintenanceBean.class);

	/** the available equipments */
	private List<GenericDto<Equipment>> equipments;

	/** the id of the selected equipments */
	private long equipmentId;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			equipments = DtoHelper.fromList(Equipment.class, EquipmentHelper.list(manager));
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------

	public void removeEquipment(ActionEvent event) {
		Iterator<GenericDto<Equipment>> iter = equipments.iterator();
		while (iter.hasNext()) {
			GenericDto<Equipment> dto = iter.next();
			Equipment equipment = dto.getEntity();
			if (equipment.getId() != equipmentId) {
				continue;
			}
			if (dto.getState() == EntryState.NEW) {
				iter.remove();
			}

			dto.setState(EntryState.DELETE);
		}
	}

	public void unremoveEquipment(ActionEvent event) {
		for (GenericDto<Equipment> dto : equipments) {
			Equipment equipment = dto.getEntity();
			if (equipment.getId() != equipmentId) {
				continue;
			}
			dto.setState(EntryState.SYNC);
		}
	}

	public void addEquipment(ActionEvent event) {
		GenericDto<Equipment> dto = new GenericDto<Equipment>(new Equipment());
		dto.setState(EntryState.NEW);
		equipments.add(dto);
	}

	public void saveEquipments() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			DtoHelper.syncronize(manager, equipments);
			EntityManagerHelper.commit(manager);
			DtoHelper.filter(equipments);
		} catch (Exception ex) {
			logger.error("Failed to remove equipment '" + equipmentId + "'", ex);
			FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
		} finally {
			EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setEquipmentId(long equipmentId) {
		this.equipmentId = equipmentId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getEquipmentId() {
		return equipmentId;
	}

	public List<GenericDto<Equipment>> getEquipments() {
		return equipments;
	}
}
