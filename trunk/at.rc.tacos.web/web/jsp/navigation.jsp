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
		<td id="BlockHead"><b>Pers&ouml;nliche Daten</b></td>
	</tr>
	<tr>
		<td id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/profile.do"%>">Meine Daten</a><br />
		<a href="#">Notiz</a><br />
		</td>
	</tr>
</table>