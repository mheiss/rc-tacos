<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="at.rc.tacos.model.StaffMember"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%
	Map<String, Object> params = (Map) request.getAttribute("params");
	List<RosterEntry> rosterList = (List) params.get("rosterList");
	UserSession userSession = (UserSession) session
			.getAttribute("userSession");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="at.rc.tacos.common.AbstractMessage"%>
<%@page import="at.rc.tacos.model.RosterEntry"%>
<%@page import="at.rc.tacos.web.utils.Timetable"%>
<%@page import="java.util.Calendar"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />
<script type="text/javascript" src="../js/windowActions.js"></script>
<title>TACOS :: RK Bruck-Kapfenberg</title>
</head>
<body>
<%@ page import="java.text.*"%>
<%@ page import="java.util.Date"%>
<%
	Calendar current = Calendar.getInstance();
	SimpleDateFormat formath = new SimpleDateFormat("dd-MM-yyyy");
	DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	Date today = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	//date to show
	String startDate = request.getParameter("startDate");
	//if we have no date, use the current date
	if (startDate == null || startDate.trim().isEmpty())
		startDate = formath.format(current.getTime());

	//current date as calendar
	current.setTime(df.parse(startDate));
%>
<form method="post"
	action="<%=request.getContextPath()+"/rosterEntryView?action=doRosterEntry"%>"
	border='0' cellpadding='0' cellspacing='0'>
<table border='0' cellpadding='0' cellspacing='0' width="100%"
	id="MainTab">
	<thead>
		<tr>
			<td>
			<table border='0' cellpadding='0' cellspacing='0' width="100%"
				id="Tablogo">
				<tr>
					<td align="left"><img src="../image/tacos_logo_left.jpg"
						name="logoLeft" id="logoLeft" /></td>
					<td align="right"><img src="../image/tacos_logo_right.jpg"
						name="logoRight" id="logoRight" /></td>
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
					+ userSession.getStaffMember().getLastName()%>
					&nbsp;&nbsp;( <a
						href="<%=request.getContextPath()+"/Dispatcher/login.do?action=logout"%>">logout</a>
					)</td>
					<td width="33%" align="center">Tages&uuml;bersicht ihrer
					Prim&auml;r-Dienststelle</td>
					<td width="33%" align="right">Heute ist der <%=format.format(today)%>
					</td>
					<td>
				</tr>
			</table>
			<table width="100%">
				<tr>

					<!-- #### LEFT CONTAINER NAVIGATION-->
					<td id="LeftContainerPanel" valign="top"><!-- NAV BLOCK  --><%@ include
						file="navigation.jsp"%></td>
					<!-- #### CONTENT -->

					<td id="ContentContainer" valign="top"><!-- CONTENT BLOCK  -->
					<table id="Block" width="100%" border='0' cellpadding='0'
						cellspacing='0'>
						<tr>
							<td id="BlockHead" align="right" valign="center">&nbsp;</td>
						</tr>
						<tr>
							<td id="BlockContent">
							<table width="100%" border="0">
								<tr>
									<td valign="middle" align="left" width="20" >
									<% current.add(Calendar.DAY_OF_MONTH, -1);%>
									<a href="<%=getServletContext().getContextPath()%>/Dispatcher/rosterDay.do?action=dayView&startDate=<%=formath.format(current.getTimeInMillis())%>" ><img src="../image/Pfeil_links.jpg" alt="# " width="15" height="15" class="hidefocus" ></a>
									</td><td valign="middle" align="left">
									<%=formath.format(current.getTimeInMillis())%>
									</td>
									<td valign="middle" align="right">
									<% current.add(Calendar.DAY_OF_MONTH, +2);%><%=formath.format(current.getTimeInMillis())%>
									</td><td valign="middle" align="right" width="20" >
									<a href="<%=getServletContext().getContextPath()%>/Dispatcher/rosterDay.do?action=dayView&startDate=<%=formath.format(current.getTimeInMillis())%>" ><img src="../image/Pfeil_rechts.jpg" alt="# " width="15" height="15" class="hidefocus" ></a>
									</td>
								</tr>
							</table>

							<table width="100%" border='0' cellpadding='0' cellspacing='0'>
								<tr>
									<td width="50%"><!-- Timetablebox Day -->
									<table width="100%" height="100%" border='0' cellpadding='0'
										cellspacing='0'>
										<tr>
											<%
												Timetable timetable = new Timetable(getServletContext()
														.getContextPath(), startDate);
												out.print(timetable.calculateTimetable(rosterList, 1));
											%>
										</tr>
									</table>
									</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					<br />
					<img src='../image/info.jpg' name='info' alt='Info'  />&nbsp;Information&nbsp;&nbsp;|&nbsp;&nbsp;
                    <img src='../image/loeschen.gif' name='info' alt='Info'  />&nbsp;l&ouml;schen
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