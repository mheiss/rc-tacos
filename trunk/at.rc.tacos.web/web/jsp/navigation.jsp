<%@page import="at.rc.tacos.common.Constants"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%@page import="at.rc.tacos.model.DayInfoMessage"%>
<%@page import="java.util.List"%>
<%
List<DayInfoMessage> dayInfoList = userSession.getDayInfoList();
%>
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
    <tr>
        <td id="BlockHeadBlack" colspan="2"><b>Nachricht des Tages</b></td>
    </tr>
    <tr>
        <td style="font-size:10px; background-color: black; color: white; padding:1px;">
1. Die Umfahrung in xxxxxx ist heute wegen Bauarbeiten gesperrt! Es wird der Verkehr &uuml;ber die neue xxxxxx umgeleitet. <br/>
2. Die Umfahrung in xxxxxx ist heute wegen Bauarbeiten gesperrt! Es wird der Verkehr &uuml;ber die neue xxxxxx umgeleitet. 
        <%
        
//           for(DayInfoMessage message : dayInfoList){
//             out.println(message);
               
//           }
        %>
        </td>
    </tr>
</table>
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td id="BlockHead" colspan="2"><b>Navigation</b></td>
	</tr>
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/rosterEntry.do"%>">Dienst eintragen</a>
		</td>
	</tr>
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/rosterDay.do?action=dayView"%>">Tages&uuml;bersicht</a>
		</td>
	</tr>
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/rosterWeek.do?action=weekView&&station=primary"%>">Wochen&uuml;bersicht</a>
		</td>
	</tr>
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/printRoster.do"%>">Dienstplan drucken</a>
		</td>
	</tr>
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/editProfile.do"%>">Profil editieren</a>
		</td>
	</tr>
</table>
<!-- NEXT BLOCK  -->
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td id="BlockHead" colspan="2"><b>Ortsstellen</b></td>
	</tr>
	<tr>
		<td id="navIcon"><img src="../image/wappen_bezirk.jpg" name="bezirk" alt="bezirk" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station="+Constants.STATION_BEZIRK%>">Bezirk <%=Constants.STATION_BEZIRK%></a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_breitenau.jpg" name="breitenau" alt="breitenau" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station="+Constants.STATION_BREITENAU%>"><%=Constants.STATION_BREITENAU%></a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_bruck.jpg" name="bruck" alt="bruck" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station="+Constants.STATION_BRUCK%>"><%=Constants.STATION_BRUCK%></a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_kapfenberg.jpg" name="kapfenberg" alt="kapfenberg" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station="+Constants.STATION_KAPFENBERG%>"><%=Constants.STATION_KAPFENBERG%></a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_stMarein.jpg" name="stMarein" alt="stMarein" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station="+Constants.STATION_MAREIN%>"><%=Constants.STATION_MAREIN%></a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_thoerl.jpg" name="thoerl" alt="thoerl" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station="+Constants.STATION_THOERL%>"><%=Constants.STATION_THOERL.replaceAll("ö", "&ouml;")%></a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_thurnau.jpg" name="thurnau" alt="thurnau" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station="+Constants.STATION_TURNAU%>"><%=Constants.STATION_TURNAU%></a>
		</td>
	</tr>
</table>
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td id="BlockHead" colspan="2"><b>Administration</b></td>
	</tr>
	<tr>
		<td id="navIcon"><img src="../image/benutzerNew.jpg" name="benutzer" alt="benutzer" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/addUser.do"%>">Benutzer anlegen</a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/benutzerEdit.jpg" name="benutzer" alt="benutzer" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/listUser.do"%>">Benutzer editieren</a>
		</td>
		<tr>
		<td id="navIcon"><img src="../image/benutzerDel.jpg" name="benutzer" alt="benutzer" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/lockUser.do"%>">Benutzer sperren</a>
		</td>
	</tr>
</table>
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
    <tr>
        <td id="BlockHead" colspan="2"><b>Statistik</b></td>
    </tr>
    <tr>
        <td id="navIcon">&nbsp;</td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/statisticEmployee.do?notice=Mitarbeiter"%>">Mitarbeiter</a>
        </td>
    </tr>
    <tr>
        <td id="navIcon">&nbsp;</td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/statisticTransport.do?notice=Transporte"%>">Transporte</a>
        </td>
    </tr>
</table>

