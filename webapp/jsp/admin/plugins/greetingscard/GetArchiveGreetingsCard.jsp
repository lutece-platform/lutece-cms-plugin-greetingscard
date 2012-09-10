<%@ page errorPage="../../ErrorPage.jsp" %>
<%@ include file="../../AdminHeader.jsp" %>
<jsp:useBean id="greetingscard" scope="session" class="fr.paris.lutece.plugins.greetingscard.web.GreetingsCardJspBean" />
<% greetingscard.init( request, greetingscard.RIGHT_MANAGE_GREETINGSCARD ); %>
<%=	greetingscard.getArchiveGreetingsCard( request ) %>

<%@ include file="../../AdminFooter.jsp" %>