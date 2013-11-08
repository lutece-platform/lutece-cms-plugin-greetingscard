/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 * Home for greeting card archive
 */
public class GreetingsCardArchiveHome
{
	private static IGreetingsCardArchiveDAO _dao = SpringContextService.getBean( "greetingsCardArchiveDAO" );

	/**
	 * Creates a greetings card in the database
	 * @param greetingsCardArchive GreetingsCardarchive to inser
	 * @param plugin The plugin
	 */
	public static void insert( GreetingsCardArchive greetingsCardArchive, Plugin plugin )
	{
		_dao.insert( greetingsCardArchive, plugin );
	}

	/**
	 * Load a greetings card archive from the database
	 * @param nIdArchive Id of the archive to load
	 * @param plugin The plugin
	 * @return The Greetings card archive
	 */
	public static GreetingsCardArchive findById( int nIdArchive, Plugin plugin )
	{
		return _dao.findById( nIdArchive, plugin );
	}

	/**
	 * Returns the list of greetings cards archives of a greetings card template
	 * @param nIdGreetingsCardTemplate The greetings card template identifier
	 * @param nYear Year of the archives. If the year is 0, then it is ignored
	 * @param plugin The plugin
	 * @return A Collection of greetings cards archives
	 */
	public static Collection<GreetingsCardArchive> findByTemplateIdAndYear( int nIdGreetingsCardTemplate, int nYear, Plugin plugin )
	{
		return _dao.findByTemplateIdAndYear( nIdGreetingsCardTemplate, nYear, plugin );
	}

	/**
	 * Returns the list of greetings cards archives of a given year
	 * @param findByYear The year
	 * @param plugin The plugin
	 * @return A Collection of greetings cards archives
	 */
	public static Collection<GreetingsCardArchive> findByYear( int year, Plugin plugin )
	{
		return _dao.findByYear( year, plugin );
	}

	/**
	 * Update a greetings card archive
	 * @param greetingsCardArchive Greetings card archive to update
	 * @param plugin The plugin
	 */
	public static void update( GreetingsCardArchive greetingsCardArchive, Plugin plugin )
	{
		_dao.update( greetingsCardArchive, plugin );
	}

	/**
	 * Remove a greetings card archive
	 * @param nIdArchive Id of the archive to remove
	 * @param plugin The plugin
	 */
	public static void remove( int nIdArchive, Plugin plugin )
	{
		_dao.remove( nIdArchive, plugin );
	}

	/**
	 * Remove every archive from the database
	 * @param plugin The plugin
	 */
	public static void removeAllArchives( Plugin plugin )
	{
		_dao.removeAllArchives( plugin );
	}

	/**
	 * Remove every archive associated to a greetings card template
	 * @param nGreetingsCardTemplate Id of the template
	 * @param plugin The plugin
	 */
	public static void removeByIdTemplate( int nGreetingsCardTemplate, Plugin plugin )
	{
		_dao.removeByIdTemplate( nGreetingsCardTemplate, plugin );
	}

	/**
	 * Get the list of every year archived
	 * @param plugin The plugin
	 * @return A reference list containing every year archived.
	 */
	public static ReferenceList getYearList( Plugin plugin )
	{
		Collection<Integer> listYear = _dao.getYearList( plugin );
		ReferenceList refListYear = new ReferenceList( );
		for ( Integer nYear : listYear )
		{
			ReferenceItem item = new ReferenceItem( );
			String strYear = String.valueOf( nYear );
			item.setCode( strYear );
			item.setName( strYear );
			refListYear.add( item );
		}
		return refListYear;
	}
}
