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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * This class provides Data Access methods for GreetingsCard objects
 */
public final class GreetingsCardDAO implements IGreetingsCardDAO
{
	// Constants
	private static final String SQL_QUERY_SELECT = "SELECT id_gc, sender_name, sender_email, recipient_email, message, message2, date, id_gct, status, is_copy, notify_user FROM greetings_card WHERE id_gc = ?";
	private static final String SQL_QUERY_INSERT = "INSERT INTO greetings_card ( id_gc, sender_name, sender_email, recipient_email, message, message2, date, sender_ip, id_gct, status, is_copy, notify_user, domain_name ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
	private static final String SQL_QUERY_DELETE = "DELETE FROM greetings_card WHERE id_gc = ?";
	private static final String SQL_QUERY_DELETE_LIST = "DELETE FROM greetings_card WHERE id_gc in ( ";
	private static final String SQL_QUERY_UPDATE = "UPDATE greetings_card SET id_gc = ?, sender_name = ?, sender_email = ?, recipient_email = ?, message = ?, message2 = ?, date = ?, id_gct = ?, sender_ip = ?, status = ?, is_copy = ?, notify_user = ?, domain_name = ? WHERE id_gc = ?";
	private static final String SQL_QUERY_NEW_PRIMARY_KEY = "SELECT id_gc FROM greetings_card WHERE id_gc = ?";
	private static final String SQL_QUERY_FIND_BY_ID = "SELECT id_gc, sender_name, sender_email, recipient_email, date, id_gct, status, is_copy, notify_user FROM greetings_card WHERE id_gct = ?";
	private static final String SQL_QUERY_FIND_ALL = "SELECT id_gc, sender_name, sender_email, recipient_email, message, message2, date, id_gct, status, is_copy, notify_user FROM greetings_card ";
	private static final String SQL_QUERY_FIND_RED_CARD_FOR_NOTIFICATION = "SELECT id_gc, sender_name, sender_email, recipient_email, message, message2, date, id_gct, status, is_copy, notify_user FROM greetings_card WHERE status = ? AND notify_user = 1";
	private static final String SQL_QUERY_FIND_FROM_FILTER = "SELECT gc.recipient_email, gc.status FROM greetings_card as gc LEFT JOIN greetings_card_template as gct ON ( gc.id_gct = gct.id_gct ) ";
	private static final String SQL_QUERY_COUNT_CARDS = "SELECT COUNT(id_gc) FROM greetings_card as gc ";
	private static final String SQL_QUERY_COUNT_CARDS_DOMAIN_NAME = "SELECT COUNT(id_gc), domain_name FROM greetings_card as gc ";
	private static final String SQL_QUERY_FILTER_WORKGROUP = " gct.workgroup_key = ? ";
	private static final String SQL_QUERY_FILTER_BY_ID = " gc.id_gct = ? ";
	private static final String SQL_QUERY_FILTER_COPY = " gc.is_copy = 0 ";
	private static final String SQL_QUERY_FILTER_COPY_SPEC = " is_copy = 0 ";
	private static final String SQL_QUERY_FILTER_IS_RED = " status > 0 ";
	private static final String SQL_QUERY_FILTER_DATE_MIN = " date > ? ";
	private static final String SQL_QUERY_FILTER_DATE_MAX = " date < ? ";
	private static final String SQL_QUERY_LIMIT = " LIMIT ? ";
	private static final String SQL_QUERY_GROUP_BY_DOMAIN_NAME = " GROUP BY domain_name ";
	private static final String SQL_QUERY_ORDER_BY_DOMAIN_NAME = " ORDER BY domain_name ASC ";
	private static final String CONSTANT_WHERE = " WHERE ";
	private static final String CONSTANT_AND = " AND ";
	private static final String CONSTANT_PARENTHESIS = " ) ";
	private static final String ARROBASE = "@";

