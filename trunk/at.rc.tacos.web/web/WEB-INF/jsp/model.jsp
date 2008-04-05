<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="at.rc.tacos.web.web.UserSession" %>
<%@ page import="at.rc.tacos.web.web.Dispatcher" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.ResourceBundle" %>
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
	<link href="../css/stylesheet.css" rel="stylesheet" />
	<link rel="icon" type="image/x-icon" href="../favicon.ico" />
	<script type="text/javascript" src="../js/windowActions.js"></script>
	<title><%= params.get("title") %></title>
</head>
<body>

	<table border='0' cellpadding='0' cellspacing='0' width="100%" id="MainTab">
		<thead>
			<tr>
				<td>
				<table border='0' cellpadding='0' cellspacing='0' width="100%" id="Tablogo">
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
						<tr>
							<td width="33%" align="left">Willkommen : <%=userSession.getStaffMember().getFirstName() + " "
							+ userSession.getStaffMember().getLastName().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss")%>
							&nbsp;&nbsp;( <a
								href="<%=request.getContextPath()+"/Dispatcher/logout.do"%>">logout</a>
							)</td>
							<td width="33%" align="center"><%= params.get("header") %></td>
							<td width="33%" align="right">Heute ist der <%=format.format(today)%></td>
							<td>
						</tr>
					</table>
					<table width="100%">
						<tr>
							<!-- #### LEFT CONTAINER NAVIGATION-->
							<td id="LeftContainerPanel" valign="top"><%@ include file="navigation.jsp"%></td>
							<!-- #### CONTENT -->
							<td id="ContentContainer" valign="top">
							<%
							if (request.getParameter("view").equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("dutiesDay.url"))) {
								%>
								<%@ include file="dutiesDay.jsp" %>
								<%
							}
							%>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</tbody>

</body>
</html>