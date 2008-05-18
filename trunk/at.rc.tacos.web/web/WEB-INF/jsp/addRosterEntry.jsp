<%@ include file="includes.jsp" %>
<c:url var="url" value="/Dispatcher/addRosterEntry.do">
</c:url>
<c:choose>
	<c:when test="${params.addedCount gt 0}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;neuen&nbsp;Dienstplaneintrag&nbsp;erstellt</div>
		<br />
	</c:when>
</c:choose>
<form action="${url}" method="post" accept-charset="ISO-8859-1">
	<table class="standardForm">
		<tr><td colspan="2">Allgemeine&nbsp;Daten:</td><td /></tr>
		<tr>
			<td>Mitarbeiter:<sup class="reqMark">*</sup></td>
			<td>
				<select size="1" id="staffMemberId" name="staffMemberId">
					<option value="noValue">-- Mitarbeiter wählen--</option>
					<c:forEach var="staffMember" items="${params.staffList}">
						<option value="${staffMember.staffMemberId}" ${(not empty params.staffMember) and (params.staffMember.staffMemberId == staffMember.staffMemberId) ? ' selected="selected"' : ''}>
							${staffMember.lastName}&nbsp;${staffMember.firstName}
						</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<span class="errorText">${params.errors.staffMember}</span>
			</td>
		</tr>
		<tr>
			<td>Ortsstelle:<sup class="reqMark">*</sup></td>
			<td>
				<select size="1" id="locationId" name="locationId">
					<option value="noValue">-- Ortsstelle wählen --</option>
					<c:forEach var="location" items="${params.locationList}">
						<option value="${location.id}" ${(not empty params.location) and (params.location.id == location.id) ? ' selected="selected"' : ''}>${location.locationName}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<span class="errorText">${params.errors.location}</span>
			</td>
		</tr>
		<tr>
			<td>Verwendung:<sup class="reqMark">*</sup></td>
			<td>
				<select size="1" id="jobId" name="jobId">
					<option value="noValue">-- Verwendung wählen --</option>
					<c:forEach var="job" items="${params.jobList}">
						<option value="${job.id}" ${(not empty params.job) and (params.job.id == job.id) ? ' selected="selected"' : ''}>${job.jobName}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<span class="errorText">${params.errors.job}</span>
			</td>
		</tr>
		<tr>
			<td>Dienstverhältnis:<sup class="reqMark">*</sup></td>
			<td>
				<select size="1" id="serviceTypeId" name="serviceTypeId">
					<option value="noValue">-- Dienstverhältnis wählen --</option>
					<c:forEach var="serviceType" items="${params.serviceTypeList}">
						<option value="${serviceType.id}" ${(not empty params.serviceType) and (params.serviceType.id == serviceType.id) ? ' selected="selected"' : ''}>${serviceType.serviceName}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<span class="errorText">${params.errors.serviceType}</span>
			</td>
		</tr>
		<tr>
			<td />
			<td><input id="standby" name="standby" type="checkbox" value="true"${(not empty params.standby) and (params.standby == true) ? ' checked="checked"' : ''} /><label for="standby" style="cursor:pointer">Bereitschaft</label></td>
			<td />
		</tr>
		<tr><td colspan="3">Anmerkungen:</td></tr>
		<tr>
			<td colspan="2">
				<textarea id="comment" name="comment" cols="40" rows="7" wrap="soft">${params.comment}</textarea>
			</td>
			<td />
		</tr>
		<tr><td>Dienstzeiten:</td></tr>
		<tr>
			<td>
				Dienst&nbsp;von:<sup class="reqMark">*</sup> 
			</td>
			<td>
				<input id="dateFrom" name="dateFrom" type="text" size="10" maxlength="10" value="${params.dateFrom}" />
				<c:url var="url" value="/image/calendar_edit.gif" />
				<img src="${url}" border="0" id="dateFromCalendarTrigger" style="cursor:pointer" />
			</td>
			<td />
		</tr>
		<tr>
			<td />
			<td>
				<select size="1" id="timeFromHours" name="timeFromHours">
					<c:forTokens var="i" items="00,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23" delims=",">
						<option ${(not empty params.timeFromHours) and (params.timeFromHours == i) ? ' selected="selected"' : ''}>${i}</option>
					</c:forTokens>
				</select> :
				<select size="1" id="timeFromMinutes" name="timeFromMinutes">
					<c:forTokens var="i" items="00,30" delims=",">
						<option ${(not empty params.timeFromMinutes) and (params.timeFromMinutes == i) ? ' selected="selected"' : ''}>${i}</option>
					</c:forTokens>
				</select>
			</td>
			<td>
				<span class="errorText">
					<c:choose>
						<c:when test="${not empty params.errors.plannedStartOfWorkMissing}">
							${params.errors.plannedStartOfWorkMissing}
						</c:when>
						<c:when test="${not empty params.errors.plannedStartOfWorkError}">
							${params.errors.plannedStartOfWorkError}
						</c:when>
						<c:when test="${not empty params.errors.plannedStartOfWorkTooSmall}">
							${params.errors.plannedStartOfWorkTooSmall}
						</c:when>
						<c:when test="${not empty params.errors.plannedStartOfWorkTooBig}">
							${params.errors.plannedStartOfWorkTooBig}
						</c:when>
					</c:choose>
				</span>
			</td>
		</tr>
		<tr>
			<td>
				bis:<sup class="reqMark">*</sup>
			</td>
			<td>
				<input id="dateTo" name="dateTo" type="text" size="10" maxlength="10" value="${params.dateTo}" />
				<c:url var="url" value="/image/calendar_edit.gif" />
				<img src="${url}" border="0" id="dateToCalendarTrigger" style="cursor:pointer" />
			</td>
			<td />
		</tr>
		<tr>
			<td />
			<td>
				<select size="1" id="timeToHours" name="timeToHours">
					<c:forTokens var="i" items="00,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23" delims=",">
						<option ${(not empty params.timeToHours) and (params.timeToHours == i) ? ' selected="selected"' : ''}>${i}</option>
					</c:forTokens>
				</select> :
				<select size="1" id="timeToMinutes" name="timeToMinutes">
					<c:forTokens var="i" items="00,30" delims=",">
						<option ${(not empty params.timeToMinutes) and (params.timeToMinutes == i) ? ' selected="selected"' : ''}>${i}</option>
					</c:forTokens>
				</select>
			</td>
			<td>
			<span class="errorText">
				<c:choose>
					<c:when test="${not empty params.errors.plannedEndOfWorkMissing}">
						${params.errors.plannedEndOfWorkMissing}
					</c:when>
					<c:when test="${not empty params.errors.plannedEndOfWorkError}">
						${params.errors.plannedEndOfWorkError}
					</c:when>
					<c:when test="${not empty params.errors.plannedEndOfWorkTooSmall}">
						${params.errors.plannedEndOfWorkTooSmall}
					</c:when>
					<c:when test="${not empty params.errors.plannedEndOfWorkTooBig}">
						${params.errors.plannedEndOfWorkTooBig}
					</c:when>
					<c:when test="${not empty params.errors.period}">
						${params.errors.period}
					</c:when>	
				</c:choose>
			</span>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="reqComment">Mit&nbsp;*&nbsp;markierte&nbsp;Felder&nbsp;sind&nbsp;Pflichtfelder.</td>
			<td />
		</tr>
		<tr>
			<td class="hButtonArea" colspan="2">
				<input type="submit" value="Anlegen" />
				<input name="action" type="hidden" value="addRosterEntry" />
			</td>
			<td />
		</tr>
	</table>
