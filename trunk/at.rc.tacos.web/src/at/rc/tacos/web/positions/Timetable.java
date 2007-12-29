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

public class Timetable {
	
	private static Timetable instance;
	private static String timetable;
	private int height;
	private int width;
	private String tabentry;
	private String tabentryHead;
	private String timetableDateHead;
	private String TimeList;


	public Timetable(){
		timetable = "";
		height = 0;
		width = 0;
		tabentry = "";
		tabentryHead = "";
		timetableDateHead ="";
		TimeList="";
		
	}
	
	public String TimetableInfo(List<StaffMember> rosterList){
		String info="";
		
		return info;
	}
	public String calculateTimetable(List<StaffMember> rosterList, int daysToShow){
		
		boolean ok1 = true;
		boolean ok2 = true;
		
		SimpleDateFormat format = new SimpleDateFormat("E, dd.MM.yyyy");
		SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
		int zaehle = 0;
		String info="";
		
		
		
		TimeList += "<div id='mainDayContainerTL'><div style=' padding:5px; width:100%; height:25px; vertical-align:middle; text-align:center;' >Zeit</div><div style='width:50px; height:400px;' id='TimeTab' align='center'>";
		int i = 5;
		do {
			if(i>24){
				i=1;
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
		
		TimeList+="</div></div>";
		
		if(rosterList.isEmpty()!=true){
				for(int j=1;j<=daysToShow;j++){
					tabentry+="<div id='mainDayContainer'><div style=' padding:5px; width:100%%; height:25px; ' ><b>" + format.format(new Date()) +  "</b></div><div style='height:400px; padding:5px; ' id='MainDivDay'>";
					for(AbstractMessage message:rosterList)
					{
						
						RosterEntry entry = (RosterEntry)message;
						timetableDateHead = "<div style='width:100%; height:25px; text-align:left; vertical-align:middle; padding-left:10px; font-size:14px;'><b>" + format.format(new Date(entry.getPlannedStartOfWork())) +  "</b></div>";

	/* Table-format
	 * info = "<table><tr><td colspan='2'>INFORMATION</td></tr>" +
									"<tr><td width='40%'>Name: <b></td><td width='60%'>"+ entry.getStaffMember().getUserName()+"</b></td></tr>" +
									"<tr><td width='40%'>Dienst als: <b></td><td width='60%'>"+ entry.getJob() + "</b></td></tr>" +
									"<tr><td width='40%'>Dienstdatum: </td><td width='60%'>" + format.format(new Date(entry.getPlannedStartOfWork())) + "</td></tr>" +
									"<tr><td width='40%'>Dienstzeit: </td><td width='60%'>" +formatHour.format(new Date(entry.getPlannedStartOfWork()))+ " - " + formatHour.format(new Date(entry.getPlannedEndOfWork())) + "</td></tr>" +
									"<tr><td width='40%'>Ortstelle: </td><td width='60%'>" + entry.getStation() + "</td></tr>" +
									"<tr><td width='40%'>angestellt als: </td><td width='60%'>"+entry.getServicetype()+"</td></tr></table>";
							
	 */
						//if(entry.getStation().equalsIgnoreCase(getPrimaryLocation())){
						//if(entry.getStation().equalsIgnoreCase("Bruck - Kapfenberg")){
							
							zaehle++;
							info = "INFORMATION<br /><br />Name:&nbsp;&nbsp;<b>"+ entry.getStaffMember().getUserName()+"</b><br />" +
									"Dienst als:&nbsp;&nbsp;<b>"+ entry.getJob() + "<br /></b>" +
									"Dienstdatum:&nbsp;&nbsp;" + format.format(new Date(entry.getPlannedStartOfWork())) + "<br />" +
									"Dienstzeit:&nbsp;&nbsp;" +formatHour.format(new Date(entry.getPlannedStartOfWork()))+ " - " + formatHour.format(new Date(entry.getPlannedEndOfWork())) + "<br />" +
									"Ortstelle:&nbsp;&nbsp;" + entry.getStation() + "<br />" +
									"angestellt als:&nbsp;&nbsp;"+entry.getServicetype()+"<br />";
							
							tabentry+= 		
								"<div id='singleEntryDiv' style='cursor:pointer; height:" + 
								this.calculateHeightForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork())), formatHour.format(new Date(entry.getPlannedEndOfWork()))) +
								"px; margin-top:" + this.calculateStartForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork()))) +
								"px; float:left;  border-width:1px; border-style:solid; border-color:#E5E4E0; background-color:#CECE52;'><a href='#'><img src='../image/info.jpg' name='info' alt='I'  class='hidefocus' /><span>" + info + "</span></a></div>";
						//}
						
						
					}
					tabentry+="</div></div>";
					
				}//timetableDateHead+
				
				timetable+=TimeList+tabentryHead+tabentry;
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
		int retval = 0;
	
		if(endPos<startPos){
			retval = ((24+endPos)-startPos)*15;
		}else{
			retval = (endPos-startPos)*15;
		}
		System.out.println("ENDPOS: "+endPos);
		return retval;
	}

	private int calculateStartForEntry(String begin){
		int startPos = (Integer.valueOf( begin.substring(0, 2) ).intValue());
		if(startPos<5){
			startPos = (Integer.valueOf( begin.substring(0, 2) ).intValue())+19;
		}else{
			startPos = (Integer.valueOf( begin.substring(0, 2) ).intValue())-5;
		}
		return (startPos*15)-2;
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
