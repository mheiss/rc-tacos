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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="at.rc.tacos.model.Transport"%>
<%@page import="at.rc.tacos.model.Competence"%>
<%@page import="at.rc.tacos.model.RosterEntry"%>
<%@page import="at.rc.tacos.common.AbstractMessage"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />
<script type="text/javascript" src="../js/calendar_js.js"></script>
<script type="text/javascript" src="../js/windowActions.js"></script>
<script type="text/javascript" src="../js/util.js"></script>
<title>TACOS :: RK Bruck-Kapfenberg</title>
</head>
<body>
<%
    String[] monthName = {"Januar", "Februar", "M&auml;rz", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
String splitDatVal[] = null;
	Calendar current = Calendar.getInstance();
	SimpleDateFormat formath = new SimpleDateFormat("dd-MM-yyyy");
	DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	DateFormat dfMonth = new SimpleDateFormat("MM");
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
                    <td width="33%" align="left">Willkommen : <%= userSession.getStaffMember().getFirstName()+ " " + userSession.getStaffMember().getLastName().replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("ü","&uuml;").replaceAll("ß","ss") %>
                    &nbsp;&nbsp;( <a
                        href="<%=request.getContextPath()+"/Dispatcher/logout.do"%>">logout</a>
                    )</td>
                    <td width="33%" align="center">Statistik&uuml;bersicht <%=request.getParameter("notice") %></td>
                    <td width="33%" align="right">Heute ist der <%=format.format(today)%>
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
                                    <% current.add(Calendar.MONTH, -2); 
                                       splitDatVal = null;
                                       splitDatVal = (df.format(current.getTimeInMillis())).split("-");
                                    %>
                                    <a href="<%=getServletContext().getContextPath()%>/Dispatcher/statisticEmployee.do?action=doEmployeeStat&notice=Mitarbeiter&startDate=<%=formath.format(current.getTimeInMillis())%>" ><img src="../image/Pfeil_links.jpg" alt="# " width="15" height="15" class="hidefocus" ></a>
                                    </td><td valign="middle" align="left">
                                    <% 
                                       current.add(Calendar.MONTH, +1);
                                       splitDatVal = null;
                                       splitDatVal = (df.format(current.getTimeInMillis())).split("-");
                                    %>
                                    <%=monthName[Integer.parseInt(splitDatVal[1])-1]%>
                                    </td>
                                    <td valign="middle" align="right">
                                    <% current.add(Calendar.MONTH, +2);
                                       splitDatVal = null;
                                       splitDatVal = (df.format(current.getTimeInMillis())).split("-");
                                    %><%=monthName[Integer.parseInt(splitDatVal[1])-1]%>
                                    </td><td valign="middle" align="right" width="20" >
                                    <a href="<%=getServletContext().getContextPath()%>/Dispatcher/statisticEmployee.do?action=doEmployeeStat&notice=Mitarbeiter&startDate=<%=formath.format(current.getTimeInMillis())%>" ><img src="../image/Pfeil_rechts.jpg" alt="# "  width="15" height="15" class="hidefocus" ></a>
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" border="0" style="border-width:1px; border-style:solid; border-color:#cccccc;">
                                <tr>
                                   
                                    <td align="center"><b>
                                       <% 
                                          current.add(Calendar.MONTH, -1); 
                                          splitDatVal = null;
                                          splitDatVal = (df.format(current.getTimeInMillis())).split("-");
                                       %>
                                       <%=monthName[Integer.parseInt(splitDatVal[1])-1]%>
                                    </b></td>
                                    
                                </tr>
                            </table>
                                <table width="100%" border="0"> <tr><td>
                                    
                                
                                </td></tr></table> 
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
</body>
</html>