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

/**
 * Class Domain
 */
public class Domain
{
	private static final String CONSTANT_PARENTHESIS_OPEN = "(";
	private static final String CONSTANT_PARENTHESIS_CLOSE = ")";
	private static final String CONSTANT_SPACE = " ";

	private String _strDomainName;
	private int _nMailSent;
	private int _nMailRead;
	private int _nTotalSent;

	/**
	 * Return the domain name
	 * @return _strDomainName the domain name
	 */
	public String getDomainName( )
	{
		return _strDomainName;
	}

	/**
	 * Set the domain name
	 * @param domainName The doamin name
	 */
	public void setDomainName( String domainName )
	{
		_strDomainName = domainName;
	}

	/**
	 * Return the number of mail sent
	 * @return _nMailSent The number of mail sent
	 */
	public String getMailSent( )
	{
		int nRatio = _nTotalSent == 0 ? 0 : _nMailSent / _nTotalSent;
		return _nMailSent + CONSTANT_SPACE + CONSTANT_PARENTHESIS_OPEN + nRatio + CONSTANT_PARENTHESIS_CLOSE;
	}

	/**
	 * Set the number of mail sent
	 * @param mailSent the number of mail sent
	 */
	public void setMailSent( int mailSent )
	{
		_nMailSent = mailSent;
	}

	/**
	 * Return the number of mail read
	 * @return _nMailRead the number of mail read
	 */
	public String getMailRead( )
	{
		int nRatio = _nMailSent == 0 ? 0 : _nMailRead / _nMailSent;
		return _nMailRead + CONSTANT_SPACE + CONSTANT_PARENTHESIS_OPEN + nRatio + CONSTANT_PARENTHESIS_CLOSE;
	}

	/**
	 * Set the number of mail read
	 * @param mailRead the number of mail read
	 */
	public void setMailRead( int mailRead )
	{
		_nMailRead = mailRead;
	}

	/**
	 * Get the total number of cards sent to every domain
	 * @return The total number of cards sent to every domain
	 */
	public int getTotalSent( )
	{
		return _nTotalSent;
	}

	/**
	 * Set the total number of cards sent to every domain
	 * @param nTotalSent The total number of cards sent to every domain
	 */
	public void setTotalSent( int nTotalSent )
	{
		_nTotalSent = nTotalSent;
	}
}
