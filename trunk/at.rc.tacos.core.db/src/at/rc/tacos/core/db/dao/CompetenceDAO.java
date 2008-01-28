package at.rc.tacos.core.db.dao;

import java.util.List;

import at.rc.tacos.model.Competence;

public interface CompetenceDAO 
{
    public static final String TABLE_NAME = "competences";
    public static final String TABLE_DEPENDENT_NAME = "staffmember_competence";
    
	/**
	 * Adds a new competence to the competence list
	 * @param competence the competence to add
	 * @return the generated it
	 */
	public int addCompetence(Competence competence);
	
	/**
	 * Updates the competence in the database
	 * @param competence the competence to update
	 * @return true if the update was successfully
	 */
	public boolean updateCompetence(Competence competence);
	
	/**
	 * Removes the competence out of the database
	 * @param id the id of the competence to remove
	 * @return true if the remove was successfully
	 */
	public boolean removeCompetence(int id);
	
	/**
	 * Returns the competence identified by the given id
	 * @param id the id of the competence to get
	 * @return the requested competence or null if no competence was found
	 */
	public Competence getCompetenceById(int id);
	
	/**
	 * Lists all competences from the database
	 * @return the list of competences
	 */
	public List<Competence> listCompetences();
	
	/**
	 * Lists all competences of a staffmember
	 * @result competencelist of a staffmember
	 */
	public List<Competence> listCompetencesOfStaffMember(int id);
}
