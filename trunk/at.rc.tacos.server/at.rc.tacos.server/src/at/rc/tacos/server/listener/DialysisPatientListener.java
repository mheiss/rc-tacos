package at.rc.tacos.server.listener;

import java.sql.SQLException;
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
	private DialysisPatientDAO dialysisDao = DaoFactory.TEST.createDialysisPatientDAO();

	/**
	 * Add a roster entry
	 */
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject)
	{
		DialysisPatient patient = (DialysisPatient)addObject;
		try
		{
			int id = dialysisDao.addDialysisPatient(patient);
			patient.setPatientId(id);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.getMessage();
		}

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

		System.out.println("New listing request");

		//if there is no filter -> request all
		if(queryFilter == null || queryFilter.getFilterList().isEmpty())
		{
			try
			{
				list.addAll(dialysisDao.listDialysisPatient());
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.getMessage();
			}
		}
		else if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER))
        {
            //get the query filter
            final String filter = queryFilter.getFilterValue(IFilterTypes.ID_FILTER);
            int id = Integer.parseInt(filter);
            try
            {
                list.add(dialysisDao.getDialysisPatientById(id));
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.getMessage();
            }
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
		try
		{
			dialysisDao.removeDialysisPatient(patient);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.getMessage();
		}
		return patient;
	}

	/**
	 * Update a roster entry
	 */
	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
	{
		DialysisPatient patient = (DialysisPatient)updateObject;
		try
		{
			dialysisDao.updateDialysisPatient(patient);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.getMessage();
		}
		return patient;
	}
}
