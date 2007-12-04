package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.NotifierDetail;

public interface CallerDAO 
{
	public int addCaller(NotifierDetail notifierDetail);
	public void updateCaller(NotifierDetail notifierDetail);
	public void removeCaller(NotifierDetail notifierDetail);
	
	public NotifierDetail getCallerByID(String callerID);
	public List<NotifierDetail> listCallers();
}
