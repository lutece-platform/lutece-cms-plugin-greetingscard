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
package fr.paris.lutece.plugins.greetingscard.service;

import fr.paris.lutece.plugins.greetingscard.business.GreetingsCard;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardArchive;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardArchiveHome;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardHome;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardTemplate;
import fr.paris.lutece.plugins.greetingscard.business.GreetingsCardTemplateHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


/**
 * Greeting card service
 */
public class GreetingsCardService
{
	public static final String beanName = "greetingsCardService";

	private static final String PROPERTY_NB_LIMIT_ROWS = "greetingscard.archive.nbLimitRows";
	private static final String PROPERTY_ADRESS_EMAIL1 = "greetingscard.isInternal.Email1";
	private static final String PROPERTY_ADRESS_EMAIL2 = "greetingscard.isInternal.Email2";

	private static final String CONSTANT_AT = "@";
	private static final String CONSTANT_COMA = ", ";
	private static final String CONSTANT_APOSTROPHE = "'";

	/**
	 * Archive greetings card by template and date
	 * @param nIdGreetingsCardTemplate The greetings card template id. If 0, then every templates are archived
	 * @param nYear Year associated to created archives
	 * @param dateMin Minimum sent date of greetings cards to archive
	 * @param dateMax Maximum sent date of greetings cards to archive
	 * @param plugin The plugin
	 * @return The number of cards archived
	 */
	public int archiveGreetingsCards( int nIdGreetingsCardTemplate, int nYear, Date dateMin, Date dateMax, Plugin plugin )
	{
		int nResultsLimit = AppPropertiesService.getPropertyInt( PROPERTY_NB_LIMIT_ROWS, 0 );
		Collection<GreetingsCard> listGreetingsCard;
		Collection<GreetingsCardTemplate> listGreetingsCardTemplate;
		if ( nIdGreetingsCardTemplate > 0 )
		{
			listGreetingsCardTemplate = new ArrayList<GreetingsCardTemplate>( );
			GreetingsCardTemplate gct = new GreetingsCardTemplate( );
			gct.setId( nIdGreetingsCardTemplate );
			listGreetingsCardTemplate.add( gct );
		}
		else
		{
			listGreetingsCardTemplate = GreetingsCardTemplateHome.findAll( plugin );
		}
		int nIdGCT;
		int nNbArchivedCards = 0;
		for ( GreetingsCardTemplate gct : listGreetingsCardTemplate )
		{
			nIdGCT = gct.getId( );
			Collection<GreetingsCardArchive> listArchives = GreetingsCardArchiveHome.findByTemplateIdAndYear( nIdGCT, nYear, plugin );

			while ( !( listGreetingsCard = GreetingsCardHome.findByTemplateAndDate( nIdGCT, dateMin, dateMax, nResultsLimit, plugin ) ).isEmpty( ) )
			{
				StringBuilder sbIdGC = null;
				for ( GreetingsCard greetingsCard : listGreetingsCard )
				{
					// We ignore copies
					if ( !greetingsCard.isCopy( ) )
					{
						String domainName = getDomainNameFromEmail( greetingsCard.getRecipientEmail( ) );
						GreetingsCardArchive archive = getArchiveFromDomainName( listArchives, domainName );
						if ( archive == null )
						{
							archive = new GreetingsCardArchive( );
							archive.setDomainName( domainName );
							archive.setYearArchive( nYear );
							archive.setIdGCT( nIdGCT );
							listArchives.add( archive );
						}
						archive.setNbCard( archive.getNbCard( ) + 1 );
						if ( greetingsCard.isRead( ) )
						{
							archive.setNbCardRed( archive.getNbCardRed( ) + 1 );
						}
					}

					if ( sbIdGC == null )
					{
						sbIdGC = new StringBuilder( );
					}
					else
					{
						sbIdGC.append( CONSTANT_COMA );
					}
					sbIdGC.append( CONSTANT_APOSTROPHE );
					sbIdGC.append( greetingsCard.getId( ) );
					sbIdGC.append( CONSTANT_APOSTROPHE );
				}
				GreetingsCardHome.removeList( sbIdGC.toString( ), plugin );
				nNbArchivedCards += listGreetingsCard.size( );
			}

			for ( GreetingsCardArchive archive : listArchives )
			{
				if ( archive.getIdArchive( ) > 0 )
				{
					GreetingsCardArchiveHome.update( archive, plugin );
				}
				else
				{
					GreetingsCardArchiveHome.insert( archive, plugin );
				}
			}
		}

		return nNbArchivedCards;
	}

	/**
	 * Test if the greeting card was sent to an internal person.
	 * @return the value of the test.
	 */
	public boolean isInternal( String strDomainName )
	{
		String strEndEmail1 = AppPropertiesService.getProperty( PROPERTY_ADRESS_EMAIL1 );
		String strEndEmail2 = AppPropertiesService.getProperty( PROPERTY_ADRESS_EMAIL2 );

		if ( StringUtils.isNotBlank( strDomainName ) && ( strDomainName.equals( strEndEmail1 ) || strDomainName.equals( strEndEmail2 ) ) )
		{
			return true;
		}

		return false;
	}

	private String getDomainNameFromEmail( String strEmail )
	{
		if ( strEmail == null || !strEmail.contains( CONSTANT_AT ) )
		{
			return StringUtils.EMPTY;
		}
		return strEmail.substring( strEmail.indexOf( CONSTANT_AT ) );
	}

	private GreetingsCardArchive getArchiveFromDomainName( Collection<GreetingsCardArchive> listArchives, String strDomainName )
	{
		for ( GreetingsCardArchive greetingsCardArchive : listArchives )
		{
			if ( StringUtils.equals( greetingsCardArchive.getDomainName( ), strDomainName ) )
			{
				return greetingsCardArchive;
			}
		}
		return null;
	}
}
