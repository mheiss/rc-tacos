<%@ include file="includes.jsp"%>
<table class="standardForm">
	<tr>
		<td style="font-weight: bold">Funktion:</td>
		<td><select size="1" id="functionId" name="functionId">
			<option value="noValue">-- Funktion wählen --</option>
			<c:forEach var="function" items="${params.functionList}">
				<option value="${function.id}" ${(not empty
					params.function) and (params.function.id==
					function.id) ? ' selected="selected"' : ''}>${function.competenceName}</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td style="font-weight: bold">Ortsstelle&nbsp;(Mitarbeiter):</td>
		<td><select size="1" id="locationStaffMemberId" name="locationStaffMemberId">
			<option value="noValue">-- Ortsstelle (Mitarbeiter) wählen --</option>
			<c:forEach var="locationStaffMember" items="${params.locationStaffMemberList}">
				<option value="${locationStaffMember.id}" ${(not empty
					params.locationStaffMember) and (params.locationStaffMember.id==
					locationStaffMember.id) ? ' selected="selected"' : ''}>${locationStaffMember.locationName}</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td style="font-weight: bold">Mitarbeiter:</td>
		<td><select size="1" id="staffMemberId" name="staffMemberId">
			<option value="noValue">-- Mitarbeiter wählen--</option>
			<c:forEach var="staffMember" items="${params.staffList}">
				<option value="${staffMember.staffMemberId}" ${(not empty
					params.staffMember) and (params.staffMember.staffMemberId==
					staffMember.staffMemberId) ? ' selected="selected"' : ''}>
				${staffMember.lastName}&nbsp;${staffMember.firstName}</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td style="font-weight: bold">Ortsstelle&nbsp;(Dienste):</td>
		<td><select size="1" id="locationId" name="locationId">
			<c:forEach var="location" items="${params.locationList}">
				<option value="${location.id}" ${(not empty
					params.location) and (params.location.id==
					location.id) ? ' selected="selected"' : ''}>${location.locationName}</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td style="font-weight: bold">Monat:</td>
		<td><select size="1" id="month" name="month">
			<option value="JANUARY" ${(not empty params.month) and (params.month == 'JANNUARY') ? ' selected="selected"' : ''}>Jänner</option>
			<option value="FEBRUARY" ${(not empty params.month) and (params.month == 'FEBRUARY') ? ' selected="selected"' : ''}>Februar</option>
			<option value="MARCH" ${(not empty params.month) and (params.month == 'MARCH') ? ' selected="selected"' : ''}>März</option>
			<option value="APRIL" ${(not empty params.month) and (params.month == 'APRIL') ? ' selected="selected"' : ''}>April</option>
			<option value="MAY" ${(not empty params.month) and (params.month == 'MAY') ? ' selected="selected"' : ''}>Mai</option>
			<option value="JUNE" ${(not empty params.month) and (params.month == 'JUNE') ? ' selected="selected"' : ''}>Juni</option>
			<option value="JULY" ${(not empty params.month) and (params.month == 'JULY') ? ' selected="selected"' : ''}>Juli</option>
			<option value="AUGUST" ${(not empty params.month) and (params.month == 'AUGUST') ? ' selected="selected"' : ''}>August</option>
			<option value="SEPTEMBER" ${(not empty params.month) and (params.month == 'SEPTEMBER') ? ' selected="selected"' : ''}>September</option>
			<option value="OCTOBER" ${(not empty params.month) and (params.month == 'OCTOBER') ? ' selected="selected"' : ''}>Oktober</option>
			<option value="NOVEMBER" ${(not empty params.month) and (params.month == 'NOVEMBER') ? ' selected="selected"' : ''}>November</option>
			<option value="DECEMBER" ${(not empty params.month) and (params.month == 'DECEMBER') ? ' selected="selected"' : ''}>Dezember</option>
		</select></td>
	</tr>
	<tr>
		<td style="font-weight: bold">Jahr:</td>
		<td><select size="1" id="year" name="year">
			<c:forEach var="year" items="${params.yearList}">
				<option value="${year}" ${(not empty
					params.year) and (params.year==
					year) ? ' selected="selected"' : ''}>${year}</option>
			</c:forEach>
		</select></td>
	</tr>
