package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;

public class CompetenceListener extends ServerListenerAdapter
{
	//the DAO
	private CompetenceDAO compDao = DaoFactory.MYSQL.createCompetenceDAO();
	
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException,SQLException
    {
        Competence comp = (Competence)addObject;
        int id = compDao.addCompetence(comp);
        //check for error while adding
        if(id == -1)
        	throw new DAOException("CompetenceListener","Failed to add the competence "+ comp +" to the database");
        comp.setId(id);
        return comp;
    }

    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
    {
    	ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
    	List<Competence> competenceList = compDao.listCompetences();
    	
    	//check the result
    	if(competenceList == null)
    		throw new DAOException("CompetenceListener","Failed to list the competences");
    	
    	//add all returned competences
    	for(AbstractMessage ab:competenceList)
    		list.add(ab);
    	
        return list;
    }

    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException,SQLException
    {
    	Competence comp = (Competence)removeObject;
    	if(!compDao.removeCompetence(comp.getId()))
    		throw new DAOException("CompetenceListener","Failed to remove the competence "+ comp);
    	return comp;
    }

    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException,SQLException
    {
    	Competence comp = (Competence)updateObject;
    	if(!compDao.updateCompetence(comp))
    		throw new DAOException("CompetenceListener","Failed to update the competence: "+comp);
    	return comp;
    }
}
