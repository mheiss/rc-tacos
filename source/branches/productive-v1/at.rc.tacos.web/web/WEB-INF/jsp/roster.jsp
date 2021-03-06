<%@ include file="includes.jsp"%>
<c:url var="url" value="/Dispatcher/addRosterEntry.do" />
<c:choose>
	<c:when test="${params.messageCode eq 'edited'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;Dienstplaneintrag&nbsp;erfolgreich&nbsp;bearbeitet</div>
		<br />
	</c:when>
	<c:when test="${params.messageCode eq 'deleted'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;Dienstplaneintrag&nbsp;erfolgreich&nbsp;gel&ouml;scht</div>
		<br />
	</c:when>
	<c:when test="${params.messageCode eq 'registered'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;erfolgreich&nbsp;einen&nbsp;Dienst&nbsp;angemeldet</div>
		<br />
	</c:when>
	<c:when test="${params.messageCode eq 'signedOff'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;erfolgreich&nbsp;einen&nbsp;Dienst&nbsp;abgemeldet</div>
		<br />
	</c:when>
</c:choose>
<table class="standardForm">
	<tr>
		<td style="font-weight: bold">Ortsstelle:</td>
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
		<td style="font-weight: bold">Datum:</td>
		<td><input id="date" name="date" type="text" size="10"
			maxlength="10"
			value="<fmt:formatDate type="date" value="${params.date}"/>" /> <c:url
			var="url" value="/image/calendar_edit.gif" /> <img src="${url}"
			border="0" id="dateCalendarTrigger" style="cursor: pointer" /></td>
	</tr>
