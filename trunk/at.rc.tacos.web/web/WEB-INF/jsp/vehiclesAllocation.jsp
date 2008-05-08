<%@ include file="includes.jsp"%>
<c:set var="fieldHeadersRow">
	<tr class="subhead2">
		<th nowrap="nowrap">Fahrzeugname</th>
		<th nowrap="nowrap">Fahrer</th>
		<th nowrap="nowrap">Sanitäter&nbsp;1</th>
		<th nowrap="nowrap">Sanitäter&nbsp;2</th>
		<th nowrap="nowrap">Handy&nbps;Name</th>
		<th nowrap="nowrap">Handynummer</th>
	</tr>
</c:set>
<c:choose>
	<c:when test="${fn:length(params.rosterEntryContainerList) gt 0}">
		<table cellpadding="3" cellspacing="0" class="list">
			<c:forEach var="locationVehicleDetailList" items="${params.vehicleListContainer.vehicleDetailMap}">
				<c:set var="location" value="${locationVehicleDetailList.key}"/>
				<tr>
					<th class="header2" colspan="6">${location.locationName}</th>
				</tr>
				<c:forEach var="vehicleDetail" items="${locationVehicleDetailList.value}" varStatus="loop">
					<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
						<td nowrap="nowrap">${vehicleDetail.vehicleName}</td>
						<td nowrap="nowrap">${vehicleDetail.driver.lastName}&nbsp;${vehicleDetail.driver.firstName}</td>
						<td nowrap="nowrap">${vehicleDetail.firstParamedic.lastName}&nbsp;${vehicleDetail.firstParamedic.firstName}</td>
						<td nowrap="nowrap">${vehicleDetail.secondParamedic.lastName}&nbsp;${vehicleDetail.secondParamedic.firstName}</td>
						<td nowrap="nowrap">${vehicleDetail.mobilePhone.mobilePhoneName}</td>
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