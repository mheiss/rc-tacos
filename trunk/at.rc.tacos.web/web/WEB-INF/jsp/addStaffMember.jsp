<%@ include file="includes.jsp" %>
<c:choose>
	<c:when test="${params.addedCount gt 0}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;neuen&nbsp;Mitarbeiter&nbsp;angelegt</div>
		<br />
	</c:when>
</c:choose>
<form action="${url}" method="post" accept-charset="ISO-8859-1" enctype="multipart/form-data">
	<table class="standardForm">
		<tr><td colspan="2">Allgemeine&nbsp;Daten:</td><td /></tr>
		<tr>
			<td>Personalnummer&nbsp;(5xxxxxxx):<sup class="reqMark">*</sup></td>
			<td>
				<input name="personnelNumber" type="text" size="30" maxlength="8" value="${params.personnelNumber}" />
			</td>
			<td>
				<span class="errorText">${params.errors.personnelNumber}</span>
			</td>
		</tr>
		<tr>
			<td>Vorname:<sup class="reqMark">*</sup></td>
			<td>
				<input name="firstName" type="text" size="30" maxlength="30" value="${params.firstName}" />
			</td>
			<td>
				<span class="errorText">${not empty params.errors.firstNameMissing ? params.errors.firstNameMissing + ' ' : ''}${not empty params.errors.firstNameTooLong ? params.errors.firstNameTooLong : ''}</span>
			</td>
		</tr>
		<tr>
			<td>Nachname:<sup class="reqMark">*</sup></td>
			<td>
				<input name="lastName" type="text" size="30" maxlength="30" value="${params.lastName}" />
			</td>
			<td>
				<span class="errorText">${not empty params.errors.lastNameMissing ? params.errors.lastNameMissing + ' ' : ''}${not empty params.errors.lastNameTooLong ? params.errors.lastNameTooLong : ''}</span>
			</td>
		</tr>
		<tr>
			<td>Geburtsdatum:</td>
			<td>
				<input id="birthDate" name="birthDate" type="text" size="10" maxlength="10" value="${params.birthDate}" />
				<c:url var="url" value="/image/calendar_edit.gif" />
				<img src="${url}" border="0" id="birthDateCalendarTrigger" style="cursor:pointer" />
			</td>
			<td>
				<span class="errorText">${not empty params.errors.birthdate ? params.errors.birthdate + ' ' : ''}${not empty params.errors.birthdateTooSmall ? params.errors.birthdateTooSmall + ' ': ''}${not empty params.errors.birthdateTooBig ? params.errors.birthdateTooBig : ''}</span>
			</td>
		</tr>
		<tr>
			<td>Geschlecht:<sup class="reqMark">*</sup></td>
			<td>
				<select size="1" name="sex">
					<option value="noValue">-- Geschlecht wählen --</option>
					<option ${(not empty params.sex) and (params.sex == 'männlich') ? ' selected="selected"' : ''}>männlich</option>
					<option ${(not empty params.sex) and (params.sex == 'weiblich') ? ' selected="selected"' : ''}>weiblich</option>
				</select>
			</td>
			<td>
				<span class="errorText">${params.errors.sex}</span>
			</td>
		</tr>
		<tr>
			<td>Telefonnummern:</td>
			<td>
				<select size="1" id="mobilePhone" name="mobilePhoneId">
					<option value="">-- Telefonnummer wählen --</option>
					<c:forEach var="mobilePhone" items="${params.mobilePhoneList}">
						<option value="${mobilePhone.id}" ${(not empty params.mobilePhone) and (params.mobilePhone.id == mobilePhone.id) ? ' selected="selected"' : ''}>${mobilePhone.mobilePhoneName} - ${mobilePhone.mobilePhoneNumber}</option>
					</c:forEach>
				</select>
				<a id="addMobilePhone" class="smallLink" style="cursor:pointer">Telefonnummer&nbsp;hinzuf&uuml;gen</a>
			</td>
			<td />
		</tr>
		<tr>
			<td>
				<input id="mobilePhoneIds" name="mobilePhoneIds" type="hidden" value="${params.mobilePhoneIds}" />
			</td>
			<td>
				<table id="mobilePhoneTable" class="list">
					<thead>
						<tr>
							<th class="header2" colspan="3">Telefonnummern</th>
						</tr>
						<tr class="subhead2">
							<th>Handy&nbsp;Bezeichnung</th>
							<th>Handynummer</th>
							<th>&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mobilePhone" items="${params.mobilePhoneTable}" varStatus="loop">
							<tr id="mobilePhone-${mobilePhone.id}" class="${loop.count % 2 == 0 ? 'even' : 'odd'}"><td id="mobilePhoneName-${mobilePhone.id}">${mobilePhone.mobilePhoneName}</td><td id="mobilePhoneNumber-${mobilePhone.id}">${mobilePhone.mobilePhoneNumber}</td><td><a id="deleteMobilePhone-${mobilePhone.id}" class="smallLink" style="cursor:pointer">L&ouml;schen</a></td></tr>
						</c:forEach>
					</tbody>
				</table>
			</td>
			<td />
		</tr>
		<tr>
			<td>Photo:</td>
			<td>
				<input name="image" type="file" />
			</td>
			<td>
				<span class="errorText">${not empty params.errors.photoTooBig ? params.errors.photoTooBig + ' ' : ''}${not empty params.errors.photoTooSmall ? params.errors.photoTooSmall : ''}</span>
			</td>
		</tr>
		<tr><td colspan="2">Ausbildung&nbsp;und&nbsp;Dienststelle:</td><td /></tr>
		<tr>
			<td>Dienststelle:<sup class="reqMark">*</sup></td>
			<td>
				<select name="locationId" size="1">
					<option value="noValue">-- Dienststelle auswählen --</option>
					<c:forEach var="location" items="${params.locationList}">
						<option value="${location.id}" ${(not empty params.location) and (params.location.id == location.id) ? ' selected="selected"' : ''}>${location.locationName}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<span class="errorText">${params.errors.location}</span>
			</td>
		</tr>
		<tr>
			<td>Kompetenzen&nbsp;(zumindest&nbsp;Volont&auml;r):<sup class="reqMark">*</sup></td>
			<td>
				<select size="1" id="competence" name="competenceId">
					<option value="">-- Kompetenz wählen --</option>
					<c:forEach var="competence" items="${params.competenceList}">
						<option value="${competence.id}" ${(not empty params.competence) and (params.competence.id == competence.id) ? ' selected="selected"' : ''}>${competence.competenceName}</option>
					</c:forEach>
				</select>
				<a id="addCompetence" class="smallLink" style="cursor:pointer">Kompetenz&nbsp;hinzuf&uuml;gen</a>
			</td>
			<td />
		</tr>
		<tr>
			<td>
				<input id="competenceIds" name="competenceIds" type="hidden" value="${params.competenceIds}" />
			</td>
			<td>
				<table id="competenceTable" class="list">
					<thead>
						<tr>
							<th class="header2" colspan="2">Kompetenzen</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="competence" items="${params.competenceTable}" varStatus="loop">
							<tr id="competence-${competence.id}" class="${loop.count % 2 == 0 ? 'even' : 'odd'}"><td id="competenceName-${competence.id}">${competence.competenceName}</td><td><a id="deleteCompetence-${competence.id}" class="smallLink" style="cursor:pointer">L&ouml;schen</a></td></tr>
						</c:forEach>
					</tbody>
				</table>
			</td>
			<td>
				<span class="errorText">${params.errors.competences}</span>
			</td>
		</tr>
		<tr><td colspan="2">Daten&nbsp;zum&nbsp;Anmelden&nbsp;am&nbsp;System&nbsp;und&nbsp;Online-Dienstplan:</td><td /></tr>
		<tr>
			<td>Benutzername:<sup class="reqMark">*</sup></td>
			<td>
				<input name="username" type="text" size="30" maxlength="30" value="${params.username}" />
			</td>
			<td>
				<span class="errorText">${not empty params.errors.usernameMissing ? params.errors.usernameMissing + ' ' : ''}${not empty params.errors.usernameTooLong ? params.errors.usernameTooLong : ''}</span>
			</td>
		</tr>
		<tr>
			<td>Passwort:<sup class="reqMark">*</sup></td>
			<td>
				<input name="password" type="password" size="30" maxlength="255" value="${params.password}" />
			</td>
			<td>
				<span class="errorText">${not empty params.errors.passwordMissing ? params.errors.passwordMissing + ' ' : ''}${not empty params.errors.passwordTooLong ? params.errors.passwordTooLong : ''}</span>
			</td>
		</tr>
		<tr>
			<td>Passwort&nbsp;(wiederholen):<sup class="reqMark">*</sup></td>
			<td>
				<input name="repeatedPassword" type="password" size="30" maxlength="255" value="${params.repeatedPassword}" />
			</td>
			<td>
				<span class="errorText">${not empty params.errors.repeatedPasswordMissing ? params.errors.repeatedPasswordMissing + ' ' : ''}${not empty params.errors.repeatedPasswordTooLong ? params.errors.repeatedPasswordTooLong + ' ' : ''}${not empty params.errors.passwordsNotEqual ? params.errors.passwordsNotEqual : ''}</span>
			</td>
		</tr>
		<tr>
			<td />
			<td><input id="lockUser" name="lockUser" type="checkbox" value="true"${(not empty params.lockUser) and (params.lockUser == true) ? ' checked="checked"' : ''} /><label for="lockUser" style="cursor:pointer">Benutzer sperren</label></td>
			<td />
		</tr>
		<tr>
			<td>Authorisierung:<sup class="reqMark">*</sup></td>
			<td>
				<select name="authorization" size="1">
					<option value="noValue">-- Authorisierung wählen --</option>
					<option value="Benutzer" ${(not empty params.authorization) and (params.authorization == 'Benutzer') ? ' selected="selected"' : ''}>Benutzer</option>
					<option value="Administrator" ${(not empty params.authorization) and (params.authorization == 'Administrator') ? ' selected="selected"' : ''}>Administrator</option>
				</select>
			</td>
			<td>
				<span class="errorText">${params.errors.authorization}</span>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="reqComment">Mit&nbsp;*&nbsp;markierte&nbsp;Felder&nbsp;sind&nbsp;Pflichtfelder.</td>
			<td />
		</tr>
		<tr>
			<td class="hButtonArea" colspan="2">
				<input type="submit" value="Anlegen" />
				<input name="action" type="hidden" value="addStaffMember" />
			</td>
			<td />
		</tr>
	</table>
