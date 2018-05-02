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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.filesystem.DirectoryNotFoundException;
import fr.paris.lutece.util.filesystem.FileSystemUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.fileupload.FileItem;


/**
 * This class provides instances management methods (create, find, ...) for GreetingsCardTemplate objects
 */
public final class GreetingsCardTemplateHome
{
	// Static variable pointed at the DAO instance
	private static IGreetingsCardTemplateDAO _dao = SpringContextService.getBean( "greetingscardTemplateDAO" );

	/** key to get the greetings card root directory path. */
	private static final String PROPERTY_PATH_GREETINGS_CARD_TEMPLATE = "greetingscard.path.greetingscardtemplates";
	private static final String PROPERTY_FILE_NAME_PICTURE = "greetingscard.getPicture.FileName";
	private static final String PATH_SEPARATOR = "/";
	private static final String EMPTY_STRING = "";

	/**
	 * Private constructor - this class need not be instantiated.
	 */
	private GreetingsCardTemplateHome( )
	{
	}

	/**
	 * Creation of an instance of an article GreetingsCardTemplate
	 * 
	 * @param greetingsCardTemplate An instance of the GreetingsCardTemplate which contains the informations to store
	 * @param plugin The plugin
	 * @return The instance of the GreetingsCardTemplate which has been created
	 */
	public static GreetingsCardTemplate create( GreetingsCardTemplate greetingsCardTemplate, Plugin plugin )
	{
		_dao.insert( greetingsCardTemplate, plugin );

		return greetingsCardTemplate;
	}

	/**
	 * Updates of the GreetingsCardTemplate instance specified in parameter
	 * 
	 * @param greetingsCardTemplate An instance of the GreetingsCardTemplate which contains the informations to store
	 * @param plugin The plugin
	 * @return The instance of the GreetingsCardTemplate which has been updated.
	 */
	public static GreetingsCardTemplate update( GreetingsCardTemplate greetingsCardTemplate, Plugin plugin )
	{
		_dao.store( greetingsCardTemplate, plugin );

		return greetingsCardTemplate;
	}

	/**
	 * Deletes the GreetingsCardTemplate instance whose identifier is specified in parameter
	 * 
	 * @param nIdGCT The identifier of the article GreetingsCardTemplate to delete in the database
	 * @param plugin The plugin
	 */
	public static void remove( int nIdGCT, Plugin plugin )
	{
		GreetingsCardArchiveHome.removeByIdTemplate( nIdGCT, plugin );
		_dao.delete( nIdGCT, plugin );
	}

	// /////////////////////////////////////////////////////////////////////////
	// Finders

	/**
	 * Returns an instance of the article GreetingsCardTemplate whose identifier is specified in parameter
	 * 
	 * @param nKey The primary key of the article to find in the database
	 * @param plugin The plugin
	 * @return An instance of the GreetingsCardTemplate which corresponds to the key
	 */
	public static GreetingsCardTemplate findByPrimaryKey( int nKey, Plugin plugin )
	{
		return _dao.load( nKey, plugin );
	}

	/**
	 * Returns GreetingsCardTemplate list
	 * 
	 * @param plugin The plugin
	 * @return the list of the GreetingsCardTemplate of the database in form of a GreetingsCardTemplate Collection object
	 */
	public static Collection<GreetingsCardTemplate> findAll( Plugin plugin )
	{
		return _dao.findAll( plugin );
	}

	/**
	 * Add a new Greetings card Template
	 * @param fFile The source file
	 * @param fFileDest The new file which will create
	 * @throws IOException For file access exception
	 */
	public static void addGreetingsCardTemplate( FileItem fFile, File fFileDest ) throws IOException
	{
		FileOutputStream fos = new FileOutputStream( fFileDest );
		fos.flush( );
		fos.write( fFile.get( ) );
		fos.close( );
	}

	/**
	 * Update a greetings card template
	 * @param fFileItem The source file
	 * @param fFileDest The new file which will update
	 * @throws IOException For file access exception
	 */
	public static void updateGreetingsCardTemplate( FileItem fFileItem, File fFileDest ) throws IOException
	{
		String strFile = fFileDest.getAbsolutePath( );
		removeGreetingsCardTemplate( strFile );

		addGreetingsCardTemplate( fFileItem, fFileDest );
	}

	/**
	 * Remove a greetings card template
	 * @param strFile The name of the deleted file
	 */
	public static void removeGreetingsCardTemplate( String strFile )
	{
		File fFile = new File( strFile );

		if ( fFile.exists( ) )
		{
			fFile.delete( );
		}
	}

	/**
	 * Add a new directory.
	 * 
	 * @param strDirName The name of the new directory.
	 */
	public static void addDirectory( String strDirName )
	{
		// Load the parameters of the greetings card plugin
		String strPathGreetingsCardTemplate = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE );
		String strRootDirectory = AppPathService.getAbsolutePathFromRelativePath( PATH_SEPARATOR + strPathGreetingsCardTemplate );

		// Create the parent Directory if it doesn't exist.
		File fParentDirectory = new File( strRootDirectory );

		if ( !fParentDirectory.exists( ) )
		{
			fParentDirectory.mkdir( );
		}

		// Create the Directory if it doesn't exist.
		File fDirectory = new File( strRootDirectory + PATH_SEPARATOR + strDirName );

		if ( !fDirectory.exists( ) )
		{
			fDirectory.mkdir( );
		}
	}

	/**
	 * Remove a directory.
	 * 
	 * @param strDirName The name of the directory.
	 */
	public static void removeDirectory( String strDirName )
	{
		// Load the parameters of the greetings card plugin
		String strPathGreetingsCardTemplate = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE );
		String strRootDirectory = AppPathService.getAbsolutePathFromRelativePath( PATH_SEPARATOR + strPathGreetingsCardTemplate );

		// Remove the Directory if it exists
		File fDirectory = new File( strRootDirectory + PATH_SEPARATOR + strDirName );

		if ( fDirectory.exists( ) )
		{
			fDirectory.delete( );
		}
	}

	/**
	 * Get the picture of a greetings card template
	 * 
	 * @param pathDirectory The directory.
	 * @param strPluginName The name of the plugin.
	 * @return The requested images.
	 * @throws DirectoryNotFoundException if the directory does not exist.
	 */
	public static String getPicture( String pathDirectory, String strPluginName ) throws DirectoryNotFoundException
	{
		// Load the parameters of the greetings card plugin
		String strPathGreetingsCardTemplate = AppPropertiesService.getProperty( PROPERTY_PATH_GREETINGS_CARD_TEMPLATE );
		String strRootDirectory = AppPathService.getAbsolutePathFromRelativePath( PATH_SEPARATOR + strPathGreetingsCardTemplate + PATH_SEPARATOR );

		String strFileNamePicture = AppPropertiesService.getProperty( PROPERTY_FILE_NAME_PICTURE );

		List<File> allFiles;
		String strPictureName = EMPTY_STRING;

		allFiles = FileSystemUtil.getFiles( strRootDirectory, pathDirectory );

		for ( File fFile : allFiles )
		{
			strPictureName = fFile.getName( );

			if ( strPictureName.substring( 0, strFileNamePicture.length( ) ).equals( strFileNamePicture ) )
			{
				break;
			}
		}

		return strPictureName;
	}

	/**
	 * Copy the Greetings Card Template
	 * 
	 * @param gct The Greetings Card Template
	 * @param plugin The Plugin
	 */
	public static void copy( GreetingsCardTemplate gct, Plugin plugin )
	{
		_dao.insert( gct, plugin );
	}
}
