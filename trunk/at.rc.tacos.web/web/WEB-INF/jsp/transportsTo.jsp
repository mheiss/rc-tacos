<%@ include file="includes.jsp"%>
<c:set var="fieldHeadersRow">
	<tr class="subhead2">
		<th nowrap="nowrap">Von</th>
		<th nowrap="nowrap">Patient</th>
		<th nowrap="nowrap">Nach</th>
		<th nowrap="nowrap">Abfahrt</th>
		<th nowrap="nowrap">Bei Patient</th>
		<th nowrap="nowrap">Termin</th>
		<th nowrap="nowrap">Anmerkungen</th>
		<th nowrap="nowrap">Begleitperson</th>
		<th nowrap="nowrap">Transportart</th>
		
	</tr>
</c:set>
<c:set var="transportsToMap" value="${params.transportsToContainerListContainer.transportsToContainerMap}"/>
<c:choose>
	<c:when test="${fn:length(transportsToMap) gt 0}">
		<table cellpadding="3" cellspacing="0" class="list">
			<c:forEach var="transportsToMapEntry" items="${transportsToMap}">
				<c:set var="location" value="${transportsToMapEntry.key}"/>
				<tr>
					<th class="header2" colspan="9">${location.locationName}</th>
				</tr>
				${fieldHeadersRow}
				<c:forEach var="transportsToContainer" items="${transportsToMapEntry.value}" varStatus="loop">
					<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
						<td nowrap="nowrap">${transportsToContainer.fromStreet} &nbsp; ${transportsToContainer.fromCity}</td>
						<td nowrap="nowrap">${transportsToContainer.patient.lastname} &nbsp; ${transportsToContainer.patient.firstname }</td>
						<td nowrap="nowrap">${transportsToContainer.toStreet} &nbsp; ${transportsToContainer.toCity }</td>
						<td nowrap="nowrap">
							<fmt:formatDate type="both" timeStyle="short" value="${transportsToContainer.plannedStartOfTransport}" />
						<td nowrap="nowrap">
							<fmt:formatDate type="time" timeStyle="short" value="${transportsToContainer.plannedTimeAtPatient}" />
						<td nowrap="nowrap">
							<fmt:formatDate type="time" timeStyle="short" value="${transportsToContainer.appointmentTimeAtDestination}" /></td>
						<td nowrap="nowrap">${transportsToContainer.notes}</td>
						<td nowrap="nowrap"><c:choose>
								<c:when
									test="${transportsToContainer.assistantPerson eq true}">Ja</c:when>
								<c:otherwise>Nein</c:otherwise>
							</c:choose></td>
						<td nowrap="nowrap">${transportsToContainer.kindOfTransport}</td>
					</tr>
				</c:forEach>
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