<%@ include file="includes.jsp" %>
<c:set var="staffMemberMap" value="${params.staffMemberListContainer.staffMemberContainerMap}"/>
<table class="standardForm">
	<tr>
		<td style="font-weight:bold">Ortsstelle:</td>
		<td><select size="1" id="locationId" name="locationId">
			<option value="noValue">-- Ortsstelle wählen --</option>
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
		<th nowrap="nowrap">&nbsp;</th>
		<th nowrap="nowrap">Nachname</th>
		<th nowrap="nowrap">Vorname</th>
		<th nowrap="nowrap">Telefonnummern</th>
		<th nowrap="nowrap">Kompetenzen</th>
		<th nowrap="nowrap">&nbsp;</th>
	</tr>
</c:set>
<br />
<br />
<c:choose>
	<c:when test="${fn:length(params.staffMemberList) gt 0}">
		<table id="rosterEntryTable" class="list">
			<c:forEach var="staffMemberMapEntry" items="${staffMemberMap}">
				<c:set var="location" value="${staffMemberMapEntrystaffMemberMapEntry.key}"/>	
				<tr>
					<th class="header2" colspan="6">${location.locationName}</th>
				</tr>
				${fieldHeadersRow}
				<tbody>
					<c:forEach var="staffMember" items="${params.staffMemberList}" varStatus="loop">
						<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
							<td nowrap="nowrap">
								<c:url var="url" value="/image/staffmembers/${staffMember.staffMemberId}" />
								<img src="${url}" width="110" height="138" alt="No photo found." />
							</td>
							<td nowrap="nowrap">${staffMember.lastName}</td>
							<td nowrap="nowrap">${staffMember.firstName}</td>
							<td>
								<c:forEach var="mobilePhone" items="${staffMember.phonelist}" varStatus="loop2"><c:if test="${loop2.count gt 1}"><br /></c:if>${mobilePhone.mobilePhoneName}&nbsp;${mobilePhone.mobilePhoneNumber}</c:forEach>
							</td>
							<td>
								<c:forEach var="competence" items="${staffMember.competenceList}" varStatus="loop3"><c:if test="${loop3.count gt 1}"><br /></c:if>${competence.competenceName}</c:forEach>
							</td>
							<td>
								<c:url var="url" value="/Dispatcher/editStaffMember.do?staffMemberId=${staffMember.staffMemberId}"></c:url>
								<a href="${url}">Mitarbeiter bearbeiten</a>
							</td>
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