	/**
	 * Creates a new GreetingsCardDAO object.
	 */
	private GreetingsCardDAO( )
	{
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// Access methods to data

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert( GreetingsCard greetingsCard, Plugin plugin )
	{
		greetingsCard.setId( newPrimaryKey( plugin ) );

		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

		daoUtil.setString( 1, greetingsCard.getId( ) );
		daoUtil.setString( 2, greetingsCard.getSenderName( ) );
		daoUtil.setString( 3, greetingsCard.getSenderEmail( ) );
		daoUtil.setString( 4, greetingsCard.getRecipientEmail( ) );
		daoUtil.setString( 5, greetingsCard.getMessage( ) );
		daoUtil.setString( 6, greetingsCard.getMessage2( ) );
		daoUtil.setDate( 7, greetingsCard.getDate( ) );
		daoUtil.setString( 8, greetingsCard.getSenderIp( ) );
		daoUtil.setInt( 9, greetingsCard.getIdGCT( ) );
		daoUtil.setInt( 10, greetingsCard.getStatus( ) );
		daoUtil.setBoolean( 11, greetingsCard.isCopy( ) );
		daoUtil.setBoolean( 12, greetingsCard.getNotifySender( ) );
		daoUtil.setString( 13, greetingsCard.getDomainName( ) );

		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete( String strIdGC, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

		daoUtil.setString( 1, strIdGC );

		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteList( String strIdGC, Plugin plugin )
	{
		StringBuilder sbSql = new StringBuilder( SQL_QUERY_DELETE_LIST );
		sbSql.append( strIdGC );
		sbSql.append( CONSTANT_PARENTHESIS );

		DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );
		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GreetingsCard load( String strIdGC, Plugin plugin )
	{
		GreetingsCard greetingsCard = new GreetingsCard( );
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );

		daoUtil.setString( 1, strIdGC );
		daoUtil.executeQuery( );

		if ( !daoUtil.next( ) )
		{
			daoUtil.free( );

			return null;
		}
		else
		{
			greetingsCard.setId( daoUtil.getString( 1 ) );
			greetingsCard.setSenderName( daoUtil.getString( 2 ) );
			greetingsCard.setSenderEmail( daoUtil.getString( 3 ) );
			greetingsCard.setRecipientEmail( daoUtil.getString( 4 ) );
			greetingsCard.setMessage( daoUtil.getString( 5 ) );
			greetingsCard.setMessage2( daoUtil.getString( 6 ) );
			greetingsCard.setDate( daoUtil.getDate( 7 ) );
			greetingsCard.setIdGCT( daoUtil.getInt( 8 ) );
			greetingsCard.setStatus( daoUtil.getInt( 9 ) );
			greetingsCard.setCopy( daoUtil.getBoolean( 10 ) );
			greetingsCard.setNotifySender( daoUtil.getBoolean( 11 ) );
		}

		daoUtil.free( );

		return greetingsCard;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void store( GreetingsCard greetingsCard, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

		daoUtil.setString( 1, greetingsCard.getId( ) );
		daoUtil.setString( 2, greetingsCard.getSenderName( ) );
		daoUtil.setString( 3, greetingsCard.getSenderEmail( ) );
		daoUtil.setString( 4, greetingsCard.getRecipientEmail( ) );
		daoUtil.setString( 5, greetingsCard.getMessage( ) );
		daoUtil.setString( 6, greetingsCard.getMessage2( ) );
		daoUtil.setDate( 7, greetingsCard.getDate( ) );
		daoUtil.setInt( 8, greetingsCard.getIdGCT( ) );
		daoUtil.setString( 9, greetingsCard.getSenderIp( ) );
		daoUtil.setInt( 10, greetingsCard.getStatus( ) );
		daoUtil.setBoolean( 11, greetingsCard.isCopy( ) );
		daoUtil.setBoolean( 12, greetingsCard.getNotifySender( ) );
		daoUtil.setString( 13, greetingsCard.getDomainName( ) );
		daoUtil.setString( 14, greetingsCard.getId( ) );

		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/**
	 * Find a free primary key
	 * @param plugin The plugin
	 * @return a new free primary key
	 */
	String newPrimaryKey( Plugin plugin )
	{
		DAOUtil daoUtil;
		StringBuffer strKey;
		int keyExists;
		Random generator = new Random( );
		int kindOfValue;

		do
		{
			keyExists = 0;
			strKey = new StringBuffer( );

			for ( int i = 0; i < 30; i++ )
			{
				kindOfValue = generator.nextInt( 10 );

				if ( ( kindOfValue % 2 ) == 0 )
				{
					strKey.append( generator.nextInt( 10 ) );
				}
				else
				{
					char letter = 'a';
					letter += generator.nextInt( 25 );
					strKey.append( letter );
				}
			}

			daoUtil = new DAOUtil( SQL_QUERY_NEW_PRIMARY_KEY, plugin );
			daoUtil.setString( 1, strKey.toString( ) );

			daoUtil.executeQuery( );

			if ( daoUtil.next( ) )
			{
				keyExists = 1;
			}
		} while ( keyExists == 1 );

		daoUtil.free( );

		return strKey.toString( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<GreetingsCard> findAll( Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL + CONSTANT_WHERE + SQL_QUERY_FILTER_COPY_SPEC, plugin );

		daoUtil.executeQuery( );

		ArrayList<GreetingsCard> list = new ArrayList<GreetingsCard>( );

		while ( daoUtil.next( ) )
		{
			GreetingsCard greetingsCard = new GreetingsCard( );
			greetingsCard.setId( daoUtil.getString( 1 ) );
			greetingsCard.setSenderName( daoUtil.getString( 2 ) );
			greetingsCard.setSenderEmail( daoUtil.getString( 3 ) );
			greetingsCard.setRecipientEmail( daoUtil.getString( 4 ) );
			greetingsCard.setMessage( daoUtil.getString( 5 ) );
			greetingsCard.setMessage2( daoUtil.getString( 6 ) );
			greetingsCard.setDate( daoUtil.getDate( 7 ) );
			greetingsCard.setIdGCT( daoUtil.getInt( 8 ) );
			greetingsCard.setStatus( daoUtil.getInt( 9 ) );
			greetingsCard.setCopy( daoUtil.getBoolean( 10 ) );
			greetingsCard.setNotifySender( daoUtil.getBoolean( 11 ) );
			list.add( greetingsCard );
		}

		daoUtil.free( );

		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<GreetingsCard> findByGreetingsCardTemplateId( int nIdGreetingsCardTemplate, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID, plugin );

		daoUtil.setInt( 1, nIdGreetingsCardTemplate );

		ArrayList<GreetingsCard> list = new ArrayList<GreetingsCard>( );
		daoUtil.executeQuery( );

		while ( daoUtil.next( ) )
		{
			GreetingsCard greetingsCard = new GreetingsCard( );
			greetingsCard.setId( daoUtil.getString( 1 ) );
			greetingsCard.setSenderName( daoUtil.getString( 2 ) );
			greetingsCard.setSenderEmail( daoUtil.getString( 3 ) );
			greetingsCard.setRecipientEmail( daoUtil.getString( 4 ) );
			greetingsCard.setDate( daoUtil.getDate( 5 ) );
			greetingsCard.setIdGCT( daoUtil.getInt( 6 ) );
			greetingsCard.setStatus( daoUtil.getInt( 7 ) );
			greetingsCard.setCopy( daoUtil.getBoolean( 8 ) );
			greetingsCard.setNotifySender( daoUtil.getBoolean( 9 ) );
			list.add( greetingsCard );
		}

		daoUtil.free( );

		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> findDomainNameOfMailSent( GreetingsCardFilter greetingsCardFilter, Plugin plugin )
	{
		List<String> listStrFilter = new ArrayList<String>( );

		if ( greetingsCardFilter.containsIdGCT( ) )
		{
			listStrFilter.add( SQL_QUERY_FILTER_BY_ID );
		}

		if ( greetingsCardFilter.containsWorkgroupCriteria( ) )
		{
			listStrFilter.add( SQL_QUERY_FILTER_WORKGROUP );
		}

		listStrFilter.add( SQL_QUERY_FILTER_COPY );

		String strQuery = buildRequetteWithFilter( SQL_QUERY_FIND_FROM_FILTER, listStrFilter );

		DAOUtil daoUtil = new DAOUtil( strQuery, plugin );
		int nIndex = 1;

		if ( greetingsCardFilter.containsIdGCT( ) )
		{
			daoUtil.setInt( nIndex, greetingsCardFilter.getIdGCT( ) );
			nIndex++;
		}

		if ( greetingsCardFilter.containsWorkgroupCriteria( ) )
		{
			daoUtil.setString( nIndex, greetingsCardFilter.getWorkgroup( ) );
			nIndex++;
		}

		List<String> listDomain = new ArrayList<String>( );

		daoUtil.executeQuery( );

		while ( daoUtil.next( ) )
		{
			String strMail = daoUtil.getString( 1 );
			strMail = strMail.substring( strMail.indexOf( ARROBASE ) + 1, strMail.length( ) );

			if ( !listDomain.contains( strMail ) )
			{
				listDomain.add( strMail );
			}
		}

		daoUtil.free( );

		return listDomain;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Integer> findNumberOfMailSentByDomain( GreetingsCardFilter greetingsCardFilter, Plugin plugin )
	{
		List<String> listStrFilter = new ArrayList<String>( );

		if ( greetingsCardFilter.containsIdGCT( ) )
		{
			listStrFilter.add( SQL_QUERY_FILTER_BY_ID );
		}

		if ( greetingsCardFilter.containsWorkgroupCriteria( ) )
		{
			listStrFilter.add( SQL_QUERY_FILTER_WORKGROUP );
		}

		listStrFilter.add( SQL_QUERY_FILTER_COPY );
		String strQuery = buildRequetteWithFilter( SQL_QUERY_COUNT_CARDS_DOMAIN_NAME, listStrFilter );
		strQuery = strQuery + SQL_QUERY_GROUP_BY_DOMAIN_NAME + SQL_QUERY_ORDER_BY_DOMAIN_NAME;

		DAOUtil daoUtil = new DAOUtil( strQuery, plugin );
		int nIndex = 1;

		if ( greetingsCardFilter.containsIdGCT( ) )
		{
			daoUtil.setInt( nIndex, greetingsCardFilter.getIdGCT( ) );
			nIndex++;
		}

		if ( greetingsCardFilter.containsWorkgroupCriteria( ) )
		{
			daoUtil.setString( nIndex, greetingsCardFilter.getWorkgroup( ) );
			nIndex++;
		}

		HashMap<String, Integer> domainNameSentCards = new HashMap<String, Integer>( );

		daoUtil.executeQuery( );
		while ( daoUtil.next( ) )
		{
			domainNameSentCards.put( daoUtil.getString( 2 ), daoUtil.getInt( 1 ) );
		}

		daoUtil.free( );

		return domainNameSentCards;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int findNumberTotalOfMailSentWithoutCopy( Plugin plugin )
	{
		List<String> listStrFilter = new ArrayList<String>( );

		listStrFilter.add( SQL_QUERY_FILTER_COPY );
		String strQuery = buildRequetteWithFilter( SQL_QUERY_COUNT_CARDS, listStrFilter );

		DAOUtil daoUtil = new DAOUtil( strQuery, plugin );

		int nCountDomain = 0;

		daoUtil.executeQuery( );
		if ( daoUtil.next( ) )
		{
			nCountDomain = daoUtil.getInt( 1 );
		}

		daoUtil.free( );

		return nCountDomain;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Integer> findNumberOfMailReadByDomain( GreetingsCardFilter greetingsCardFilter, Plugin plugin )
	{
		List<String> listStrFilter = new ArrayList<String>( );

		if ( greetingsCardFilter.containsIdGCT( ) )
		{
			listStrFilter.add( SQL_QUERY_FILTER_BY_ID );
		}

		if ( greetingsCardFilter.containsWorkgroupCriteria( ) )
		{
			listStrFilter.add( SQL_QUERY_FILTER_WORKGROUP );
		}

		listStrFilter.add( SQL_QUERY_FILTER_COPY );
		listStrFilter.add( SQL_QUERY_FILTER_IS_RED );

		String strQuery = buildRequetteWithFilter( SQL_QUERY_COUNT_CARDS_DOMAIN_NAME, listStrFilter );
		strQuery = strQuery + SQL_QUERY_GROUP_BY_DOMAIN_NAME + SQL_QUERY_ORDER_BY_DOMAIN_NAME;

		DAOUtil daoUtil = new DAOUtil( strQuery, plugin );
		int nIndex = 1;

		if ( greetingsCardFilter.containsIdGCT( ) )
		{
			daoUtil.setInt( nIndex, greetingsCardFilter.getIdGCT( ) );
			nIndex++;
		}

		if ( greetingsCardFilter.containsWorkgroupCriteria( ) )
		{
			daoUtil.setString( nIndex, greetingsCardFilter.getWorkgroup( ) );
			nIndex++;
		}

		HashMap<String, Integer> domainNameReadCards = new HashMap<String, Integer>( );

		daoUtil.executeQuery( );
		while ( daoUtil.next( ) )
		{
			domainNameReadCards.put( daoUtil.getString( 2 ), daoUtil.getInt( 1 ) );
		}

		daoUtil.free( );

		return domainNameReadCards;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<GreetingsCard> findCardsToSendNotification( Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_RED_CARD_FOR_NOTIFICATION, plugin );
		daoUtil.setInt( 1, GreetingsCard.STATUS_RED );
		daoUtil.executeQuery( );

		List<GreetingsCard> list = new ArrayList<GreetingsCard>( );

		while ( daoUtil.next( ) )
		{
			GreetingsCard greetingsCard = new GreetingsCard( );
			greetingsCard.setId( daoUtil.getString( 1 ) );
			greetingsCard.setSenderName( daoUtil.getString( 2 ) );
			greetingsCard.setSenderEmail( daoUtil.getString( 3 ) );
			greetingsCard.setRecipientEmail( daoUtil.getString( 4 ) );
			greetingsCard.setMessage( daoUtil.getString( 5 ) );
			greetingsCard.setMessage2( daoUtil.getString( 6 ) );
			greetingsCard.setDate( daoUtil.getDate( 7 ) );
			greetingsCard.setIdGCT( daoUtil.getInt( 8 ) );
			greetingsCard.setStatus( daoUtil.getInt( 9 ) );
			greetingsCard.setCopy( daoUtil.getBoolean( 10 ) );
			greetingsCard.setNotifySender( daoUtil.getBoolean( 11 ) );
			list.add( greetingsCard );
		}
		daoUtil.free( );

		return list;
	}

	/**
	 * Builds a query with filters placed in parameters
	 * @param strSelect the select of the query
	 * @param listStrFilter the list of filter to add in the query
	 * @return a query
	 */
	private String buildRequetteWithFilter( String strSelect, List<String> listStrFilter )
	{
		StringBuffer strBuffer = new StringBuffer( );
		strBuffer.append( strSelect );

		int nCount = 0;

		for ( String strFilter : listStrFilter )
		{
			if ( ++nCount == 1 )
			{
				strBuffer.append( CONSTANT_WHERE );
			}

			strBuffer.append( strFilter );

			if ( nCount != listStrFilter.size( ) )
			{
				strBuffer.append( CONSTANT_AND );
			}
		}

		return strBuffer.toString( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<GreetingsCard> findByTemplateAndDate( int nIdGreetingsCardTemplate, Date dateMin, Date dateMax, int nResultsLimit, Plugin plugin )
	{
		StringBuilder sbSql = new StringBuilder( SQL_QUERY_FIND_BY_ID );

		if ( dateMin != null )
		{
			sbSql.append( CONSTANT_AND );
			sbSql.append( SQL_QUERY_FILTER_DATE_MIN );
		}
		if ( dateMax != null )
		{
			sbSql.append( CONSTANT_AND );
			sbSql.append( SQL_QUERY_FILTER_DATE_MAX );
		}
		if ( nResultsLimit > 0 )
		{
			sbSql.append( SQL_QUERY_LIMIT );
		}
		DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );
		int nArgs = 1;
		daoUtil.setInt( nArgs++, nIdGreetingsCardTemplate );
		if ( dateMin != null )
		{
			daoUtil.setDate( nArgs++, new java.sql.Date( dateMin.getTime( ) ) );
		}
		if ( dateMax != null )
		{
			daoUtil.setDate( nArgs++, new java.sql.Date( dateMax.getTime( ) ) );
		}
		if ( nResultsLimit > 0 )
		{
			daoUtil.setInt( nArgs++, nResultsLimit );
		}

		ArrayList<GreetingsCard> list = new ArrayList<GreetingsCard>( );
		daoUtil.executeQuery( );

		while ( daoUtil.next( ) )
		{
			GreetingsCard greetingsCard = new GreetingsCard( );
			greetingsCard.setId( daoUtil.getString( 1 ) );
			greetingsCard.setSenderName( daoUtil.getString( 2 ) );
			greetingsCard.setSenderEmail( daoUtil.getString( 3 ) );
			greetingsCard.setRecipientEmail( daoUtil.getString( 4 ) );
			greetingsCard.setDate( daoUtil.getDate( 5 ) );
			greetingsCard.setIdGCT( daoUtil.getInt( 6 ) );
			greetingsCard.setStatus( daoUtil.getInt( 7 ) );
			greetingsCard.setCopy( daoUtil.getBoolean( 8 ) );
			greetingsCard.setNotifySender( daoUtil.getBoolean( 9 ) );
			list.add( greetingsCard );
		}

		daoUtil.free( );

		return list;
	}

}
