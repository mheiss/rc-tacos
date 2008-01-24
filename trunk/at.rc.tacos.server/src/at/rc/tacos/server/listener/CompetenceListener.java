package at.rc.tacos.server.listener;

import java.util.ArrayList;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.QueryFilter;

public class CompetenceListener extends ServerListenerAdapter
{
	//the DAO
	private CompetenceDAO compDao = DaoFactory.TEST.createCompetenceDAO();
	
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        Competence comp = (Competence)addObject;
        int id = compDao.addCompetence(comp);
        comp.setId(id);
        return comp;
    }

    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
    	ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
    	list.addAll(compDao.listCompetences());
        return list;
    }

    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
    	Competence comp = (Competence)removeObject;
    	compDao.removeCompetence(comp.getId());
    	return comp;
    }

    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
    	Competence comp = (Competence)updateObject;
    	compDao.updateCompetence(comp);
    	return comp;
    }
}
