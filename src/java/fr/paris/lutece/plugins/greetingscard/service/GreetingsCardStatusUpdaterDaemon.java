package fr.paris.lutece.plugins.greetingscard.service;

import fr.paris.lutece.plugins.greetingscard.business.GreetingsCard;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardHome;
import fr.paris.lutece.portal.business.template.DatabaseTemplateHome;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;


public class GreetingsCardStatusUpdaterDaemon extends Daemon
{
	private static final String MARK_RECIPIENT_EMAIL = "recipient_email";

	private static final String PROPERTY_DATABASE_TEMPLATE_CARD_RED = "greetingscard.template_card_red";

	private static final String MESSAGE_EMAIL_CARD_RED_SUBJECT = "greetingscard.email_card_red.subject";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run( )
	{
		Plugin plugin = getPlugin( );
		String strDatabaseTemplateKey = AppPropertiesService.getProperty( PROPERTY_DATABASE_TEMPLATE_CARD_RED ); // TODO : ajouter init de l'id du template
		Collection<GreetingsCard> listGreetingsCardRed = GreetingsCardHome.findCardsToSendNotification( plugin );
		for ( GreetingsCard greetingsCard : listGreetingsCardRed )
		{
			sendNotificationToSender( greetingsCard, strDatabaseTemplateKey );

			greetingsCard.setStatus( GreetingsCard.STATUS_RED_NOTIFIED );
			GreetingsCardHome.update( greetingsCard, plugin );
		}
		if ( listGreetingsCardRed != null && listGreetingsCardRed.size( ) > 0 )
		{
			StringBuilder sb = new StringBuilder( );
			sb.append( listGreetingsCardRed.size( ) );
			sb.append( " card(s) have been updated, and their sender notified" );
			this.setLastRunLogs( sb.toString( ) );
		}
		else
		{
			this.setLastRunLogs( "No cards to update" );
		}
	}

	private void sendNotificationToSender( GreetingsCard greetingsCard, String strDatabaseTemplateKey )
	{
		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_RECIPIENT_EMAIL, greetingsCard.getRecipientEmail( ) );

		String strTemplateContent = DatabaseTemplateHome.getTemplateFromKey( strDatabaseTemplateKey );
		HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( strTemplateContent, Locale.getDefault( ), model );
		MailService.sendMailHtml( greetingsCard.getSenderEmail( ), greetingsCard.getSenderName( ), MailService.getNoReplyEmail( ), I18nService.getLocalizedString( MESSAGE_EMAIL_CARD_RED_SUBJECT,
				Locale.getDefault( ) ), template.getHtml( ) );
	}

	private Plugin getPlugin( )
	{
		return PluginService.getPlugin( getPluginName( ) );
	}

}
