<%@ include file="includes.jsp"%>
<c:choose>
	<c:when test="${params.editedCount gt 0}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;Ihr&nbsp;Passwort&nbsp;erfolgreich&nbsp;ge&auml;ndert</div>
		<br />
	</c:when>
</c:choose>
<c:url var="url" value="/Dispatcher/personnelData.do" />
<form action="${url}" method="post" accept-charset="ISO-8859-1">
<table class="standardForm">
	<tr>
		<td colspan="2" valign="top"><c:url var="url"
			value="/image/staffmembers/${userSession.loginInformation.userInformation.staffMemberId}.jpg">
			<c:param name="refresh">${params.timestamp}</c:param>
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
				<td style="font-weight: bold">Telefon 1:</td>
				<td>${userSession.loginInformation.userInformation.phone1}</td>
			</tr>
			<tr>
				<td style="font-weight: bold">Telefon 2:</td>
				<td>${userSession.loginInformation.userInformation.phone2}</td>
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
			<tr>
				<td style="font-weight: bold">Passwort:<sup class="reqMark">*</sup></td>
				<td><input name="passwd" type="password" size="30"
					maxlength="255" /></td>
				<td><span class="errorText"> <c:choose>
					<c:when test="${not empty params.errors.passwordMissing}">
									${params.errors.passwordMissing}
								</c:when>
					<c:when test="${not empty params.errors.passwordTooLong}">
									${params.errors.passwordTooLong}
								</c:when>
				</c:choose> </span></td>
			</tr>
			<tr>
				<td style="font-weight: bold">Passwort&nbsp;(wiederholen):<sup class="reqMark">*</sup></td>
				<td><input name="repeatedPassword" type="password" size="30"
					maxlength="255" /></td>
				<td><span class="errorText"> <c:choose>
					<c:when test="${not empty params.errors.repeatedPasswordMissing}">
									${params.errors.repeatedPasswordMissing}
								</c:when>
					<c:when test="${not empty params.errors.repeatedPasswordTooLong}">
									${params.errors.repeatedPasswordTooLong}
								</c:when>
					<c:when test="${not empty params.errors.passwordsNotEqual}">
									${params.errors.passwordsNotEqual}
								</c:when>
				</c:choose> </span></td>
			</tr>
			<tr>
				<td colspan="3" class="reqComment">Mit&nbsp;*&nbsp;markierte&nbsp;Felder&nbsp;sind&nbsp;Pflichtfelder.</td>
				<td />
			</tr>
			<tr>
				<td class="hButtonArea" colspan="3"><input type="submit"
					value="Passwort ändern" /> <input name="action" type="hidden"
					value="updateStaffMember" /></td>
				<td />
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>