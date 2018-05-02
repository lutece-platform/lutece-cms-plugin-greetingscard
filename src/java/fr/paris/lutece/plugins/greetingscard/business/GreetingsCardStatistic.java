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

/**
 * DTO describing statistics values of a greetings card template.
 */
public class GreetingsCardStatistic
{
	private int _nIdGCT;
	private String _strDescription;
	private int _nNbCardSentInternal;
	private int _nNbCardSentExternal;
	private int _nNbCardRedInternal;
	private int _nNbCardRedExternal;

	/**
	 * Get the id of the greetings card template
	 * @return nIdGCT the id of the greetings card template
	 */
	public int getIdGCT( )
	{
		return _nIdGCT;
	}

	/**
	 * Set the id of the greetings card template
	 * @param nIdGCT the id of the greetings card template
	 */
	public void setIdGCT( int nIdGCT )
	{
		_nIdGCT = nIdGCT;
	}

	/**
	 * Get the description of the greetings card template
	 * @return The description of the greetings card template
	 */
	public String getDescription( )
	{
		return _strDescription;
	}

	/**
	 * Set the description of the greetings card template
	 * @param strDescription The description of the greetings card template
	 */
	public void setDescription( String strDescription )
	{
		_strDescription = strDescription;
	}

	/**
	 * Get the number of cards sent to internal people
	 * @return The number of cards sent to internal people
	 */
	public int getNbCardSentInternal( )
	{
		return _nNbCardSentInternal;
	}

	/**
	 * Set the number of cards sent to internal people
	 * @param nNbCardSentInternal The number of cards sent to internal people
	 */
	public void setNbCardSentInternal( int nNbCardSentInternal )
	{
		_nNbCardSentInternal = nNbCardSentInternal;
	}

	/**
	 * Get the number of cards sent to external people
	 * @return The number of cards sent to external people
	 */
	public int getNbCardSentExternal( )
	{
		return _nNbCardSentExternal;
	}

	/**
	 * Set the number of cards sent to external people
	 * @param nNbCardSentExternal The number of cards sent to external people
	 */
	public void setNbCardSentExternal( int nNbCardSentExternal )
	{
		_nNbCardSentExternal = nNbCardSentExternal;
	}

	/**
	 * Get the number of cards red by internal people
	 * @return The number of cards red by internal people
	 */
	public int getNbCardRedInternal( )
	{
		return _nNbCardRedInternal;
	}

	/**
	 * Set the number of cards red by internal people
	 * @param nNbCardRedInternal The number of cards red by internal people
	 */
	public void setNbCardRedInternal( int nNbCardRedInternal )
	{
		_nNbCardRedInternal = nNbCardRedInternal;
	}

	/**
	 * Get the number of cards red by external people
	 * @return The number of cards red by external people
	 */
	public int getNbCardRedExternal( )
	{
		return _nNbCardRedExternal;
	}

	/**
	 * Set the number of cards red by external people
	 * @param nNbCardRedInternal The number of cards red by external people
	 */
	public void setNbCardRedExternal( int nNbCardRedExternal )
	{
		_nNbCardRedExternal = nNbCardRedExternal;
	}

	public int getNbCardSent( )
	{
		return _nNbCardSentExternal + _nNbCardSentInternal;
	}

	public int getNbCardRed( )
	{
		return _nNbCardRedExternal + _nNbCardRedInternal;
	}

}
