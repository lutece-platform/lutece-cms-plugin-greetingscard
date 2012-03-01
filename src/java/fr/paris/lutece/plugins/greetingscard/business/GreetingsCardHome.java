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
package fr.paris.lutece.plugins.greetingscard.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;
import java.util.List;


/**
 * This class provides instances management methods (create, find, ...)
 * for GreetingsCard objects
 */
public final class GreetingsCardHome
{
    // Static variable pointed at the DAO instance
    private static IGreetingsCardDAO _dao = (IGreetingsCardDAO) SpringContextService.getPluginBean( "greetingscard",
            "greetingscardDAO" );

    /**
     * Private constructor - this class need not be instantiated.
     */
    private GreetingsCardHome(  )
    {
    }

    /**
     *
     * @param greetingsCard An instance of the GreetingsCard which contains the informations to insert
     * @param plugin The plugin
     * @return The instance of the GreetingsCard which has been created.
     */
    public static GreetingsCard create( GreetingsCard greetingsCard, Plugin plugin )
    {
        _dao.insert( greetingsCard, plugin );

        return greetingsCard;
    }

    /**
     *
     * @param greetingsCard An instance of the GreetingsCard which contains the informations to store
     * @param plugin The plugin
     * @return The instance of the GreetingsCard which has been updated.
     */
    public static GreetingsCard update( GreetingsCard greetingsCard, Plugin plugin )
    {
        _dao.store( greetingsCard, plugin );

        return greetingsCard;
    }

    /**
     *
     * @param strIdGC The Id of the deleted greeting card
     * @param plugin The plugin
     */
    public static void remove( String strIdGC, Plugin plugin )
    {
        _dao.delete( strIdGC, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of the article GreetingsCard whose identifier is specified in parameter
     *
     * @param strKey The primary key of the article to find in the database
     * @param plugin The plugin
     * @return An instance of the GreetingsCard which corresponds to the key
     */
    public static GreetingsCard findByPrimaryKey( String strKey, Plugin plugin )
    {
        return _dao.load( strKey, plugin );
    }

    /**
     * Returns GreetingsCard list
     *
     * @param plugin The plugin
     * @return the list of the GreetingsCard of the database in form of a GreetingsCard Collection object
     */
    public static Collection<GreetingsCard> findAll( Plugin plugin )
    {
        return _dao.findAll( plugin );
    }

    /**
     * Returns the list of greetings cards of a greetings card template
     * @param nIdGreetingsCardTemplate The greetings card template identifier
     * @param plugin The plugin
     * @return A Collection of greetings cards
     */
    public static Collection<GreetingsCard> findByGreetingsCardTemplateId( int nIdGreetingsCardTemplate, Plugin plugin )
    {
        return _dao.findByGreetingsCardTemplateId( nIdGreetingsCardTemplate, plugin );
    }

    /**
     * Returns the list of domain name of mail sent
     * @param greetingsCardFilter The greetings card filter
     * @param plugin The plugin
     * @return A Collection of greetings cards
     */
    public static List<String> findDomainNameOfMailSent( GreetingsCardFilter greetingsCardFilter, Plugin plugin )
    {
        return _dao.findDomainNameOfMailSent( greetingsCardFilter, plugin );
    }

    /**
     * Return number of mail sent by domain
     * @param strDomain Name of domain
     * @param greetingsCardFilter The greetings card filter
     * @param plugin The plugin
     * @return number of mail sent by domain
     */
    public static int findNumberOfMailSentByDomain( String strDomain, GreetingsCardFilter greetingsCardFilter,
        Plugin plugin )
    {
        return _dao.findNumberOfMailSentByDomain( strDomain, greetingsCardFilter, plugin );
    }

    /**
     * Return number of mail read by domain
     * @param strDomain Name of domain
     * @param greetingsCardFilter The greetings card filter
     * @param plugin The plugin
     * @return number of mail sent by domain
     */
    public static int findNumberOfMailReadByDomain( String strDomain, GreetingsCardFilter greetingsCardFilter,
        Plugin plugin )
    {
        return _dao.findNumberOfMailReadByDomain( strDomain, greetingsCardFilter, plugin );
    }
}
