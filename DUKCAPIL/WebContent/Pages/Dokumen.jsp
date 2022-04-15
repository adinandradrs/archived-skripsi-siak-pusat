<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Insert title here</title>
	<!-- EXT JS -->
	<link rel="Stylesheet" type="text/css" href="/DUKCAPIL/ExtJS4/resources/css/CheckHeader.css" />
	<link rel="Stylesheet" type="text/css" href="/DUKCAPIL/ExtJS4/resources/css/GridFilters.css" />
   	<link rel="Stylesheet" type="text/css" href="/DUKCAPIL/ExtJS4/resources/css/ext-all.css" />
   	<script type="text/javascript" src="/DUKCAPIL/ExtJS4/ext-all.js"></script>
   	<script type="text/javascript" src="/DUKCAPIL/ExtJS4/ext-all-debug.js"></script>
   	<script type="text/javascript" src="/DUKCAPIL/ExtJS4/pluggin/CheckColumn.js"></script>
   	<script type="text/javascript" src="/DUKCAPIL/ExtJS4/pluggin/RowExpander.js"></script>
   	<!-- EXTERNAL CSS -->
   	<link rel="Stylesheet" type="text/css" href="/DUKCAPIL/App_Themes/CSS/Style.css" />
   	<!-- MODUL -->
   	<script type="text/javascript" src="/DUKCAPIL/MyScripts/Loader.js"></script>
   	<script type="text/javascript" src="/DUKCAPIL/MyScripts/JenisCetak.js"></script>
   	<%
   		String jenisDokumen = request.getParameter("jenisDokumen");
		if(jenisDokumen.equalsIgnoreCase("aktaKelahiran")){
	%>
   			<script type="text/javascript" src="/DUKCAPIL/MyScripts/Modul/AktaKelahiran.js"></script>
   	<%				
   		}
   		else if(jenisDokumen.equalsIgnoreCase("aktaNikah")){
 	%>
   			<script type="text/javascript" src="/DUKCAPIL/MyScripts/Modul/AktaNikah.js"></script>
   	<%		
   		}
   		else if(jenisDokumen.equalsIgnoreCase("aktaKematian")){
   	%>
   			<script type="text/javascript" src="/DUKCAPIL/MyScripts/Modul/AktaKematian.js"></script>
   	<%		
   		}
   	%>
</head>
<body>
</body>
</html>