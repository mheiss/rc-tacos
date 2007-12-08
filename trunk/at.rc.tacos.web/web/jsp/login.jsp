<%
	Map<String, Object> params = (Map)request.getAttribute("params");
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="../css/stylesheetLog.css">
		<link  rel='icon' type="image/x-icon" href="../favicon.ico">
		<title>Dienstplan RK Bruck-Kapfenberg</title>
	</head>
	
	
	<body onLoad="document.loginPanel.username.focus()">
	<%@ page import="java.util.*,java.text.*"%>
	<%@page import="java.util.ResourceBundle" %>
	<%@page session="true" %>

	<% ResourceBundle res = (ResourceBundle)session.getAttribute("curResBundle");%>
	
	<%-- format the date %--%>
	<%Date current = new Date();
		DateFormat dateformat;
		dateformat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);%>
		
		<%! int cnt=0; %> <% cnt++;%>
		<%! int s = -1; %>

			
		<table cellpadding="0" border="0" cellspacing="0" width="800" align="center" id="mainTabLogDate"><tr><td align="right">Heute ist der <%=dateformat.format(current) %></td></tr></table>
		<table cellpadding="0" border="0" cellspacing="0" width="800" align="center" id="mainTabLog">
			<tr>
				<td id="leftLogSite" align="left" valign="bottom"><a href="#">Rotes Kreuz Österreich</a><br/><a href="#">Impressum</a></td>
				<td align="right" valign="bottom" id="rightLogSite">
					<% if(params.containsKey("loginError")) { %>
						<div id='meldungstext'><%=params.get("loginError") %> </div>
					<% 	} %>
					<form  method="post" action="<%=request.getContextPath()+"/Dispatcher/login.do?action=login"%>" > 
						<table id="loginPanel">
		  					<tr><td align='right'>Username:</td><td align='left'><input type="text" name="username" size="20" id="username"></td></tr>
		  				 	<tr><td align='right'>Password:</td><td align='left'><input type="password" name="password" size="20" maxlength="12" id="password"></td></tr>
		  					<tr><td colspan='2' align='right'><input type="submit" value="Login" id="login"></td></tr>		
		  				</table>
					</form> 
				</td>
			</tr>
		</table>
		<table cellpadding="0" border="0" cellspacing="0" width="800" align="center"><tr><td align="left" width="190"><img src="../image/tacos_logo_7.png" name="tacos" alt="tacos_logo" /></td><td align="left" valign="bottom" width="610"><img src="../image/tacos_logo_6.png" name="tacos" alt="tacos_logo" /></td></tr></table>
	</body>
</html>	


