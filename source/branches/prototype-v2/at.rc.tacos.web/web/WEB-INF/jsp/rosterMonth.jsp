<%@ include file="includes.jsp"%>
<c:choose>
	<c:when test="${params.messageCode eq 'edited'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;Dienstplaneintrag&nbsp;erfolgreich&nbsp;bearbeitet</div>
		<br />
	</c:when>
	<c:when test="${params.messageCode eq 'deleted'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;Dienstplaneintrag&nbsp;erfolgreich&nbsp;gel&ouml;scht</div>
		<br />
	</c:when>
</c:choose>
<table class="standardForm">
	<tr>
		<td style="font-weight: bold">Funktion:</td>
		<td><select size="1" id="functionId" name="functionId">
			<c:forEach var="function" items="${params.functionList}">
				<option value="${function.id}" ${(not empty
					params.function) and (params.function.id==
					function.id) ? ' selected="selected"' : ''}>${function.competenceName}</option>
			</c:forEach>
		</select></td>
	</tr>
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
	value="${params.rosterMonthContainer.rosterEntryContainerMap}" />
<c:set var="dayList" value="${params.rosterMonthContainer.dayList}" />
<c:set var="staffMemberList"
	value="${params.rosterMonthContainer.staffMemberList}" />
<c:set var="jobList" value="${params.rosterMonthContainer.jobList}" />
<c:set var="serviceTypeList"
	value="${params.rosterMonthContainer.serviceTypeList}" />
