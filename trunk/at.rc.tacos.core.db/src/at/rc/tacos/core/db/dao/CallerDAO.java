package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.CallerDetail;

public interface CallerDAO 
{
	public int addCaller(CallerDetail notifierDetail);
	public void updateCaller(CallerDetail notifierDetail);
	public void removeCaller(CallerDetail notifierDetail);
	
	public CallerDetail getCallerByID(String callerID);
	public List<CallerDetail> listCallers();
}
