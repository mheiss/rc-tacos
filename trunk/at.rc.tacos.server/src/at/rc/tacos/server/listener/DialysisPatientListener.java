package at.rc.tacos.server.listener;

import java.text.ParseException;
import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.QueryFilter;

public class DialysisPatientListener extends ServerListenerAdapter
{
	private DialysisPatientDAO dialysisDao = DaoFactory.MYSQL.createDialysisPatientDAO();

	/**
	 * Add a roster entry
	 */
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject)
	{
		DialysisPatient patient = (DialysisPatient)addObject;
		int id = dialysisDao.addDialysisPatient(patient);
		patient.setId(id);
		return patient;
	}

	/**
	 * Listing of all entries 
	 * @throws ParseException 
	 */
	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		//if there is no filter -> request all
		if(queryFilter == null || queryFilter.getFilterList().isEmpty())
		{
			list.addAll(dialysisDao.listDialysisPatient());
		}
		else if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER))
		{
			//get the query filter
			final String filter = queryFilter.getFilterValue(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);

			list.add(dialysisDao.getDialysisPatientById(id));
		}
		//return the list
		return list;
	}

	/**
	 * Remove a roster entry
	 */
	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
	{
		DialysisPatient patient = (DialysisPatient)removeObject;
		dialysisDao.removeDialysisPatient(patient.getId());
		return patient;
	}

	/**
	 * Update a roster entry
	 */
	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
	{
		DialysisPatient patient = (DialysisPatient)updateObject;
		dialysisDao.updateDialysisPatient(patient);
		return patient;
	}
}
