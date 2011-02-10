<%@ page errorPage="../../ErrorPage.jsp" %>


<jsp:useBean id="greetingscard" scope="session" class="fr.paris.lutece.plugins.greetingscard.web.GreetingsCardJspBean" />


<% greetingscard.init( request, greetingscard.RIGHT_MANAGE_GREETINGSCARD );
 response.sendRedirect( greetingscard.doRemoveGreetingsCardTemplate( request ) );%>



