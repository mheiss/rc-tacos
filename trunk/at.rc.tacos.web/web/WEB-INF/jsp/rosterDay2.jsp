<%@ include file="includes.jsp" %>
<c:url var="url" value="/Dispatcher/rosterDay2.do?action=addRosterEntry" />
<form action="url" method="post" accept-charset="utf-8">
	<div id="generalDataformHeader" class="formHeader">Allgemeine Daten</div>
	<table id="generalDataForm" class="standardForm">
		<tr>
			<td>Mitarbeiter:</td>
			<td>
				<select size="1" name="staffMemberId">
					<c:forEach var="staffMember" items="${params.staffList}">
						<option value="${staffMember.staffMemberId}">
							${staffMember.lastName}&nbsp;${staffMember.firstName}
						</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>Ortsstelle:</td>
			<td>
				<select size="1" name="locationId">
					<c:forEach var="location" items="${params.locationList}">
						<option value="${location.id}">${location.locationName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr colspan="2">
			<td>Bereitschaft</td>
		</tr>
		<tr>
			<td>Verwendung:</td>
			<td>
				<select size="1" name="jobId">
					<c:forEach var="job" items="${params.jobList}">
						<option value="${job.id}">${job.jobName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>Dienstverhältnis:</td>
			<td>
				<select size="1" name="serviceTypeId">
					<c:forEach var="serviceType" items="${params.serviceTypeList}">
						<option value="${serviceType.id}">${serviceType.serviceName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>
	<div id="commentFormHeader" class="formHeader">Anmerkungen</div>
	<table id="commentForm" class="standardForm">
		<tr>
			<td></td>
		</tr>
	</table>
	<div id="timeFormHeader" class="formHeader">Dienstzeiten</div>
	<table id="timeForm" class="standardForm">
		<tr>
			<td>Dienst von: </td><td>bis: </td>
		</tr>
	</table>
</form>