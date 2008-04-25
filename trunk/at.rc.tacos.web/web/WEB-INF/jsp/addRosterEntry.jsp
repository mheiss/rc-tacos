<%@ include file="includes.jsp" %>
<c:url var="url" value="/Dispatcher/rosterDay2.do?action=addRosterEntry">
	<c:param name="action">addRosterEntry</c:param>
</c:url>
<form action="${url}" method="post" accept-charset="utf-8">
	<div id="generalDataformHeader" class="formHeader">Allgemeine Daten</div>
	<table id="generalDataForm" class="standardForm">
		<tr>
			<td>Mitarbeiter:</td>
			<td>
				<select size="1" id="staffMemberId" name="staffMemberId">
					<option value="">-- Mitarbeiter wählen--</option>
					<c:forEach var="staffMember" items="${params.staffList}">
						<option value="${staffMember.staffMemberId}" ${(not empty params.staffMember) and (params.staffMember.staffMemberId == staffMember.staffMemberId) ? ' selected="selected"' : ''}>
							${staffMember.lastName}&nbsp;${staffMember.firstName}
						</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>Ortsstelle:</td>
			<td>
				<select size="1" id="locationId" name="locationId">
					<option value="">-- Ortsstelle wählen --</option>
					<c:forEach var="location" items="${params.locationList}">
						<option value="${location.id}" ${(not empty params.location) and (params.location.id == location.id) ? ' selected="selected"' : ''}>${location.locationName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td/>
			<td><label for="standby" style="cursor:pointer"/><input id="standby" name="standby" type="checkbox" />Bereitschaft</td>
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
				<select size="1" id="serviceTypeId" name="serviceTypeId">
					<option value="">-- Dienstverhältnis wählen --</option>
					<c:forEach var="serviceType" items="${params.serviceTypeList}">
						<option value="${serviceType.id}" ${(not empty params.serviceType) and (params.serviceType.id == serviceType.id) ? ' selected="selected"' : ''}>${serviceType.serviceName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>
	<div id="commentFormHeader" class="formHeader">Anmerkungen</div>
	<table id="commentForm" class="standardForm">
		<tr>
			<td>
				<textarea id="comment" cols="40" rows="7" wrap="soft">
				</textarea>
			</td>
		</tr>
	</table>
	<div id="timeFormHeader" class="formHeader">Dienstzeiten</div>
	<table id="timeForm" class="standardForm">
		<tr>
			<td>
				<table>
					<tr>
						<td>
							Dienst von: 
						</td>
						<td>
							<input id="dateFrom" name="dateFrom" type="text" size="10" maxlength="10" />
							<c:url var="url" value="/image/calendar_edit.gif" />
							<img src="${url}" border="0" id="dateFromCalendarTrigger" style="cursor:pointer" />
						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<select size="1" id="timeFromHours" name="timeFromHours">
								<c:forTokens var="i" items="0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23" delims=",">
									<option>${i}</option>
								</c:forTokens>
							</select>
							<select size="1" id="timeFromMinutes" name="timeFromMinutes">
								<c:forTokens var="i" items="0,5,10,15,20,25,30,35,40,45,50,55,60" delims=",">
									<option>${i}</option>
								</c:forTokens>
							</select>
						</td>
					</tr>
					<tr>
						<td>
							bis: 
						</td>
						<td>
							<input id="dateTo" name="dateTo" type="text" size="10" maxlength="10" />
							<c:url var="url" value="/image/calendar_edit.gif" />
							<img src="${url}" border="0" id="dateToCalendarTrigger" style="cursor:pointer" />
						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<select size="1" id="timeToHours" name="timeToHours">
								<c:forTokens var="i" items="0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23" delims=",">
									<option>${i}</option>
								</c:forTokens>
							</select>
							<select size="1" id="timeToMinutes" name="timeToMinutes">
								<c:forTokens var="i" items="0,5,10,15,20,25,30,35,40,45,50,55,60" delims=",">
									<option>${i}</option>
								</c:forTokens>
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td>
				<input type="submit" value="Anlegen" />
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
		var staffMemberId = $('#staffMemberId').val();
		var locationId = $('#locationId').val();
		var jobId = $('#jobId').val();
		var serviceTypeId = $('#serviceTypeId').val();
		var comment = null;
		comment = $('#comment').val();
		var dateFrom = $('#dateFrom').val();
		var timeFromHours = $('#timeFromHours').val();
		var timeFromMinutes = $('#timeFromMinutes').val();
		var dateTo = $('#dateTo').val();
		var timeToHours = $('#timeToHours').val();
		var timeToMinutes = $('#timeToMinutes').val();
		var url = '?jobId=' + $(this).val();
		if (staffMemberId) {
			 url += '&staffMemberId=' + staffMemberId;
		}
		if (locationId) {
			url += '&locationId=' + locationId;
		}
		if (jobId) {
			url += '&jobId=' + jobId;
		}
		if (serviceTypeId) {
			url += '&serviceTypeId=' + serviceTypeId;
		}
		if (comment && jQuery.trim(comment) != '') {
			url += '&comment=' + comment;
		}
		if (dateFrom) {
			url += '&dateFrom=' + dateFrom;
		}
		if (timeFromHours) {
			url += '&timeFromHours=' + timeFromHours;
		}
		if (timeFromMinutes) {
			url += '&timeFromMinutes=' + timeFromMinutes;
		}
		if (dateTo) {
			url += '&dateTo=' + dateTo;
		}
		if (timeToHours) {
			url += '&timeToHours=' + timeToHours;
		}
		if (timeToMinutes) {
			url += '&timeToMinutes=' + timeToMinutes;
		}
		document.location = url;
	});
});
</script>