package fr.paris.lutece.plugins.greetingscard.service;

import fr.paris.lutece.plugins.greetingscard.business.GreetingsCard;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardHome;
import fr.paris.lutece.portal.business.template.DatabaseTemplateHome;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;


public class GreetingsCardStatusUpdaterDaemon extends Daemon
{
	private static final String MARK_RECIPIENT_EMAIL = "recipient_email";

	private static final String PROPERTY_DATABASE_TEMPLATE_CARD_RED = "greetingscard.template_card_red";

	private static final String PARAMETER_NEXT_AUTO_ARCHIVING = "greetingscard.nextAutoArchiving";
	private static final String PARAMETER_YEAR_NEXT_AUTO_ARCHIVING = "greetingscard.yearNextAutoArchiving";

	private static final String MESSAGE_EMAIL_CARD_RED_SUBJECT = "greetingscard.email_card_red.subject";

	private GreetingsCardService _greetingsCardService = SpringContextService.getBean( GreetingsCardService.beanName );

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run( )
	{
		Plugin plugin = getPlugin( );
		StringBuilder sbResult = new StringBuilder( );

		// read status of greetings card and notifications
		String strDatabaseTemplateKey = AppPropertiesService.getProperty( PROPERTY_DATABASE_TEMPLATE_CARD_RED );
		Collection<GreetingsCard> listGreetingsCardRed = GreetingsCardHome.findCardsToSendNotification( plugin );

		if ( !StringUtils.isEmpty( strDatabaseTemplateKey ) )
		{
			String strTemplateContent = DatabaseTemplateHome.getTemplateFromKey( strDatabaseTemplateKey );
			for ( GreetingsCard greetingsCard : listGreetingsCardRed )
			{
				sendNotificationToSender( greetingsCard, strTemplateContent );

				greetingsCard.setStatus( GreetingsCard.STATUS_RED_NOTIFIED );
				GreetingsCardHome.update( greetingsCard, plugin );
			}
		}
		if ( listGreetingsCardRed != null && listGreetingsCardRed.size( ) > 0 )
		{
			sbResult.append( listGreetingsCardRed.size( ) );
			sbResult.append( " card(s) have been updated, and their sender notified\n" );
		}
		else
		{
			sbResult.append( "No cards to update\n" );
		}

		// Greetings cards automatic archives
		String strDateNextArchiveAuto = DatastoreService.getDataValue( PARAMETER_NEXT_AUTO_ARCHIVING, StringUtils.EMPTY );
		String strYearNextArchiveAuto = DatastoreService.getDataValue( PARAMETER_YEAR_NEXT_AUTO_ARCHIVING, StringUtils.EMPTY );
		Date dateNextArchiving = null;
		DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.SHORT, Locale.FRANCE );
		dateFormat.setLenient( false );

		if ( StringUtils.isNotBlank( strDateNextArchiveAuto ) )
		{
			try
			{
				dateNextArchiving = dateFormat.parse( strDateNextArchiveAuto );
			}
			catch ( ParseException e )
			{
				AppLogService.error( e.getMessage( ), e );
				sbResult.append( "Error : wrong date format !" );
				this.setLastRunLogs( sbResult.toString( ) );
				return;
			}

			// If we must archive greetings cards
			if ( dateNextArchiving.getTime( ) < new Date( ).getTime( ) )
			{
				int nYear = 0;
				if ( StringUtils.isNotBlank( strYearNextArchiveAuto ) )
				{
					try
					{
						nYear = Integer.parseInt( strYearNextArchiveAuto );
					}
					catch ( NumberFormatException e )
					{
						AppLogService.error( e.getMessage( ), e );
						sbResult.append( "Error : wrong number format for date !" );
						this.setLastRunLogs( sbResult.toString( ) );
						return;
					}
				}
				int nNbCardsArchived = _greetingsCardService.archiveGreetingsCards( 0, nYear, null, null, PluginService.getPlugin( GreetingsCardResourceIdService.PLUGIN_NAME ) );
				sbResult.append( nNbCardsArchived + " greetings cards have been archived with the year '" + nYear + "'\n" );
				Calendar calendar = new GregorianCalendar( );
				calendar.setTime( dateNextArchiving );
				calendar.add( Calendar.YEAR, 1 );
				dateNextArchiving = calendar.getTime( );
				strDateNextArchiveAuto = dateFormat.format( dateNextArchiving );
				DatastoreService.setDataValue( PARAMETER_NEXT_AUTO_ARCHIVING, strDateNextArchiveAuto );
				DatastoreService.setDataValue( PARAMETER_YEAR_NEXT_AUTO_ARCHIVING, String.valueOf( nYear + 1 ) );
			}

		}
		this.setLastRunLogs( sbResult.toString( ) );
	}

	private void sendNotificationToSender( GreetingsCard greetingsCard, String strTemplateContent )
	{
		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_RECIPIENT_EMAIL, greetingsCard.getRecipientEmail( ) );

		HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( strTemplateContent, Locale.getDefault( ), model );
		MailService.sendMailHtml( greetingsCard.getSenderEmail( ), greetingsCard.getSenderName( ), MailService.getNoReplyEmail( ), I18nService.getLocalizedString( MESSAGE_EMAIL_CARD_RED_SUBJECT,
				Locale.getDefault( ) ), template.getHtml( ) );
	}

	private Plugin getPlugin( )
	{
		return PluginService.getPlugin( getPluginName( ) );
	}

}
