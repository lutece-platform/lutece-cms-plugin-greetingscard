<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="greetingscardmail" scope="session" class="fr.paris.lutece.plugins.greetingscard.web.GreetingsCardMailJspBean" />


<% greetingscardmail.init( request, greetingscardmail.RIGHT_MANAGE_GREETINGSCARD_MAIL ); %>
<%= greetingscardmail.getManageGreetingsCardMail( request ) %>


<%@ include file="../../AdminFooter.jsp" %>