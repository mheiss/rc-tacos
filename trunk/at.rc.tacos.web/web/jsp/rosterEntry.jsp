<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="at.rc.tacos.model.StaffMember"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%
	Map<String,Object> params = (Map)request.getAttribute("params");
	List<StaffMember> list = (List)params.get("employeeList");
	UserSession userSession = (UserSession)session.getAttribute("userSession"); 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="at.rc.tacos.common.AbstractMessage"%>
<%@page import="at.rc.tacos.model.RosterEntry"%>
<%@page import="at.rc.tacos.common.Constants"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />

<title>TACOS :: RK Bruck-Kapfenberg</title>

</head>
<body>

<%@ page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>

<%
	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
	Date current = new Date();
%>

<form method="post" action="<%=request.getContextPath()+"/Dispatcher/rosterEntry.do?action=doRosterEntry"%>" border='0' cellpadding='0' cellspacing='0'>
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
					<td width="50%" align="left"> Willkommen : <%=userSession.getUsername()%>
					&nbsp;&nbsp;( <a href="<%=request.getContextPath()+"/Dispatcher/login.do?action=logout"%>">logout</a>
					)</td>
					<td width="50%" align="right">Heute ist der <%=format.format(current)%>
					</td>
				</tr>
			</table>
			<table width="100%">
				<tr>

					<!-- #### LEFT CONTAINER NAVIGATION-->
					<td id="LeftContainerPanel" valign="top"><!-- NAV BLOCK  -->
					<%@ include file="navigation.jsp"%></td>
					<!-- #### CONTENT -->
					<td id="ContentContainer" valign="top"><!-- CONTENT BLOCK  -->
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
											<td id="rosterViewDayHeadline">RK-Mitglied:&nbsp;</td>
											<td><!-- Mitarbeiterliste --> <select name="employee" id="rosterViewDayHeadSelbox">
												<%
													for (StaffMember member : list) {
												%>
												<option value="<%=member.getPersonId()%>"><%=member.getUserName()%></option>
												<%
													}
												%>
											</select></td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline">Ortsstelle:&nbsp;</td>
											<td><select name="station" id="rosterViewDayHeadSelbox">
												<option><%=Constants.STATION_BREITENAU%></option>
												<option><%=Constants.STATION_BRUCK%></option>
												<option><%=Constants.STATION_KAPFENBERG%></option>
												<option><%=Constants.STATION_MAREIN%></option>
												<option><%=Constants.STATION_THOERL%></option>
												<option><%=Constants.STATION_TURNAU%></option>
											</select></td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline">RK-Dienst:&nbsp;</td>
											<td><select name="competence" id="rosterViewDayHeadSelbox">
												<option><%=Constants.COMPETENCE_DISPON%></option>
												<option><%=Constants.COMPETENCE_DOCTOR%></option>
												<option><%=Constants.COMPETENCE_DRIVER%></option>
												<option><%=Constants.COMPETENCE_EMERGENCY%></option>
												<option><%=Constants.COMPETENCE_HELPER%></option>
												<option><%=Constants.COMPETENCE_OTHER%></option>
												<option><%=Constants.COMPETENCE_SANI%></option>
											</select></td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline">RK-Dienstverh&auml;ltniss:&nbsp;</td>
											<td><select name="service" id="rosterViewDayHeadSelbox">
												<option><%=Constants.SERVICE_MAIN%></option>
												<option><%=Constants.SERVICE_OTHER%></option>
												<option><%=Constants.SERVICE_VOLUNT%></option>
												<option><%=Constants.SERVICE_ZIVI%></option>
											</select></td>
										</tr>
										<tr>
											<td id="rosterViewDayHeadline2" colpsan="2"><b>Dienstzeit:</b>
											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td id="rosterViewDayName">
											<!-- VON -->&nbsp;von:&nbsp;
											<!-- hour --> <select name="startHour" id="startHour">
												<option value="leer" selected>Std.</option>
												<%
													int hb = 0;
													while (hb < 24) {
														hb += 1;
												%>
												<option value="<%=hb%>"><%=hb%></option>
												<%
													}
												%>
											</select> 
											<!-- minute --> <select name="startMinute" id="rosterViewDayHeadSelboxTime">
												<option value="leer" selected>Min.</option>
												<option value="0" selected>0</option>
												<%
													int mb = 0;
													while (mb < 55) {
														mb += 5;
												%>
												<option value="<%=mb%>"><%=mb%></option>
												<%
													}
												%>
											</select> 
											<!-- day --> <select name="startDay" id="rosterViewDayHeadSelboxTime">
												<option value="leer" selected>Tag</option>
												<%
													int tb = 00;
													while (tb < 31) {
														tb += 1;
												%>
												<option value="<%=tb%>"><%=tb%></option>
												<%
													}
												%>
											</select> 
											 <!-- month --> <select name="startMonth" id="rosterViewDayHeadSelboxTime">
												<option value="leer" selected>Monat</option>
												<%
													int mob = 0;
													while (mob < 12) {
														mob += 1;
												%>
												<option value="<%=mob%>"><%=mob%></option>
												<%
													}
												%>
											</select> 
											 <!-- year --> <select name="startYear" id="rosterViewDayHeadSelboxTime">
												<option value="leer" selected>Jahr</option>
												<%
													int yb = 2006;
													while (yb < 2012) {
														yb += 1;
												%>
												<option value="<%=yb%>"><%=yb%></option>
												<%
													}
												%>
											</select> 
											<br />
											<!-- BIS -->&nbsp;bis:&nbsp;&nbsp;
											<!-- hour --> <select name="endHour" id="rosterViewDayHeadSelboxTime">
												<option value="leer" selected>Std.</option>
												<%
													int he = 0;
													while (he < 24) {
														he += 1;
												%>
												<option value="<%=he%>"><%=he%></option>
												<%
													}
												%>
											</select>
											<!-- minute --> <select name="endMinute" id="rosterViewDayHeadSelboxTime">
												<option value="leer" selected>Min.</option>
												<option value="0" selected>0</option>
												<%
													int me = 0;
													while (me < 55) {
														me += 5;
												%>
												<option value="<%=me%>"><%=me%></option>
												<%
													}
												%>
											</select>
											<!-- day --> <select name="endDay" id="rosterViewDayHeadSelboxTime">
												<option value="leer" selected>Tag</option>
												<%
													int te = 00;
													while (te < 31) {
														te += 1;
												%>
												<option value="<%=te%>"><%=te%></option>
												<%
													}
												%>
											</select> 
											 <!-- month --> <select name="endMonth" id="rosterViewDayHeadSelboxTime">
												<option value="leer" selected>Monat</option>
												<%
													int moe = 0;
													while (moe < 12) {
														moe += 1;
												%>
												<option value="<%=moe%>"><%=moe%></option>
												<%
													}
												%>
											</select>
											<!-- year --> <select name="endYear" id="rosterViewDayHeadSelboxTime">
												<option value="leer" selected>Jahr</option>
												<%
													int ye = 2006;
													while (ye < 2012) {
														ye += 1;
												%>
												<option value="<%=ye%>"><%=ye%></option>
												<%
													}
												%>
											</select> </td>
										</tr>
										<tr>
											<td colspan="2" align="right" style="padding: 10px;"><input
												type="submit" src="../image/button_ok.jpg" id="senden"
												value="anmelden" id="buttonOk" /></td>
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
</form>
</body>
</html>