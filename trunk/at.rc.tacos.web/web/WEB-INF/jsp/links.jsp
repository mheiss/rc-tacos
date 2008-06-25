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
<br />
<c:set var="fieldHeadersRow">
	<tr class="subhead2">
		<th nowrap="nowrap">Text</th>
		<th nowrap="nowrap">URL</th>
		<th nowrap="nowrap">Beschreibung</th>
		<th nowrap="nowrap">zuletzt&nbsp;ge&auml;ndert&nbsp;von</th>
		<th>&nbsp;</th>
	</tr>
</c:set>
<c:choose>
	<c:when test="${fn:length(param.linkList) gt 0}">
		<table cellpadding="3" cellspacing="0" class="list">
			<c:forEach var="link" items="${linkList}" varStatus="loop">
				<tr class="${loop.count % 2 == 0 ? 'even' : 'odd'}">
					<td nowrap="nowrap">${link.innerText}</td>
					<td nowrap="nowrap">${link.href}</td>
					<td>${link.title}</td>
					<td nowrap="nowrap">${link.username}</td>
					<td>
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