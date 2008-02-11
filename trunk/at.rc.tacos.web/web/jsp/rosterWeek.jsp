<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%@page import="at.rc.tacos.model.RosterEntry"%>
<%@page import="java.util.Calendar"%>
<%@ page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%
    Map<String,Object> params = (Map)request.getAttribute("params");
    List<RosterEntry> rosterList = (List)params.get("rosterList");
    UserSession userSession = (UserSession)session.getAttribute("userSession"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="at.rc.tacos.web.utils.TimetableVertical"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />
<script type="text/javascript" src="../js/windowActions.js"></script>
<title>TACOS :: RK Bruck-Kapfenberg</title>
</head>
<body >
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

<form method="post" action="<%=request.getContextPath()+"/rosterEntryView?action=doRosterEntry"%>" border='0' cellpadding='0' cellspacing='0'>
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
					<td width="33%" align="left"> Willkommen : <%= userSession.getStaffMember().getFirstName()+ " " + userSession.getStaffMember().getLastName().replaceAll("�","&auml;").replaceAll("�","&ouml;").replaceAll("�","&uuml;").replaceAll("�","ss") %>
					&nbsp;&nbsp;( <a href="<%=request.getContextPath()+"/Dispatcher/login.do?action=logout"%>">logout</a>
					)</td>
					<td width="33%" align="center">Wochen&uuml;bersicht ihrer Prim&auml;r-Dienststelle</td>
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
									<% current.add(Calendar.DAY_OF_MONTH, -7);%>
									<a href="<%=getServletContext().getContextPath()%>/Dispatcher/rosterWeek.do?action=weekView&station=primary&startDate=<%=formath.format(current.getTimeInMillis())%>" ><img src="../image/Pfeil_links.jpg" alt="# " width="15" height="15" class="hidefocus" ></a>
									</td><td valign="middle" align="left">
									<%=formath.format(current.getTimeInMillis())%>
									</td>
									<td valign="middle" align="right">
									<% current.add(Calendar.DAY_OF_MONTH, +14);%><%=formath.format(current.getTimeInMillis())%>
									</td><td valign="middle" align="right" width="20" >
									<a href="<%=getServletContext().getContextPath()%>/Dispatcher/rosterWeek.do?action=weekView&station=primary&startDate=<%=formath.format(current.getTimeInMillis())%>" ><img src="../image/Pfeil_rechts.jpg" alt="# "  width="15" height="15" class="hidefocus" ></a>
									</td>
								</tr>
							</table>
                            <table width="100%" border='0' cellpadding='0' cellspacing='0'>
                                <tr>
                                    <td width="50%"><!-- Timetablebox Day -->
                                    <table width="100%" height="100%" border='0' cellpadding='0'
                                        cellspacing='0'>
                                        <tr>  <% 
										  TimetableVertical timetable = new TimetableVertical(getServletContext().getContextPath(),startDate);
	                                      out.print(timetable.calculateTimetable(rosterList, 7));
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
					<img src='../image/info.png' name='info' alt='Info'  />&nbsp;Information&nbsp;&nbsp;|&nbsp;&nbsp;
					<img src='../image/b_edit.png' name='info' alt='Editieren'  />&nbsp;Dienst editieren&nbsp;&nbsp;|&nbsp;&nbsp;
					<img src='../image/b_drop.png' name='info' alt='L&ouml;schen'  />&nbsp;Dienst&nbsp;l&ouml;schen&nbsp;&nbsp;|&nbsp;&nbsp;
                    <img src='../image/rosterArrowRight.jpg' alt='Tages&uuml;bergreifend' />&nbsp;Dienst kann nicht vollst&auml;ndig angezeigt werden (weiterf&uuml;hrend n&auml;chster Tag)&nbsp;&nbsp;|&nbsp;&nbsp;
                    <img src='../image/rosterArrowLeft.jpg' alt='Tages&uuml;bergreifend' />&nbsp;Dienst kann nicht vollst&auml;ndig angezeigt werden (beginnend vorheriger Tag)<br />
                    <img src='../image/tableLegendDriver.jpg' name='info' alt='Fahrer'  />&nbsp;sicherer&nbsp;Einsatzfahrer&nbsp;&nbsp;|&nbsp;&nbsp;
                    <img src='../image/tableLegendParametic.jpg' name='info' alt='Sanitaeter'  />&nbsp;Sanit&auml;ter&nbsp;&nbsp;|&nbsp;&nbsp;
                    <img src='../image/tableLegendEParametic.jpg' name='info' alt='Notfall Sanitaeter'  />&nbsp;Notfall&nbsp;Sanit&auml;ter&nbsp;&nbsp;|&nbsp;&nbsp;
                    <img src='../image/tableLegendDoctor.jpg' name='info' alt='Notfall Arzt'  />&nbsp;Notfall&nbsp;Arzt&nbsp;&nbsp;|&nbsp;&nbsp;
                    <img src='../image/tableLegendOther.jpg' name='info' alt='Sonstige'  />&nbsp;Sonstige
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