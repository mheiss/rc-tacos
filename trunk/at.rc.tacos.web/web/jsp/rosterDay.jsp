<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="at.rc.tacos.model.StaffMember"%>
<%
	Map<String,Object> params = (Map)request.getAttribute("params");
	List<StaffMember> list = (List)params.get("employeeList");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/stylesheet.css" rel="stylesheet" /> 
<link rel='icon' type="image/x-icon" href="../favicon.ico" />

<title>TACOS :: RK Bruck-Kapfenberg</title>

<script language="javascript">
    if (AC_FL_RunContent == 0) {
        alert("Diese Seite erfordert die Datei \"AC_RunActiveContent.js\".");
    } else {
        AC_FL_RunContent(
            'codebase', 'http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0',
            'width', '100%',
            'height', '100',
            'src', 'tacos_logo',
            'quality', 'high',
            'pluginspage', 'http://www.macromedia.com/go/getflashplayer',
            'align', 'middle',
            'play', 'true',
            'loop', 'true',
            'scale', 'showall',
            'wmode', 'window',
            'devicefont', 'false',
            'id', 'tacos_logo',
            'bgcolor', '#ffffff',
            'name', 'tacos_logo',
            'menu', 'true',
            'allowFullScreen', 'false',
            'allowScriptAccess','sameDomain',
            'movie', 'tacos_logo',
            'salign', 'lt'
            ); //end AC code
    }
</script>
</head>
<body>

<%@ page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>

<% 


        Date current = new Date();
        DateFormat dateformat;
        dateformat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);

%>

    <form method="post" border='0' cellpadding='0' cellspacing='0'>
        <table border='0' cellpadding='0' cellspacing='0' width="100%" id="MainTab">
            <thead>
                <tr>
                    <td>
                        <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0" width="100%" height="100" id="tacos_logo" align="middle">
                        <param name="allowScriptAccess" value="sameDomain" />
                        <param name="allowFullScreen" value="false" />
                        <param name="movie" value="../image/tacos_logo.swf" />
                        <param name="quality" value="high" />
                        <param name="salign" value="lt" /><param name="bgcolor" value="#ffffff" />  
                            <embed src="../image/tacos_logo.swf" quality="high" salign="lt" bgcolor="#ffffff" width="100%" height="100" name="tacos_logo" align="middle" allowScriptAccess="sameDomain" allowFullScreen="false" type="application/x-shockwave-flash" />
                        </object>
                    </td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td id="MainBodyContent">
                        <table width="100%" id="userInfo">
                            <tr>
                                <td width="50%" align="left">
                                <!-- 
                                <form  method="post" action="login" border='0' cellpadding='0' cellspacing='0' width="200"><input type="submit" name="buttonLogout" value="" id="buttonLogout" /></form>
                                 -->
                                    Willkommen : <%= request.getAttribute("username") %>
                                    &nbsp;&nbsp;( <a href="./login.jsp">logout</a> )
                                </td>
                                <td width="50%" align="right">
                                    Heute ist der <%= dateformat.format(current) %>
                                </td>
                            </tr>
                        </table>
                        <table width="100%">
                        <tr>


<jsp:include page="navigation.jsp" flush="true" />



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
														<td>Tageseintrag</td>
													</tr>
												</table>
												</td>
												<td width="50%">
												<table width="100%" border='0' cellpadding='0' cellspacing='0'
													id="TabAnmeldung">
													<tr>
														<td id="rosterViewDayHeadline2" colpsan="2"><b>Dienstdaten:</b>
														</td>
													</tr>
													<tr>
														<td id="rosterViewDayHeadline">RK-Mitglied:&nbsp;</td>
														<td><!-- Mitarbeiterliste --> <select name="employee"
															id="rosterViewDayHeadSelbox">
															<% for(StaffMember member:list) { %>
															<option value="<%=member.getPersonId()%>"><%=member.getUserName()%></option>
															<% } %>
														</select></td>
													</tr>
													<tr>
														<td id="rosterViewDayHeadline">Ortsstelle:&nbsp;</td>
														<td><select name="ortsstelle" id="rosterViewDayHeadSelbox">
															<option value="id">Kapfenberg</option>
															<option value="id">Bruck a. d. Mur</option>
														</select></td>
								
													</tr>
													<tr>
														<td id="rosterViewDayHeadline">RK-Dienst:&nbsp;</td>
														<td><select name="duty" id="rosterViewDayHeadSelbox">
															<option value="id">RTW Fahrer</option>
															<option value="id">NEF Fahrer</option>
															<option value="id">Santit&auml;ter 1</option>
															<option value="id">Santit&auml;ter 2</option>
															<option value="id">Zivildiener</option>
															<option value="id">Sonstige</option>
														</select></td>
													</tr>
													<tr>
														<td id="rosterViewDayHeadline2" colpsan="2"><b>Dienstzeit:</b>
														</td>
													</tr>
													<tr>
														<td id="rosterViewDayHeadline"><input type="checkbox"
															id="timeTo" /></td>
														<td id="rosterViewDayName">&nbsp;Fr&uuml;h (6-11)</td>
								
								
													</tr>
													<tr>
														<td id="rosterViewDayHeadline"><input type="checkbox"
															id="timeTo" /></td>
														<td id="rosterViewDayName">&nbsp;Tag (7-18)</td>
													</tr>
													<tr>
														<td id="rosterViewDayHeadline"><input type="checkbox"
															id="timeTo" /></td>
														<td id="rosterViewDayName">&nbsp;Nacht(18-7)</td>
													</tr>
													<tr>
														<td id="rosterViewDayHeadline"><input type="checkbox"
															id="timeTo" /></td>
														<td id="rosterViewDayName">&nbsp;Ambulanz (6-14)</td>
													</tr>
													<tr>
														<td colspan="2" align="right" style="padding: 10px;"><input
															type="image" src="../image/button_ok.jpg" id="senden"
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