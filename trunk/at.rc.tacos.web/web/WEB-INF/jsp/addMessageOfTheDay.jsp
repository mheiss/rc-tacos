<%@ include file="includes.jsp"%>
<c:choose>
	<c:when test="${params.addedCount gt 0}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;neuen&nbsp;Message&nbsp;of&nbsp;the&nbsp;day&nbsp;angelegt</div>
		<br />
	</c:when>
</c:choose>
<form action="${url}" method="post" accept-charset="ISO-8859-1"
	enctype="multipart/form-data">
<table class="standardForm">
	<tr>
		<td colspan="3" style="font-weight: bold">Add&nbsp;message&nbsp;of&nbsp;the&nbsp;day:</td>
	</tr>
	
	<tr>
		<td style="font-weight: bold">Message of the day:<sup class="reqMark">*</sup></td>
		<td><input name="messageOfTheDay" type="text" size="50" maxlength="300"
			value="${params.messageOfTheDay}" /></td>
		<td><span class="errorText"> <c:choose>
			<c:when test="${not empty params.errors.firstNameMissing}">
							${params.errors.firstNameMissing}
						</c:when>
			<c:when test="${not empty params.errors.firstNameTooLong}">
							${params.errors.firstNameTooLong}
						</c:when>
		</c:choose> </span></td>
	</tr>

	<tr>
		<td style="font-weight: bold">Datum:<sup class="reqMark">*</sup></td>
		<td><input id="messageOfTheDayDate" name="messageOfTheDayDate" type="text" size="10"
			maxlength="10" value="${params.messageOfTheDayDate}" /> <c:url var="url"
			value="/image/calendar_edit.gif" /> <img src="${url}" border="0"
			id="dateCalendarTrigger" style="cursor: pointer" /></td>
		<td><span class="errorText"> <c:choose>
			<c:when test="${not empty params.errors.date}">
							${params.errors.date}
						</c:when>
			<c:when test="${not empty params.errors.dateTooSmall}">
							${params.errors.dateTooSmall}
						</c:when>
			<c:when test="${not empty params.errors.dateTooBig}">
							${params.errors.dateTooBig}
						</c:when>
		</c:choose> </span></td>
	</tr>
	
	<tr>
		<td colspan="3" class="reqComment">Mit&nbsp;*&nbsp;markierte&nbsp;Felder&nbsp;sind&nbsp;Pflichtfelder.</td>
		<td />
	</tr>
	<tr>
		<td class="hButtonArea" colspan="3"><input type="submit"
			value="Message of the day anlegen" /> <input name="action" type="hidden"
			value="addMessageOfTheDay" /></td>
		<td />
	</tr>
</table>
</form>
<script type="text/javascript">
$(document).ready(function() {
	var m = 0;
	var c = 0;
	Calendar.setup ({
		inputField : "messageOfTheDayDate",
		button : "dateCalendarTrigger",
		date : new Date(${params.calendarDefaultDateMilliseconds}),
		range : new Array (${params.calendarRangeStart}, ${params.calendarRangeEnd}),
		align : "Tr",
		ifFormat : "%d.%m.%Y",
		daFormat : "%d.%m.%Y"
	});
});
	
	
	
</script>