</form>
<script type="text/javascript">
$(document).ready(function() {
	Calendar.setup ({
		inputField : "dateFrom",
		button : "dateFromCalendarTrigger",
		date : new Date(${params.calendarDefaultDateMilliseconds}),
		range : new Array (${params.calendarRangeStart}, ${params.calendarRangeEnd}),
		align : "Tr",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y"
	});
	Calendar.setup ({
		inputField : "dateTo",
		button : "dateToCalendarTrigger",
		date : new Date(${params.calendarDefaultDateMilliseconds}),
		range : new Array (${params.calendarRangeStart}, ${params.calendarRangeEnd}),
		align : "Tr",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y"
	});
	$('#jobId').change(function() {
		var staffMemberId = $('#staffMemberId').val();
		var locationId = $('#locationId').val();
		var jobId = $('#jobId').val();
		var serviceTypeId = $('#serviceTypeId').val();
		var standby = $('#standby').attr('checked');
		var comment = $('#comment').val();
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
		if (standby) {
			url += '&standby=' + standby;
		}
		url += '&comment=' + comment;
		
		url += '&dateFrom=' + dateFrom;	
		url += '&timeFromHours=' + timeFromHours;
		url += '&timeFromMinutes=' + timeFromMinutes;	
		url += '&dateTo=' + dateTo;
		url += '&timeToHours=' + timeToHours;
		url += '&timeToMinutes=' + timeToMinutes;
		
		document.location = url;
	});
});
</script>