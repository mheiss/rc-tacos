<%@page import="at.rc.tacos.common.Constants"%>
<%@page import="at.rc.tacos.util.MyUtils"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%@ page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%
	UserSession userSession = (UserSession) session.getAttribute("userSession");
	out.println("Location:"  + userSession.getStaffMember().getPrimaryLocation());

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
<%
	Date current = new Date();
	SimpleDateFormat formath = new SimpleDateFormat("dd-MM-yyyy");
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
					<td width="33%" align="left">Willkommen : <%=userSession.getStaffMember().getFirstName() + " "
					+ userSession.getStaffMember().getLastName()%>
					&nbsp;&nbsp;( <a
						href="<%=request.getContextPath()+"/Dispatcher/login.do?action=logout"%>">logout</a>
					)</td>
					<td width="33%" align="center">Profil editieren</td>
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
											<table width="682" height="302" border="0">
												<tr>
													<td><strong>Pers&ouml;nliche Daten</strong></td>
													<td>&nbsp;</td>
													<td><strong>Wohnort</strong></td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td>Vorname:</td>
													<td><input disabled="disabled" type="text" name="firstName" id="textfield" value="<%=userSession.getStaffMember().getFirstName().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>" /></td>
													<td>Strasse:</td>
													<td><input type="text" name="streetname" id="textfield7" value="<%=userSession.getStaffMember().getStreetname().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>"/></td>
												</tr>
												<tr>
													<td>Nachname:</td>
													<td><input type="text" name="lastName" id="textfield2" value="<%=userSession.getStaffMember().getLastName().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>"/></td>
													<td>Ort:</td>
													<td><input type="text" name="cityname" id="textfield8" value="<%=userSession.getStaffMember().getCityname().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>"/></td>
												</tr>
												<tr>
													<td>Geburtsdatum:</td>
													<td><input disabled="disabled" type="text" name="birthday" id="textfield12" value="<%=MyUtils.timestampToString(userSession.getStaffMember().getBirthday(),MyUtils.dateFormat)%>" /></td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td>K&uuml;rzel:</td>
													<td><input type="text" name="lastName" id="textfield2" value="<%=userSession.getStaffMember().getUserName().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>"/></td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td><strong>Kontakt Daten</strong></td>
													<td>&nbsp;</td>
													<td><strong>Dienst Daten</strong></td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td>E-Mail Adresse:</td>
													<td><input type="text" name="eMail" id="textfield4" value="<%=userSession.getStaffMember().getEMail() %>" /></td>
													<td>Prim&auml;re Ortsstelle:</td>
													<td><input disabled="disabled" type="text" name="station" id="textfield4" value=" <!--<%=userSession.getStaffMember().getPrimaryLocation().getLocationName().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss")%>" /></td>
												</tr>
												<tr>
													<td>Tel. Nr.:</td>
													<td><input type="text" name="phonenumber" id="textfield4" value="<%=userSession.getStaffMember().getPhonelist() %>"/></td>
													<td><%=Constants.COMPETENCE_DRIVER%>:</td>
													<td><input type="checkbox" name="checkbox"
														id="checkbox" /></td>
												</tr>
												<tr>
													<td>Tel. Nr.:</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_SANI.replaceAll("ä", "&auml;")%>:</td>
													<td><input type="checkbox" name="checkbox"
														id="checkbox" /></td>
												</tr>
												<tr>
													<td>Tel. Nr.:</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_EXECUTIVE_INSP%>:</td>
													<td><input type="checkbox" name="checkbox"
														id="checkbox" /></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_EXECUTIVE_DF%>:</td>
													<td><input type="checkbox" name="checkbox"
														id="checkbox" /></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_DISPON%>:</td>
													<td><input type="checkbox" name="checkbox"
														id="checkbox" /></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_EMERGENCY.replaceAll("ä", "&auml;")%>:</td>
													<td><input type="checkbox" name="checkbox"
														id="checkbox" /></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_DOCTOR%>:</td>
													<td><input type="checkbox" name="checkbox"
														id="checkbox" /></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_OTHER%>:</td>
													<td><input type="checkbox" name="checkbox"
														id="checkbox" /></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_INTERVENTION%>:</td>
													<td><input type="checkbox" name="checkbox"
														id="checkbox" /></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><input type="submit" name="button" id="button"
														value="Speichern"></td>
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