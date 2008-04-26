<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="includes.jsp" %>
<%@ page import="at.rc.tacos.web.web.UserSession" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.*" %>
<%
final Map<String, Object> params = (Map<String, Object>)request.getAttribute("params");
final UserSession userSession = (UserSession)session.getAttribute("userSession");
final Date today = new Date();
final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/stylesheet.css"/>" />
	<c:if test="${not empty css}"><c:forTokens var="item" items="${css}" delims="|"><link rel="stylesheet" href="<c:url value="/css/${item}"/>" type="text/css" /></c:forTokens></c:if>
	<link rel="icon" type="image/x-icon" href="<c:url value="/favicon.ico"/>" />
	<script type="text/javascript" src="<c:url value="/js/windowActions.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/jquery-1.2.3.js"/>"></script>
	<c:if test="${not empty js}"><c:forTokens var="item" items="${js}" delims="|"><script type="text/javascript" src="<c:url value="/js/${item}"/>"></script></c:forTokens></c:if>
	<title>${title}</title>
</head>
<body>

	<table border="0" cellpadding="0" cellspacing="0" width="100%" id="MainTab">
		<thead>
			<tr>
				<td>
				<table border="0" cellpadding="0" cellspacing="0" width="100%" id="Tablogo">
					<tr>
						<td style="background:url(../image/headlogopx.jpg) repeat-x center;"><img src="../image/headlogo.jpg" name="logoLeft" id="logoLeft" /></td>
					</tr>
				</table>
				</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td id="MainBodyContent">
					<table width="100%" id="userInfo">
						<c:url var="url" value="/Dispatcher/logout.do" />
						<tr>
							<td width="33%" align="left">Willkommen : <%=userSession.getStaffMember().getFirstName() + " "
							+ userSession.getStaffMember().getLastName().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss")%>
							&nbsp;&nbsp;( <a
								href="${url}">Logout</a>
							)</td>
							<td width="33%" align="center">${htitle}</td>
							<td width="33%" align="right">Heute ist der <%=format.format(today)%></td>
							<td>
						</tr>
					</table>
					<table>
						<tr>
							<!-- #### LEFT CONTAINER NAVIGATION-->
							<td id="LeftContainerPanel" valign="top"><%@ include file="navigation.jsp"%></td>
							<!-- #### CONTENT -->
							<td id="ContentContainer" valign="top">
								<jsp:include page="${view}" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</tbody>

</body>
</html>