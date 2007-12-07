

 <jsp:include page="header.jsp" flush="true" />
 <jsp:include page="navigation.jsp" flush="true" />


                            
                        <!-- #### CONTENT -->
                                                   
                            <td id="ContentContainer" valign="top">
                            <!-- CONTENT BLOCK  -->
                            
                                <table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
                                    <tr>
                                        <td id="BlockHead" align="right" valign="center">&nbsp;
                                        </td>
                                    </tr>
                                    <tr>
                                        <td id="BlockContent">
                                            <table width="100%" border='0' cellpadding='0' cellspacing='0'>
                                                <tr>
                                                    <td width="50%">
                                                    <!-- Timetablebox Day -->
                                                    <table width="100%" height="100%" border='0' cellpadding='0' cellspacing='0'>
                                                    <tr><td>Tageseintrag</td></tr>
                                                    </table>
                                                    </td>
                                                    <td width="50%">
                                                        <table width="100%" border='0' cellpadding='0' cellspacing='0' id="TabAnmeldung">
                                                            <tr>
                                                                <td id="rosterViewDayHeadline2" colpsan="2" ><b>Dienstdaten:</b> 
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td id="rosterViewDayHeadline">RK-Mitglied:&nbsp;</td>
                                                                <td>
                                                                <!-- Mitarbeiterliste -->
                                                                <select name="employee" id="rosterViewDayHeadSelbox">
					                                                 <%  
                                                                     if(request.getParameter("emplyee")!=null){
                                                                         
                                                                         while(request.getParameter("emplyee")!=null){
                                                                             %>
                                                                                <option value="id"><%=request.getParameter("emplyee") %></option>
                                                                             <% 
                                                                             
                                                                         }
                                                                     }
                                                                     %>
					                                             
					                                            </select>
					                                            </td>
                                                            </tr>
                                                            <tr>
                                                                <td id="rosterViewDayHeadline">Ortsstelle:&nbsp;</td>
                                                                <td>
                                                                
                                                                <!--  Orstellenliste -->
					                                            <select name="ortsstelle" id="rosterViewDayHeadSelbox">
					                                                 <%  
					                                                 if(request.getParameter("place")!=null){
					                                                	 int i;
					                                                	 for(i=0;i<=10;i++){
					                                                		 %>
                                                                             <option value="id"><%=request.getParameter("place") %></option>
                                                                             <% 
					                                                		 
					                                                	 }
					                                                 }
					                                                 %>
					                                            </select>
                                                                </td>
                                                           
                                                            </tr>
                                                            <tr>
                                                                <td id="rosterViewDayHeadline">RK-Dienst:&nbsp;</td>
                                                                <td>
                                                                <select name="duty" id="rosterViewDayHeadSelbox">
                                                                <%  
                                                                     if(request.getParameter("roster")!=null){
                                                                         int i;
                                                                         for(i=0;i<=10;i++){
                                                                        	 %>
                                                                        	 <option value="id"><%=request.getParameter("roster") %></option>
                                                                             <%
                                                                         }
                                                                     }
                                                                 %>
                                                                 </select> 
                                                                 <!-- Tätigkeiten 
                                                                
                                                                     <option value="id">RTW Fahrer</option>
                                                                     <option value="id">NEF Fahrer</option>
                                                                    <option value="id">Santit&auml;ter 1</option>
                                                                    <option value="id">Santit&auml;ter 2</option>
                                                                    <option value="id">Zivildiener</option>
                                                                    <option value="id">Sonstige</option>
                                                                 
                                                                
                                                                --> 
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td id="rosterViewDayHeadline2" colpsan="2"><b>Dienstzeit:</b> 
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td id="rosterViewDayHeadline"><input type="checkbox" id="timeTo" /></td>
                                                                <td id="rosterViewDayName">&nbsp;Fr&uuml;h (6-11)</td>
                                                              
                                                               
                                                            </tr>
                                                            <tr>
                                                                <td id="rosterViewDayHeadline"><input type="checkbox" id="timeTo" /></td>
                                                                <td id="rosterViewDayName">&nbsp;Tag (7-18)</td>
                                                            </tr>
                                                            <tr>
                                                                <td id="rosterViewDayHeadline"><input type="checkbox" id="timeTo" /></td>
                                                                <td id="rosterViewDayName">&nbsp;Nacht(18-7)</td>
                                                            </tr>
                                                            <tr>
                                                                <td id="rosterViewDayHeadline"><input type="checkbox" id="timeTo" /></td>
                                                                <td id="rosterViewDayName">&nbsp;Ambulanz (6-14)</td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="2" align="right" style="padding:10px;"><input type="image" src="image/button_ok.jpg" id="senden" value="anmelden" id="buttonOk" /></td>

                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                     </tr>
                                </table>
                            </td>

  
 <jsp:include page="footer.jsp" flush="true" />