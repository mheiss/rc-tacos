<%@ include file="includes.jsp"%>
<table class="standardForm">
	<tr>
		<td style="font-weight: bold">Ortsstelle&nbsp;(Prim&auml;re&nbsp;Ortsstelle&nbsp;des&nbsp;Mitarbeiter):</td>
		<td><select size="1" id="locationStaffMemberId"
			name="locationStaffMemberId">
			<option value="noValue">-- Ortsstelle (Mitarbeiter) wählen
			--</option>
			<c:forEach var="locationStaffMember"
				items="${params.locationStaffMemberList}">
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
			<option value="noValue">-- Ortsstelle wählen --</option>
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
			<option value="JANUARY" ${(not empty
				params.month) and (params.month== 'JANUARY') ? ' selected="selected"' : ''}>Jänner</option>
			<option value="FEBRUARY" ${(not empty
				params.month) and (params.month==
				'FEBRUARY') ? ' selected="selected"' : ''}>Februar</option>
			<option value="MARCH" ${(not empty params.month) and (params.month==
				'MARCH') ? ' selected="selected"' : ''}>März</option>
			<option value="APRIL" ${(not empty params.month) and (params.month==
				'APRIL') ? ' selected="selected"' : ''}>April</option>
			<option value="MAY" ${(not empty params.month) and (params.month==
				'MAY') ? ' selected="selected"' : ''}>Mai</option>
			<option value="JUNE" ${(not empty params.month) and (params.month==
				'JUNE') ? ' selected="selected"' : ''}>Juni</option>
			<option value="JULY" ${(not empty params.month) and (params.month==
				'JULY') ? ' selected="selected"' : ''}>Juli</option>
			<option value="AUGUST" ${(not empty params.month) and (params.month==
				'AUGUST') ? ' selected="selected"' : ''}>August</option>
			<option value="SEPTEMBER" ${(not empty
				params.month) and (params.month==
				'SEPTEMBER') ? ' selected="selected"' : ''}>September</option>
			<option value="OCTOBER" ${(not empty
				params.month) and (params.month== 'OCTOBER') ? ' selected="selected"' : ''}>Oktober</option>
			<option value="NOVEMBER" ${(not empty
				params.month) and (params.month==
				'NOVEMBER') ? ' selected="selected"' : ''}>November</option>
			<option value="DECEMBER" ${(not empty
				params.month) and (params.month==
				'DECEMBER') ? ' selected="selected"' : ''}>Dezember</option>
		</select></td>
	</tr>
	<tr>
		<td style="font-weight: bold">Jahr:</td>
		<td><select size="1" id="year" name="year">
			<c:forEach var="year" items="${params.yearList}">
				<option value="${year}" ${(not empty params.year) and (params.year==
					year) ? ' selected="selected"' : ''}>${year}</option>
			</c:forEach>
		</select></td>
	</tr>
</table>
<br />
<br />
<c:set var="rosterEntryContainerMap"
	value="${params.adminStatisticContainer.rosterEntryContainerMap}" />
<c:set var="jobList" value="${params.adminStatisticContainer.jobList}" />
<c:set var="staffMemberRosterMonthStatMap" value="${params.adminStatisticContainer.staffMemberRosterMonthStatMap}" />
<c:choose>
	<c:when test="${fn:length(staffMemberList) gt 0}">
		<c:forEach var="rosterEntryContainerMapEntry" items="${rosterEntryContainerMap}">
			<c:set var="staffMember" value="${rosterEntryContainerMapEntry.key}" />
			<c:set var="rosterEntryContainerList" value="${rosterEntryContainerMapEntry.value}" />
			<table class="list" cellpadding="0" cellspacing="0">
				<tr>
					<th class="header2" colspan="11"><c:choose>
							<c:when test="${params.month eq 'JANUARY'}">J&auml;nner</c:when>
							<c:when test="${params.month eq 'FEBRUARY'}">Februar</c:when>
							<c:when test="${params.month eq 'MARCH'}">M&auml;rz</c:when>
							<c:when test="${params.month eq 'APRIL'}">April</c:when>
							<c:when test="${params.month eq 'MAY'}">Mai</c:when>
							<c:when test="${params.month eq 'JUNE'}">Juni</c:when>
							<c:when test="${params.month eq 'JULY'}">Juli</c:when>
							<c:when test="${params.month eq 'AUGUST'}">August</c:when>
							<c:when test="${params.month eq 'SEPTEMBER'}">September</c:when>
							<c:when test="${params.month eq 'OCTOBER'}">Oktober</c:when>
							<c:when test="${params.month eq 'NOVEMBER'}">November</c:when>
							<c:when test="${params.month eq 'DECEMBER'}">Dezember</c:when>
						</c:choose>${params.year}
						<c:if test="${params.location ne null}">
							in&nbsp;${params.location.locationName}&nbsp;${staffMember.lastName}&nbsp;${staffMember.firstName}
						</c:if>
					</th>
				</tr>
				<tr class="subhead2">
					<th nowrap="nowrap">Datum</th>
					<th nowrap="nowrap">Ortsstelle</th>
					<th nowrap="nowrap">Verwendung</th>
					<th nowrap="nowrap">&nbsp;</th>
					<th nowrap="nowrap">Zeit&nbsp;von&nbsp;(geplant)</th>
					<th nowrap="nowrap">Zeit&nbsp;bis&nbsp;(geplant)</th>
					<th nowrap="nowrap">Zeit&nbsp;von&nbsp;(real)</th>
					<th nowrap="nowrap">Zeit&nbsp;bis&nbsp;(real)</th>
					<th nowrap="nowrap">Summe&nbsp;(geplant)</th>
					<th nowrap="nowrap">Summe&nbsp;(real)</th>
					<th nowrap="nowrap">Summe&nbsp;(real, gewichtet)</th>
				</tr>
				<tbody>
					<c:forEach var="rosterEntryContainer" items="${rosterEntryContainerList}">
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</c:forEach>
					<tr>
						<td />
						<td />
						<td />
						<td />
						<td />
						<td />
						<td />
						<td />
						<td />
						<td />
						<td>Summe</td>
					</tr>
				</tbody>
			</table>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<table cellpadding="3" cellspacing="0" class="list">
			<tr>
				<td nowrap="nowrap" class="nodata">Keine&nbsp;Treffer</td>
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
		var locationStaffMemberId = $('#locationStaffMemberId').val();
		var staffMemberId = $('#staffMemberId').val();
		var month = $('#month').val();
		var year = $('#year').val();
		if (code == 'l') {
			url = url + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&year=' + year;
		} else if (code == 'lsm') {
			url = url + '&locationId=' + locationId + '&functionId=' + functionId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&year=' + year;
		} else if (code == 's') {
			url = url + '&locationId=' + locationId + '&locationStaffMemberId=' + locationStaffMemberId + '&month=' + month + '&year=' + year;
		} else if (code == 'm') {
			url = url + '&locationId=' + locationId + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&year=' + year;
		} else if (code == 'y') {
			url = url + '&locationId=' + locationId + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&month=' + month;
		}
		document.location = url;
	}
});
</script>