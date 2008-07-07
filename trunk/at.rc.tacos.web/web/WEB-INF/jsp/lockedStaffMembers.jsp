<%@ include file="includes.jsp"%>
<c:set var="staffMemberMap"
	value="${params.staffMemberListContainer.staffMemberContainerMap}" />
<table class="standardForm">
	<tr>
		<td style="font-weight: bold">Ortsstelle:</td>
		<td><select size="1" id="locationId" name="locationId">
			<option value="noValue">-- Ortsstelle w�hlen --</option>
			<c:forEach var="location" items="${params.locationList}">
				<option value="${location.id}" ${(not empty
					params.location) and (params.location.id==
					location.id) ? ' selected="selected"' : ''}>${location.locationName}</option>
			</c:forEach>
		</select></td>
	</tr>
</table>
<c:set var="fieldHeadersRow">
	<tr class="subhead2">
		<th nowrap="nowrap">Nachname</th>
		<th nowrap="nowrap">Vorname</th>
		<th nowrap="nowrap">Photo</th>
		<th nowrap="nowrap">Telefon 1</th>
		<th nowrap="nowrap">Telefon 2</th>
		<th nowrap="nowrap">Kompetenzen</th>
		<th nowrap="nowrap">&nbsp;</th>
	</tr>
</c:set>
<br />
<br />
<c:choose>
	<c:when test="${fn:length(staffMemberMap) gt 0}">
		<table id="rosterEntryTable" class="list" cellpadding="3"
			cellspacing="0">
			<c:forEach var="staffMemberMapEntry" items="${staffMemberMap}">
				<c:set var="location" value="${staffMemberMapEntry.key}" />
				<tr>
					<th class="header2" colspan="7">${location.locationName}</th>
				</tr>
				${fieldHeadersRow}
				<tbody>
					<c:forEach var="staffMember" items="${staffMemberMapEntry.value}"
						varStatus="loop">
						<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
							<td nowrap="nowrap">${staffMember.lastName}</td>
							<td nowrap="nowrap">${staffMember.firstName}</td>
							<td nowrap="nowrap"><c:url var="url"
								value="/image/staffmembers/${staffMember.staffMemberId}.jpg">
								<c:param name="refresh">${params.timestamp}</c:param>
							</c:url><img src="${url}" width="55" height="69" alt="No photo found." />
							</td>
							<td nowrap="nowrap"> &nbsp;${staffMember.phone1}</td>
							<td nowrap="nowrap"> &nbsp;${staffMember.phone2}</td>
							<td><c:choose>
								<c:when test="${fn:length(staffMember.competenceList) gt 0}">
									<c:forEach var="competence"
										items="${staffMember.competenceList}" varStatus="loop3">
										<c:if test="${loop3.count gt 1}">
											<br />
										</c:if>${competence.competenceName}</c:forEach>
								</c:when>
								<c:otherwise>&nbsp;</c:otherwise>
							</c:choose></td>
							<td><c:url var="url"
								value="/Dispatcher/editLockedStaffMember.do?staffMemberId=${staffMember.staffMemberId}"></c:url>
							<a href="${url}">Mitarbeiter bearbeiten</a></td>
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
<script>
$(document).ready(function() {
	$('#locationId').change(function() {
		var url = '?locationId=' + $(this).val();
		document.location = url;
	});
});
</script>