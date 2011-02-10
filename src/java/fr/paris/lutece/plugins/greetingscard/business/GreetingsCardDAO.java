/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
import java.util.List;
import java.util.Random;


/**
 * This class provides Data Access methods for GreetingsCard objects
 */
public final class GreetingsCardDAO implements IGreetingsCardDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_gc, sender_name, sender_email, recipient_email, message, message2, date, id_gct, is_read, is_copy FROM greetings_card WHERE id_gc = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO greetings_card ( id_gc, sender_name, sender_email, recipient_email, message, message2, date, sender_ip, id_gct, is_read, is_copy ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM greetings_card WHERE id_gc = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE greetings_card SET id_gc = ?, sender_name = ?, sender_email = ?, recipient_email = ?, message = ?, message2 = ?, date = ?, id_gct = ?, sender_ip = ?, is_read = ?, is_copy = ? WHERE id_gc = ?";
    private static final String SQL_QUERY_NEW_PRIMARY_KEY = "SELECT id_gc, sender_name, sender_email, recipient_email, message, message2, id_gct, is_read, is_copy FROM greetings_card WHERE id_gc = ?";
    private static final String SQL_QUERY_FIND_BY_ID = "SELECT id_gc, sender_name, sender_email, recipient_email, message, message2, date, id_gct, is_read, is_copy FROM greetings_card WHERE id_gct = ?";
    private static final String SQL_QUERY_FIND_ALL = "SELECT id_gc, sender_name, sender_email, recipient_email, message, message2, date, id_gct, is_read, is_copy FROM greetings_card";
    private static final String SQL_QUERY_FIND_FROM_FILTER = "SELECT gc.recipient_email, gc.is_read FROM greetings_card as gc LEFT JOIN greetings_card_template as gct ON ( gc.id_gct = gct.id_gct ) ";
    private static final String SQL_QUERY_FILTER_WORKGROUP = " gct.workgroup_key = ? ";
    private static final String SQL_QUERY_FILTER_BY_ID = " gc.id_gct = ? ";
    private static final String SQL_QUERY_FILTER_COPY = " gc.is_copy = 0 ";
    private static final String SQL_QUERY_FILTER_COPY_SPEC = " is_copy = 0 ";
    private static final String CONSTANT_WHERE = " WHERE ";
    private static final String CONSTANT_AND = " AND ";
    private static final String EMPTY_STRING = "";
    private static final String ARROBASE = "@";

    /**
     * Creates a new GreetingsCardDAO object.
     */
    private GreetingsCardDAO(  )
    {
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table.
     *
     * @param greetingsCard The Instance of the GreetingsCard object
     * @param plugin The plugin
     */
    public void insert( GreetingsCard greetingsCard, Plugin plugin )
    {
        greetingsCard.setId( newPrimaryKey( plugin ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        daoUtil.setString( 1, greetingsCard.getId(  ) );
        daoUtil.setString( 2, greetingsCard.getSenderName(  ) );
        daoUtil.setString( 3, greetingsCard.getSenderEmail(  ) );
        daoUtil.setString( 4, greetingsCard.getRecipientEmail(  ) );
        daoUtil.setString( 5, greetingsCard.getMessage(  ) );
        daoUtil.setString( 6, greetingsCard.getMessage2(  ) );
        daoUtil.setDate( 7, greetingsCard.getDate(  ) );
        daoUtil.setString( 8, greetingsCard.getSenderIp(  ) );
        daoUtil.setInt( 9, greetingsCard.getIdGCT(  ) );
        daoUtil.setBoolean( 10, greetingsCard.isRead(  ) );
        daoUtil.setBoolean( 11, greetingsCard.isCopy(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     *
     * @param strIdGC The indentifier of the object GreetingsCard
     * @param plugin The plugin
     */
    public void delete( String strIdGC, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

        daoUtil.setString( 1, strIdGC );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of GreetingsCard from the table
     *
     * @param strIdGC The identifier of the object GreetingsCard
     * @param plugin The plugin
     * @return The Instance of the object GreetingsCard
     */
    public GreetingsCard load( String strIdGC, Plugin plugin )
    {
        GreetingsCard greetingsCard = new GreetingsCard(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );

        daoUtil.setString( 1, strIdGC );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

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
            greetingsCard.setRead( daoUtil.getBoolean( 9 ) );
            greetingsCard.setCopy( daoUtil.getBoolean( 10 ) );
        }

        daoUtil.free(  );

        return greetingsCard;
    }

    /**
     * Update the record in the table
     *
     * @param greetingsCard The instance of the GreetingsCard to update
     * @param plugin The plugin
     */
    public void store( GreetingsCard greetingsCard, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setString( 1, greetingsCard.getId(  ) );
        daoUtil.setString( 2, greetingsCard.getSenderName(  ) );
        daoUtil.setString( 3, greetingsCard.getSenderEmail(  ) );
        daoUtil.setString( 4, greetingsCard.getRecipientEmail(  ) );
        daoUtil.setString( 5, greetingsCard.getMessage(  ) );
        daoUtil.setString( 6, greetingsCard.getMessage2(  ) );
        daoUtil.setDate( 7, greetingsCard.getDate(  ) );
        daoUtil.setInt( 8, greetingsCard.getIdGCT(  ) );
        daoUtil.setString( 9, greetingsCard.getSenderIp(  ) );
        daoUtil.setBoolean( 10, greetingsCard.isRead(  ) );
        daoUtil.setBoolean( 11, greetingsCard.isCopy(  ) );
        daoUtil.setString( 12, greetingsCard.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
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
        Random generator = new Random(  );
        int kindOfValue;

        do
        {
            keyExists = 0;
            strKey = new StringBuffer(  );

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
            daoUtil.setString( 1, strKey.toString(  ) );

            daoUtil.executeQuery(  );

            if ( daoUtil.next(  ) )
            {
                keyExists = 1;
            }
        }
        while ( keyExists == 1 );

        daoUtil.free(  );

        return strKey.toString(  );
    }

    /**
     * Finds all objects of this type
     * @param plugin The plugin
     * @return A collection of objects
     */
    public Collection<GreetingsCard> findAll( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL + CONSTANT_WHERE + SQL_QUERY_FILTER_COPY_SPEC, plugin );

        daoUtil.executeQuery(  );

        ArrayList<GreetingsCard> list = new ArrayList<GreetingsCard>(  );

        while ( daoUtil.next(  ) )
        {
            GreetingsCard greetingsCard = new GreetingsCard(  );
            greetingsCard.setId( daoUtil.getString( 1 ) );
            greetingsCard.setSenderName( daoUtil.getString( 2 ) );
            greetingsCard.setSenderEmail( daoUtil.getString( 3 ) );
            greetingsCard.setRecipientEmail( daoUtil.getString( 4 ) );
            greetingsCard.setMessage( daoUtil.getString( 5 ) );
            greetingsCard.setMessage2( daoUtil.getString( 6 ) );
            greetingsCard.setDate( daoUtil.getDate( 7 ) );
            greetingsCard.setIdGCT( daoUtil.getInt( 8 ) );
            greetingsCard.setRead( daoUtil.getBoolean( 9 ) );
            greetingsCard.setCopy( daoUtil.getBoolean( 10 ) );
            list.add( greetingsCard );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Returns the list of greetings cards of a greetings card template
     * @param nIdGreetingsCardTemplate The greetings card template identifier
     * @param plugin The plugin
     * @return A Collection of greetings cards
     */
    public Collection<GreetingsCard> findByGreetingsCardTemplateId( int nIdGreetingsCardTemplate, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID, plugin );

        daoUtil.setInt( 1, nIdGreetingsCardTemplate );

        ArrayList<GreetingsCard> list = new ArrayList<GreetingsCard>(  );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            GreetingsCard greetingsCard = new GreetingsCard(  );
            greetingsCard.setId( daoUtil.getString( 1 ) );
            greetingsCard.setSenderName( daoUtil.getString( 2 ) );
            greetingsCard.setSenderEmail( daoUtil.getString( 3 ) );
            greetingsCard.setRecipientEmail( daoUtil.getString( 4 ) );
            greetingsCard.setMessage( daoUtil.getString( 5 ) );
            greetingsCard.setMessage2( daoUtil.getString( 6 ) );
            greetingsCard.setDate( daoUtil.getDate( 7 ) );
            greetingsCard.setIdGCT( daoUtil.getInt( 8 ) );
            greetingsCard.setRead( daoUtil.getBoolean( 9 ) );
            greetingsCard.setCopy( daoUtil.getBoolean( 10 ) );
            list.add( greetingsCard );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Returns the list of domain name of mail sent
     * @param greetingsCardFilter The greetings card template filter
     * @param plugin The plugin
     * @return A list of domain
     */
    public List<String> findDomainNameOfMailSent( GreetingsCardFilter greetingsCardFilter, Plugin plugin )
    {
        List<String> listStrFilter = new ArrayList<String>(  );

        if ( greetingsCardFilter.containsIdGCT(  ) )
        {
            listStrFilter.add( SQL_QUERY_FILTER_BY_ID );
        }

        if ( greetingsCardFilter.containsWorkgroupCriteria(  ) )
        {
            listStrFilter.add( SQL_QUERY_FILTER_WORKGROUP );
        }

        listStrFilter.add( SQL_QUERY_FILTER_COPY );

        String strQuery = buildRequetteWithFilter( SQL_QUERY_FIND_FROM_FILTER, listStrFilter );

        DAOUtil daoUtil = new DAOUtil( strQuery, plugin );
        int nIndex = 1;

        if ( greetingsCardFilter.containsIdGCT(  ) )
        {
            daoUtil.setInt( nIndex, greetingsCardFilter.getIdGCT(  ) );
            nIndex++;
        }

        if ( greetingsCardFilter.containsWorkgroupCriteria(  ) )
        {
            daoUtil.setString( nIndex, greetingsCardFilter.getWorkgroup(  ) );
            nIndex++;
        }

        List<String> listDomain = new ArrayList<String>(  );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            String strMail = daoUtil.getString( 1 );
            strMail = strMail.substring( strMail.indexOf( ARROBASE ) + 1, strMail.length(  ) );

            if ( !listDomain.contains( strMail ) )
            {
                listDomain.add( strMail );
            }
        }

        daoUtil.free(  );

        return listDomain;
    }

    /**
    * Return number of mail sent by domain
    * @param strDomain Name of domain
    * @param greetingsCardFilter The greetings card filter
    * @param plugin The plugin
    * @return number of mail sent by domain
    */
    public int findNumberOfMailSentByDomain( String strDomain, GreetingsCardFilter greetingsCardFilter, Plugin plugin )
    {
        List<String> listStrFilter = new ArrayList<String>(  );

        if ( greetingsCardFilter.containsIdGCT(  ) )
        {
            listStrFilter.add( SQL_QUERY_FILTER_BY_ID );
        }

        if ( greetingsCardFilter.containsWorkgroupCriteria(  ) )
        {
            listStrFilter.add( SQL_QUERY_FILTER_WORKGROUP );
        }

        listStrFilter.add( SQL_QUERY_FILTER_COPY );

        String strQuery = buildRequetteWithFilter( SQL_QUERY_FIND_FROM_FILTER, listStrFilter );

        DAOUtil daoUtil = new DAOUtil( strQuery, plugin );
        int nIndex = 1;

        if ( greetingsCardFilter.containsIdGCT(  ) )
        {
            daoUtil.setInt( nIndex, greetingsCardFilter.getIdGCT(  ) );
            nIndex++;
        }

        if ( greetingsCardFilter.containsWorkgroupCriteria(  ) )
        {
            daoUtil.setString( nIndex, greetingsCardFilter.getWorkgroup(  ) );
            nIndex++;
        }

        int nCountDomain = 0;

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            String strMail = daoUtil.getString( 1 );
            strMail = strMail.substring( strMail.indexOf( ARROBASE ) + 1, strMail.length(  ) );

            if ( strDomain.equals( strMail ) )
            {
                nCountDomain++;
            }
        }

        daoUtil.free(  );

        return nCountDomain;
    }

    /**
     * Return number of mail read by domain
     * @param strDomain Name of domain
     * @param greetingsCardFilter The greetings card filter
     * @param plugin The plugin
     * @return number of mail read by domain
     */
    public int findNumberOfMailReadByDomain( String strDomain, GreetingsCardFilter greetingsCardFilter, Plugin plugin )
    {
        List<String> listStrFilter = new ArrayList<String>(  );

        if ( greetingsCardFilter.containsIdGCT(  ) )
        {
            listStrFilter.add( SQL_QUERY_FILTER_BY_ID );
        }

        if ( greetingsCardFilter.containsWorkgroupCriteria(  ) )
        {
            listStrFilter.add( SQL_QUERY_FILTER_WORKGROUP );
        }

        listStrFilter.add( SQL_QUERY_FILTER_COPY );

        String strQuery = buildRequetteWithFilter( SQL_QUERY_FIND_FROM_FILTER, listStrFilter );

        DAOUtil daoUtil = new DAOUtil( strQuery, plugin );
        int nIndex = 1;

        if ( greetingsCardFilter.containsIdGCT(  ) )
        {
            daoUtil.setInt( nIndex, greetingsCardFilter.getIdGCT(  ) );
            nIndex++;
        }

        if ( greetingsCardFilter.containsWorkgroupCriteria(  ) )
        {
            daoUtil.setString( nIndex, greetingsCardFilter.getWorkgroup(  ) );
            nIndex++;
        }

        int nCountDomain = 0;

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            String strMail = daoUtil.getString( 1 );
            boolean isRead = daoUtil.getBoolean( 2 );
            strMail = strMail.substring( strMail.indexOf( ARROBASE ) + 1, strMail.length(  ) );

            if ( strDomain.equals( strMail ) && isRead )
            {
                nCountDomain++;
            }
        }

        daoUtil.free(  );

        return nCountDomain;
    }

    /**
    * Builds a query with filters placed in parameters
    * @param strSelect the select of the  query
    * @param listStrFilter the list of filter to add in the query
    * @return a query
    */
    public String buildRequetteWithFilter( String strSelect, List<String> listStrFilter )
    {
        StringBuffer strBuffer = new StringBuffer(  );
        strBuffer.append( strSelect );

        int nCount = 0;

        for ( String strFilter : listStrFilter )
        {
            if ( ++nCount == 1 )
            {
                strBuffer.append( CONSTANT_WHERE );
            }

            strBuffer.append( strFilter );

            if ( nCount != listStrFilter.size(  ) )
            {
                strBuffer.append( CONSTANT_AND );
            }
        }

        return strBuffer.toString(  );
    }
}
