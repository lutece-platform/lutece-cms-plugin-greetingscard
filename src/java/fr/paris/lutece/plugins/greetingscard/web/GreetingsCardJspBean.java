/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.plugins.greetingscard.business.Domain;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCard;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardArchive;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardArchiveHome;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardFilter;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardHome;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardStatistic;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardTemplate;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardTemplateHome;
import fr.paris.lutece.plugins.greetingscard.service.GreetingsCardResourceIdService;
import fr.paris.lutece.plugins.greetingscard.service.GreetingsCardService;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.xsl.XslExport;
import fr.paris.lutece.portal.business.xsl.XslExportHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.service.xsl.XslExportService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.filesystem.DirectoryNotFoundException;
import fr.paris.lutece.util.filesystem.UploadUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.xml.XmlUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;


/**
 * This class provides the user interface to manage greetingscard features ( manage, create, modify, remove)
 */
public class GreetingsCardJspBean extends AdminFeaturesPageJspBean
{
	public static final String RIGHT_MANAGE_GREETINGSCARD = "GREETINGSCARD_MANAGEMENT";

	// Marks
	private static final String MARK_GREETINGS_CARD_TEMPLATE_LIST = "greetings_card_template_list";
	private static final String MARK_GREETINGS_CARD_TEMPLATE_ID = "gct_id";
	private static final String MARK_GREETINGS_CARD_TEMPLATE_DESCRIPTION = "description";
	private static final String MARK_GREETINGS_CARD_TEMPLATE_OBJECT_EMAIL = "object_email";
	private static final String MARK_GREETINGS_CARD_TEMPLATE_CHECK_PASSWORD = "check_password";
	private static final String MARK_GREETINGS_CARD_TEMPLATE_STATUS = "status";
	private static final String MARK_GREETINGS_CARD_TEMPLATE_PASSWORD = "password";
	private static final String MARK_GREETINGS_CARD_ALL_MODEL_COMBO = "all_model";
	private static final String MARK_GREETINGS_CARD_STATISTIC = "greetings_card_statistic";
	private static final String MARK_GREETINGS_CARD_STATISTIC_LIST = "greetings_card_statistic_list";
	private static final String MARK_PLUGIN_NAME = "plugin_name";
	private static final String MARK_GREETINGS_CARD_TEMPLATE_HEIGHT = "height";
	private static final String MARK_GREETINGS_CARD_TEMPLATE_WIDTH = "width";
	private static final String MARK_USER_WORKGROUP_REF_LIST = "user_workgroup_list";
	private static final String MARK_USER_WORKGROUP_SELECTED = "user_workgroup_selected";
	private static final String MARK_WEBAPP_URL = "webapp_url";
	private static final String MARK_LOCALE = "locale";
	private static final String MARK_DATE_NEXT_ARCHIVE = "date_next_archive";
	private static final String MARK_YEAR_NEXT_ARCHIVE = "year_next_archive";
	private static final String MARK_TEMPLATE_LIST = "template_list";
	private static final String MARK_GREETINGS_CARD_LIST_DOMAIN = "domain_list";
	private static final String MARK_DOMAIN_TOTAL_READ = "domain_total_read";
	private static final String MARK_DOMAIN_TOTAL_SENT = "domain_total_sent";
	private static final String MARK_DOMAIN_TOTAL_CARDS = "total_cards_sent";
	private static final String MARK_PERMISSION_CREATE = "permission_create";
	private static final String MARK_PERMISSION_MODIFY = "permission_modify";
	private static final String MARK_PERMISSION_DELETE = "permission_delete";
	private static final String MARK_PERMISSION_SEND = "permission_send";
	private static final String MARK_PERMISSION_STATS = "permission_stats";
	private static final String MARK_PERMISSION_EXPORT = "permission_export";
	private static final String MARK_PERMISSION_ARCHIVE = "permission_archive";
	private static final String MARK_VIEW_HTML_CARD_FROM_INTRANET = "view_html_card_from_intranet";
	private static final String MARK_VIEW_HTML_CARD_FROM_INTERNET = "view_html_card_from_internet";
	private static final String MARK_VIEW_FLASH_CARD_FROM_INTRANET = "view_flash_card_from_intranet";
	private static final String MARK_VIEW_FLASH_CARD_FROM_INTERNET = "view_flash_card_from_internet";
	private static final String MARK_SENDER_NAME = "sender_name";
	private static final String MARK_GC_ID = "gc_id";
	private static final String MARK_PORTAL_URL = "portal_url";
	private static final String MARK_LIST_XSL_EXPORT = "list_xsl_export";
	private static final String MARK_LIST_YEARS = "list_years";
	private static final String MARK_YEAR = "year";

	// Parameters
	private static final String PARAMETER_DESCRIPTION = "description";
	private static final String PARAMETER_OBJECT_EMAIL = "object_email";
	private static final String PARAMETER_CHECK_PASSWORD = "check_password";
	private static final String PARAMETER_STATUS = "status";
	private static final String PARAMETER_PASSWORD = "password";
	private static final String PARAMETER_GREETINGS_CARD_TEMPLATE_ID = "gct_id";
	private static final String PARAMETER_CREATE_CARD = "create_card";
	private static final String PARAMETER_VIEW_CARD = "view_card";
	private static final String PARAMETER_PICTURE_CARD = "picture_card";
	private static final String PARAMETER_VIEW_HTML_CARD = "view_html_card";
	private static final String PARAMETER_CREATE_HTML_CARD = "create_html_card";
	private static final String PARAMETER_MAIL_CARD = "mail_card";
	private static final String PARAMETER_PLUGIN_NAME = "plugin_name";
	private static final String PARAMETER_GREETINGS_CARD_ID = "gc_id";
	private static final String PARAMETER_HEIGHT = "height";
	private static final String PARAMETER_WIDTH = "width";
	private static final String PARAMETER_DAYS = "days";
	private static final String PARAMETER_FILE_IMPORT = "recipient_email";
	private static final String PARAMETER_WORKGROUP = "workgroup";
	private static final String PARAMETER_STATS = "stats";
	private static final String PARAMETER_CREATE = "create";
	private static final String PARAMETER_ARCHIVE = "archive";
	private static final String PARAMETER_NEXT_AUTO_ARCHIVING = "greetingscard.nextAutoArchiving";
	private static final String PARAMETER_YEAR_NEXT_AUTO_ARCHIVING = "greetingscard.yearNextAutoArchiving";

	// JSP
	private static final String JSP_URL_GREETINGS_CARD_TEMPLATES_LIST = "GreetingsCardTemplatesList.jsp";
	private static final String JSP_URL_GREETINGS_CARDS_STATISTICS = "Statistics.jsp";
	private static final String JSP_MANAGE_GREETINGS_CARD = "jsp/admin/plugins/greetingscard/GreetingsCardTemplatesList.jsp";

	// Templates
	private static final String TEMPLATE_GREETINGS_CARD_MENU = "admin/plugins/greetingscard/greetings_card_menu.html";
	private static final String TEMPLATE_GREETINGS_CARD_TEMPLATES = "admin/plugins/greetingscard/greetings_card_templates.html";
	private static final String TEMPLATE_CREATE_GREETINGS_CARD_TEMPLATE = "admin/plugins/greetingscard/create_greetings_card_template.html";
	private static final String TEMPLATE_MODIFY_GREETINGS_CARD_TEMPLATE = "admin/plugins/greetingscard/modify_greetings_card_template.html";
	private static final String TEMPLATE_STATISTICS = "admin/plugins/greetingscard/statistics.html";
	private static final String TEMPLATE_GLOBAL_STATISTICS = "admin/plugins/greetingscard/global_statistics.html";
	private static final String TEMPLATE_GREETINGS_CARD_MAIL = "admin/plugins/greetingscard/greetings_card_mail.html";
	private static final String TEMPLATE_EXPORT_GREETINGS_CARD = "admin/plugins/greetingscard/export_greetings_card.html";
	private static final String TEMPLATE_ARCHIVE_GREETINGS_CARD = "admin/plugins/greetingscard/archive_greetings_card.html";

	// Properties
	private static final String PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME = "greetingscard.path.greetingscardtemplatedirname";
	private static final String PROPERTY_PATH_GREETINGS_CARD_TEMPLATES = "greetingscard.path.greetingscardtemplates";
	private static final String PROPERTY_PAGE_TITLE_STATS = "greetingscard.manage_greetingscard.statistic.labelTitle";
	private static final String PROPERTY_PAGE_TITLE_GREETINGSCARD_MODEL = "greetingscard.manage_greetingscard.model.labelTitle";
	private static final String PROPERTY_PAGE_TITLE_GREETINGSCARD_MODIFY = "greetingscard.modify_greetings_card_template.pageTitle";
	private static final String PROPERTY_PAGE_TITLE_GREETINGSCARD_CREATE = "greetingscard.create_greetings_card_template.pageTitle";
	private static final String PROPERTY_GREETINGSCARD_ALL_MODEL_COMBO = "greetingscard.statistics.allModelCombo";
	private static final String PROPERTY_FORMAT_HTML = "html";
	private static final String PROPERTY_FORMAT_FLASH = "flash";
	private static final String PROPERTY_ACTION_VIEW = "view";
	private static final String PROPERTY_LUTECE_PROD_URL = "lutece.prod.url";
	private static final String PROPERTY_PATH_GREETINGS_CARD_TEMPLATES_FLASH = "path.lutece.plugins";
	private static final String PROPERTY_COPY_OF = "greetingscard.label.copyof";
	private static final String PROPERTY_IMPORT_CSV_DELIMITER = "greetingscard.import.csv.delimiter";

