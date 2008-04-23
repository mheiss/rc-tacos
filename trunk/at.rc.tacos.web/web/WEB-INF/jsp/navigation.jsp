<%@ include file="includes.jsp" %>
<%@page import="at.rc.tacos.model.Login"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%
	UserSession user = (UserSession)session.getAttribute("userSession"); 
%>
<!--
<table id="Block" width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Navigation</b></td>
	</tr>
	<c:url var="url" value="/Dispatcher/rosterDay2.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Dienstplan Tagesansicht</a></td>
	</tr>
</table>-->
<table id="Block" width="100%" border="0" cellpadding="0" cellspacing="0">
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
		<td id="navIcon"><img src="../image/wappen_bezirk.jpg" name="bezirk" alt="bezirk" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station=1"%>">Bezirk: Bruck - Kapfenberg</a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_breitenau.jpg" name="breitenau" alt="breitenau" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station=6"%>">Breitenau</a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_bruck.jpg" name="bruck" alt="bruck" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station=2"%>">Bruck an der Mur</a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_kapfenberg.jpg" name="kapfenberg" alt="kapfenberg" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station=3"%>">Kapfenberg</a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_stMarein.jpg" name="stMarein" alt="stMarein" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station=7"%>">St. Marein</a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_thoerl.jpg" name="thoerl" alt="thoerl" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station=4"%>">Th&ouml;rl</a>
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_thurnau.jpg" name="thurnau" alt="thurnau" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action=weekView&&station=5"%>">Turnau</a>
		</td>
	</tr>
</table>
<% if(user.getLoginInformation().getAuthorization().equals(Login.AUTH_ADMIN)) 
{
%>
	<table id="Block" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td id="BlockHead" colspan="2"><b>Administration</b></td>
		</tr>
		<tr>
			<td id="navIcon">
				<img src="../image/benutzerNew.jpg" name="benutzer" alt="benutzer" />
			</td>
			<td id="BlockContentNav">
				<a href="<%= request.getContextPath() %>/Dispatcher/addUser.do">Benutzer anlegen</a>
			</td>
		</tr>
		<tr>
			<td id="navIcon"><img src="../image/benutzerEdit.jpg" name="benutzer" alt="benutzer" /></td><td id="BlockContentNav"><a href="<%= request.getContextPath() %>/Dispatcher/listUser.do">Benutzer editieren</a></td>
		<tr>
			<td id="navIcon">
				<img src="../image/benutzerDel.jpg" name="benutzer" alt="benutzer" /></td><td id="BlockContentNav"><a href="<%= request.getContextPath() %>/Dispatcher/lockUser.do">Benutzer sperren</a>
			</td>
		</tr>
	</table>
<%
} 
%>
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
    <tr>
        <td id="BlockHead" colspan="2"><b>Statistik</b></td>
    </tr>
    <tr>
        <td id="navIcon">&nbsp;</td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/statisticEmployee.do?action=doEmployeeStat&notice=Mitarbeiter"%>">Mitarbeiter</a>
        </td>
    </tr>
    <tr>
        <td id="navIcon">&nbsp;</td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/statisticTransport.do?action=doTransportStat&notice=Transporte"%>">Transporte</a>
        </td>
    </tr>
</table>

