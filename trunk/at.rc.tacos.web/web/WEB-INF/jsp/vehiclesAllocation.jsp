<%@ include file="includes.jsp"%>
<c:set var="fieldHeadersRow">
	<tr class="subhead2">
		<th nowrap="nowrap">Fahrzeugname</th>
		<th nowrap="nowrap">Fahrer</th>
		<th nowrap="nowrap">Sanitäter&nbsp;1</th>
		<th nowrap="nowrap">Sanitäter&nbsp;2</th>
		<th nowrap="nowrap">Handy Bezeichnung</th>
		<th nowrap="nowrap">Handynummer</th>
	</tr>
</c:set>
<c:set var="vehicleMap"
	value="${params.vehicleContainerListContainer.vehicleContainerMap}" />
<br />
<c:choose>
	<c:when test="${fn:length(vehicleMap) gt 0}">
		<table cellpadding="3" cellspacing="0" class="list">
			<c:forEach var="vehicleMapEntry" items="${vehicleMap}">
				<c:set var="location" value="${vehicleMapEntry.key}" />
				<tr>
					<th class="header2" colspan="6">${location.locationName}</th>
				</tr>
				${fieldHeadersRow}
				<c:forEach var="vehicleContainer" items="${vehicleMapEntry.value}"
					varStatus="loop">
					<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
						<td nowrap="nowrap">${vehicleContainer.vehicleName}</td>
						<td nowrap="nowrap">${vehicleContainer.driver.lastName}&nbsp;${vehicleDetailContainer.driver.firstName}</td>
						<td nowrap="nowrap">${vehicleContainer.firstParamedic.lastName}&nbsp;${vehicleDetailContainer.firstParamedic.firstName}</td>
						<td nowrap="nowrap">${vehicleContainer.secondParamedic.lastName}&nbsp;${vehicleDetailContainer.secondParamedic.firstName}</td>
						<td nowrap="nowrap">${vehicleContainer.mobilePhone.mobilePhoneName}</td>
						<td nowrap="nowrap">${vehicleContainer.mobilePhone.mobilePhoneNumber}</td>
					</tr>
				</c:forEach>
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