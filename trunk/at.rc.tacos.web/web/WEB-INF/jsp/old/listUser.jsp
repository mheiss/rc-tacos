<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="at.rc.tacos.model.StaffMember"%>
<%@ page import="at.rc.tacos.web.session.UserSession" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.Date"%>
<%
	Map<String, Object> params = (Map) request.getAttribute("params");
	List<StaffMember> list = (List) params.get("employeeList");
	UserSession userSession = (UserSession) session.getAttribute("userSession");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="at.rc.tacos.common.Constants"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<script type="text/javascript" src="../js/windowActions.js"></script>
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
					<td width="33%" align="left">Willkommen : <%=userSession.getStaffMember().getFirstName() + " "
					+ userSession.getStaffMember().getLastName().replaceAll("�","&auml;").replaceAll("�","&ouml;").replaceAll("�","&uuml;").replaceAll("�","ss")%> &nbsp;&nbsp;( <a
						href="<%=request.getContextPath()+"/Dispatcher/logout.do"%>">logout</a>
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
                                            <table width="200" border="0"> 
                                                 
                                                <% 
                                                    for (StaffMember member : list) { 
                                                %> 
                                                    <tr> 
                                                        <td width="40%" id="BlockContent"><%=member.getFirstName() + " " + member.getLastName().replaceAll("�","&auml;").replaceAll("�","&ouml;").replaceAll("�","&uuml;").replaceAll("�","ss")%></td>
                                                        <td width="50" id="BlockContent"><a href="<%=request.getContextPath()+"/Dispatcher/editUser.do?id="+ member.getStaffMemberId()%>"><img src="../image/b_edit.png" class="hidefocus" /></a></td> 
                                                    </tr> 
                                                    <% 
                                                    } 
                                                %> 
                                            </table> 
                                        </tr> 
                                    </table>
									</td>
								</tr>
							</table>
							<br />
                    <img src='../image/b_edit.png' name='info' alt='Editieren'  />&nbsp;Benutzer&nbsp;editieren
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