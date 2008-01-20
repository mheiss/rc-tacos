<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />
<script type="text/javascript" src="../js/windowActions.js"></script>
<title>TACOS :: RK Bruck-Kapfenberg</title>
</head>
<body>
<table width="100%" border='0' cellpadding='0' cellspacing='0'>
	<tr>
		<td width="50%"><!-- Timetablebox Day -->
		<table width="100%" height="100%" border='0' cellpadding='0'
			cellspacing='0'>
			<tr><br />
				<% 
					out.println("Wollen Sie diesen Dienst-Eintrag wirklich l&ouml;schen?<br /><br />"
					+ "<a href=" + request.getContextPath() + "/Dispatcher/deleteEntryPopUp.do?action=doRemoveEntry&id=" + request.getParameter("id") + ">Ja</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "<a href=\"javascript:window.close()\">Abbrechen</a><br />");
				%>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>