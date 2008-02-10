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
    List<Location> listLocation = userSession.getLocationList();
    List<ServiceType> listServiceType = userSession.getServiceTypeList();
    List<Job> listJob = userSession.getJobList();
    List<Competence> competenceList = userSession.getCompetenceList();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="at.rc.tacos.model.Transport"%>
<%@page import="at.rc.tacos.model.Competence"%>
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
    Date current = new Date();
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
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
                        href="<%=request.getContextPath()+"/Dispatcher/login.do?action=logout"%>">logout</a>
                    )</td>
                    <td width="33%" align="center">Statistik&uuml;bersicht <%=request.getParameter("notice") %></td>
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
                    <table id="Block" width="100%" border='0' cellpadding='0'
                        cellspacing='0'>
                        <tr>
                            <td id="BlockHead" align="right" valign="center">&nbsp;</td>
                        </tr>
                        <tr>
                            <td id="BlockContent">
                                <table width="200" border="0"> 
                                                 
                                                <% 
                                                    for (StaffMember member : list) { 
                                                %> 
                                                    <tr> 
                                                        <td><%=member.getLastName() %></td>
                                                    </tr> 
                                                    <% 
                                                    } 
                                                %> 
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
</body>
</html>