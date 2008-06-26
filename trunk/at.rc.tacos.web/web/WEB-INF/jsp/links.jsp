<%@ include file="includes.jsp"%>
<c:choose>
	<c:when test="${params.messageCode eq 'edited'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;Link&nbsp;erfolgreich&nbsp;bearbeitet</div>
		<br />
	</c:when>
	<c:when test="${params.messageCode eq 'deleted'}">
		<div id="submitSuccess">Sie&nbsp;haben&nbsp;einen&nbsp;Link&nbsp;erfolgreich&nbsp;gelöscht</div>
		<br />
	</c:when>
</c:choose>
<br />
<c:set var="fieldHeadersRow">
	<tr>
		<th nowrap="nowrap" class="header2">Text</th>
		<th nowrap="nowrap" class="header2">URL</th>
		<th nowrap="nowrap" class="header2">Beschreibung</th>
		<th nowrap="nowrap" class="header2">zuletzt&nbsp;ge&auml;ndert&nbsp;von</th>
		<th nowrap="nowrap" class="header2">&nbsp;</th>
	</tr>
</c:set>
<c:choose>
	<c:when test="${fn:length(params.linkList) gt 0}">
		<table cellpadding="3" cellspacing="0" class="list">
			${fieldHeadersRow}
			<c:forEach var="link" items="${params.linkList}" varStatus="loop">
				<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
					<td nowrap="nowrap">${link.innerText}</td>
					<td nowrap="nowrap">${link.href}</td>
					<td>${link.title}</td>
					<td nowrap="nowrap">${link.username}</td>
					<td>
						<c:url var="url" value="/Dispatcher/editLink.do">
							<c:param name="linkId">${link.id}</c:param>
							<c:param name="savedUrl">/links.do</c:param>
						</c:url>
						<a href="${url}">Bearbeiten</a>
						<br />
						<c:url var="url" value="/Dispatcher/deleteLink.do">
							<c:param name="linkId">${link.id}</c:param>
							<c:param name="savedUrl">/links.do</c:param>
						</c:url>
						<a href="${url}">L&ouml;schen</a>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
		<table cellpadding="3" cellspacing="0" class="list">
			<tr>
				<td class="nodata">Keine&nbsp;Treffer</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose>