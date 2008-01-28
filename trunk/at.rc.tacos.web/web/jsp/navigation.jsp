<%@page import="at.rc.tacos.common.Constants"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td id="BlockHead"><b>Navigation</b></td>
	</tr>
	<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/rosterEntry.do"%>">Dienst eintragen</a><br />
		</td>
	</tr>
	<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/rosterDay.do?action=dayView"%>">Tages&uuml;bersicht</a><br />
		</td>
	</tr>
	<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/rosterWeek.do?action=weekView"%>">Wochen&uuml;bersicht</a><br />
		</td>
	</tr>
	<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/printRoster.do"%>">Dienstplan drucken</a><br />
		</td>
	</tr>
	<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/editProfile.do"%>">Profil editieren</a><br />
		</td>
	</tr>
</table>
<!-- NEXT BLOCK  -->
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td id="BlockHead"><b>Ortsstellen</b></td>
	</tr>
	<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_BEZIRK%>"><%=Constants.STATION_BEZIRK%></a><br />
		</td></tr>
		<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_BREITENAU%>"><%=Constants.STATION_BREITENAU%></a><br />
		</td></tr>
		<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_BRUCK%>"><%=Constants.STATION_BRUCK%></a><br />
		</td></tr>
		<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_KAPFENBERG%>"><%=Constants.STATION_KAPFENBERG%></a><br />
		</td></tr>
		<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_MAREIN%>"><%=Constants.STATION_MAREIN%></a><br />
		</td></tr>
		<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_THOERL%>"><%=Constants.STATION_THOERL.replaceAll("ö", "&ouml;")%></a><br />
		</td></tr>
		<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/stationWeek.do?action="+Constants.STATION_TURNAU%>"><%=Constants.STATION_TURNAU%></a><br />
		</td>
	</tr>
</table>
<table id="Block" width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td id="BlockHead"><b>Administration</b></td>
	</tr>
	<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/addUser.do"%>">Benutzer anlegen</a><br />
		</td></tr>
		<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/listUser.do"%>">Benutzer editieren</a><br />
		</td>
		<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/lockUser.do"%>">Benutzer sperren</a><br />
		</td>
	</tr>
</table>
