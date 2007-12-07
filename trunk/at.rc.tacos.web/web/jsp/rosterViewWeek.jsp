
  
 <jsp:include page="header.jsp" flush="true" />
 <jsp:include page="navigation.jsp" flush="true" />
                            
                        <!-- #### CONTENT -->
                            <td id="ContentContainer" valign="top">
                            <!-- CONTENT BLOCK  -->
                                <table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
                                    <tr>
                                        <td id="BlockHead" align="right" valign="center"><b>Ortsstelle:</b> 
                                            <select name="ortsstelle">
                                                 <option value="1">Bruck-Kapfenberg</option>
                                                 <option value="2">Bruck an der Mur</option>
                                                 <option value="3">Kapfenberg</option>
                                                 <option value="4">St. Marein</option>
                                                 <option value="5">Th&ouml;rl</option>
                                                 <option value="6">Turnau</option>
                                                 <option value="7">Breitenau</option>
                                                 <option value="8">NEF</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td id="BlockContent">
                                            
                                            <table width="100%" border='0' cellpadding='0' cellspacing='0'>
                                                <tr>
                                                    <td id="weekdayZ">Zeit</td>
                                                    <td id="weekday"><a href="<%=request.getContextPath()%>/Timetable?action=getTimetableDay">Montag</a><br>01.12.07</td>
                                                    <td id="weekday">Dienstag<br>01.12.07</td>
                                                    <td id="weekday">Mittwoch<br>02.12.07</td>
                                                    <td id="weekday">Donnerstag<br>03.12.07</td>
                                                    <td id="weekday">Freitag<br>04.12.07</td>
                                                    <td id="weekday">Samstag<br>05.12.07</td>
                                                    <td id="weekday">Sonntag<br>06.12.07</td>
                                                </tr>
                                            </table>
                                            <table width="100%" border='0' cellpadding='0' cellspacing='0'>
                                                <tr>
                                                    <td id="weekdayTimetableZ">
                                                        <table  width="100%" border='0' cellpadding='0' cellspacing='0'>
                                                            <tr><td id="TEST00">07:00</td></tr>
                                                            <tr><td id="TEST00">08:00</td></tr>
                                                            <tr><td id="TEST00">09:00</td></tr>
                                                            <tr><td id="TEST00">10:00</td></tr>
                                                            <tr><td id="TEST00">11:00</td></tr>
                                                            <tr><td id="TEST00">12:00</td></tr>
                                                            <tr><td id="TEST00">13:00</td></tr>
                                                            <tr><td id="TEST00">14:00</td></tr>
                                                            <tr><td id="TEST00">15:00</td></tr>
                                                            <tr><td id="TEST00">16:00</td></tr>
                                                            <tr><td id="TEST00">17:00</td></tr>
                                                            <tr><td id="TEST00">18:00</td></tr>
                                                            <tr><td id="TEST00">19:00</td></tr>
                                                            <tr><td id="TEST00">20:00</td></tr>
                                                            <tr><td id="TEST00">21:00</td></tr>
                                                            <tr><td id="TEST00">22:00</td></tr>
                                                            <tr><td id="TEST00">23:00</td></tr>
                                                            <tr><td id="TEST00">24:00</td></tr>
                                                            <tr><td id="TEST00">01:00</td></tr>
                                                            <tr><td id="TEST00">02:00</td></tr>
                                                            <tr><td id="TEST00">03:00</td></tr>
                                                            <tr><td id="TEST00">04:00</td></tr>
                                                            <tr><td id="TEST00">05:00</td></tr>
                                                            <tr><td id="TEST00">06:00</td></tr>
                                                            <tr><td id="TEST00">07:00</td></tr>
                                                        </table>
                                                    </td>
                                                    <td id="weekdayTimetable">
                                                    <a href="rosterViewDay.jsp">
                                                    
                                                        <table  width="100%" border='0' cellpadding='0' cellspacing='0' style="margin:2px;">
                                                            
                                                            <tr><td id="TEST01"><b>Franz Huber</b></td></tr>
                                                            <tr><td id="TEST01">(Fahrer)</td></tr>
                                                            <tr><td id="TEST01">Hauptamlicher</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"><b>Nina Wiener</b></td></tr>
                                                            <tr><td id="TEST01">(Sanitäter)</td></tr>
                                                            <tr><td id="TEST01">Freiwillge</td></tr>    
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST02"><b>Michael Neuberger</b></td></tr>
                                                            <tr><td id="TEST02">(Fahrer)</td></tr>
                                                            <tr><td id="TEST02">Freiwilliger</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02"><b>Stefan Eder</b></td></tr>
                                                            <tr><td id="TEST02">(Sanitäter)</td></tr>
                                                            <tr><td id="TEST02">Freiwillge</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                        </table>
                                                        
                                                    </a>
                                                    </td>
                                                    <td id="weekdayTimetable">
                                                        <table  width="100%" border='0' cellpadding='0' cellspacing='0'>
                                                            <tr><td id="TEST01"><b>Franz Huber</b></td></tr>
                                                            <tr><td id="TEST01">(Fahrer)</td></tr>
                                                            <tr><td id="TEST01">Hauptamlicher</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"><b>Nina Wiener</b></td></tr>
                                                            <tr><td id="TEST01">(Sanitäter)</td></tr>
                                                            <tr><td id="TEST01">Freiwillge</td></tr>    
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            
                                                            
                                                        </table>
                                                    </td>
                                                    <td id="weekdayTimetable">
                                                        <table  width="100%" border='0' cellpadding='0' cellspacing='0'>
                                                            
                                                            <tr><td id="TEST01"><b>Franz Huber</b></td></tr>
                                                            <tr><td id="TEST01">(Fahrer)</td></tr>
                                                            <tr><td id="TEST01">Hauptamlicher</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"><b>Nina Wiener</b></td></tr>
                                                            <tr><td id="TEST01">(Sanitäter)</td></tr>
                                                            <tr><td id="TEST01">Freiwillge</td></tr>    
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST02"><b>Susanne Winter</b></td></tr>
                                                            <tr><td id="TEST02">(Fahrer)</td></tr>
                                                            <tr><td id="TEST02">Freiwilliger</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02"><b>Stefan Eder</b></td></tr>
                                                            <tr><td id="TEST02">(Sanitäter)</td></tr>
                                                            <tr><td id="TEST02">Freiwillge</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                        </table>                                                    
                                                    </td>
                                                    <td id="weekdayTimetable">
                                                        <table  width="100%" border='0' cellpadding='0' cellspacing='0'>
                                                            
                                                            <tr><td id="TEST01"><b>Franz Huber</b></td></tr>
                                                            <tr><td id="TEST01">(Fahrer)</td></tr>
                                                            <tr><td id="TEST01">Hauptamlicher</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"><b>Nina Wiener</b></td></tr>
                                                            <tr><td id="TEST01">(Sanitäter)</td></tr>
                                                            <tr><td id="TEST01">Freiwillge</td></tr>    
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                        </table>                                                    
                                                    </td>
                                                    <td id="weekdayTimetable">
                                                        <table  width="100%" border='0' cellpadding='0' cellspacing='0'>
                                                            
                                                            <tr><td id="TEST01"><b>Franz Huber</b></td></tr>
                                                            <tr><td id="TEST01">(Fahrer)</td></tr>
                                                            <tr><td id="TEST01">Hauptamlicher</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"><b>Nina Wiener</b></td></tr>
                                                            <tr><td id="TEST01">(Sanitäter)</td></tr>
                                                            <tr><td id="TEST01">Freiwillge</td></tr>    
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST02"><b>Michael Neuberger</b></td></tr>
                                                            <tr><td id="TEST02">(Fahrer)</td></tr>
                                                            <tr><td id="TEST02">Freiwilliger</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02"><b>Stefan Eder</b></td></tr>
                                                            <tr><td id="TEST02">(Sanitäter)</td></tr>
                                                            <tr><td id="TEST02">Freiwillge</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                            <tr><td id="TEST02">&nbsp;</td></tr>
                                                        </table>                                                    
                                                    </td>
                                                    <td id="weekdayTimetable">
                                                        <table  width="100%" border='0' cellpadding='0' cellspacing='0'>
                                                            <tr><td id="TEST01"><b>Franz Huber</b></td></tr>
                                                            <tr><td id="TEST01">(Fahrer)</td></tr>
                                                            <tr><td id="TEST01">Hauptamlicher</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"><b>Nina Wiener</b></td></tr>
                                                            <tr><td id="TEST01">(Sanitäter)</td></tr>
                                                            <tr><td id="TEST01">Freiwillge</td></tr>    
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                        </table>                                                    
                                                    </td>
                                                    <td id="weekdayTimetable">
                                                        <table  width="100%" border='0' cellpadding='0' cellspacing='0'>
                                                            <tr><td id="TEST01"><b>Josef Huber</b></td></tr>
                                                            <tr><td id="TEST01">(Fahrer)</td></tr>
                                                            <tr><td id="TEST01">Freiwillger</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"><b>Nina Wiener</b></td></tr>
                                                            <tr><td id="TEST01">(Sanitäter)</td></tr>
                                                            <tr><td id="TEST01">Freiwillge</td></tr>    
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01">&nbsp;</td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST01"></td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                            <tr><td id="TEST03">&nbsp;</td></tr>
                                                        </table>                                                    
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>

 <jsp:include page="footer.jsp" flush="true" />