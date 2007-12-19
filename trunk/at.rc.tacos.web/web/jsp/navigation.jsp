<%@page import="at.rc.tacos.common.Constants"%>
<table id="Block" width="100%" border='0' cellpadding='0'
	cellspacing='0'>
	<tr>
		<td id="BlockHead"><b>Navigation</b></td>
	</tr>
	<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/home.do"%>">Home</a><br />
		</td>
	</tr>
	<tr>
		<td id="BlockContent"><a
			href="<%=request.getContextPath()+"/Dispatcher/rosterEntry.do"%>">Dienst eintragen</a><br />
		</td>
	</tr>
	<tr>
		<td id="BlockContent"><a
			href="<%=request.getContextPath()+"/Dispatcher/rosterDay.do?action=dayView"%>">Tages&uuml;bersicht</a><br />
		</td>
	</tr>
	<tr>
		<td id="BlockContent"><a
			href="<%=request.getContextPath()+"/Dispatcher/rosterWeek.do?action=weekView"%>">Wochen&uuml;bersicht</a><br />
		</td>
	</tr>
</table>
<!-- NEXT BLOCK  -->
<table id="Block" width="100%" border='0' cellpadding='0'
	cellspacing='0'>
	<tr>
		<td id="BlockHead"><b>Ortsstellen</b></td>
	</tr>
	<tr>
		<td id="BlockContent">
		<a href="<%=request.getContextPath()+"/Dispatcher/station.do?action=Breitenau"%>"><%=Constants.STATION_BREITENAU%></a><br />
		<a href="<%=request.getContextPath()+"/Dispatcher/station.do?action=Bruck"%>"><%=Constants.STATION_BRUCK%></a><br />
		<a href="<%=request.getContextPath()+"/Dispatcher/station.do?action=Kapfenberg"%>"><%=Constants.STATION_KAPFENBERG%></a><br />
		<a href="<%=request.getContextPath()+"/Dispatcher/station.do?action=StMarein"%>"><%=Constants.STATION_MAREIN%></a><br />
		<a href="<%=request.getContextPath()+"/Dispatcher/station.do?action=Thoerl"%>"><%=Constants.STATION_THOERL.replaceAll("ö","&ouml;")%></a><br />
		<a href="<%=request.getContextPath()+"/Dispatcher/station.do?action=Turnau"%>"><%=Constants.STATION_TURNAU%></a><br />
		</td>
	</tr>
</table>