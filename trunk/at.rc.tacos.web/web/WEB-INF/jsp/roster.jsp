<%@ include file="includes.jsp" %>
<c:url var="url" value="/Dispatcher/addRosterEntry.do" />
<table class="standardForm"">
	<tr>
		<td>Ortsstelle:</td>
		<td>
			<select size="1" id="locationId" name="locationId">
				<option value="noValue">-- Ortsstelle wählen --</option>
				<c:forEach var="location" items="${params.locationList}">
					<option value="${location.id}" ${(not empty params.location) and (params.location.id == location.id) ? ' selected="selected"' : ''}>${location.locationName}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td>Datum:</td>
		<td>
			<input id="date" name="date" type="text" size="10" maxlength="10" value="<fmt:formatDate type="date" value="${params.date}"/>" />
			<c:url var="url" value="/image/calendar_edit.gif" />
			<img src="${url}" border="0" id="dateCalendarTrigger" style="cursor:pointer" />
		</td>
	</tr>
</table>
<script type="text/javascript">
$(document).ready(function() {
	Calendar.setup ({
		inputField : "date",
		button : "dateCalendarTrigger",
		date : new Date(${params.calendarDefaultDateMilliseconds}),
		range : new Array (${params.calendarRangeStart}, ${params.calendarRangeEnd}),
		align : "Br",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y",
		onClose : update
	});
});
$(function() {			
	$('#locationId').change(function() {
		var url = '?locationId=' + $(this).val();
		var date = $('#date').val();
		if (date) {
			url += '&date=' + date;
		}
		document.location = url;
	});
});
function update(cal) {
	var url = '?date=' + $('#date').val();
	var locationId = $('#locationId').val();
	if (locationId) {
		url += '&locationId=' + locationId;
	}
	document.location = url;
}
</script>