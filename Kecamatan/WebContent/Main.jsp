<%@page import="org.siak.data.Gruppengguna"%>
<%
	Gruppengguna gp = null;
	if(session.getAttribute("hasLogin").equals(null)){
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}
	else{
		gp = (Gruppengguna) session.getAttribute("user");
	}
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    	               "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    	<title>GlassFish JSP Page</title>
    	<script type="text/javascript">
    		var username = "<%=gp.getPengguna().getUsername()%>";
    	</script>
    	
    	<link rel="Stylesheet" type="text/css" href="ExtJS3/css/desktop.css" />
    	<link rel="Stylesheet" type="text/css" href="ExtJS3/css/ext-all.css" />
    	<link rel="Stylesheet" type="text/css" href="App_Themes/CSS/Style.css" />
    	
    	<!-- LIB EXTJS 3-->
    	<script type="text/javascript" src="ExtJS3/ext-base.js"></script>
    	<script type="text/javascript" src="ExtJS3/ext-all.js"></script>
    	
    	<!-- LAYOUT -->
    	<script type="text/javascript" src="ExtJS3/js/StartMenu.js"></script>
	    <script type="text/javascript" src="ExtJS3/js/TaskBar.js"></script>
	    <script type="text/javascript" src="ExtJS3/js/Desktop.js"></script>
	    <script type="text/javascript" src="ExtJS3/js/App.js"></script>
	    <script type="text/javascript" src="ExtJS3/js/Module.js"></script>
	    
	    <script type="text/javascript" src="ExtJS3/desktop.js"></script>
  </head>
  <body>
  	
  	<div id="x-desktop">
        <dl id="x-shortcuts">
        </dl>
    </div>
  
    <div id="ux-taskbar">
        <div id="ux-taskbar-start"></div>
        <div id="ux-taskbuttons-panel"></div>
        <div class="x-clear"></div>
    </div>
  </body>
</html> 
