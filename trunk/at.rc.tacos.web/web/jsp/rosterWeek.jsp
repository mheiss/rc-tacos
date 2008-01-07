<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="at.rc.tacos.model.StaffMember"%>
<%@page import="at.rc.tacos.web.web.UserSession"%>
<%
    Map<String,Object> params = (Map)request.getAttribute("params");
    List<StaffMember> rosterList = (List)params.get("rosterList");
    UserSession userSession = (UserSession)session.getAttribute("userSession"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="at.rc.tacos.common.AbstractMessage"%>
<%@page import="at.rc.tacos.model.RosterEntry"%>
<%@page import="at.rc.tacos.web.utils.Timetable"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" />
<link rel='icon' type="image/x-icon" href="../favicon.ico" />

<title>TACOS :: RK Bruck-Kapfenberg</title>
</head>
<body >
<%@ page import="java.text.*"%>
<%@page import="java.util.Date"%>
<% 
        Date current = new Date();
        SimpleDateFormat formath = new SimpleDateFormat("dd.MM.yyyy");
%>

<form method="post" action="<%=request.getContextPath()+"/rosterEntryView?action=doRosterEntry"%>" border='0' cellpadding='0' cellspacing='0'>
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
                    <td width="50%" align="left"> Willkommen : <%= userSession.getUsername() %>
                    &nbsp;&nbsp;( <a href="<%=request.getContextPath()+"/Dispatcher/login.do?action=logout"%>">logout</a>
                    )</td>
                    <td width="50%" align="right">Heute ist der <%= formath.format(current) %>
                    </td>
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
                                          <% 
                                          //Timetable timetable = Timetable.getInstance();
                                          //out.print(timetable.calculateTimetable(rosterList, 7));
                                          
                                          %>
                                        
                                        </tr>
                                    </table>
                                    </td>
                                </tr>
                            </table>
                            </td>
                        </tr>
                    </table>
                    legende</td>
                </tr>
            </table>
            </td>
        </tr>
    </tbody>
</table>
</form>

</body>
</html>