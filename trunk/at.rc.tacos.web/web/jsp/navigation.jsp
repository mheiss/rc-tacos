<%@page import="at.rc.tacos.common.Constants"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td id="BlockHead" colspan="2"><b>Navigation</b></td>
	</tr>
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/rosterEntry.do"%>">Dienst eintragen</a><br />
		</td>
	</tr>
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/rosterDay.do?action=dayView"%>">Tages&uuml;bersicht</a><br />
		</td>
	</tr>
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/rosterWeek.do?action=weekView"%>">Wochen&uuml;bersicht</a><br />
		</td>
	</tr>
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/printRoster.do"%>">Dienstplan drucken</a><br />
		</td>
	</tr>
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/editProfile.do"%>">Profil editieren</a><br />
		</td>
	</tr>
</table>
<!-- NEXT BLOCK  -->
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td id="BlockHead" colspan="2"><b>Ortsstellen</b></td>
	</tr>
	<tr>
		<td id="navIcon"><img src="../image/wappen_bezirk.jpg" name="bezirk" alt="bezirk" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_BEZIRK%>">Bezirk <%=Constants.STATION_BEZIRK%></a><br />
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_breitenau.jpg" name="breitenau" alt="breitenau" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_BREITENAU%>"><%=Constants.STATION_BREITENAU%></a><br />
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_bruck.jpg" name="bruck" alt="bruck" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_BRUCK%>"><%=Constants.STATION_BRUCK%></a><br />
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_kapfenberg.jpg" name="kapfenberg" alt="kapfenberg" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_KAPFENBERG%>"><%=Constants.STATION_KAPFENBERG%></a><br />
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_stMarein.jpg" name="stMarein" alt="stMarein" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_MAREIN%>"><%=Constants.STATION_MAREIN%></a><br />
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_thoerl.jpg" name="thoerl" alt="thoerl" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_THOERL%>"><%=Constants.STATION_THOERL.replaceAll("�", "&ouml;")%></a><br />
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/wappen_thurnau.jpg" name="thurnau" alt="thurnau" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_TURNAU%>"><%=Constants.STATION_TURNAU%></a><br />
		</td>
	</tr>
</table>
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td id="BlockHead" colspan="2"><b>Administration</b></td>
	</tr>
	<tr>
		<td id="navIcon"><img src="../image/benutzerNew.jpg" name="benutzer" alt="benutzer" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/addUser.do"%>">Benutzer anlegen</a><br />
		</td></tr>
		<tr>
		<td id="navIcon"><img src="../image/benutzerEdit.jpg" name="benutzer" alt="benutzer" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/listUser.do"%>">Benutzer editieren</a><br />
		</td>
		<tr>
		<td id="navIcon"><img src="../image/benutzerDel.jpg" name="benutzer" alt="benutzer" /></td><td id="BlockContentNav"><a href="<%=request.getContextPath()+"/Dispatcher/lockUser.do"%>">Benutzer sperren</a><br />
		</td>
	</tr>
</table>
