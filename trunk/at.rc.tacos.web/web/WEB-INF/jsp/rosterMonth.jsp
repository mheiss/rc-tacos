<%@ include file="includes.jsp"%>
<c:choose>
	<c:when test="${params.messageCode eq 'edited'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;Dienstplaneintrag&nbsp;erfolgreich&nbsp;bearbeitet</div>
		<br />
	</c:when>
	<c:when test="${params.messageCode eq 'deleted'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;Dienstplaneintrag&nbsp;erfolgreich&nbsp;gelöscht</div>
		<br />
	</c:when>
</c:choose>
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
		<td style="font-weight: bold">Ortsstelle&nbsp;(Prim&auml;re&nbsp;Ortsstelle&nbsp;des&nbsp;Mitarbeiter):</td>
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
<c:set var="jobList" value="${params.rosterMonthContainer.jobList}" />
<c:set var="serviceTypeList" value="${params.rosterMonthContainer.serviceTypeList}" />
<c:choose>
<c:when test="${fn:length(staffMemberList) gt 0}">
	<table id="rosterEntryTable" class="list" cellpadding="3" cellspacing="0">
		<c:forEach var="function" items="${functionList}">
			<c:choose>
				<c:when test="${function.function.competenceName eq '_LS'}">
					<c:forEach var="jobIterator" items="${jobList}">
						<c:if test="${jobIterator.jobName eq 'Leitstellendisponent'}">
							<c:set var="job" value="${jobIterator}" />
						</c:if>
					</c:forEach>
					<c:forEach var="serviceTypeIterator" items="${serviceTypeList}">
						<c:if test="${serviceTypeIterator.serviceName eq 'Hauptamtlich'}">
							<c:set var="serviceType" value="${serviceTypeIterator}" />
						</c:if>
					</c:forEach>
				</c:when>
				<c:when test="${function.function.competenceName eq '_HA'}">
					<c:forEach var="jobIterator" items="${jobList}">
						<c:if test="${jobIterator.jobName eq 'Fahrer'}">
							<c:set var="job" value="${jobIterator}" />
						</c:if>
					</c:forEach>
					<c:forEach var="serviceTypeIterator" items="${serviceTypeList}">
						<c:if test="${serviceTypeIterator.serviceName eq 'Hauptamtlich'}">
							<c:set var="serviceType" value="${serviceTypeIterator}" />
						</c:if>
					</c:forEach>
				</c:when>
				<c:when test="${function.function.competenceName eq '_ZD'}">
					<c:forEach var="jobIterator" items="${jobList}">
						<c:if test="${jobIterator.jobName eq 'Sanitäter'}">
							<c:set var="job" value="${jobIterator}" />
						</c:if>
					</c:forEach>
					<c:forEach var="serviceTypeIterator" items="${serviceTypeList}">
						<c:if test="${serviceTypeIterator.serviceName eq 'Zivildiener'}">
							<c:set var="serviceType" value="${serviceTypeIterator}" />
						</c:if>
					</c:forEach>
				</c:when>
			</c:choose>
			<tr>
				<th class="header2" colspan="${fn:length(staffMemberList)*3+2}">${function.function.competenceName}</th>
			</tr>
			<tr class="subhead2">
				<th nowrap="nowrap">&nbsp;</th>
				<th nowrap="nowrap">&nbsp;</th>
				<c:forEach var="staffMemberIterator" items="${staffMemberList}">
					<th nowrap="nowrap" colspan="3">${staffMemberIterator.lastName}&nbsp;${staffMemberIterator.firstName}</th>
				</c:forEach>
			</tr>
			<c:forEach var="day" items="${dayList}">
				<tr>
					<td>${day.day}</td>
					<td>
						<c:choose>
							<c:when test="${day.dayOfWeek eq 1}"><span style="color:red">So</span></c:when>
							<c:when test="${day.dayOfWeek eq 2}">Mo</c:when>
							<c:when test="${day.dayOfWeek eq 3}">Di</c:when>
							<c:when test="${day.dayOfWeek eq 4}">Mi</c:when>
							<c:when test="${day.dayOfWeek eq 5}">Do</c:when>
							<c:when test="${day.dayOfWeek eq 6}">Fr</c:when>
							<c:when test="${day.dayOfWeek eq 7}">Sa</c:when>
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
				
					<c:forEach var="staffMemberIterator" items="${staffMemberList}">
						<c:forEach var="staffMemberRosterEntryContainerMap" items="${dayRosterEntryContainerMap.value}">
							<c:set var="staffMemberTemp" value="${staffMemberRosterEntryContainerMap.key}" />
							<c:if test="${staffMemberIterator.staffMemberId eq staffMemberTemp.staffMemberId}">
								<c:set var="rosterEntryContainerList" value="${staffMemberRosterEntryContainerMap.value}" />
							</c:if>
						</c:forEach>
						<c:forEach var="competenceIterator" items="${staffMemberIterator.competenceList}">
							<c:if test="${job.id eq competenceIterator.id or job.jobName eq competenceIterator.competenceName}">
								<c:set var="staffMemberHasCompetence" value="true" />
							</c:if>
						</c:forEach>
						<c:choose>
							<c:when test="${fn:length(rosterEntryContainerList) gt 0}">
								<td>
									<c:forEach var="rosterEntryContainer" items="${rosterEntryContainerList}">
										<c:choose>
											<c:when test="${fn:length(rosterEntryContainer.rosterEntry.job.jobName) gt 6}">
												<c:set var="truncatedTitle">
													<str:truncateNicely lower="4" upper="6">${rosterEntryContainer.rosterEntry.job.jobName}</str:truncateNicely>
												</c:set>
												<span class="showJobName" style="cursor:pointer" title="${rosterEntryContainer.rosterEntry.job.jobName}">${truncatedTitle}</span>
											</c:when>
											<c:otherwise>
												<span>${rosterEntryContainer.rosterEntry.job.jobName}</span>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</td>
								<td>
									<c:forEach var="rosterEntryContainer" items="${rosterEntryContainerList}">
										<span><fmt:formatDate type="time" timeStyle="short" value="${rosterEntryContainer.plannedStartOfWork}" />-<fmt:formatDate type="time" timeStyle="short" value="${rosterEntryContainer.plannedEndOfWork}" /></span><br />
									</c:forEach>
								</td>
								<td nowrap="nowrap">
									<c:forEach var="rosterEntryContainer" items="${rosterEntryContainerList}">
										<c:set var="title">
											<ul>
												<li>Planzeiten:&nbsp;<fmt:formatDate type="time" timeStyle="short" value="${rosterEntryContainer.plannedStartOfWork}" />-<fmt:formatDate type="time" timeStyle="short" value="${rosterEntryContainer.plannedEndOfWork}" /></li>
												<li>Reale Zeiten:&nbsp;<c:if test="${rosterEntryContainer.realStartOfWork ne null}"><fmt:formatDate type="time" timeStyle="short" value="${rosterEntryContainer.realStartOfWork}" /></c:if>-<c:if test="${rosterEntryContainer.realEndOfWork ne null}"><fmt:formatDate type="time" timeStyle="short" value="${rosterEntryContainer.realEndOfWork}" /></c:if></li>
												<li>Dienstverh&auml;ltnis:&nbsp;${rosterEntryContainer.rosterEntry.servicetype.serviceName}</li>
												<li>Bereitschaft:<c:choose><c:when test="${rosterEntryContainer.rosterEntry.standby eq true}">Ja</c:when><c:otherwise>Nein</c:otherwise></c:choose></li>
											</ul>
											<p>${rosterEntryContainer.rosterEntry.rosterNotes}</p>
										</c:set>
										<c:url var="addRosterEntryUrl" value="/Dispatcher/addRosterEntry.do">
											<c:param name="jobId">${job.id}</c:param>
											<c:param name="staffMemberId">${staffMemberIterator.staffMemberId}</c:param>
											<c:param name="locationId">${params.location.id}</c:param>
											<c:param name="serviceTypeId">${serviceType.id}</c:param>
											<c:param name="dateFrom">
												<fmt:formatDate type="date" dateStyle="medium" value="${day.dateOfDay}" />
											</c:param>
											<c:param name="dateTo">
												<fmt:formatDate type ="date" dateStyle="medium" value="${day.dateOfDay}" />
											</c:param>
										</c:url>
										<img class="showRosterEntryInfo" border="0" title="${title}" src="<c:url value="/image/info.gif"/>" />
										<c:if test="${staffMemberHasCompetence ne null}">
											<a href="${addRosterEntryUrl}"><img class="addRosterEntry" border="0" title="Dienst&nbsp;anlegen" src="<c:url value="/image/b_add.gif"/>" /></a>
										</c:if>
										<c:url var="editRosterEntryUrl" value="/Dispatcher/editRosterEntry.do">
											<c:param name="savedUrl">/rosterMonth.do</c:param>
											<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
										</c:url>
										<a href="${editRosterEntryUrl}"><img class="editRosterEntry" border="0" title="Bearbeiten"  src="<c:url value="/image/b_edit.png"/>" /></a>
										<c:url var="deleteRosterEntryUrl" value="/Dispatcher/deleteRosterEntry.do">
											<c:param name="savedUrl">/rosterMonth.do</c:param>
											<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
										</c:url>
										<a href="${deleteRosterEntryUrl}"><img class="deleteRosterEntry" border="0" title="L&ouml;schen" src="<c:url value="/image/b_drop.png"/>" /></a><br />
									</c:forEach>
								</td>
							</c:when>
							<c:otherwise>
								<c:url var="url" value="/Dispatcher/addRosterEntry.do">
									<c:param name="jobId">${job.id}</c:param>
									<c:param name="staffMemberId">${staffMemberIterator.staffMemberId}</c:param>
									<c:param name="locationId">${params.location.id}</c:param>
									<c:param name="serviceTypeId">${serviceType.id}</c:param>
									<c:param name="dateFrom">
										<fmt:formatDate type="date" dateStyle="medium" value="${day.dateOfDay}" />
									</c:param>
									<c:param name="dateTo">
										<fmt:formatDate type ="date" dateStyle="medium" value="${day.dateOfDay}" />
									</c:param>
								</c:url>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>
									<c:choose>
										<c:when test="${staffMemberHasCompetence ne null}">
											<a href="${url}"><img class="addRosterEntry" border="0" title="Dienst&nbsp;anlegen" src="<c:url value="/image/b_add.gif"/>" /></a>
										</c:when>
										<c:otherwise>&nbsp;</c:otherwise>
									</c:choose>
								</td>
							</c:otherwise>
						</c:choose>
						<c:remove var="staffMemberHasCompetence" />
						<c:remove var="rosterEntryContainerList" />
					</c:forEach>
				</tr>
				<c:remove var="functionRosterEntryContainerMap" />
				<c:remove var="dayRosterEntryContainerMap" />
			</c:forEach>
			<c:remove var="job"	/>
			<c:remove var="serviceType" />
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
	$('#rosterEntryTable .showJobName').Tooltip({ delay: 100, showURL: false });
	$('#rosterEntryTable .showRosterEntryInfo').Tooltip({ delay: 100, showURL: false });
	$('#rosterEntryTable .addRosterEntry').Tooltip({ delay: 100, showURL: false });
	$('#rosterEntryTable .editRosterEntry').Tooltip({ delay: 100, showURL: false });
	$('#rosterEntryTable .deleteRosterEntry').Tooltip({ delay: 100, showURL: false });
});
</script>