<%@ include file="includes.jsp" %>
<%@ page import="at.rc.tacos.model.Login"%>
<%@ page import="at.rc.tacos.web.session.UserSession" %>
<%
UserSession user = (UserSession)session.getAttribute("userSession");
%>
<table id="Block" width="100%" border="0" cellpadding="0" cellspacing="0">
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
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Dienst eintragen</a>
		</td>
	</tr>
</table>
<table id="Block" width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Fahrzeuge</b></td>
	</tr>
	<c:url var="url" value="/Dispatcher/vehiclesAllocation.do" />
	<tr>
		<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Fahrzeugbesetzungen</a>
		</td>
	</tr>
</table>
<table id="Block" width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Mitarbeiter</b></td>
	</tr>
	<c:if test="${authorization eq 'Administrator'}">
		<c:url var="url" value="/Dispatcher/addStaffMember.do" />
		<tr>
			<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Mitarbeiter anlegen</a>
			</td>
		</tr>
		<c:url var="url" value="/Dispatcher/editStaffMember.do" />
		<tr>
			<td id="navIcon"></td><td id="BlockContentNav"><a href="${url}">Mitarbeiter bearbeiten</a>
			</td>
		</tr>
	</c:if>
</table>