<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="at.rc.tacos.model.StaffMember"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%@page import="at.rc.tacos.util.MyUtils"%>
<%@page import="at.rc.tacos.common.Constants"%>
<%@ page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%
	Map<String, Object> params = (Map) request.getAttribute("params");
	List<StaffMember> lista = (List) params.get("employeeList");
	UserSession userSession = (UserSession) session.getAttribute("userSession");
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
					<td width="33%" align="left">Willkommen : <%=userSession.getStaffMember().getFirstName() + " "
					+ userSession.getStaffMember().getLastName()%>
					&nbsp;&nbsp;( <a
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
											<table width="682" height="302" border="0">
												<tr>
													<td><strong>Pers&ouml;nliche Daten</strong></td>
													<td>&nbsp;</td>
													<td><strong>Wohnort</strong></td>
													<td>&nbsp;</td>
												</tr>
												<tr><% String id = request.getParameter("id");
												for (StaffMember member : lista) { 
												 %>
													<td>Vorname:</td>
													<td><input type="text" name="firstName" id="textfield" value="<%=member.getFirstName() %>" /></td>
													<td>Strasse:</td>
													<td><input type="text" name="streetname" id="textfield7" value="<%=member.getStreetname() %>"/></td>
												</tr>
												 
												<tr>
													<td>Nachname:</td>
													<td><input type="text" name="lastName" id="textfield2" value="<%=member.getLastName() %>"/></td>
													<td>Ort:</td>
													<td><input type="text" name="cityname" id="textfield8" value="<%=member.getCityname() %>"/></td>
												</tr>
												<tr>
													<td>Gebrtsdatum:</td>
													<td><input type="text" name="birthday" id="textfield12" value="<%=MyUtils.formatDate(member.getBirthday() )%>" /></td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td>K&uuml;rzel:</td>
													<td>&nbsp;</td>
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
													<td><input type="text" name="eMail" id="textfield4" value="<%=member.getEMail() %>" /></td>
													<td>Prim&auml;re Ortsstelle:</td>
													<td><select name="station" id="rosterViewDayHeadSelbox">
														<option><%=member.getPrimaryLocation()%></option>
														<option><%=Constants.STATION_BEZIRK%></option>
														<option><%=Constants.STATION_BREITENAU%></option>
														<option><%=Constants.STATION_BRUCK%></option>
														<option><%=Constants.STATION_KAPFENBERG%></option>
														<option><%=Constants.STATION_MAREIN%></option>
														<option><%=Constants.STATION_THOERL.replaceAll("ö", "&ouml;")%></option>
														<option><%=Constants.STATION_TURNAU%></option>
													</select></td>
												</tr>
												<tr>
													<td>Tel. Nr.:</td>
													<td><input type="text" name="phonenumber" id="textfield4" value="<%=member.getPhonelist().get(0).getMobilePhoneNumber()%>"/></td>
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
  <% 
                                                    } 
                                                %> 

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