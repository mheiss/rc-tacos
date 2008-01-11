<%@page import="java.util.*"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<table border="0" cellpadding="5" style="" cellpadding="0"
	cellspacing="0" id="calTabMain">

	<%
                                                               
                                               
	/* Die Monatsnamen */
	String monate[]={"Januar", "Februar", "M&auml;rz", "April",
	"Mai", "Juni", "Juli", "August", "September", "Oktober", "November",
	"Dezember"};
	
    /* Die Anzahl der Tage */
   int anzahltage[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
   int mm, yy;
                                                               
   //out.print("MONTH:"+request.getParameter("startMonth"));
   if(request.getParameter("month") == null) {
         Calendar c = Calendar.getInstance();
                                
         mm = c.get(Calendar.MONTH);
         yy = c.get(Calendar.YEAR);
   }
   else {
       mm = Integer.parseInt(request.getParameter("month").toString());
       yy = Integer.parseInt(request.getParameter("year").toString());
                                                                    
  }
                                                               
  if(request.getParameter("mo") != null && request.getParameter("ye") != null) {
     mm = Integer.parseInt(request.getParameter("mo").toString());
     yy = Integer.parseInt(request.getParameter("ye").toString());
  }
                                                       
                                                               
  /* Die Anzahl der leerbleibenden Tage */
                                                           
     int leeretage = 0;
     if (mm < 0 || mm > 11) throw new IllegalArgumentException("Monat " +(mm+1) + " nein, soll 1-12 sein");
                                                         
  out.print("<tr><td id='calMonthYearInfo'>O</td><td colspan='5' align='center' id='calMonthYearInfo'>" + monate[mm] + " " + yy + "</td><td id='calMonthYearInfo'>O</td></tr>");
                                                   
  //out.print(" ");
 //out.print(yy);
  out.print("<br />");
  GregorianCalendar calendar = new GregorianCalendar(yy, mm, 1);
  out.println("<tr><td id='calTabWeekDays'><b>So</b></td>"+
              "<td id='calTabWeekDays'><b>Mo</b></td>"+
              "<td id='calTabWeekDays'><b>Di</b></td>"+
              "<td id='calTabWeekDays'><b>Mi</b></td>"+
              "<td id='calTabWeekDays'><b>Do</b></td>"+
              "<td id='calTabWeekDays'><b>Fr</b></td>"+
              "<td id='calTabWeekDays'><b>Sa</b></td></tr><tr>");
                                                      
  // Berechne wieviel vor dem Ersten leer bleibt
  // get() gibt korrekt 0 für Sonntag zurück
  leeretage = calendar.get(Calendar.DAY_OF_WEEK)-1;
                                                   
  int tageimmonat = anzahltage[mm];
  if (calendar.isLeapYear(calendar.get(Calendar.YEAR)) && mm == 1)
      ++tageimmonat;
                                                         
  // die Labels vor dem Monatsersten leer lassen
  for (int i=0; i< leeretage; i++) {
     out.print("<td> </td>");
  }
                                                            
  Calendar c = Calendar.getInstance();
  int currentday = c.get(Calendar.DAY_OF_MONTH);
  int currentmonth = new Date().getMonth();
  int currentyear = new Date().getYear()+1900;
                                                      
                                                      
                                                      
 // Die Zahlen für die Monatstage einsetzen
 for(int i=1; i<=tageimmonat; i++) {
                                                                           
 if (i == currentday  && currentmonth == mm && currentyear == yy){
 
 out.print(c.get(Calendar.DAY_OF_WEEK));
 %>
	<td
		style='border-color: #444444; border-style: solid; border-width: 2px;'>
	<div id="calDay" name="calDay"
		onclick="setDataToInput('<%=i %>', '<%=mm+1 %>', '<%=yy %>')"><b><%=i %></b></div>
	</td>
	<% 
 }
 else{
 %>
	<td>
	<div id="calDay" name="calDay"
		onclick="setDataToInput('<%=i %>', '<%=mm+1 %>', '<%=yy %>')"><%=i %></div>
	</td>
<%                                                
 }
    if ((leeretage + i) % 7 == 0)
        out.println("</tr><tr>");
    }
    out.println("</tr>");
%>
</table>

<%                  
                                                        

                                                
if(request.getParameter("month")==null) {
	%>
	<input type="hidden" name="month" id="month" value="<%=currentmonth %>"/> 
	<% 
}else{
	   %>
	    <input type="hidden" name="month" id="month" value="<%=request.getParameter("month") %>"/> 
	    <%
}

if(request.getParameter("year")==null) {
	%>
	<input type="hidden" name="year" id="month" value="<%=currentyear %>"/> 
	<% 
}else{
    %>
    <input type="hidden" name="month" id="month" value="<%=request.getParameter("year") %>"/> 
    <%
}
%>
 
 