</form>
<script type="text/javascript">
$(document).ready(function() {
	var m = 0;
	var c = 0;
	Calendar.setup ({
		inputField : "birthDate",
		button : "birthDateCalendarTrigger",
		date : new Date(${params.calendarDefaultDateMilliseconds}),
		range : new Array (${params.calendarRangeStart}, ${params.calendarRangeEnd}),
		align : "Tr",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y"
	});
	
	$('#addMobilePhone').click(function() {
		m++;
		var mobilePhoneId = $('select#mobilePhone').val();
		var mobilePhoneString = $('select#mobilePhone option:selected').text();
		var mobilePhoneStringArray = mobilePhoneString.split(' - ');
		var mobilePhoneName = mobilePhoneStringArray[0];
		var mobilePhoneNumber = mobilePhoneStringArray[1];
		if (mobilePhoneId) {
			var doubleMobilePhoneId = $('tr#mobilePhone-' + mobilePhoneId).attr('id');
			if (!doubleMobilePhoneId) {
				var a = '<a id="deleteMobilePhone-' + mobilePhoneId + '" class="smallLink" style="cursor:pointer">L&ouml;schen</a>';
				var tr = null;
				if (m % 2 == 0) {
					tr = '<tr id="mobilePhone-' + mobilePhoneId + '" class="even"><td id="mobilePhoneName-' + mobilePhoneId + '">' + mobilePhoneName + '</td><td id="mobilePhoneNumber-' + mobilePhoneId + '">' + mobilePhoneNumber + '</td><td>' + a + '</td></tr>';
				} else {
					tr = '<tr id="mobilePhone-' + mobilePhoneId + '" class="odd"><td id="mobilePhoneName-' + mobilePhoneId + '">' + mobilePhoneName + '</td><td id="mobilePhoneNumber-' + mobilePhoneId + '">' + mobilePhoneNumber + '</td><td>' + a + '</td></tr>';
				}
				$('table#mobilePhoneTable > tbody').append(tr);
				var mobilePhoneIds = $('input#mobilePhoneIds').val()
				if (mobilePhoneIds) {
					$('input#mobilePhoneIds').val(mobilePhoneIds + ',' + mobilePhoneId);
				} else {
					$('input#mobilePhoneIds').val(mobilePhoneId);
				}
				$('#deleteMobilePhone-' + mobilePhoneId).click(function() {
					var mobilePhoneIdString = $(this).attr('id');
					var mobilePhoneId = mobilePhoneIdString.split('-')[1];
					var mobilePhoneIds = $('input#mobilePhoneIds').val();
					var mobilePhoneIdsArray = mobilePhoneIds.split(',');
					var newMobilePhoneIds = null;
					for (var i = 0; i < mobilePhoneIdsArray.length; i++) {
						if (mobilePhoneIdsArray[i] != mobilePhoneId) {
							if (newMobilePhoneIds) {
								newMobilePhoneIds = newMobilePhoneIds + ',' + mobilePhoneIdsArray[i];
							} else {
								newMobilePhoneIds = mobilePhoneIdsArray[i];
							}
						}
					}
					if (newMobilePhoneIds) {
						$('input#mobilePhoneIds').val(newMobilePhoneIds);
					} else {
						$('input#mobilePhoneIds').removeAttr('value');
					}
					$('tr#mobilePhone-' + mobilePhoneId).remove();
				});
			}
		}
	});
	
	$('#addCompetence').click(function() {
		c++;
		var competenceId = $('select#competence').val();
		var competenceString = $('select#competence option:selected').text();
		if (competenceId) {
			var doubleCompetenceId = $('tr#competence-' + competenceId).attr('id');
			if (!doubleCompetenceId) {
				var a = '<a id="deleteCompetence-' + competenceId + '" class="smallLink" style="cursor:pointer">L&ouml;schen</a>';
				var tr = null;
				if (c % 2 == 0) {
					tr = '<tr id="competence-' + competenceId + '" class="even"><td id="competenceName-' + competenceId + '">' + competenceString + '</td><td>' + a + '</td></tr>';
				} else {
					tr = '<tr id="competence-' + competenceId + '" class="odd"><td id="competenceName-' + competenceId + '">' + competenceString + '</td><td>' + a + '</td></tr>';
				}
				$('table#competenceTable > tbody').append(tr);
				var competenceIds = $('input#competenceIds').val();
				if (competenceIds) {
					$('input#competenceIds').val(competenceIds + ',' + competenceId);
				} else {
					$('input#competenceIds').val(competenceId);
				}
				$('#deleteCompetence-' + competenceId).click(function() {
					var competenceIdString = $(this).attr('id');
					var competenceId = competenceIdString.split('-')[1];
					var competenceIds = $('input#competenceIds').val();
					var competenceIdsArray = competenceIds.split(',');
					var newCompetenceIds = null;
					for (var i = 0; i < competenceIdsArray.length; i++) {
						if (competenceIdsArray[i] != competenceId) {
							if (newCompetenceIds) {
								newCompetenceIds = newCompetenceIds + ',' + competenceIdsArray[i];
							} else {
								newCompetenceIds = competenceIdsArray[i];
							}
						}
					}
					if (newCompetenceIds) {
						$('input#competenceIds').val(newCompetenceIds);
					} else {
						$('input#competenceIds').removeAttr('value');
					}
					$('tr#competence-' + competenceId).remove();
				});
			}
		}
	});
	
	$('[id^=deleteMobilePhone]').click(function() {
		var mobilePhoneIdString = $(this).attr('id');
		var mobilePhoneId = mobilePhoneIdString.split('-')[1];
		var mobilePhoneIds = $('#mobilePhoneIds').val();
		var mobilePhoneIdsArray = mobilePhoneIds.split(',');
		var newMobilePhoneIds = null;
		for (var i = 0; i < mobilePhoneIdsArray.length; i++) {
			if (mobilePhoneIdsArray[i] != mobilePhoneId) {
				if (newMobilePhoneIds) {
					newMobilePhoneIds = newMobilePhoneIds + ',' + mobilePhoneIdsArray[i];
				} else {
					newMobilePhoneIds = mobilePhoneIdsArray[i];
				}
			}
		}
		if (newMobilePhoneIds) {
			$('#mobilePhoneIds').val(newMobilePhoneIds);
		} else {
			$('#mobilePhoneIds').removeAttr('value');
		}
		$('tr#mobilePhone-' + mobilePhoneId).remove();
	});
	
	$('[id^=deleteCompetence]').click(function() {
		var competenceIdString = $(this).attr('id');
		var competenceId = competenceIdString.split('-')[1];
		var competenceIds = $('#competenceIds').val();
		var competenceIdsArray = competenceIds.split(',');
		var newCompetenceIds = null;
		for (var i = 0; i < competenceIdsArray.length; i++) {
			if (competenceIdsArray[i] != competenceId) {
				if (newCompetenceIds) {
					newCompetenceIds = newCompetenceIds + ',' + competenceIdsArray[i];
				} else {
					newCompetenceIds = competenceIdsArray[i];
				}
			}
		}
		if (newCompetenceIds) {
			$('#competenceIds').val(newCompetenceIds);
		} else {
			$('#competenceIds').removeAttr('value');
		}
		$('tr#competence-' + competenceId).remove();
	});
});
</script>