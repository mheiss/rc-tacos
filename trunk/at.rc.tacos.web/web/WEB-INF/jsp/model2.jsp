<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ include file="includes.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/stylesheet.css"/>" />
	<c:if test="${not empty css}"><c:forTokens var="item" items="${css}" delims="|"><link rel="stylesheet" href="<c:url value="/css/${item}"/>" type="text/css" /></c:forTokens></c:if>
	<link rel="icon" type="image/x-icon" href="<c:url value="/favicon.ico"/>" />
	<script type="text/javascript" src="<c:url value="/js/windowActions.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/jquery-1.2.3.js"/>"></script>
	<c:if test="${not empty js}"><c:forTokens var="item" items="${js}" delims="|"><script type="text/javascript" src="<c:url value="/js/${item}"/>"></script></c:forTokens></c:if>
	<title>${title}</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td valign="top" width="33%"></td>
				<td id="ContentContainer" valign="top" width="33%">
					<jsp:include page="${view}" />
				</td>
			<td valign="top" width="33%"></td>
		</tr>
	</table>
</body>
</html>