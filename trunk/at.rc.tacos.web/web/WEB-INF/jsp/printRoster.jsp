<%@ include file="includes.jsp"%>
<c:set var="fieldHeadersRow">
	<tr class="subhead2">
		<th nowrap="nowrap">Name</th>
		<th nowrap="nowrap">von&nbsp;(geplant)</th>
		<th nowrap="nowrap">bis&nbsp;(geplant)</th>
		<th nowrap="nowrap">von&nbsp;(real)</th>
		<th nowrap="nowrap">bis&nbsp;(real)</th>
		<th nowrap="nowrap">Verwendung</th>
		<th nowrap="nowrap">Dienstverhältnis</th>
		<th nowrap="nowrap">Bereitschaft</th>
	</tr>
</c:set>
<c:set var="rosterEntryContainerMap"
	value="${params.rosterEntryContainerListContainer.rosterEntryContainerMap}" />
<c:choose>
	<c:when test="${fn:length(rosterEntryContainerMap) gt 0}">
		<table id="rosterEntryTable" class="list" cellpadding="3"
			cellspacing="0">
			<c:forEach var="rosterEntryContainerMapEntry"
				items="${rosterEntryContainerMap}">
				<c:set var="location" value="${rosterEntryContainerMapEntry.key}" />
				<tr>
					<th class="header2" colspan="8">${location.locationName}&nbsp;am&nbsp;<fmt:formatDate
						type="date" dateStyle="medium" value="${params.date}" /></th>
				</tr>
				${fieldHeadersRow}
				<tbody>
					<c:forEach var="rosterEntryContainer"
						items="${rosterEntryContainerMapEntry.value}" varStatus="loop">
						<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
							<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.staffMember.lastName}
							${rosterEntryContainer.rosterEntry.staffMember.firstName}</td>
							<td><c:choose>
								<c:when
									test="${rosterEntryContainer.plannedStartOfWorkDayOfYear ne params.dateDayOfYear or rosterEntryContainer.plannedStartOfWorkMonth ne params.dateMonth or rosterEntryContainer.plannedStartOfWorkYear ne params.dateYear}">
									<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
										value="${rosterEntryContainer.plannedStartOfWork}" />
								</c:when>
								<c:otherwise>
									<fmt:formatDate type="time" timeStyle="short"
										value="${rosterEntryContainer.plannedStartOfWork}" />
								</c:otherwise>
							</c:choose></td>
							<td><c:choose>
								<c:when
									test="${rosterEntryContainer.plannedEndOfWorkDayOfYear ne params.dateDayOfYear or rosterEntryContainer.plannedEndOfWorkMonth ne params.dateMonth or rosterEntryContainer.plannedEndOfWorkYear ne params.dateYear}">
									<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
										value="${rosterEntryContainer.plannedEndOfWork}" />
								</c:when>
								<c:otherwise>
									<fmt:formatDate type="time" timeStyle="short"
										value="${rosterEntryContainer.plannedEndOfWork}" />
								</c:otherwise>
							</c:choose></td>
							<td><c:choose>
								<c:when test="${rosterEntryContainer.realStartOfWork eq null}">-</c:when>
								<c:otherwise>
									<c:choose>
										<c:when
											test="${(rosterEntryContainer.realStartOfWorkDayOfYear ne params.dateDayOfYear or rosterEntryContainer.realStartOfWorkMonth ne params.dateMonth or rosterEntryContainer.realStartOfWorkYear ne params.dateYear) and (rosterEntryContainer.realStartOfWorkDayOfYear ne -1 and rosterEntryContainer.realStartOfWorkMonth ne -1 and rosterEntryContainer.realStartOfWorkYear ne -1)}">
											<fmt:formatDate type="both" dateStyle="short"
												timeStyle="short"
												value="${rosterEntryContainer.realStartOfWork}" />
										</c:when>
										<c:otherwise>
											<fmt:formatDate type="time" timeStyle="short"
												value="${rosterEntryContainer.realStartOfWork}" />
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose></td>
							<td><c:choose>
								<c:when test="${rosterEntryContainer.realEndOfWork eq null}">-</c:when>
								<c:otherwise>
									<c:choose>
										<c:when
											test="${(rosterEntryContainer.realEndOfWorkDayOfYear ne params.dateDayOfYear or rosterEntryContainer.realEndOfWorkMonth ne params.dateMonth or rosterEntryContainer.realEndOfWorkYear ne params.dateYear) and (rosterEntryContainer.realEndOfWorkDayOfYear ne -1 and rosterEntryContainer.realEndOfWorkMonth ne -1 and rosterEntryContainer.realEndOfWorkYear ne -1)}">
											<fmt:formatDate type="both" dateStyle="short"
												timeStyle="short"
												value="${rosterEntryContainer.realEndOfWork}" />
										</c:when>
										<c:otherwise>
											<fmt:formatDate type="time" timeStyle="short"
												value="${rosterEntryContainer.realEndOfWork}" />
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose></td>
							<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.job.jobName}</td>
							<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.servicetype.serviceName}</td>
							<td nowrap="nowrap"><c:choose>
								<c:when
									test="${rosterEntryContainer.rosterEntry.standby eq true}">Ja</c:when>
								<c:otherwise>Nein</c:otherwise>
							</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
		<table cellpadding="3" cellspacing="0" class="list">
			<tr>
				<td class="nodata">Keine&nbsp;Treffer</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose>
<br />
<br />
<c:url var="url" value="/Dispatcher/roster.do" />
<a href="${url}">Zur&uuml;ck</a>