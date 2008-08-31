package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.QueryFilter;

public class DialysisPatientListener extends ServerListenerAdapter
{
	private DialysisPatientDAO dialysisDao = DaoFactory.SQL.createDialysisPatientDAO();
	//the logger
	private static Logger logger = Logger.getLogger(DialysisPatientListener.class);

	/**
	 * Add a roster entry
	 */
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException,SQLException
	{
		DialysisPatient patient = (DialysisPatient)addObject;
		int id = dialysisDao.addDialysisPatient(patient);
		if(id == -1)
			throw new DAOException("DialysisPatientListener","Failed to add the dialysis patient: "+patient);
		patient.setId(id);
		logger.info("added by:" +username +";" +patient);
		return patient;
	}

	/**
	 * Listing of all entries 
	 * @throws ParseException 
	 */
	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<DialysisPatient> patientList = new ArrayList<DialysisPatient>();
		
		//if there is no filter -> request all
		if(queryFilter == null || queryFilter.getFilterList().isEmpty())
		{
			patientList = dialysisDao.listDialysisPatient();
			if(patientList == null)
				throw new DAOException("DialysisPatientListener","Failed to list the dialysis patients");
			list.addAll(patientList);
		}
		else if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER))
		{
			//get the query filter
			final String filter = queryFilter.getFilterValue(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);
			
			DialysisPatient patient = dialysisDao.getDialysisPatientById(id);
			//check the result
			if(patient == null)
				throw new DAOException("DialysisPatientListener","Failed to get the dialysis patient with the id "+id);

			list.add(patient);
		}
		//return the list
		return list;
	}

	/**
	 * Remove a dialysis patient
	 */
	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException,SQLException
	{
		DialysisPatient patient = (DialysisPatient)removeObject;
		if(!dialysisDao.removeDialysisPatient(patient.getId()))
			throw new DAOException("DialysisPatientListener","Failed to update the dialysis patient "+patient);
		return patient;
	}

	/**
	 * Update a roster entry
	 */
	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException,SQLException
	{
		DialysisPatient patient = (DialysisPatient)updateObject;
		if(!dialysisDao.updateDialysisPatient(patient))
			throw new DAOException("DialysisPatientListener","Failed to update the dialysis patient "+patient);
		logger.info("updated by: " +username +";" +patient);
		return patient;
	}
}
