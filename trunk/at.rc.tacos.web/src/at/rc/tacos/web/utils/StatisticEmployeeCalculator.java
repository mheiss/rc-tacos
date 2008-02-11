package at.rc.tacos.web.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;

public class StatisticEmployeeCalculator {

	private String startDate = null;
	private String splitedDate[] = null;
	
	public StatisticEmployeeCalculator() {
	}	

	private String GetStatData(List<RosterEntry> rosterList){
		//the result listing, that should contain the week result 
		List<AbstractMessage> resultList = new ArrayList<AbstractMessage>(); 
		//the calendar instance with the current date 
		Calendar cal = Calendar.getInstance(); 
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy"); 
		
		
		//if we have no date, use the current date
		//else take the request parameter "startDate"
		if (startDate == null || startDate.trim().isEmpty()){
			startDate = format.format(cal.getTime());
			
		}else{
			splitedDate = startDate.split("-");
			//cal.set(year, month, day)
			cal.set(Integer.parseInt(splitedDate[2]), Integer.parseInt(splitedDate[1])-1, 1);		
		}
		
		//get roster entries 
		List<String> filterdByDate = new ArrayList<String>();
		for(int i=1; i<=cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) 
		{ 
			
		} 
		return null;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
