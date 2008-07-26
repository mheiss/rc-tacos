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
	<tr>
		<td style="font-weight: bold">Dienstverhältnis:</td>
		<td><select size="1" id="serviceTypeId" name="serviceTypeId">
			<option value="noValue">-- Dienstverhältnis wählen --</option>
			<c:forEach var="serviceType" items="${params.serviceTypeList}">
				<option value="${serviceType.id}" ${(not empty
					params.serviceType) and (params.serviceType.id==
					serviceType.id) ? ' selected="selected"' : ''}>${serviceType.serviceName}</option>
			</c:forEach>
		</select></td>
	</tr>
</table>
<br />
<br />
<c:set var="rosterEntryContainerMap"
	value="${params.adminStatisticContainer.rosterEntryContainerMap}" />
<c:set var="staffMemberRosterMonthStatMap" value="${params.adminStatisticContainer.staffMemberRosterMonthStatMap}" />
<c:choose>
	<c:when test="${fn:length(rosterEntryContainerMap) gt 0}">
		<table class="list" cellpadding="0" cellspacing="0">
			<c:forEach var="rosterEntryContainerMapEntry" items="${rosterEntryContainerMap}">
				<c:set var="staffMember" value="${rosterEntryContainerMapEntry.key}" />
				<c:set var="rosterEntryContainerList" value="${rosterEntryContainerMapEntry.value}" />
					<tr>
						<th class="header2" colspan="10">${staffMember.lastName}&nbsp;${staffMember.firstName}</th>
					</tr>
					<tr class="subhead2">
						<th nowrap="nowrap">Datum</th>
						<th nowrap="nowrap">Ortsstelle</th>
						<th nowrap="nowrap">Verwendung</th>
						<th nowrap="nowrap">von&nbsp;(geplant)</th>
						<th nowrap="nowrap">bis&nbsp;(geplant)</th>
						<th nowrap="nowrap">von</th>
						<th nowrap="nowrap">bis</th>
						<th nowrap="nowrap"></th>
						<th nowrap="nowrap"></th>
						<th nowrap="nowrap">Summe</th>
					</tr>
					<tbody>
						<c:forEach var="rosterEntryContainer" items="${rosterEntryContainerList}">
							<tr>
								<td nowrap="nowrap">
									<fmt:formatDate type="date" dateStyle="short" value="${rosterEntryContainer.plannedStartOfWork}" />
								</td>
								<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.station.locationName}</td>
								<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.job.jobName}</td>
								<td nowrap="nowrap">
									<fmt:formatDate type="time" timeStyle="short" value="${rosterEntryContainer.plannedStartOfWork}" />
								</td>
								<td nowrap="nowrap">
									<c:choose>
										<c:when test="${(rosterEntryContainer.plannedEndOfWorkDayOfYear ne rosterEntryContainer.plannedStartOfWorkDayOfYear or rosterEntryContainer.plannedEndOfWorkMonth ne rosterEntryContainer.plannedStartOfWorkMonth or rosterEntryContainer.plannedEndOfWorkYear ne rosterEntryContainer.plannedStartOfWorkYear) and (rosterEntryContainer.plannedEndOfWorkDayOfYear ne -1 and rosterEntryContainer.plannedEndOfWorkMonth ne -1 and rosterEntryContainer.plannedEndOfWorkYear ne -1)}">
											<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${rosterEntryContainer.plannedEndOfWork}" />
										</c:when>
										<c:otherwise>
											<fmt:formatDate type="time" timeStyle="short" value="${rosterEntryContainer.plannedEndOfWork}" />
										</c:otherwise>
									</c:choose>
								</td>
								<td nowrap="nowrap">
									<c:if test="${rosterEntryContainer.realStartOfWork ne null}">
										<c:choose>
											<c:when test="${(rosterEntryContainer.realStartOfWorkDayOfYear ne rosterEntryContainer.plannedStartOfWorkDayOfYear or rosterEntryContainer.realStartOfWorkMonth ne rosterEntryContainer.plannedStartOfWorkMonth or rosterEntryContainer.realStartOfWorkYear ne rosterEntryContainer.plannedStartOfWorkYear) and (rosterEntryContainer.realStartOfWorkDayOfYear ne -1 and rosterEntryContainer.realStartOfWorkMonth ne -1 and rosterEntryContainer.realStartOfWorkYear ne -1)}">
												<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${rosterEntryContainer.realStartOfWork}" />
											</c:when>
											<c:otherwise>
												<fmt:formatDate type="time" timeStyle="short" value="${rosterEntryContainer.realStartOfWork}" />
											</c:otherwise>
										</c:choose>
									</c:if>
								</td>
								<td nowrap="nowrap">
									<c:if test="${rosterEntryContainer.realEndOfWork ne null}">
										<c:choose>
											<c:when test="${(rosterEntryContainer.realEndOfWorkDayOfYear ne rosterEntryContainer.plannedStartOfWorkDayOfYear or rosterEntryContainer.realEndOfWorkMonth ne rosterEntryContainer.plannedStartOfWorkMonth or rosterEntryContainer.realEndOfWorkYear ne rosterEntryContainer.plannedStartOfWorkYear) and (rosterEntryContainer.realEndOfWorkDayOfYear ne -1 and rosterEntryContainer.realEndOfWorkMonth ne -1 and rosterEntryContainer.realEndOfWorkYear ne -1)}">
												<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${rosterEntryContainer.realEndOfWork}" />
											</c:when>
											<c:otherwise>
												<fmt:formatDate type="time" timeStyle="short" value="${rosterEntryContainer.realEndOfWork}" />
											</c:otherwise>
										</c:choose>
									</c:if>
								</td>
								<td nowrap="nowrap">
									<c:choose>
										<c:when test="${rosterEntryContainer.rosterEntry.standby eq true}">
											<c:url var="url" value="/image/yes.png" />
											<img src="${url}" alt="B" />
										</c:when>
										<c:otherwise>&nbsp;</c:otherwise>
									</c:choose>
								</td>
								<td nowrap="nowrap"><c:if test="${rosterEntryContainer.rosterEntry.standby eq true}">${rosterEntryContainer.durationForStatisticHours}h&nbsp;${rosterEntryContainer.durationForStatisticMinutes}min</c:if></td>
								<td nowrap="nowrap">${rosterEntryContainer.durationForStatisticWeightedHours}h&nbsp;${rosterEntryContainer.durationForStatisticWeightedMinutes}min</td>
							</tr>
						</c:forEach>
						<c:forEach var="staffMemberRosterMonthStatMapEntry" items="${staffMemberRosterMonthStatMap}">
							<c:set var="staffMember2" value="${staffMemberRosterMonthStatMapEntry.key}" />
							<c:if test="${staffMember.staffMemberId eq staffMember2.staffMemberId}">
								<c:set var="rosterMonthStat" value="${staffMemberRosterMonthStatMapEntry.value}" />
							</c:if>
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
							<td><b>Summe:</b>&nbsp;${rosterMonthStat.durationForStatisticWeightedHours}h&nbsp;${rosterMonthStat.durationForStatisticWeightedMinutes}min</td>
						</tr>
					</tbody>
				<c:remove var="rosterMonthStat" />
			</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
		<table cellpadding="3" cellspacing="0" class="list">
			<tr>
				<td nowrap="nowrap" class="nodata">Keine&nbsp;Treffer</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose>
