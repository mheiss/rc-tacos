<%@ include file="includes.jsp"%>
<table class="standardForm">
	<tr>
		<td colspan="2" valign="top"><c:url var="url"
			value="/image/staffmembers/${userSession.loginInformation.userInformation.staffMemberId}.jpg">
			<c:param name="refresh">1</c:param>
		</c:url><img src="${url}" width="110" height="138" alt="No photo found." /></td>
		<td>
		<table class="standardForm">
			<tr>
				<td style="font-weight: bold">Personalnummer:</td>
				<td>${userSession.loginInformation.userInformation.staffMemberId}</td>
			</tr>
			<tr>
				<td style="font-weight: bold">Vorname:</td>
				<td>${userSession.loginInformation.userInformation.firstName}</td>
			</tr>
			<tr>
				<td style="font-weight: bold">Nachname:</td>
				<td>${userSession.loginInformation.userInformation.lastName}</td>
			</tr>
			<tr>
				<td style="font-weight: bold">Geburtsdatum:</td>
				<td><c:choose>
					<c:when test="${params.date ne null}">
						<fmt:formatDate type="date" dateStyle="medium"
							value="${params.date}"></fmt:formatDate>
					</c:when>
					<c:otherwise>&nbsp;</c:otherwise>
				</c:choose></td>
			</tr>
			<tr>
				<td style="font-weight: bold">Geschlecht:</td>
				<td><c:choose>
					<c:when test="${userSession.loginInformation.userInformation.male}">
				männlich
			</c:when>
					<c:otherwise>
				weiblich
			</c:otherwise>
				</c:choose></td>
			</tr>
			<tr>
				<td style="font-weight: bold">Telefonnummern:</td>
				<td>
				<table id="mobilePhoneTable" class="list" cellpadding="3"
					cellspacing="0">
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
						<c:forEach var="mobilePhone"
							items="${userSession.loginInformation.userInformation.phonelist}"
							varStatus="loop">
							<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
								<td>${mobilePhone.mobilePhoneName}</td>
								<td>${mobilePhone.mobilePhoneNumber}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</td>
			</tr>
			<tr>
				<td style="font-weight: bold">Dienststelle:</td>
				<td>
				${userSession.loginInformation.userInformation.primaryLocation.locationName}
				</td>
			</tr>
			<tr>
				<td style="font-weight: bold">Kompetenzen:</td>
				<td>
				<table id="competenceTable" class="list" cellpadding="3"
					cellspacing="0">
					<thead>
						<tr>
							<th class="header2" colspan="2">Kompetenzen</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="competence"
							items="${userSession.loginInformation.userInformation.competenceList}"
							varStatus="loop">
							<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
								<td>${competence.competenceName}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</td>
			</tr>
			<tr>
				<td style="font-weight: bold">Benutzername:</td>
				<td>${userSession.loginInformation.username}</td>
			</tr>
			<tr>
				<td style="font-weight: bold">Authorisierung:</td>
				<td>${userSession.loginInformation.authorization}</td>
			</tr>
		</table>
		</td>
	</tr>
</table>