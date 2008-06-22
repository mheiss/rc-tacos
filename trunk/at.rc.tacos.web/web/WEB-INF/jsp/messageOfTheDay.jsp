<%@ include file="includes.jsp"%>
<c:choose>
	<c:when test="${params.addedCount gt 0}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;neuen&nbsp;Message&nbsp;of&nbsp;the&nbsp;day&nbsp;angelegt</div>
		<br />
	</c:when>
</c:choose>
<c:url var="url" value="/Dispatcher/messageOfTheDay.do" />
<form action="${url}" method="post" accept-charset="ISO-8859-1">
<table class="standardForm">
	<tr>
		<td colspan="3" style="font-weight: bold">Message&nbsp;of&nbsp;the&nbsp;day:</td>
	</tr>
	<tr>
		<td style="font-weight: bold">Datum:<sup class="reqMark">*</sup></td>
		<td><input id="date" name="date" type="text" size="10"
			maxlength="10" value="${params.date}" /> <c:url var="url"
			value="/image/calendar_edit.gif" /> <img src="${url}" border="0"
			id="dateCalendarTrigger" style="cursor: pointer" /></td>
		<td />
	</tr>
	<tr>
		<td colspan="2"><textarea id="comment" name="comment" cols="40"
			rows="7" wrap="soft">${params.messageOfTheDay}</textarea><sup class="reqMark">*</sup></td>
		<td />
		<td><span class="errorText"> <c:choose>
			<c:when test="${not empty params.errors.messageOfTheDayMissing}">
							${params.errors.messageOfTheDayMissing}
						</c:when>
		</c:choose></span></td>
	</tr>
	<tr>
		<td colspan="3" class="reqComment">Mit&nbsp;*&nbsp;markierte&nbsp;Felder&nbsp;sind&nbsp;Pflichtfelder.</td>
		<td />
	</tr>
	<tr>
		<td class="hButtonArea" colspan="3"><input type="submit"
			value="Speichern" /> <input name="action" type="hidden"
			value="addMessageOfTheDay" /></td>
		<td />
	</tr>
</table>
</form>
<script type="text/javascript">
$(document).ready(function() {
	Calendar.setup ({
		inputField : "messageOfTheDayDate",
		button : "dateCalendarTrigger",
		date : new Date(${params.calendarDefaultDateMilliseconds}),
		range : new Array (${params.calendarRangeStart}, ${params.calendarRangeEnd}),
		align : "Tr",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y",
		onClose : update
	});
	
	function update(cal) {
		var url = '?date=' + $('#date').val();
		var locationId = $('#locationId').val();
		if (messageOfTheDay) {
			url += '&messageOfTheDay=' + messageOfTheDay;
		}
		document.location = url;
	}
});
	
</script>