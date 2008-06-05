<%@ include file="includes.jsp"%>
<c:url var="url" value="/Dispatcher/addRosterEntry.do" />

<c:set var="fieldHeadersRow">
	<tr class="subhead2">	
		<th nowrap="nowrap">TrNr</th>
		<th nowrap="nowrap">Fahrzeug</th>
		<th nowrap="nowrap">Von</th>
		<th nowrap="nowrap">Patient&nbsp;1</th>
		<th nowrap="nowrap">Nach</th>
		<th nowrap="nowrap">Anmerkungen</th>
		<th nowrap="nowrap">Transportart</th>
		<%-- <th nowrap="nowrap">S1</th>
		<th nowrap="nowrap">S2</th>
		<th nowrap="nowrap">S3</th>
		<th nowrap="nowrap">S4</th>
		<th nowrap="nowrap">S5</th>
		<th nowrap="nowrap">S6</th> --%>
		<th nowrap="nowrap">Disponent</th>
		<th nowrap="nowrap">Rückmeldung</th>
		<th nowrap="nowrap">Anrufer</th>
		<th nowrap="nowrap">Erkr/Verl</th>
		
	</tr>
</c:set>
<br />
<br />
<c:set var="journalContainerMap" value="${params.journalContainerListContainer.journalContainerMap}"/>
<c:choose>
	<c:when test="${fn:length(journalContainerMap) gt 0}">
		<table id="runningTable" class="list" cellpadding="3" cellspacing="0">
			<c:forEach var="journalContainerMapEntry" items="${journalContainerMap}">
				<c:set var="location" value="${journalContainerMapEntry.key}"/>	
				<tr>
					<th class="header2" colspan="11">${location.locationName}&nbsp;am&nbsp;<fmt:formatDate
						type="date" dateStyle="medium" value="${params.date}" /></th>
				</tr>
				${fieldHeadersRow}
				<tbody>
					<c:forEach var="journalContainer"
						items="${journalContainerMapEntry.value}" varStatus="loop">
						<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
							<td nowrap="nowrap">${journalContainer.transportNumber}</td>
							<td nowrap="nowrap">${journalContainer.vehicleContainer.vehicleName}</td>
							<td nowrap="nowrap">${journalContainer.fromStreet} &nbsp; ${journalContainer.fromCity}</td>
							<td nowrap="nowrap">${journalContainer.patient.lastname} &nbsp; ${journalContainer.patient.firstname }</td>
							<td nowrap="nowrap">${journalContainer.toStreet} &nbsp; ${journalContainer.toCity }</td>
							<td nowrap="nowrap">${journalContainer.notes}</td>
							<td nowrap="nowrap">${journalContainer.kindOfTransport}</td>
							<td nowrap="nowrap">${journalContainer.disposedByUser}</td>
							<td nowrap="nowrap">${journalContainer.feedback}</td>
							<td nowrap="nowrap">${journalContainer.caller.callerName} &nbsp; ${journalContainer.caller.callerTelephoneNumber}</td>
							<td nowrap="nowrap">${journalContainer.kindOfIllness}</td>
						</tr>
					</c:forEach>
				</tbody>
			</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
		<table cellpadding="3" cellspacing="0" class="list">
			<tr>
				<td class="nodata">Keine&nbsp;Treffer</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose>
