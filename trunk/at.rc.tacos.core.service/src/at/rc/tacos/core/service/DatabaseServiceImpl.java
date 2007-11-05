package at.rc.tacos.core.service;

import at.rc.tacos.core.db.DbWrapper;

/**
 * Provides access to the database layer
 * @author Michael
 */
public class DatabaseServiceImpl implements IDatabaseLayer
{
	@Override
	public void queryItem() 
	{
		DbWrapper.getDefault().queryItem();
	}
}