	// Messages
	private static final String FIELD_FILE_IMPORT = "greetingscard.import_csv.label_file";
	private static final String MESSAGE_MANDATORY_FIELD = "greetingscard.message.mandatory.field";
	private static final String MESSAGE_MAIL_SEND = "greetingscard.message.mail.send";
	private static final String MESSAGE_ERROR_CSV_FILE_IMPORT = "greetingscard.message.error_csv_file_import";
	private static final String MESSAGE_ERROR_CSV_MAIL = "greetingscard.message.error_csv_mail";
	private static final String MESSAGE_FORBIDEN = "greetingscard.message.forbiden";
	private static final String MESSAGE_ERROR_PARSE_DATE = "greetingscard.message.error.errorParseDate";
	private static final String MESSAGE_DATE_AUTO_ARCHIVE_UPDATED = "greetingscard.message.dateNextArchiveUpdated";
	private static final String MESSAGE_ARCHIVED_OK = "greetingscard.message.greetingscards_archived";
	private static final String MESSAGE_ERROR_PARSE_NUMBER = "greetingscard.message.error.parseNumberFailed";
	private static final String MANDATORY_MESSAGE = "greetingscard.mandatory_field.message";
	private static final String MANDATORY_SENDER_NAME = "greetingscard.mandatory_field.sender_name";
	private static final String MANDATORY_SENDER_EMAIL = "greetingscard.mandatory_field.sender_mail";
	private static final String MANDATORY_YEAR = "greetingscard.mandatory_field.year";
	/** The message error for the number format in properties files */
	private static final String NUMBER_FORMAT_GREETINGS_CARD = "greetingscard.message_error.numberFormat";
	private static final String NUMBER_FORMAT_DAYS = "greetingscard.message_error.numberFormatDays";
	private static final String DIFFERENT_FILES_SWF = "greetingscard.message_error.differentFilesSWF";
	private static final String DIFFERENT_FILES_HTML = "greetingscard.message_error.differentFilesHTML";
	private static final String LABEL_CURRENT_YEAR = "greetingscard.global_statistics.labelCurrentYear";
	private static final String LABEL_YEAR = "greetingscard.greetings_card_templates.labelYearAuto";

	// Param
	private static final String PARAM_PAGE = "page";
	private static final String PARAM_ACTION = "action";
	private static final String PARAM_FORMAT = "format";
	private static final String PARAM_GREETINGS_CARD_ID = "gc_id";
	private static final String PARAM_MAIL_CARD = "mail_card";
	private static final String PARAM_MESSAGE = "message";
	private static final String PARAM_MESSAGE2 = "message2";
	private static final String PARAM_MAIL_COPY = "mail_copy";
	private static final String PARAM_SENDER_EMAIL = "sender_email";
	private static final String PARAM_SENDER_NAME = "sender_name";
	private static final String PARAM_GREETINGS_CARD_TEMPLATE_ID = "gct_id";
	private static final String PARAM_XSL_EXPORT_ID = "xsl_export_id";
	private static final String PARAM_NOTIFY_USER = "notify_user";
	private static final String PARAM_DATE_MIN = "date_min";
	private static final String PARAM_DATE_MAX = "date_max";
	private static final String PARAM_YEAR = "year";
	private static final String PARAM_NEXT_AUTO_ARCHIVING = "date_next_archive";
	private static final String PARAM_YEAR_NEXT_AUTO_ARCHIVING = "year_next_archive";

	private static final String URL_JSP_LIST_TEMPLATES = "jsp/admin/plugins/greetingscard/ManageGreetingsCard.jsp";

	private static final String HTML_BR = "<br>";
	private static final String HTML_SUBSTITUTE_BR = "\r\n";
	private static final String POINT_HTML = ".html";
	private static final String CHECKBOX_ON = "on";
	private static final String WHITE_SPACE = " ";
	private static final String SWF = ".swf";
	private static final String HTML = ".html";
	private static final String EMPTY_STRING = "";
	private static final String PATH_SEPARATOR = "/";
	private static final String UNDERSCORE = "_";
	private static final String POINT = ".";
	private static final String CHECKED = "checked=\"checked\"";
	private static final String UNCHECKED = "unchecked";
	private static final String MESSAGE = "message=";
	private static final String XML = "xml";
	private static final String SENDER_NAME = "&sender_name=";
	private static final String SENDER_EMAIL = "&sender_email=";
	private static final String PASSWORD = "password=";

	// Constant
	private static final String PLUGIN_NAME = "greetingscard";

	// Misc
	private static String CONSTANT_EXTENSION_CSV_FILE = ".csv";
	private static String CONSTANT_EXTENSION_XML_FILE = ".xml";
	private static String CONSTANT_MIME_TYPE_CSV = "application/csv";
	private static String CONSTANT_MIME_TYPE_XML = "application/xml";
	private static String CONSTANT_MIME_TYPE_OCTETSTREAM = "application/octet-stream";
	private static String _strWorkGroup = AdminWorkgroupService.ALL_GROUPS;

	private GreetingsCardService _greetingsCardService = SpringContextService.getBean( GreetingsCardService.beanName );

	/**
	 * Creates a new GreetingsCardJspBean object.
	 */
	public GreetingsCardJspBean( )
	{
	}

	/**
	 * Returns the Greetings Card administration menu
	 * @param request The Http request
	 * @return The Html template
	 */
	public String getManageGreetingsCard( HttpServletRequest request )
	{
		setPageTitleProperty( PARAMETER_PLUGIN_NAME );

		HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_GREETINGS_CARD_MENU, getLocale( ) );

