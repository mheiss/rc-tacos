<%-- Place here your scriptlets --%>
<%
	String result = (String)request.getAttribute("result");
%>
<!-- The start of the page -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>WebClient - Login</title>
</head>
<body>
<h1> WebClient - login </h1>
<p> Please klick the link below to login to the server </p>
<p> Request to <a href="<%=request.getContextPath()%>/LoginController?action=login"> login </a> to the server </p>
<!--  The result of the query -->
<% if(result != null) { %>
    <p> The response from the server </p>
	<p> <%=result %> </p> 
<% } %>
</body>
</html>