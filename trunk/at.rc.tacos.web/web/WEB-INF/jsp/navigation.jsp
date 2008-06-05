<%@ include file="includes.jsp" %>
<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Dienstplan</b></td>
	</tr>
	<c:url var="url" value="/Dispatcher/addRosterEntry.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Dienst&nbsp;eintragen</a>
		</td>
	</tr>
	<c:url var="url" value="/Dispatcher/roster.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Tagesansicht</a>
		</td>
	</tr>
	<c:url var="url" value="/Dispatcher/rosterMonth.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Monatsansicht</a>
		</td>
	</tr>
</table>
<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Transporte</b></td>
	</tr>
	<c:url var="url" value="/Dispatcher/vehiclesAllocation.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Fahrzeugzuweisung</a>
		</td>
	</tr>
	<c:url var="url" value="/Dispatcher/transportsTo.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Vormerkungen</a>
		</td>
	</tr>
	<c:url var="url" value="/Dispatcher/journal.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Journal</a>
		</td>
	</tr>
</table>
<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Verwaltung</b></td>
	</tr>
	<c:if test="${userSession.loginInformation.authorization eq 'Administrator'}">
		<c:url var="url" value="/Dispatcher/staffMembers.do" />
		<tr>
			<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Mitarbeiter</a>
			</td>
		</tr>
		<c:url var="url" value="/Dispatcher/addStaffMember.do" />
		<tr>
			<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Mitarbeiter&nbsp;anlegen</a>
			</td>
		</tr>
		<c:url var="url" value="/Dispatcher/editStaffMember.do" />
		<tr>
			<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Mitarbeiter&nbsp;bearbeiten</a>
			</td>
		</tr>
	</c:if>
	<c:url var="url" value="/Dispatcher/personnelData.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Profil</a>
		</td>
	</tr>
</table>