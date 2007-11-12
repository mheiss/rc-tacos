package at.rc.tacos.common;

/**
 * This interface is designed to offer the server the 
 * chance to access the database layer throught the service layer.
 * @author Michael
 */
public interface IDatabaseLayer
{
	public void queryItem();
}
