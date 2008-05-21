<%@ include file="includes.jsp"%>
<table class="standardForm">
<tr>
<td colspan="2" valign="top">
	<c:url var="url" value="/image/staffmembers/${userSession.loginInformation.userInformation.staffMemberId}" />
	<img src="${url}" width="110" height="138" alt="No photo found." content="no-cache" />
</td>
<td>
<table class="standardForm">
	<tr>
		<td>Personalnummer:</td>
		<td>${userSession.loginInformation.userInformation.staffMemberId}</td>
	</tr>
	<tr>
		<td>Vorname:</td>
		<td>${userSession.loginInformation.userInformation.firstName}</td>
	</tr>
	<tr>
		<td>Nachname:</td>
		<td>${userSession.loginInformation.userInformation.lastName}</td>
	</tr>
	<tr>
		<td>Geburtsdatum:</td>
		<td><fmt:formatDate type="date" dateStyle="medium" value="${params.date}"></fmt:formatDate></td>
	</tr>
	<tr>
		<td>Geschlecht:</td>
		<td>
		<c:choose>
			<c:when test="${userSession.loginInformation.userInformation.male}">
				männlich
			</c:when>
			<c:otherwise>
				weiblich
			</c:otherwise>
		</c:choose>
		</td>
	</tr>
	<tr>
		<td>Telefonnummern:</td>
		<td>
			<table id="mobilePhoneTable" class="list">
				<thead>
					<tr>
						<th class="header2" colspan="3">Telefonnummern</th>
					</tr>
					<tr class="subhead2">
						<th>Handy&nbsp;Bezeichnung</th>
						<th>Handynummer</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mobilePhone" items="${userSession.loginInformation.userInformation.phonelist}" varStatus="loop">
						<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}"><td>${mobilePhone.mobilePhoneName}</td><td>${mobilePhone.mobilePhoneNumber}</td></tr>
					</c:forEach>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td>Dienststelle:</td>
		<td>
			${userSession.loginInformation.userInformation.primaryLocation.locationName}
		</td>
	</tr>
	<tr>
		<td>Kompetenzen:</td>
		<td>
			<table id="competenceTable" class="list">
				<thead>
					<tr>
						<th class="header2" colspan="2">Kompetenzen</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="competence" items="${userSession.loginInformation.userInformation.competenceList}" varStatus="loop">
						<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}"><td>${competence.competenceName}</td></tr>
					</c:forEach>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td>Benutzername:</td>
		<td>${userSession.loginInformation.username}</td>
	</tr>
	<tr>
		<td>Authorisierung:</td>
		<td>${userSession.loginInformation.authorization}</td>
	</tr>
</table>
</td>
</tr>
</table>