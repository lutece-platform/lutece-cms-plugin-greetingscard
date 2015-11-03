/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.greetingscard.web;

import fr.paris.lutece.plugins.greetingscard.business.GreetingsCard;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardHome;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardTemplate;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardTemplateHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.captcha.CaptchaSecurityService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.filesystem.DirectoryNotFoundException;
import fr.paris.lutece.util.filesystem.UploadUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * This class implements the HelpDesk XPage.
 */
public class GreetingsCardApp implements XPageApplication
{
	private static final String TEMPLATE_VIEW_FLASH_GREETINGS_CARD = "greetingscard/view_flash_greetings_card.html";
	private static final String TEMPLATE_CREATE_FLASH_GREETINGS_CARD = "greetingscard/create_flash_greetings_card.html";
	private static final String TEMPLATE_PASSWORD_GREETINGS_CARD_TEMPLATE = "greetingscard/password_greetings_card_template.html";
	private static final String MARK_PLUGIN_NAME = "plugin_name";
	private static final String MARK_PICTURE_CARD = "picture_card";
	private static final String MARK_PORTAL_URL = "portal_url";
	private static final String MARK_GC_ID = "gc_id";
	private static final String MARK_GCT_ID = "gct_id";
	private static final String MARK_FORMAT = "format";
	private static final String MARK_HEIGHT = "height";
	private static final String MARK_WIDTH = "width";
	private static final String MARK_GREETINGS_CARD_TEMPLATES_PATH = "greetings_card_templates_path";
	private static final String MARK_GREETINGS_CARD_TEMPLATE_DIR_NAME = "greetings_card_template_dir_name";
	private static final String MARK_VIEW_HTML_CARD_FROM_INTRANET = "view_html_card_from_intranet";
	private static final String MARK_VIEW_HTML_CARD_FROM_INTERNET = "view_html_card_from_internet";
	private static final String MARK_VIEW_FLASH_CARD_FROM_INTRANET = "view_flash_card_from_intranet";
	private static final String MARK_VIEW_FLASH_CARD_FROM_INTERNET = "view_flash_card_from_internet";
	private static final String MARK_MESSAGE = "message";
	private static final String MARK_MESSAGE2 = "message2";
	private static final String MARK_MAIL_COPY = "mail_copy";
	private static final String MARK_SENDER_NAME = "sender_name";
	private static final String MARK_SENDER_EMAIL = "sender_email";
	private static final String MARK_EMAIL_SENDED = "sended_email";
	private static final String MARK_WEBAPP_URL = "webapp_url";
	private static final String MARK_LOCALE = "locale";
	private static final String PARAM_ACTION = "action";
	private static final String PARAM_FORMAT = "format";
	private static final String PARAM_GREETINGS_CARD_ID = "gc_id";
	private static final String PARAM_GREETINGS_CARD_TEMPLATE_ID = "gct_id";
	private static final String PARAM_MESSAGE = "message";
	private static final String PARAM_MESSAGE2 = "message2";
	private static final String PARAM_MAIL_COPY = "mail_copy";
	private static final String PARAM_SENDER_EMAIL = "sender_email";
	private static final String PARAM_RECIPIENT_EMAIL = "recipient_email";
	private static final String PARAM_SENDER_NAME = "sender_name";
	private static final String PARAM_PASSWORD = "password";
	private static final String PARAM_NOTIFY_USER = "notify_user";
	private static final String PARAM_VIEW_HTML_CARD = "view_html_card";
	private static final String PARAM_MAIL_CARD = "mail_card";
	private static final String PARAM_CREATE_HTML_CARD = "create_html_card";
	private static final String PARAM_PAGE = "page";
	private static final String PROPERTY_GREETINGS_CARD_PATH_LABEL = "greetingscard.pagePathLabel";
	private static final String PROPERTY_PATH_GREETINGS_CARD_TEMPLATES = "greetingscard.path.greetingscardtemplates";
	private static final String PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME = "greetingscard.path.greetingscardtemplatedirname";
	private static final String PROPERTY_PATH_GREETINGS_CARD_TEMPLATES_FLASH = "path.lutece.plugins";
	private static final String PROPERTY_MESSAGE_EMAIL_SENDED = "greetingscard.front.sentEmail";
	private static final String PROPERTY_LUTECE_PROD_URL = "lutece.prod.url";
	private static final String PROPERTY_FORMAT_HTML = "html";
	private static final String PROPERTY_FORMAT_FLASH = "flash";
	private static final String PROPERTY_ACTION_CREATE = "create";
	private static final String PROPERTY_ACTION_VIEW = "view";
	private static final String PROPERTY_NO_PASSWORD_ERROR_MESSAGE = "greetingscard.siteMessage.no_password.message";
	private static final String PROPERTY_NO_PASSWORD_TITLE_MESSAGE = "greetingscard.siteMessage.no_password.title";
	private static final String PROPERTY_BAD_PASSWORD_ERROR_MESSAGE = "greetingscard.siteMessage.bad_password.message";
	private static final String PROPERTY_NO_GREETINGS_CARD_TEMPLATE_ERROR_MESSAGE = "greetingscard.siteMessage.no_greetingscard_template.message";
	private static final String PROPERTY_NO_GREETINGS_CARD_TEMPLATE_TITLE_MESSAGE = "greetingscard.siteMessage.no_greetingscard_template.title";
	private static final String PROPERTY_INVALID_PARAMETERS_ERROR_MESSAGE = "greetingscard.siteMessage.invalid_parameters.message";
	private static final String PROPERTY_NO_GREETINGS_CARD_ERROR_MESSAGE = "greetingscard.siteMessage.no_cards.message";
	private static final String PROPERTY_CHAIN_MAIL_MALFORMED = "greetingscard.siteMessage.error.mail";
	private static final String PROPERTY_ERROR_MESSAGE = "greetingscard.siteMessage.error.title";
	private static final String PROPERTY_COPY_OF = "greetingscard.label.copyof";
	private static final String ADDING_MAILING_LIST_REC_MESSAGE = "greetingscard.mainling_list_recipient.message";
	private static final String CHECKBOX_ON = "on";
	private static final String POINT_HTML = ".html";
	private static final String HTML = "html";
	private static final String EMPTY_STRING = "";
	private static final String WHITE_SPACE = " ";
	private static final String PATH_SEPARATOR = "/";
	private static final String ACTION_NAME_SEND = "send";
	private static final String UNDERSCORE = "_";
	private static final String SPACE = " ";
	private static final String HTML_BR = "<br>";
	private static final String HTML_SUBSTITUTE_BR = "\r\n";
	private static final String SEPARATOR_MAIL_LIST = AppPropertiesService.getProperty( "mail.list.separator" );

