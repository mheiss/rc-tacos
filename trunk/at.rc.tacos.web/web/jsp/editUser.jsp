<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="at.rc.tacos.model.StaffMember"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%@page import="at.rc.tacos.util.MyUtils"%>
<%@page import="at.rc.tacos.common.Constants"%>
<%@page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%@page import="at.rc.tacos.model.MobilePhoneDetail"%>
<%@page import="at.rc.tacos.model.Competence"%>
<%
	UserSession userSession = (UserSession) session.getAttribute("userSession");
	Map<String, Object> params = (Map) request.getAttribute("params");
	List<Location> listLocation = userSession.getLocationList();
	StaffMember editStaffMember = (StaffMember) params.get("editStaffMember");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="at.rc.tacos.model.Location"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />
<title>TACOS :: RK Bruck-Kapfenberg</title>
</head>
<body>
<%
	Date current = new Date();
	SimpleDateFormat formath = new SimpleDateFormat("dd.MM.yyyy");
%>
<form method="post" action="" border='0' cellpadding='0' cellspacing='0'>
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
					<td width="33%" align="left">Willkommen : <%=userSession.getStaffMember().getFirstName()
					+ " "
					+ userSession.getStaffMember().getLastName().replaceAll(
							"ä", "&auml;").replaceAll("ö", "&ouml;")
							.replaceAll("ü", "&uuml;").replaceAll("ß", "ss")%> &nbsp;&nbsp;(
					<a
						href="<%=request.getContextPath()+"/Dispatcher/login.do?action=logout"%>">logout</a>
					)</td>
					<td width="33%" align="center">Benutzer editieren</td>
					<td width="33%" align="right">Heute ist der <%=formath.format(current)%>
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
							<table width="100%" border='0' cellpadding='0' cellspacing='0'>
								<tr>
									<td width="50%"><!-- Timetablebox Day -->
									<table width="100%" height="100%" border='0' cellpadding='0'
										cellspacing='0'>
										<tr>
											<table width="682" height="127" border="0">
												<tr>
													<td width="138"><strong>Pers&ouml;nliche
													Daten</strong></td>
													<td width="144">&nbsp;</td>
													<td width="24">&nbsp;</td>
													<td width="74"><strong>Wohnort</strong></td>
													<td width="280">&nbsp;</td>
												</tr>
												<tr>
													<td>Vorname:</td>
													<td><input type="text" name="firstName" id="textfield"
														value="<%=editStaffMember.getFirstName().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>" /></td>
													<td>&nbsp;</td>
													<td>Strasse:</td>
													<td><input type="text" name="streetname"
														id="textfield7"
														value="<%=editStaffMember.getStreetname().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>" /></td>
												</tr>
												<tr>
													<td>Nachname:</td>
													<td><input type="text" name="lastName" id="textfield2"
														value="<%=editStaffMember.getLastName().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>" /></td>
													<td>&nbsp;</td>
													<td>Ort:</td>
													<td><input type="text" name="cityname" id="textfield8"
														value="<%=editStaffMember.getCityname().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>" /></td>
												</tr>
												<tr>
													<td>Geburtsdatum:</td>
													<td><input type="text" name="birthday"
														id="textfield12"
														value="<%=MyUtils.timestampToString(editStaffMember.getBirthday(),MyUtils.dateFormat)%>" /></td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td>K&uuml;rzel:</td>
													<td><input type="text" name="userName" id="textfield2"
														value="<%=editStaffMember.getUserName().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>" /></td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
												</tr>
											</table>
											<table width="682" border="0">
												<tr>
													<td>
													<table width="336" height="133" border="0">
														<tr>
															<td width="111"><strong>Kontakt Daten</strong></td>
															<td width="200">&nbsp;</td>
														</tr>
														<tr>
															<td>E-Mail Adresse:</td>
															<td><input type="text" name="eMail" id="textfield4"
																value="<%=editStaffMember.getEMail() %>" /></td>
														</tr>
															<%
																for (MobilePhoneDetail phone : editStaffMember.getPhonelist()) 
																{
															%>
														<tr>
															<td>Tel. Nr.:</td>
															<td><input type="text" name="phonenumber" id="textfield4" value="<%=phone.getMobilePhoneNumber()%>" /></td>
														</tr>
															<% } %>
													</table>
													</td>
													<td>
													<table width="284" height="71" border="0">
														<tr>
															<td width="146"><strong>Dienst Daten</strong></td>
															<td width="120">&nbsp;</td>
														</tr>
														<tr>
															<td>Prim&auml;re Ortsstelle:</td>
															<td>&nbsp;</td>
														</tr>
														<%
																for (Competence comp : editStaffMember.getCompetenceList()) 
																{
														%>
														<tr>
															<td>Kompetenzen:</td>
															<td>&nbsp;</td>
														</tr>
														<tr>
												        <td>&nbsp;</td>
												        <td><input type="checkbox" name="checkbox" id="checkbox"></td>
												      </tr>
															<% } %>
													</table>
													</td>
												</tr>
											</table>
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