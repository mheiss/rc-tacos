<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="includes.jsp" %>
<%@ page import="java.util.*,java.text.*"%>
<%@ page session="true"%>
<%
	Map<String, Object> params = (Map<String, Object>) request.getAttribute("params");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="../css/stylesheetLog.css" />
	<link rel="icon" type="image/x-icon" href="../favicon.ico" />
	<title>TACOS : Login</title>
</head>
<body onload="document.loginPanel.username.focus()">
<%
	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
	Date current = new Date();
%>

<table cellpadding="0" border="0" cellspacing="0" width="800" align="center" id="mainTabLogDate">
	<tr>
		<td align="right">Heute ist der <%= format.format(current) %></td>
	</tr>
</table>
<table cellpadding="0" border="0" cellspacing="0" width="800" align="center" id="mainTabLog">
	<tr>
		<td id="leftLogSite" align="left" valign="bottom"><a
			href="http://www.st.roteskreuz.at/">Rotes Kreuz Steiermark</a> <br /></td>
		<td align="right" valign="bottom" id="rightLogSite">
		<%
			if (params.containsKey("loginError")) {
		%>
		<div id="meldungstext"><%=params.get("loginError")%></div>
		<%
			}
		%>
		<c:url var="url" value="/Dispatcher/login.do?action=login" />
		<form method="post" action="${url}">
			<table id="loginPanel">
				<tr>
					<td align="right">Benutzername:</td>
					<td align="left"><input type="text" name="username" size="20" id="username"/></td>
				</tr>
				<tr>
					<td align="right">Passwort:</td>
					<td align="left"><input type="password" name="password" size="20" maxlength="12" id="password"/></td>
				</tr>
				<tr>
					<td>
						<%
						if (request.getParameter("responseUrl") != null) {
						%>
						<input type=hidden name="responseUrl" value="<%= request.getParameter("responseUrl") %>"/>
						<%
						}
						%>	
					</td>
					<td colspan="2" align="right"><input type="submit" value="Login" id="login"/></td>
				</tr>
			</table>
		</form>
		</td>
	</tr>
</table>
<table cellpadding="0" border="0" cellspacing="0" width="800" align="center">
	<tr>
		<td align="left" width="190">
			<img src="../image/tacos_logo_7.jpg" name="tacos" alt="tacos_logo" />
		</td>
		<td align="left" valign="bottom" width="610">
			<img src="../image/tacos_logo_6.jpg" name="tacos" alt="tacos_logo" />
		</td>
	</tr>
</table>
</body>
</html>


