<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />
<script type="text/javascript" src="../js/windowActions.js"></script>
<title>TACOS :: RK Bruck-Kapfenberg</title>
</head>
<%@page import="java.util.Map"%>
<% 	Map<String,Object> params = (Map)request.getAttribute("params"); %>
<body>
<table width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td width="50%"><!-- Timetablebox Day -->
		<table width="100%" height="100%" border='0' cellpadding='0'
			cellspacing='0'>
			<tr><br />
				<% 
					out.println("Wollen Sie diesen User wirklich l&ouml;schen?<br /><br />"
					+ "<a href=" + request.getContextPath() + "/Dispatcher/deleteUser.do?action=doRemoveUser&id=" + request.getParameter("id") + ">Ja</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" 
					+ "<a href=\"javascript:window.close()\">Abbrechen</a><br />");
				%>
				
				<% if (params.containsKey("userdelete-success")) 
			{
				out.println("Der User wurde erfolgreich gelöscht!");
			} 
			else 
			{
				out.println("");
			}
		%>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>