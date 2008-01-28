<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="at.rc.tacos.model.StaffMember"%>
<%@page import="at.rc.tacos.model.Location"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%@page import="at.rc.tacos.model.ServiceType"%>
<%@page import="at.rc.tacos.model.Job"%>
<%@page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%
	Map<String,Object> params = (Map)request.getAttribute("params");
	UserSession userSession = (UserSession)session.getAttribute("userSession"); 
	List<StaffMember> list = userSession.getStaffList();
	List<Location> lista = userSession.getLocationList();
	List<ServiceType> listServiceType = userSession.getServiceTypeList();
	List<Job> listJob = userSession.getJobList();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link href="../css/calendar_js.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />
<script type="text/javascript" src="../js/calendar_js.js"></script>
<title>TACOS :: RK Bruck-Kapfenberg</title>
</head>
<body>
<%
	Date current = new Date();
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
%>
<form method="post" name="form"
	action="<%=request.getContextPath()+"/Dispatcher/rosterEntry.do?action=doRosterEntry"%>"
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
					<td width="33%" align="left">Willkommen : <%= userSession.getStaffMember().getFirstName()+ " " + userSession.getStaffMember().getLastName() %>
					&nbsp;&nbsp;( <a
						href="<%=request.getContextPath()+"/Dispatcher/login.do?action=logout"%>">logout</a>
					)</td>
					<td width="33%" align="center">Diensteintrag hinzuf&uuml;gen</td>
					<td width="33%" align="right">Heute ist der <%= format.format(current) %>
					</td>
					<td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<!-- #### LEFT CONTAINER NAVIGATION-->
					<td id="LeftContainerPanel" valign="top"><!-- NAV BLOCK  --> <%@ include
						file="navigation.jsp"%></td>
					<!-- #### CONTENT -->
					<td id="ContentContainer" valign="top"><!-- CONTENT BLOCK  -->

					
							<% if (params.containsKey("entry-success")) 
			{
				out.println("<table id=\"Block\" width=\"100%\" border='0' cellpadding='0' cellspacing='0'><tr><td id=\"BlockHead\" align=\"right\" valign=\"center\">&nbsp;</td></tr><tr><td id=\"BlockContent\">"
						+ "<table width=\"100%\" border='0' cellpadding='0' cellspacing='0'>"
						+ "<tr><td width=\"50%\"><!-- quick entry -->"
						+ "<table width=\"100%\" border='1' cellpadding='0' cellspacing='0'><tr><td>"
						+ "<div id='meldungstext'>"+params.get("entry-success")+"</td></tr></table></td></tr></table>"
						+ "</td></tr></table>");
			} 
			else 
			{
				out.println("");
			}
		%>
							
					<table id="Block" width="100%" border='0' cellpadding='0'
						cellspacing='0'>
						<tr>
							<td id="BlockHead" align="right" valign="center">&nbsp;</td>
						</tr>
						<tr>
							<td id="BlockContent">
							<table width="100%" border='0' cellpadding='0' cellspacing='0'>
								<tr>
									<td width="50%">
									<table width="100%" border='0' cellpadding='0' cellspacing='0'
										id="TabAnmeldung">
										<tr>
											<td id="rosterViewDayHeadline2" colpsan="2"><b>Dienstdaten:</b>
											</td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline">Mitglied:&nbsp;</td>
											<td><!-- Mitarbeiterliste --> 
												<select name="employee" id="rosterViewDayHeadSelbox">
												<% for (StaffMember member : list) {
													if(member.equals(userSession.getStaffMember())) { %>
														<option selected="selected" value="<%=userSession.getStaffMember().getStaffMemberId()%>"><%=userSession.getStaffMember().getFirstName() + " " + userSession.getStaffMember().getLastName()%></option>
													<% } else { %>
														<option value="<%=member.getStaffMemberId()%>"><%=member.getFirstName() + " " + member.getLastName()%></option>
													<% } } %>
												</select>
											</td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline">Bezirk /
											Ortsstelle:&nbsp;</td>
											<td><select name="station" id="rosterViewDayHeadSelbox">
											<% for (Location location : lista) {
													if(location.equals(userSession.getStaffMember().getPrimaryLocation())) { %>
												<option selected="selected" value="<%=location.getId()%>"><%=location.getLocationName()%></option>
												<% } else { %>
												<option value="<%=location.getId()%>"><%=location.getLocationName()%></option>
												<% } } %>
											</select></td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline">T&auml;tigkeit:&nbsp;</td>
											<td><select name="job" id="rosterViewDayHeadSelbox">
											<% for (Job job :  listJob) { %>
												<option value="<%=job.getId()%>"><%=job.getJobName()%></option>
											<% } %>
											</select></td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline">Dienstverh&auml;ltniss:&nbsp;</td>
											<td><select name="service" id="rosterViewDayHeadSelbox">
											<% for (ServiceType service :  listServiceType) { %>
												<option value="<%=service.getId()%>"><%=service.getServiceName()%></option>
											<% } %>
											</select></td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline2" colpsan="2"><b>Dienstzeit:</b>
											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td id="rosterViewDayName">
											<table width="75%">
												<tr>
													<td width="50%"><!-- KALENDER --><table border="0" cellpadding="5" style="" cellpadding="0"
	cellspacing="0" id="calTabMain">

<tr><td valign="top" align="center" width="100%"><div id="calendar">Tacos Calendar</div></td></tr>
</table></td>
													<td>gew&auml;hltes Datum: <input type="text"
														disabled="disabled" id="selDateView" /><br />
													<br />
													<table width="162" border="0">
														<tr>
															<td width="64">&nbsp;</td>
															<td width="64">Stunde</td>
															<td width="120">Minute</td>
														</tr>
														<tr>
															<td>von:</td>
															<td><!-- hour --> <select name="startHour"
																id="rosterViewDayHeadSelboxTime">
																<%
                                                    int hb = 0;
                                                    while (hb < 24) {
                                                        hb += 1;
                                                %>
																<option value="<%=hb%>"><%=hb%></option>
																<%
                                                    }
                                                %>
															</select></td>
															<td><!-- minute --> <select name="startMinute"
																id="rosterViewDayHeadSelboxTime">
																<option value="00">00</option>
																<option value="30">30</option>
															</select></td>
														</tr>
														<tr>
															<td>bis:</td>
															<td><!-- hour --> <select name="endHour"
																id="rosterViewDayHeadSelboxTime">
																<%
                                                    int he = 0;
                                                    while (he < 24) {
                                                        he += 1;
                                                %>
																<option value="<%=he%>"><%=he%></option>
																<%
                                                    }
                                                %>
															</select></td>
															<td><!-- minute --> <select name="endMinute"
																id="rosterViewDayHeadSelboxTime">
																<option value="0">00</option>
																<option value="30">30</option>
															</select></td>
														</tr>
													</table>
													</td>
													<td></td>
												</tr>
											</table>
											</td>
										</tr>
										<tr>
											<td colspan="2" align="right" style="padding: 10px;"><input
												type="submit" id="senden" value="" /></td>
										</tr>
									</table>
									</td>
								</tr>
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
<!-- 
startHour, startMinute, startDay, startMonth, startYear
endHour, endMinute, endDay, endMonth, endYear
--> <input type="hidden" name="startDay" value="" /> <input
	type="hidden" name="startMonth" value="" /> <input type="hidden"
	name="startYear" value="" /> <br>
<input type="hidden" name="endDay" value="" /> <input type="hidden"
	name="endMonth" value="" /> <input type="hidden" name="endYear"
	value="" /></form>


</body>
</html>