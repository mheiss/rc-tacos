package at.rc.tacos.web.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.RosterEntry;

public class TimetableVertical 
{
	private String timetable;
	private String startDate;
	private int height;
	private int width;
	private String tabentry;
	private String tabentryHead;
	private String TimeList;
	private String currentUser;
	private String path;

	public static final String DRIVER_COLOR = "#0F1BFF";
	public static final String PARAMEDIC_COLOR = "#6863FF";
	public static final String EPARAMEDIC_COLOR = "#FFBD72";
	public static final String DOC_COLOR = "#FF871E";
	public static final String OTHERS_COLOR = "#3CC140";

	public TimetableVertical(String path,String startDate, String currentUser)
	{
		timetable = "";
		this.startDate = startDate;
		height = 0;
		width = 0;
		tabentry = "";
		tabentryHead = "";
		TimeList="";
		this.path = path;
		this.currentUser = currentUser;
	}

	public String calculateTimetable(List<RosterEntry> rosterList, int daysToShow)
	{

		boolean ok1 = true;
		boolean ok2 = true;

		SimpleDateFormat format = new SimpleDateFormat("E, dd-MM-yyyy");
		SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
		int zaehle = 0;
		String info="";

		TimeList += "<div id='mainDayContainerTL' align='center'>";
		int i = 5;
		do 
		{
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

		}
		while(ok1);

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

				tabentry+=TimeList;

				tabentry+="<div id='mainDayContainer'><div style='padding:1px; width:100%; height:25px;' ><b>" + format.format(dtCal) +  "</b></div><div style='padding:5px; ' id='MainDivDay'>";
				for(AbstractMessage message:rosterList)
				{
					RosterEntry entry = (RosterEntry)message;
					if(format.format(new Date(entry.getPlannedStartOfWork())).equals(format.format(dtCal)))
					{
						if(entry.getCreatedByUsername().equals(entry.getStaffMember().getUserName()))
						{
							zaehle++;
							info = "INFORMATION<br /><br />Name:&nbsp;&nbsp;<b>"+ entry.getStaffMember().getUserName()+"</b><br />" +
							"Dienst als:&nbsp;&nbsp;<b>"+ entry.getJob().getJobName().replaceAll("ä", "&auml;") + "<br /></b>" +
							"Dienstdatum(Start):&nbsp;&nbsp;" + format.format(new Date(entry.getPlannedStartOfWork())) + "<br />" +
							"Dienstdatum(Ende):&nbsp;&nbsp;" + format.format(new Date(entry.getPlannedEndOfWork())) + "<br />" +
							"Dienstzeit:&nbsp;&nbsp;" +formatHour.format(new Date(entry.getPlannedStartOfWork()))+ " - " + formatHour.format(new Date(entry.getPlannedEndOfWork())) + "<br />" +
							"Ortstelle:&nbsp;&nbsp;" + entry.getStation().getLocationName().replaceAll("ö","&ouml;") + "<br />" +
							"angestellt als:&nbsp;&nbsp;"+entry.getServicetype().getServiceName()+"<br />" +
							"eingetragen von:&nbsp;&nbsp;" +entry.getCreatedByUsername() +"<br />";
						}
						else 
						{
							zaehle++;
							info = "INFORMATION<br /><br />Name:&nbsp;&nbsp;<b>"+ entry.getStaffMember().getUserName()+"</b><br />" +
							"Dienst als:&nbsp;&nbsp;<b>"+ entry.getJob().getJobName().replaceAll("ä", "&auml;") + "<br /></b>" +
							"Dienstdatum(Start):&nbsp;&nbsp;" + format.format(new Date(entry.getPlannedStartOfWork())) + "<br />" +
							"Dienstdatum(Ende):&nbsp;&nbsp;" + format.format(new Date(entry.getPlannedEndOfWork())) + "<br />" +
							"Dienstzeit:&nbsp;&nbsp;" +formatHour.format(new Date(entry.getPlannedStartOfWork()))+ " - " + formatHour.format(new Date(entry.getPlannedEndOfWork())) + "<br />" +
							"Ortstelle:&nbsp;&nbsp;" + entry.getStation().getLocationName().replaceAll("ö","&ouml;") + "<br />" +
							"angestellt als:&nbsp;&nbsp;"+entry.getServicetype().getServiceName()+"<br />" +
							"eingetragen von:&nbsp;&nbsp;<b style=\"color:#FF0000\">" +entry.getCreatedByUsername() +"</b><br />";
						}
						tabentry+= 		
							"<div id='singleEntryDivCase' style='width:100%; padding-left:" + this.calculateStartForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork()))) +
							"%; ' >";
						if(entry.getCreatedByUsername().equals(entry.getStaffMember().getUserName()) || currentUser.equals(entry.getStaffMember().getUserName()) || currentUser.equals(entry.getCreatedByUsername()))
						{
							tabentry+= 		
								"<div id='singleEntryDiv' style='" + 
								" width:" +this.calculateWidthForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork())), formatHour.format(new Date(entry.getPlannedEndOfWork()))) +
								"%; " +
								"background-color:" + this.getBgColor(entry.getJob().getJobName()) + ";'><a href='#' style='float:left;'><img src='../image/info.png' name='info' alt='Info'  class='hidefocus' /><span id='infoBox' >" + info + "</span><br /></a>" +
								"<a href='"+ path +"/Dispatcher/updateEntry.do?action=doUpdateEntry&id=" + entry.getRosterId() +"'  style='float:left;'>" +
								"<img src='../image/b_edit.png' id='edit' class='hidefocus' /></a>" +
								"<a href='"+ path +"/Dispatcher/rosterEntry.do?action=doRemoveEntry&id=" + entry.getRosterId() +"' onClick=\"return confirm('M&ouml;chten Sie diesen Dienst wirklich l&ouml;schen?')\"  style='float:left;'>" +							
								"<img src='../image/b_drop.png' id='del' class='hidefocus' /></a>" +
								"&nbsp&nbsp;" + this.getTooLong(formatHour.format(new Date(entry.getPlannedStartOfWork())), formatHour.format(new Date(entry.getPlannedEndOfWork()))) + 
								"</div>";							
						}
						else
						{
							tabentry+= 		
								"<div id='singleEntryDiv' style='" +
								" width:" +this.calculateWidthForEntry(formatHour.format(new Date(entry.getPlannedStartOfWork())), formatHour.format(new Date(entry.getPlannedEndOfWork()))) +
								"%; " +
								"background-color:" + this.getBgColor(entry.getJob().getJobName()) + ";'><a href='#' style='float:left;'><img src='../image/info.png' name='info' alt='Info'  class='hidefocus' /><span id='infoBox' >" + info + "</span><br /></a>" +
								"&nbsp&nbsp;" + this.getTooLong(formatHour.format(new Date(entry.getPlannedStartOfWork())), formatHour.format(new Date(entry.getPlannedEndOfWork()))) +
								"</div>";
						}
						tabentry+= "</div>";
					}
				}
				tabentry+="</div></div>";
			}
			timetable+=tabentryHead+tabentry;
			tabentry="";
			return timetable;
		}
		else
		{
			return "Keine Dienste vorhanden!";
		}

	}

	//caculate the width-value of the div-tag
	private double calculateWidthForEntry(String begin, String end)
	{
		double startPos = Integer.valueOf( begin.substring(0, 2) ).intValue();
		double minStart = (Integer.valueOf( begin.substring(3, 5) ).intValue());
		double endPos = Integer.valueOf( end.substring(0, 2) ).intValue();
		double minEnd = (Integer.valueOf( end.substring(3, 5) ).intValue());
		double diff = 0;
		double retval = 0.0;

		if(minStart>0){
			minStart = 2;
		}
		if(minEnd>0){
			minEnd = 2;
		}		

		if(startPos >= 0 && startPos < 5){
			startPos = 4.5 ;
		}

		if(endPos>startPos){
			retval = ((endPos - startPos) * 100) / 25;
		}else{
			retval = (((endPos + 24) - startPos) * 100) / 25;
		}

		if(minStart != 0 && minEnd != 0){
			//nothing should happen
		}else{
			if(minStart != 0){
				retval -= minStart;
			}else if (minEnd != 0){
				retval += minEnd;
			}
		}
		return retval + 0.4;
	}

	//caculate the startposition of the div-tag
	private double calculateStartForEntry(String begin)
	{
		int startPos = (Integer.valueOf( begin.substring(0, 2) ).intValue());
		int min = (Integer.valueOf( begin.substring(3, 5) ).intValue());
		double retval = 0.0;
		if(min>0){
			min = 2;
		}
		if(startPos >= 11){
			if(startPos >= 17){
				retval = (((100 * (startPos-5)) / 24) + min) - 0.2;
			}else{
				retval = (((100 * (startPos-5)) / 24) + min) + 0.5;
			}
		}else{
			retval = (((100 * (startPos-5)) / 24) + min) + 1.5;
		}
		if(retval < 0){
			retval =0;
		}
		return retval;
	}

	private String getTooLong(String begin, String end)
	{
		String tooLong;
		double widthFromleft = this.calculateStartForEntry(begin);
		double widthFromLine = this.calculateWidthForEntry(begin, end);
		String retval = null;

		//check and cut too long values 
		if((widthFromleft+widthFromLine)>100)
		{
			retval = "<img src='../image/rosterArrowRight.jpg' alt='Nachtdienst' />";
		}else if(widthFromleft == 0){
			retval = "<img src='../image/rosterArrowLeft.jpg' alt='Nachtdienst' />";
		}else{
			retval = "";
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

	public String getBgColor(String TODO) 
	{
		if(TODO.equalsIgnoreCase("Notarzt"))
		{
			return DOC_COLOR;
		} else if (TODO.equalsIgnoreCase("Notfallsanitäter"))
		{
			return EPARAMEDIC_COLOR;
		} else if (TODO.equalsIgnoreCase("Sanitäter")) 
		{
			return PARAMEDIC_COLOR;
		} else if (TODO.equalsIgnoreCase("Fahrer")) 
		{
			return DRIVER_COLOR;
		} else{
			return OTHERS_COLOR;
		}

	}   
}
