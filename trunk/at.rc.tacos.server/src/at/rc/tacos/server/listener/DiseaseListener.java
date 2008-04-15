package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.DiseaseDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Disease;
import at.rc.tacos.model.QueryFilter;

/**
 * Listens to request form clients to manage the disease
 * @author Michael
 */
public class DiseaseListener extends ServerListenerAdapter 
{
	private DiseaseDAO diseaseDAO = DaoFactory.SQL.createDiseaseDAO();

	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException, SQLException 
	{
		Disease addDisease = (Disease)addObject;
		int id = diseaseDAO.addDisease(addDisease);
		if(id == -1)
			throw new DAOException("DiseaseListener","Failed to add the disease "+addDisease);
		//set the generated id
		addDisease.setId(id);
		return addDisease;
	}

	@Override
	public List<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException 
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<Disease> diseaseList = diseaseDAO.getDiseaseList();
		//assert valid
		if(diseaseList == null)
			throw new DAOException("DiseaseListener","Failed to list the diseases");
		list.addAll(diseaseList);
		return list;
	}

	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException, SQLException 
	{
		Disease removeDisease = (Disease)removeObject;
		if(!diseaseDAO.removeDisease(removeDisease.getId()))
			throw new DAOException("DiseaseListener","Failed to remove the disease: "+removeDisease);
		return removeDisease;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException, SQLException 
	{
		Disease updateDisease = (Disease)updateObject;
		if(!diseaseDAO.updateDisease(updateDisease))
			throw new DAOException("DiseaseListener","Failed to update the disease: "+updateDisease);
		return updateDisease;
	}
}
