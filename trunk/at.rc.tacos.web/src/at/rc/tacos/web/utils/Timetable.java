package at.rc.tacos.web.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.web.web.Dispatcher;

public class Timetable {
	
	private static Timetable instance;
	private static String timetable;
	private int height;
	private int width;
	private String tabentry;
	private String tabentryHead;
	private String timetableDateHead;
	private String TimeList;
	private String tooLong;
	private String path;

	public Timetable(String path){
		timetable = "";
		height = 0;
		width = 0;
		tabentry = "";
		tabentryHead = "";
		timetableDateHead ="";
		TimeList="";
		tooLong="";
		this.path = path;
	}
	
	public String TimetableInfo(List<StaffMember> rosterList){
		String info="";
		
		return info;
	}
	public String calculateTimetable(List<StaffMember> rosterList, int daysToShow, String action){
		
		boolean ok1 = true;
		boolean ok2 = true;
		String date = null;
		
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
			Date dt=null;
            Date dtCal=null;
				for(int j=0;j<daysToShow;j++){
					if(daysToShow>1){
						if(j==0){
			           		  dt = new Date();
			           		  dtCal = dt;
			           	}else{
			           		  dt=dtCal;
			           		  dtCal = new Date(dt.getTime()+86400000);
			           	}
						
					}else{
						dt = new Date();
						dtCal = dt;
					}
					
		           	 //set date + one day(->timestamp=86400000)
					
		             tabentry+="<div id='mainDayContainer'><div style=' padding:5px; width:100%%; height:25px; ' ><b>" + format.format(dtCal) +  "</b></div><div style='height:400px; padding:5px; ' id='MainDivDay'>";
					for(AbstractMessage message:rosterList)
					{
						
						RosterEntry entry = (RosterEntry)message;
						if(format.format(new Date(entry.getPlannedStartOfWork())).equals(format.format(dtCal))){
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
									"Dienst als:&nbsp;&nbsp;<b>"+ entry.getJob().replaceAll("�","&auml;") + "<br /></b>" +
									"Dienstdatum:&nbsp;&nbsp;" + format.format(new Date(entry.getPlannedStartOfWork())) + "<br />" +
									"Dienstzeit:&nbsp;&nbsp;" +formatHour.format(new Date(entry.getPlannedStartOfWork()))+ " - " + formatHour.format(new Date(entry.getPlannedEndOfWork())) + "<br />" +
									"Ortstelle:&nbsp;&nbsp;" + entry.getStation().replaceAll("�","&ouml;") + "<br />" +
									"angestellt als:&nbsp;&nbsp;"+entry.getServicetype()+"<br />";
							if (action.equals("weekView"))
							{
								tabentry+= 		
									"<div id='singleEntryDiv' style='cursor:pointer; height:" + 
									this.calculateHeightForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork())), formatHour.format(new Date(entry.getPlannedEndOfWork()))) +
									"px; margin-top:" + this.calculateStartForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork()))) +
									"px; float:left;" +
									this.tooLong + 
									"background-color:#CECE52;'><a href='#'><img src='../image/info.jpg' name='info' alt='Info'  class='hidefocus' /><span>" + info + "</span><br /></a>" +
									"<a href='" + path + "/Dispatcher/rosterEntry.do?action=doRemoveEntry&id=" + entry.getRosterId() + "' alt='loeschen' name='loeschen' >" +
									"<img src='../image/loeschen.gif' id='del' /></a></div>";
							}
							else
							{
								tabentry+= 		
									"<div id='singleEntryDiv' style='cursor:pointer; height:" + 
									this.calculateHeightForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork())), formatHour.format(new Date(entry.getPlannedEndOfWork()))) +
									"px; margin-top:" + this.calculateStartForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork()))) +
									"px; float:left;" +
									this.tooLong + 
									"background-color:#CECE52;'><a href='#'><img src='../image/info.jpg' name='info' alt='Info'  class='hidefocus' /><span>" + info + "</span><br /></a>" +
									"<a href='" + path + "/Dispatcher/rosterEntry.do?action=doRemoveDayEntry&id=" + entry.getRosterId() + "' alt='loeschen' name='loeschen' >" +
									"<img src='../image/loeschen.gif' id='del' /></a></div>";	
							}
							
								

							}
						
						
					}
					tabentry+="</div></div>";
					
				}//timetableDateHead+
				
				timetable+=TimeList+tabentryHead+tabentry;
				tabentry="";
			return timetable;
		}
		else{
			return "Keine Dienste vorhanden!";
		}
		
	}

	//caculate the height-value of the div-tag
	private int calculateHeightForEntry(String begin, String end){
		int startPos = Integer.valueOf( begin.substring(0, 2) ).intValue();
		int endPos = Integer.valueOf( end.substring(0, 2) ).intValue();
		int retval = 0;
		int widthFromTop = this.calculateStartForEntry(begin);
		tooLong="";
		
		if(endPos<startPos){
			retval = ((24+endPos)-startPos)*15;
		}else{
			retval = (endPos-startPos)*15;
		}
		
		//check and cut too long values 
		if((widthFromTop+retval)>365){
			tooLong="border-bottom-width:3px; border-bottom-style:dotted; border-bottom-color:black;";
			retval = retval -((widthFromTop+retval)-360);
		}
		
		return retval;
	}

	//caculate the startposition of the div-tag
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
	public static Timetable getInstance(String path) //step 1 
    { 
        //do we have a valid instance? 
        //if(instance == null) 
            instance = new Timetable(path); 
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
