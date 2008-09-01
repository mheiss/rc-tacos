<%@ include file="includes.jsp"%>
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
<c:url var="url" value="/Dispatcher/personnelStatistic.do" />
<a href="${url}">Zur&uuml;ck</a>