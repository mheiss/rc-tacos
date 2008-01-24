package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.TestDataSource;

public class CompetenceDAOMemory implements CompetenceDAO
{
	//the shared instance
	private static CompetenceDAOMemory instance;

	//the list of all competences
	private List<Competence> competenceList;
	
	/**
	 * Default class constructor
	 */
	private CompetenceDAOMemory()
	{
		competenceList = new ArrayList<Competence>();
		//add test data
		for(Competence comp:TestDataSource.getInstance().competenceList)
			competenceList.add(comp);
	}
	
	/**
	 * Returns the shared instance
	 * @return the instance
	 */
	public static CompetenceDAOMemory getInstance()
	{
		if(instance == null)
			instance = new CompetenceDAOMemory();
		return instance;
	}
	
	@Override
	public int addCompetence(Competence competence) 
	{
		competenceList.add(competence);
		return competenceList.size();
	}

	@Override
	public Competence getCompetenceById(int id) 
	{
		return competenceList.get(id);
	}

	@Override
	public List<Competence> listCompetences() 
	{
		return competenceList;
	}

	@Override
	public boolean removeCompetence(int id) 
	{
		if(competenceList.remove(id) != null)
			return true;
		//failed to remove
		return false;
	}

	@Override
	public boolean updateCompetence(Competence competence)
	{
		int index = competenceList.indexOf(competence);
		competenceList.set(index, competence);
		return true;
	}
}
