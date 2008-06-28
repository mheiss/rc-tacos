package at.rc.tacos.model;

/**
 * DAO Exception is used to encapsulate all error that occured during
 * the query of the database.
 * @author Michael
 */
public class DAOException extends Exception
{	
	/**
	 *  The identification string
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Throws a new DAO exception with a source and a error message.
	 * @param source the source where the exception occured
	 * @param errorMessage the error that occured
	 */
	public DAOException(String source,String errorMessage)
	{
		super("DAOError occured in " + source + ": "+errorMessage);
	}
}
