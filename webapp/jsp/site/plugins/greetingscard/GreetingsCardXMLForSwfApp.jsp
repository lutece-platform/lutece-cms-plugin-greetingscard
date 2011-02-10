<%@ page errorPage="../../ErrorPagePortal.jsp" %>
<jsp:include page="../../PortalHeader.jsp" />


<jsp:useBean id="greetingscard" scope="session" class="fr.paris.lutece.plugins.greetingscard.web.GreetingsCardJspBean" />

<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	out.print(greetingscard.getGreetingsCardXMLForSwfApp(request));
%>