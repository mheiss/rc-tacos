/**
 * 2007 - 2008
 * @author hade - Hannes Derler
 */

package at.rc.tacos.web.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.RosterEntry;

public class TimetableVertical {
	private String timetable;
	private String startDate;
	private int height;
	private int width;
	private String tabentry;
	private String tabentryHead;
	private String timetableDateHead;
	private String TimeList;
	private String tooLong;
	private String path;

	public static final String DRIVER_COLOR = "#0F1BFF";
	public static final String PARAMEDIC_COLOR = "#6863FF";
	public static final String EPARAMEDIC_COLOR = "#FFBD72";
	public static final String DOC_COLOR = "#FF871E";
	public static final String OTHERS_COLOR = "#3CC140";


	public TimetableVertical(String path,String startDate){
		timetable = "";
		this.startDate = startDate;
		height = 0;
		width = 0;
		tabentry = "";
		tabentryHead = "";
		timetableDateHead ="";
		TimeList="";
		tooLong="";
		this.path = path;
	}

	public String TimetableInfo(List<RosterEntry> rosterList){
		String info="";

		return info;
	}

	public String calculateTimetable(List<RosterEntry> rosterList, int daysToShow){

		boolean ok1 = true;
		boolean ok2 = true;

		SimpleDateFormat format = new SimpleDateFormat("E, dd-MM-yyyy");
		SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
		int zaehle = 0;
		String info="";
//<div style='padding:5px; width:30px; height:25px; vertical-align:middle; text-align:left; float:left;' ><img src='../image/clock_klein.jpg' /></div>
		TimeList += "<div id='mainDayContainerTL' align='center'>";
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
				TimeList+="<div id='timeList'>0"+i+"</div>";
			}else{
				TimeList+="<div  id='timeList'>"+i+"</div>";
			}
			i++;

		}while(ok1);

		TimeList += "</div>"; 

		if(!rosterList.isEmpty())
		{
			Date dt=null;
			Date dtCal=null;
			for(int j=0;j<daysToShow;j++)
			{
				if(j==0)
				{
					//convert the start date to a date
					DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					try
					{
						dt = df.parse(startDate);
					}
					catch(ParseException pe)
					{
						System.out.println("Invalid start date, using current date");
						dt = new Date();
					}
					dtCal = dt;
				}
				else
				{
					dt = dtCal;
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(dt.getTime());
					cal.add(Calendar.DAY_OF_MONTH, 1);
					dtCal = cal.getTime();
				}

				tabentry+="<div id='mainDayContainer'><div style='padding:1px; width:100%; height:25px;' ><b>" + format.format(dtCal) +  "</b></div><div style='padding:5px; ' id='MainDivDay'>";
				for(AbstractMessage message:rosterList)
				{
					RosterEntry entry = (RosterEntry)message;
					if(format.format(new Date(entry.getPlannedStartOfWork())).equals(format.format(dtCal))){

						zaehle++;
						info = "INFORMATION<br /><br />Name:&nbsp;&nbsp;<b>"+ entry.getStaffMember().getUserName()+"</b><br />" +
						"Dienst als:&nbsp;&nbsp;<b>"+ entry.getJob().getJobName().replaceAll("ä", "&auml;") + "<br /></b>" +
						"Dienstdatum(Start):&nbsp;&nbsp;" + format.format(new Date(entry.getPlannedStartOfWork())) + "<br />" +
						"Dienstdatum(Ende):&nbsp;&nbsp;" + format.format(new Date(entry.getPlannedEndOfWork())) + "<br />" +
						"Dienstzeit:&nbsp;&nbsp;" +formatHour.format(new Date(entry.getPlannedStartOfWork()))+ " - " + formatHour.format(new Date(entry.getPlannedEndOfWork())) + "<br />" +
						"Ortstelle:&nbsp;&nbsp;" + entry.getStation().getLocationName().replaceAll("ö","&ouml;") + "<br />" +
						"angestellt als:&nbsp;&nbsp;"+entry.getServicetype().getServiceName()+"<br />" + 
						"eingetragen von:&nbsp;&nbsp;"+entry.getCreatedByUsername() +"<br />";

						if(entry.getCreatedByUsername().equals(entry.getStaffMember().getUserName()))
						{
							tabentry+= 		
								"<div id='singleEntryDiv' style='margin-left:" + this.calculateStartForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork()))) +
								"%; " +
								this.tooLong + 
								" width:" +this.calculateWidthForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork())), formatHour.format(new Date(entry.getPlannedEndOfWork()))) +
								"%; " +
								"background-color:" + this.getBgColor(entry.getJob().getJobName()) + ";'><a href='#' style='float:left;'><img src='../image/info.png' name='info' alt='Info'  class='hidefocus' /><span id='infoBox' >" + info + "</span><br /></a>" +
								"<a href='"+ path +"/Dispatcher/updateEntry.do?action=doUpdateEntry&id=" + entry.getRosterId() +"'  style='float:left;'>" +
								"<img src='../image/b_edit.png' id='edit' class='hidefocus' /></a>" +
								"<a href='"+ path +"/Dispatcher/rosterEntry.do?action=doRemoveEntry&id=" + entry.getRosterId() +"' onClick=\"return confirm('M&ouml;chten Sie diesen Dienst wirklich l&ouml;schen?')\"  style='float:left;'>" +							
								"<img src='../image/b_drop.png' id='del' class='hidefocus' /></a></div>";							
						}
						else
						{
							tabentry+= 		
								"<div id='singleEntryDiv' style='margin-left:" + this.calculateStartForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork()))) +
								"%; " +
								this.tooLong + 
								" width:" +this.calculateWidthForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork())), formatHour.format(new Date(entry.getPlannedEndOfWork()))) +
								"%; " +
								"background-color:" + this.getBgColor(entry.getJob().getJobName()) + ";'><a href='#'><img src='../image/info.png' name='info' alt='Info'  class='hidefocus' /><span id='infoBox' >" + info + "</span><br /></a></div>";
						}

					}
				}
				tabentry+="</div></div>";
			}
			//timetableDateHead+
			timetable+=TimeList+tabentryHead+tabentry;
			tabentry="";
			return timetable;
		}
		else
		{
			return "Keine Dienste vorhanden!";
		}

	}

	//caculate the width-value of the div-tag
	private int calculateWidthForEntry(String begin, String end)
	{
		int startPos = Integer.valueOf( begin.substring(0, 2) ).intValue();
		int endPos = Integer.valueOf( end.substring(0, 2) ).intValue();
		int retval = 0;
		int val1 = 0, val2 = 0;
		//int widthFromleft = this.calculateStartForEntry(begin);
		tooLong="";

		if(endPos>startPos){
			retval = ((endPos-startPos)*100) / 25;
		}else if(endPos == startPos){
			retval = 100;
		}else{
			retval = ((startPos-endPos)*100) / 25;
		}

		//check and cut too long values 
		if(retval>98)
		{
			tooLong="border-right-width:3px; border-right-style:dotted; border-right-color:black;";
			retval = 98;
		}

		
		return retval;
	}

	//caculate the startposition of the div-tag
	private int calculateStartForEntry(String begin)
	{
		int startPos = (Integer.valueOf( begin.substring(0, 2) ).intValue());
		int min = (Integer.valueOf( begin.substring(3, 5) ).intValue());
		int retval = 0;
		if(min>0){
			min = 2;
		}
		retval = ((100 * (startPos-5)) / 24) + min;
		if(retval < 0){
			retval = 0;
		}

		return retval;
	}

	/**
	 * GETTER and SETTERS
	 * 
	 */
	public String getTimetable() 
	{
		return timetable;
	}

	public int getHeight() 
	{
		return height;
	}

	public void setHeight(int height) 
	{
		this.height = height;
	}

	public int getWidth() 
	{
		return width;
	}

	public void setWidth(int width) 
	{
		this.width = width;
	}

	public String getBgColor(String TODO) {
		if(TODO.equalsIgnoreCase("Notarzt")){
			return DOC_COLOR;
		} else if (TODO.equalsIgnoreCase("Notfallsanitäter")) {
			return EPARAMEDIC_COLOR;
		} else if (TODO.equalsIgnoreCase("Sanitäter")) {
			return PARAMEDIC_COLOR;
		} else if (TODO.equalsIgnoreCase("Fahrer")) {
			return DRIVER_COLOR;
		} else{
			return OTHERS_COLOR;
		}

	}   
}
