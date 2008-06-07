<%@ include file="includes.jsp"%>
<table class="standardForm">
	<tr>
		<td style="font-weight: bold">Ortsstelle:</td>
		<td><select size="1" id="locationId" name="locationId">
			<c:forEach var="location" items="${params.locationList}">
				<option value="${location.id}" ${(not empty
					params.location) and (params.location.id==
					location.id) ? ' selected="selected"' : ''}>${location.locationName}</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td style="font-weight: bold">Monat:</td>
		<td><select size="1" id="month" name="month">
			<option value="JANUARY" ${(not empty params.month) and (params.month == 'JANNUARY') ? ' selected="selected"' : ''}>Jänner</option>
			<option value="FEBRUARY" ${(not empty params.month) and (params.month == 'FEBRUARY') ? ' selected="selected"' : ''}>Februar</option>
			<option value="MARCH" ${(not empty params.month) and (params.month == 'MARCH') ? ' selected="selected"' : ''}>März</option>
			<option value="APRIL" ${(not empty params.month) and (params.month == 'APRIL') ? ' selected="selected"' : ''}>April</option>
			<option value="MAY" ${(not empty params.month) and (params.month == 'MAY') ? ' selected="selected"' : ''}>Mai</option>
			<option value="JUNE" ${(not empty params.month) and (params.month == 'JUNE') ? ' selected="selected"' : ''}>Juni</option>
			<option value="JULY" ${(not empty params.month) and (params.month == 'JULY') ? ' selected="selected"' : ''}>Juli</option>
			<option value="AUGUST" ${(not empty params.month) and (params.month == 'AUGUST') ? ' selected="selected"' : ''}>August</option>
			<option value="SEPTEMBER" ${(not empty params.month) and (params.month == 'SEPTEMBER') ? ' selected="selected"' : ''}>September</option>
			<option value="OCTOBER" ${(not empty params.month) and (params.month == 'OCTOBER') ? ' selected="selected"' : ''}>Oktober</option>
			<option value="NOVEMBER" ${(not empty params.month) and (params.month == 'NOVEMBER') ? ' selected="selected"' : ''}>November</option>
			<option value="DECEMBER" ${(not empty params.month) and (params.month == 'DECEMBER') ? ' selected="selected"' : ''}>Dezember</option>
		</select></td>
	</tr>
	<tr>
		<td style="font-weight: bold">Jahr:</td>
		<td><select size="1" id="year" name="year">
			<c:forEach var="year" items="${params.yearList}">
				<option value="${year}" ${(not empty
					params.year) and (params.year==
					year) ? ' selected="selected"' : ''}>${year}</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td style="font-weight: bold">Funktion:</td>
		<td><select size="1" id="functionId" name="functionId">
			<option value="noValue">-- Funktion wählen --</option>
			<c:forEach var="function" items="${params.functionList}">
				<option value="${function.id}" ${(not empty
					params.function) and (params.function.id==
					function.id) ? ' selected="selected"' : ''}>${function.competenceName}</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td style="font-weight: bold">Mitarbeiter:</td>
		<td><select size="1" id="staffMemberId" name="staffMemberId">
			<option value="noValue">-- Mitarbeiter wählen--</option>
			<c:forEach var="staffMember" items="${params.staffList}">
				<option value="${staffMember.staffMemberId}" ${(not empty
					params.staffMember) and (params.staffMember.staffMemberId==
					staffMember.staffMemberId) ? ' selected="selected"' : ''}>
				${staffMember.lastName}&nbsp;${staffMember.firstName}</option>
			</c:forEach>
		</select></td>
	</tr>
</table>
<br />
<table>
</table>
<script type="text/javascript">
$(document).ready(function() {
	$('#locationId').change(function() {
		var url = '?locationId=' + $(this).val();
		update(url, 'l');
	});
	$('#functionId').change(function() {
		var url = '?functionId=' + $(this).val();
		update(url, 'f');
	});
	$('#staffMemberId').change(function() {
		var url = '?staffMemberId=' + $(this).val();
		update(url, 's');
	});
	$('#month').change(function() {
		var url = '?month=' + $(this).val();
		update(url, 'm');
	});
	$('#year').change(function() {
		var url = '?year=' + $(this).val();
		update(url, 'y');
	});
	function update(url, code) {
		var locationId = $('#locationId').val();
		var functionId = $('#functionId').val();
		var staffMemberId = $('#staffMemberId').val();
		var month = $('#month').val();
		var year = $('#year').val();
		if (code == 'l') {
			url = url + '&functionId=' + functionId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&year=' + year;
		} else if (code == 'f') {
			url = url + '&locationId=' + locationId + '&staffMemberId=' + staffMemberId + '&month=' + month + '&year=' + year;
		} else if (code == 's') {
			url = url + '&locationId=' + locationId + '&functionId=' + functionId + '&month=' + month + '&year=' + year;
		} else if (code == 'm') {
			url = url + '&locationId=' + locationId + '&functionId=' + functionId + '&staffMemberId=' + staffMemberId + '&year=' + year;
		} else if (code == 'y') {
			url = url + '&locationId=' + locationId + '&functionId=' + functionId + '&staffMemberId=' + staffMemberId + '&month=' + month;
		}
		document.location = url;
	}
});
</script>