<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", -1);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="PRAGMA" content="NO-CACHE" />
	<link rel="stylesheet" type="text/css" href="../css/stylesheet.css" />
	<link rel="icon" type="image/x-icon" href="../favicon.ico" />
<title>TACOS :: Seite nicht gefunden</title>
</head>
<body>
<table align="center" width="350" height="350" style="border-width:1px; border-style:solid; border-color:#8F0022; margin-top:10%;">
	<tr><td><img src="../image/pageNotFound.jpg" alt="page not found" /></td></tr>
	<tr><td align="justify"><b>Seite nicht gefunden.<br />Die von Ihnen gesuchte Seite ist uns leider nicht bekannt.</b></td></tr>
	<tr><td align="center"><img src="../image/tacos_logo_6.jpg" alt="page not found" style="margin:15px;"/></td></tr>
</table>
</body>
</html>