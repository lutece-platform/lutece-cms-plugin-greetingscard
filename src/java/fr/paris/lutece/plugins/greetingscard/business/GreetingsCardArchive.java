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

import fr.paris.lutece.util.xml.XmlUtil;


/**
 * Archive for greetings card
 */
public class GreetingsCardArchive
{
	private static final String TAG_GREETINGS_CARD_ARCHIVE = "greetingscard-archive";
	private static final String TAG_DOMAIN_NAME = "domain-name";
	private static final String TAG_GREETTINGSCARD_TEMPLATE_ID = "greetingscard-template-id";
	private static final String TAG_NB_CARDS_SENT = "number-cards-sent";
	private static final String TAG_NB_CARDS_RED = "number-cards-red";
	private static final String TAG_YEAR = "year";

	private int _nIdArchive;
	private String _strDomainName;
	private int _nIdGCT;
	private int _nNbCard;
	private int _nNbCardRed;
	private int _nYearArchive;

	/**
	 * Get the id of the archive
	 * @return The id of the archive
	 */
	public int getIdArchive( )
	{
		return _nIdArchive;
	}

	/**
	 * Set the id of the archive
	 * @param nIdArchive The id of the archive
	 */
	public void setIdArchive( int nIdArchive )
	{
		_nIdArchive = nIdArchive;
	}

	/**
	 * Get the domain name
	 * @return the domain name
	 */
	public String getDomainName( )
	{
		return _strDomainName;
	}

	/**
	 * Set the domain name
	 * @param strDomainName The domain name
	 */
	public void setDomainName( String strDomainName )
	{
		_strDomainName = strDomainName;
	}

	/**
	 * Get the greetings card template id
	 * @return The greetings card template id
	 */
	public int getIdGCT( )
	{
		return _nIdGCT;
	}

	/**
	 * Set the greetings card template id
	 * @param nIdGCT The greetings card template id
	 */
	public void setIdGCT( int nIdGCT )
	{
		_nIdGCT = nIdGCT;
	}

	/**
	 * Get the number of card send to this domain name
	 * @return The number of card send to this domain name
	 */
	public int getNbCard( )
	{
		return _nNbCard;
	}

	/**
	 * Set the number of card send to this domain name
	 * @param nNbCard The number of card send to this domain name
	 */
	public void setNbCard( int nNbCard )
	{
		_nNbCard = nNbCard;
	}

	/**
	 * Get the number of card red
	 * @return The number of card red
	 */
	public int getNbCardRed( )
	{
		return _nNbCardRed;
	}

	/**
	 * Set the number of card red
	 * @param nNbCardRed The number of card red
	 */
	public void setNbCardRed( int nNbCardRed )
	{
		_nNbCardRed = nNbCardRed;
	}

	/**
	 * Get the year of the archive
	 * @return The year of the archive
	 */
	public int getYearArchive( )
	{
		return _nYearArchive;
	}

	/**
	 * Set the year of the archive
	 * @param nYearArchive The year of the archive
	 */
	public void setYearArchive( int nYearArchive )
	{
		_nYearArchive = nYearArchive;
	}

	public StringBuffer getXmlWithoutHeader( )
	{
		StringBuffer strXml = new StringBuffer( );
		XmlUtil.beginElement( strXml, TAG_GREETINGS_CARD_ARCHIVE );
		XmlUtil.addElement( strXml, TAG_DOMAIN_NAME, getDomainName( ) );
		XmlUtil.addElement( strXml, TAG_GREETTINGSCARD_TEMPLATE_ID, getIdGCT( ) );
		XmlUtil.addElement( strXml, TAG_NB_CARDS_SENT, getNbCard( ) );
		XmlUtil.addElement( strXml, TAG_NB_CARDS_RED, getNbCardRed( ) );
		XmlUtil.addElement( strXml, TAG_YEAR, getYearArchive( ) );
		XmlUtil.endElement( strXml, TAG_GREETINGS_CARD_ARCHIVE );

		return strXml;
	}

}
