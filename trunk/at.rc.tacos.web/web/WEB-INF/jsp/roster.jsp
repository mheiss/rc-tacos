<%@ include file="includes.jsp" %>
<c:url var="url" value="/Dispatcher/addRosterEntry.do" />
<table class="standardForm"">
	<tr>
		<td>Ortsstelle:</td>
		<td>
			<select size="1" id="locationId" name="locationId">
				<option value="noValue">-- Ortsstelle wählen --</option>
				<c:forEach var="location" items="${params.locationList}">
					<option value="${location.id}" ${(not empty params.location) and (params.location.id == location.id) ? ' selected="selected"' : ''}>${location.locationName}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td>Datum:</td>
		<td>
			<input id="date" name="date" type="text" size="10" maxlength="10" value="<fmt:formatDate type="date" value="${params.date}"/>" />
			<c:url var="url" value="/image/calendar_edit.gif" />
			<img src="${url}" border="0" id="dateCalendarTrigger" style="cursor:pointer" />
		</td>
	</tr>
</table>
<c:set var="fieldHeadersRow">
	<tr class="subhead2">
		<th nowrap="nowrap">Name</th>
		<th nowrap="nowrap">von (geplant)</th>
		<th nowrap="nowrap">bis (geplant)</th>
		<th nowrap="nowrap">von (real)</th>
		<th nowrap="nowrap">bis (real)</th>
		<th nowrap="nowrap">Verwendung</th>
		<th nowrap="nowrap">Dienstverhältnis</th>
		<th nowrap="nowrap">&nbsp;</th>
		<th nowrap="nowrap">Bereitschaft</th>
		<th nowrap="nowrap">&nbsp;</th>
	</tr>
</c:set>
<br />
<br />
<table class="list">
	<tr>
		<th class="header2" colspan="10">Dienste</th>
	</tr>
	${fieldHeadersRow}
	<tbody>
		<c:forEach var="rosterEntryContainer" items="${params.rosterEntryContainerList}" varStatus="loop">
			<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
				<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.staffMember.lastName} ${rosterEntryContainer.rosterEntry.staffMember.firstName}</td>
				<td nowrap="nowrap">
					<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${rosterEntryContainer.plannedStartOfWork}" />
				</td>
				<td nowrap="nowrap">
					<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${rosterEntryContainer.plannedEndOfWork}" />
				</td>
				<td nowrap="nowrap">
					<c:choose>
						<c:when test="${rosterEntryContainer.realStartOfWork eq null}">-</c:when>
						<c:otherwise>
							<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${rosterEntryContainer.realStartOfWork}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td nowrap="nowrap">
					<c:choose>
						<c:when test="${rosterEntry.Container.realEndOfWork eq null}">-</c:when>
						<c:otherwise>
							<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${rosterEntryContainer.realEndOfWork}" />
						</c:otherwise>
					</c:choose>
				</td>
				<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.job.jobName}</td>
				<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.servicetype.serviceName}</td>
				<td>&nbsp;</td>
				<td nowrap="nowrap">
					<c:choose>
						<c:when test="${rosterEntryContainer.rosterEntry.standby eq true}">Ja</c:when>
						<c:otherwise>Nein</c:otherwise>
					</c:choose>
				</td>
				<td>&nbsp;</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
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
});
$(function() {			
	$('#locationId').change(function() {
		var url = '?locationId=' + $(this).val();
		var date = $('#date').val();
		if (date) {
			url += '&date=' + date;
		}
		document.location = url;
	});
});
function update(cal) {
	var url = '?date=' + $('#date').val();
	var locationId = $('#locationId').val();
	if (locationId) {
		url += '&locationId=' + locationId;
	}
	document.location = url;
}
</script>