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

import java.util.Collection;


public interface IGreetingsCardArchiveDAO
{
	/**
	 * Creates a greetings card in the database
	 * @param greetingsCardArchive GreetingsCardarchive to inser
	 * @param plugin The plugin
	 */
	void insert( GreetingsCardArchive greetingsCardArchive, Plugin plugin );

	/**
	 * Load a greetings card archive from the database
	 * @param nIdArchive Id of the archive to load
	 * @param plugin The plugin
	 * @return The Greetings card archive
	 */
	GreetingsCardArchive findById( int nIdArchive, Plugin plugin );

	/**
	 * Returns the list of greetings cards archives of a greetings card template
	 * @param nIdGreetingsCardTemplate The greetings card template identifier
	 * @param nYear Year of the archives. If the year is 0, then it is ignored
	 * @param plugin The plugin
	 * @return A Collection of greetings cards archives
	 */
	Collection<GreetingsCardArchive> findByTemplateIdAndYear( int nIdGreetingsCardTemplate, int nYear, Plugin plugin );

	/**
	 * Returns the list of greetings cards archives of a given year
	 * @param findByYear The year
	 * @param plugin The plugin
	 * @return A Collection of greetings cards archives
	 */
	Collection<GreetingsCardArchive> findByYear( int year, Plugin plugin );

	/**
	 * Update a greetings card archive
	 * @param greetingsCardArchive Greetings card archive to update
	 * @param plugin The plugin
	 */
	void update( GreetingsCardArchive greetingsCardArchive, Plugin plugin );

	/**
	 * Remove a greetings card archive
	 * @param nIdArchive Id of the archive to remove
	 * @param plugin The plugin
	 */
	void remove( int nIdArchive, Plugin plugin );

	/**
	 * Remove every archive from the database
	 * @param plugin The plugin
	 */
	void removeAllArchives( Plugin plugin );

	/**
	 * Remove every archive associated to a greetings card template
	 * @param nGreetingsCardTemplate Id of the template
	 * @param plugin The plugin
	 */
	void removeByIdTemplate( int nGreetingsCardTemplate, Plugin plugin );

	/**
	 * Get the list of every year archived
	 * @param plugin The plugin
	 * @return A collection containing every year archived.
	 */
	Collection<Integer> getYearList( Plugin plugin );
}