</table>
<br />
<br />
<c:set var="rosterEntryContainerMap" value="${params.rosterMonthContainer.rosterEntryContainerMap}"/>
<c:set var="functionList" value="${params.rosterMonthContainer.functionList}" />
<c:set var="dayList" value="${params.rosterMonthContainer.dayList}" />
<c:set var="staffMemberList" value="${params.rosterMonthContainer.staffMemberList}" />
<c:choose>
<c:when test="${fn:length(staffMemberList) gt 0}">
	<table id="rosterEntryTable" class="list" cellpadding="3" cellspacing="0">
		<c:forEach var="function" items="${functionList}">
			<tr>
				<th class="header2" colspan="${fn:length(staffMemberList)*2+2}">${function.function.competenceName}</th>
			</tr>
			<tr class="subhead2">
				<th nowrap="nowrap">&nbsp;</th>
				<th nowrap="nowrap">&nbsp;</th>
				<c:forEach var="staffMember" items="${staffMemberList}">
					<th nowrap="nowrap" colspan="2">${staffMember.lastName}&nbsp;${staffMember.firstName}</th>
				</c:forEach>
			</tr>
			<c:forEach var="day" items="${dayList}">
				<tr>
					<td>${day.day+1}</td>
					<td>
						<c:choose>
							<c:when test="${day.dayOfWeek eq 1}">Mo</c:when>
							<c:when test="${day.dayOfWeek eq 2}">Di</c:when>
							<c:when test="${day.dayOfWeek eq 3}">Mi</c:when>
							<c:when test="${day.dayOfWeek eq 4}">Do</c:when>
							<c:when test="${day.dayOfWeek eq 5}">Fr</c:when>
							<c:when test="${day.dayOfWeek eq 6}">Sa</c:when>
							<c:when test="${day.dayOfWeek eq 7}"><span style="color:red">So</span></c:when>
						</c:choose>
					</td>
					<c:forEach var="functionRosterEntryContainerMapTemp" items="${rosterEntryContainerMap}">
						<c:set var="functionTemp" value="${functionRosterEntryContainerMapTemp.key}" />
						<c:if test="${function.function.id eq functionTemp.function.id}">
							<c:set var="functionRosterEntryContainerMap" value="${functionRosterEntryContainerMapTemp}" />
						</c:if>
					</c:forEach>
					<c:forEach var="dayRosterEntryContainerMapTemp" items="${functionRosterEntryContainerMap.value}">
						<c:set var="dayTemp" value="${dayRosterEntryContainerMapTemp.key}" />
						<c:if test="${day.day eq dayTemp.day}">
							<c:set var="dayRosterEntryContainerMap" value="${dayRosterEntryContainerMapTemp}" />
						</c:if>
					</c:forEach>
				
					<c:forEach var="staffMember" items="${staffMemberList}">
						<c:forEach var="staffMemberRosterEntryContainerMap" items="${dayRosterEntryContainerMap.value}">
							<c:set var="staffMemberTemp" value="${staffMemberRosterEntryContainerMap.key}" />
							<c:if test="${staffMember.staffMemberId eq staffMemberTemp.staffMemberId}">
								<c:set var="rosterEntryContainerList" value="${staffMemberRosterEntryContainerMap.value}" />
							</c:if>
						</c:forEach>
						<c:choose>
							<c:when test="${fn:length(rosterEntryContainerList) gt 0}">
								<td>${staffMember.lastName}&nbsp;${staffMember.firstName}</td><td>&nbsp;</td>
							</c:when>
							<c:otherwise>
								<td>&nbsp;</td><td>&nbsp;</td>
							</c:otherwise>
						</c:choose>
						<c:remove var="rosterEntryContainerList" />
					</c:forEach>
				</tr>
				<c:remove var="functionRosterEntryContainerMap" />
				<c:remove var="dayRosterEntryContainerMap" />
			</c:forEach>		
		</c:forEach>
	</table>
</c:when>
<c:otherwise>
	<table cellpadding="3" cellspacing="0" class="list">
		<tr>
			<td class="nodata">Keine&nbsp;Mitarbeiter&nbsp;mit&nbsp;entsprechenden&nbsp;Kompetenzen&nbsp;gefunden</td>
		</tr>
	</table>
</c:otherwise>
</c:choose>
<script type="text/javascript">
$(document).ready(function() {
	$('#locationId').change(function() {
		var url = '?locationId=' + $(this).val();
		update(url, 'l');
	});
	$('#functionId').change(function() {
		var url = '?functionId=' + $(this).val();
		update(url, 'f');
	});
	$('#locationStaffMemberId').change(function() {
		var url = '?locationStaffMemberId=' + $(this).val();
		update(url, 'lsm');
	});
	$('#staffMemberId').change(function() {
		var url = '?staffMemberId=' + $(this).val();
		update(url, 's');
	});
	$('#month').change(function() {
		var url = '?month=' + $(this).val();
		update(url, 'm');
	});
	$('#year').change(function() {
		var url = '?year=' + $(this).val();
		update(url, 'y');
	});
	function update(url, code) {
		var locationId = $('#locationId').val();
		var functionId = $('#functionId').val();
		var locationStaffMemberId = $('#locationStaffMemberId').val();
		var staffMemberId = $('#staffMemberId').val();
		var month = $('#month').val();
		var year = $('#year').val();
		if (code == 'l') {
			url = url + '&functionId=' + functionId + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&year=' + year;
		} else if (code == 'f') {
			url = url + '&locationId=' + locationId + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&year=' + year;
		} else if (code == 'lsm') {
			url = url + '&locationId=' + locationId + '&functionId=' + functionId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&year=' + year;
		} else if (code == 's') {
			url = url + '&locationId=' + locationId + '&functionId=' + functionId + '&locationStaffMemberId=' + locationStaffMemberId + '&month=' + month + '&year=' + year;
		} else if (code == 'm') {
			url = url + '&locationId=' + locationId + '&functionId=' + functionId + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&year=' + year;
		} else if (code == 'y') {
			url = url + '&locationId=' + locationId + '&functionId=' + functionId + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&month=' + month;
		}
		document.location = url;
	}
});
</script>