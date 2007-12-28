package at.rc.tacos.web.positions;



import java.awt.Color;
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
	
	public String TimetableInfo(List<StaffMember> rosterList){
		String info="";
		
		return info;
	}
	public String calculateTimetable(List<StaffMember> rosterList){
		
		boolean ok1 = true;
		boolean ok2 = true;
		int entryCount = 0;
		
		SimpleDateFormat format = new SimpleDateFormat("E, dd.MM.yyyy");
		SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
		String date;
		int zaehle = 0;
		String info="";
		
		
		
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
					timetableDateHead = "<div style='width:100%; height:25px; text-align:left; vertical-align:middle; padding-left:10px;'><b>" + format.format(new Date(entry.getPlannedStartOfWork())) + " - " + entry.getStaffMember().getPrimaryLocation() + "</b></div>";
					if(entry.isSplitEntry()){
						date = format.format(new Date(entry.getPlannedStartOfWork()));
						
					}
					else{
						date = formatHour.format(new Date(entry.getPlannedStartOfWork()));	
					}

					
					//if(entry.getStation().equalsIgnoreCase("Bruck - Kapfenberg")){
						
						zaehle++;
						info = "INFORMATION<br><br>Name: <b>"+ entry.getStaffMember().getUserName()+"</b><br>Dienst als: <b>"+ entry.getJob() +"</b><br>Dienstzeit: " +formatHour.format(new Date(entry.getPlannedStartOfWork()))+ " - " + formatHour.format(new Date(entry.getPlannedEndOfWork())) + "<br>Ortstelle: " + entry.getStation() + "<br>angestellt als: "+entry.getServicetype()+"<br>";
						tabentry+= 		
							"<div id='singleEntryDiv' style='cursor:pointer; height:" + 
							this.calculateHeightForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork())), formatHour.format(new Date(entry.getPlannedEndOfWork()))) +
							"px; margin-top:" + this.calculateStartForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork()))) +
							"px; float:left;  border-width:1px; border-style:solid; border-color:#E5E4E0; background-color:#CECE52;'><a href='#'><img src='../image/info.jpg' name='info' alt='I'  class='hidefocus' /><span>" + info + "</span></a></div>";
					//}
					
					
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

	private int calculateHeightForEntry(String begin, String end){
		int startPos = Integer.valueOf( begin.substring(0, 2) ).intValue();
		int endPos = Integer.valueOf( end.substring(0, 2) ).intValue();
		
		return (endPos-startPos)*15;
	}

	private int calculateStartForEntry(String begin){
		int startPos = (Integer.valueOf( begin.substring(0, 2) ).intValue())-5;	
		return startPos*15;
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
