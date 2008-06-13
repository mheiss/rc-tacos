<%@ include file="includes.jsp"%>
<c:url var="url" value="/Dispatcher/addRosterEntry.do" />

<table class="standardForm">
	<tr>
		<td style="font-weight:bold">Ortsstelle:</td>
		<td><select size="1" id="locationId" name="locationId">
			<option value="noValue">-- Ortsstelle w�hlen --</option>
			<c:forEach var="location" items="${params.locationList}">
				<option value="${location.id}" ${(not empty
					params.location) and (params.location.id==
					location.id) ? ' selected="selected"' : ''}>${location.locationName}</option>
			</c:forEach>
		</select></td>
	</tr>
	
	<tr>
		<td style="font-weight:bold">Fahrzeug:</td>
		<td><select size="1" id="vehicleName" name="vehicleName">
			<option value="noValue">-- Fahrzeug w�hlen --</option>
			<c:forEach var="vehicleDetail" items="${params.vehicleDetailList}">
				<option value="${vehicleDetail.vehicleName}" ${(not empty
					params.vehicleContainer) and (params.vehicleContainer.vehicleName==
					vehicleDetail.vehicleName) ? ' selected="selected"' : ''}>${vehicleDetail.vehicleName}</option>
			</c:forEach>
		</select></td>
	</tr>
	
	<tr>
		<td style="font-weight:bold">Datum:</td>
		<td><input id="date" name="date" type="text" size="10"
			maxlength="10"
			value="<fmt:formatDate type="date" value="${params.date}"/>" /> <c:url
			var="url" value="/image/calendar_edit.gif" /> <img src="${url}"
			border="0" id="dateCalendarTrigger" style="cursor: pointer" /></td>
	</tr>
</table>
<c:set var="fieldHeadersRow">
	<tr class="subhead2">	
		<th nowrap="nowrap">TrNr</th>
		<th nowrap="nowrap">Fahrzeug</th>
		<th nowrap="nowrap">Fahrer</th>
		<th nowrap="nowrap">Sanit�ter I</th>
		<th nowrap="nowrap">Sanit�ter II</th>
		<th nowrap="nowrap">S1</th>
		<th nowrap="nowrap">S2</th>
		<th nowrap="nowrap">S5</th>
		<th nowrap="nowrap">Von</th>
		<th nowrap="nowrap">Patient&nbsp;1</th>
		<th nowrap="nowrap">Nach</th>
		<th nowrap="nowrap">Transportart</th>
		<th nowrap="nowrap">Disponent</th>
		
	</tr>
</c:set>
<br />
<br />
<c:set var="journalContainerMap" value="${params.journalContainerListContainer.journalContainerMap}"/>
<c:choose>
	<c:when test="${fn:length(journalContainerMap) gt 0}">
		<table id="journalTable" class="list" cellpadding="3" cellspacing="0">
			<c:forEach var="journalContainerMapEntry" items="${journalContainerMap}">
				<c:set var="location" value="${journalContainerMapEntry.key}"/>	
				<tr>
					<th class="header2" colspan="13">${location.locationName}&nbsp;am&nbsp;<fmt:formatDate
						type="date" dateStyle="medium" value="${params.date}" /></th>
				</tr>
				${fieldHeadersRow}
				<tbody>
					<c:forEach var="journalContainer"
						items="${journalContainerMapEntry.value}" varStatus="loop">
						<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
							<td nowrap="nowrap"><c:choose>
								<c:when
									test="${journalContainer.transportNumber eq -4}"> NEF</c:when>
								<c:when
									test="${journalContainer.transportNumber eq -1}"> STORNO</c:when>
								<c:when
									test="${journalContainer.transportNumber eq -2}"> WTGL</c:when>
								<c:otherwise>${journalContainer.transportNumber}</c:otherwise>
							</c:choose></td>
							<td nowrap="nowrap">${journalContainer.vehicleContainer.vehicleName}</td>
							<td nowrap="nowrap">${journalContainer.vehicleContainer.driver.lastName}&nbsp;${journalContainer.vehicleContainer.driver.firstName}</td>
							<td nowrap="nowrap">${journalContainer.vehicleContainer.firstParamedic.lastName}&nbsp;${journalContainer.vehicleContainer.firstParamedic.firstName}</td>
							<td nowrap="nowrap">${journalContainer.vehicleContainer.secondParamedic.lastName}&nbsp;${journalContainer.vehicleContainer.secondParamedic.firstName}</td>
							<td nowrap="nowrap">
								<fmt:formatDate type="time" timeStyle="short" value="${journalContainer.s1}" />
							<td nowrap="nowrap">
								<fmt:formatDate type="time" timeStyle="short" value="${journalContainer.s2}" />
							<td nowrap="nowrap">
								<fmt:formatDate type="time" timeStyle="short" value="${journalContainer.s5}" />
							<td nowrap="nowrap">${journalContainer.fromStreet} &nbsp; ${journalContainer.fromCity}</td>
							<td nowrap="nowrap">${journalContainer.patient.lastname} &nbsp; ${journalContainer.patient.firstname }</td>
							<td nowrap="nowrap">${journalContainer.toStreet} &nbsp; ${journalContainer.toCity }</td>
							<td nowrap="nowrap">${journalContainer.kindOfTransport}</td>
							<td nowrap="nowrap">${journalContainer.disposedByUser}</td>
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
<script type="text/javascript">
$(document).ready(function() {
	Calendar.setup ({
		inputField : "date",
		button : "dateCalendarTrigger",
		date : new Date(${params.calendarDefaultDateMilliseconds}),
		range : new Array (${params.calendarRangeStart}, ${params.calendarRangeEnd}),
		align : "Br",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y",
		onClose : update
	});
	
	$('#locationId').change(function() {
		var url = '?locationId=' + $(this).val();
		update(url, 'l');
	});
	
	$('#vehicleName').change(function() {
		var url = '?vehicleName=' + $(this).val();		
		update(url, 'v');
	});
	
	$('#journalTable .showJournalInfo').Tooltip({ delay: 100, showURL: false });
	
	function update(url, code) {
		var locationId = $('#locationId').val();
		var vehicleName = $('#vehicleName').val();
		if (code == 'l') {
			url += '&vehicleName=' + vehicleName;
		} else if (code == 'v') {
			url += '&locationId=' + locationId;
		}
		document.location = url;
	}
});
</script>