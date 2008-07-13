<%@ include file="includes.jsp"%>
<c:if test="${messageOfTheDay ne null and messageOfTheDay.message ne ''}">
	<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td id="BlockHead" colspan="2"><b>Message&nbsp;of&nbsp;the&nbsp;day&nbsp;
			<c:choose>
				<c:when test="${userSession.defaultFormValues.defaultDate ne null}">
					<fmt:formatDate type="date" dateStyle="medium" value="${userSession.defaultFormValues.defaultDate}" />
				</c:when>
				<c:otherwise>
					<fmt:formatDate type="date" dateStyle="medium" value="${userSession.today}" />
				</c:otherwise>
			</c:choose>
			by
			${messageOfTheDay.lastChangedBy}</b></td>
		</tr>
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav">${messageOfTheDay.message}</td>
		</tr>
	</table>
</c:if>
<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Dienstplan</b></td>
	</tr>
	<c:url var="url" value="/Dispatcher/addRosterEntry.do" />
	<tr>
		<td id="navIcon"></td>
		<td id="BlockContentNav"><a href="${url}">Dienst&nbsp;eintragen</a>
		</td>
	</tr>
	<c:url var="url" value="/Dispatcher/roster.do" />
	<tr>
		<td id="navIcon"></td>
		<td id="BlockContentNav"><a href="${url}">Tagesansicht</a></td>
	</tr>
	<c:if
		test="${userSession.loginInformation.authorization eq 'Administrator'}">
		<c:url var="url" value="/Dispatcher/rosterMonth.do" />
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav"><a href="${url}">Monatsansicht</a></td>
		</tr>
	</c:if>
</table>
<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Statistiken</b></td>
	</tr>
	<c:url var="url" value="/Dispatcher/personalStatistic.do" />
	<tr>
		<td id="navIcon"></td>
		<td id="BlockContentNav"><a href="${url}">Pers&ouml;nliche&nbsp;Statistik</a>
		</td>
	</tr>
	<c:if
		test="${userSession.loginInformation.authorization eq 'Administrator'}">
		<c:url var="url" value="/Dispatcher/adminStatistic.do" />
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav"><a href="${url}">Adminstatistik</a></td>
		</tr>
	</c:if>
</table>
<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Transporte</b></td>
	</tr>
	<c:url var="url" value="/Dispatcher/vehiclesAllocation.do" />
	<tr>
		<td id="navIcon"></td>
		<td id="BlockContentNav"><a href="${url}">Fahrzeugzuweisung</a></td>
	</tr>
	<c:if
		test="${userSession.loginInformation.authorization eq 'Administrator'}">
		<c:url var="url" value="/Dispatcher/transportsTo.do" />
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav"><a href="${url}">Vormerkungen</a></td>
		</tr>
	</c:if>
	<c:url var="url" value="/Dispatcher/running.do" />
	<tr>
		<td id="navIcon"></td>
		<td id="BlockContentNav"><a href="${url}">Laufende Transporte</a>
		</td>
	</tr>
	<c:if
		test="${userSession.loginInformation.authorization eq 'Administrator'}">
		<c:url var="url" value="/Dispatcher/journal.do" />
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav"><a href="${url}">Journal</a></td>
		</tr>
	</c:if>
	<c:url var="url" value="/Dispatcher/journalShort.do" />
	<tr>
		<td id="navIcon"></td>
		<td id="BlockContentNav"><a href="${url}">Kurzjournal</a></td>
	</tr>
</table>
<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td id="BlockHead" colspan="2"><b>Verwaltung</b></td>
	</tr>
	<c:if
		test="${userSession.loginInformation.authorization eq 'Administrator'}">
		<c:url var="url" value="/Dispatcher/staffMembers.do" />
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav"><a href="${url}">Mitarbeiter</a></td>
		</tr>
		<c:url var="url" value="/Dispatcher/addStaffMember.do" />
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav"><a href="${url}">Mitarbeiter&nbsp;anlegen</a>
			</td>
		</tr>
		<c:url var="url" value="/Dispatcher/editStaffMember.do" />
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav"><a href="${url}">Mitarbeiter&nbsp;bearbeiten</a>
			</td>
		</tr>
	</c:if>
	<c:url var="url" value="/Dispatcher/personalData.do" />
	<tr>
		<td id="navIcon"></td>
		<td id="BlockContentNav"><a href="${url}">Profil</a></td>
	</tr>
	<c:if test="${userSession.loginInformation.authorization eq 'Administrator'}">
		<c:url var="url" value="/Dispatcher/messageOfTheDay.do" />
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav"><a href="${url}">Message&nbsp;of&nbsp;the&nbsp;day</a></td>
		</tr>
		<c:url var="url" value="/Dispatcher/addLink.do" />
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav"><a href="${url}">Link&nbsp;anlegen</a></td>
		</tr>
		<c:url var="url" value="/Dispatcher/links.do" />
		<tr>
			<td id="navIcon"></td>
			<td id="BlockContentNav"><a href="${url}">Links</a></td>
		</tr>
	</c:if>
</table>
<c:if test="${fn:length(linkList) gt 0}">
	<table id="Block" width="250" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td id="BlockHead" colspan="2"><b>Links</b></td>
		</tr>
		<c:forEach var="link" items="${linkList}">
			<tr>
				<c:set var="title">${link.title}</c:set>
				<td id="navIcon"></td>
				<td id="BlockContentNav">
					<a href="${link.href}" class="showLinkInfo" title="${title}">${link.innerText}</a>
				</td>
			</tr>
		</c:forEach>
	</table>
</c:if>
<script type="text/javascript">
$(document).ready(function() {
	$('.showLinkInfo').Tooltip({ delay: 100, showURL: false });
});
</script>