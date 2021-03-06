<%@ include file="includes.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<c:if test="${useCache eq false}">
	<meta http-equiv="PRAGMA" content="NO-CACHE" />
</c:if>
<c:if test="${refresh ne -1}">
	<c:url var="url" value="/Dispatcher/login.do" />
	<meta http-equiv="refresh" content="${refresh};url=${url}" />
</c:if>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/stylesheetLog.css"/>" />
<link rel="icon" type="image/x-icon"
	href="<c:url value="/favicon.ico"/>" />
<title>TACOS :: Login</title>
</head>
<body onload="document.loginPanel.username.focus()">
<table cellpadding="0" border="0" cellspacing="0" width="800"
	align="center" id="mainTabLogDate">
	<tr>
		<td align="right">Heute&nbsp;ist&nbsp;der&nbsp;<fmt:formatDate type="date" dateStyle="medium" value="${userSession.today}" /></td>
	</tr>
</table>
<table cellpadding="0" border="0" cellspacing="0" width="800"
	align="center" id="mainTabLog">
	<tr>
		<td id="leftLogSite" align="left" valign="bottom"><a
			href="http://www.st.roteskreuz.at/">Rotes Kreuz Steiermark</a> <br />
		</td>
		<td align="right" valign="bottom" id="rightLogSite">
		<c:if test="${params.loginError ne null}">
			<div id="meldungstext">${params.loginError}</div>
		</c:if>
		<c:url var="url" value="/Dispatcher/login.do?action=login" />
		<form method="post" action="${url}" accept-charset="ISO-8859-1">
		<table id="loginPanel">
			<tr>
				<td align="right">Benutzername:</td>
				<td align="left"><input type="text" name="username" size="20"
					id="username" /></td>
			</tr>
			<tr>
				<td align="right">Passwort:</td>
				<td align="left"><input type="password" name="password"
					size="20" maxlength="255" id="password" /></td>
			</tr>
			<tr>
				<td><input type="hidden" name="savedUrl" value="${savedUrl}" />
				</td>
				<td colspan="2" align="right"><input type="submit"
					value="Login" id="login" /></td>
			</tr>
		</table>
		</form>
		</td>
	</tr>
</table>
<table cellpadding="0" border="0" cellspacing="0" width="800"
	align="center">
	<tr>
		<td align="left" width="190"><img src="../image/tacos_logo_7.jpg"
			name="tacos" alt="tacos_logo" /></td>
		<td align="left" valign="bottom" width="610"><img
			src="../image/tacos_logo_6.jpg" name="tacos" alt="tacos_logo" /></td>
	</tr>
</table>
</body>
</html>


