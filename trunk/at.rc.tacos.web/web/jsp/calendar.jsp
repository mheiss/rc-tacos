<%@page import="java.util.*"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!--  
<from name="formCal" method="post" action="<%=request.getContextPath()+"/Dispatcher/calendar.do?action="+request.getParameter("month")+"" %>" > 
--> 
<table border="1" cellpadding="5" style="" cellpadding="0"
	cellspacing="0">
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
                                                      if (mm < 0 || mm > 11)
                                                         throw new IllegalArgumentException("Monat " +(mm+1) + " nein, soll 1-12 sein");
                                                         
                                                      //out.print("<br /><br />" + monate[mm]);
                                                   
                                                      //out.print(" ");
                                                      //out.print(yy);
                                                      out.print("<br />");
                                                      GregorianCalendar calendar = new GregorianCalendar(yy, mm, 1);
                                                      out.println("<tr><td><b>So</b></td><td> <b>Mo</b></td><td> <b>Di</b></td><td> <b>Mi</b></td><td> <b>Do</b></td><td> <b>Fr</b></td><td> <b>Sa</b></td></tr><tr>");
                                                      
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
                                                      
                                                      //out.print(currentday);
                                                      
                                                      
                                                      // Die Zahlen für die Monatstage einsetzen
                                                      for(int i=1; i<=tageimmonat; i++) {
                                                                           
                                                            if (i == currentday  && currentmonth == mm && currentyear == yy){
                                                                    
                                                                %>
	<td style='border-color:#444444; border-style:solid; border-width:2px;'>
	<div id="calDay" name="calDay" 
		onclick="setDataToInput('<%=i %>', '<%=mm+1 %>', '<%=yy %>')"><b><%=i %></b></div>
	</td>
	<% 
                                                          }
                                                            else{
                                                                 %>
	<td>
	<div id="calDay" name="calDay" 
		onclick="setDataToInput('<%=i %>', '<%=mm+1 %>', '<%=yy %>')" ><%=i %></div>
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
                                                        
                                                        out.print("<select name='month'>");
                                                    
                                                            for(int i=0; i<12; i++) {
                                                            	if((mm)==i){
                                                            %>
                                                                <option value="<%=i %>" selected><%=monate[i] %></option>
                                                            <%		
                                                            	}else{
                                                            %>
                                                               <option value="<%=i %>" ><%=monate[i] %></option>
                                                            <%
                                                            	}
                                                            }
                                                
                                                            out.print("</select>");
                                                            out.print("<select name='year'>");
                                                        
                                                            int tmpdate;
                                                            
                                                            for(int i = 0; i <= 1;i++){
                                                            	tmpdate = new Date().getYear()+1900;
                                                                tmpdate = tmpdate +i; 
                                                                
                                                            	if(yy==tmpdate){
                                                            		out.print("<option value='"+(tmpdate)+"' selected >"+(tmpdate)+"</option>");      
                                                                }else{
                                                                
                                                                out.print("<option value='"+(tmpdate)+"'>"+(tmpdate)+"</option>");
                                                                }
                                                            }
                                                        
                                                            out.print("</select>");
                                                
                                                           // out.print("<input type='submit' name='submit' value='Monat &auml;ndern'></form><br />"); 
                                                        %>
<!--  
<input type="submit" name="calChange" value="change" />
</from>
 -->