	private static final String MARK_CAPTCHA = "captcha";
	private static final String MARK_IS_ACTIVE_CAPTCHA = "is_active_captcha";
	private static final String MARK_MYLUTECE_USER = "mylutece_user";
	private static final String JCAPTCHA_PLUGIN = "jcaptcha";

	// Captcha
	private CaptchaSecurityService _captchaService;

	/**
	 * Creates a new QuizPage object.
	 */
	public GreetingsCardApp( )
	{
	}

	/**
	 * Returns the Greetingscard XPage content depending on the request parameters and the current mode.
	 * 
	 * @param request The HTTP request.
	 * @param nMode The current mode.
	 * @param plugin The plugin.
	 * @return The page content.
	 * @throws SiteMessageException occurs when a site message need to be displayed
	 */
	public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin ) throws SiteMessageException
	{
		XPage page = new XPage( );
		String strAction = request.getParameter( PARAM_ACTION );
		String strFormat = request.getParameter( PARAM_FORMAT );

		String strBaseUrl = AppPathService.getBaseUrl( request );

		page.setPathLabel( I18nService.getLocalizedString( PROPERTY_GREETINGS_CARD_PATH_LABEL, request.getLocale( ) ) );
		page.setTitle( I18nService.getLocalizedString( PROPERTY_GREETINGS_CARD_PATH_LABEL, request.getLocale( ) ) );

		if ( strAction != null )
		{
			if ( strAction.equals( PROPERTY_ACTION_VIEW ) )
			{
				String strIdGC = request.getParameter( PARAM_GREETINGS_CARD_ID );

				if ( GreetingsCardHome.findByPrimaryKey( strIdGC, plugin ) == null )
				{
					SiteMessageService.setMessage( request, PROPERTY_NO_GREETINGS_CARD_ERROR_MESSAGE, null, PROPERTY_ERROR_MESSAGE, null, null, SiteMessage.TYPE_INFO );
				}
				else
				{
					GreetingsCard greetingsCard = GreetingsCardHome.findByPrimaryKey( strIdGC, plugin );

					if ( !greetingsCard.isCopy( ) )
					{
						greetingsCard.setStatus( GreetingsCard.STATUS_RED );
					}

					GreetingsCardHome.update( greetingsCard, plugin );

					if ( ( strFormat != null ) && ( strIdGC != null ) )
					{
						if ( strFormat.equals( PROPERTY_FORMAT_HTML ) )
						{
							try
							{
								page.setContent( getViewHTMLGreetingsCard( request, strIdGC, plugin ) );
							}
							catch ( DirectoryNotFoundException e )
							{
								throw new AppException( e.getMessage( ), e );
							}
						}
						else if ( strFormat.equals( PROPERTY_FORMAT_FLASH ) )
						{
							page.setContent( getViewFlashGreetingsCard( request, strIdGC, plugin ) );
						}
					}
				}
			}
			else if ( strAction.equals( PROPERTY_ACTION_CREATE ) )
			{
				String strIdGCT = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
				GreetingsCardTemplate greetingsCardTemplate;

				if ( strIdGCT != null )
				{
					int nIdGCT = Integer.parseInt( strIdGCT );
					greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( nIdGCT, plugin );

					if ( ( greetingsCardTemplate == null ) || !greetingsCardTemplate.isEnabled( ) )
					{
						SiteMessageService.setMessage( request, PROPERTY_NO_GREETINGS_CARD_TEMPLATE_ERROR_MESSAGE, null, PROPERTY_NO_GREETINGS_CARD_TEMPLATE_TITLE_MESSAGE, null, null,
								SiteMessage.TYPE_WARNING );
					}
					else
					{
						greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( nIdGCT, plugin );

						String strPassword = request.getParameter( PARAM_PASSWORD );

						if ( ( greetingsCardTemplate.getPassword( ) != null ) && !greetingsCardTemplate.getPassword( ).equals( strPassword ) )
						{
							// If the password is empty
							if ( ( strPassword != null ) && strPassword.equals( EMPTY_STRING ) )
							{
								SiteMessageService.setMessage( request, PROPERTY_NO_PASSWORD_ERROR_MESSAGE, null, PROPERTY_NO_PASSWORD_TITLE_MESSAGE, null, null, SiteMessage.TYPE_STOP );
							}

							if ( ( strPassword != null ) && !greetingsCardTemplate.getPassword( ).equals( strPassword ) )
							{
								SiteMessageService.setMessage( request, PROPERTY_BAD_PASSWORD_ERROR_MESSAGE, null, PROPERTY_NO_PASSWORD_TITLE_MESSAGE, null, null, SiteMessage.TYPE_STOP );
							}

							page.setContent( getGreetingsCardTemplatePasswordForm( request ) );
						}
						else
						{
							if ( strFormat != null )
							{
								if ( strFormat.equals( PROPERTY_FORMAT_HTML ) )
								{
									try
									{
										page.setContent( getCreateHTMLGreetingsCard( request, plugin ) );
									}
									catch ( DirectoryNotFoundException e )
									{
										throw new AppException( e.getMessage( ), e );
									}
								}
								else if ( strFormat.equals( PROPERTY_FORMAT_FLASH ) )
								{
									page.setContent( getCreateFlashGreetingsCard( request, plugin ) );
								}
							}
						}
					}
				}
			}
			else if ( strAction.equals( ACTION_NAME_SEND ) )
			{
				try
				{
					page.setContent( doSendGreetingsCard( request, plugin, strBaseUrl ) );
				}
				catch ( DirectoryNotFoundException e )
				{
					throw new AppException( e.getMessage( ), e );
				}
				catch ( AccessDeniedException ae )
				{
					throw new AppException( ae.getMessage( ), ae );
				}
			}
		}

		if ( page.getContent( ) == null )
		{
			SiteMessageService.setMessage( request, PROPERTY_INVALID_PARAMETERS_ERROR_MESSAGE, null, PROPERTY_NO_GREETINGS_CARD_TEMPLATE_TITLE_MESSAGE, null, null, SiteMessage.TYPE_WARNING );
		}

		return page;
	}

	/**
	 * Returns an HTML format greetings card
	 * @param request the HTTPServletRequest
	 * @param strIdGC The string
	 * @param plugin The plugin
	 * @return The Html template
	 * @throws DirectoryNotFoundException exception for DirectoryNotFoundException
	 */
	private String getViewHTMLGreetingsCard( HttpServletRequest request, String strIdGC, Plugin plugin ) throws DirectoryNotFoundException
	{
		GreetingsCard greetingsCard = GreetingsCardHome.findByPrimaryKey( strIdGC, plugin );

		// Load the parameters of the greetings card plugin
		String strPathGreetingsCardTemplateDirName = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME );
		String strPathGreetingsCardTemplatesRelative = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATES );

		String strNewDirectoryName = strPathGreetingsCardTemplateDirName + greetingsCard.getIdGCT( );
		strNewDirectoryName = UploadUtil.cleanFileName( strNewDirectoryName );

		String strPicture = GreetingsCardTemplateHome.getPicture( strNewDirectoryName, plugin.getName( ) );
		GreetingsCardTemplate greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( greetingsCard.getIdGCT( ), plugin );

		String strPathPictureCard = strPathGreetingsCardTemplatesRelative + PATH_SEPARATOR + strPathGreetingsCardTemplateDirName + greetingsCard.getIdGCT( ) + PATH_SEPARATOR + strPicture;

		HashMap<String, Object> model = new HashMap<String, Object>( );

		// Captcha
		boolean bIsCaptchaEnabled = PluginService.isPluginEnable( JCAPTCHA_PLUGIN );
		model.put( MARK_IS_ACTIVE_CAPTCHA, bIsCaptchaEnabled );

		model.put( MARK_MESSAGE, greetingsCard.getMessage( ) );
		model.put( MARK_MESSAGE2, greetingsCard.getMessage2( ) );
		model.put( MARK_SENDER_NAME, greetingsCard.getSenderName( ) );
		model.put( MARK_SENDER_EMAIL, greetingsCard.getSenderEmail( ) );
		model.put( MARK_HEIGHT, greetingsCardTemplate.getHeight( ) );
		model.put( MARK_WIDTH, greetingsCardTemplate.getWidth( ) );
		model.put( MARK_PICTURE_CARD, strPathPictureCard );

		HtmlTemplate t = getLocaleTemplate( strNewDirectoryName + PATH_SEPARATOR + PARAM_VIEW_HTML_CARD + UNDERSCORE + greetingsCard.getIdGCT( ) + POINT_HTML, request.getLocale( ), model, 0 );

		return t.getHtml( );
	}

	/**
	 * Returns an HTML format greetings card
	 * @param request The HTTPServletRequest
	 * @param strIdGC The send card identifier
	 * @param plugin The plugin
	 * @return The Html template
	 */
	private String getViewFlashGreetingsCard( HttpServletRequest request, String strIdGC, Plugin plugin )
	{
		GreetingsCard greetingsCard = GreetingsCardHome.findByPrimaryKey( strIdGC, plugin );
		GreetingsCardTemplate greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( greetingsCard.getIdGCT( ), plugin );

		// Load the parameters of the greetings card plugin
		String strPathGreetingsCardTemplates = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATES );
		String strPathGreetingsCardTemplateDirName = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME );

		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_GCT_ID, greetingsCard.getIdGCT( ) );
		model.put( MARK_GC_ID, greetingsCard.getId( ) );
		model.put( MARK_WIDTH, greetingsCardTemplate.getWidth( ) );
		model.put( MARK_HEIGHT, greetingsCardTemplate.getHeight( ) );
		model.put( MARK_GREETINGS_CARD_TEMPLATES_PATH, strPathGreetingsCardTemplates );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_DIR_NAME, strPathGreetingsCardTemplateDirName );

		HtmlTemplate t = getLocaleTemplate( TEMPLATE_VIEW_FLASH_GREETINGS_CARD, request.getLocale( ), model, 1 );

		return t.getHtml( );
	}

	/**
	 * Returns an HTML format greetings card
	 * @param request The request
	 * @param plugin The plugin
	 * @return The Html template
	 */
	private String getCreateFlashGreetingsCard( HttpServletRequest request, Plugin plugin )
	{
		String strIdGCT = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
		int nIdGCT = Integer.parseInt( strIdGCT );

		GreetingsCardTemplate greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( nIdGCT, plugin );

		// Load the parameters of the greetings card plugin
		String strPathGreetingsCardTemplates = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATES );
		String strPathGreetingsCardTemplateDirName = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME );

		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_GCT_ID, strIdGCT );
		model.put( MARK_WIDTH, greetingsCardTemplate.getWidth( ) );
		model.put( MARK_HEIGHT, greetingsCardTemplate.getHeight( ) );
		model.put( MARK_GREETINGS_CARD_TEMPLATES_PATH, strPathGreetingsCardTemplates );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_DIR_NAME, strPathGreetingsCardTemplateDirName );

		HtmlTemplate t = getLocaleTemplate( TEMPLATE_CREATE_FLASH_GREETINGS_CARD, request.getLocale( ), model, 1 );

		return t.getHtml( );
	}

	/**
	 * Returns the greetings card template password form
	 * @param request The request
	 * @return The Html template
	 */
	private String getGreetingsCardTemplatePasswordForm( HttpServletRequest request )
	{
		String strIdGCT = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
		String strFormat = request.getParameter( PARAM_FORMAT );

		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_GCT_ID, strIdGCT );
		model.put( MARK_FORMAT, strFormat );

		HtmlTemplate t = getLocaleTemplate( TEMPLATE_PASSWORD_GREETINGS_CARD_TEMPLATE, request.getLocale( ), model, 1 );

		return t.getHtml( );
	}

	/**
	 * Returns the HTML format greetings card sending form
	 * @param request The request
	 * @param plugin The plugin
	 * @return The Html template
	 * @throws DirectoryNotFoundException exception for DirectoryNotFoundException
	 */
	private String getCreateHTMLGreetingsCard( HttpServletRequest request, Plugin plugin ) throws DirectoryNotFoundException
	{
		String strIdGCT = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
		int nIdGCT = Integer.parseInt( strIdGCT );
		String strMessage = request.getParameter( PARAM_MESSAGE );
		String strMessage2 = request.getParameter( PARAM_MESSAGE2 );
		String strMailCopy = request.getParameter( PARAM_MAIL_COPY );
		String strSenderName = request.getParameter( PARAM_SENDER_NAME );
		String strSenderEmail = request.getParameter( PARAM_SENDER_EMAIL );
		String strAction = request.getParameter( PARAM_ACTION );

		String strMessageSend = EMPTY_STRING;

		// Load the parameters of the greetings card plugin
		String strPathGreetingsCardTemplates = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATES );
		String strPathGreetingsCardTemplateDirName = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME );

		String strNewDirectoryName = strPathGreetingsCardTemplateDirName + strIdGCT;
		strNewDirectoryName = UploadUtil.cleanFileName( strNewDirectoryName );

		String strPicture = GreetingsCardTemplateHome.getPicture( strNewDirectoryName, plugin.getName( ) );
		String strPathPictureCard = strPathGreetingsCardTemplates + PATH_SEPARATOR + strPathGreetingsCardTemplateDirName + strIdGCT + PATH_SEPARATOR + strPicture;

		GreetingsCardTemplate greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( nIdGCT, plugin );

		if ( strMessage == null )
		{
			strMessage = EMPTY_STRING;
		}

		if ( strMessage2 == null )
		{
			strMessage2 = EMPTY_STRING;
		}

		if ( strMailCopy == null )
		{
			strMailCopy = EMPTY_STRING;
		}

		if ( strSenderName == null )
		{
			strSenderName = EMPTY_STRING;
		}

		if ( strSenderEmail == null )
		{
			strSenderEmail = EMPTY_STRING;
		}

		if ( strAction.equals( ACTION_NAME_SEND ) )
		{
			strMessageSend = I18nService.getLocalizedString( PROPERTY_MESSAGE_EMAIL_SENDED, request.getLocale( ) );
		}

		HashMap<String, Object> model = new HashMap<String, Object>( );

		boolean bIsCaptchaEnabled = PluginService.isPluginEnable( JCAPTCHA_PLUGIN );
		model.put( MARK_IS_ACTIVE_CAPTCHA, bIsCaptchaEnabled );

		if ( bIsCaptchaEnabled )
		{
			_captchaService = new CaptchaSecurityService( );
			model.put( MARK_CAPTCHA, _captchaService.getHtmlCode( ) );
		}

		if ( SecurityService.isAuthenticationEnable( ) )
		{
			LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
			if ( user != null )
			{
				model.put( MARK_MYLUTECE_USER, user );
				strSenderEmail = user.getUserInfo( "user.business-info.online.email" );
				strSenderName = StringUtils.trim( user.getUserInfo( "user.name.family" ) + SPACE + user.getUserInfo( "user.name.given" ) );
			}
		}

		model.put( MARK_GCT_ID, strIdGCT );
		model.put( MARK_HEIGHT, greetingsCardTemplate.getHeight( ) );
		model.put( MARK_WIDTH, greetingsCardTemplate.getWidth( ) );
		model.put( MARK_PICTURE_CARD, strPathPictureCard );
		model.put( MARK_MESSAGE, strMessage );
		model.put( MARK_MESSAGE2, strMessage2 );
		model.put( MARK_MAIL_COPY, strMailCopy );
		model.put( MARK_SENDER_NAME, strSenderName );
		model.put( MARK_SENDER_EMAIL, strSenderEmail );
		model.put( MARK_EMAIL_SENDED, strMessageSend );
		model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
		model.put( MARK_LOCALE, AdminUserService.getLocale( request ).getLanguage( ) );

		HtmlTemplate t = getLocaleTemplate( strNewDirectoryName + PATH_SEPARATOR + PARAM_CREATE_HTML_CARD + UNDERSCORE + strIdGCT + POINT_HTML, request.getLocale( ), model, 0 );

		return t.getHtml( );
	}

	/**
	 * Returns the HTML format greetings card sending form
	 * @param request The request
	 * @param plugin The plugin
	 * @param strBaseUrl The webapp Url
	 * @return The Html template
	 * @throws DirectoryNotFoundException exception for DirectoryNotFoundException
	 * @throws AccessDeniedException exception for AccessDeniedException
	 */
	private String doSendGreetingsCard( HttpServletRequest request, Plugin plugin, String strBaseUrl ) throws DirectoryNotFoundException, AccessDeniedException, SiteMessageException
	{
		String strToday = DateUtil.getCurrentDateString( request.getLocale( ) );
		java.sql.Date dateToday = DateUtil.getDateSql( strToday );
		String strRecipientEmail = request.getParameter( PARAM_RECIPIENT_EMAIL );
		String strMessage = request.getParameter( PARAM_MESSAGE );
		strMessage = StringUtil.substitute( strMessage, HTML_BR, HTML_SUBSTITUTE_BR );

		String strMessage2 = request.getParameter( PARAM_MESSAGE2 );

		if ( strMessage2 != null )
		{
			strMessage2 = StringUtil.substitute( strMessage2, HTML_BR, HTML_SUBSTITUTE_BR );
		}

		String strMailCopy = request.getParameter( PARAM_MAIL_COPY );

		String strSenderName = request.getParameter( PARAM_SENDER_NAME );
		String strSenderEmail = request.getParameter( PARAM_SENDER_EMAIL );
		String strGreetingsCardTemplateId = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
		String strSenderIp = request.getRemoteAddr( );
		boolean bNotifyUser = request.getParameter( PARAM_NOTIFY_USER ) != null && request.getParameter( PARAM_NOTIFY_USER ).equals( CHECKBOX_ON );

		int nGreetingsCardTemplateId = Integer.parseInt( strGreetingsCardTemplateId );

		GreetingsCardTemplate greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( nGreetingsCardTemplateId, plugin );

		String[] listMail =
		{ strRecipientEmail };

		if ( strRecipientEmail.indexOf( SEPARATOR_MAIL_LIST ) != -1 )
		{
			listMail = strRecipientEmail.split( SEPARATOR_MAIL_LIST );
		}

		boolean bCopySended = false;
		String strSubject = greetingsCardTemplate.getObjectEmail( );
		String strMail = "";

		for ( String strRecipient : listMail )
		{
			strRecipient = strRecipient.trim( );

			if ( strRecipient == null || strRecipient.equals( EMPTY_STRING ) || !StringUtil.checkEmail( strRecipient ) )
			{
				Object[] malformedRecipient =
				{ strRecipient };
				SiteMessageService.setMessage( request, PROPERTY_CHAIN_MAIL_MALFORMED, malformedRecipient, PROPERTY_ERROR_MESSAGE, null, null, SiteMessage.TYPE_INFO );
			}

			GreetingsCard greetingsCard = new GreetingsCard( );
			greetingsCard.setRecipientEmail( strRecipient );
			greetingsCard.setMessage( strMessage );
			greetingsCard.setMessage2( strMessage2 );
			greetingsCard.setSenderName( strSenderName );
			greetingsCard.setSenderEmail( strSenderEmail );
			greetingsCard.setDate( dateToday );
			greetingsCard.setSenderIp( strSenderIp );
			greetingsCard.setIdGCT( nGreetingsCardTemplateId );
			greetingsCard.setStatus( GreetingsCard.STATUS_SENT );
			greetingsCard.setCopy( false );
			greetingsCard.setNotifySender( bNotifyUser );

			GreetingsCardHome.create( greetingsCard, plugin );

			String strInternetPortalUrl = AppPropertiesService.getProperty( PROPERTY_LUTECE_PROD_URL );
			String strPathGreetingsCardTemplateDirName = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME );

			String strNewDirectoryName = strPathGreetingsCardTemplateDirName + greetingsCard.getIdGCT( );
			strNewDirectoryName = UploadUtil.cleanFileName( strNewDirectoryName );

			UrlItem urlToViewHtmlCardFromIntranet = new UrlItem( strBaseUrl + PATH_SEPARATOR + AppPathService.getPortalUrl( ) );
			urlToViewHtmlCardFromIntranet.addParameter( PARAM_PAGE, plugin.getName( ) );
			urlToViewHtmlCardFromIntranet.addParameter( PARAM_ACTION, PROPERTY_ACTION_VIEW );
			urlToViewHtmlCardFromIntranet.addParameter( PARAM_FORMAT, PROPERTY_FORMAT_HTML );
			urlToViewHtmlCardFromIntranet.addParameter( PARAM_GREETINGS_CARD_ID, greetingsCard.getId( ) );

			UrlItem urlToViewFlashCardFromIntranet = new UrlItem( strBaseUrl + PATH_SEPARATOR + AppPathService.getPortalUrl( ) );
			urlToViewFlashCardFromIntranet.addParameter( PARAM_PAGE, plugin.getName( ) );
			urlToViewFlashCardFromIntranet.addParameter( PARAM_ACTION, PROPERTY_ACTION_VIEW );
			urlToViewFlashCardFromIntranet.addParameter( PARAM_FORMAT, PROPERTY_FORMAT_FLASH );
			urlToViewFlashCardFromIntranet.addParameter( PARAM_GREETINGS_CARD_ID, greetingsCard.getId( ) );

			UrlItem urlToViewHtmlCardFromInternet = new UrlItem( strInternetPortalUrl + PATH_SEPARATOR + AppPathService.getPortalUrl( ) );
			urlToViewHtmlCardFromInternet.addParameter( PARAM_PAGE, plugin.getName( ) );
			urlToViewHtmlCardFromInternet.addParameter( PARAM_ACTION, PROPERTY_ACTION_VIEW );
			urlToViewHtmlCardFromInternet.addParameter( PARAM_FORMAT, PROPERTY_FORMAT_HTML );
			urlToViewHtmlCardFromInternet.addParameter( PARAM_GREETINGS_CARD_ID, greetingsCard.getId( ) );

			UrlItem urlToViewFlashCardFromInternet = new UrlItem( strInternetPortalUrl + PATH_SEPARATOR + AppPathService.getPortalUrl( ) );
			urlToViewFlashCardFromInternet.addParameter( PARAM_PAGE, plugin.getName( ) );
			urlToViewFlashCardFromInternet.addParameter( PARAM_ACTION, PROPERTY_ACTION_VIEW );
			urlToViewFlashCardFromInternet.addParameter( PARAM_FORMAT, PROPERTY_FORMAT_FLASH );
			urlToViewFlashCardFromInternet.addParameter( PARAM_GREETINGS_CARD_ID, greetingsCard.getId( ) );

			HashMap<String, Object> model = new HashMap<String, Object>( );
			model.put( MARK_VIEW_HTML_CARD_FROM_INTRANET, urlToViewHtmlCardFromIntranet.getUrl( ) );
			model.put( MARK_VIEW_FLASH_CARD_FROM_INTRANET, urlToViewFlashCardFromIntranet.getUrl( ) );
			model.put( MARK_VIEW_HTML_CARD_FROM_INTERNET, urlToViewHtmlCardFromInternet.getUrl( ) );
			model.put( MARK_VIEW_FLASH_CARD_FROM_INTERNET, urlToViewFlashCardFromInternet.getUrl( ) );

			model.put( MARK_SENDER_NAME, greetingsCard.getSenderName( ) );
			model.put( MARK_PORTAL_URL, strBaseUrl );
			model.put( MARK_PLUGIN_NAME, plugin.getName( ) );
			model.put( MARK_GC_ID, greetingsCard.getId( ) );

			HtmlTemplate tMail = getLocaleTemplate( strNewDirectoryName + PATH_SEPARATOR + PARAM_MAIL_CARD + UNDERSCORE + greetingsCard.getIdGCT( ) + POINT_HTML, request.getLocale( ), model, 0 );
			strMail = tMail.getHtml( );

			// MailService.sendMailMultipartHtml( null, null, strRecipientEmail, strSenderName, strSenderEmail, strSubject, strMail, null, null );
			MailService.sendMailHtml( null, null, strRecipient, strSenderName, strSenderEmail, strSubject, strMail );

			// Template for copy
			if ( ( strMailCopy != null ) && !strMailCopy.equals( EMPTY_STRING ) && strMailCopy.equals( CHECKBOX_ON ) )
			{
				if ( !bCopySended )
				{
					greetingsCard.setCopy( true );
					
					// the addition of a list of email addresses of the recipients in the content of the email copy.
					String strMailContent = greetingsCard.getMessage();
					strMailContent += I18nService.getLocalizedString( ADDING_MAILING_LIST_REC_MESSAGE, request.getLocale( ) );
					int i=1;
					for ( String strMailRec : listMail )
					{
						strMailContent += strMailRec;
						if(i == listMail.length)
						{
							strMailContent += ".";
						}
						else
						{
							strMailContent += ", ";
						}
						i++;
					}
					greetingsCard.setMessage(strMailContent);
					GreetingsCardHome.create( greetingsCard, plugin );

					String strCopyOf = I18nService.getLocalizedString( PROPERTY_COPY_OF, request.getLocale( ) );
					MailService.sendMailHtml( strSenderEmail, strSenderName, strSenderEmail, strCopyOf + WHITE_SPACE + strSubject, strMail );
					bCopySended = true;
				}
			}
		}

		// UrlItem urlToSendingForm = new UrlItem( PORTAL_JSP );
		// urlToSendingForm.addParameter( PARAM_ACTION, PROPERTY_ACTION_CREATE );
		// urlToSendingForm.addParameter( PARAM_FORMAT, strFormat );
		// urlToSendingForm.addParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID, nGreetingsCardTemplateId );
		// urlToSendingForm.addParameter( PARAM_SENDED, 1 );

		if ( request.getParameter( PARAM_FORMAT ).equals( HTML ) )
		{
			return getCreateHTMLGreetingsCard( request, plugin );
		}
		else
		{
			return getCreateFlashGreetingsCard( request, plugin );
		}
	}

	/**
	 * Returns a reference on a template object (load the template or get it from the cache if present.)
	 * @param strTemplate The name of the template
	 * @param locale The current locale to localize the template
	 * @param model the model to use for loading
	 * @param nMode the mode HTML or Flash
	 * @return The template object.
	 * @since 1.5
	 */
	public static HtmlTemplate getLocaleTemplate( String strTemplate, Locale locale, Object model, int nMode )
	{
		HtmlTemplate template = null;
		String strPathGreetingsCardTemplates = EMPTY_STRING;

		if ( nMode == 0 )
		{
			// We have to load the HTML template
			strPathGreetingsCardTemplates = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATES );
		}
		else
		{
			// nMode == 1
			// We have to load the Flash template or the password template
			strPathGreetingsCardTemplates = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATES_FLASH );
		}

		// Load the template from the file
		template = AppTemplateService.getTemplate( strPathGreetingsCardTemplates + PATH_SEPARATOR + strTemplate, EMPTY_STRING, locale, model );

		return template;
	}
}
