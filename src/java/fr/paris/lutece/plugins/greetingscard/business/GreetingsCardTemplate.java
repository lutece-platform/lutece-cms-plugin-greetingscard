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

import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;


/**
 * This class represents a GreetingsCardTemplate object.
 */
public class GreetingsCardTemplate implements AdminWorkgroupResource
{
	private int _nIdGCT;
	private String _strDescription;
	private int _nHeight;
	private int _nWidth;
	private boolean _bIsEnabled;
	private String _strObjectEmail;
	// private Collection<GreetingsCard> _greetingsCards;
	private String _strWorkgroupKey;

	/**
	 * Creates a new GreetingsCardTemplate object.
	 */
	public GreetingsCardTemplate( )
	{
	}

	/**
	 * Returns the identifier of the greetings card template
	 * @return The identifier of the greetings card template
	 */
	public int getId( )
	{
		return _nIdGCT;
	}

	/**
	 * Sets the identifier of the greetings card template to the specified value
	 * @param nIdGCT The new value
	 */
	public void setId( int nIdGCT )
	{
		_nIdGCT = nIdGCT;
	}

	/**
	 * Returns the description string
	 * @return The description string
	 */
	public String getDescription( )
	{
		return _strDescription;
	}

	/**
	 * Sets the description string to the specified value
	 * @param strDescription The new value
	 */
	public void setDescription( String strDescription )
	{
		_strDescription = strDescription;
	}

	/**
	 * Returns the height int
	 * @return The height int
	 */
	public int getHeight( )
	{
		return _nHeight;
	}

	/**
	 * Sets the height int to the specified value
	 * @param nHeight The new value
	 */
	public void setHeight( int nHeight )
	{
		_nHeight = nHeight;
	}

	/**
	 * Returns the width int
	 * @return The width int
	 */
	public int getWidth( )
	{
		return _nWidth;
	}

	/**
	 * Sets the width int to the specified value
	 * @param nWidth The new value
	 */
	public void setWidth( int nWidth )
	{
		_nWidth = nWidth;
	}

	/**
	 * Returns the status of the object
	 * @return true if the object is enabled
	 */
	public boolean isEnabled( )
	{
		return _bIsEnabled;
	}

	/**
	 * Sets the status of the object
	 * @param nStatus 1 to enable the object, any other value to disable it
	 */
	public void setStatus( int nStatus )
	{
		if ( nStatus == 1 )
		{
			_bIsEnabled = true;
		}
		else
		{
			_bIsEnabled = false;
		}
	}

	/**
	 * Returns the object email string
	 * @return The object email string
	 */
	public String getObjectEmail( )
	{
		return _strObjectEmail;
	}

	/**
	 * Sets the object email string to the specified value
	 * @param strObjectEmail The new value
	 */
	public void setObjectEmail( String strObjectEmail )
	{
		_strObjectEmail = strObjectEmail;
	}

	/**
	 * @param strWorkgroupKey the WorkgroupKey to set
	 */
	public void setWorkgroupKey( String strWorkgroupKey )
	{
		_strWorkgroupKey = strWorkgroupKey;
	}

	/**
	 * @return the _strWorkgroupKey
	 */
	public String getWorkgroupKey( )
	{
		return _strWorkgroupKey;
	}

	/**
	 * Get Workgroup
	 * @return the workgroup of notice
	 */
	public String getWorkgroup( )
	{
		return _strWorkgroupKey;
	}
}
