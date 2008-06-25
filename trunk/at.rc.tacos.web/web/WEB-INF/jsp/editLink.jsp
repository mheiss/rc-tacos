<%@ include file="includes.jsp"%>
<c:url var="url" value="/Dispatcher/editLink.do" />
<form action="${url}" method="post" accept-charset="ISO-8859-1">
<table class="standardForm">
	<tr>
		<td style="font-weight: bold">Text:<sup class="reqMark">*</sup></td>
		<td><input name="innerText" type="text" size="30" maxlength="250"
			value="${params.innerText}" /></td>
		<td><span class="errorText"> <c:choose>
			<c:when test="${not empty params.errors.innerTextMissing}">
				${params.errors.innerTextMissing}
			</c:when>
			<c:when test="${not empty params.errors.innerTextTooLong}">
				${params.errors.innerTextTooLong}
			</c:when>
		</c:choose></span></td>
	</tr>
	<tr>
		<td style="font-weight: bold">URL:<sup class="reqMark">*</sup></td>
		<td><input name="href" type="text" size="30" maxlength="250"
			value="${params.href}" /></td>
		<td><span class="errorText"> <c:choose>
			<c:when test="${not empty params.errors.hrefMissing}">
				${params.errors.hrefMissing}
			</c:when>
			<c:when test="${not empty params.errors.hrefTooLong}">
				${params.errors.hrefTooLong}
			</c:when>
		</c:choose></span></td>
	</tr>
	<tr>
		<td colspan="2"><textarea name="title" cols="30" rows="7"
			wrap="soft">${params.title}</textarea></td>
		<td><span class="errorText"> <c:choose>
			<c:when test="${not empty params.errors.titleTooLong}">
							${params.errors.titleTooLong}
						</c:when>
		</c:choose></span></td>
	</tr>
	<tr>
		<td colspan="3" class="reqComment">Mit&nbsp;*&nbsp;markierte&nbsp;Felder&nbsp;sind&nbsp;Pflichtfelder.</td>
	</tr>
	<tr>
		<td class="hButtonArea" colspan="3">
			<input type="submit" value="Link speichern" />
			<input name="action" type="hidden" value="addLink" />
			<input name="linkId" type="hidden" value="${params.link.id}" />
		</td>
	</tr>
</table>
</form>