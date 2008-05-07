<%@ page import="java.util.Map"%>
<%@ page import="at.rc.tacos.model.StaffMember"%>
<%@ page import="at.rc.tacos.web.session.UserSession" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.Date"%>
<%
	Map<String,Object> params = (Map)request.getAttribute("params");
	UserSession userSession = (UserSession) session.getAttribute("userSession");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="at.rc.tacos.common.Constants"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />
<title>TACOS :: RK Bruck-Kapfenberg</title>
</head>
<body>
<%
	Date current = new Date();
	SimpleDateFormat formath = new SimpleDateFormat("dd-MM-yyyy");
%>

<form method="post" 
    action="<%=request.getContextPath()+"/Dispatcher/addUser.do?action=doAddUser"%>" 
    border='0' cellpadding='0' cellspacing='0'>
<table border='0' cellpadding='0' cellspacing='0' width="100%"
	id="MainTab">
	<thead>
		<tr>
			<td>
			<table border='0' cellpadding='0' cellspacing='0' width="100%" id="Tablogo">
				<tr>
					<td style="background:url(../image/headlogopx.jpg) repeat-x center;"><img src="../image/headlogo.jpg" name="logoLeft" id="logoLeft" /></td>
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
					<td width="33%" align="left"> Willkommen : <%=userSession.getStaffMember().getFirstName()+ " " + userSession.getStaffMember().getLastName().replaceAll("�","&auml;").replaceAll("�","&ouml;").replaceAll("�","&uuml;").replaceAll("�","ss")%>
					&nbsp;&nbsp;( <a href="<%=request.getContextPath()+"/Dispatcher/logout.do"%>">logout</a>
					)</td>
					<td width="33%" align="center">Benutzer anlegen</td>
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
									 <% if (params.containsKey("entry-success")) 
            {
                out.println("<table width=\"100%\" border='0' cellpadding='0' cellspacing='0' style=\"background-color:#4fd138; color:#ffffff; padding-left:10px; padding-top:3px; padding-bottom:3px;\" ><tr><td>"
                        +params.get("entry-success")+"</td></tr></table>");
            } 
            else 
            {
                if(params.containsKey("entry-error")){
                out.println("<table width=\"100%\" border='0' cellpadding='0' cellspacing='0' style=\"background-color:red; color:#ffffff; padding-left:10px; padding-top:3px; padding-bottom:3px;\" ><tr><td>"
                        +params.get("entry-error")+"</td></tr></table>");
                }
            }
        %>
									<table width="100%" height="100%" border='0' cellpadding='0'
										cellspacing='0'>
										<tr>
											<table width="100%"  border="0">
												<tr>
													<td><strong>Pers&ouml;nliche Daten</strong></td>
													<td>&nbsp;</td>
													<td><strong>Wohnort</strong></td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td>Vorname:</td>
													<td><input type="text" name="firstName" id="textfield" /></td>
													<td>Strasse:</td>
													<td><input type="text" name="streetname"
														id="textfield7" /></td>
												</tr>
												<tr>
													<td>Nachname:</td>
													<td><input type="text" name="lastName" id="textfield2" /></td>
													<td>Ort:</td>
													<td><input type="text" name="cityname" id="textfield8" /></td>
												</tr>
												<tr>
													<td valign="center" >Geburtsdatum:</td>
													<td><table ><tr><td>Tag</td><td>Monat</td><td>Jahr</td></tr><tr>
													<td >
													<select name="birthday" id="rosterViewDayHeadSelboxTime" style="width: 50px;">
                                                        <% 
                                                        int d = 0;
                                                        for(d=1; d<=31; d++){ 
                                                        %>
                                                        <option><%=d %></option>
                                                        <%
                                                        }
                                                        %>
                                                        </select>
													</td>
													<td >
													<select name="birthmonth" id="rosterViewDayHeadSelboxTime" style="width: 50px;">
                                                        <% 
                                                        int m = 0;
                                                        for(m=1; m<=12; m++){ 
                                                        %>
                                                        <option><%=m %></option>
                                                        <%
                                                        }
                                                        %>
                                                        </select>
													</td>
													<td >
													<select name="birthyear" id="rosterViewDayHeadSelboxTime" style="width: 70px;">
                                                        <% 
                                                        int y = 0;
                                                        SimpleDateFormat format = new SimpleDateFormat("yyyy");
                                                        String year = format.format(current);
                                                        //current year - 80 years
                                                        for(y=(Integer.valueOf(year).intValue())-80; y<=Integer.valueOf(year).intValue(); y++){
                                                        %>
                                                        <option><%=y %></option>
                                                        <%
                                                        }
                                                        %>
                                                        </select>
													</td>
													</tr></table>
														
                                                    </td>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td>K&uuml;rzel:</td>
													<td>Wird automatisch generiert</td>
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
													<td><input type="text" name="eMail" id="textfield4" /></td>
													<td>Prim&auml;re Ortsstelle:</td>
													<td><select name="station"
														id="rosterViewDayHeadSelbox">
														<option><%=Constants.STATION_BEZIRK%></option>
														<option><%=Constants.STATION_BREITENAU%></option>
														<option><%=Constants.STATION_BRUCK%></option>
														<option><%=Constants.STATION_KAPFENBERG%></option>
														<option><%=Constants.STATION_MAREIN%></option>
														<option><%=Constants.STATION_THOERL.replaceAll("�", "&ouml;")%></option>
														<option><%=Constants.STATION_TURNAU%></option>
													</select></td>
												</tr>
												<tr>
													<td>Tel. Nr.:</td>
													<td><input type="text" name="phonenumber"
														id="textfield4" /></td>
													<td><%=Constants.COMPETENCE_DRIVER%>:</td>
													<td><input type="checkbox" name="competenceDriver"
														id="competenceDriver" value="<%=Constants.COMPETENCE_DRIVER%>"/></td>
												</tr>
												<tr>
													<td>Tel. Nr.:</td>
													<td>-</td>
													<td><%=Constants.COMPETENCE_SANI.replaceAll("�","&auml;")%>:</td>
													<td><input type="checkbox" name="competenceSani"
														id="competenceSani" value="<%=Constants.COMPETENCE_SANI.replaceAll("�","&auml;")%>"/></td>
												</tr>
												<tr>
													<td>Tel. Nr.:</td>
													<td>-</td>
													<td><%=Constants.COMPETENCE_EXECUTIVE_INSP%>:</td>
													<td><input type="checkbox" name="competenceIsp"
														id="competenceIsp" value="<%=Constants.COMPETENCE_EXECUTIVE_INSP%>"/></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_EXECUTIVE_DF%>:</td>
													<td><input type="checkbox" name="competenceDf"
														id="competenceDf" value="<%=Constants.COMPETENCE_EXECUTIVE_DF%>" /></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_DISPON%>:</td>
													<td><input type="checkbox" name="competenceDispon"
														id="competenceDispon" value="<%=Constants.COMPETENCE_DISPON%>"/></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_EMERGENCY.replaceAll("�","&auml;")%>:</td>
													<td><input type="checkbox" name="competenceEmergency"
														id="competenceEmergency" value="<%=Constants.COMPETENCE_EMERGENCY.replaceAll("�","&auml;")%>"/></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_DOCTOR%>:</td>
													<td><input type="checkbox" name="competenceDoctor"
														id="competenceDoctor" value="<%=Constants.COMPETENCE_DOCTOR%>"/></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_OTHER%>:</td>
													<td><input type="checkbox" name="competenceOther"
														id="competenceOther" value="<%=Constants.COMPETENCE_OTHER%>"/></td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><%=Constants.COMPETENCE_INTERVENTION%>:</td>
													<td><input type="checkbox" name="competenceIntervention"
														id="competenceIntervention" value="<%=Constants.COMPETENCE_INTERVENTION%>"/></td>
												</tr>
												
												<tr>
                                            <td colspan="4" align="right" style="padding: 10px;"><input type="submit" id="submitButton" value="Benutzer anlegen" /></td>
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