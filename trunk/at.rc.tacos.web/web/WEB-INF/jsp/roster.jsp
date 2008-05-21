<%@ include file="includes.jsp"%>
<c:url var="url" value="/Dispatcher/addRosterEntry.do" />
<c:choose>
	<c:when test="${params.messageCode eq 'edited'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;Dienstplaneintrag&nbsp;erfolgreich&nbsp;bearbeitet</div>
		<br />
	</c:when>
	<c:when test="${params.messageCode eq 'deleted'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;Dienstplaneintrag&nbsp;erfolgreich&nbsp;gelöscht</div>
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
<table class="standardForm"">
	<tr>
		<td>Ortsstelle:</td>
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
		<td>Datum:</td>
		<td><input id="date" name="date" type="text" size="10"
			maxlength="10"
			value="<fmt:formatDate type="date" value="${params.date}"/>" /> <c:url
			var="url" value="/image/calendar_edit.gif" /> <img src="${url}"
			border="0" id="dateCalendarTrigger" style="cursor: pointer" /></td>
	</tr>
</table>
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
<br />
<c:choose>
	<c:when test="${fn:length(params.rosterEntryContainerList) gt 0}">
		<table id="rosterEntryTable" class="list">
			<tr>
				<th class="header2" colspan="10">${params.location.locationName}&nbsp;am&nbsp;<fmt:formatDate
					type="date" dateStyle="medium" value="${params.date}" /></th>
			</tr>
			${fieldHeadersRow}
			<tbody>
				<c:forEach var="rosterEntryContainer"
					items="${params.rosterEntryContainerList}" varStatus="loop">
					<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
						<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.staffMember.lastName}
						${rosterEntryContainer.rosterEntry.staffMember.firstName}</td>
						<td><fmt:formatDate type="both" dateStyle="short"
							timeStyle="short"
							value="${rosterEntryContainer.plannedStartOfWork}" /></td>
						<td><fmt:formatDate type="both" dateStyle="short"
							timeStyle="short"
							value="${rosterEntryContainer.plannedEndOfWork}" /></td>
						<td><c:choose>
							<c:when test="${rosterEntryContainer.realStartOfWork eq null}">-</c:when>
							<c:otherwise>
								<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
									value="${rosterEntryContainer.realStartOfWork}" />
							</c:otherwise>
						</c:choose></td>
						<td><c:choose>
							<c:when test="${rosterEntryContainer.realEndOfWork eq null}">-</c:when>
							<c:otherwise>
								<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
									value="${rosterEntryContainer.realEndOfWork}" />
							</c:otherwise>
						</c:choose></td>
						<td nowrap="nowrap">${rosterEntryContainer.rosterEntry.job.jobName}</td>
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
							<c:when test="${authorization eq 'Benutzer'}">
								<c:choose>
									<c:when
										test="${params.currentDate gt rosterEntryContainer.deadline}">
									</c:when>
									<c:when
										test="${rosterEntryContainer.rosterEntry.servicetype.serviceName eq 'Freiwillig'}">
										<c:url var="url" value="/Dispatcher/editRosterEntry.do">
											<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
										</c:url>
										<a href="${url}">Bearbeiten</a>
										<br />
										<c:url var="url" value="/Dispatcher/deleteRosterEntry.do">
											<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
										</c:url>
										<a href="${url}">Löschen</a>
										<c:set var="breakRow">true</c:set>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>
								<c:if test="${userSession.internalSession eq true}">
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
								</c:if>
							</c:when>
							<c:when test="${authorization eq 'Administrator'}">
								<c:url var="url" value="/Dispatcher/editRosterEntry.do">
									<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
								</c:url>
								<a href="${url}">Bearbeiten</a>
								<br />
								<c:url var="url" value="/Dispatcher/deleteRosterEntry.do">
									<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
								</c:url>
								<a href="${url}">Löschen</a>
								<c:set var="breakRow">true</c:set>
								<c:if test="${userSession.internalSession eq true}">
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
												<c:param name="action">signoff</c:param>
												<c:param name="rosterEntryId">${rosterEntryContainer.rosterEntry.rosterId}</c:param>
											</c:url>
											<c:if test="${breakRow eq true}">
												<br />
											</c:if>
											<a href="${url}">Abmelden</a>
										</c:when>
									</c:choose>
								</c:if>
							</c:when>
						</c:choose></td>
					</tr>
				</c:forEach>
			</tbody>
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