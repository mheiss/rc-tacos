<%@ include file="includes.jsp"%>
<c:set var="rosterEntryContainerMap"
	value="${params.rosterMonthContainer.rosterEntryContainerMap}" />
<c:set var="dayList" value="${params.rosterMonthContainer.dayList}" />
<c:set var="staffMemberList"
	value="${params.rosterMonthContainer.staffMemberList}" />
<c:choose>
	<c:when test="${fn:length(staffMemberList) gt 0}">
		<table id="rosterEntryTable" class="list" cellpadding="0"
			cellspacing="0">
				<tr>
					<th class="header2" colspan="${fn:length(staffMemberList)*2+2}">${function.function.competenceName}&nbsp;im
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
					</c:choose> ${params.year}&nbsp;in&nbsp;${params.location.locationName}
					</th>
				</tr>
				<tr class="subhead2">
					<th nowrap="nowrap">&nbsp;</th>
					<th nowrap="nowrap">&nbsp;</th>
					<c:forEach var="staffMemberIterator" items="${staffMemberList}">
						<th nowrap="nowrap" colspan="2">${staffMemberIterator.lastName}&nbsp;${staffMemberIterator.firstName}</th>
					</c:forEach>
				</tr>
				<c:forEach var="day" items="${dayList}">
					<tr>
						<td>${day.day}</td>
						<td><c:choose>
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
									<td>
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
												<td><c:choose>
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
									<td>
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
												<td><span><fmt:formatDate type="time"
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
								</c:when>
								<c:otherwise>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</c:otherwise>
							</c:choose>
							<c:remove var="rosterEntryContainerList" />
						</c:forEach>
					</tr>
					<c:remove var="dayRosterEntryContainerMap" />
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
<br />
<br />
<c:url var="url" value="/Dispatcher/rosterMonth.do" />
<a href="${url}">Zur&uuml;ck</a>
<script type="text/javascript">
$(document).ready(function() {
	$('#rosterEntryTable .showJobName').Tooltip({ delay: 100, showURL: false });
});
</script>