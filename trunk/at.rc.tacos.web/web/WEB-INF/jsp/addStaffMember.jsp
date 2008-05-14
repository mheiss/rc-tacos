<%@ include file="includes.jsp" %>
<form action="${url}" method="post" accept-charset="ISO-8859-1">
	<table class="standardForm">
		<tr><td colspan="2">Allgemeine&nbsp;Daten</td><td /></tr>
		<tr>
			<td>Personalnummer:<sup class="reqMark">*</sup></td>
			<td>
				<input name="staffMemberId" type="text" size="30" maxlength="8" />
			</td>
			<td>
				<span class="errorText">${params.errors.staffMemberId}</span>
			</td>
		</tr>
		<tr>
			<td>Vorname:<sup class="reqMark">*</sup></td>
			<td>
				<input name="firstName" type="text" size="30" maxlength="30" />
			</td>
			<td>
				<span class="errorText">${params.errors.firstName}</span>
			</td>
		</tr>
		<tr>
			<td>Nachname:<sup class="reqMark">*</sup></td>
			<td>
				<input name="lastName" type="text" size="30" maxlength="30" />
			</td>
			<td>
				<span class="errorText">${params.errors.firstName}</span>
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
				<span class="errorText">${params.errors.birthDate}</span>
			</td>
		</tr>
		<tr>
			<td>Geschlecht:<sup class="reqMark">*</sup></td>
			<td>
				<select size="1" name="sex">
					<option value="noValue">-- Geschlecht wählen --</option>
					<option value="m">-- männlich --</option>
					<option value="w">-- weiblich --</option>
				</select>
			</td>
			<td>
				<span class="errorText">${params.errors.sex}</span>
			</td>
		</tr>
		<tr>
			<td>Telefonnummern:<sup class="reqMark">*</sup></td>
			<td>
				<select size="1" id="phone">
				</select>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
$(document).ready(function() {
	Calendar.setup ({
		inputField : "birthDate",
		button : "birthDateCalendarTrigger",
		date : new Date(${params.calendarDefaultDateMilliseconds}),
		range : new Array (${params.calendarRangeStart}, ${params.calendarRangeEnd}),
		align : "Tr",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y"
	});
});
</script>