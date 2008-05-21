<%@ include file="includes.jsp" %>
<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Dienste</b></td>
	</tr>
	<c:url var="url" value="/Dispatcher/roster.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Dienstplan</a>
		</td>
	</tr>
	<c:url var="url" value="/Dispatcher/addRosterEntry.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Dienst&nbsp;eintragen</a>
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
</table>
<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Mitarbeiter</b></td>
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
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Pers&ouml;nliche&nbsp;Daten</a>
		</td>
	</tr>
</table>