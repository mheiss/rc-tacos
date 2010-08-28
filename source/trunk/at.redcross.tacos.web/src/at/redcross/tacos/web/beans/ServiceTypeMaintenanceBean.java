package at.redcross.tacos.web.beans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.redcross.tacos.dbal.entity.ServiceType;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.DtoState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "serviceTypeMaintenanceBean")
public class ServiceTypeMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = 9185867004047296041L;

	private final static Log logger = LogFactory.getLog(ServiceTypeMaintenanceBean.class);

	/** the available serviceTypes */
	private List<GenericDto<ServiceType>> serviceTypes;

	/** the id of the selected serviceTypes */
	private long serviceTypeId;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			serviceTypes = DtoHelper.fromList(ServiceType.class, ServiceTypeHelper.list(manager));
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------

	public void removeServiceType(ActionEvent event) {
		Iterator<GenericDto<ServiceType>> iter = serviceTypes.iterator();
		while (iter.hasNext()) {
			GenericDto<ServiceType> dto = iter.next();
			ServiceType serviceType = dto.getEntity();
			if (serviceType.getId() != serviceTypeId) {
				continue;
			}
			if (dto.getState() == DtoState.NEW) {
				iter.remove();
			}

			dto.setState(DtoState.DELETE);
		}
	}

	public void unremoveServiceType(ActionEvent event) {
		for (GenericDto<ServiceType> dto : serviceTypes) {
			ServiceType serviceType = dto.getEntity();
			if (serviceType.getId() != serviceTypeId) {
				continue;
			}
			dto.setState(DtoState.SYNC);
		}
	}

	public void addServiceType(ActionEvent event) {
		GenericDto<ServiceType> dto = new GenericDto<ServiceType>(new ServiceType());
		dto.setState(DtoState.NEW);
		serviceTypes.add(dto);
	}

	public void saveServiceTypes() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			DtoHelper.syncronize(manager, serviceTypes);
			EntityManagerHelper.commit(manager);
			DtoHelper.filter(serviceTypes);
		} catch (Exception ex) {
			logger.error("Failed to remove serviceType '" + serviceTypeId + "'", ex);
			FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
		} finally {
			EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setServiceTypeId(long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getServiceTypeId() {
		return serviceTypeId;
	}

	public List<GenericDto<ServiceType>> getServiceTypes() {
		return serviceTypes;
	}
}
