<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="at.rc.tacos.model.StaffMember"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%
	Map<String,Object> params = (Map)request.getAttribute("params");
	List<StaffMember> list = (List)params.get("employeeList");
	List<StaffMember> rosterList = (List)params.get("rosterList");
	UserSession userSession = (UserSession)session.getAttribute("userSession"); 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="at.rc.tacos.common.AbstractMessage"%>
<%@page import="at.rc.tacos.model.RosterEntry"%>
<%@page import="at.rc.tacos.model.StaffMember"%>
<%@page import="at.rc.tacos.common.Constants"%>
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

<%@page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>

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
					<td width="33%" align="center">Diensteintrag bearbeiten</td>
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
											<td><!-- Mitarbeiterliste --> <select name="employee"
												id="rosterViewDayHeadSelbox">
												<%
													for (StaffMember member : list) {
												%>
												<option selected value="<%=member.getFirstName() + " " + member.getLastName()%>"><%=member.getFirstName() + " " + member.getLastName()%></option>
												<option value="<%=member.getPersonId()%>"><%=member.getUserName()%></option>
												<%
													}
												%>
											</select></td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline">Bezirk /
											Ortsstelle:&nbsp;</td>
											<td><select name="station" id="rosterViewDayHeadSelbox">
												<option><%=userSession.getStaffMember().getPrimaryLocation()%></option>
												<option><%=Constants.STATION_BEZIRK%></option>
												<option><%=Constants.STATION_BREITENAU%></option>
												<option><%=Constants.STATION_BRUCK%></option>
												<option><%=Constants.STATION_KAPFENBERG%></option>
												<option><%=Constants.STATION_MAREIN%></option>
												<option><%=Constants.STATION_THOERL.replaceAll("ö","&ouml;")%></option>
												<option><%=Constants.STATION_TURNAU%></option>
											</select></td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline">T&auml;tigkeit:&nbsp;</td>
											<td><select name="job" id="rosterViewDayHeadSelbox">
												<option><%=Constants.JOB_DRIVER%></option>
												<option><%=Constants.JOB_SANI.replaceAll("ä","&auml;")%></option>
												<option><%=Constants.JOB_EMERGENCY.replaceAll("ä","&auml;")%></option>
												<option><%=Constants.JOB_DOCTOR%></option>
												<option><%=Constants.JOB_DISPON%></option>
												<option><%=Constants.JOB_DF.replaceAll("ü","&uuml;")%></option>
												<option><%=Constants.JOB_BRKDT%></option>
												<option><%=Constants.JOB_INSP%></option>
												<option><%=Constants.JOB_BKTW_DRIVER%></option>
												<option><%=Constants.JOB_JOURNAL%></option>
												<option><%=Constants.JOB_VOLON.replaceAll("ä","&auml;")%></option>
												<option><%=Constants.JOB_OTHER%></option>
											</select></td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline">Dienstverh&auml;ltniss:&nbsp;</td>
											<td><select name="service" id="rosterViewDayHeadSelbox">
												<option><%=Constants.SERVICE_MAIN%></option>
												<option><%=Constants.SERVICE_VOLUNT%></option>
												<option><%=Constants.SERVICE_ZIVI%></option>
												<option><%=Constants.SERVICE_OTHER%></option>
												<option><%=Constants.SERVICE_TEMP%></option>
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