</table>
<br />
<br />
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
		<th nowrap="nowrap">&nbsp;</th>
		<th nowrap="nowrap">&nbsp;</th>
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
					<th class="header2" colspan="10">${location.locationName}&nbsp;am&nbsp;<fmt:formatDate
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
							<td nowrap="nowrap">
								<c:choose>
									<c:when test="${rosterEntryContainer.rosterEntry.rosterNotes eq 'Urlaub'}">
										Urlaub
									</c:when>
									<c:otherwise>
										${rosterEntryContainer.rosterEntry.job.jobName}
									</c:otherwise>
								</c:choose>
							</td>
							<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.servicetype.serviceName}</td>
							<td nowrap="nowrap"><c:choose>
								<c:when
									test="${rosterEntryContainer.rosterEntry.standby eq true}">Ja</c:when>
								<c:otherwise>Nein</c:otherwise>
							</c:choose></td>
							<td><img class="showRosterEntryInfo"
								title="${rosterEntryContainer.rosterEntry.rosterNotes}"
								src="<c:url value="/image/info.gif"/>" /></td>
							<td><c:choose>
								<c:when
									test="${userSession.loginInformation.authorization eq 'Benutzer'}">
									<c:choose>
										<c:when
											test="${params.currentDate gt rosterEntryContainer.deadline}">
										</c:when>
										<c:when
											test="${rosterEntryContainer.rosterEntry.servicetype.serviceName eq 'Freiwillig'}">
											<c:url var="url" value="/Dispatcher/editRosterEntry.do">
												<c:param name="savedUrl">/roster.do</c:param>
												<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
											</c:url>
											<a href="${url}">Bearbeiten</a>
											<br />
											<c:url var="url" value="/Dispatcher/deleteRosterEntry.do">
												<c:param name="savedUrl">/roster.do</c:param>
												<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
											</c:url>
											<a href="${url}">L&ouml;schen</a>
											<c:set var="breakRow">true</c:set>
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
									<%--<c:if test="${userSession.internalSession eq true}">--%>
									<c:choose>
										<c:when
											test="${rosterEntryContainer.plannedStartOfWork ne null and rosterEntryContainer.plannedEndOfWork ne null and rosterEntryContainer.realStartOfWork eq null and rosterEntryContainer.realEndOfWork eq null and params.currentDate ge rosterEntryContainer.registerStart}">
											<c:url var="url" value="/Dispatcher/registerRosterEntry.do">
												<c:param name="action">register</c:param>
												<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
											</c:url>
											<c:if test="${breakRow eq true}">
												<br />
											</c:if>
											<a href="${url}">Anmelden</a>
										</c:when>
										<c:when
											test="${rosterEntryContainer.plannedStartOfWork ne null and rosterEntryContainer.plannedEndOfWork ne null and rosterEntryContainer.realStartOfWork ne null and rosterEntryContainer.realEndOfWork eq null and params.currentDate ge rosterEntryContainer.plannedEndOfWork}">
											<c:url var="url" value="/Dispatcher/registerRosterEntry.do">
												<c:param name="action">signOff</c:param>
												<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
											</c:url>
											<c:if test="${breakRow eq true}">
												<br />
											</c:if>
											<a href="${url}">Abmelden</a>
										</c:when>
									</c:choose>
									<%--</c:if>--%>
								</c:when>
								<c:when
									test="${userSession.loginInformation.authorization eq 'Administrator'}">
									<c:url var="url" value="/Dispatcher/editRosterEntry.do">
										<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
										<c:param name="savedUrl">/roster.do</c:param>
									</c:url>
									<a href="${url}">Bearbeiten</a>
									<br />
									<c:url var="url" value="/Dispatcher/deleteRosterEntry.do">
										<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
										<c:param name="savedUrl">/roster.do</c:param>
									</c:url>
									<a href="${url}">L&ouml;schen</a>
									<c:set var="breakRow">true</c:set>
									<%--<c:if test="${userSession.internalSession eq true}">--%>
									<c:choose>
										<c:when
											test="${rosterEntryContainer.plannedStartOfWork ne null and rosterEntryContainer.plannedEndOfWork ne null and rosterEntryContainer.realStartOfWork eq null and rosterEntryContainer.realEndOfWork eq null}">
											<c:url var="url" value="/Dispatcher/registerRosterEntry.do">
												<c:param name="action">register</c:param>
												<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
											</c:url>
											<c:if test="${breakRow eq true}">
												<br />
											</c:if>
											<a href="${url}">Anmelden</a>
										</c:when>
										<c:when
											test="${rosterEntryContainer.plannedStartOfWork ne null and rosterEntryContainer.plannedEndOfWork ne null and rosterEntryContainer.realStartOfWork ne null and rosterEntryContainer.realEndOfWork eq null}">
											<c:url var="url" value="/Dispatcher/registerRosterEntry.do">
												<c:param name="action">signOff</c:param>
												<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
											</c:url>
											<c:if test="${breakRow eq true}">
												<br />
											</c:if>
											<a href="${url}">Abmelden</a>
										</c:when>
									</c:choose>
									<%--</c:if>--%>
								</c:when>
							</c:choose></td>
						</tr>
						<c:remove var="class" />
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
<c:url var="url" value="/Dispatcher/printRoster.do">
	<c:param name="locationId">${params.location.id}</c:param>
	<c:param name="date">
		<fmt:formatDate type="date" value="${params.date}" />
	</c:param>
</c:url>
<a href="${url}">Dienstplan&nbsp;drucken</a>
<script type="text/javascript">
$(document).ready(function() {
	Calendar.setup ({
		inputField : "date",
		button : "dateCalendarTrigger",
		date : new Date(${params.calendarDefaultDateMilliseconds}),
		range : new Array (${params.calendarRangeStart}, ${params.calendarRangeEnd}),
		align : "Br",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y",
		onClose : update
	});
	
	$('#locationId').change(function() {
		var url = '?locationId=' + $(this).val();
		var date = $('#date').val();
		if (date) {
			url += '&date=' + date;
		}
		document.location = url;
	});
	
	$('#rosterEntryTable .showRosterEntryInfo').Tooltip({ delay: 100, showURL: false });
	
	function update(cal) {
		var url = '?date=' + $('#date').val();
		var locationId = $('#locationId').val();
		if (locationId) {
			url += '&locationId=' + locationId;
		}
		document.location = url;
	}
});
</script>