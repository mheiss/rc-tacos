<%@page import="java.util.*"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body>
   
    <table border="1" cellpadding="5" style="" cellpadding="0" cellspacing="0">
    <%
                /* Die Monatsnamen */
                String monate[]={"Januar", "Februar", "März", "April",
                "Mai", "Juni", "Juli", "August", "September", "Oktober", "November",
                "Dezember"};

                out.print("<form name='form1' action='index.jsp' methode='post'>");
                out.print("<select name='month'>");
               
                for(int i=0; i<12; i++) {
                    out.print("<option value='" + i + "'>" + monate[i] + "</option>");
                }
               
                out.print("</select>");
               
                out.print("<select name='year'>");
                out.print("<option name='2007' selected='selected'>2007</option>");
                out.print("<option name='2008'>2008</option>");
                out.print("<option name='2009'>2009</option>");
                out.print("</select>");
               
                out.print("<input type='submit' name='submit' value='Monat ändern'></form><br />");
               
                /* Die Anzahl der Tage */
                int anzahltage[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
                int mm, yy;
               
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
      if (mm < 0 || mm > 11)
         throw new IllegalArgumentException("Monat " +(mm+1) + " nein, soll 1-12 sein");
         
      out.print("<br /><br />" + monate[mm]);
   
      out.print(" ");
      out.print(yy);
      out.print("<br />");
      GregorianCalendar calendar = new GregorianCalendar(yy, mm, 1);
      out.println("<tr><td>So</td><td> Mo</td><td> Di</td><td> Mi</td><td> Do</td><td> Fr</td><td> Sa</td></tr><tr>");
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
   
      // Die Zahlen für die Monatstage einsetzen
      for(int i=1; i<=tageimmonat; i++) {
                           
                            out.print("<td><a href='bla.jsp?day=" + i + "'>" + i + "</a></td>");
                           
         if ((leeretage + i) % 7 == 0)
            out.println("</tr><tr>");
      }
      out.println("</tr>");
    %>
    </table>
    <br /><br />
    <%
       
       int next=0, last=0;
       if(mm > 0) {
           last = mm-1;
           out.print("<a href='index.jsp?mo=" + last + "&ye=" + yy + "'>Vorheriger Monat</a>  ");
       }
       if(mm < 11) {
            next = mm+1;
            out.print("<a href='index.jsp?mo=" + next + "&ye=" + yy + "'>Nächster Monat</a>");
       }
    %>
   
    </body>
</html> 