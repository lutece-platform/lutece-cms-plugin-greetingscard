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
package fr.paris.lutece.plugins.greetingscard.service;

import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardTemplate;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardTemplateHome;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;


/**
 * Class GreetingsCardResourceIdService
 */
public class GreetingsCardResourceIdService extends ResourceIdService
{
	public static final String PLUGIN_NAME = "greetingscard";
	public static final String RESOURCE_TYPE = "GREETINGS_CARD_TYPE";
	public static final String PERMISSION_CREATE = "CREATE_CARD";
	public static final String PERMISSION_SEND = "SEND_CARD";
	public static final String PERMISSION_MODIFY = "MODIFY_CARD";
	public static final String PERMISSION_DELETE = "DELETE_CARD";
	public static final String PERMISSION_SEE_STATS = "SEE_STATS";
	public static final String PERMISSION_EXPORT = "EXPORT";
	private static final String PROPERTY_LABEL_CREATE = "greetingscard.permission.label.create_greetingscard";
	private static final String PROPERTY_LABEL_MODIFY = "greetingscard.permission.label.modify_greetingscard";
	private static final String PROPERTY_LABEL_SEND = "greetingscard.permission.label.send_greetingscard";
	private static final String PROPERTY_LABEL_SEE_STATS = "greetingscard.permission.label.stats_greetingscard";
	private static final String PROPERTY_LABEL_DELETE = "greetingscard.permission.label.delete_greetingscard";
	private static final String PROPERTY_LABEL_RESOURCE_TYPE = "greetingscard.permission.label.resource_type_greetingscard";
	private static final String PROPERTY_LABEL_EXPORT = "greetingscard.permission.label.export_greetingscard";

	/**
	 * Return the ResourceIdList
	 * @param local The Locale
	 * @return the ResourceIdList
	 */
	@Override
	public ReferenceList getResourceIdList( Locale local )
	{
		return null;
	}

	/**
	 * Return the title
	 * @param strId The Id
	 * @param local The Locale
	 * @return The title
	 */
	@Override
	public String getTitle( String strId, Locale local )
	{
		int nIdGreetingCardTemplate = Integer.parseInt( strId );
		GreetingsCardTemplate greetingsCardTemplate = GreetingsCardTemplateHome.findByPrimaryKey( nIdGreetingCardTemplate, PluginService.getPlugin( PLUGIN_NAME ) );

		return ( greetingsCardTemplate != null ) ? greetingsCardTemplate.getDescription( ) : null;
	}

	/**
	 * Register permission
	 */
	@Override
	public void register( )
	{
		ResourceType rt = new ResourceType( );
		rt.setResourceIdServiceClass( GreetingsCardResourceIdService.class.getName( ) );
		rt.setPluginName( PLUGIN_NAME );
		rt.setResourceTypeKey( RESOURCE_TYPE );
		rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

		Permission p = new Permission( );
		p.setPermissionKey( PERMISSION_CREATE );
		p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
		rt.registerPermission( p );

		p = new Permission( );
		p.setPermissionKey( PERMISSION_MODIFY );
		p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
		rt.registerPermission( p );

		p = new Permission( );
		p.setPermissionKey( PERMISSION_DELETE );
		p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
		rt.registerPermission( p );

		p = new Permission( );
		p.setPermissionKey( PERMISSION_SEND );
		p.setPermissionTitleKey( PROPERTY_LABEL_SEND );
		rt.registerPermission( p );

		p = new Permission( );
		p.setPermissionKey( PERMISSION_SEE_STATS );
		p.setPermissionTitleKey( PROPERTY_LABEL_SEE_STATS );
		rt.registerPermission( p );

		p = new Permission( );
		p.setPermissionKey( PERMISSION_EXPORT );
		p.setPermissionTitleKey( PROPERTY_LABEL_EXPORT );
		rt.registerPermission( p );

		ResourceTypeManager.registerResourceType( rt );
	}
}
