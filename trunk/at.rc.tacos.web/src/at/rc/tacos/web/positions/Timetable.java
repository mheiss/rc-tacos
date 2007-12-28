package at.rc.tacos.web.positions;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;

public class Timetable extends HttpServlet {
	
	private static Timetable instance;
	private static String timetable;
	private int height;
	private int width;
	private String tabentry;
	private String timetableDateHead;
	private String TimeList;

	public Timetable(){
		timetable = "";
		height = 0;
		width = 0;
		tabentry = "";
		timetableDateHead ="";
		TimeList="";
	}
	
	public String calculateTimetable(List<StaffMember> rosterList, String dateNow){
		
		boolean ok1 = true;
		boolean ok2 = true;
		int entryCount = 0;
		
		SimpleDateFormat format = new SimpleDateFormat("E, dd.MM.yyyy");
		SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
		String date;
		
		
		
		TimeList += "<div style='float:left;' >&nbsp;</div><div style='float:left; margin-left:5px; padding:5px; border-width:1px; border-style:solid; border-color:red; width:50px; height:400px;' id='TimeTab' align='center'>";
		int i = 5;
		do {
			if(i>24){
				i=0;
				ok2=false;
			}
			if(i>4 && !ok2){
				ok1=false;
			}
			if(i<10){
				TimeList+="<div id='timeList'>0"+i+":00</div>";
			}else{
				TimeList+="<div  id='timeList'>"+i+":00</div>";
			}
			i++;
			
		}while(ok1);
		
		TimeList+="</div>";
		if(rosterList.isEmpty()!=true){
	
				tabentry+="<div style='float:left; margin-left:5px; border-width:1px; border-style:solid; border-color:red; width:13.5%; height:400px; padding:5px; ' id='MainDivDay'>";
				entryCount = rosterList.size();
				for(AbstractMessage message:rosterList)
				{
					
					RosterEntry entry = (RosterEntry)message;
					if(entry.isSplitEntry()){
						date = format.format(new Date(entry.getPlannedStartOfWork()));
						timetableDateHead = "<div style='width:100%; height:25px; text-align:left; vertical-align:middle; padding-left:10px;'><b>" + dateNow + " - " + entry.getStaffMember().getPrimaryLocation() + "</b></div>";
					}
					else{
						date = formatHour.format(new Date(entry.getPlannedStartOfWork()));	
					}
					formatHour.format(new Date(entry.getPlannedStartOfWork()));
					formatHour.format(new Date(entry.getPlannedEndOfWork()));
					if(entry.getStation().equalsIgnoreCase("Bruck - Kapfenberg")){
						tabentry+= 		
							"<div onmouseover='showInfo();' style='float:left; width:"+ (100/entryCount) +"%;'>" +entry.getRosterId()+entry.getStaffmemberId() + "<br></div>";
					}
					
					
				}
				
				tabentry+="</div>";
				timetable+=timetableDateHead+TimeList+tabentry;
				tabentry="";
			return timetable;
		}
		else{
			return "kein Eintrag gefunden!";
		}
		
	}




	private void TABLE(){
		timetable+="<table border='1' width='300'>"+timetable+"</table>";
	}

//	private void TABLEROW(){
//		timetable="<tr>"+timetable+"</tr>";
//	}
//	
//	private void TABLECOLUMN(){
//		timetable="<td>"+timetable+"</td>";
//	}
	private void DIV(){
		timetable+="<div style='float:left; margin-left:5px;' width='300'>"+timetable+"</div>";
	}
    /**
     * Creates a new instance of this class or returns the 
     * previousely used instance.
     * @return a instance of the <code>Timetable</code> class.
     */
    public static synchronized Timetable getInstance() //step 1
    {
    	
        //do we have a valid instance?
        if(instance == null)
            //create a new and return it
            return new Timetable();
        else    
            return instance;
    }
    
    /**
     * GETTER and SETTERS
     * 
     */
	public static String getTimetable() {
		return timetable;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}   
}