<c:set var="staffMemberRosterMonthStatMap" value="${params.rosterMonthContainer.staffMemberRosterMonthStatMap}" />
<c:choose>
	<c:when test="${fn:length(staffMemberList) gt 0}">
		<table id="rosterEntryTable" class="list" cellpadding="0"
			cellspacing="0">
				<c:choose>
					<c:when test="${params.function.competenceName eq '_LS'}">
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
					<c:when test="${params.function.competenceName eq '_HA'}">
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
					<c:when test="${params.function.competenceName eq '_ZD'}">
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
					<th class="header2" colspan="${fn:length(staffMemberList)*3+2}">${params.function.competenceName}&nbsp;im
						<c:choose>
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
							in&nbsp;${params.location.locationName}
						</c:if>
					</th>
				</tr>
				<tr class="subhead2">
					<th nowrap="nowrap">&nbsp;</th>
					<th nowrap="nowrap">&nbsp;</th>
					<c:forEach var="staffMemberIterator" items="${staffMemberList}">
						<th nowrap="nowrap" colspan="3">${staffMemberIterator.lastName}&nbsp;${staffMemberIterator.firstName}</th>
					</c:forEach>
				</tr>
				<tbody>
					<c:forEach var="day" items="${dayList}">
						<tr>
							<td nowrap="nowrap">${day.day}</td>
							<td nowrap="nowrap"><c:choose>
								<c:when test="${day.dayOfWeek eq 1}">
									<span style="color: red">So</span>
								</c:when>
								<c:when test="${day.dayOfWeek eq 2}">Mo</c:when>
								<c:when test="${day.dayOfWeek eq 3}">Di</c:when>
								<c:when test="${day.dayOfWeek eq 4}">Mi</c:when>
								<c:when test="${day.dayOfWeek eq 5}">Do</c:when>
								<c:when test="${day.dayOfWeek eq 6}">Fr</c:when>
								<c:when test="${day.dayOfWeek eq 7}">Sa</c:when>
							</c:choose></td>
							<c:forEach var="dayRosterEntryContainerMapTemp"
								items="${rosterEntryContainerMap}">
								<c:set var="dayTemp"
									value="${dayRosterEntryContainerMapTemp.key}" />
								<c:if test="${day.day eq dayTemp.day}">
									<c:set var="dayRosterEntryContainerMap"
										value="${dayRosterEntryContainerMapTemp}" />
								</c:if>
							</c:forEach>
	
							<c:forEach var="staffMemberIterator" items="${staffMemberList}">
								<c:forEach var="staffMemberRosterEntryContainerMap"
									items="${dayRosterEntryContainerMap.value}">
									<c:set var="staffMemberTemp"
										value="${staffMemberRosterEntryContainerMap.key}" />
									<c:if
										test="${staffMemberIterator.staffMemberId eq staffMemberTemp.staffMemberId}">
										<c:set var="rosterEntryContainerList"
											value="${staffMemberRosterEntryContainerMap.value}" />
									</c:if>
								</c:forEach>
								<c:choose>
									<c:when test="${fn:length(rosterEntryContainerList) gt 0}">
										<td nowrap="nowrap">
										<table class="innerTable" cellspacing="0" cellpadding="0">
											<c:forEach var="rosterEntryContainer"
												items="${rosterEntryContainerList}">
												<c:choose>
													<c:when test="${rosterEntryContainer.rosterEntry.rosterNotes eq 'Urlaub'}">
														<c:set var="class">holiday</c:set>
													</c:when>
													<c:when test="${rosterEntryContainer.rosterEntry.job.jobName eq 'Leitstellendisponent'}">
														<c:set var="class">controlOperator</c:set>
													</c:when>
													<c:when test="${rosterEntryContainer.rosterEntry.job.jobName eq 'Notfallsanitäter'}">
														<c:set var="class">emergencyMedic</c:set>
													</c:when>
												</c:choose>
												<tr class="${class}">
													<td nowrap="nowrap"><c:choose>
													<c:when test="${rosterEntryContainer.rosterEntry.rosterNotes eq 'Urlaub'}">
														<c:choose>
															<c:when
																test="${fn:length(rosterEntryContainer.rosterEntry.rosterNotes) gt 6}">
																<c:set var="truncatedTitle">
																	<str:truncateNicely lower="4" upper="6">${rosterEntryContainer.rosterEntry.rosterNotes}</str:truncateNicely>
																</c:set>
																<span class="showJobName" style="cursor:pointer"
																	title="${rosterEntryContainer.rosterEntry.rosterNotes}">${truncatedTitle}</span>
															</c:when>
															<c:otherwise>
																<span>${rosterEntryContainer.rosterEntry.rosterNotes}</span>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when
																test="${fn:length(rosterEntryContainer.rosterEntry.job.jobName) gt 6}">
																<c:set var="truncatedTitle">
																	<str:truncateNicely lower="4" upper="6">${rosterEntryContainer.rosterEntry.job.jobName}</str:truncateNicely>
																</c:set>
																<span class="showJobName" style="cursor:pointer"
																	title="${rosterEntryContainer.rosterEntry.job.jobName}">${truncatedTitle}</span>
															</c:when>
															<c:otherwise>
																<span>${rosterEntryContainer.rosterEntry.job.jobName}</span>
															</c:otherwise>
														</c:choose>
													</c:otherwise>
													</c:choose></td>
												</tr>
												<c:remove var="class" />
											</c:forEach>
										</table>
										</td>
										<td nowrap="nowrap">
										<table class="innerTable" cellspacing="0" cellpadding="0">
											<c:forEach var="rosterEntryContainer"
												items="${rosterEntryContainerList}">
												<c:choose>
													<c:when test="${rosterEntryContainer.rosterEntry.rosterNotes eq 'Urlaub'}">
														<c:set var="class">holiday</c:set>
													</c:when>
													<c:when test="${rosterEntryContainer.rosterEntry.job.jobName eq 'Notfallsanitäter'}">
														<c:set var="class">emergencyMedic</c:set>
													</c:when>
													<c:when test="${rosterEntryContainer.rosterEntry.job.jobName eq 'Leitstellendisponent'}">
														<c:set var="class">controlOperator</c:set>
													</c:when>
												</c:choose>
												<tr class="${class}">
													<td nowrap="nowrap"><span><fmt:formatDate type="time"
														timeStyle="short"
														value="${rosterEntryContainer.plannedStartOfWork}" />-<fmt:formatDate
														type="time" timeStyle="short"
														value="${rosterEntryContainer.plannedEndOfWork}" /></span> <br />
													</td>
												</tr>
												<c:remove var="class" />
											</c:forEach>
										</table>
										</td>
										<td nowrap="nowrap">
										<table class="innerTable" cellspacing="0" cellpadding="0">
											<c:forEach var="rosterEntryContainer"
												items="${rosterEntryContainerList}">
												<c:choose>
													<c:when test="${rosterEntryContainer.rosterEntry.rosterNotes eq 'Urlaub'}">
														<c:set var="class">holiday</c:set>
													</c:when>
													<c:when test="${rosterEntryContainer.rosterEntry.job.jobName eq 'Notfallsanitäter'}">
														<c:set var="class">emergencyMedic</c:set>
													</c:when>
													<c:when test="${rosterEntryContainer.rosterEntry.job.jobName eq 'Leitstellendisponent'}">
														<c:set var="class">controlOperator</c:set>
													</c:when>
												</c:choose>
												<tr class="${class}">
													<td nowrap="nowrap"><c:set var="title">
														<ul>
															<li>Ortsstelle:&nbsp;${rosterEntryContainer.rosterEntry.station.locationName}</li>
															<li>Planzeiten:&nbsp;<fmt:formatDate type="time"
																timeStyle="short"
																value="${rosterEntryContainer.plannedStartOfWork}" />-<fmt:formatDate
																type="time" timeStyle="short"
																value="${rosterEntryContainer.plannedEndOfWork}" /></li>
															<li>Reale Zeiten:&nbsp;<c:if
																test="${rosterEntryContainer.realStartOfWork ne null}">
																<fmt:formatDate type="time" timeStyle="short"
																	value="${rosterEntryContainer.realStartOfWork}" />
															</c:if>-<c:if
																test="${rosterEntryContainer.realEndOfWork ne null}">
																<fmt:formatDate type="time" timeStyle="short"
																	value="${rosterEntryContainer.realEndOfWork}" />
															</c:if></li>
															<li>Dienstverh&auml;ltnis:&nbsp;${rosterEntryContainer.rosterEntry.servicetype.serviceName}</li>
															<li>Bereitschaft:<c:choose>
																<c:when
																	test="${rosterEntryContainer.rosterEntry.standby eq true}">Ja</c:when>
																<c:otherwise>Nein</c:otherwise>
															</c:choose></li>
														</ul>
														<p>${rosterEntryContainer.rosterEntry.rosterNotes}</p>
													</c:set> <c:url var="addRosterEntryUrl"
														value="/Dispatcher/addRosterEntry.do">
														<c:param name="jobId">${job.id}</c:param>
														<c:param name="staffMemberId">${staffMemberIterator.staffMemberId}</c:param>
														<c:param name="locationId">${params.location.id}</c:param>
														<c:param name="serviceTypeId">${serviceType.id}</c:param>
														<c:param name="dateFrom">
															<fmt:formatDate type="date" dateStyle="medium"
																value="${day.dateOfDay}" />
														</c:param>
														<c:param name="dateTo">
															<fmt:formatDate type="date" dateStyle="medium"
																value="${day.dateOfDay}" />
														</c:param>
													</c:url> <img class="showRosterEntryInfo" border="0"
														title="${title}" src="<c:url value="/image/info.gif"/>" />
	
													<a href="${addRosterEntryUrl}"><img
														class="addRosterEntry" border="0"
														title="Dienst&nbsp;anlegen"
														src="<c:url value="/image/b_add.gif"/>" /></a> <c:url
														var="editRosterEntryUrl"
														value="/Dispatcher/editRosterEntry.do">
														<c:param name="savedUrl">/rosterMonth.do</c:param>
														<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
													</c:url> <a href="${editRosterEntryUrl}"><img
														class="editRosterEntry" border="0" title="Bearbeiten"
														src="<c:url value="/image/b_edit.png"/>" /></a> <c:url
														var="deleteRosterEntryUrl"
														value="/Dispatcher/deleteRosterEntry.do">
														<c:param name="savedUrl">/rosterMonth.do</c:param>
														<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
													</c:url> <a href="${deleteRosterEntryUrl}"><img
														class="deleteRosterEntry" border="0" title="L&ouml;schen"
														src="<c:url value="/image/b_drop.png"/>" /></a></td>
												</tr>
												<c:remove var="class" /> 
											</c:forEach>
										</table>
										</td>
									</c:when>
									<c:otherwise>
										<c:url var="url" value="/Dispatcher/addRosterEntry.do">
											<c:param name="jobId">${job.id}</c:param>
											<c:param name="staffMemberId">${staffMemberIterator.staffMemberId}</c:param>
											<c:param name="locationId">${params.location.id}</c:param>
											<c:param name="serviceTypeId">${serviceType.id}</c:param>
											<c:param name="dateFrom">
												<fmt:formatDate type="date" dateStyle="medium"
													value="${day.dateOfDay}" />
											</c:param>
											<c:param name="dateTo">
												<fmt:formatDate type="date" dateStyle="medium"
													value="${day.dateOfDay}" />
											</c:param>
										</c:url>
										<td nowrap="nowrap">&nbsp;</td>
										<td nowrap="nowrap">&nbsp;</td>
										<td nowrap="nowrap"><a href="${url}"><img class="addRosterEntry"
											border="0" title="Dienst&nbsp;anlegen"
											src="<c:url value="/image/b_add.gif"/>" /></a></td>
									</c:otherwise>
								</c:choose>
								<c:remove var="rosterEntryContainerList" />
							</c:forEach>
						</tr>
						<c:remove var="dayRosterEntryContainerMap" />
					</c:forEach>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<c:forEach var="staffMemberIterator" items="${staffMemberList}">
							<c:forEach var="staffMemberRosterMonthStatMapEntry" items="${staffMemberRosterMonthStatMap}">
								<c:set var="staffMemberTemp" value="${staffMemberRosterMonthStatMapEntry.key}" />
								<c:if test="${staffMemberIterator.staffMemberId eq staffMemberTemp.staffMemberId}">
									<c:set var="rosterMonthStat" value="${staffMemberRosterMonthStatMapEntry.value}" />
								</c:if>
							</c:forEach>
							<c:choose>
								<c:when test="${rosterMonthStat eq null}">
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</c:when>
								<c:otherwise>
									<td>&nbsp;</td>
									<td><b>Gesamt:</b>&nbsp;${rosterMonthStat.plannedDurationHours}h&nbsp;${rosterMonthStat.plannedDurationMinutes}min</td>
									<td>&nbsp;</td>
								</c:otherwise>
							</c:choose>
							<c:remove var="rosterMonthStat" />
						</c:forEach>
					</tr>
				</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<table cellpadding="3" cellspacing="0" class="list">
			<tr>
				<td nowrap="nowrap" class="nodata">Keine&nbsp;Mitarbeiter&nbsp;mit&nbsp;entsprechenden&nbsp;Kompetenzen&nbsp;gefunden</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose>
<br />
<br />
<c:url var="url" value="/Dispatcher/printRosterMonth.do">
	<c:param name="functionId">${params.function.id}</c:param>
	<c:param name="locationStaffMemberId">${params.locationStaffMember.id}</c:param>
	<c:param name="staffMemberId">${params.staffMember.staffMemberId}</c:param>
	<c:param name="locationId">${params.location.id}</c:param>
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