package at.rc.tacos.core.service;

import at.rc.tacos.common.IDatabaseLayer;
import at.rc.tacos.core.db.DbWrapper;

/**
 * Provides access to the database layer
 * @author Michael
 */
public class DatabaseLayerImpl implements IDatabaseLayer
{
	@Override
	public void queryItem() 
	{
		DbWrapper.getDefault().queryItem();
	}
}
