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
