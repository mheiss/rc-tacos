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
	private boolean ok1 = true;
	private boolean ok2 = true;

	public Timetable(){
		timetable = "";
		height = 0;
		width = 0;
		tabentry = "";
	}
	
	public String calculateTimetable(List<StaffMember> rosterList){
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
		String date;
		
		timetable = "<div style='float:left; margin-left:5px; border-width:1px; border-style:solid; border-color:red; width:50px;' id='TimeTab' align='center'>";

		for(int i=0;i<=24;i++){
			timetable+=i+":00\n";
		}
		timetable+="</div>";
		if(rosterList.isEmpty()!=true){
	
				tabentry+="<div onmouseover='showInfo();' style='float:left; margin-left:5px; border-width:1px; border-style:solid; border-color:red; width:13.5%;' id='MainDivDay'>";
				for(AbstractMessage message:rosterList)
				{
					
					RosterEntry entry = (RosterEntry)message;
					if(entry.isSplitEntry())
						date = format.format(new Date(entry.getPlannedEndOfWork()));
					else
						date = formatHour.format(new Date(entry.getPlannedEndOfWork()));	
					
					
					tabentry+="<b>" + entry.getStaffMember().getUserName() + "</b><br>" +
							entry.getStaffMember().getAuthorization()+ "";
					
					
					
				}
				
				tabentry+="</div>";
				timetable+=tabentry;
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
