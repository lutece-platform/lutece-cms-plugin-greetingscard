<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="greetingscard" scope="session" class="fr.paris.lutece.plugins.greetingscard.web.GreetingsCardJspBean" />


<% greetingscard.init( request, greetingscard.RIGHT_MANAGE_GREETINGSCARD ); %>
<%= greetingscard.getGreetingsCardTemplatesList ( request ) %>


<%@ include file="../../AdminFooter.jsp" %>