<br />
<br />
<c:url var="url" value="/Dispatcher/printAdminStatistic.do">
	<c:param name="locationId">${params.location.id}</c:param>
	<c:param name="locationStaffMemberId">${params.locationStaffMember.id}</c:param>
	<c:param name="staffMemberId">${params.staffMember.staffMemberId}</c:param>
	<c:param name="month">${params.month}</c:param>
	<c:param name="year">${params.year}</c:param>
</c:url>
<a href="${url}">Dienstplan&nbsp;drucken</a>
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
	$('#serviceTypeId').change(function() {
		var url = '?serviceTypeId=' + $(this).val();
		update(url, 'st');
	});
	function update(url, code) {
		var locationId = $('#locationId').val();
		var locationStaffMemberId = $('#locationStaffMemberId').val();
		var staffMemberId = $('#staffMemberId').val();
		var month = $('#month').val();
		var year = $('#year').val();
		var serviceTypeId = $('#serviceTypeId').val();
		if (code == 'l') {
			url = url + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&year=' + year + '&serviceTypeId=' + serviceTypeId;
		} else if (code == 'lsm') {
			url = url + '&locationId=' + locationId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&year=' + year + '&serviceTypeId=' + serviceTypeId;
		} else if (code == 's') {
			url = url + '&locationId=' + locationId + '&locationStaffMemberId=' + locationStaffMemberId + '&month=' + month + '&year=' + year + '&serviceTypeId=' + serviceTypeId;
		} else if (code == 'm') {
			url = url + '&locationId=' + locationId + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&year=' + year + '&serviceTypeId=' + serviceTypeId;
		} else if (code == 'y') {
			url = url + '&locationId=' + locationId + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&serviceTypeId=' + serviceTypeId;
		} else if (code == 'st') {
			url = url + '&locationId=' + locationId + '&locationStaffMemberId=' + locationStaffMemberId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&year=' + year;
		}
		document.location = url;
	}
});
</script>