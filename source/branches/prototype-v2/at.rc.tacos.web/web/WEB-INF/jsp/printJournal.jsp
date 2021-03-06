<%@ include file="includes.jsp"%>
<c:set var="fieldHeadersRow">
	<tr class="subhead2">
		<th nowrap="nowrap">TrNr</th>
		<th nowrap="nowrap">Von</th>
		<th nowrap="nowrap">Patient&nbsp;1</th>
		<th nowrap="nowrap">Nach</th>
		<th nowrap="nowrap">Transportart</th>
		<th nowrap="nowrap">Disponent</th>
		<th nowrap="nowrap">Anrufer</th>
		<th nowrap="nowrap">Erkr/Verl</th>
		<th nowrap="nowrap">Fahrzeug</th>
		<th nowrap="nowrap">Fahrer</th>
		<th nowrap="nowrap">Sanitšter I</th>
		<th nowrap="nowrap">Sanitšter II</th>
		<th nowrap="nowrap">S1</th>
		<th nowrap="nowrap">S2</th>
		<th nowrap="nowrap">S3</th>
		<th nowrap="nowrap">S4</th>
		<th nowrap="nowrap">S5</th>
		<th nowrap="nowrap">S6</th>
	</tr>
</c:set>
<br />
<br />
<c:set var="journalContainerMap"
	value="${params.journalContainerListContainer.journalContainerMap}" />
<c:choose>
	<c:when test="${fn:length(journalContainerMap) gt 0}">
		<table id="journalTable" class="list" cellpadding="3" cellspacing="0">
			<c:forEach var="journalContainerMapEntry"
				items="${journalContainerMap}">
				<c:set var="location" value="${journalContainerMapEntry.key}" />
				<tr>
					<th class="header2" colspan="20">${location.locationName}&nbsp;am&nbsp;<fmt:formatDate
						type="date" dateStyle="medium" value="${params.date}" /></th>
				</tr>
				${fieldHeadersRow}
				<tbody>
					<c:forEach var="journalContainer"
						items="${journalContainerMapEntry.value}" varStatus="loop">
						<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
							<td nowrap="nowrap"><c:choose>
								<c:when test="${journalContainer.transportNumber eq -4}"> NEF</c:when>
								<c:when test="${journalContainer.transportNumber eq -1}"> STORNO</c:when>
								<c:when test="${journalContainer.transportNumber eq -2}"> WTGL</c:when>
								<c:otherwise>${journalContainer.transportNumber}</c:otherwise>
							</c:choose></td>
							<td nowrap="nowrap">${journalContainer.fromStreet} &nbsp;
							${journalContainer.fromCity}</td>
							<td nowrap="nowrap">${journalContainer.patient.lastname}
							&nbsp; ${journalContainer.patient.firstname }</td>
							<td nowrap="nowrap">${journalContainer.toStreet} &nbsp;
							${journalContainer.toCity }</td>
							<td nowrap="nowrap">${journalContainer.kindOfTransport}</td>
							<td nowrap="nowrap">${journalContainer.disposedByUser}</td>
							<td nowrap="nowrap">${journalContainer.caller.callerName}
							&nbsp; ${journalContainer.caller.callerTelephoneNumber}</td>
							<td nowrap="nowrap">${journalContainer.kindOfIllness.diseaseName}</td>
							<td nowrap="nowrap">${journalContainer.vehicleContainer.vehicleName}</td>
							<td nowrap="nowrap">${journalContainer.vehicleContainer.driver.lastName}&nbsp;${journalContainer.vehicleContainer.driver.firstName}</td>
							<td nowrap="nowrap">${journalContainer.vehicleContainer.firstParamedic.lastName}&nbsp;${journalContainer.vehicleContainer.firstParamedic.firstName}</td>
							<td nowrap="nowrap">${journalContainer.vehicleContainer.secondParamedic.lastName}&nbsp;${journalContainer.vehicleContainer.secondParamedic.firstName}</td>
							<td nowrap="nowrap"><fmt:formatDate type="time"
								timeStyle="short" value="${journalContainer.s1}" />
							<td nowrap="nowrap"><fmt:formatDate type="time"
								timeStyle="short" value="${journalContainer.s2}" />
							<td nowrap="nowrap"><fmt:formatDate type="time"
								timeStyle="short" value="${journalContainer.s3}" />
							<td nowrap="nowrap"><fmt:formatDate type="time"
								timeStyle="short" value="${journalContainer.s4}" />
							<td nowrap="nowrap"><fmt:formatDate type="time"
								timeStyle="short" value="${journalContainer.s5}" />
							<td nowrap="nowrap"><fmt:formatDate type="time"
								timeStyle="short" value="${journalContainer.s6}" />
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

<br />
<br />
<c:url var="url" value="/Dispatcher/journal.do" />
<a href="${url}">Zur&uuml;ck</a>