<%@ include file="includes.jsp" %>
<c:url var="url" value="/Dispatcher/rosterDay2.do?action=addRosterEntry" />
<form action="url" method="post" accept-charset="utf-8">
	<div id="generalDataformHeader" class="formHeader">Allgemeine Daten</div>
	<table id="generalDataForm" class="standardForm">
		<tr>
			<td>Mitarbeiter:</td>
			<td>
				<select size="1" name="staffMemberId">
					<option value="">-- Mitarbeiter wählen--</option>
					<c:forEach var="staffMember" items="${params.staffList}">
						<option value="${staffMember.staffMemberId}">
							${staffMember.lastName}&nbsp;${staffMember.firstName}
						</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>Ortsstelle:</td>
			<td>
				<select size="1" name="locationId">
					<option value="">-- Ortsstelle wählen --</option>
					<c:forEach var="location" items="${params.locationList}">
						<option value="${location.id}">${location.locationName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr colspan="2">
			<td>Bereitschaft</td>
		</tr>
		<tr>
			<td>Verwendung:</td>
			<td>
				<c:url var="url" value="/Dispatcher/rosterDay2.do">
				</c:url>
				<select size="1" id="jobId" name="jobId">
					<option value="">-- Verwendung wählen --</option>
					<c:forEach var="job" items="${params.jobList}">
						<option value="${job.id}" ${(not empty params.job) and (params.job.id == job.id) ? ' selected="selected"' : ''}>${job.jobName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>Dienstverhältnis:</td>
			<td>
				<select size="1" name="serviceTypeId">
					<option value="">-- Dienstverhältnis wählen --</option>
					<c:forEach var="serviceType" items="${params.serviceTypeList}">
						<option value="${serviceType.id}">${serviceType.serviceName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>
	<div id="commentFormHeader" class="formHeader">Anmerkungen</div>
	<table id="commentForm" class="standardForm">
		<tr>
			<td>
				<textarea cols="50" rows="10" wrap="soft">
				</textarea>
			</td>
		</tr>
	</table>
	<div id="timeFormHeader" class="formHeader">Dienstzeiten</div>
	<table id="timeForm" class="standardForm">
		<tr>
			<td>Dienst von: 
				<input id="dateFrom" type="text" size="20" maxlength="20" />
				<c:url var="url" value="/image/calendar_edit.gif" />
				<img src="${url}" border="0" id="dateFromCalendarTrigger" style="cursor:pointer" /> 
			</td>
			<td>bis: 
				<input id="dateTo" type="text" size="20" maxlength="20" />
				<c:url var="url" value="/image/calendar_edit.gif" />
				<img src="${url}" border="0" id="dateToCalendarTrigger" style="cursor:pointer" />
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
$(document).ready(function() {
	Calendar.setup ({
		inputField : "dateFrom",
		button : "dateFromCalendarTrigger",
		date : new Date(${params.dateMilliseconds}),
		range : new Array (${params.rangeStart}, ${params.rangeEnd}),
		align : "Tr",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y"
	});
	Calendar.setup ({
		inputField : "dateTo",
		button : "dateToCalendarTrigger",
		date : new Date(${params.dateMilliseconds}),
		range : new Array (${params.rangeStart}, ${params.rangeEnd}),
		align : "Tr",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y"
	});
});
$(function() {			
	$('#jobId').change(function() {
		document.location = '?jobId=' + $(this).val(); 
	});
});
</script>