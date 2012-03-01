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

import java.util.Collection;
import java.util.List;


/**
 *
 * Interface for GreetingsCardDAO
 *
 */
public interface IGreetingsCardDAO
{
    /**
    * Insert a new record in the table.
    *
    * @param greetingsCard The Instance of the GreetingsCard object
    * @param plugin The plugin
    */
    void insert( GreetingsCard greetingsCard, Plugin plugin );

    /**
     * Delete a record from the table
     *
     * @param strIdGC The indentifier of the object GreetingsCard
     * @param plugin The plugin
     */
    void delete( String strIdGC, Plugin plugin );

    /**
     * load the data of GreetingsCard from the table
     *
     * @param strIdGC The identifier of the object GreetingsCard
     * @param plugin The plugin
     * @return The Instance of the object GreetingsCard
     */
    GreetingsCard load( String strIdGC, Plugin plugin );

    /**
     * Update the record in the table
     *
     * @param greetingsCard The instance of the GreetingsCard to update
     * @param plugin The plugin
     */
    void store( GreetingsCard greetingsCard, Plugin plugin );

    /**
     * Finds all objects of this type
     * @param plugin The plugin
     * @return A collection of objects
     */
    Collection<GreetingsCard> findAll( Plugin plugin );

    /**
     * Returns the list of greetings cards of a greetings card template
     * @param nIdGreetingsCardTemplate The greetings card template identifier
     * @param plugin The plugin
     * @return A Collection of greetings cards
     */
    Collection<GreetingsCard> findByGreetingsCardTemplateId( int nIdGreetingsCardTemplate, Plugin plugin );

    /**
     * Returns the list of domain name of mail sent
     * @param greetingsCardFilter The greetings card filter
     * @param plugin The plugin
     * @return A list of domain
     */
    List<String> findDomainNameOfMailSent( GreetingsCardFilter greetingsCardFilter, Plugin plugin );

    /**
     * Return number of mail sent by domain
     * @param strDomain Name of domain
     * @param greetingsCardFilter The greetings card filter
     * @param plugin The plugin
     * @return number of mail sent by domain
     */
    int findNumberOfMailSentByDomain( String strDomain, GreetingsCardFilter greetingsCardFilter, Plugin plugin );

    /**
     * Return number of mail read by domain
     * @param strDomain Name of domain
     * @param greetingsCardFilter The greetings card filter
     * @param plugin The plugin
     * @return number of mail sent by domain
     */
    int findNumberOfMailReadByDomain( String strDomain, GreetingsCardFilter greetingsCardFilter, Plugin plugin );
}
