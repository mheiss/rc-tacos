package at.rc.tacos.server.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.Competence;

public interface CompetenceDAO 
{
    public static final String TABLE_NAME = "competences";
    public static final String TABLE_DEPENDENT_NAME = "staffmember_competence";
    
	/**
	 * Adds a new competence to the competence list
	 * @param competence the competence to add
	 * @return the generated id
	 */
	public int addCompetence(Competence competence) throws SQLException;
	
	/**
	 * Updates the competence in the database
	 * @param competence the competence to update
	 * @return true if the update was successfully
	 */
	public boolean updateCompetence(Competence competence) throws SQLException;
	
	/**
	 * Removes the competence out of the database
	 * @param id the id of the competence to remove
	 * @return true if the remove was successfully
	 */
	public boolean removeCompetence(int id) throws SQLException;
	
	/**
	 * Returns the competence identified by the given id
	 * @param id the id of the competence to get
	 * @return the requested competence or null if no competence was found
	 */
	public Competence getCompetenceById(int id) throws SQLException;
	
	/**
	 * Lists all competences from the database
	 * @return the list of competences
	 */
	public List<Competence> listCompetences() throws SQLException;
	
	/**
	 * Lists all competences of a staffmember.
	 * @param id the id of the staff member to get the competences
	 * @result competencelist of a staffmember
	 */
	public List<Competence> listCompetencesOfStaffMember(int id) throws SQLException;
}