		return getAdminPage( templateList.getHtml( ) );
	}

	/**
	 * Returns the list of greetings card templates
	 * @param request The Http request
	 * @return The Html template
	 */
	public String getGreetingsCardTemplatesList( HttpServletRequest request )
	{
		setPageTitleProperty( PROPERTY_PAGE_TITLE_GREETINGSCARD_MODEL );

		Collection<GreetingsCardTemplate> listGreetingsCardTemplate = GreetingsCardTemplateHome.findAll( getPlugin( ) );
		listGreetingsCardTemplate = AdminWorkgroupService.getAuthorizedCollection( listGreetingsCardTemplate, getUser( ) );

		// We use a specific locale so that the daemon will have the same.
		Locale locale = Locale.FRANCE;

		String strDateNextArchiveAuto = DatastoreService.getDataValue( PARAMETER_NEXT_AUTO_ARCHIVING, StringUtils.EMPTY );
		String strYearNextArchiveAuto = DatastoreService.getDataValue( PARAMETER_YEAR_NEXT_AUTO_ARCHIVING, StringUtils.EMPTY );

		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_PLUGIN_NAME, getPlugin( ).getName( ) );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_LIST, listGreetingsCardTemplate );
		model.put( MARK_LOCALE, locale );
		model.put( MARK_DATE_NEXT_ARCHIVE, strDateNextArchiveAuto );
		model.put( MARK_YEAR_NEXT_ARCHIVE, strYearNextArchiveAuto );
		addPermissionsToHashmap( model );

		HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_GREETINGS_CARD_TEMPLATES, getLocale( ), model );

		return getAdminPage( t.getHtml( ) );
	}

	/**
	 * Return the template selected
	 * @param request The request
	 * @return the template selected
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String getChoiceGreetingsCardTemplate( HttpServletRequest request ) throws AccessDeniedException
	{
		if ( request.getParameter( PARAMETER_CREATE ) != null )
		{
			return getCreateGreetingsCardTemplate( request );
		}
		else if ( request.getParameter( PARAMETER_STATS ) != null )
		{
			return getGlobalStatistics( request );
		}
		else if ( request.getParameter( PARAMETER_ARCHIVE ) != null )
		{
			return getArchiveGreetingsCard( request );
		}

		return getGreetingsCardTemplatesList( request );
	}

	/**
	 * Returns the greetings card template creation form
	 * @param request The Http request
	 * @return The Html template
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String getCreateGreetingsCardTemplate( HttpServletRequest request ) throws AccessDeniedException
	{
		setPageTitleProperty( PROPERTY_PAGE_TITLE_GREETINGSCARD_CREATE );

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_CREATE, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}

		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_USER_WORKGROUP_REF_LIST, AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) ) );
		model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
		model.put( MARK_LOCALE, AdminUserService.getLocale( request ).getLanguage( ) );

		HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_CREATE_GREETINGS_CARD_TEMPLATE, getLocale( ), model );

		return getAdminPage( t.getHtml( ) );
	}

	/**
	 * Process greetings card template Creation
	 * @param request The Http request
	 * @return String url to the jsp.
	 * @throws IOException For file access exception
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String doCreateGreetingsCardTemplate( HttpServletRequest request ) throws IOException, AccessDeniedException
	{
		// Constructs the url to the greetings card templates list
		UrlItem urlToGreetingsCardTemplatesList = new UrlItem( JSP_URL_GREETINGS_CARD_TEMPLATES_LIST );

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_CREATE, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}

		try
		{
			MultipartHttpServletRequest multipartRequest = ( MultipartHttpServletRequest ) request;

			FileItem fCreateFile = multipartRequest.getFile( PARAMETER_CREATE_CARD );
			FileItem fViewFile = multipartRequest.getFile( PARAMETER_VIEW_CARD );
			FileItem fPictureFile = multipartRequest.getFile( PARAMETER_PICTURE_CARD );
			FileItem fViewHTMLFile = multipartRequest.getFile( PARAMETER_VIEW_HTML_CARD );
			FileItem fCreateHTMLFile = multipartRequest.getFile( PARAMETER_CREATE_HTML_CARD );
			FileItem fMailFile = multipartRequest.getFile( PARAMETER_MAIL_CARD );

			// Mandatory field
			if ( ( multipartRequest.getParameter( PARAMETER_DESCRIPTION ).equals( EMPTY_STRING ) ) || ( multipartRequest.getParameter( PARAMETER_OBJECT_EMAIL ).equals( EMPTY_STRING ) )
					|| ( multipartRequest.getParameter( PARAMETER_PASSWORD ).equals( EMPTY_STRING ) && ( multipartRequest.getParameter( PARAMETER_CHECK_PASSWORD ) != null ) )
					|| ( multipartRequest.getParameter( PARAMETER_HEIGHT ).equals( EMPTY_STRING ) ) || ( multipartRequest.getParameter( PARAMETER_WIDTH ).equals( EMPTY_STRING ) )
					|| ( fCreateFile == null ) || ( fViewFile == null ) || ( fPictureFile == null ) || ( fViewHTMLFile == null ) || ( fCreateHTMLFile == null ) || ( fMailFile == null )
					|| ( fCreateFile.getSize( ) == 0 ) || ( fViewFile.getSize( ) == 0 ) || ( fPictureFile.getSize( ) == 0 ) || ( fViewHTMLFile.getSize( ) == 0 ) || ( fCreateHTMLFile.getSize( ) == 0 )
					|| ( fMailFile.getSize( ) == 0 ) )
			{
				return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
			}

			if ( fCreateFile.getFieldName( ).equals( fViewFile.getFieldName( ) ) )
			{
				return AdminMessageService.getMessageUrl( request, DIFFERENT_FILES_SWF, AdminMessage.TYPE_STOP );
			}

			if ( fViewHTMLFile.getFieldName( ).equals( fCreateHTMLFile.getFieldName( ) ) )
			{
				return AdminMessageService.getMessageUrl( request, DIFFERENT_FILES_HTML, AdminMessage.TYPE_STOP );
			}

			String strDescription = multipartRequest.getParameter( PARAMETER_DESCRIPTION );
			String strObjectEmail = multipartRequest.getParameter( PARAMETER_OBJECT_EMAIL );
			String strHeight = multipartRequest.getParameter( PARAMETER_HEIGHT );
			int nHeight = Integer.parseInt( strHeight );
			String strWidth = multipartRequest.getParameter( PARAMETER_WIDTH );
			int nWidth = Integer.parseInt( strWidth );
			_strWorkGroup = multipartRequest.getParameter( PARAMETER_WORKGROUP );

			int nStatus = 0;

			if ( request.getParameter( PARAMETER_STATUS ) != null )
			{
				String strStatus = request.getParameter( PARAMETER_STATUS );
				nStatus = Integer.parseInt( strStatus );
			}

			GreetingsCardTemplate greetingsCardTemplate = new GreetingsCardTemplate( );
			greetingsCardTemplate.setDescription( strDescription );
			greetingsCardTemplate.setObjectEmail( strObjectEmail );
			greetingsCardTemplate.setHeight( nHeight );
			greetingsCardTemplate.setWidth( nWidth );
			greetingsCardTemplate.setStatus( nStatus );
			greetingsCardTemplate.setWorkgroupKey( _strWorkGroup );

			if ( multipartRequest.getParameter( PARAMETER_CHECK_PASSWORD ) != null )
			{
				String strPassword = multipartRequest.getParameter( PARAMETER_PASSWORD );
				greetingsCardTemplate.setPassword( strPassword );
			}

			GreetingsCardTemplateHome.create( greetingsCardTemplate, getPlugin( ) );

			String strPathGreetingsCardTemplateDirName = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME );
			String strNewDirectoryName = strPathGreetingsCardTemplateDirName + greetingsCardTemplate.getId( );
			strNewDirectoryName = UploadUtil.cleanFileName( strNewDirectoryName );

			GreetingsCardTemplateHome.addDirectory( strNewDirectoryName );

			String strRelativePathTemplates = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATES );
			String strPathTemplates = AppPathService.getAbsolutePathFromRelativePath( PATH_SEPARATOR + strRelativePathTemplates );

			// Renames the downloaded create file with the appropriate name.
			File fCreateFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_CREATE_CARD + UNDERSCORE + greetingsCardTemplate.getId( ) + SWF );
			GreetingsCardTemplateHome.addGreetingsCardTemplate( fCreateFile, fCreateFileDest );

			// Renames the downloaded view file with the appropriate name.
			File fViewFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_VIEW_CARD + UNDERSCORE + greetingsCardTemplate.getId( ) + SWF );
			GreetingsCardTemplateHome.addGreetingsCardTemplate( fViewFile, fViewFileDest );

			// Renames the downloaded picture file with the appropriate name.
			String strName = fPictureFile.getName( );
			int extensionDot = strName.lastIndexOf( POINT );
			String strExtension = strName.substring( extensionDot );

			File fPictureFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_PICTURE_CARD + UNDERSCORE + greetingsCardTemplate.getId( )
					+ strExtension );
			GreetingsCardTemplateHome.addGreetingsCardTemplate( fPictureFile, fPictureFileDest );

			// Renames the downloaded view file with the appropriate name.
			File fViewHTMLFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_VIEW_HTML_CARD + UNDERSCORE + greetingsCardTemplate.getId( ) + HTML );
			GreetingsCardTemplateHome.addGreetingsCardTemplate( fViewHTMLFile, fViewHTMLFileDest );

			// Renames the downloaded create file with the appropriate name.
			File fCreateHTMLFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_CREATE_HTML_CARD + UNDERSCORE + greetingsCardTemplate.getId( )
					+ HTML );
			GreetingsCardTemplateHome.addGreetingsCardTemplate( fCreateHTMLFile, fCreateHTMLFileDest );

			// Renames the downloaded mail file with the appropriate name.
			File fMailFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_MAIL_CARD + UNDERSCORE + greetingsCardTemplate.getId( ) + HTML );
			GreetingsCardTemplateHome.addGreetingsCardTemplate( fMailFile, fMailFileDest );
		}
		catch ( NumberFormatException e )
		{
			return AdminMessageService.getMessageUrl( request, NUMBER_FORMAT_GREETINGS_CARD, AdminMessage.TYPE_STOP );
		}

		return urlToGreetingsCardTemplatesList.getUrl( );
	}

	/**
	 * Returns the greetings card template Modification form
	 * @param request The Http request
	 * @return The Html template
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String getModifyGreetingsCardTemplate( HttpServletRequest request ) throws AccessDeniedException
	{
		setPageTitleProperty( PROPERTY_PAGE_TITLE_GREETINGSCARD_MODIFY );

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_MODIFY, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}

		String strGreetingsCardTemplateId = request.getParameter( PARAMETER_GREETINGS_CARD_TEMPLATE_ID );
		int nGreetingsCardTemplateId = Integer.parseInt( strGreetingsCardTemplateId );

		GreetingsCardTemplate greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( nGreetingsCardTemplateId, getPlugin( ) );

		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_ID, greetingsCardTemplate.getId( ) );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_DESCRIPTION, greetingsCardTemplate.getDescription( ) );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_HEIGHT, greetingsCardTemplate.getHeight( ) );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_WIDTH, greetingsCardTemplate.getWidth( ) );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_OBJECT_EMAIL, greetingsCardTemplate.getObjectEmail( ) );
		model.put( MARK_USER_WORKGROUP_REF_LIST, AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) ) );
		model.put( MARK_USER_WORKGROUP_SELECTED, greetingsCardTemplate.getWorkgroupKey( ) );

		if ( greetingsCardTemplate.isEnabled( ) )
		{
			model.put( MARK_GREETINGS_CARD_TEMPLATE_STATUS, CHECKED );
		}
		else
		{
			model.put( MARK_GREETINGS_CARD_TEMPLATE_STATUS, UNCHECKED );
		}

		if ( greetingsCardTemplate.getPassword( ) == null )
		{
			model.put( MARK_GREETINGS_CARD_TEMPLATE_CHECK_PASSWORD, UNCHECKED );
			model.put( MARK_GREETINGS_CARD_TEMPLATE_PASSWORD, EMPTY_STRING );
		}
		else
		{
			model.put( MARK_GREETINGS_CARD_TEMPLATE_CHECK_PASSWORD, CHECKED );
			model.put( MARK_GREETINGS_CARD_TEMPLATE_PASSWORD, greetingsCardTemplate.getPassword( ) );
		}

		HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_GREETINGS_CARD_TEMPLATE, getLocale( ), model );

		return getAdminPage( t.getHtml( ) );
	}

	/**
	 * Process greetings card template modification
	 * 
	 * @param request Http request.
	 * @return String url to the image library jsp.
	 * @throws Exception For file access exception
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String doModifyGreetingsCardTemplate( HttpServletRequest request ) throws Exception, AccessDeniedException
	{
		// Constructs the url to the greetings card templates list
		UrlItem urlToGreetingsCardTemplatesList = new UrlItem( JSP_URL_GREETINGS_CARD_TEMPLATES_LIST );

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_MODIFY, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}

		try
		{
			MultipartHttpServletRequest multipartRequest = ( MultipartHttpServletRequest ) request;

			FileItem fCreateFile = multipartRequest.getFile( PARAMETER_CREATE_CARD );
			FileItem fViewFile = multipartRequest.getFile( PARAMETER_VIEW_CARD );
			FileItem fPictureFile = multipartRequest.getFile( PARAMETER_PICTURE_CARD );
			FileItem fViewHTMLFile = multipartRequest.getFile( PARAMETER_VIEW_HTML_CARD );
			FileItem fCreateHTMLFile = multipartRequest.getFile( PARAMETER_CREATE_HTML_CARD );
			FileItem fMailFile = multipartRequest.getFile( PARAMETER_MAIL_CARD );

			// Mandatory field
			if ( ( request.getParameter( PARAMETER_DESCRIPTION ).equals( EMPTY_STRING ) ) || ( request.getParameter( PARAMETER_OBJECT_EMAIL ).equals( EMPTY_STRING ) )
					|| ( ( request.getParameter( PARAMETER_CHECK_PASSWORD ) != null ) && request.getParameter( PARAMETER_PASSWORD ).equals( EMPTY_STRING ) )
					|| ( request.getParameter( PARAMETER_HEIGHT ).equals( EMPTY_STRING ) ) || ( request.getParameter( PARAMETER_WIDTH ).equals( EMPTY_STRING ) ) )
			{
				return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
			}

			// check if the two files are differents
			if ( ( fCreateFile != null ) && ( fViewFile != null ) && fCreateFile.getFieldName( ).equals( fViewFile.getFieldName( ) ) )
			{
				return AdminMessageService.getMessageUrl( request, DIFFERENT_FILES_SWF, AdminMessage.TYPE_STOP );
			}

			if ( ( fCreateHTMLFile != null ) && ( fViewHTMLFile != null ) && fCreateHTMLFile.getFieldName( ).equals( fViewHTMLFile.getFieldName( ) ) )
			{
				return AdminMessageService.getMessageUrl( request, DIFFERENT_FILES_HTML, AdminMessage.TYPE_STOP );
			}

			String strDescription = request.getParameter( PARAMETER_DESCRIPTION );
			String strObjectEmail = request.getParameter( PARAMETER_OBJECT_EMAIL );
			String strGreetingsCardTemplateId = request.getParameter( PARAMETER_GREETINGS_CARD_TEMPLATE_ID );
			int nGreetingsCardTemplateId = Integer.parseInt( strGreetingsCardTemplateId );
			String strHeight = request.getParameter( PARAMETER_HEIGHT );
			int nHeight = Integer.parseInt( strHeight );
			String strWidth = request.getParameter( PARAMETER_WIDTH );
			int nWidth = Integer.parseInt( strWidth );
			String strWorkGroup = request.getParameter( PARAMETER_WORKGROUP );

			int nStatus = 0;

			if ( request.getParameter( PARAMETER_STATUS ) != null )
			{
				String strStatus = request.getParameter( PARAMETER_STATUS );
				nStatus = Integer.parseInt( strStatus );
			}

			GreetingsCardTemplate greetingsCardTemplate = new GreetingsCardTemplate( );
			greetingsCardTemplate.setId( nGreetingsCardTemplateId );
			greetingsCardTemplate.setDescription( strDescription );
			greetingsCardTemplate.setObjectEmail( strObjectEmail );
			greetingsCardTemplate.setHeight( nHeight );
			greetingsCardTemplate.setWidth( nWidth );
			greetingsCardTemplate.setStatus( nStatus );
			greetingsCardTemplate.setWorkgroupKey( strWorkGroup );

			if ( request.getParameter( PARAMETER_CHECK_PASSWORD ) != null )
			{
				String strPassword = request.getParameter( PARAMETER_PASSWORD );
				greetingsCardTemplate.setPassword( strPassword );
			}

			GreetingsCardTemplateHome.update( greetingsCardTemplate, getPlugin( ) );

			String strPathGreetingsCardTemplateDirName = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME );

			String strNewDirectoryName = strPathGreetingsCardTemplateDirName + greetingsCardTemplate.getId( );
			strNewDirectoryName = UploadUtil.cleanFileName( strNewDirectoryName );

			String strRelativePathTemplates = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATES );
			String strPathTemplates = AppPathService.getAbsolutePathFromRelativePath( PATH_SEPARATOR + strRelativePathTemplates );

			if ( ( fCreateFile != null ) && ( fCreateFile.getSize( ) != 0 ) )
			{
				// Renames the downloaded edit file with the appropriate name.
				File fCreateFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_CREATE_CARD + UNDERSCORE + greetingsCardTemplate.getId( ) + SWF );
				GreetingsCardTemplateHome.updateGreetingsCardTemplate( fCreateFile, fCreateFileDest );
			}

			if ( ( fViewFile != null ) && ( fViewFile.getSize( ) != 0 ) )
			{ // Renames the downloaded view file with the appropriate name.

				File fViewFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_VIEW_CARD + UNDERSCORE + greetingsCardTemplate.getId( ) + SWF );
				GreetingsCardTemplateHome.updateGreetingsCardTemplate( fViewFile, fViewFileDest );
			}

			if ( ( fPictureFile != null ) && ( fPictureFile.getSize( ) != 0 ) )
			{
				// Renames the downloaded Picture file with the appropriate name.
				String strPathPicture = GreetingsCardTemplateHome.getPicture( strNewDirectoryName, getPlugin( ).getName( ) );
				GreetingsCardTemplateHome.removeGreetingsCardTemplate( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + strPathPicture );

				String strName = fPictureFile.getName( );
				int extensionDot = strName.lastIndexOf( POINT );
				String strExtension = strName.substring( extensionDot );

				File fPictureFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_PICTURE_CARD + UNDERSCORE + greetingsCardTemplate.getId( )
						+ strExtension );
				GreetingsCardTemplateHome.addGreetingsCardTemplate( fPictureFile, fPictureFileDest );
			}

			if ( ( fCreateHTMLFile != null ) && ( fCreateHTMLFile.getSize( ) != 0 ) )
			{ // Renames the downloaded create HTML file with the appropriate name.

				File fCreateHTMLFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_CREATE_HTML_CARD + UNDERSCORE + greetingsCardTemplate.getId( )
						+ HTML );
				GreetingsCardTemplateHome.updateGreetingsCardTemplate( fCreateHTMLFile, fCreateHTMLFileDest );
			}

			if ( ( fViewHTMLFile != null ) && ( fViewHTMLFile.getSize( ) != 0 ) )
			{ // Renames the downloaded view HTML file with the appropriate name.

				File fViewHTMLFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_VIEW_HTML_CARD + UNDERSCORE + greetingsCardTemplate.getId( )
						+ HTML );
				GreetingsCardTemplateHome.updateGreetingsCardTemplate( fViewHTMLFile, fViewHTMLFileDest );
			}

			if ( ( fMailFile != null ) && ( fMailFile.getSize( ) != 0 ) )
			{ // Renames the downloaded mail file with the appropriate name.

				File fMailFileDest = new File( strPathTemplates + PATH_SEPARATOR + strNewDirectoryName + PATH_SEPARATOR + PARAMETER_MAIL_CARD + UNDERSCORE + greetingsCardTemplate.getId( ) + HTML );
				GreetingsCardTemplateHome.updateGreetingsCardTemplate( fMailFile, fMailFileDest );
			}
		}
		catch ( NumberFormatException e )
		{
			return AdminMessageService.getMessageUrl( request, NUMBER_FORMAT_GREETINGS_CARD, AdminMessage.TYPE_STOP );
		}

		return urlToGreetingsCardTemplatesList.getUrl( );
	}

	/**
	 * Processes GreetingsCardTemplate removal
	 * @param request The Http request
	 * @return The URL to redirect to
	 * @throws DirectoryNotFoundException if the directory does not exist.
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String doRemoveGreetingsCardTemplate( HttpServletRequest request ) throws DirectoryNotFoundException, AccessDeniedException
	{
		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_DELETE, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}

		// Constructs the url to the greetings card templates list
		UrlItem urlToGreetingsCardTemplatesList = new UrlItem( JSP_URL_GREETINGS_CARD_TEMPLATES_LIST );

		String strGreetingsCardTemplateId = request.getParameter( PARAMETER_GREETINGS_CARD_TEMPLATE_ID );
		int nIdGreetingsCardTemplate = Integer.parseInt( strGreetingsCardTemplateId );

		GreetingsCardTemplate greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( nIdGreetingsCardTemplate, getPlugin( ) );

		Collection<GreetingsCard> listGreetingsCards = GreetingsCardHome.findByGreetingsCardTemplateId( greetingsCardTemplate.getId( ), getPlugin( ) );

		for ( GreetingsCard greetingsCard : listGreetingsCards )
		{
			String strIdGreetingsCard = greetingsCard.getId( );
			GreetingsCardHome.remove( strIdGreetingsCard, getPlugin( ) );
		}

		GreetingsCardTemplateHome.remove( nIdGreetingsCardTemplate, getPlugin( ) );

		String strPathGreetingsCardTemplateDirName = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME );

		String strDirName = strPathGreetingsCardTemplateDirName + nIdGreetingsCardTemplate;
		strDirName = UploadUtil.cleanFileName( strDirName );

		String strRelativePathTemplates = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATES );
		String strPathTemplates = AppPathService.getAbsolutePathFromRelativePath( PATH_SEPARATOR + strRelativePathTemplates );

		GreetingsCardTemplateHome.removeGreetingsCardTemplate( strPathTemplates + PATH_SEPARATOR + strDirName + PATH_SEPARATOR + PARAMETER_CREATE_CARD + UNDERSCORE + nIdGreetingsCardTemplate + SWF );
		GreetingsCardTemplateHome.removeGreetingsCardTemplate( strPathTemplates + PATH_SEPARATOR + strDirName + PATH_SEPARATOR + PARAMETER_VIEW_CARD + UNDERSCORE + nIdGreetingsCardTemplate + SWF );

		String strPathPicture = GreetingsCardTemplateHome.getPicture( PATH_SEPARATOR + strDirName, getPlugin( ).getName( ) );
		GreetingsCardTemplateHome.removeGreetingsCardTemplate( strPathTemplates + PATH_SEPARATOR + strDirName + PATH_SEPARATOR + strPathPicture );

		GreetingsCardTemplateHome.removeGreetingsCardTemplate( strPathTemplates + PATH_SEPARATOR + strDirName + PATH_SEPARATOR + PARAMETER_CREATE_HTML_CARD + UNDERSCORE + nIdGreetingsCardTemplate
				+ HTML );
		GreetingsCardTemplateHome.removeGreetingsCardTemplate( strPathTemplates + PATH_SEPARATOR + strDirName + PATH_SEPARATOR + PARAMETER_VIEW_HTML_CARD + UNDERSCORE + nIdGreetingsCardTemplate
				+ HTML );
		GreetingsCardTemplateHome.removeGreetingsCardTemplate( strPathTemplates + PATH_SEPARATOR + strDirName + PATH_SEPARATOR + PARAMETER_MAIL_CARD + UNDERSCORE + nIdGreetingsCardTemplate + HTML );

		GreetingsCardTemplateHome.removeDirectory( strDirName );

		// If the operation is successfull, redirect towards the list of question/answer couples
		return urlToGreetingsCardTemplatesList.getUrl( );
	}

	/**
	 * Returns the parameters for the swf app text mode
	 * @param request the request
	 * @return The Html template
	 */
	public String getGreetingsCardTextForSwfApp( HttpServletRequest request )
	{
		String strIdGC = request.getParameter( PARAMETER_GREETINGS_CARD_ID );

		String strParamsForSwfApp = EMPTY_STRING;
		GreetingsCard greetingsCard = GreetingsCardHome.findByPrimaryKey( strIdGC, getPlugin( ) );

		strParamsForSwfApp = MESSAGE + greetingsCard.getMessage( ) + SENDER_NAME + greetingsCard.getSenderName( ) + SENDER_EMAIL + greetingsCard.getSenderEmail( );

		return strParamsForSwfApp;
	}

	/**
	 * Returns the parameters for the swf app text mode
	 * @param request the request
	 * @return The Html template
	 */
	public String getGreetingsCardXMLForSwfApp( HttpServletRequest request )
	{
		String strIdGC = request.getParameter( PARAMETER_GREETINGS_CARD_ID );

		String strParamsForSwfApp = EMPTY_STRING;
		GreetingsCard greetingsCard = GreetingsCardHome.findByPrimaryKey( strIdGC, getPlugin( ) );

		strParamsForSwfApp = XML + greetingsCard.getXml( );

		return strParamsForSwfApp;
	}

	/**
	 * Returns the password of the greetings card template for the swf app
	 * @param request the request
	 * @return The Html template
	 */
	public String getPasswordForSwfApp( HttpServletRequest request )
	{
		String strIdGreetingsCardTemplate = request.getParameter( PARAMETER_GREETINGS_CARD_TEMPLATE_ID );
		int nIdGreetingsCardTemplate = Integer.parseInt( strIdGreetingsCardTemplate );

		GreetingsCardTemplate greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( nIdGreetingsCardTemplate, getPlugin( ) );

		String strPassword = PASSWORD + greetingsCardTemplate.getPassword( );

		return strPassword;
	}

	/**
	 * Returns the statistics main page
	 * @param request The Http request
	 * @return The Html template
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String getStatistics( HttpServletRequest request ) throws AccessDeniedException
	{
		setPageTitleProperty( PROPERTY_PAGE_TITLE_STATS );

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_SEE_STATS, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}

		String strYear = request.getParameter( PARAM_YEAR );
		int nYear = 0;
		if ( strYear == null )
		{
			strYear = StringUtils.EMPTY;
		}
		else
		{
			try
			{
				nYear = Integer.parseInt( strYear );
			}
			catch ( NumberFormatException e )
			{
				nYear = 0;
			}
		}
		if ( nYear > 0 )
		{
			return getAdminPage( getArchiveStatistics( request, TEMPLATE_STATISTICS, nYear ) );
		}

		String strIdGCT = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
		int nIdGCT = Integer.parseInt( strIdGCT );

		GreetingsCardTemplate gct = GreetingsCardTemplateHome.findByPrimaryKey( nIdGCT, getPlugin( ) );

		GreetingsCardFilter greetingsCardFilter = new GreetingsCardFilter( );
		greetingsCardFilter.setIdGCT( nIdGCT );

		List<Domain> listDomain = new ArrayList<Domain>( );
		int nTotalCardsSent = GreetingsCardHome.findNumberTotalOfMailSentWithoutCopy( getPlugin( ) );
		int nTotalReadDomain = 0;
		int nTotalSentDomain = 0;

		int nNbCardSentInternal = 0;
		int nNbCardSentExternal = 0;
		int nNbCardRedInternal = 0;
		int nNbCardRedExternal = 0;

		Map<String, Integer> cardsSentByDomain = GreetingsCardHome.findNumberOfMailSentByDomain( greetingsCardFilter, getPlugin( ) );
		Map<String, Integer> cardsRedByDomain = GreetingsCardHome.findNumberOfMailReadByDomain( greetingsCardFilter, getPlugin( ) );

		String[] strArrayDomain = new String[cardsSentByDomain.keySet( ).size( )];
		strArrayDomain = cardsSentByDomain.keySet( ).toArray( strArrayDomain );
		java.util.Arrays.sort( strArrayDomain );
		for ( String strDomain : strArrayDomain )
		{
			Domain domain = new Domain( );
			domain.setDomainName( strDomain );

			Integer nMailSent = cardsSentByDomain.get( strDomain );
			if ( nMailSent == null )
			{
				nMailSent = 0;
			}
			domain.setMailSent( nMailSent );

			Integer nMailRead = cardsRedByDomain.get( strDomain );
			if ( nMailRead == null )
			{
				nMailRead = 0;
			}
			domain.setMailRead( nMailRead );
			listDomain.add( domain );

			if ( _greetingsCardService.isInternal( strDomain ) )
			{
				nNbCardSentInternal += nMailSent;
				nNbCardRedInternal += nMailRead;
			}
			else
			{
				nNbCardSentExternal += nMailSent;
				nNbCardRedExternal += nMailRead;
			}

			nTotalReadDomain += nMailRead;
			nTotalSentDomain += nMailSent;
		}

		GreetingsCardStatistic greetingsCardStatistic = new GreetingsCardStatistic( );
		greetingsCardStatistic.setDescription( gct.getDescription( ) );
		greetingsCardStatistic.setIdGCT( gct.getId( ) );
		greetingsCardStatistic.setNbCardSentInternal( nNbCardSentInternal );
		greetingsCardStatistic.setNbCardRedInternal( nNbCardRedInternal );
		greetingsCardStatistic.setNbCardSentExternal( nNbCardSentExternal );
		greetingsCardStatistic.setNbCardRedExternal( nNbCardRedExternal );

		ReferenceList refListYears = GreetingsCardArchiveHome.getYearList( getPlugin( ) );
		ReferenceItem refItem = new ReferenceItem( );
		refItem.setName( I18nService.getLocalizedString( LABEL_CURRENT_YEAR, request.getLocale( ) ) );
		refItem.setCode( StringUtils.EMPTY );
		refListYears.add( 0, refItem );

		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_GREETINGS_CARD_STATISTIC, greetingsCardStatistic );
		model.put( MARK_GREETINGS_CARD_LIST_DOMAIN, listDomain );
		model.put( MARK_DOMAIN_TOTAL_READ, nTotalReadDomain );
		model.put( MARK_DOMAIN_TOTAL_SENT, nTotalSentDomain );
		model.put( MARK_DOMAIN_TOTAL_CARDS, nTotalCardsSent );
		model.put( MARK_LIST_YEARS, refListYears );
		model.put( MARK_YEAR, strYear );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_ID, strIdGCT );

		HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_STATISTICS, getLocale( ), model );

		return getAdminPage( t.getHtml( ) );
	}

	/**
	 * Returns the statistics main page
	 * @param request The Http request
	 * @return The Html template
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String getGlobalStatistics( HttpServletRequest request ) throws AccessDeniedException
	{
		setPageTitleProperty( PROPERTY_PAGE_TITLE_STATS );

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_SEE_STATS, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}

		String strYear = request.getParameter( PARAM_YEAR );
		int nYear = 0;
		if ( strYear == null )
		{
			strYear = StringUtils.EMPTY;
		}
		else
		{
			try
			{
				nYear = Integer.parseInt( strYear );
			}
			catch ( NumberFormatException e )
			{
				nYear = 0;
			}
		}
		if ( nYear > 0 )
		{
			return getAdminPage( getArchiveStatistics( request, TEMPLATE_GLOBAL_STATISTICS, nYear ) );
		}

		Collection<GreetingsCardTemplate> listGreetingsCardTemplate = GreetingsCardTemplateHome.findAll( getPlugin( ) );
		listGreetingsCardTemplate = AdminWorkgroupService.getAuthorizedCollection( listGreetingsCardTemplate, getUser( ) );

		GreetingsCardFilter greetingsCardFilter = new GreetingsCardFilter( );

		List<Domain> listDomain = new ArrayList<Domain>( );

		List<GreetingsCardStatistic> listGreetingsCardStatistic = new ArrayList<GreetingsCardStatistic>( );
		Map<String, Integer> totCardsSentByDomain = new HashMap<String, Integer>( );
		Map<String, Integer> totCardsRedByDomain = new HashMap<String, Integer>( );
		for ( GreetingsCardTemplate gct : listGreetingsCardTemplate )
		{
			Integer nMailSent = 0;
			Integer nMailRead = 0;
			int nNbCardSentInternal = 0;
			int nNbCardSentExternal = 0;
			int nNbCardRedInternal = 0;
			int nNbCardRedExternal = 0;
			greetingsCardFilter.setIdGCT( gct.getId( ) );
			Map<String, Integer> cardsSentByDomain = GreetingsCardHome.findNumberOfMailSentByDomain( greetingsCardFilter, getPlugin( ) );
			Map<String, Integer> cardsRedByDomain = GreetingsCardHome.findNumberOfMailReadByDomain( greetingsCardFilter, getPlugin( ) );

			Set<String> setStrDomain = cardsSentByDomain.keySet( );
			for ( String strDomain : setStrDomain )
			{

				nMailSent = cardsSentByDomain.get( strDomain );
				if ( nMailSent == null )
				{
					nMailSent = 0;
				}
				nMailRead = cardsRedByDomain.get( strDomain );
				if ( nMailRead == null )
				{
					nMailRead = 0;
				}

				if ( _greetingsCardService.isInternal( strDomain ) )
				{
					nNbCardSentInternal += nMailSent;
					nNbCardRedInternal += nMailRead;
				}
				else
				{
					nNbCardSentExternal += nMailSent;
					nNbCardRedExternal += nMailRead;
				}

				Integer nTotCardSent = totCardsSentByDomain.get( strDomain );
				if ( nTotCardSent == null )
				{
					nTotCardSent = 0;
				}
				totCardsSentByDomain.put( strDomain, nTotCardSent + nMailSent );

				Integer nTotCardRed = totCardsRedByDomain.get( strDomain );
				if ( nTotCardRed == null )
				{
					nTotCardRed = 0;
				}
				totCardsRedByDomain.put( strDomain, nTotCardRed + nMailRead );
			}

			GreetingsCardStatistic greetingsCardStatistic = new GreetingsCardStatistic( );
			greetingsCardStatistic.setDescription( gct.getDescription( ) );
			greetingsCardStatistic.setIdGCT( gct.getId( ) );
			greetingsCardStatistic.setNbCardSentInternal( nNbCardSentInternal );
			greetingsCardStatistic.setNbCardRedInternal( nNbCardRedInternal );
			greetingsCardStatistic.setNbCardSentExternal( nNbCardSentExternal );
			greetingsCardStatistic.setNbCardRedExternal( nNbCardRedExternal );
			listGreetingsCardStatistic.add( greetingsCardStatistic );
		}

		String[] strArrayDomain = new String[totCardsSentByDomain.keySet( ).size( )];
		strArrayDomain = totCardsSentByDomain.keySet( ).toArray( strArrayDomain );
		java.util.Arrays.sort( strArrayDomain );
		int nTotalRead = 0;
		int nTotalSent = 0;
		Integer nMailSent = 0;
		Integer nMailRead = 0;
		for ( String strDomain : strArrayDomain )
		{
			nMailSent = totCardsSentByDomain.get( strDomain );
			if ( nMailSent == null )
			{
				nMailSent = 0;
			}
			nMailRead = totCardsRedByDomain.get( strDomain );
			if ( nMailRead == null )
			{
				nMailRead = 0;
			}
			Domain domain = new Domain( );
			domain.setDomainName( strDomain );
			domain.setMailSent( nMailSent );
			domain.setMailRead( nMailRead );
			listDomain.add( domain );

			nTotalSent += nMailSent;
			nTotalRead += nMailRead;
		}

		ReferenceList refListYears = GreetingsCardArchiveHome.getYearList( getPlugin( ) );
		ReferenceItem refItem = new ReferenceItem( );
		refItem.setName( I18nService.getLocalizedString( LABEL_CURRENT_YEAR, request.getLocale( ) ) );
		refItem.setCode( StringUtils.EMPTY );
		refListYears.add( 0, refItem );

		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_GREETINGS_CARD_STATISTIC_LIST, listGreetingsCardStatistic );
		model.put( MARK_GREETINGS_CARD_LIST_DOMAIN, listDomain );
		model.put( MARK_DOMAIN_TOTAL_READ, nTotalRead );
		model.put( MARK_DOMAIN_TOTAL_SENT, nTotalSent );
		model.put( MARK_DOMAIN_TOTAL_CARDS, nTotalSent );
		model.put( MARK_LIST_YEARS, refListYears );
		model.put( MARK_YEAR, strYear );

		HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_GLOBAL_STATISTICS, getLocale( ), model );

		return getAdminPage( t.getHtml( ) );
	}

	/**
	 * Get statistics of an archived year
	 * @param request the request. If the parameter "gct_id" exists, and contains a numeral value, then only data of the template with the given id are displayed
	 * @param strTemplate The template to use
	 * @param nYear Year to consider
	 * @return The HTML code generated from the template
	 */
	private String getArchiveStatistics( HttpServletRequest request, String strTemplate, int nYear )
	{
		String strIdGCT = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
		int nIdGCT = 0;
		try
		{
			nIdGCT = Integer.parseInt( strIdGCT );
		}
		catch ( NumberFormatException e )
		{
			nIdGCT = 0;
		}
		Collection<GreetingsCardTemplate> listGreetingsCardTemplate;

		if ( nIdGCT > 0 )
		{
			listGreetingsCardTemplate = new ArrayList<GreetingsCardTemplate>( );
			listGreetingsCardTemplate.add( GreetingsCardTemplateHome.findByPrimaryKey( nIdGCT, getPlugin( ) ) );
		}
		else
		{
			listGreetingsCardTemplate = GreetingsCardTemplateHome.findAll( getPlugin( ) );
		}

		List<Domain> listDomain = new ArrayList<Domain>( );
		List<GreetingsCardStatistic> listGreetingsCardStatistic = new ArrayList<GreetingsCardStatistic>( );

		Map<String, Integer> totCardsSentByDomain = new HashMap<String, Integer>( );
		Map<String, Integer> totCardsRedByDomain = new HashMap<String, Integer>( );

		for ( GreetingsCardTemplate gct : listGreetingsCardTemplate )
		{
			Collection<GreetingsCardArchive> listGreetingsCardArchive = GreetingsCardArchiveHome.findByTemplateIdAndYear( gct.getId( ), nYear, getPlugin( ) );
			GreetingsCardStatistic greetingsCardStatistic = new GreetingsCardStatistic( );
			greetingsCardStatistic.setIdGCT( gct.getId( ) );
			greetingsCardStatistic.setDescription( gct.getDescription( ) );
			for ( GreetingsCardArchive archive : listGreetingsCardArchive )
			{
				if ( _greetingsCardService.isInternal( archive.getDomainName( ) ) )
				{
					greetingsCardStatistic.setNbCardSentInternal( greetingsCardStatistic.getNbCardSentInternal( ) + archive.getNbCard( ) );
					greetingsCardStatistic.setNbCardRedInternal( greetingsCardStatistic.getNbCardRedInternal( ) + archive.getNbCardRed( ) );
				}
				else
				{
					greetingsCardStatistic.setNbCardSentExternal( greetingsCardStatistic.getNbCardSentExternal( ) + archive.getNbCard( ) );
					greetingsCardStatistic.setNbCardRedExternal( greetingsCardStatistic.getNbCardRedExternal( ) + archive.getNbCardRed( ) );
				}

				Integer nTotCardSent = totCardsSentByDomain.get( archive.getDomainName( ) );
				if ( nTotCardSent == null )
				{
					nTotCardSent = 0;
				}
				totCardsSentByDomain.put( archive.getDomainName( ), nTotCardSent + archive.getNbCard( ) );

				Integer nTotCardRed = totCardsRedByDomain.get( archive.getDomainName( ) );
				if ( nTotCardRed == null )
				{
					nTotCardRed = 0;
				}
				totCardsRedByDomain.put( archive.getDomainName( ), nTotCardRed + archive.getNbCardRed( ) );
			}
			listGreetingsCardStatistic.add( greetingsCardStatistic );
		}

		String[] strArrayDomain = new String[totCardsSentByDomain.keySet( ).size( )];
		strArrayDomain = totCardsSentByDomain.keySet( ).toArray( strArrayDomain );
		java.util.Arrays.sort( strArrayDomain );
		int nTotalRead = 0;
		int nTotalSent = 0;
		Integer nMailSent = 0;
		Integer nMailRead = 0;
		for ( String strDomain : strArrayDomain )
		{
			nMailSent = totCardsSentByDomain.get( strDomain );
			if ( nMailSent == null )
			{
				nMailSent = 0;
			}
			nMailRead = totCardsRedByDomain.get( strDomain );
			if ( nMailRead == null )
			{
				nMailRead = 0;
			}
			Domain domain = new Domain( );
			domain.setDomainName( strDomain );
			domain.setMailSent( nMailSent );
			domain.setMailRead( nMailRead );
			listDomain.add( domain );

			nTotalSent += nMailSent;
			nTotalRead += nMailRead;
		}

		ReferenceList refListYears = GreetingsCardArchiveHome.getYearList( getPlugin( ) );
		ReferenceItem refItem = new ReferenceItem( );
		refItem.setName( I18nService.getLocalizedString( LABEL_CURRENT_YEAR, request.getLocale( ) ) );
		refItem.setCode( StringUtils.EMPTY );
		refListYears.add( 0, refItem );

		HashMap<String, Object> model = new HashMap<String, Object>( );
		if ( nIdGCT > 0 )
		{
			model.put( MARK_GREETINGS_CARD_STATISTIC, listGreetingsCardStatistic.get( 0 ) );
			model.put( MARK_GREETINGS_CARD_TEMPLATE_ID, strIdGCT );
		}
		else
		{
			model.put( MARK_GREETINGS_CARD_STATISTIC_LIST, listGreetingsCardStatistic );
		}
		model.put( MARK_GREETINGS_CARD_LIST_DOMAIN, listDomain );
		model.put( MARK_DOMAIN_TOTAL_READ, nTotalRead );
		model.put( MARK_DOMAIN_TOTAL_SENT, nTotalSent );
		model.put( MARK_DOMAIN_TOTAL_CARDS, nTotalSent );
		model.put( MARK_LIST_YEARS, refListYears );
		model.put( MARK_YEAR, String.valueOf( nYear ) );

		HtmlTemplate t = AppTemplateService.getTemplate( strTemplate, getLocale( ), model );

		return t.getHtml( );
	}

	/**
	 * Processes asked greetings cards removal
	 * @param request The Http request
	 * @return The URL to redirect to
	 */
	public String doRemoveGreetingsCards( HttpServletRequest request )
	{
		// Constructs the url to the greetings card templates list
		UrlItem urlToGreetingsCardsStatistics = new UrlItem( JSP_URL_GREETINGS_CARDS_STATISTICS );

		String strDays = request.getParameter( PARAMETER_DAYS );
		String strIdGCT = request.getParameter( PARAMETER_GREETINGS_CARD_TEMPLATE_ID );

		try
		{
			int nDays = Integer.parseInt( strDays );
			int nIdGCT = Integer.parseInt( strIdGCT );

			if ( nDays <= 0 )
			{
				// Complete removal
				nDays -= 1;
			}

			String strToday = DateUtil.getCurrentDateString( request.getLocale( ) );
			java.sql.Date dateDate = DateUtil.getDateSql( strToday );

			long lDate = dateDate.getTime( );
			lDate = lDate - ( 1000 * 3600 * 24 * nDays );
			dateDate.setTime( lDate );

			Collection<GreetingsCard> listGreetingsCards = GreetingsCardHome.findAll( getPlugin( ) );

			for ( GreetingsCard greetingsCard : listGreetingsCards )
			{
				// Removal of greetings cards owning to all greetings card templates
				if ( nIdGCT == 0 )
				{
					if ( greetingsCard.getDate( ).before( dateDate ) )
					{
						GreetingsCardHome.remove( greetingsCard.getId( ), getPlugin( ) );
					}
				}
				else
				{
					if ( ( greetingsCard.getIdGCT( ) == nIdGCT ) && greetingsCard.getDate( ).before( dateDate ) )
					{
						GreetingsCardHome.remove( greetingsCard.getId( ), getPlugin( ) );
					}
				}
			}
		}
		catch ( NumberFormatException e )
		{
			return AdminMessageService.getMessageUrl( request, NUMBER_FORMAT_DAYS, AdminMessage.TYPE_STOP );
		}

		return urlToGreetingsCardsStatistics.getUrl( );
	}

	/**
	 * Returns the Greetings Card administration menu
	 * @param request The Http request
	 * @return The Html template
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String getSendGreetingsCard( HttpServletRequest request ) throws AccessDeniedException
	{
		setPageTitleProperty( PARAMETER_PLUGIN_NAME );

		String strGreetingsCardTemplateId = request.getParameter( PARAMETER_GREETINGS_CARD_TEMPLATE_ID );
		int nGreetingsCardTemplateId = Integer.parseInt( strGreetingsCardTemplateId );

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_SEND, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}

		HashMap<String, Object> model = new HashMap<String, Object>( );

		model.put( MARK_TEMPLATE_LIST, getGreetingsCardTemplateListe( ) );
		model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
		model.put( MARK_LOCALE, AdminUserService.getLocale( request ).getLanguage( ) );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_ID, nGreetingsCardTemplateId );

		HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_GREETINGS_CARD_MAIL, getLocale( ), model );

		return getAdminPage( templateList.getHtml( ) );
	}

	/**
	 * Send Greetings Card
	 * @param request The request
	 * @return Information message corresponding to send
	 */
	public String doSendGreetingsCard( HttpServletRequest request )
	{
		MultipartHttpServletRequest multipartRequest = ( MultipartHttpServletRequest ) request;
		FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE_IMPORT );

		String strBaseUrl = AppPathService.getBaseUrl( request );
		String strError = null;

		if ( ( fileItem == null ) || ( fileItem.getName( ) == null ) || EMPTY_STRING.equals( fileItem.getName( ) ) )
		{
			Object[] tabRequiredFields =
			{ I18nService.getLocalizedString( FIELD_FILE_IMPORT, getLocale( ) ) };

			return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
		}

		if ( !fileItem.getName( ).toLowerCase( ).endsWith( CONSTANT_EXTENSION_CSV_FILE ) )
		{
			return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_CSV_FILE_IMPORT, AdminMessage.TYPE_STOP );
		}

		List<String> listMail = getMaiListFromCSV( fileItem );

		if ( listMail == null )
		{
			return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_CSV_FILE_IMPORT, AdminMessage.TYPE_ERROR );
		}

		String strToday = DateUtil.getCurrentDateString( request.getLocale( ) );
		java.sql.Date dateToday = DateUtil.getDateSql( strToday );
		String strMessage = request.getParameter( PARAM_MESSAGE );
		strMessage = StringUtil.substitute( strMessage, HTML_BR, HTML_SUBSTITUTE_BR );

		String strMessage2 = request.getParameter( PARAM_MESSAGE2 );
		strMessage2 = StringUtil.substitute( strMessage2, HTML_BR, HTML_SUBSTITUTE_BR );

		String strMailCopy = request.getParameter( PARAM_MAIL_COPY );

		String strSenderName = request.getParameter( PARAM_SENDER_NAME );
		String strSenderEmail = request.getParameter( PARAM_SENDER_EMAIL );
		String strGreetingsCardTemplateId = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
		String strSenderIp = request.getRemoteAddr( );
		boolean bNotifyUser = request.getParameter( PARAM_NOTIFY_USER ) != null && request.getParameter( PARAM_NOTIFY_USER ).equals( CHECKBOX_ON );

		if ( ( strMessage == null ) || strMessage.equals( EMPTY_STRING ) )
		{
			strError = I18nService.getLocalizedString( MANDATORY_MESSAGE, request.getLocale( ) );
		}

		if ( ( strSenderName == null ) || strSenderName.equals( EMPTY_STRING ) )
		{
			strError = I18nService.getLocalizedString( MANDATORY_SENDER_NAME, request.getLocale( ) );
		}

		if ( ( strSenderEmail == null ) || strSenderEmail.equals( EMPTY_STRING ) )
		{
			strError = I18nService.getLocalizedString( MANDATORY_SENDER_EMAIL, request.getLocale( ) );
		}

		if ( strError != null )
		{
			Object[] tabRequiredFields =
			{ strError };

			return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
		}

		int nGreetingsCardTemplateId = Integer.parseInt( strGreetingsCardTemplateId );

		GreetingsCardTemplate greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( nGreetingsCardTemplateId, getPlugin( ) );

		boolean bCopySended = false;
		int nMailSended = 0;
		List<String> distinctRecipient = new ArrayList<String>( );

		for ( int i = 0; i < listMail.size( ); i++ )
		{
			String strRecipientEmail = listMail.get( i );

			if ( !StringUtil.checkEmail( strRecipientEmail ) )
			{
				Object[] tabRequiredFields =
				{ strRecipientEmail };

				return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_CSV_MAIL, tabRequiredFields, AdminMessage.TYPE_ERROR );
			}

			if ( !distinctRecipient.contains( strRecipientEmail ) )
			{
				distinctRecipient.add( strRecipientEmail );
			}

			GreetingsCard greetingsCard = new GreetingsCard( );
			greetingsCard.setRecipientEmail( strRecipientEmail );
			greetingsCard.setMessage( strMessage );
			greetingsCard.setMessage2( strMessage2 );
			greetingsCard.setSenderName( strSenderName );
			greetingsCard.setSenderEmail( strSenderEmail );
			greetingsCard.setDate( dateToday );
			greetingsCard.setSenderIp( strSenderIp );
			greetingsCard.setIdGCT( nGreetingsCardTemplateId );
			greetingsCard.setStatus( GreetingsCard.STATUS_SENT );
			greetingsCard.setNotifySender( bNotifyUser );

			GreetingsCardHome.create( greetingsCard, getPlugin( ) );

			String strInternetPortalUrl = AppPropertiesService.getProperty( PROPERTY_LUTECE_PROD_URL );
			String strPathGreetingsCardTemplateDirName = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE_DIR_NAME );

			String strNewDirectoryName = strPathGreetingsCardTemplateDirName + greetingsCard.getIdGCT( );
			strNewDirectoryName = UploadUtil.cleanFileName( strNewDirectoryName );

			UrlItem urlToViewHtmlCardFromIntranet = new UrlItem( strBaseUrl + PATH_SEPARATOR + AppPathService.getPortalUrl( ) );
			urlToViewHtmlCardFromIntranet.addParameter( PARAM_PAGE, getPlugin( ).getName( ) );
			urlToViewHtmlCardFromIntranet.addParameter( PARAM_ACTION, PROPERTY_ACTION_VIEW );
			urlToViewHtmlCardFromIntranet.addParameter( PARAM_FORMAT, PROPERTY_FORMAT_HTML );
			urlToViewHtmlCardFromIntranet.addParameter( PARAM_GREETINGS_CARD_ID, greetingsCard.getId( ) );

			UrlItem urlToViewFlashCardFromIntranet = new UrlItem( strBaseUrl + PATH_SEPARATOR + AppPathService.getPortalUrl( ) );
			urlToViewFlashCardFromIntranet.addParameter( PARAM_PAGE, getPlugin( ).getName( ) );
			urlToViewFlashCardFromIntranet.addParameter( PARAM_ACTION, PROPERTY_ACTION_VIEW );
			urlToViewFlashCardFromIntranet.addParameter( PARAM_FORMAT, PROPERTY_FORMAT_FLASH );
			urlToViewFlashCardFromIntranet.addParameter( PARAM_GREETINGS_CARD_ID, greetingsCard.getId( ) );

			UrlItem urlToViewHtmlCardFromInternet = new UrlItem( strInternetPortalUrl + PATH_SEPARATOR + AppPathService.getPortalUrl( ) );
			urlToViewHtmlCardFromInternet.addParameter( PARAM_PAGE, getPlugin( ).getName( ) );
			urlToViewHtmlCardFromInternet.addParameter( PARAM_ACTION, PROPERTY_ACTION_VIEW );
			urlToViewHtmlCardFromInternet.addParameter( PARAM_FORMAT, PROPERTY_FORMAT_HTML );
			urlToViewHtmlCardFromInternet.addParameter( PARAM_GREETINGS_CARD_ID, greetingsCard.getId( ) );

			UrlItem urlToViewFlashCardFromInternet = new UrlItem( strInternetPortalUrl + PATH_SEPARATOR + AppPathService.getPortalUrl( ) );
			urlToViewFlashCardFromInternet.addParameter( PARAM_PAGE, getPlugin( ).getName( ) );
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
			model.put( MARK_PLUGIN_NAME, getPlugin( ).getName( ) );
			model.put( MARK_GC_ID, greetingsCard.getId( ) );

			String strSubject = greetingsCardTemplate.getObjectEmail( );
			HtmlTemplate tMail = getLocaleTemplate( strNewDirectoryName + PATH_SEPARATOR + PARAM_MAIL_CARD + UNDERSCORE + greetingsCard.getIdGCT( ) + POINT_HTML, request.getLocale( ), model, 0 );
			String strMail = tMail.getHtml( );

			MailService.sendMailHtml( null, null, strRecipientEmail, strSenderName, strSenderEmail, strSubject, strMail );

			nMailSended++;

			if ( ( strMailCopy != null ) && !strMailCopy.equals( EMPTY_STRING ) && strMailCopy.equals( CHECKBOX_ON ) )
			{
				if ( !bCopySended )
				{
					greetingsCard.setCopy( true );
					greetingsCard.setNotifySender( false );
					GreetingsCardHome.create( greetingsCard, getPlugin( ) );

					String strCopyOf = I18nService.getLocalizedString( PROPERTY_COPY_OF, request.getLocale( ) );
					MailService.sendMailHtml( strSenderEmail, strSenderName, strSenderEmail, strCopyOf + WHITE_SPACE + strSubject, strMail );
					bCopySended = true;
				}
			}

			// UrlItem urlToSendingForm = new UrlItem( PORTAL_JSP );
			// urlToSendingForm.addParameter( PARAM_ACTION, PROPERTY_ACTION_CREATE );
			// urlToSendingForm.addParameter( PARAM_FORMAT, strFormat );
			// urlToSendingForm.addParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID, nGreetingsCardTemplateId );
			// urlToSendingForm.addParameter( PARAM_SENDED, 1 );
		}

		Object[] tabFields =
		{ nMailSended, distinctRecipient.size( ) };

		return AdminMessageService.getMessageUrl( request, MESSAGE_MAIL_SEND, tabFields, AdminMessage.TYPE_INFO );

		// return getJspManageGreetingsCard( multipartRequest );
	}

	/**
	 * Get the export greetings card page
	 * @param request The request
	 * @return The HTML content of the page
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String getExportGreetingsCard( HttpServletRequest request ) throws AccessDeniedException
	{
		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_EXPORT, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}
		String strGreetingsCardTemplateId = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );

		HashMap<String, Object> model = new HashMap<String, Object>( );
		ReferenceList refListXslExport = XslExportHome.getRefList( );

		model.put( MARK_GREETINGS_CARD_TEMPLATE_ID, strGreetingsCardTemplateId );
		model.put( MARK_LIST_XSL_EXPORT, refListXslExport );
		HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EXPORT_GREETINGS_CARD, request.getLocale( ), model );

		return getAdminPage( template.getHtml( ) );
	}

	/**
	 * Download data of a greetings card
	 * @param request The request
	 * @param response The response
	 * @throws IOException Throw an exception if the outputstream has error.
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public void doExportGreetingsCard( HttpServletRequest request, HttpServletResponse response ) throws IOException, AccessDeniedException
	{
		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_EXPORT, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}
		String strGreetingsCardTemplateId = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
		String strXslExportId = request.getParameter( PARAM_XSL_EXPORT_ID );
		String strExportedGreetingsCard = StringUtils.EMPTY;

		int nGreetingsCardTemplateId = Integer.parseInt( strGreetingsCardTemplateId );
		int nXslExportId = Integer.parseInt( strXslExportId );

		XslExport xslExport = XslExportHome.findByPrimaryKey( nXslExportId );
		GreetingsCardTemplate gct = GreetingsCardTemplateHome.findByPrimaryKey( nGreetingsCardTemplateId, getPlugin( ) );
		Collection<GreetingsCard> listGreetingsCards = GreetingsCardHome.findByGreetingsCardTemplateId( nGreetingsCardTemplateId, getPlugin( ) );

		if ( listGreetingsCards != null && listGreetingsCards.size( ) > 0 )
		{
			StringBuilder sb = new StringBuilder( XmlUtil.getXmlHeader( ) );
			sb.append( "<GreetingsCards>\n" );
			for ( GreetingsCard greetingsCard : listGreetingsCards )
			{
				sb.append( greetingsCard.getXmlWithoutHeaderForStats( ) );
			}
			sb.append( "</GreetingsCards>\n" );
			String strXml = StringUtil.replaceAccent( sb.toString( ) );
			strExportedGreetingsCard = XslExportService.exportXMLWithXSL( nXslExportId, strXml );

		}
		if ( CONSTANT_MIME_TYPE_CSV.contains( xslExport.getExtension( ) ) )
		{
			response.setContentType( CONSTANT_MIME_TYPE_CSV );
		}
		else if ( CONSTANT_EXTENSION_XML_FILE.contains( xslExport.getExtension( ) ) )
		{
			response.setContentType( CONSTANT_MIME_TYPE_XML );
		}
		else
		{
			response.setContentType( CONSTANT_MIME_TYPE_OCTETSTREAM );
		}
		String strFileName = StringUtil.replaceAccent( gct.getDescription( ) ).trim( ).replace( ' ', '_' ) + "." + xslExport.getExtension( );
		response.setHeader( "Content-Disposition", "attachement; filename=\"" + strFileName + "\"" );
		PrintWriter out = response.getWriter( );
		out.write( strExportedGreetingsCard );
		out.flush( );
		out.close( );
	}

	/**
	 * Get the archive greetings cards page
	 * @param request The request
	 * @return The HTML content
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String getArchiveGreetingsCard( HttpServletRequest request ) throws AccessDeniedException
	{
		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_ARCHIVE, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}
		String strIdGreetingsCardTemplate = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
		HashMap<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_GREETINGS_CARD_TEMPLATE_ID, strIdGreetingsCardTemplate );
		Locale locale = request.getLocale( );
		if ( locale == null )
		{
			locale = I18nService.getDefaultLocale( );
		}
		model.put( MARK_LOCALE, locale );
		HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ARCHIVE_GREETINGS_CARD, request.getLocale( ), model );

		return getAdminPage( template.getHtml( ) );
	}

	/**
	 * Archive greetings cards
	 * @param request The request
	 * @return The URL describing the result of the action
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String doArchiveGreetingsCard( HttpServletRequest request ) throws AccessDeniedException
	{
		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_ARCHIVE, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}
		String strIdGreetingsCardTemplate = request.getParameter( PARAM_GREETINGS_CARD_TEMPLATE_ID );
		String strDateMin = request.getParameter( PARAM_DATE_MIN );
		String strDateMax = request.getParameter( PARAM_DATE_MAX );
		String strYear = request.getParameter( PARAM_YEAR );

		int nIdGCT = 0;
		if ( StringUtils.isNotBlank( strIdGreetingsCardTemplate ) )
		{
			try
			{
				nIdGCT = Integer.parseInt( strIdGreetingsCardTemplate );
			}
			catch ( NumberFormatException e )
			{
				nIdGCT = 0;
			}
		}

		int nYear = 0;
		try
		{
			nYear = Integer.parseInt( strYear );
		}
		catch ( NumberFormatException e )
		{
			Object[] tabRequiredFields =
			{ I18nService.getLocalizedString( MANDATORY_YEAR, request.getLocale( ) ) };

			return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
		}

		DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.SHORT, request.getLocale( ) );
		dateFormat.setLenient( false );
		Date dateMin = null;
		Date dateMax = null;
		try
		{
			if ( StringUtils.isNotBlank( strDateMin ) )
			{
				dateMin = dateFormat.parse( strDateMin );
			}
			if ( StringUtils.isNotBlank( strDateMax ) )
			{
				dateMax = dateFormat.parse( strDateMax );
			}
		}
		catch ( ParseException e )
		{
			AppLogService.error( e.getMessage( ), e );
			Object[] messageArgs =
			{ ( ( SimpleDateFormat ) dateFormat ).toPattern( ) };
			return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PARSE_DATE, messageArgs, AdminMessage.TYPE_ERROR );
		}
		int nNbArchivedCards = _greetingsCardService.archiveGreetingsCards( nIdGCT, nYear, dateMin, dateMax, getPlugin( ) );

		Object[] messageArgs =
		{ nNbArchivedCards };
		return AdminMessageService.getMessageUrl( request, MESSAGE_ARCHIVED_OK, messageArgs, URL_JSP_LIST_TEMPLATES, AdminMessage.TYPE_INFO );
	}

	/**
	 * Update the date of next automatic archiving of greetings cards
	 * @param request The request
	 * @return The URL describing the result of the action
	 * @throws AccessDeniedException the exception for AccessDeniedException
	 */
	public String doUpdateArchiveAutoGreetingsCard( HttpServletRequest request ) throws AccessDeniedException
	{
		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_ARCHIVE, getUser( ) ) )
		{
			throw new AccessDeniedException( MESSAGE_FORBIDEN );
		}

		String strYearNextAutoArchiving = request.getParameter( PARAM_YEAR_NEXT_AUTO_ARCHIVING );
		String strDateNextAutoArchiving = request.getParameter( PARAM_NEXT_AUTO_ARCHIVING );
		try
		{
			Integer.parseInt( strYearNextAutoArchiving );
		}
		catch ( NumberFormatException e )
		{
			Object[] messageArgs =
			{ I18nService.getLocalizedString( LABEL_YEAR, request.getLocale( ) ) };
			return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PARSE_NUMBER, messageArgs, AdminMessage.TYPE_STOP );
		}
		if ( StringUtils.isNotBlank( strDateNextAutoArchiving ) )
		{
			DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.SHORT, Locale.FRANCE );
			dateFormat.setLenient( false );
			try
			{
				dateFormat.parse( strDateNextAutoArchiving );
			}
			catch ( ParseException e )
			{
				AppLogService.error( e.getMessage( ), e );
				Object[] messageArgs =
				{ ( ( SimpleDateFormat ) dateFormat ).toPattern( ) };
				return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PARSE_DATE, messageArgs, AdminMessage.TYPE_ERROR );
			}
		}
		else
		{
			strDateNextAutoArchiving = StringUtils.EMPTY;
		}
		DatastoreService.setDataValue( PARAMETER_NEXT_AUTO_ARCHIVING, strDateNextAutoArchiving );
		DatastoreService.setDataValue( PARAMETER_YEAR_NEXT_AUTO_ARCHIVING, strYearNextAutoArchiving );

		return AdminMessageService.getMessageUrl( request, MESSAGE_DATE_AUTO_ARCHIVE_UPDATED, URL_JSP_LIST_TEMPLATES, AdminMessage.TYPE_INFO );
	}

	/**
	 * Return the list of greetings card template
	 * @return the list of greetings card template
	 */
	private ReferenceList getGreetingsCardTemplateListe( )
	{
		ReferenceList refListGreetingsCardTemplate = new ReferenceList( );
		Collection<GreetingsCardTemplate> listGCT = GreetingsCardTemplateHome.findAll( getPlugin( ) );

		for ( GreetingsCardTemplate gtc : listGCT )
		{
			ReferenceItem refItem = new ReferenceItem( );
			refItem.setCode( EMPTY_STRING + gtc.getId( ) );
			refItem.setName( gtc.getDescription( ) );
			refListGreetingsCardTemplate.add( refItem );
		}

		return refListGreetingsCardTemplate;
	}

	/**
	 * Return the list of mail
	 * @param fileItem The file which contains mail list
	 * @param request The request
	 * @return the list of mail
	 */
	private List<String> getMaiListFromCSV( FileItem fileItem )
	{
		List<String> listMail = new ArrayList<String>( );
		Character strCsvSeparator = AppPropertiesService.getProperty( PROPERTY_IMPORT_CSV_DELIMITER ).charAt( 0 );

		try
		{
			InputStreamReader inputStreamReader = new InputStreamReader( fileItem.getInputStream( ) );
			CSVReader csvReader = new CSVReader( inputStreamReader, strCsvSeparator, '\"' );

			String[] nextLine;

			while ( ( nextLine = csvReader.readNext( ) ) != null )
			{
				for ( int i = 0; i < nextLine.length; i++ )
				{
					listMail.add( nextLine[i] );
				}
			}
		}

		catch ( IOException e )
		{
			AppLogService.error( e );
		}

		return listMail;
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

	/**
	 * Permet de stocker toutes les permissions afin de gérer les profils au niveau des templates
	 * @param model le hashmap contenant les parametres qui vont être envoyés au template
	 */
	private void addPermissionsToHashmap( Map<String, Object> model )
	{
		/*
		 * Permission de créer
		 */
		boolean bPermissionCreate = true;

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_CREATE, getUser( ) ) )
		{
			bPermissionCreate = false;
		}

		model.put( MARK_PERMISSION_CREATE, bPermissionCreate );

		/*
		 * Permission de modifier
		 */
		boolean bPermissionModify = true;

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_MODIFY, getUser( ) ) )
		{
			bPermissionModify = false;
		}

		model.put( MARK_PERMISSION_MODIFY, bPermissionModify );

		/*
		 * Permission de supprimer
		 */
		boolean bPermissionDelete = true;

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_DELETE, getUser( ) ) )
		{
			bPermissionDelete = false;
		}

		model.put( MARK_PERMISSION_DELETE, bPermissionDelete );

		/*
		 * Permission d'envoyer
		 */
		boolean bPermissionSend = true;

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_SEND, getUser( ) ) )
		{
			bPermissionSend = false;
		}

		model.put( MARK_PERMISSION_SEND, bPermissionSend );

		/*
		 * Permission de voir les stats
		 */
		boolean bPermissionStats = true;

		if ( !RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_SEE_STATS, getUser( ) ) )
		{
			bPermissionStats = false;
		}

		model.put( MARK_PERMISSION_STATS, bPermissionStats );

		/*
		 * Permission to export data
		 */
		boolean bPermissionExport = RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_EXPORT, getUser( ) );
		model.put( MARK_PERMISSION_EXPORT, bPermissionExport );

		/*
		 * Permission to export data
		 */
		boolean bPermissionArchive = RBACService.isAuthorized( GreetingsCardResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, GreetingsCardResourceIdService.PERMISSION_ARCHIVE, getUser( ) );
		model.put( MARK_PERMISSION_ARCHIVE, bPermissionArchive );
	}

	/**
	 * Return the plugin
	 * @return Plugin
	 */
	public Plugin getPlugin( )
	{
		return PluginService.getPlugin( PLUGIN_NAME );
	}
}
