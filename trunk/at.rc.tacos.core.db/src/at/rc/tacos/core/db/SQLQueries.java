package at.rc.tacos.core.db;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This is a helper class to load the queries from the queries.properties file
 * @author Michael
 */
public class SQLQueries 
{
	//the properties file
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.sqlqueries";
	//the shared instance
	protected static SQLQueries instance;
	
	//the map containing the queries
	private final Map<String,String> queries = new HashMap<String, String>();
	
	/**
	 * Default class constructor, loads the properties file.
	 */
	private SQLQueries()
	{
		loadQueries();
	}
	
	/**
	 * Returns the shared instance
	 * @return the instance
	 */
	public static SQLQueries getInstance()
	{
		//create new if new do not have one
		if(instance == null)
			instance = new SQLQueries();
		return instance;
	}
	
	/**
	 * Loads all properties from the given file and stores them in the map
	 */
	private void loadQueries()
	{
		ResourceBundle queryBundle = ResourceBundle.getBundle(QUERIES_BUNDLE_PATH);
		//loop and load all keys
		for(String queryName:queryBundle.keySet())
		{
			String queryValue = queryBundle.getString(queryName);
			//store in the map
			queries.put(queryName, queryValue);
		}
	}
	
	/**
	 * Returns the query identified by the given queryName in the queries.properties file.
	 * @param key the name of the key to get the query for
	 * @return the query value or null if the queryName is not defined in the properties file
	 */
	public String getStatment(String key)
	{
		//assert we have the key
		if(!queries.containsKey(key))
			return null;
		//return the value for the key
		return queries.get(key);
	}
}
