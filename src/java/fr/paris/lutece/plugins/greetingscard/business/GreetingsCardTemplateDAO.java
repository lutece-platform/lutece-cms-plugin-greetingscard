/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.greetingscard.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for GreetingsCardTemplate objects
 */
public final class GreetingsCardTemplateDAO implements IGreetingsCardTemplateDAO
{
	private static final String SQL_QUERY_SELECT = "SELECT id_gct, description, height, width, status, object_email, workgroup_key FROM greetings_card_template WHERE id_gct = ?";
	private static final String SQL_QUERY_INSERT = "INSERT INTO greetings_card_template ( id_gct, description, height, width, status, object_email, workgroup_key ) VALUES ( ?, ?, ?, ?, ?, ?, ? )";
	private static final String SQL_QUERY_DELETE = "DELETE FROM greetings_card_template WHERE id_gct = ?";
	private static final String SQL_QUERY_UPDATE = "UPDATE greetings_card_template SET id_gct = ?, description = ?, height = ?, width = ?, status = ?, object_email = ?, workgroup_key = ? WHERE id_gct = ?";
	private static final String SQL_QUERY_NEW_PRIMARY_KEY = "SELECT max( id_gct ) FROM greetings_card_template";
	private static final String SQL_QUERY_FIND_ALL = "SELECT id_gct, description, height, width, status, workgroup_key FROM greetings_card_template order by id_gct";

	/**
	 * Creates a new DownloadFileDAO object.
	 */
	private GreetingsCardTemplateDAO( )
	{
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// Access methods to data

	/**
	 * Insert a new record in the table.
	 * 
	 * @param greetingsCardTemplate The Instance of the GreetingsCardTemplate object
	 * @param plugin The plugin
	 */
	public void insert( GreetingsCardTemplate greetingsCardTemplate, Plugin plugin )
	{
		greetingsCardTemplate.setId( newPrimaryKey( plugin ) );

		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

		daoUtil.setInt( 1, greetingsCardTemplate.getId( ) );
		daoUtil.setString( 2, greetingsCardTemplate.getDescription( ) );
		daoUtil.setInt( 3, greetingsCardTemplate.getHeight( ) );
		daoUtil.setInt( 4, greetingsCardTemplate.getWidth( ) );

		if ( greetingsCardTemplate.isEnabled( ) )
		{
			daoUtil.setInt( 5, 1 );
		}
		else
		{
			daoUtil.setInt( 5, 0 );
		}

		daoUtil.setString( 6, greetingsCardTemplate.getObjectEmail( ) );
		daoUtil.setString( 7, greetingsCardTemplate.getWorkgroupKey( ) );

		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/**
	 * Delete a record from the table
	 * 
	 * @param nIdGCT The indentifier of the object GreetingsCardTemplate
	 * @param plugin The plugin
	 */
	public void delete( int nIdGCT, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

		daoUtil.setInt( 1, nIdGCT );

		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/**
	 * load the data of GreetingsCardTemplate from the table
	 * 
	 * @param nIdGCT The indentifier of the object GreetingsCardTemplate
	 * @param plugin The plugin
	 * @return The Instance of the object GreetingsCardTemplate
	 */
	public GreetingsCardTemplate load( int nIdGCT, Plugin plugin )
	{
		GreetingsCardTemplate greetingsCardTemplate = new GreetingsCardTemplate( );

		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
		daoUtil.setInt( 1, nIdGCT );

		daoUtil.executeQuery( );

		if ( !daoUtil.next( ) )
		{
			daoUtil.free( );

			return null;
		}
		else
		{
			greetingsCardTemplate.setId( daoUtil.getInt( 1 ) );
			greetingsCardTemplate.setDescription( daoUtil.getString( 2 ) );
			greetingsCardTemplate.setHeight( daoUtil.getInt( 3 ) );
			greetingsCardTemplate.setWidth( daoUtil.getInt( 4 ) );
			greetingsCardTemplate.setStatus( daoUtil.getInt( 5 ) );
			greetingsCardTemplate.setObjectEmail( daoUtil.getString( 6 ) );
			greetingsCardTemplate.setWorkgroupKey( daoUtil.getString( 7 ) );

			// Load greetings cards
			// greetingsCardTemplate.setGreetingsCards( GreetingsCardHome.findByGreetingsCardTemplateId( greetingsCardTemplate.getId( ), plugin ) );
		}

		daoUtil.free( );

		return greetingsCardTemplate;
	}

	/**
	 * Update the record in the table
	 * 
	 * @param greetingsCardTemplate The instance of the GreetingsCardTemplate to update
	 * @param plugin The plugin
	 */
	public void store( GreetingsCardTemplate greetingsCardTemplate, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

		daoUtil.setInt( 1, greetingsCardTemplate.getId( ) );
		daoUtil.setString( 2, greetingsCardTemplate.getDescription( ) );
		daoUtil.setInt( 3, greetingsCardTemplate.getHeight( ) );
		daoUtil.setInt( 4, greetingsCardTemplate.getWidth( ) );

		if ( greetingsCardTemplate.isEnabled( ) )
		{
			daoUtil.setInt( 5, 1 );
		}
		else
		{
			daoUtil.setInt( 5, 0 );
		}

		daoUtil.setString( 6, greetingsCardTemplate.getObjectEmail( ) );
		daoUtil.setString( 7, greetingsCardTemplate.getWorkgroupKey( ) );
		daoUtil.setInt( 8, greetingsCardTemplate.getId( ) );

		daoUtil.executeUpdate( );

		daoUtil.free( );
	}

	/**
	 * Calculate a new primary key to add a new GreetingsCardTemplate
	 * 
	 * @param plugin The plugin
	 * @return The new key.
	 */
	int newPrimaryKey( Plugin plugin )
	{
		int nKey;

		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PRIMARY_KEY, plugin );

		daoUtil.executeQuery( );

		if ( !daoUtil.next( ) )
		{
			// If the table is empty
			nKey = 1;
		}

		nKey = daoUtil.getInt( 1 ) + 1;

		daoUtil.free( );

		return nKey;
	}

	/**
	 * Finds all objects of this type
	 * @param plugin The plugin
	 * @return A collection of objects
	 */
	public Collection<GreetingsCardTemplate> findAll( Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL, plugin );

		ArrayList<GreetingsCardTemplate> list = new ArrayList<GreetingsCardTemplate>( );
		daoUtil.executeQuery( );

		while ( daoUtil.next( ) )
		{
			GreetingsCardTemplate greetingsCardTemplate = new GreetingsCardTemplate( );
			greetingsCardTemplate.setId( daoUtil.getInt( 1 ) );
			greetingsCardTemplate.setDescription( daoUtil.getString( 2 ) );
			greetingsCardTemplate.setHeight( daoUtil.getInt( 3 ) );
			greetingsCardTemplate.setWidth( daoUtil.getInt( 4 ) );
			greetingsCardTemplate.setStatus( daoUtil.getInt( 5 ) );
			greetingsCardTemplate.setWorkgroupKey( daoUtil.getString( 6 ) );

			// Load greetings cards
			// greetingsCardTemplate.setGreetingsCards( GreetingsCardHome.findByGreetingsCardTemplateId(
			// greetingsCardTemplate.getId( ), plugin ) );

			list.add( greetingsCardTemplate );
		}

		daoUtil.free( );

		return list;
	}
}
