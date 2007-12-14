<%@ page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%
	Date current = new Date();
	DateFormat dateformat;
	dateformat = DateFormat.getDateInstance(DateFormat.SHORT,
			Locale.GERMANY);
	UserSession userSession = (UserSession)session.getAttribute("userSession");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />

<title>TACOS :: RK Bruck-Kapfenberg</title>

</head>
<body>

<form method="post" border='0' cellpadding='0' cellspacing='0'>
<table border='0' cellpadding='0' cellspacing='0' width="100%"
	id="MainTab">
    <thead>
        <tr>
            <td>
            <table border='0' cellpadding='0' cellspacing='0' width="100%" id="Tablogo">
             <tr>
                 <td align="left" ><img src="../image/tacos_logo_left.jpg" name="logoLeft" id="logoLeft" /></td>
                 <td align="right" ><img src="../image/tacos_logo_right.jpg" name="logoRight" id="logoRight" /></td>
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
					<td width="50%" align="left"><!-- 
                                <form  method="post" action="login" border='0' cellpadding='0' cellspacing='0' width="200"><input type="submit" name="buttonLogout" value="" id="buttonLogout" /></form>
                                 --> Willkommen : <%= userSession.getUsername()  %>&nbsp;&nbsp;( <a href="<%=request.getContextPath()+"/Dispatcher/login.do?action=logout"%>">logout</a> )</td>
					<td width="50%" align="right">Heute ist der <%=dateformat.format(current)%>
					</td>
				</tr>
			</table>
			<table width="100%">
				<tr>


					<!-- #### LEFT CONTAINER NAVIGATION-->
					<td id="LeftContainerPanel" valign="top"><!-- NAV BLOCK  --> 
					<%@ include file="navigation.jsp" %></td>

					<!-- #### CONTENT -->
					<td id="ContentContainer" valign="top"><!-- CONTENT BLOCK  -->
					<table id="Block" width="100%" border='0' cellpadding='0'
						cellspacing='0'>
						<tr>
							<td id="BlockHead" align="right" valign="center"><b>Ortsstelle:</b>
							<!--  Orstellenliste --> 
							<select name="ortsstelle" id="rosterViewDayHeadSelbox">
								<option value="id">Kapfenberg</option>
								<option value="id">Bruck a. d. Mur</option>
								<option value="id">St. Marein</option>
								<option value="id">Th&ouml;rl</option>
								<option value="id">Turnau</option>
								<option value="id">Breitenau</option>
								<option value="id">NEF</option>
							</select></td>
						</tr>
						<tr>
							<td id="BlockContent">
							<table width="100%" border='0' cellpadding='0' cellspacing='0'>
								<tr>
									<%
										if (request.getParameter("timetableEntryDate") != null) {
											int count = 0;
											while (request.getParameter("timetableEntryDate") != null) {
												count++;
									%>
									<td id="weekday"></td>

									<%
										}
										}
									%>


								</tr>
							</table>
							<table id="weekdayTimetable" width="100%" border='0'
								cellpadding='0' cellspacing='0' style="margin: 2px;">
								<tr>
									<td>Weekday</td>
								</tr>
								<%
									if (request.getParameter("timetableEntry") != null) {

										while (request.getParameter("timetableEntry") != null) {
								%>
								<tr>
									<td><%=request.getParameter("timetableEntry")%></td>
								</tr>

								<%
									}
									}
								%>
							</table>
							</td>
						</tr>
					</table>
					</td>

				</tr>

			</table>
			</td>
		</tr>
	</tbody>
</table>
</form>
</body>
</html>